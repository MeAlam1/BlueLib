/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.event.mod;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class ExcludedMods {

	@NotNull
	protected Set<String> getBaseExcludedMods() {
		Set<String> excluded = new HashSet<>();
		excluded.add("minecraft");
		excluded.add("neoforge");
		excluded.add("fabric");
		excluded.add("fabric-renderer-api-v1");
		excluded.add("fabric-keybindings-v0");
		excluded.add("fabricloader");
		excluded.add("fabric-transfer-api-v1");
		excluded.add("fabric-dimensions-v1");
		excluded.add("fabric-object-builder-api-v1");
		excluded.add("java");
		excluded.add("fabric-game-rule-api-v1");
		excluded.add("fabric-resource-conditions-api-v1");
		excluded.add("fabric-api-base");
		excluded.add("fabric-sound-api-v1");
		excluded.add("fabric-model-loading-api-v1");
		excluded.add("fabric-rendering-data-attachment-v1");
		excluded.add("mixinextras");
		excluded.add("fabric-data-attachment-api-v1");
		excluded.add("fabric-rendering-fluids-v1");
		excluded.add("fabric-blockrenderlayer-v1");
		excluded.add("fabric-lifecycle-events-v1");
		excluded.add("fabric-renderer-registries-v1");
		excluded.add("fabric-particles-v1");
		excluded.add("fabric-data-generation-api-v1");
		excluded.add("fabric-api-lookup-api-v1");
		excluded.add("fabric-screen-handler-api-v1");
		excluded.add("fabric-command-api-v2");
		excluded.add("fabric-block-api-v1");
		excluded.add("fabric-command-api-v1");
		excluded.add("fabric-screen-api-v1");
		excluded.add("fabric-renderer-indigo");
		excluded.add("fabric-convention-tags-v2");
		excluded.add("fabric-api");
		excluded.add("fabric-events-interaction-v0");
		excluded.add("fabric-item-api-v1");
		excluded.add("fabric-crash-report-info-v1");
		excluded.add("fabric-convention-tags-v1");
		excluded.add("fabric-item-group-api-v1");
		excluded.add("fabric-recipe-api-v1");
		excluded.add("fabric-entity-events-v1");
		excluded.add("fabric-rendering-v0");
		excluded.add("fabric-key-binding-api-v1");
		excluded.add("fabric-rendering-v1");
		excluded.add("fabric-resource-loader-v0");
		excluded.add("fabric-content-registries-v0");
		excluded.add("fabric-block-view-api-v2");
		excluded.add("fabric-biome-api-v1");
		excluded.add("fabric-gametest-api-v1");
		excluded.add("fabric-client-tags-api-v1");
		excluded.add("fabric-loot-api-v3");
		excluded.add("fabric-loot-api-v2");
		excluded.add("fabric-message-api-v1");
		excluded.add("fabric-commands-v0");
		excluded.add("fabric-registry-sync-v0");
		excluded.add("fabric-transitive-access-wideners-v1");
		excluded.add("fabric-networking-api-v1");
		return excluded;
	}

	@NotNull
	public Set<String> getExcludedMods() {
		return getBaseExcludedMods();
	}
}
