package ac.rs.uns.ftn.Projekat_Web.repository;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Postignuca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repozitorijum_Postignuca extends JpaRepository<Postignuca, Long> {
    //postignuca jednog korisnika
    List<Postignuca> findByKorisnici_Id(Long korisnikId);

    //postignuca za jednu igru
    List<Postignuca> findByIgra_Id(Long igraId);

    //postignuca korisnika na odredjenoj-izabranoj igrici
    List<Postignuca> findByKorisnici_IdAndIgra_Id(Long korisnikId, Long igraId);

    //gledamo da li korisnik vec ima odredjeno postignuce
    boolean existsByKorisniciAndIgraAndNaziv(Korisnik korisnik, Igra igra, String naziv);


    //broj postignuca korisnika
    long countByKorisnici_Id(Long korisnikId);



}
