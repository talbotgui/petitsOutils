package com.guillaumetalbot.maven_angular_plugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AngularPlugin {
	private static final Logger LOG = LoggerFactory.getLogger(AngularPlugin.class);

	/** Analyseurs */
	private final List<Analyseur> analyseurs = Arrays.asList(new MethodesInutiliseesAnalyseur());

	/** Répertoire de base de l'analyse */
	private final File repertoireDebutAnalyse;

	/** Pattern des noms de fichier des composants Angular */
	private final String suffixNomFichierComposantAngular;

	/**
	 * Constructeur du plugin
	 * 
	 * @param cheminProjetAngular
	 *            Paramètre obligatoire
	 * @param suffixNomFichierComposantAngular
	 */
	public AngularPlugin(final String cheminProjetAngular, final String suffixNomFichierComposantAngular) {

		// Validation du chemin du projet
		final File cheminProjetFile = new File(cheminProjetAngular);
		if (!cheminProjetFile.exists() || !cheminProjetFile.isDirectory()) {
			throw new RuntimeException("Le répertoire '" + cheminProjetAngular + "' n'existe pas");
		}

		// Répertoire de source
		final Path cheminRepertoireSource = Paths.get(cheminProjetFile.getAbsolutePath(), "src");
		this.repertoireDebutAnalyse = cheminRepertoireSource.toFile();
		if (!this.repertoireDebutAnalyse.exists() || !this.repertoireDebutAnalyse.isDirectory()) {
			throw new RuntimeException("Le répertoire '" + cheminProjetAngular + "' ne contient pas de répertoire 'src'");
		}

		// Validation du pattern
		if (StringUtils.isEmpty(suffixNomFichierComposantAngular)) {
			throw new RuntimeException("Pattern des noms de fichier des composants Angular invalide ('" + suffixNomFichierComposantAngular + "')");
		}
		this.suffixNomFichierComposantAngular = suffixNomFichierComposantAngular;
	}

	/** Exécution des analyseurs. */
	private boolean analyser(final File fichierAanalyser) {
		boolean resultat = true;
		for (final Analyseur analyseur : this.analyseurs) {
			resultat &= analyseur.analyse(fichierAanalyser);
		}
		return resultat;
	}

	/** Execution de l'analyse */
	public boolean lancerAnalyser() {

		// LOG
		LOG.info("Démarrage de l'analyse du répertoire {}", this.repertoireDebutAnalyse.getAbsolutePath());
		LOG.info("Recherche des fichiers suffixés par '{}' pour analyse", this.suffixNomFichierComposantAngular);

		// Parcours des fichiers et répertoires à la recherche de composants Angular
		final boolean resultat = this.visiterEtAnalyser(this.repertoireDebutAnalyse);

		// LOG
		LOG.info("Fin de l'analyse");

		// Retour
		return resultat;
	}

	/**
	 * Parcours des fichiers (récursive) et analyse
	 *
	 * @param repertoireAanalyser
	 */
	private boolean visiterEtAnalyser(final File repertoireAanalyser) {
		LOG.debug("Visite du répertoire {}", repertoireAanalyser.getAbsolutePath());
		boolean resultat = true;
		for (final File file : repertoireAanalyser.listFiles()) {
			if (file.isDirectory()) {
				resultat &= this.visiterEtAnalyser(file);
			} else if (file.getName().endsWith(this.suffixNomFichierComposantAngular)) {
				resultat &= this.analyser(file);
			}
		}
		return resultat;
	}

}
