package ac.rs.uns.ftn.Projekat_Web.dto;

public class StatistikaZaAdminDTO {

    private Long korisnikId;
    private String imeKorisnika;
    private String prezimeKorisnika;
    private long ukupnoVremeIgranja;
    private long brojPokretanja;

    public StatistikaZaAdminDTO() {}

    public Long getKorisnikId() { return korisnikId; }
    public void setKorisnikId(Long korisnikId) { this.korisnikId = korisnikId; }

    public String getImeKorisnika() { return imeKorisnika; }
    public void setImeKorisnika(String imeKorisnika) { this.imeKorisnika = imeKorisnika; }

    public String getPrezimeKorisnika() { return prezimeKorisnika; }
    public void setPrezimeKorisnika(String prezimeKorisnika) { this.prezimeKorisnika = prezimeKorisnika; }

    public long getUkupnoVremeIgranja() { return ukupnoVremeIgranja; }
    public void setUkupnoVremeIgranja(long ukupnoVremeIgranja) { this.ukupnoVremeIgranja = ukupnoVremeIgranja; }

    public long getBrojPokretanja() { return brojPokretanja; }
    public void setBrojPokretanja(long brojPokretanja) { this.brojPokretanja = brojPokretanja; }
}
