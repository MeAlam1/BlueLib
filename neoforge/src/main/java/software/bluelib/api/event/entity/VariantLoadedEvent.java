/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.entity;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class VariantLoadedEvent extends Event implements IModBusEvent {

	@NotNull
	final String entityName;
	@NotNull
	final String variant;

	public VariantLoadedEvent(@NotNull String pEntityName, @NotNull String pVariant) {
		super();
		this.entityName = pEntityName;
		this.variant = pVariant;
	}

	@NotNull
	public String getEntity() {
		return entityName;
	}

	@NotNull
	public String getVariant() {
		return variant;
	}

	public static class Pre extends VariantLoadedEvent implements ICancellableEvent {

		public Pre(@NotNull String pEntityName, @NotNull String pVariant) {
			super(pEntityName, pVariant);
		}
	}

	public static class Post extends VariantLoadedEvent {

		public Post(@NotNull String pEntityName, @NotNull String pVariant) {
			super(pEntityName, pVariant);
		}
	}
}
