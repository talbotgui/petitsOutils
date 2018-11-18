package com.guillaumetalbot.maven_angular_plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodesInutiliseesAnalyseur implements Analyseur {

	private static final String CLEF_POUR_IGNORER_LA_LIGNE_SUIVANTE = "@@angular:analyse:ignorerLigneSuivante@@";

	private static final Logger LOG = LoggerFactory.getLogger(MethodesInutiliseesAnalyseur.class);

	// Méthodes propres à Angular à ignorer
	private static final List<String> METHODES_ANGULAR_A_IGNORER = Arrays.asList("constructor", "ngOnInit", "ngAfterViewInit", "ngOnChanges");

	// Pattern de détection des méthodes
	public static final Pattern PATTERN_METHODE_DANS_HTML = Pattern.compile("[\\(\\[][a-z]*[\\)\\]]=\"([a-zA-Z0-9]*)\\(");
	public static final String PATTERN_METHODE_DANS_HTML_REMPLACEMENT = "\"%s\\(";

	// Pattern de détection des méthodes
	private static final Pattern PATTERN_METHODE_DANS_TS = Pattern.compile("^  ([a-zA-Z0-9]*)\\(");

	@Override
	public boolean analyse(final File fichierComposantTs) {

		// Recherche du fichier HTML
		final File fichierHtml = new File(fichierComposantTs.getAbsolutePath().replace(".ts", ".html"));

		// Si pas de fichier HTML, on s'arrête là
		if (!fichierHtml.exists() || !fichierHtml.isFile()) {
			return true;
		}

		// LOG
		LOG.debug("Analyse du composant graphique TS {} avec l'HTML {}", fichierComposantTs.getAbsolutePath(), fichierHtml.getAbsolutePath());

		// Recherche des méthodes dans le composant Ts
		final List<String> methodesDansLeTs = this.extraireMethodes(fichierComposantTs, PATTERN_METHODE_DANS_TS, null);
		methodesDansLeTs.removeAll(METHODES_ANGULAR_A_IGNORER);

		// Recherche des appels de méthode dans la page HTML
		final List<String> appelsDepuisPageHtml = this.extraireMethodes(fichierHtml, PATTERN_METHODE_DANS_HTML,
				PATTERN_METHODE_DANS_HTML_REMPLACEMENT);

		// Soustraction
		methodesDansLeTs.removeAll(appelsDepuisPageHtml);

		// Log des méthodes en trop
		if (!methodesDansLeTs.isEmpty()) {
			LOG.error("{} : les méthodes de controleur suivantes ne sont pas utilisées depuis une page WEB et doivent être privées : {}",
					fichierComposantTs.getAbsolutePath(), methodesDansLeTs);
			return false;
		} else {
			return true;
		}
	}

	private List<String> extraireMethodes(final File fichierAanalyser, final Pattern patternDetection, final String patterRemplacement) {

		// Lecture du fichier
		List<String> lignesDeLaPage = null;
		try {
			lignesDeLaPage = Files.readAllLines(Paths.get(fichierAanalyser.toURI()), Charset.forName("UTF-8"));
		} catch (final IOException e) {
			throw new RuntimeException("Impossible de lire le fichier " + fichierAanalyser.getAbsolutePath(), e);
		}

		// Parcours du fichier (sauf les lignes à ignorer)
		final List<String> methodesAppellees = new ArrayList<>();
		boolean ligneSuivanteAignorer = false;
		for (final String ligne : lignesDeLaPage) {
			if (ligneSuivanteAignorer) {
				ligneSuivanteAignorer = false;
			} else {
				this.extraireMethodesDuneLigne(ligne, patternDetection, patterRemplacement, methodesAppellees);
				ligneSuivanteAignorer = this.ligneSuivanteAignorer(ligne);
			}
		}

		// Renvoi
		return methodesAppellees;
	}

	/**
	 * Ajoute les méthodes trouvées dans une ligne dans la liste 'methodesApppellees'
	 *
	 * @param ligne
	 * @param patternDetection
	 * @param patternRemplacement
	 * @param methodesAppellees
	 */
	private void extraireMethodesDuneLigne(String ligne, final Pattern patternDetection, final String patternRemplacement,
			final List<String> methodesAppellees) {
		Matcher matcher;
		do {
			// On tente de trouver un element correspondant au pattern
			matcher = patternDetection.matcher(ligne);

			// Si on en a un
			if (matcher.find()) {

				// Ajout à la liste
				methodesAppellees.add(matcher.group(1));

				// On supprime l'element trouvé pour une nouvelle tentative
				if (patternRemplacement != null) {
					ligne = ligne.replaceFirst(String.format(patternRemplacement, matcher.group(1)), "");
				}
			}

		} while (matcher.find() && patternRemplacement != null);
	}

	/**
	 * Détection si la ligne courante demande à ignorer la ligne suivante
	 * 
	 * @param ligne
	 * @return
	 */
	private boolean ligneSuivanteAignorer(final String ligne) {
		return ligne.contains(CLEF_POUR_IGNORER_LA_LIGNE_SUIVANTE);
	}
}
