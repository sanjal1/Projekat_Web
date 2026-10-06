package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Postignuca;

public class PostignucaAdminDTO {
    private Long id;
    private String naziv;
    private String opis;

    public PostignucaAdminDTO() {}

    public PostignucaAdminDTO(Postignuca p) {
        this.id = p.getId();
        this.naziv = p.getNaziv();
        this.opis = p.getOpis();
    }


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }

    public void setOpis(String opis) { this.opis = opis; }
}
