package software.bluelib.api.registry.builders.keybinds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import software.bluelib.BlueLibConstants;
import software.bluelib.api.registry.AbstractRegistryBuilder;

public class KeybindBuilder {

	public static final List<KeybindBuilder> REGISTERED_BUILDERS = new ArrayList<>();
	private final String name;
	private final int keyCode;
	private String category = "key.categories." + AbstractRegistryBuilder.getModID();
	private Supplier<KeyMapping> keyMappingSupplier;

	private KeybindBuilder(String name, int keyCode) {
		this.name = name;
		this.keyCode = keyCode;
	}

	public static KeybindBuilder keybind(String name, int keyCode) {
		return new KeybindBuilder(name, keyCode);
	}

	public KeybindBuilder category(String category) {
		this.category = category;
		return this;
	}

	public Supplier<KeyMapping> register() {
		keyMappingSupplier = BlueLibConstants.PlatformHelper.REGISTRY.registerKeybind(name, () -> new KeyMapping(
				"key." + AbstractRegistryBuilder.getModID() + "." + name,
				keyCode,
				category));

		REGISTERED_BUILDERS.add(this);
		return keyMappingSupplier;
	}

	public String getName() {
		return name;
	}

	public Supplier<KeyMapping> getKeyMapping() {
		return keyMappingSupplier;
	}
}
