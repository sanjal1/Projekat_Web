package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.IgraAdminDTO;
import ac.rs.uns.ftn.Projekat_Web.dto.IgraDetaljnoDTO;
import ac.rs.uns.ftn.Projekat_Web.dto.IgraOsnovnoDTO;
import ac.rs.uns.ftn.Projekat_Web.dto.RecenzijaDTO;
import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Recenzija;
import ac.rs.uns.ftn.Projekat_Web.service.IgraServis;
import ac.rs.uns.ftn.Projekat_Web.service.KategorijaServis;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Recenzija;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Recenzija;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/igre")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"}, allowCredentials = "true")

public class Kontroler_Igra {

    @Autowired
    private IgraServis igraServis;

    @Autowired
    private Servis_Recenzija recenzijaServis;

    @Autowired
    private KategorijaServis kategorijaServis;

    //broj aktivnih igrica (pocetna stranica)
    @GetMapping("/broj")
    public ResponseEntity<Long> getBrojIgara() {
        return ResponseEntity.ok(igraServis.getBrojAktivnih());
    }

    //sve aktivne igre
    @GetMapping
    public ResponseEntity<List<IgraOsnovnoDTO>> sveIgre() {
        List<IgraOsnovnoDTO> dtos = new ArrayList<>();
        for (Igra igra : igraServis.findActive()) {
            IgraOsnovnoDTO dto = new IgraOsnovnoDTO(igra);
            Double prosek = recenzijaServis.getProsecnaOcena(igra.getId());
            dto.setProsecnaOcena(prosek != null ? prosek : 0.0);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    //detalji igre + recenzije
    @GetMapping("/{id}")
    public ResponseEntity<?> detalji(@PathVariable Long id) {
        Igra igra = igraServis.findOne(id);
        if (igra == null)
            return ResponseEntity.notFound().build();

        IgraDetaljnoDTO dto = new IgraDetaljnoDTO(igra);
        Double prosek = recenzijaServis.getProsecnaOcena(id);
        dto.setProsecnaOcena(prosek != null ? prosek : 0.0);

        List<RecenzijaDTO> recenzije = new ArrayList<>();
        for (Recenzija r : recenzijaServis.getRecenzijeZaIgru(id))
            recenzije.add(new RecenzijaDTO(r));

        Map<String, Object> rezultat = new HashMap<>();
        rezultat.put("igra", dto);
        rezultat.put("recenzije", recenzije);
        return ResponseEntity.ok(rezultat);
    }

    //pretraga po nazivu
    @GetMapping("/pretraga")
    public ResponseEntity<?> pretraga(@RequestParam String naziv, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        List<IgraOsnovnoDTO> dtos = new ArrayList<>();
        for (Igra igra : igraServis.pretrazi(naziv)) {
            IgraOsnovnoDTO dto = new IgraOsnovnoDTO(igra);
            Double prosek = recenzijaServis.getProsecnaOcena(igra.getId());
            dto.setProsecnaOcena(prosek != null ? prosek : 0.0);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    //filtriranje po kategoriji
    @GetMapping("/kategorija/{kategorijaId}")
    public ResponseEntity<?> poKategoriji(@PathVariable Long kategorijaId, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        List<IgraOsnovnoDTO> dtos = new ArrayList<>();
        for (Igra igra : igraServis.poKategoriji(kategorijaId)) {
            IgraOsnovnoDTO dto = new IgraOsnovnoDTO(igra);
            Double prosek = recenzijaServis.getProsecnaOcena(igra.getId());
            dto.setProsecnaOcena(prosek != null ? prosek : 0.0);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    //sortiranje po prosecnoj oceni
    @GetMapping("/sortirane")
    public ResponseEntity<?> sortiranePoOceni(HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);

        List<IgraOsnovnoDTO> dtos = new ArrayList<>();
        for (Igra igra : igraServis.sortiranePoOceni()) {
            IgraOsnovnoDTO dto = new IgraOsnovnoDTO(igra);
            Double prosek = recenzijaServis.getProsecnaOcena(igra.getId());
            dto.setProsecnaOcena(prosek != null ? prosek : 0.0);
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    // admin - sve igre
    @GetMapping("/admin")
    public ResponseEntity<?> sveIgreAdmin(HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        List<IgraAdminDTO> dtos = new ArrayList<>();
        for (Igra igra : igraServis.findAll()) {
            IgraAdminDTO dto = new IgraAdminDTO(igra);
            Double prosek = recenzijaServis.getProsecnaOcena(igra.getId());
            dto.setProsecnaOcena(prosek != null ? prosek : 0.0);
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    //admin - dodavanje igrice
    @PostMapping
    public ResponseEntity<?> dodaj(@RequestBody IgraAdminDTO dto, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Kategorija kategorija = kategorijaServis.findByNazivIgnoreCase(dto.getKategorija());

        if (kategorija == null)
            return ResponseEntity.badRequest().body("Kategorija ne postoji!");

        Igra igra = new Igra();
        igra.setNaziv(dto.getNaziv());
        igra.setOpis(dto.getOpis());
        igra.setKategorija(kategorija);
        igra.setAktivna(dto.isAktivna());
        igra.setURL(dto.getUrl());
        igra.setSlika(dto.getSlika());

        return ResponseEntity.status(HttpStatus.CREATED).body(new IgraAdminDTO(igraServis.save(igra)));
    }

    //admin - izmena igrice
    @PutMapping("/{id}")
    public ResponseEntity<?> izmeni(@PathVariable Long id, @RequestBody IgraAdminDTO dto, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Igra postojeca = igraServis.findOne(id);
        if (postojeca == null)
            return ResponseEntity.notFound().build();

        Kategorija kategorija = kategorijaServis.findByNazivIgnoreCase(dto.getKategorija());
        if (kategorija == null)
            return ResponseEntity.badRequest().body("Kategorija ne postoji!");

        postojeca.setNaziv(dto.getNaziv());
        postojeca.setOpis(dto.getOpis());
        postojeca.setKategorija(kategorija);
        postojeca.setAktivna(dto.isAktivna());
        postojeca.setURL(dto.getUrl());
        postojeca.setSlika(dto.getSlika());

        return ResponseEntity.ok(new IgraAdminDTO(igraServis.save(postojeca)));
    }

    //admin - aktivacija/deaktivacija igrice
    @PatchMapping("/{id}/aktivnost")
    public ResponseEntity<?> promeniAktivnost(@PathVariable Long id, @RequestBody Map<String, Object> body, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null)
            return new ResponseEntity<>("Niste ulogovani!", HttpStatus.UNAUTHORIZED);
        if (korisnik.getUloga() != Korisnik.Uloga.ADMINISTRATOR)
            return new ResponseEntity<>("Nemate pravo pristupa!", HttpStatus.FORBIDDEN);

        Igra igra = igraServis.findOne(id);
        if (igra == null)
            return ResponseEntity.notFound().build();

        Boolean aktivna = (Boolean) body.get("aktivna");
        igra.setAktivna(aktivna);
        return ResponseEntity.ok(new IgraAdminDTO(igraServis.save(igra)));
    }
}