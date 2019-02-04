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
		String cheminFichierAvecUneErreur1 = cheminProjetAngular + "/src/administration/page-client/page-client.component.ts";
		String cheminFichierAvecUneErreur2 = cheminProjetAngular + "/src/administration/page-utilisateur/page-utilisateur.component.ts";
		if (File.separator.equals("\\")) {
			cheminFichierAvecUneErreur1 = cheminFichierAvecUneErreur1.replaceAll("/", "\\\\");
			cheminFichierAvecUneErreur2 = cheminFichierAvecUneErreur2.replaceAll("/", "\\\\");
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
		Mockito.verify(monLogger, Mockito.times(2)).error(argument1.capture(), argument2.capture(), argument3.capture());

		Assert.assertTrue(argument2.getAllValues().contains(cheminFichierAvecUneErreur1));
		Assert.assertTrue(argument2.getAllValues().contains(cheminFichierAvecUneErreur2));
		Assert.assertEquals(1, ((Collection<String>) argument3.getAllValues().get(0)).size());
		Assert.assertEquals(1, ((Collection<String>) argument3.getAllValues().get(1)).size());

	}
}
