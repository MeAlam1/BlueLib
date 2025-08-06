/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.molang.expression.MoLangExpression;

public class MoLang {

	public static @Nullable Object evaluate(@NotNull String pExpression) {
		return evaluate(pExpression, builder -> {});
	}

	public static @Nullable Object evaluate(@NotNull String pExpression, @NotNull Consumer<MoLangRuntimeBuilder> pBuilderConsumer) {
		MoLangExpression parsed = MoLangExpression.parse(pExpression);
		return evaluateInternal(parsed, pBuilderConsumer);
	}

	public static @Nullable Object evaluate(@NotNull MoLangExpression pExpression) {
		return evaluate(pExpression, builder -> {});
	}

	public static @Nullable Object evaluate(@NotNull MoLangExpression pExpression, @NotNull Consumer<MoLangRuntimeBuilder> pBuilderConsumer) {
		return evaluateInternal(pExpression, pBuilderConsumer);
	}

	@ApiStatus.Internal
	private static @Nullable Object evaluateInternal(@NotNull MoLangExpression pExpression, @NotNull Consumer<MoLangRuntimeBuilder> pBuilderConsumer) {
		MoLangRuntimeBuilder builder = new MoLangRuntimeBuilder();
		pBuilderConsumer.accept(builder);
		MoLangRuntime runtime = builder.build();
		return pExpression.evaluate(runtime);
	}
}
