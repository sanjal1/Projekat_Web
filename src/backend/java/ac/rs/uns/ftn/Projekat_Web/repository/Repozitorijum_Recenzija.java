package ac.rs.uns.ftn.Projekat_Web.repository;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Recenzija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repozitorijum_Recenzija extends JpaRepository<Recenzija, Long> {
    //recenzije za jednu igru
    List<Recenzija> findByIgraId(Long igraId);

    //recenzije jednog korisnika
    List<Recenzija> findByKorisnikId(Long korisnikId);

    //prosecna ocena igrice
    @Query("SELECT AVG(r.ocena) FROM Recenzija r WHERE r.igra.id = :igraId")
    Double prosecnaOcenaZaIgru(@Param("igraId") Long igraId);

    // da li je korisnik već ocenio igru
    boolean existsByKorisnikIdAndIgraId(
            Long korisnikId,
            Long igraId);

    // jedna konkretna recenzija korisnika za igru
    Optional<Recenzija> findByKorisnikIdAndIgraId(
            Long korisnikId,
            Long igraId);
}
