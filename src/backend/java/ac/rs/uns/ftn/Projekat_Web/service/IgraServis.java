package ac.rs.uns.ftn.Projekat_Web.service;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Igra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IgraServis {

    @Autowired
    private Repozitorijum_Igra igraRepo;

    public Igra findOne(Long id) {
        Optional<Igra> igra = igraRepo.findById(id);
        if (igra.isPresent())
            return igra.get();
        return null;
    }

    public List<Igra> findAll() {return igraRepo.findAll();}

    public List<Igra> findActive() {
        List<Igra> igre = igraRepo.findByAktivnaTrue();
        return igre != null ? igre : new ArrayList<>(); }

    public long getBrojAktivnih() {
        return igraRepo.countByAktivnaTrue();
    }

    public List<Igra> pretrazi(String naziv) {
        return igraRepo.findByAktivnaTrueAndNazivContainingIgnoreCase(naziv);
    }

    public List<Igra> poKategoriji(Long kategorijaId) {
        return igraRepo.findByAktivnaTrueAndKategorijaId(kategorijaId);
    }

    public List<Igra> sortiranePoOceni() {
        return igraRepo.igreSortiranePoOceni();
    }

    public Igra save(Igra igra) {
        return igraRepo.save(igra);
    }

    public void delete(Long id) {
        igraRepo.deleteById(id);
    }

}
