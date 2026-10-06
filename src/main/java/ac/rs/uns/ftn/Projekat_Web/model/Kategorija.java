package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kategorije")
public class Kategorija implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String naziv;

    @Column
    private String opis;


    public Kategorija() {
        this.naziv = "";
        this.opis = "";
    }

    public Kategorija(String naziv, String opis) {
        this.naziv = naziv;
        this.opis = opis;
    }


    @OneToMany(mappedBy = "kategorija", cascade = CascadeType.ALL)
    private List<Igra> igrice = new ArrayList<Igra>();


    public List<Igra> getIgrice() { return igrice; }

    public void setIgrice(List<Igra> igrice) { this.igrice = igrice; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }


    @Override
    public String toString() {
        return "---Kategorija---\n" +
                "ID:" + id +
                "\nIgrice: " + igrice +
                "\nNaziv: '" + naziv + '\'' +
                "\nOpis: '" + opis + '\'' + "\n";
    }

}
