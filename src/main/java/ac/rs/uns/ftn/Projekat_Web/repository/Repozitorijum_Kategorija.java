package ac.rs.uns.ftn.Projekat_Web.repository;

import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repozitorijum_Kategorija extends JpaRepository<Kategorija, Long> {
    //pretraga po nazivu
    List<Kategorija> findByNazivContainingIgnoreCase(String naziv);

    //dodeljivanje kategorija igrica
    Kategorija findByNazivIgnoreCase(String naziv);

}
