package ac.rs.uns.ftn.Projekat_Web.controller;

import ac.rs.uns.ftn.Projekat_Web.dto.ChatPorukaDTO;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.service.Servis_Korisnik;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class Kontroler_Chat {

    private final Servis_Korisnik korisnikServis;

    public Kontroler_Chat(Servis_Korisnik korisnikServis) {
        this.korisnikServis = korisnikServis;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatPorukaDTO send(
            ChatPorukaDTO poruka,
            SimpMessageHeaderAccessor headerAccessor) {

        Korisnik ulogovani = (Korisnik) headerAccessor
                .getSessionAttributes()
                .get("korisnik");

        if (ulogovani == null) {
            throw new RuntimeException("Niste ulogovani!");
        }

        if (ulogovani.isBlokiran()) {
            throw new RuntimeException("Blokirani korisnici ne mogu koristiti chat.");
        }

        // Učitaj svežeg korisnika iz baze da bi imao ažuriranu profilnu sliku
        Korisnik svezi = korisnikServis.getById(ulogovani.getId());

        poruka.setKorisnikIme(svezi.getIme());
        poruka.setKorisnikId(svezi.getId());

        // Ukloni navodnike iz profilne slike
        String slika = svezi.getProfilnaSlika();
        poruka.setProfilnaSlika(slika != null ? slika.replace("\"", "").trim() : null);

        return poruka;

    }
}