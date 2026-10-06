package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "postignuca")
public class Postignuca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String naziv;

    @Column
    private String opis;

    private int vremeIgranja;

    private int brojPokretanjaIgrice;


    public Postignuca() {
        this.naziv = "";
        this.opis = "";
    }

    public Postignuca(String naziv, String opis) {
        this.naziv = naziv;
        this.opis = opis;
    }


    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnici;

    //jedna igra po postignucu
    @ManyToOne
    @JoinColumn(name = "igra_id")
    private Igra igra;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id;}

    public int getVremeIgranja() { return vremeIgranja; }

    public void setVremeIgranja(int vremeIgranja) { this.vremeIgranja = vremeIgranja; }

    public int getBrojPokretanjaIgrice() { return brojPokretanjaIgrice; }

    public void setBrojPokretanjaIgrice(int brojPokretanjaIgrice) { this.brojPokretanjaIgrice = brojPokretanjaIgrice; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    public Korisnik getKorisnici() { return korisnici; }

    public void setKorisnici(Korisnik korisnici) { this.korisnici = korisnici; }

    public Igra getIgra() { return igra; }

    public void setIgra(Igra igra) { this.igra = igra; }

    @Override
    public String toString() {
        return "---Postignuca---\n" +
                "ID: " + id +
                "\nNaziv: '" + naziv + '\'' +
                "\nOpis: '" + opis + '\'' + "\n";
    }

}
