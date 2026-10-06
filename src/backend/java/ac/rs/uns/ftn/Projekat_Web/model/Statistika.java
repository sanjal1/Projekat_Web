package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistike")
public class Statistika {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "igra_id", nullable = false)
    private Igra igra;

    @Column(nullable = false)
    private LocalDateTime vremePocetka;

    private LocalDateTime vremeZavrsetka;

    public Statistika() {}

    public Statistika(Korisnik korisnik, Igra igra, LocalDateTime vremePocetka) {
        this.korisnik = korisnik;
        this.igra = igra;
        this.vremePocetka = vremePocetka;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Korisnik getKorisnik() { return korisnik; }
    public void setKorisnik(Korisnik korisnik) { this.korisnik = korisnik; }

    public Igra getIgra() { return igra; }
    public void setIgra(Igra igra) { this.igra = igra; }

    public LocalDateTime getVremePocetka() { return vremePocetka; }
    public void setVremePocetka(LocalDateTime vremePocetka) { this.vremePocetka = vremePocetka; }

    public LocalDateTime getVremeZavrsetka() { return vremeZavrsetka; }
    public void setVremeZavrsetka(LocalDateTime vremeZavrsetka) { this.vremeZavrsetka = vremeZavrsetka; }

    public long trajanjeSekundi() {
        if (this.vremeZavrsetka == null) return 0;
        return java.time.Duration.between(this.vremePocetka, this.vremeZavrsetka).getSeconds();
    }

}
