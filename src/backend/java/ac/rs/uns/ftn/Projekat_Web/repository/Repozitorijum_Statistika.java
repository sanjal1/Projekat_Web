package ac.rs.uns.ftn.Projekat_Web.repository;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Statistika;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface Repozitorijum_Statistika extends JpaRepository<Statistika, Long> {

    List<Statistika> findByKorisnik(Korisnik korisnik);

    List<Statistika> findByIgra(Igra igra);

    List<Statistika> findByKorisnikAndIgra(Korisnik korisnik, Igra igra);

    Optional<Statistika> findByKorisnikAndVremeZavrsetkaIsNull(Korisnik korisnik);

    List<Statistika> findByKorisnikAndVremeZavrsetkaIsNotNull(Korisnik korisnik);

    long countByKorisnikAndIgra(Korisnik korisnik, Igra igra);

    List<Statistika> findByVremeZavrsetkaIsNotNull();

    @Query("SELECT s.igra, COUNT(s) as brojPokretanja " +
            "FROM Statistika s " +
            "WHERE s.vremeZavrsetka IS NOT NULL " +
            "GROUP BY s.igra " +
            "ORDER BY brojPokretanja DESC")
    List<Object[]> findNajigranjijeIgrice();

    @Query("SELECT s.igra, COUNT(s) as brojPokretanja " +
            "FROM Statistika s " +
            "WHERE s.vremePocetka >= :datum " +
            "AND s.vremeZavrsetka IS NOT NULL " +
            "GROUP BY s.igra " +
            "ORDER BY brojPokretanja DESC")
    List<Object[]> findNajigranjijeIgriceU30Dana(LocalDateTime datum);
}