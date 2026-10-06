package ac.rs.uns.ftn.Projekat_Web.service;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Postignuca;
import ac.rs.uns.ftn.Projekat_Web.model.Statistika;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Statistika;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Servis_Statistika {

    private final Repozitorijum_Statistika statistikaRepository;
    private final PostignucaServis postignucaServis;


    public Servis_Statistika(Repozitorijum_Statistika statistikaRepository,
                             PostignucaServis postignucaServis) {
        this.statistikaRepository = statistikaRepository;
        this.postignucaServis = postignucaServis;
    }

    // POČETAK I ZAVRŠETAK IGRANJA

    public Statistika zapocniIgranje(Korisnik korisnik, Igra igra) {

        Optional<Statistika> aktivnaSesija =
                statistikaRepository
                        .findByKorisnikAndVremeZavrsetkaIsNull(korisnik);

        if (aktivnaSesija.isPresent()) {
            throw new RuntimeException(
                    "Morate prvo završiti trenutno aktivnu igru."
            );
        }

        Statistika statistika = new Statistika();
        statistika.setKorisnik(korisnik);
        statistika.setIgra(igra);
        statistika.setVremePocetka(LocalDateTime.now());

        return statistikaRepository.save(statistika);
    }

    public Statistika zavrsiIgranje(Korisnik korisnik) {
        Statistika statistika = statistikaRepository
                .findByKorisnikAndVremeZavrsetkaIsNull(korisnik)
                .orElseThrow(() -> new RuntimeException("Ne postoji aktivna sesija."));
        statistika.setVremeZavrsetka(LocalDateTime.now());
        Statistika sacuvana = statistikaRepository.save(statistika);

        postignucaServis.proveriIDodeliPostignuca(korisnik, sacuvana.getIgra(), this);
        return statistika;


    }

    //  STATISTIKA KORISNIKA

    // Ukupno vreme igranja
    public long getUkupnoVremeIgranja(Korisnik korisnik) {
        List<Statistika> sesije = statistikaRepository
                .findByKorisnikAndVremeZavrsetkaIsNotNull(korisnik);
        long ukupno = 0;
        for (Statistika s : sesije) {
            ukupno += s.trajanjeSekundi();
        }
        return ukupno;
    }

    // Vreme igranja za konkretnu igricu
    public long getUkupnoVremeZaIgru(Korisnik korisnik, Igra igra) {
        List<Statistika> sesije = statistikaRepository
                .findByKorisnikAndIgra(korisnik, igra);
        long ukupno = 0;
        for (Statistika s : sesije) {
            if (s.getVremeZavrsetka() != null) {
                ukupno += s.trajanjeSekundi();
            }
        }
        return ukupno;
    }

    // Vreme igranja za sve igrice - mapa igrica i vremena
    public Map<Igra, Long> getVremePoIgrici(Korisnik korisnik) {
        List<Statistika> sesije = statistikaRepository
                .findByKorisnikAndVremeZavrsetkaIsNotNull(korisnik);
        Map<Igra, Long> rezultat = new HashMap<>();
        for (Statistika s : sesije) {
            Igra igra = s.getIgra();
            long trenutno = rezultat.getOrDefault(igra, 0L);
            rezultat.put(igra, trenutno + s.trajanjeSekundi());
        }
        return rezultat;
    }

    // Broj pokretanja igrice
    public long getBrojPokretanja(Korisnik korisnik, Igra igra) {
        return statistikaRepository.countByKorisnikAndIgra(korisnik, igra);
    }

    // Ukupno vreme po kategorijama
    public Map<String, Long> getVremePoKategorijama(Korisnik korisnik) {
        List<Statistika> sesije = statistikaRepository
                .findByKorisnikAndVremeZavrsetkaIsNotNull(korisnik);
        Map<String, Long> rezultat = new HashMap<>();
        for (Statistika s : sesije) {
            String kategorija = s.getIgra().getKategorija().getNaziv();
            long trenutno = rezultat.getOrDefault(kategorija, 0L);
            rezultat.put(kategorija, trenutno + s.trajanjeSekundi());
        }
        return rezultat;
    }

    // Sve sesije korisnika
    public List<Statistika> getSesijeKorisnika(Korisnik korisnik) {
        return statistikaRepository.findByKorisnik(korisnik);
    }

    // ADMIN

    public List<Statistika> getSveSesije() {
        return statistikaRepository.findAll();
    }

    public List<Statistika> getSveZavrseneSesije() {
        return statistikaRepository.findByVremeZavrsetkaIsNotNull();
    }

    public List<Statistika> getSesijeIgre(Igra igra) {
        return statistikaRepository.findByIgra(igra);
    }

    public List<Object[]> getNajigranijeIgrice() {
        return statistikaRepository.findNajigranjijeIgrice();
    }

    // ADMIN DASHBOARD

    public List<Object[]> getNajigranijeIgriceU30Dana() {
        LocalDateTime pre30Dana = LocalDateTime.now().minusDays(30);
        return statistikaRepository.findNajigranjijeIgriceU30Dana(pre30Dana);
    }

    public Map<Korisnik, Long> getNajaktivnijiKorisnici() {
        List<Statistika> sveSesije = statistikaRepository
                .findByVremeZavrsetkaIsNotNull();
        Map<Korisnik, Long> rezultat = new HashMap<>();
        for (Statistika s : sveSesije) {
            Korisnik korisnik = s.getKorisnik();
            long trenutno = rezultat.getOrDefault(korisnik, 0L);
            rezultat.put(korisnik, trenutno + s.trajanjeSekundi());
        }
        return rezultat;
    }



    public Statistika getById(Long id) {
        return statistikaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistika nije pronađena."));
    }

    public void obrisi(Long id) {
        Statistika statistika = getById(id);
        statistikaRepository.delete(statistika);
    }
}
