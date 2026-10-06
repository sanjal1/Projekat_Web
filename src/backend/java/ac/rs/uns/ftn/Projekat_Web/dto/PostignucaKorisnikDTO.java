package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Postignuca;

public class PostignucaKorisnikDTO {
    private Long id;
    private String naziv;
    private String opis;
    private String igra;

    public PostignucaKorisnikDTO() {}

    public PostignucaKorisnikDTO(Postignuca p) {
        this.id = p.getId();
        this.naziv = p.getNaziv();
        this.opis = p.getOpis();
        this.igra = p.getIgra() != null ? p.getIgra().getNaziv() : null;
    }



    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getIgra() { return igra; }

    public void setIgra(String igra) { this.igra = igra; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }
}
