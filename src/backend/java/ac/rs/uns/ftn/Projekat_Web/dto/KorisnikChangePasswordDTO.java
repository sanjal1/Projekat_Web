package ac.rs.uns.ftn.Projekat_Web.dto;

public class KorisnikChangePasswordDTO {

    private String staraLozinka;
    private String novaLozinka;

    public KorisnikChangePasswordDTO() {
    }

    public String getStaraLozinka() {
        return staraLozinka;
    }

    public void setStaraLozinka(String staraLozinka) {
        this.staraLozinka = staraLozinka;
    }

    public String getNovaLozinka() {
        return novaLozinka;
    }

    public void setNovaLozinka(String novaLozinka) {
        this.novaLozinka = novaLozinka;
    }
}