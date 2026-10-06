package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class KorisnikResponseDTO {

    private Long id;
    private String ime;
    private String prezime;
    private String email;
    private LocalDate datumRodjenja;
    private Korisnik.Uloga uloga;
    private String profilnaSlika;
    private LocalDateTime datumRegistracije;
    private boolean blokiran;

    public KorisnikResponseDTO() {}

    // Konstruktor koji prima Korisnik objekat i pretvara ga u DTO
    public KorisnikResponseDTO(Korisnik korisnik) {
        this.id = korisnik.getId();
        this.ime = korisnik.getIme();
        this.prezime = korisnik.getPrezime();
        this.email = korisnik.getEmail();
        this.datumRodjenja = korisnik.getDatumRodjenja();
        this.uloga = korisnik.getUloga();
        String slika = korisnik.getProfilnaSlika();
        this.profilnaSlika = slika != null ? slika.replace("\"", "").trim() : null;
        this.datumRegistracije = korisnik.getDatumRegistracije();
        this.blokiran = korisnik.isBlokiran();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }

    public Korisnik.Uloga getUloga() { return uloga; }
    public void setUloga(Korisnik.Uloga uloga) { this.uloga = uloga; }

    public String getProfilnaSlika() { return profilnaSlika; }
    public void setProfilnaSlika(String profilnaSlika) { this.profilnaSlika = profilnaSlika; }

    public LocalDateTime getDatumRegistracije() { return datumRegistracije; }
    public void setDatumRegistracije(LocalDateTime datumRegistracije) { this.datumRegistracije = datumRegistracije; }

    public boolean isBlokiran() { return blokiran; }
    public void setBlokiran(boolean blokiran) { this.blokiran = blokiran; }
}
