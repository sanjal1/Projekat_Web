package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.*;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Korisnik;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/korisnici")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"}, allowCredentials = "true")

public class Kontroler_Korisnik {

    private final Servis_Korisnik servisKorisnik;

    public Kontroler_Korisnik(Servis_Korisnik servisKorisnik) {
        this.servisKorisnik = servisKorisnik;
    }

    // NEPRIJAVLJENI KORISNIK

    @GetMapping("/broj")
    public long brojKorisnika() {
        return servisKorisnik.getBrojKorisnika();
    }

    //REGISTRACIJA

    @PostMapping("/registracija")
    public ResponseEntity<?> registracija(
            @RequestBody KorisnikRegisterDTO dto) {
        try {
            Korisnik korisnik = new Korisnik();
            korisnik.setIme(dto.getIme());
            korisnik.setPrezime(dto.getPrezime());
            korisnik.setEmail(dto.getEmail());
            korisnik.setLozinka(dto.getLozinka());
            korisnik.setDatumRodjenja(dto.getDatumRodjenja());

            Korisnik sacuvan = servisKorisnik.registrujKorisnika(korisnik);

            return ResponseEntity
                    .status(HttpStatus.CREATED)     // 201 - uspešno kreiran
                    .body(new KorisnikResponseDTO(sacuvan));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // 400 - email već postoji
                    .body(e.getMessage());
        }
    }

    // LOGIN

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody KorisnikLoginDTO dto, HttpSession session) {
        try {
            Korisnik korisnik = servisKorisnik.prijava(
                    dto.getEmail(),
                    dto.getLozinka()
            );

            // ovde se kreira JSESSIONID i pamti korisnik u sesiji
            session.setAttribute("korisnik", korisnik);
            return ResponseEntity.ok(new KorisnikResponseDTO(korisnik));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401 - pogrešni kredencijali
                    .body(e.getMessage());
        }
    }

    @GetMapping("/ja")
    public ResponseEntity<?> trenutniKorisnik(HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niste ulogovani.");
        return ResponseEntity.ok(new KorisnikResponseDTO(korisnik));
    }

    // PODEŠAVANJE PROFILA

    @PutMapping("/{id}")
    public ResponseEntity<?> izmeniProfil(
            @PathVariable Long id,
            @RequestBody KorisnikUpdateProfileDTO dto,
            HttpSession session) {
        try {
            Korisnik korisnik = servisKorisnik.izmeniProfil(
                    id,
                    dto.getIme(),
                    dto.getPrezime(),
                    dto.getEmail(),
                    dto.getDatumRodjenja(),
                    dto.getProfilnaSlika()
            );

            session.setAttribute("korisnik", korisnik);

            return ResponseEntity.ok(new KorisnikResponseDTO(korisnik));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/profilna-slika")
    public ResponseEntity<?> promeniProfilnuSliku(
            @PathVariable Long id,
            @RequestBody String profilnaSlika,
            HttpSession session) {
        try {

            String ociscenaSlika = profilnaSlika
                    .replace("\"", "")
                    .trim();


            Korisnik korisnik = servisKorisnik.promeniProfilnuSliku(
                    id,
                    profilnaSlika
            );

            session.setAttribute("korisnik", korisnik);

            return ResponseEntity.ok(new KorisnikResponseDTO(korisnik));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/lozinka")
    public ResponseEntity<?> promeniLozinku(
            @PathVariable Long id,
            @RequestBody KorisnikChangePasswordDTO dto,
            HttpSession session) {
        try {
            servisKorisnik.promeniLozinku(
                    id,
                    dto.getStaraLozinka(),
                    dto.getNovaLozinka()
            );

            Korisnik azuriran = servisKorisnik.getById(id);
            session.setAttribute("korisnik", azuriran);

            return ResponseEntity.ok("Lozinka uspešno promenjena.");

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ADMINISTRATOR

    @GetMapping
    public ResponseEntity<?> sviKorisnici(HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(servisKorisnik
                .getSviKorisnici()
                .stream()
                .map(KorisnikResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> korisnikPoId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    new KorisnikResponseDTO(servisKorisnik.getById(id))
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)   // 404 - korisnik ne postoji
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/blokiraj")
    public ResponseEntity<?> blokiraj(@PathVariable Long id,HttpSession session) {

        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);
        try {
            return ResponseEntity.ok(
                    new KorisnikResponseDTO(servisKorisnik.blokiraj(id))
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/odblokiraj")
    public ResponseEntity<?> odblokiraj(@PathVariable Long id, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);
        try {
            return ResponseEntity.ok(
                    new KorisnikResponseDTO(servisKorisnik.odblokiraj(id))
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/pretraga")
    public List<KorisnikResponseDTO> pretraga(@RequestParam String tekst) {
        return servisKorisnik
                .pretraga(tekst)
                .stream()
                .map(KorisnikResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/blokirani")
    public ResponseEntity<?> blokirani(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(servisKorisnik
                .getBlokiraniKorisnici()
                .stream()
                .map(KorisnikResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/aktivni")
    public ResponseEntity<?> aktivni(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(servisKorisnik
                .getAktivniKorisnici()
                .stream()
                .map(KorisnikResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/broj-aktivnih")
    public ResponseEntity<?> brojAktivnih(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(servisKorisnik.getBrojAktivnihKorisnika());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // briše sve podatke iz sesije
        return ResponseEntity.ok("Uspešno ste se odjavili.");
    }
}
