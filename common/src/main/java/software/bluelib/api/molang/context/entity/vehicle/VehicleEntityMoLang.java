/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.molang.context.entity.vehicle;

import java.util.function.Supplier;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import software.bluelib.api.molang.context.BaseMoLangContext;

public class VehicleEntityMoLang extends BaseMoLangContext {

	public VehicleEntityMoLang(@NotNull Supplier<VehicleEntity> pVehicleEntitySup) {
		VehicleEntity vehicleEntity = pVehicleEntitySup.get();
		setVariable("get_damage", vehicleEntity.getDamage());
		setVariable("get_hurt_dir", vehicleEntity.getHurtDir());
		setVariable("get_hurt_time", vehicleEntity.getHurtTime());
		registerFunction("destroy", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Item dropItem)) {
				return false;
			}
			vehicleEntity.destroy(dropItem);
			return true;
		});

		registerFunction("set_damage", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number num)) {
				return vehicleEntity.getDamage();
			}
			float damage = num.floatValue();
			vehicleEntity.setDamage(damage);
			return vehicleEntity.getDamage();
		});
		registerFunction("set_hurt_dir", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number num)) {
				return vehicleEntity.getHurtDir();
			}
			int hurtDir = num.intValue();
			vehicleEntity.setHurtDir(hurtDir);
			return vehicleEntity.getHurtDir();
		});
		registerFunction("set_hurt_time", (args, runtime) -> {
			if (args.size() != 1 || !(args.getFirst() instanceof Number num)) {
				return vehicleEntity.getHurtTime();
			}
			int hurtTime = num.intValue();
			vehicleEntity.setHurtTime(hurtTime);
			return vehicleEntity.getHurtTime();
		});
	}
}
