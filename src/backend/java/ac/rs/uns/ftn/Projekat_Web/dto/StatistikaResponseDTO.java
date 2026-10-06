package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Statistika;
import java.time.LocalDateTime;

public class StatistikaResponseDTO {

    private Long id;
    private String korisnikIme;
    private String korisnikPrezime;
    private String igraNaziv;
    private LocalDateTime vremePocetka;
    private LocalDateTime vremeZavrsetka;
    private long trajanjeSekundi;

    public StatistikaResponseDTO() {}

    public StatistikaResponseDTO(Statistika statistika) {
        this.id = statistika.getId();
        this.korisnikIme = statistika.getKorisnik().getIme();
        this.korisnikPrezime = statistika.getKorisnik().getPrezime();
        this.igraNaziv = statistika.getIgra().getNaziv();
        this.vremePocetka = statistika.getVremePocetka();
        this.vremeZavrsetka = statistika.getVremeZavrsetka();
        this.trajanjeSekundi = statistika.trajanjeSekundi();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKorisnikIme() { return korisnikIme; }
    public void setKorisnikIme(String korisnikIme) { this.korisnikIme = korisnikIme; }

    public String getKorisnikPrezime() { return korisnikPrezime; }
    public void setKorisnikPrezime(String korisnikPrezime) { this.korisnikPrezime = korisnikPrezime; }

    public String getIgraNaziv() { return igraNaziv; }
    public void setIgraNaziv(String igraNaziv) { this.igraNaziv = igraNaziv; }

    public LocalDateTime getVremePocetka() { return vremePocetka; }
    public void setVremePocetka(LocalDateTime vremePocetka) { this.vremePocetka = vremePocetka; }

    public LocalDateTime getVremeZavrsetka() { return vremeZavrsetka; }
    public void setVremeZavrsetka(LocalDateTime vremeZavrsetka) { this.vremeZavrsetka = vremeZavrsetka; }

    public long getTrajanjeSekundi() { return trajanjeSekundi; }
    public void setTrajanjeSekundi(long trajanjeSekundi) { this.trajanjeSekundi = trajanjeSekundi; }
}
