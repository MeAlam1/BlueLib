/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.*;

public class MoLangService {

	@NotNull
	private final Map<MoLangType, MoLangRuntime> runtimes = new HashMap<>();
	@NotNull
	private final CompositeMoLangContext sharedComposite = new CompositeMoLangContext();

	public MoLangService() {
		Map<MoLangType, Supplier<BaseMoLangContext>> contextFactories = Map.of(
				MoLangType.GENERAL, GeneralMoLang::new,
				MoLangType.MATH, MathMoLang::new,
				MoLangType.OPERATOR, OperatorMoLang::new);

		for (MoLangType type : MoLangType.values()) {
			MoLangRuntime runtime = new MoLangRuntime();
			register(type, runtime);

			Supplier<BaseMoLangContext> factory = contextFactories.get(type);
			if (factory != null) {
				BaseMoLangContext ctx = factory.get();
				runtime.registerContext(type.id(), ctx);
				sharedComposite.addContext(ctx);
			}
		}

		MoLangRuntime qRuntime = getRuntimeFor(MoLangType.ANIMATABLE);
		qRuntime.registerContext(MoLangType.ANIMATABLE.id(), sharedComposite);
	}

	public void register(@NotNull MoLangType pType, @NotNull MoLangRuntime pRuntime) {
		runtimes.put(pType, pRuntime);
	}

	public void registerContextToComposite(@NotNull MoLangType pType, @NotNull MoLangContext pContext) {
		getRuntimeFor(pType).registerContext(pType.id(), pContext);
		sharedComposite.addContext(pContext);
	}

	public @NotNull MoLangRuntime getRuntimeFor(@NotNull MoLangType pType) {
		MoLangRuntime runtime = runtimes.get(pType);
		if (runtime == null) {
			throw new IllegalArgumentException("No MoLangRuntime registered for type: " + pType.id());
		}
		return runtime;
	}

	public @NotNull CompositeMoLangContext getSharedComposite() {
		return sharedComposite;
	}
}
