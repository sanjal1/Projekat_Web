package ac.rs.uns.ftn.Projekat_Web.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Kategorija implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String naziv;

    @Column
    private String opis;


    @OneToMany(mappedBy = "kategorija", cascade = CascadeType.ALL)
    private List<Igra> igrice;


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
