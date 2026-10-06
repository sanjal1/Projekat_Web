package ac.rs.uns.ftn.Projekat_Web.config;

import ac.rs.uns.ftn.Projekat_Web.model.Igra;
import ac.rs.uns.ftn.Projekat_Web.repository.Repozitorijum_Igra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class GameDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private Repozitorijum_Igra igraRepo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ucitajIgre();
    }

    private void ucitajIgre() {
        try {
            ClassPathResource resource = new ClassPathResource("games");
            File gamesFolder = resource.getFile();

            if (!gamesFolder.exists() || !gamesFolder.isDirectory()) {
                System.out.println("Folder 'games' nije pronađen.");
                return;
            }

            File[] folderi = gamesFolder.listFiles(File::isDirectory);
            if (folderi == null) return;

            for (File folder : folderi) {
                String ime = folder.getName();
                String naziv = formatNaziv(ime);
                String url = "games/" + ime + "/index.html";

                //preskoci ako nema index.html
                if (!new File(folder, "index.html").exists()) {
                    System.out.println("Ne postoji index.html: " + ime);
                    continue;
                }

                //pronadji sliku u folderu
                File[] slike = folder.listFiles((dir, name) ->
                        name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
                String slikaPath = (slike != null && slike.length > 0)
                        ? "games/" + ime + "/" + slike[0].getName()
                        : "";

                //ako vec postoji - azuriraj sliku ako je URL
                if (igraRepo.existsByNazivIgnoreCase(naziv)) {
                    igraRepo.findByNazivIgnoreCase(naziv).ifPresent(postojeca -> {
                        if (postojeca.getSlika() == null || postojeca.getSlika().startsWith("http")) {
                            postojeca.setSlika(slikaPath);
                            igraRepo.save(postojeca);
                            System.out.println("Ažurirana slika: " + naziv);
                        } else {
                            System.out.println("Već postoji: " + naziv);
                        }
                    });
                    continue;
                }

                //nova igrica
                Igra igra = new Igra();
                igra.setNaziv(naziv);
                igra.setOpis("Opis igrice " + naziv);
                igra.setURL(url);
                igra.setSlika(slikaPath);
                igra.setAktivna(true);

                igraRepo.save(igra);
                System.out.println("Učitana igra: " + igra.getNaziv());
            }

        } catch (IOException e) {
            System.err.println("Greška u učitavanju: " + e.getMessage());
        }
    }


    private String formatNaziv(String folderName) {
        return Arrays.stream(folderName.split("[-_]"))
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                .collect(Collectors.joining(" "));
    }
}