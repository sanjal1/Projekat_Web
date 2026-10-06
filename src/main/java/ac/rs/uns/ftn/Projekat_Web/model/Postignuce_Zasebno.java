package ac.rs.uns.ftn.Projekat_Web.model;

import  jakarta.persistence.*;

@Entity
public class Postignuce_Zasebno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private String opis;
    private String tip;
    private int vrednost;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    public String getTip() { return tip; }

    public void setTip(String tip) { this.tip = tip; }

    public int getVrednost() { return vrednost; }

    public void setVrednost(int vrednost) { this.vrednost = vrednost; }

}
