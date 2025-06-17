package software.bluelib.api.molang;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record MoLangType(String id) {

	private static final Map<String, MoLangType> REGISTRY = new LinkedHashMap<>();

	public MoLangType(String id) {
		this.id = id;
		if (REGISTRY.containsKey(id)) {
			throw new IllegalArgumentException("MoLangType with id '" + id + "' is already registered.");
		}
		REGISTRY.put(id, this);
	}

	public static MoLangType byId(String pId) {
		return REGISTRY.get(pId);
	}

	public static Collection<MoLangType> values() {
		return Collections.unmodifiableCollection(REGISTRY.values());
	}

	@Override
	public @NotNull String toString() {
		return id;
	}

	public static final MoLangType GENERAL = new MoLangType("general");
	public static final MoLangType MATH = new MoLangType("math");
}
