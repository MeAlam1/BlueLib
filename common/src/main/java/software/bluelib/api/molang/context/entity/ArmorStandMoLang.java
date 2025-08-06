/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity;

import java.util.function.Supplier;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class ArmorStandMoLang extends BaseMoLangContext {

	public ArmorStandMoLang(@NotNull Supplier<ArmorStand> pArmorStand) {
		setVariable("is_marker", pArmorStand.get().isMarker());
		setVariable("is_small", pArmorStand.get().isSmall());
		setVariable("is_show_arms", pArmorStand.get().isShowArms());
		setVariable("get_head_pose", pArmorStand.get().getHeadPose());
		setVariable("get_body_pose", pArmorStand.get().getBodyPose());
		setVariable("get_left_arm_pose", pArmorStand.get().getLeftArmPose());
		setVariable("get_right_arm_pose", pArmorStand.get().getRightArmPose());
		setVariable("get_left_leg_pose", pArmorStand.get().getLeftLegPose());
		setVariable("get_right_leg_pose", pArmorStand.get().getRightLegPose());
	}
}
