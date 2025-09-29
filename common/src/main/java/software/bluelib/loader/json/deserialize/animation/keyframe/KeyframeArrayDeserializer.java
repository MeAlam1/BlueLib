package software.bluelib.loader.json.deserialize.animation.keyframe;

import java.util.List;
import software.bluelib.api.molang.value.MoLangValue;

public record KeyframeArrayDeserializer(
		List<MoLangValue> keyframes) implements KeyframeDeserializerData {}
