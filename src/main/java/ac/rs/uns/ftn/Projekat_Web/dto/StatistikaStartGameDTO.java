package ac.rs.uns.ftn.Projekat_Web.dto;

public class StatistikaStartGameDTO {

    private Long korisnikId;
    private Long igraId;

    public StatistikaStartGameDTO() {}

    public Long getKorisnikId() { return korisnikId; }
    public void setKorisnikId(Long korisnikId) { this.korisnikId = korisnikId; }

    public Long getIgraId() { return igraId; }
    public void setIgraId(Long igraId) { this.igraId = igraId; }
}
