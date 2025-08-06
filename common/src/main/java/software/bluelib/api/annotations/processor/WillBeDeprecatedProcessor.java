/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.annotations.processor;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import software.bluelib.api.annotations.WillBeDeprecated;

@SupportedAnnotationTypes("software.bluelib.api.annotations.WillBeDeprecated")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class WillBeDeprecatedProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> pAnnotations, RoundEnvironment pRoundEnv) {
		for (Element e : pRoundEnv.getElementsAnnotatedWith(WillBeDeprecated.class)) {
			WillBeDeprecated ann = e.getAnnotation(WillBeDeprecated.class);
			if (ann.showWarning()) {
				processingEnv.getMessager().printMessage(
						Diagnostic.Kind.WARNING,
						"Element " + e.getSimpleName() + " is planned for future deprecation: " + ann.reason(),
						e);
			}
		}
		return false;
	}
}
