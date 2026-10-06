package ac.rs.uns.ftn.Projekat_Web.config;

import ac.rs.uns.ftn.Projekat_Web.model.Kategorija;
import ac.rs.uns.ftn.Projekat_Web.model.Korisnik;
import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Kategorija;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Korisnik;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Igra;
import ac.rs.uns.ftn.Projekat_Web.service.LozinkaUtil;
import ac.rs.uns.ftn.Projekat_Web.service.LozinkaUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.time.LocalDateTime;

@Configuration
public class DatabaseConfiguration {

    @Autowired
    private Repozitorijum_Korisnik korisnikRepository;

    @Autowired
    private Repozitorijum_Kategorija kategorijaRepository;

    @Autowired
    private Repozitorijum_Igra igraRepository;

    @Bean
    public boolean instantiate() {

        // ADMIN NALOG
        if (!korisnikRepository.existsByEmail("admin@gmail.com")) {
            Korisnik admin = new Korisnik();
            admin.setIme("Admin");
            admin.setPrezime("Administrator");
            admin.setEmail("admin@gmail.com");
            admin.setLozinka(LozinkaUtil.hesuj("admin123"));
            admin.setUloga(Korisnik.Uloga.ADMINISTRATOR);
            korisnikRepository.save(admin);
        }

        // KATEGORIJE
        Kategorija idle = new Kategorija();
        idle.setNaziv("Idle");
        idle.setOpis("opustene igrice");

        Kategorija akcija = new Kategorija();
        akcija.setNaziv("Akcija");
        akcija.setOpis("uzbudljive igrice");

        Kategorija logicke = new Kategorija();
        logicke.setNaziv("Logičke");
        logicke.setOpis("igrice za razmišljanje");

        kategorijaRepository.saveAll(List.of(idle, akcija, logicke));

        // IGRE
        Igra igra1 = new Igra(
                "Clicker Heroes",
                "Idle igra od Playsaurus studija",
                "https://cdn.clickerheroes.com/gamebuild/index.php",
                "https://clickerheroes.com/images/logo.png",
                true
        );
        igra1.setKategorija(idle);

        Igra igra2 = new Igra(
                "Poker Quest",
                "Kartaška roguelike igra od Playsaurus studija",
                "https://playsaurus.com/kongPokerQuest63/",
                "https://pokerquest.com/images/logo.png",
                true
        );
        igra2.setKategorija(logicke);

        igraRepository.saveAll(List.of(igra1, igra2));

        return true;
    }
}