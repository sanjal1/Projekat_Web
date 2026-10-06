package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.*;
import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Statistika;
import ac.rs.uns.ftn.Projekat_Web.service.IgraServis;
import ac.rs.uns.ftn.Projekat_Web.service.IgraServis;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Korisnik;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Statistika;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistika")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"}, allowCredentials = "true")
public class Kontroler_Statistika {

    private final Servis_Statistika servisStatistika;
    private final Servis_Korisnik servisKorisnik;
    private final IgraServis servisIgra;

    public Kontroler_Statistika(Servis_Statistika servisStatistika,
                                Servis_Korisnik servisKorisnik,
                                IgraServis servisIgra) {
        this.servisStatistika = servisStatistika;
        this.servisKorisnik = servisKorisnik;
        this.servisIgra = servisIgra;
    }

    // POČETAK IGRANJA

    @PostMapping("/zapocni")
    public ResponseEntity<?> zapocniIgranje(
            @RequestBody StatistikaStartGameDTO dto, HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        try {
            // Frontend šalje id-eve, mi pronalazimo objekte
            Korisnik korisnik = servisKorisnik.getById(dto.getKorisnikId());
            Igra igra = servisIgra.findOne(dto.getIgraId());

            Statistika statistika = servisStatistika.zapocniIgranje(korisnik, igra);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new StatistikaResponseDTO(statistika));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ZAVRŠETAK IGRANJA

    @PutMapping("/zavrsi/{korisnikId}")
    public ResponseEntity<?> zavrsiIgranje(
            @PathVariable Long korisnikId, HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        if (!ulogovani.getId().equals(korisnikId))
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        try {
            Korisnik korisnik = servisKorisnik.getById(korisnikId);
            Statistika statistika = servisStatistika.zavrsiIgranje(korisnik);

            return ResponseEntity.ok(new StatistikaResponseDTO(statistika));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // LIČNA STATISTIKA KORISNIKA

    @GetMapping("/korisnik/{korisnikId}")
    public ResponseEntity<?> getLicnaStatistika(
            @PathVariable Long korisnikId, HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        // Korisnik vidi samo svoju statistiku, admin može videti svačiju
        if (!ulogovani.getId().equals(korisnikId) &&
                ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        try {
            Korisnik korisnik = servisKorisnik.getById(korisnikId);

            // Ukupno vreme igranja
            long ukupnoVreme = servisStatistika.getUkupnoVremeIgranja(korisnik);

            // Vreme i broj pokretanja po svakoj igrici → List<StatistikaNajigranijaIgraDTO>
            Map<Igra, Long> vremePoIgrici = servisStatistika.getVremePoIgrici(korisnik);
            List<StatistikaNajigranijaIgraDTO> statistikaPoIgrama = new ArrayList<>();

            for (Map.Entry<Igra, Long> entry : vremePoIgrici.entrySet()) {
                Igra igra = entry.getKey();
                long vreme = entry.getValue();
                long brojPokretanja = servisStatistika.getBrojPokretanja(korisnik, igra);

                statistikaPoIgrama.add(new StatistikaNajigranijaIgraDTO(
                        igra.getId(),
                        igra.getNaziv(),
                        vreme,
                        brojPokretanja
                ));
            }

            // Vreme po kategorijama → List<StatistikaPoKategorijiDTO>
            Map<String, Long> vremePoKategorijama = servisStatistika
                    .getVremePoKategorijama(korisnik);
            List<StatistikaPoKategorijiDTO> statistikaPoKategorijama = new ArrayList<>();

            for (Map.Entry<String, Long> entry : vremePoKategorijama.entrySet()) {
                statistikaPoKategorijama.add(new StatistikaPoKategorijiDTO(
                        entry.getKey(),
                        entry.getValue()
                ));
            }

            // Sve spakovano u jedan DTO
            StatistikaZaKorisnikDTO rezultat = new StatistikaZaKorisnikDTO(
                    ukupnoVreme,
                    statistikaPoIgrama,
                    statistikaPoKategorijama
            );

            return ResponseEntity.ok(rezultat);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // Sve sesije jednog korisnika
    @GetMapping("/korisnik/{korisnikId}/sesije")
    public ResponseEntity<?> getSesijeKorisnika(
            @PathVariable Long korisnikId, HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        if (!ulogovani.getId().equals(korisnikId) &&
                ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        try {
            Korisnik korisnik = servisKorisnik.getById(korisnikId);

            List<StatistikaResponseDTO> sesije = servisStatistika
                    .getSesijeKorisnika(korisnik)
                    .stream()
                    .map(StatistikaResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(sesije);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // ADMIN MONITORING

    @GetMapping("/admin/sve-sesije")
    public ResponseEntity<?> getSveSesije(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(servisStatistika
                .getSveSesije()
                .stream()
                .map(StatistikaResponseDTO::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/admin/zavrsene-sesije")
    public ResponseEntity<?> getSveZavrseneSesije(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.ok(servisStatistika
                .getSveZavrseneSesije()
                .stream()
                .map(StatistikaResponseDTO::new)
                .collect(Collectors.toList()));
    }

    // Sesije za jednu igricu
    @GetMapping("/admin/igra/{igraId}")
    public ResponseEntity<?> getSesijeIgre(@PathVariable Long igraId, HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        try {
            Igra igra = servisIgra.findOne(igraId);

            List<StatistikaResponseDTO> sesije = servisStatistika
                    .getSesijeIgre(igra)
                    .stream()
                    .map(StatistikaResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(sesije);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/admin/najigranije")
    public ResponseEntity<?> getNajigranijeIgrice(HttpSession session) {

        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);


        List<Object[]> rezultati = servisStatistika.getNajigranijeIgrice();
        List<StatistikaNajigranijaIgraDTO> lista = new ArrayList<>();

        for (Object[] red : rezultati) {
            Igra igra = (Igra) red[0];
            long brojPokretanja = (long) red[1];

            List<Statistika> sesije = servisStatistika.getSesijeIgre(igra);

            long ukupnoVreme = 0;

            for (Statistika s : sesije) {
                if (s.getVremeZavrsetka() != null) {
                    ukupnoVreme += s.trajanjeSekundi();
                }
            }

            lista.add(new StatistikaNajigranijaIgraDTO(
                    igra.getId(),
                    igra.getNaziv(),
                    ukupnoVreme,
                    brojPokretanja
            ));
        }

        return ResponseEntity.ok(lista);
    }

    // ADMIN DASHBOARD

    @GetMapping("/dashboard/najigranije-30-dana")
    public ResponseEntity<?> getNajigranijeU30Dana(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        List<Object[]> rezultati = servisStatistika.getNajigranijeIgriceU30Dana();
        List<StatistikaNajigranijaIgraDTO> lista = new ArrayList<>();

        LocalDateTime pre30Dana = LocalDateTime.now().minusDays(30);

        for (Object[] red : rezultati) {
            Igra igra = (Igra) red[0];
            long brojPokretanja = (long) red[1];

            List<Statistika> sesije = servisStatistika.getSesijeIgre(igra);

            long ukupnoVreme = 0;

            for (Statistika s : sesije) {
                if (s.getVremeZavrsetka() != null &&
                        s.getVremeZavrsetka().isAfter(pre30Dana)) {

                    ukupnoVreme += s.trajanjeSekundi();
                }
            }

            lista.add(new StatistikaNajigranijaIgraDTO(
                    igra.getId(),
                    igra.getNaziv(),
                    ukupnoVreme,
                    brojPokretanja
            ));
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/dashboard/najaktivniji-korisnici")
    public ResponseEntity<?> getNajaktivnijiKorisnici(HttpSession session) {
        Korisnik ulogovani = (Korisnik) session.getAttribute("korisnik");
        if (ulogovani == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (ulogovani.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Map<Korisnik, Long> rezultati = servisStatistika.getNajaktivnijiKorisnici();
        List<StatistikaZaAdminDTO> lista = new ArrayList<>();

        for (Map.Entry<Korisnik, Long> entry : rezultati.entrySet()) {
            Korisnik korisnik = entry.getKey();
            long ukupnoVreme = entry.getValue();

            long brojPokretanja = servisStatistika
                    .getSesijeKorisnika(korisnik)
                    .size();


            StatistikaZaAdminDTO dto = new StatistikaZaAdminDTO();
            dto.setKorisnikId(korisnik.getId());
            dto.setImeKorisnika(korisnik.getIme());
            dto.setPrezimeKorisnika(korisnik.getPrezime());
            dto.setUkupnoVremeIgranja(ukupnoVreme);
            dto.setBrojPokretanja(brojPokretanja);


            lista.add(dto);
        }

        return ResponseEntity.ok(lista);
    }
}
