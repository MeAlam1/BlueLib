/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.registry.datagen.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import software.bluelib.api.registry.AbstractRegistryBuilder;
import software.bluelib.api.registry.datagen.DataGenUtils;

public class EntityTagBuilder extends DataGenUtils {

	private static final List<String> generatedTags = new ArrayList<>();
	private static final Map<String, List<EntityType<?>>> tagEntityTypes = new HashMap<>();
	private final String name;
	private final List<EntityType<?>> entityTypes = new ArrayList<>();

	private EntityTagBuilder(String name) {
		this.name = name;
	}

	public static EntityTagBuilder createEntityTag(String name) {
		return new EntityTagBuilder(name);
	}

	public EntityTagBuilder addEntries(EntityType<?>... entityTypes) {
		this.entityTypes.addAll(Arrays.asList(entityTypes));
		return this;
	}

	public TagKey<EntityType<?>> build() {
		generatedTags.add(name);
		tagEntityTypes.put(name, new ArrayList<>(entityTypes));
		return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(AbstractRegistryBuilder.getModID(), name));
	}

	public static void doTagJsonGen(String modId) {
		for (String tagName : generatedTags) {
			List<EntityType<?>> entities = tagEntityTypes.getOrDefault(tagName, new ArrayList<>());
			generateTagJson(modId, tagName, entities);
		}
	}

	private static void generateTagJson(String modId, String tagName, List<EntityType<?>> entityTypes) {
		Path tagPath = findProjectRoot().resolve(modId + "/tags/entity_type/" + tagName + ".json");

		try {
			if (Files.exists(tagPath)) {
				System.out.println("Entity tag for '" + tagName + "' already exists at: " + tagPath + ". Skipping creation.");
				return;
			}

			JsonObject tagJson = new JsonObject();
			JsonArray values = new JsonArray();

			for (EntityType<?> entityType : entityTypes) {
				ResourceLocation registryName = EntityType.getKey(entityType);
				if (registryName != null) {
					values.add(registryName.toString());
				}
			}

			tagJson.add("values", values);

			Files.createDirectories(tagPath.getParent());
			Files.write(tagPath, GSON.toJson(tagJson).getBytes(), StandardOpenOption.CREATE_NEW);
			System.out.println("Entity tag for '" + tagName + "' created at: " + tagPath);

		} catch (IOException e) {
			System.err.println("Failed [ERROR]: Failed to create entity tag for '" + tagName + "' at " + tagPath + ": " + e.getMessage());
		}
	}

	public static Path findProjectRoot() {
		Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
		while (current != null) {
			Path resources = findResourcesPath(current);
			if (resources != null) return resources;
			current = current.getParent();
		}
		throw new IllegalStateException("Could not locate project root");
	}

	private static Path findResourcesPath(Path current) {
		String[] potentialPaths = {
				"src/main/resources/data",
				"common/src/main/resources/data"
		};

		for (String path : potentialPaths) {
			Path resources = current.resolve(path);
			if (Files.exists(resources) && Files.isDirectory(resources)) {
				return resources;
			}
		}

		String currentDirName = current.getFileName() != null ? current.getFileName().toString() : "";
		if (currentDirName.matches("fabric|forge|neoforge|quilt")) {
			for (String path : potentialPaths) {
				Path parentResources = current.getParent().resolve(path);
				if (Files.exists(parentResources) && Files.isDirectory(parentResources)) {
					return parentResources;
				}
			}
		}

		return null;
	}
}
