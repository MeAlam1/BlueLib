package software.bluelib.loader.animation.keyframe.event.data;

import software.bluelib.loader.animation.keyframe.Keyframe;

import java.util.Objects;


public class SoundKeyframeData extends KeyFrameData {
	private final String sound;

	public SoundKeyframeData(Double startTick, String sound) {
		super(startTick);

		this.sound = sound;
	}

	
	public String getSound() {
		return this.sound;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), this.sound);
	}
}
