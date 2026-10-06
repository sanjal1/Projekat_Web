package ac.rs.uns.ftn.Projekat_Web.dto;

public class ChatPorukaDTO {

    private Long korisnikId;
    private String korisnikIme;
    private String poruka;
    private String profilnaSlika;

    public ChatPorukaDTO() {
    }

    public Long getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }

    public String getKorisnikIme() {
        return korisnikIme;
    }

    public void setKorisnikIme(String korisnikIme) {
        this.korisnikIme = korisnikIme;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getProfilnaSlika() { return profilnaSlika; }

    public void setProfilnaSlika(String profilnaSlika) { this.profilnaSlika = profilnaSlika; }
}

