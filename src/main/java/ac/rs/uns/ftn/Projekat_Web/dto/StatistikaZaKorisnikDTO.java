package ac.rs.uns.ftn.Projekat_Web.dto;

import java.util.List;

public class StatistikaZaKorisnikDTO {

    // Specifikacija: "Ukupno vreme igranja"
    private long ukupnoVremeIgranja;

    // Specifikacija: "Najigranije igrice (Vreme igranja za svaku igru)"
    // + "Broj pokretanja igrica"
    private List<StatistikaNajigranijaIgraDTO> statistikaPoIgrama;

    // Specifikacija: "Ukupno vreme igranja po kategorijama"
    private List<StatistikaPoKategorijiDTO> statistikaPoKategorijama;

    public StatistikaZaKorisnikDTO() {}

    public StatistikaZaKorisnikDTO(long ukupnoVremeIgranja,
                              List<StatistikaNajigranijaIgraDTO> statistikaPoIgrama,
                              List<StatistikaPoKategorijiDTO> statistikaPoKategorijama) {
        this.ukupnoVremeIgranja = ukupnoVremeIgranja;
        this.statistikaPoIgrama = statistikaPoIgrama;
        this.statistikaPoKategorijama = statistikaPoKategorijama;
    }

    public long getUkupnoVremeIgranja() { return ukupnoVremeIgranja; }
    public void setUkupnoVremeIgranja(long ukupnoVremeIgranja) { this.ukupnoVremeIgranja = ukupnoVremeIgranja; }

    public List<StatistikaNajigranijaIgraDTO> getStatistikaPoIgrama() { return statistikaPoIgrama; }
    public void setStatistikaPoIgrama(List<StatistikaNajigranijaIgraDTO> statistikaPoIgrama) { this.statistikaPoIgrama = statistikaPoIgrama; }

    public List<StatistikaPoKategorijiDTO> getStatistikaPoKategorijama() { return statistikaPoKategorijama; }
    public void setStatistikaPoKategorijama(List<StatistikaPoKategorijiDTO> statistikaPoKategorijama) { this.statistikaPoKategorijama = statistikaPoKategorijama; }
}
