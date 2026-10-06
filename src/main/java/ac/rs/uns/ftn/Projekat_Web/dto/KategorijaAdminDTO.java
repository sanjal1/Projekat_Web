package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;

public class KategorijaAdminDTO {
    private Long id;
    private String naziv;
    private String opis;
    private int brojIgara;

    public KategorijaAdminDTO() {}

    public KategorijaAdminDTO(Kategorija k) {
        this.id = k.getId();
        this.naziv = k.getNaziv();
        this.opis = k.getOpis();
        this.brojIgara = k.getIgrice().size();
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis;}

    public void setOpis(String opis) { this.opis = opis; }

    public int getBrojIgara() { return brojIgara; }

    public void setBrojIgara(int brojIgara) { this.brojIgara = brojIgara; }

}
