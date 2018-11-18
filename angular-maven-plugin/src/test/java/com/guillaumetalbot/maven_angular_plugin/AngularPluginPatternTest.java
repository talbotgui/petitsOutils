package com.guillaumetalbot.maven_angular_plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class AngularPluginPatternTest {

	@Test
	public void testTousLesCas()
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// Arrange : liste des cas de test
		final Map<String, List<String>> tests = new HashMap<>();
		tests.put("<mat-checkbox [checked]=\"estAutorise(ro, ressource)\"", Arrays.asList("estAutorise"));
		tests.put("<mat-checkbox (change)=\"changerAutorisation(ro, ressource)\"></mat-checkbox>", Arrays.asList("changerAutorisation"));
		tests.put("<mat-checkbox [checked]=\"estAutorise(ro, ressource)\" (change)=\"changerAutorisation(ro, ressource)\"></mat-checkbox>",
				Arrays.asList("estAutorise", "changerAutorisation"));
		tests.put("<em (click)=\"creer(); leForm.reset(roleSelectionne);\"", Arrays.asList("creer"));
		// Arrange : methode à appeler
		final MethodesInutiliseesAnalyseur analyseur = new MethodesInutiliseesAnalyseur();
		final Method method = analyseur.getClass().getDeclaredMethod("extraireMethodesDuneLigne", String.class, Pattern.class, String.class,
				List.class);
		method.setAccessible(true);

		// Act & Assert
		for (final Map.Entry<String, List<String>> entree : tests.entrySet()) {
			final String ligne = entree.getKey();
			final List<String> methodes = entree.getValue();
			final List<String> listeResultats = new ArrayList<>();

			method.invoke(analyseur, ligne, MethodesInutiliseesAnalyseur.PATTERN_METHODE_DANS_HTML,
					MethodesInutiliseesAnalyseur.PATTERN_METHODE_DANS_HTML_REMPLACEMENT, listeResultats);

			Assertions.assertThat(methodes).containsExactlyElementsOf(listeResultats);
		}
	}
}
