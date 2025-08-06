package software.bluelib.api.registry.datagen.items;

import com.google.gson.JsonElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.datagen.DataGenUtils;

public class ItemModelGenerator extends DataGenUtils {

	public static void generateItemModel(String modId, String name, ItemModelTemplates modelTemplate) {
		Path itemModelPath = Path.of(BlueLibConstants.PlatformHelper.PLATFORM.getAssetsDir(true) + "/models/item/" + name + ".json");

		try {
			if (Files.exists(itemModelPath)) {
				System.out.println("Item model for '" + name + "' already exists at: " + itemModelPath + ". Skipping creation.");
				return;
			}

			JsonElement modelJson = generateModelJson(modId, name, modelTemplate);

			Files.createDirectories(itemModelPath.getParent());
			Files.write(itemModelPath, GSON.toJson(modelJson).getBytes(), StandardOpenOption.CREATE_NEW);
			System.out.println("Item model for '" + name + "' created at: " + itemModelPath);

		} catch (IOException e) {
			System.err.println("Failed [ERROR]: Failed to create item model for '" + name + "' at " + itemModelPath + ": " + e.getMessage());
		}
	}

	private static JsonElement generateModelJson(String modId, String name, ItemModelTemplates modelTemplate) {
		ResourceLocation modelLocation = ResourceLocation.fromNamespaceAndPath(modId, "item/" + name);
		ModelTemplate template = modelTemplate.getTemplate();

		final JsonElement[] capturedJson = new JsonElement[1];
		BiConsumer<ResourceLocation, Supplier<JsonElement>> tempConsumer = (location, jsonSupplier) -> {
			capturedJson[0] = jsonSupplier.get();
			System.out.println("Generated JSON for '" + location + "':\n" + GSON.toJson(capturedJson[0]));
		};

		template.create(modelLocation, TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(modId, "item/" + name)), tempConsumer);
		return capturedJson[0];
	}
}
