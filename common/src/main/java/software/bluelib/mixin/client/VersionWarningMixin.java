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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;

@Mixin(Minecraft.class)
public final class VersionWarningMixin {

	@Unique
	private static final Set<String> blueLib$dontShowAgain = new HashSet<>();
	@Unique
	private static final String FILE_NAME = "versionWarning.txt";
	@Unique
	private static boolean blueLib$loaded = false;

	@Unique
	private static void blueLib$loadDontShowAgain() {
		if (blueLib$loaded) return;
		blueLib$loaded = true;
		Path file = Path.of(FILE_NAME);
		if (Files.exists(file)) {
			try (BufferedReader reader = Files.newBufferedReader(file)) {
				String line;
				while ((line = reader.readLine()) != null) {
					blueLib$dontShowAgain.add(line.trim());
				}
			} catch (IOException ignored) {
			}
		}
	}

	@Unique
	private static void blueLib$saveDontShowAgain() {
		Path file = Path.of(FILE_NAME);
		try (BufferedWriter writer = Files.newBufferedWriter(file)) {
			for (String key : blueLib$dontShowAgain) {
				writer.write(key);
				writer.newLine();
			}
		} catch (IOException ignored) {
		}
	}

	@Inject(method = "addInitialScreens", at = @At("TAIL"))
	public void cobblemon$addSnapshotWarningScreen(List<Function<Runnable, Screen>> pOutput, CallbackInfo pCi) {
		blueLib$loadDontShowAgain();
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
								blueLib$saveDontShowAgain();
							}
							runnable.run();
						}
				));
			}
		}
	}
}