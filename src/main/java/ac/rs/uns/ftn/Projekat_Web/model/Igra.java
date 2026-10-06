package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "igrice")
public class Igra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String naziv;

    @Column
    private String opis;

    private String URL;

    private String slika;

    @Column
    private LocalDateTime datum_dodavanja;

    @Column
    private boolean aktivna;


    public Igra() {
        this.naziv = "";
        this.opis = "";
        this.URL = "";
        this.slika = "";
        this.aktivna = false;
    }

    public Igra(String naziv, String opis, String URL, String slika, boolean aktivna) {
        this.naziv = naziv;
        this.opis = opis;
        this.URL = URL;
        this.slika = slika;
        this.aktivna = aktivna;
    }


    @PrePersist
    protected void DodavanjeIgrice() {
        this.datum_dodavanja = LocalDateTime.now();
    }


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "kategorija_id")
    private Kategorija kategorija;

    //vise postignuca moze pripadati 1-noj igri
    @OneToMany(mappedBy = "igra", cascade = CascadeType.ALL)
    private List<Postignuca> postignuca = new ArrayList<>();

    @OneToMany(mappedBy = "igra", cascade = CascadeType.ALL)
    private List<Statistika> statistike;

    @OneToMany(mappedBy = "igra", cascade = CascadeType.ALL)
    private List<Recenzija> recenzije;



    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    public String getURL() { return URL; }

    public void setURL(String URL) { this.URL = URL; }

    public String getSlika() { return slika; }

    public void setSlika(String slika) { this.slika = slika; }

    public Kategorija getKategorija() { return kategorija; }

    public void setKategorija(Kategorija kategorija) { this.kategorija = kategorija; }

    public LocalDateTime getDatum_dodavanja() { return datum_dodavanja; }

    public void setDatum_dodavanja(LocalDateTime datum_dodavanja) { this.datum_dodavanja = datum_dodavanja; }

    public boolean isAktivna() { return aktivna; }

    public void setAktivna(boolean aktivna) { this.aktivna = aktivna; }

    public List<Postignuca> getPostignuca() { return postignuca; }

    public void setPostignuca(List<Postignuca> postignuca) { this.postignuca = postignuca; }

    public List<Statistika> getStatistike() { return statistike; }

    public void setStatistike(List<Statistika> statistike) { this.statistike = statistike; }

    public List<Recenzija> getRecenzije() { return recenzije; }

    public void setRecenzije(List<Recenzija> recenzije) { this.recenzije = recenzije; }

    @Override
    public String toString() {
        return "---Igra---\n" +
                "ID: " + id +
                "\nNaziv: '" + naziv + '\'' +
                "\nOpis: '" + opis + '\'' +
                "\nKategorija: " + kategorija +
                "\nDatum dodavanja: " + datum_dodavanja +
                "\nAktivna: " + (aktivna ? "Da" : "Ne") + "\n";
    }
}
