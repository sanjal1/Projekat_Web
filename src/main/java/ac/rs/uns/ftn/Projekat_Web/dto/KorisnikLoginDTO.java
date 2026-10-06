package ac.rs.uns.ftn.Projekat_Web.dto;

public class KorisnikLoginDTO {

    private String email;
    private String lozinka;

    public KorisnikLoginDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
