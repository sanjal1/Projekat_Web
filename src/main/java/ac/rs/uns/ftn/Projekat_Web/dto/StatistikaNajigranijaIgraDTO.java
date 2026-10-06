package ac.rs.uns.ftn.Projekat_Web.dto;

public class StatistikaNajigranijaIgraDTO {

    private Long igraId;
    private String nazivIgre;
    private long ukupnoVremeIgranja;
    private long brojPokretanja;

    public StatistikaNajigranijaIgraDTO() {}

    public StatistikaNajigranijaIgraDTO(Long igraId, String nazivIgre,
                              long ukupnoVremeIgranja, long brojPokretanja) {
        this.igraId = igraId;
        this.nazivIgre = nazivIgre;
        this.ukupnoVremeIgranja = ukupnoVremeIgranja;
        this.brojPokretanja = brojPokretanja;
    }

    public Long getIgraId() { return igraId; }
    public void setIgraId(Long igraId) { this.igraId = igraId; }

    public String getNazivIgre() { return nazivIgre; }
    public void setNazivIgre(String nazivIgre) { this.nazivIgre = nazivIgre; }

    public long getUkupnoVremeIgranja() { return ukupnoVremeIgranja; }
    public void setUkupnoVremeIgranja(long ukupnoVremeIgranja) { this.ukupnoVremeIgranja = ukupnoVremeIgranja; }

    public long getBrojPokretanja() { return brojPokretanja; }
    public void setBrojPokretanja(long brojPokretanja) { this.brojPokretanja = brojPokretanja; }
}