package software.bluelib.loader.animation.keyframe.event.data;

import software.bluelib.loader.animation.keyframe.Keyframe;

import java.util.Objects;


public class ParticleKeyframeData extends KeyFrameData {
	private final String effect;
	private final String locator;
	private final String script;

	public ParticleKeyframeData(double startTick, String effect, String locator, String script) {
		super(startTick);

		this.script = script;
		this.locator = locator;
		this.effect = effect;
	}

	
	public String getEffect() {
		return this.effect;
	}

	
	public String getLocator() {
		return this.locator;
	}

	
	public String script() {
		return this.script;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStartTick(), effect, locator, script);
	}
}
