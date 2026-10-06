package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;

public class IgraOsnovnoDTO {
    private Long id;
    private String naziv;
    private String slika;
    private String kategorija;
    private String opis;
    private String url;
    private double prosecnaOcena;

    public IgraOsnovnoDTO() {}

    public IgraOsnovnoDTO(Igra igra) {
        this.id = igra.getId();
        this.naziv = igra.getNaziv();
        this.slika = igra.getSlika();
        this.url = igra.getURL();
        this.opis = igra.getOpis();
        this.kategorija = igra.getKategorija() != null ? igra.getKategorija().getNaziv() : "Bez kategorije";

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getSlika() { return slika; }

    public void setSlika(String slika) { this.slika = slika; }

    public String getKategorija() { return kategorija; }

    public void setKategorija(String kategorija) { this.kategorija = kategorija; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public double getProsecnaOcena() { return prosecnaOcena; }

    public void setProsecnaOcena(double prosecnaOcena) { this.prosecnaOcena = prosecnaOcena; }

}
