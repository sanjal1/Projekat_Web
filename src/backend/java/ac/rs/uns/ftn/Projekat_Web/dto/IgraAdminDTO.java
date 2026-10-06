package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;

public class    IgraAdminDTO {
    private Long id;
    private String naziv;
    private String opis;
    private String slika;
    private String url;
    private String kategorija;
    private boolean aktivna;
    private Double prosecnaOcena;

    public IgraAdminDTO() {}

    public IgraAdminDTO(Igra igra) {
        this.id = igra.getId();
        this.naziv = igra.getNaziv();
        this.slika = igra.getSlika();
        this.opis = igra.getOpis();
        this.url = igra.getURL();
        this.kategorija = igra.getKategorija() != null ? igra.getKategorija().getNaziv() : null;
        this.aktivna = igra.isAktivna();
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

    public boolean isAktivna() { return aktivna; }

    public void setAktivna(boolean aktivna) { this.aktivna = aktivna; }

    public String getKategorija() { return kategorija; }

    public void setKategorija(String kategorija) { this.kategorija = kategorija; }

    public Double getProsecnaOcena() { return prosecnaOcena; }

    public void setProsecnaOcena(double prosecnaOcena) { this.prosecnaOcena = prosecnaOcena; }

}
