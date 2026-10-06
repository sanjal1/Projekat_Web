package ac.rs.uns.ftn.Projekat_Web.service;

import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Kategorija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KategorijaServis {

    @Autowired
    private Repozitorijum_Kategorija kategorijaRepo;

    public Kategorija findOne(Long id) {
        Optional<Kategorija> kategorija = kategorijaRepo.findById(id);
        if (kategorija.isPresent())
            return kategorija.get();
        return null;
    }

    public Kategorija findByNazivIgnoreCase(String naziv) {
        return kategorijaRepo.findByNazivIgnoreCase(naziv);
    }

    public List<Kategorija> pretragaPoNazivu(String naziv) {
        return kategorijaRepo.findByNazivContainingIgnoreCase(naziv);
    }

    public List<Kategorija> findAll() {
        return kategorijaRepo.findAll();
    }

    public Kategorija save(Kategorija kategorija) {
        return kategorijaRepo.save(kategorija);
    }

    public void delete(Long id) {
        kategorijaRepo.deleteById(id);
    }
}
