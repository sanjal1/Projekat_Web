package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.RecenzijaDTO;
import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Recenzija;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Igra;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Recenzija;
import ac.rs.uns.ftn.Projekat_Web.service.IgraServis;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Recenzija;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recenzije")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"}, allowCredentials = "true")

public class Kontroler_Recenzija {

    @Autowired
    private Servis_Recenzija recenzijaServis;

    @Autowired
    private IgraServis igraServis;

    //sve recenzije
    @GetMapping("/igra/{id}")
    public ResponseEntity<List<RecenzijaDTO>> recenzijeZaIgru(@PathVariable Long id) {
        List<RecenzijaDTO> dtos = new ArrayList<>();
        for (Recenzija r : recenzijaServis.getRecenzijeZaIgru(id))
            dtos.add(new RecenzijaDTO(r));
        return ResponseEntity.ok(dtos);
    }

    //dodavanje recenzije za igru
    @PostMapping("/igra/{id}")
    public ResponseEntity<?> dodajRecenziju(@PathVariable Long id, @RequestBody Recenzija nova, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Morate biti ulogovani!", HttpStatus.UNAUTHORIZED);

        Igra igra = igraServis.findOne(id);
        if (igra == null)
            return ResponseEntity.notFound().build();

        nova.setIgra(igra);
        nova.setKorisnik(korisnik);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RecenzijaDTO(recenzijaServis.dodajRecenziju(nova)));
    }

    //admin - brisanje recenzije
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisi(@PathVariable Long id, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Recenzija recenzija = recenzijaServis.getById(id);
        if (recenzija == null)
            return ResponseEntity.notFound().build();

        recenzijaServis.obrisi(id);
        return ResponseEntity.ok("Uspešno obrisana recenzija!");
    }
}
