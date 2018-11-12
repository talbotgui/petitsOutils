package com.guillaumetalbot.maven_angular_plugin;

import java.io.File;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import junit.framework.Assert;

public class AngularPluginCasErreurTest {

	@Test
	public void test01CasParametreCheminInvalide() {

		//
		final String cheminProjetAngularInvalide = new File("src/test/resources/log4j2.xml").getAbsolutePath();

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			new AngularPlugin(cheminProjetAngularInvalide, null);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertEquals(RuntimeException.class, thrown.getClass());
		Assert.assertTrue(thrown.getMessage().endsWith("n'existe pas"));
	}

	@Test
	public void test02CasParametreCheminSansSrc() {

		//
		final String cheminProjetAngularInvalide = new File("src/test/").getAbsolutePath();

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			new AngularPlugin(cheminProjetAngularInvalide, null);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertEquals(RuntimeException.class, thrown.getClass());
		Assert.assertTrue(thrown.getMessage().endsWith("ne contient pas de répertoire 'src'"));
	}

	@Test
	public void test03CasParametreSuffixeInvalide() {

		//
		final String cheminProjetAngular = new File("src/test/resources").getAbsolutePath();

		//
		final Throwable thrown = Assertions.catchThrowable(() -> {
			new AngularPlugin(cheminProjetAngular, null);
		});

		//
		Assert.assertNotNull(thrown);
		Assert.assertEquals(RuntimeException.class, thrown.getClass());
		Assert.assertTrue(thrown.getMessage().startsWith("Pattern des noms de fichier des composants Angular invalide"));
	}
}
