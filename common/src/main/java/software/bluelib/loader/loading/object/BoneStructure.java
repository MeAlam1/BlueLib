package software.bluelib.loader.loading.object;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import software.bluelib.loader.loading.json.raw.Bone;

import java.util.Map;


public record BoneStructure(Bone self, Map<String, BoneStructure> children) {
	public BoneStructure(Bone self) {
		this(self, new Object2ObjectOpenHashMap<>());
	}
}
