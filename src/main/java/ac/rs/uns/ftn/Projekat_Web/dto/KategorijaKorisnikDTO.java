package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;

public class KategorijaKorisnikDTO {
    private Long id;
    private String naziv;
    private String opis;

    public KategorijaKorisnikDTO() {}

    public KategorijaKorisnikDTO(Kategorija kategorija) {
        this.id = kategorija.getId();
        this.naziv = kategorija.getNaziv();
        this.opis = kategorija.getOpis();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
}
