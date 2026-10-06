package ac.rs.uns.ftn.Projekat_Web.repository;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Repozitorijum_Igra extends JpaRepository<Igra, Long> {
    List<Igra> findByNazivContainingIgnoreCase(String naziv);

    //sort po pros oceni
    @Query("SELECT i FROM Igra i LEFT JOIN i.recenzije r GROUP BY i.id ORDER BY COALESCE(AVG(r.ocena),0) DESC")
    List<Igra> igreSortiranePoOceni();

    // sve igre po nazivu kategorije
    List<Igra> findByKategorijaNazivIgnoreCase(String naziv);

    //filtriranje po kategoriji
    List<Igra> findByKategorijaId(Long kategorijaId);

    //sve igre
    List<Igra> findAll();

    //samo aktivne igre
    List<Igra> findByAktivnaTrue();

    //aktivne igre po kategoriji
    List<Igra> findByAktivnaTrueAndKategorijaId(Long kategorijaId);

    //pretraga aktivnih igara po nazivu
    List<Igra> findByAktivnaTrueAndNazivContainingIgnoreCase(String naziv);

    //ukupno dostupnih (aktivnih) igrica(na poc. str.)
    long countByAktivnaTrue();

    boolean existsByNazivIgnoreCase(String naziv);
    Optional<Igra> findByNazivIgnoreCase(String naziv);

}
