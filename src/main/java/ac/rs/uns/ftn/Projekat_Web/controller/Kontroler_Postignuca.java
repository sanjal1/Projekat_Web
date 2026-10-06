package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.PostignucaAdminDTO;
import ac.rs.uns.ftn.Projekat_Web.dto.PostignucaKorisnikDTO;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Postignuca;
import ac.rs.uns.ftn.Projekat_Web.service.PostignucaServis;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/postignuca")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"}, allowCredentials = "true")

public class Kontroler_Postignuca {

    @Autowired
    private PostignucaServis postignucaServis;

    //sva postignuca - samo admin moze videti
    @GetMapping
    public ResponseEntity<?> svaPostignuca(HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        List<PostignucaAdminDTO> dtos = new ArrayList<>();
        for (Postignuca p : postignucaServis.findAll())
            dtos.add(new PostignucaAdminDTO(p));
        return ResponseEntity.ok(dtos);
    }

    //postignuca za ulogovanog korisnika
    @GetMapping("/korisnik/{korisnikId}")
    public ResponseEntity<?> postignucaZaKorisnika(@PathVariable Long korisnikId, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        List<PostignucaKorisnikDTO> dtos = new ArrayList<>();
        for (Postignuca p : postignucaServis.findByKorisnik(korisnikId))
            dtos.add(new PostignucaKorisnikDTO(p));
        return ResponseEntity.ok(dtos);
    }

    //admin - dodavanje postignuca
    @PostMapping
    public ResponseEntity<?> dodaj(@RequestBody Postignuca novo, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PostignucaAdminDTO(postignucaServis.save(novo)));
    }

    //admin - brisanje postignuca
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisi(@PathVariable Long id, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Postignuca postojece = postignucaServis.findOne(id);
        if (postojece == null)
            return ResponseEntity.notFound().build();

        postignucaServis.delete(id);
        return ResponseEntity.ok("Postignuće uspešno obrisano!");
    }
}