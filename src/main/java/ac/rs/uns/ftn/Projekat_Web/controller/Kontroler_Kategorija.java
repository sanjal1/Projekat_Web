package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.KategorijaAdminDTO;
import ac.rs.uns.ftn.Projekat_Web.dto.KategorijaKorisnikDTO;
import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.service.KategorijaServis;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/kategorije")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"}, allowCredentials = "true")

public class Kontroler_Kategorija {

    @Autowired
    private KategorijaServis kategorijaServis;

    //sve kategorije
    @GetMapping
    public ResponseEntity<List<KategorijaKorisnikDTO>> sveKategorije() {
        List<KategorijaKorisnikDTO> dtos = new ArrayList<>();
        for (Kategorija k : kategorijaServis.findAll())
            dtos.add(new KategorijaKorisnikDTO(k));
        return ResponseEntity.ok(dtos);
    }

    //kategorija po id-u
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Kategorija k = kategorijaServis.findOne(id);
        if (k == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new KategorijaKorisnikDTO(k));
    }

    //admin - dodavanje
    @PostMapping
    public ResponseEntity<?> dodaj(@RequestBody Kategorija kategorija, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new KategorijaAdminDTO(kategorijaServis.save(kategorija)));
    }

    //admin - izmena
    @PutMapping("/{id}")
    public ResponseEntity<?> izmeni(@PathVariable Long id, @RequestBody Kategorija kategorija, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Kategorija postojeca = kategorijaServis.findOne(id);
        if (postojeca == null)
            return ResponseEntity.notFound().build();

        postojeca.setNaziv(kategorija.getNaziv());
        postojeca.setOpis(kategorija.getOpis());
        return ResponseEntity.ok(new KategorijaAdminDTO(kategorijaServis.save(postojeca)));
    }

    //admin - brisanje
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisi(@PathVariable Long id, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Kategorija postojeca = kategorijaServis.findOne(id);
        if (postojeca == null)
            return ResponseEntity.notFound().build();

        kategorijaServis.delete(id);
        return ResponseEntity.ok("Kategorija uspešno obrisana!");
    }
}