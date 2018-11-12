package com.guillaumetalbot.maven_angular_plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

/**
 * Analyse du code d'un projet Angular.
 */
@Mojo(name = "analyse", defaultPhase = LifecyclePhase.COMPILE)
public class MyMojo extends AbstractMojo {

	/** Répertoire de base de l'analyse */
	@Parameter(required = true)
	private String cheminProjetAngular;

	/** Pattern des noms de fichier des composants Angular */
	@Parameter(required = true)
	private String suffixNomFichierComposantAngular;

	@Override
	public void execute() throws MojoExecutionException {

		// Validation
		if (StringUtils.isEmpty(this.cheminProjetAngular)) {
			throw new RuntimeException("Paramètre 'sourceDir' obligatoire (par exemple '${project.src.directory}')");
		}
		if (StringUtils.isEmpty(this.suffixNomFichierComposantAngular)) {
			throw new RuntimeException("Paramètre 'sourceDir' obligatoire (par exemple '.component.ts')");
		}

		// Instanciation
		final AngularPlugin angularPlugin = new AngularPlugin(this.cheminProjetAngular, this.suffixNomFichierComposantAngular);

		// Execution
		if (!angularPlugin.lancerAnalyser()) {
			throw new RuntimeException("Erreur(s) détectée(s) durant l'analyse");
		}
	}
}
