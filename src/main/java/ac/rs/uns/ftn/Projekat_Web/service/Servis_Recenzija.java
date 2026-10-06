package ac.rs.uns.ftn.Projekat_Web.service;

import ac.rs.uns.ftn.Projekat_Web.model.Recenzija;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Recenzija;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Servis_Recenzija {

    private final Repozitorijum_Recenzija recenzijaRepository;

    public Servis_Recenzija(
            Repozitorijum_Recenzija recenzijaRepository) {

        this.recenzijaRepository = recenzijaRepository;
    }

    // DODAVANJE RECENZIJE

    public Recenzija dodajRecenziju(
            Recenzija recenzija) {

        if (recenzija.getOcena() < 1 ||
                recenzija.getOcena() > 5) {

            throw new RuntimeException(
                    "Ocena mora biti između 1 i 5.");
        }

        if (recenzijaRepository
                .existsByKorisnikIdAndIgraId(
                        recenzija.getKorisnik().getId(),
                        recenzija.getIgra().getId())) {

            throw new RuntimeException(
                    "Već ste ocenili ovu igru.");
        }

        return recenzijaRepository.save(recenzija);
    }

    // RECENZIJA PO ID

    public Recenzija getById(Long id) {

        return recenzijaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Recenzija nije pronađena."));
    }

    // RECENZIJE ZA IGRU

    public List<Recenzija> getRecenzijeZaIgru(
            Long igraId) {

        return recenzijaRepository
                .findByIgraId(igraId);
    }

    // RECENZIJE KORISNIKA

    public List<Recenzija> getRecenzijeKorisnika(
            Long korisnikId) {

        return recenzijaRepository
                .findByKorisnikId(korisnikId);
    }

    // PROSEČNA OCENA

    public double getProsecnaOcena(
            Long igraId) {

        Double prosecna =
                recenzijaRepository
                        .prosecnaOcenaZaIgru(
                                igraId);

        return prosecna == null
                ? 0
                : prosecna;
    }

    // IZMENA RECENZIJE

    public Recenzija izmeniRecenziju(
            Long id,
            int ocena,
            String komentar) {

        if (ocena < 1 || ocena > 5) {

            throw new RuntimeException(
                    "Ocena mora biti između 1 i 5.");
        }

        Recenzija recenzija = getById(id);

        if(recenzija.getKorisnik() == null){
            throw new RuntimeException("Korisnik nije definisan.");
        }

        if(recenzija.getIgra() == null){
            throw new RuntimeException("Igra nije definisana.");
        }

        recenzija.setOcena(ocena);
        recenzija.setKomentar(komentar);

        return recenzijaRepository.save(recenzija);
    }

    // BRISANJE RECENZIJE

    public void obrisi(Long id) {

        Recenzija recenzija = getById(id);

        recenzijaRepository.delete(recenzija);
    }
}