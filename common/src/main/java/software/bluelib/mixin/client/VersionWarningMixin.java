package software.bluelib.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bluelib.BlueLibConstants;
import software.bluelib.BuildDetails;
import software.bluelib.client.gui.version.VersionWarningScreen;

import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;

@Mixin(Minecraft.class)
public final class VersionWarningMixin {

	@Unique
	private static final Set<String> blueLib$dontShowAgain = new HashSet<>();

	@Inject(method = "addInitialScreens", at = @At("TAIL"))
	public void cobblemon$addSnapshotWarningScreen(List<Function<Runnable, Screen>> pOutput, CallbackInfo pCi) {
		ServiceLoader<BuildDetails> loader = BlueLibConstants.loadAll(BuildDetails.class);
		for (BuildDetails details : loader) {
			String key = details.getModId() + ":" + details.getVersion();
			if (details.displayWarning() && !blueLib$dontShowAgain.contains(key)) {
				pOutput.add((runnable) -> new VersionWarningScreen(
						details.getModId(),
						details.getVersion(),
						(ack, dontShow) -> {
							if (ack == VersionWarningScreen.Acknowledgement.NO) {
								Minecraft.getInstance().close();
							}
							if (dontShow) {
								blueLib$dontShowAgain.add(key);
							}
							runnable.run();
						}
				));
			}
		}
	}
}