package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
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
    private Date datum_dodavanja;

    @Column
    private boolean aktivna;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "kategorija_id")
    private Kategorija kategorija;

    //many to many ili da svaka igra ima samo jedan achivment??
    //ako ima samo jedan achivment:
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JoinColumn(name = "postignuce_id")
    //private Postignuca postignuce;

    //ako ima vise:
    @ManyToMany
    @JoinTable(
            name = "igra_postignuce",
            joinColumns = @JoinColumn(name = "igra_id"),
            inverseJoinColumns = @JoinColumn(name = "postignuce_id")
    )
    private List<Postignuca> postignuca = new ArrayList<>();

    @OneToMany(mappedBy = "igra", cascade = CascadeType.ALL)
    private List<Statistika> statistike;


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

    public Date getDatum_dodavanja() { return datum_dodavanja; }

    public void setDatum_dodavanja(Date datum_dodavanja) { this.datum_dodavanja = datum_dodavanja; }

    public boolean isAktivna() { return aktivna; }

    public void setAktivna(boolean aktivna) { this.aktivna = aktivna; }


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
