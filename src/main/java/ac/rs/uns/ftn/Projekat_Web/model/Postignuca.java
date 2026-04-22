package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Postignuca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String naziv;

    @Column
    private String opis;


    @ManyToMany(mappedBy = "postignuca")
    private List<Korisnik> korisnici =  new ArrayList<>();

    //ako ima 1 igra 1 post:
    //@OneToMany(mappedBy = "postignuce", cascade = CascadeType.ALL)
    //private List<Igra> igrice = new ArrayList<>();

    //ako ima vise:
    @ManyToMany(mappedBy = "postignuca")
    private List<Igra> igrice = new ArrayList<>();


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id;}

    public List<Korisnik> getKorisnici() { return korisnici; }

    public void setKorisnici(List<Korisnik> korisnici) { this.korisnici = korisnici; }

    public List<Igra> getIgrice() { return igrice; }

    public void setIgrice(List<Igra> igrice) { this.igrice = igrice; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }


    @Override
    public String toString() {
        return "---Postignuca---\n" +
                "ID: " + id +
                "\nNaziv: '" + naziv + '\'' +
                "\nOpis: '" + opis + '\'' + "\n";
    }


}
