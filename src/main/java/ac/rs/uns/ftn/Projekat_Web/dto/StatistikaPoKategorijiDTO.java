package ac.rs.uns.ftn.Projekat_Web.dto;

public class StatistikaPoKategorijiDTO {

    private String kategorijaNaziv;
    private long ukupnoVremeIgranja;

    public StatistikaPoKategorijiDTO() {}

    public StatistikaPoKategorijiDTO(String kategorijaNaziv, long ukupnoVremeIgranja) {
        this.kategorijaNaziv = kategorijaNaziv;
        this.ukupnoVremeIgranja = ukupnoVremeIgranja;
    }

    public String getKategorijaNaziv() { return kategorijaNaziv; }
    public void setKategorijaNaziv(String naziv) { this.kategorijaNaziv = naziv; }

    public long getUkupnoVremeIgranja() { return ukupnoVremeIgranja; }
    public void setUkupnoVremeIgranja(long vreme) { this.ukupnoVremeIgranja = vreme; }
}