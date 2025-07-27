/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context;

public class GeneralMoLang extends BaseMoLangContext {

	public GeneralMoLang() {
		registerFunction("print", (arguments, runtime) -> {
			for (Object arg : arguments) {
				System.out.println(arg);
			}
			return null;
		});

		setVariable("true", true);
		setVariable("false", false);
		setVariable("null", null);
		setVariable("undefined", null);
		setVariable("NaN", Double.NaN);
		setVariable("Infinity", Double.POSITIVE_INFINITY);
		setVariable("NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY);
	}
}
