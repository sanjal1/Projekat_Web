package ac.rs.uns.ftn.Projekat_Web.dto;

import ac.rs.uns.ftn.Projekat_Web.model.Recenzija;

public class RecenzijaDTO {
    private String korisnik;
    private int ocena;
    private String komentar;

    public RecenzijaDTO() {}

    public RecenzijaDTO(Recenzija r) {
        this.korisnik = r.getKorisnik() != null ? r.getKorisnik().getIme() + " " + r.getKorisnik().getPrezime() : null;
        this.ocena = r.getOcena();
        this.komentar = r.getKomentar();
    }


    public String getKorisnik() { return korisnik; }

    public void setKorisnik(String korisnik) { this.korisnik = korisnik; }

    public int getOcena() { return ocena; }

    public void setOcena(int ocena) { this.ocena = ocena; }

    public String getKomentar() { return komentar; }

    public void setKomentar(String komentar) { this.komentar = komentar; }

}
