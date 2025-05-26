package software.bluelib.loader.cache.object;

import software.bluelib.loader.loading.json.raw.ModelProperties;

import java.util.List;
import java.util.Optional;


public record BakedGeoModel(List<GeoBone> topLevelBones, ModelProperties properties) {
	
	public Optional<GeoBone> getBone(String name) {
		for (GeoBone bone : this.topLevelBones) {
			GeoBone childBone = searchForChildBone(bone, name);

			if (childBone != null)
				return Optional.of(childBone);
		}

		return Optional.empty();
	}

	
	public GeoBone searchForChildBone(GeoBone parent, String name) {
		if (parent.getName().equals(name))
			return parent;

		for (GeoBone bone : parent.getChildBones()) {
			if (bone.getName().equals(name))
				return bone;

			GeoBone subChildBone = searchForChildBone(bone, name);

			if (subChildBone != null)
				return subChildBone;
		}

		return null;
	}
}
