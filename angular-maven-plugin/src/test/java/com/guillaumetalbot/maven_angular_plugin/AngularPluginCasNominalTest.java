package com.guillaumetalbot.maven_angular_plugin;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class AngularPluginCasNominalTest {
	@SuppressWarnings("unchecked")
	@Test
	public void test01CasNominal() {

		//
		final String suffixNomFichierComposantAngular = ".component.ts";
		final String cheminProjetAngular = new File("src/test/resources").getAbsolutePath();
		String cheminFichierAvecUneErreur = cheminProjetAngular + "/src/administration/page-utilisateur/page-utilisateur.component.ts";
		if (File.separator.equals("\\")) {
			cheminFichierAvecUneErreur = cheminFichierAvecUneErreur.replaceAll("/", "\\\\");
		}

		final ArgumentCaptor<String> argument1 = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Object> argument2 = ArgumentCaptor.forClass(Object.class);
		final ArgumentCaptor<Object> argument3 = ArgumentCaptor.forClass(Object.class);
		final Logger monLogger = Mockito.mock(Logger.class);
		PowerMockito.mockStatic(LoggerFactory.class);
		PowerMockito.when(LoggerFactory.getLogger(Mockito.any(Class.class))).thenReturn(monLogger);

		//
		final boolean resultat = new AngularPlugin(cheminProjetAngular, suffixNomFichierComposantAngular).lancerAnalyser();

		//
		Assert.assertFalse(resultat);
		Mockito.verify(monLogger).error(argument1.capture(), argument2.capture(), argument3.capture());
		Assert.assertEquals(cheminFichierAvecUneErreur, argument2.getValue());
		Assert.assertEquals(1, ((Collection<String>) argument3.getValue()).size());
	}
}
