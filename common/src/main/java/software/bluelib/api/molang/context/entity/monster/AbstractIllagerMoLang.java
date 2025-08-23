/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.monster;

import java.util.function.Supplier;
import net.minecraft.world.entity.monster.AbstractIllager;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class AbstractIllagerMoLang extends BaseMoLangContext {

	public AbstractIllagerMoLang(@NotNull Supplier<AbstractIllager> pAbstractIllagerSup) {
		AbstractIllager abstractIllager = pAbstractIllagerSup.get();
		setVariable("get_arm_pose", abstractIllager.getArmPose());
	}
}
