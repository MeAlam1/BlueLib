/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bluelib.api.annotations.WillBeDeprecated;
import software.bluelib.api.utils.logging.BaseLogLevel;
import software.bluelib.api.utils.logging.BaseLogger;
import software.bluelib.loader.geckolib.constant.dataticket.DataTicket;
import software.bluelib.loader.geckolib.constant.dataticket.SerializableDataTicket;
import software.bluelib.loader.geckolib.data.EntityModelData;

@WillBeDeprecated(since = "2.5.0", reason = "DataTicket will be made redundant with the new MoLang System.")
public final class DataTickets {

	private static final Map<String, SerializableDataTicket<?>> SERIALIZABLE_TICKETS = new ConcurrentHashMap<>();

	public static final DataTicket<BlockEntity> BLOCK_ENTITY = new DataTicket<>("block_entity", BlockEntity.class);
	public static final DataTicket<ItemStack> ITEMSTACK = new DataTicket<>("itemstack", ItemStack.class);
	public static final DataTicket<Entity> ENTITY = new DataTicket<>("entity", Entity.class);
	public static final DataTicket<EquipmentSlot> EQUIPMENT_SLOT = new DataTicket<>("equipment_slot", EquipmentSlot.class);
	public static final DataTicket<EntityModelData> ENTITY_MODEL_DATA = new DataTicket<>("entity_model_data", EntityModelData.class);
	public static final DataTicket<Double> TICK = new DataTicket<>("tick", Double.class);
	public static final DataTicket<ItemDisplayContext> ITEM_RENDER_PERSPECTIVE = new DataTicket<>("item_render_perspective", ItemDisplayContext.class);

	@Nullable
	public static SerializableDataTicket<?> byName(String pId) {
		return SERIALIZABLE_TICKETS.getOrDefault(pId, null);
	}

	public static <D> SerializableDataTicket<D> registerSerializable(SerializableDataTicket<D> pTicket) {
		SerializableDataTicket<?> existingTicket = SERIALIZABLE_TICKETS.putIfAbsent(pTicket.id(), pTicket);

		if (existingTicket != null) {
			BaseLogger.log(BaseLogLevel.ERROR, "Duplicate SerializableDataTicket registered! This will cause issues. Existing: " + existingTicket.id() + ", New: " + pTicket.id());
		}

		return pTicket;
	}
}
