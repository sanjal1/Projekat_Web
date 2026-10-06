package ac.rs.uns.ftn.Projekat_Web.dto;

import java.time.LocalDate;

public class KorisnikUpdateProfileDTO {

    private String ime;
    private String prezime;
    private String email;        // ← dodati
    private LocalDate datumRodjenja;
    private String profilnaSlika;

    public KorisnikUpdateProfileDTO() {}

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getEmail() { return email; }        // ← dodati
    public void setEmail(String email) { this.email = email; }  // ← dodati

    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }

    public String getProfilnaSlika() { return profilnaSlika; }
    public void setProfilnaSlika(String profilnaSlika) { this.profilnaSlika = profilnaSlika; }
}
