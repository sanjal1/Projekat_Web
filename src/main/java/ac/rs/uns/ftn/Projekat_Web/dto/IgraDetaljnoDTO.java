package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;

public class IgraDetaljnoDTO {
    private Long id;
    private String naziv;
    private String slika;
    private String opis;
    private String url;
    private String kategorija;
    private double prosecnaOcena;

    public IgraDetaljnoDTO() {}

    public IgraDetaljnoDTO(Igra igra) {
        this.id = igra.getId();
        this.naziv = igra.getNaziv();
        this.slika = igra.getSlika();
        this.opis = igra.getOpis();
        this.url = igra.getURL();
        this.kategorija = igra.getKategorija() != null ? igra.getKategorija().getNaziv() : null;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getSlika() { return slika; }

    public void setSlika(String slika) { this.slika = slika; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getKategorija() { return kategorija; }

    public void setKategorija(String kategorija) { this.kategorija = kategorija; }

    public double getProsecnaOcena() { return prosecnaOcena; }

    public void setProsecnaOcena(double prosecnaOcena) { this.prosecnaOcena = prosecnaOcena; }

}
