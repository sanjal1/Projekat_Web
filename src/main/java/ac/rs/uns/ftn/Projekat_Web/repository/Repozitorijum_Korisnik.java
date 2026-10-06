package ac.rs.uns.ftn.Projekat_Web.repository;

import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface Repozitorijum_Korisnik extends JpaRepository<Korisnik, Long> {

    Optional<Korisnik> findByEmail(String email);

    List<Korisnik> findByBlokiran(boolean blokiran);

    List<Korisnik> findByUloga(Korisnik.Uloga uloga);

    List<Korisnik> findByImeContainingOrPrezimeContaining(String ime, String prezime);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT s.korisnik FROM Statistika s WHERE s.vremePocetka >= :datum")
    List<Korisnik> findAktivneKorisnike(LocalDateTime datum);
}
