package ac.rs.uns.ftn.Projekat_Web.service;

import ac.rs.uns.ftn.Projekat_Web.model.*;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Postignuca;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Postignuce_Zasebno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostignucaServis {

    @Autowired
    private Repozitorijum_Postignuca postignucaRepo;

    @Autowired
    private Repozitorijum_Postignuce_Zasebno zasebnoRepo;

    public Postignuca findOne(Long id) {
        Optional<Postignuca> postignuca = postignucaRepo.findById(id);
        if (postignuca.isPresent())
            return postignuca.get();
        return null;
    }

    public void proveriIDodeliPostignuca(Korisnik korisnik, Igra igra, Servis_Statistika stat) {
        List<Postignuce_Zasebno> pravila = zasebnoRepo.findAll();

        for (Postignuce_Zasebno pravilo : pravila) {
            boolean uslovIspunjen = false;

            switch (pravilo.getTip()) {
                case "BROJ_POKRETANJA":
                    uslovIspunjen = stat.getBrojPokretanja(korisnik, igra) >= pravilo.getVrednost();
                    break;
                case "VREME_IGRANJA":
                    uslovIspunjen = stat.getUkupnoVremeZaIgru(korisnik, igra) >= pravilo.getVrednost();
                    break;
                default:
                    break;
            }

            if (uslovIspunjen && !vecImaPostignuce(korisnik, igra, pravilo.getNaziv())) {
                Postignuca p = new Postignuca();
                p.setNaziv(pravilo.getNaziv());
                p.setOpis(pravilo.getOpis());
                p.setKorisnici(korisnik);
                p.setIgra(igra);
                postignucaRepo.save(p);
            }
        }
    }

    private boolean vecImaPostignuce(Korisnik korisnik, Igra igra, String naziv) {
        return postignucaRepo.existsByKorisniciAndIgraAndNaziv(korisnik, igra, naziv);
    }

    public List<Postignuca> findAll() { return postignucaRepo.findAll(); }

    public List<Postignuca> findByKorisnik(Long korisnikId) {
        return postignucaRepo.findByKorisnici_Id(korisnikId);
    }

    public List<Postignuca> findByIgra(Long igraId) {
        return postignucaRepo.findByIgra_Id(igraId);
    }

    public long getBrojPostignuca(Long korisnikId) {
        return postignucaRepo.countByKorisnici_Id(korisnikId);
    }

    public Postignuca save(Postignuca postignuca) {
        return postignucaRepo.save(postignuca);
    }

    public void delete(Long id) {
        postignucaRepo.deleteById(id);
    }
}
