package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import javax.sql.RowSet;

@Entity
public class Recenzija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ocena;

    private String komentar;

    @ManyToOne
    @JoinColumn(name = "igra_id")
    private Igra igra;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;


    public int getOcena() { return ocena; }

    public void setOcena(int ocena) { this.ocena = ocena; }

    public String getKomentar() { return komentar; }

    public void setKomentar(String komentar) { this.komentar = komentar; }

    public Igra getIgra() { return igra; }

    public void setIgra(Igra igra) { this.igra = igra; }

    public Korisnik getKorisnik() { return korisnik; }

    public void setKorisnik(Korisnik korisnik) { this.korisnik = korisnik; }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}
}
