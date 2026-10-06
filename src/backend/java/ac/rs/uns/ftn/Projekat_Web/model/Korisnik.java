package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "korisnici")
public class Korisnik {

    public enum Uloga {
        KORISNIK,
        ADMINISTRATOR
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String lozinka;

    private LocalDate datumRodjenja;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Uloga uloga;

    private String profilnaSlika;

    @Column(nullable = false)
    private LocalDateTime datumRegistracije;

    @Column(nullable = false)
    private boolean blokiran = false;

    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL)
    private List<Recenzija> recenzije;

    public Korisnik() {}

    public Korisnik(Long id, String ime, String prezime, String email, String lozinka,
                    LocalDate datumRodjenja, Uloga uloga, String profilnaSlika,
                    LocalDateTime datumRegistracije, boolean blokiran) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
        this.datumRodjenja = datumRodjenja;
        this.uloga = uloga;
        this.profilnaSlika = profilnaSlika;
        this.datumRegistracije = datumRegistracije;
        this.blokiran = blokiran;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }

    public Uloga getUloga() { return uloga; }
    public void setUloga(Uloga uloga) { this.uloga = uloga; }

    public String getProfilnaSlika() { return profilnaSlika; }
    public void setProfilnaSlika(String profilnaSlika) { this.profilnaSlika = profilnaSlika; }

    public LocalDateTime getDatumRegistracije() { return datumRegistracije; }
    public void setDatumRegistracije(LocalDateTime datumRegistracije) { this.datumRegistracije = datumRegistracije; }

    public boolean isBlokiran() { return blokiran; }
    public void setBlokiran(boolean blokiran) { this.blokiran = blokiran; }

    @PrePersist
    public void prePersist() {
        this.datumRegistracije = LocalDateTime.now();
        if (this.uloga == null) {
            this.uloga = Uloga.KORISNIK;
        }
    }
}
