package fr.pokemon.parser.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RepertoireOutput {

    File subDirVersion;
    File allDir;
    File diffDir;

    public RepertoireOutput(File outputDir) throws IOException {
        this(outputDir, getNameSubDirCurrentVersion());
    }

    public RepertoireOutput(File outputDir, String subDirName) throws IOException {
        // Créer le repertoire de la version courante s'il n'existe pas
        subDirVersion = new File(outputDir, subDirName);
        if (!subDirVersion.exists() && !subDirVersion.mkdir()) {
            throw new IOException("Impossible de créer le répertoire output pour la version " + subDirName);
        }
        // Créer le sous répertoire "all" de la version courante
        allDir = new File(subDirVersion, "all");
        if (!allDir.exists() && !allDir.mkdir()) {
            throw new IOException("Impossible de créer le répertoire 'all' pour la version " + subDirName);
        }
        // Créer le sous répertoire "diff" de la version courante
        diffDir = new File(subDirVersion, "diff");
        if (!diffDir.exists() && !diffDir.mkdir()) {
            throw new IOException("Impossible de créer le répertoire 'diff' pour la version " + subDirName);
        }
    }

    public static String getNomSubDirLatestVersion(File outputDir) {
        String nameSubDirCurrentVersion = getNameSubDirCurrentVersion();
        Pattern patternDirHisto = Pattern.compile("\\d{4}_\\d{2}_\\d{2}");
        File[] lstDirHisto = outputDir.listFiles((dir) -> dir.isDirectory() && patternDirHisto.matcher(dir.getName()).matches() && !nameSubDirCurrentVersion.equals(dir.getName()));
        File latestDirHisto = null;
        if (lstDirHisto != null) {
            latestDirHisto = Stream.of(lstDirHisto).max(Comparator.comparing(File::getName)).orElse(null);
        }
        return latestDirHisto != null ? latestDirHisto.getName() : null;
    }

    public static String getNameSubDirCurrentVersion() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }
}
