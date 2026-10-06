package ac.rs.uns.ftn.Projekat_Web.service;

import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Korisnik;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Servis_Korisnik {

    private final Repozitorijum_Korisnik korisnikRepository;

    public Servis_Korisnik(Repozitorijum_Korisnik korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }
    // REGISTRACIJA

    public Korisnik registrujKorisnika(Korisnik korisnik) {

        if (korisnikRepository.existsByEmail(korisnik.getEmail())) {
            throw new RuntimeException("Korisnik sa tim emailom već postoji.");
        }

        korisnik.setLozinka(LozinkaUtil.hesuj(korisnik.getLozinka()));

        korisnik.setBlokiran(false);

        return korisnikRepository.save(korisnik);
    }

    // LOGIN

    public Korisnik prijava(String email, String lozinka) {

        Korisnik korisnik = korisnikRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Korisnik nije pronađen."));

        if (korisnik.isBlokiran()) {
            throw new RuntimeException("Korisnik je blokiran.");
        }

        if (!LozinkaUtil.proveri(lozinka, korisnik.getLozinka())) {
            throw new RuntimeException("Pogrešna lozinka.");
        }

        return korisnik;
    }

    // SVI KORISNICI

    public List<Korisnik> getSviKorisnici() {
        return korisnikRepository.findAll();
    }

    // KORISNIK PO ID

    public Korisnik getById(Long id) {

        return korisnikRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Korisnik nije pronađen."));
    }

    // BLOKIRANJE

    public Korisnik blokiraj(Long id) {

        Korisnik korisnik = getById(id);

        korisnik.setBlokiran(true);

        return korisnikRepository.save(korisnik);
    }

    // ODBLOKIRANJE

    public Korisnik odblokiraj(Long id) {

        Korisnik korisnik = getById(id);

        korisnik.setBlokiran(false);

        return korisnikRepository.save(korisnik);
    }

    // IZMENA PROFILA

    public Korisnik izmeniProfil(Long id,
                                 String ime,
                                 String prezime,
                                 String email,
                                 LocalDate datumRodjenja,
                                 String profilnaSlika) {

        Korisnik korisnik = getById(id);

        Optional<Korisnik> postojeci =
                korisnikRepository.findByEmail(email);

        if(postojeci.isPresent()
                && !postojeci.get().getId().equals(id)) {

            throw new RuntimeException(
                    "Email je već zauzet."
            );
        }

        korisnik.setIme(ime);
        korisnik.setPrezime(prezime);
        korisnik.setEmail(email);
        korisnik.setDatumRodjenja(datumRodjenja);
        korisnik.setProfilnaSlika(profilnaSlika);

        return korisnikRepository.save(korisnik);
    }

    // PROMENA PROFILNE SLIKE

    public Korisnik promeniProfilnuSliku(Long id,
                                         String profilnaSlika) {

        Korisnik korisnik = getById(id);

        korisnik.setProfilnaSlika(profilnaSlika);

        return korisnikRepository.save(korisnik);
    }

    // PROMENA LOZINKE

    public void promeniLozinku(Long id,
                               String staraLozinka,
                               String novaLozinka) {

        Korisnik korisnik = getById(id);

        if (!LozinkaUtil.proveri(staraLozinka, korisnik.getLozinka())) {
            throw new RuntimeException("Stara lozinka nije tačna.");
        }

        korisnik.setLozinka(LozinkaUtil.hesuj(novaLozinka));
        korisnikRepository.save(korisnik);
    }

    // PRETRAGA PO IMENU ILI PREZIMENU

    public List<Korisnik> pretraga(String tekst) {

        return korisnikRepository
                .findByImeContainingOrPrezimeContaining(
                        tekst,
                        tekst
                );
    }

    // KORISNICI PO ULOZI

    public List<Korisnik> getPoUlozi(Korisnik.Uloga uloga) {

        return korisnikRepository.findByUloga(uloga);
    }

    // BLOKIRANI KORISNICI

    public List<Korisnik> getBlokiraniKorisnici() {

        return korisnikRepository.findByBlokiran(true);
    }

    // AKTIVNI KORISNICI (POSLEDNJIH 30 DANA)

    public List<Korisnik> getAktivniKorisnici() {

        LocalDateTime pre30Dana =
                LocalDateTime.now().minusDays(30);

        return korisnikRepository
                .findAktivneKorisnike(pre30Dana);
    }

    // BROJ AKTIVNIH KORISNIKA

    public long getBrojAktivnihKorisnika() {

        return getAktivniKorisnici().size();
    }

    // UKUPAN BROJ KORISNIKA

    public long getBrojKorisnika() {

        return korisnikRepository.count();
    }
}
