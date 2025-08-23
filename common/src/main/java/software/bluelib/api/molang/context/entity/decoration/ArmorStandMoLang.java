/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.decoration;

import java.util.function.Supplier;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class ArmorStandMoLang extends BaseMoLangContext {

	public ArmorStandMoLang(@NotNull Supplier<ArmorStand> pArmorStandSup) {
		ArmorStand armorStand = pArmorStandSup.get();
		setVariable("wobble_time", ArmorStand.WOBBLE_TIME);
		setVariable("disable_taking_offset", ArmorStand.DISABLE_TAKING_OFFSET);
		setVariable("disable_putting_offset", ArmorStand.DISABLE_PUTTING_OFFSET);
		setVariable("client_flag_small", ArmorStand.CLIENT_FLAG_SMALL);
		setVariable("client_flag_show_arms", ArmorStand.CLIENT_FLAG_SHOW_ARMS);
		setVariable("client_flag_no_baseplate", ArmorStand.CLIENT_FLAG_NO_BASEPLATE);
		setVariable("client_flag_marker", ArmorStand.CLIENT_FLAG_MARKER);
		setVariable("data_client_flags", ArmorStand.DATA_CLIENT_FLAGS);
		setVariable("data_head_pose", ArmorStand.DATA_HEAD_POSE);
		setVariable("data_body_pose", ArmorStand.DATA_BODY_POSE);
		setVariable("data_left_arm_pose", ArmorStand.DATA_LEFT_ARM_POSE);
		setVariable("data_right_arm_pose", ArmorStand.DATA_RIGHT_ARM_POSE);
		setVariable("data_left_leg_pose", ArmorStand.DATA_LEFT_LEG_POSE);
		setVariable("data_right_leg_pose", ArmorStand.DATA_RIGHT_LEG_POSE);
		setVariable("last_hit", armorStand.lastHit);

		setVariable("is_marker", armorStand.isMarker());
		setVariable("is_small", armorStand.isSmall());
		setVariable("is_show_arms", armorStand.isShowArms());
		setVariable("is_no_base_plate", armorStand.isNoBasePlate());
		setVariable("get_head_pose", armorStand.getHeadPose());
		setVariable("get_body_pose", armorStand.getBodyPose());
		setVariable("get_left_arm_pose", armorStand.getLeftArmPose());
		setVariable("get_right_arm_pose", armorStand.getRightArmPose());
		setVariable("get_left_leg_pose", armorStand.getLeftLegPose());
		setVariable("get_right_leg_pose", armorStand.getRightLegPose());

		registerFunction("set_show_arms", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean bool)) {
				return armorStand.isShowArms();
			}
			armorStand.setShowArms(bool);
			return armorStand.isShowArms();
		});

		registerFunction("set_no_base_plate", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Boolean bool)) {
				return armorStand.isNoBasePlate();
			}
			armorStand.setNoBasePlate(bool);
			return armorStand.isNoBasePlate();
		});

		registerFunction("set_head_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Rotations rot)) {
				return armorStand.getHeadPose();
			}
			armorStand.setHeadPose(rot);
			return armorStand.getHeadPose();
		});

		registerFunction("set_body_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Rotations rot)) {
				return armorStand.getBodyPose();
			}
			armorStand.setBodyPose(rot);
			return armorStand.getBodyPose();
		});

		registerFunction("set_left_arm_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Rotations rot)) {
				return armorStand.getLeftArmPose();
			}
			armorStand.setLeftArmPose(rot);
			return armorStand.getLeftArmPose();
		});

		registerFunction("set_right_arm_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Rotations rot)) {
				return armorStand.getRightArmPose();
			}
			armorStand.setRightArmPose(rot);
			return armorStand.getRightArmPose();
		});

		registerFunction("set_left_leg_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Rotations rot)) {
				return armorStand.getLeftLegPose();
			}
			armorStand.setLeftLegPose(rot);
			return armorStand.getLeftLegPose();
		});

		registerFunction("set_right_leg_pose", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Rotations rot)) {
				return armorStand.getRightLegPose();
			}
			armorStand.setRightLegPose(rot);
			return armorStand.getRightLegPose();
		});
	}
}
