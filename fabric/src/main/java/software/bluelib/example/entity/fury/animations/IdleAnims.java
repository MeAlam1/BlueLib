package software.bluelib.example.entity.fury.animations;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bluelib.example.entity.fury.FuryRender;
import software.bluelib.utils.logging.BaseLogLevel;
import software.bluelib.utils.logging.BaseLogger;

public class IdleAnims extends FuryRender {

    /**
     * Constructor
     *
     * @param pRenderManager {@link EntityRendererProvider.Context} - The render manager.
     * @author MeAlam
     * @since 1.0.0
     */
    public IdleAnims(EntityRendererProvider.Context pRenderManager) {
        super(pRenderManager);
    }

    private static long lastCallTime = 0;

    public static void playIdleAnims() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastCallTime;
        double elapsedSeconds = elapsedTime / 1_000_000_000.0;
        double point2 = 2.0;

        FuryRender.root.setPosY(calculateWave(0.5F, elapsedSeconds, 2));


        if (elapsedSeconds >= point2) {
            lastCallTime = currentTime;
        }
    }

    public static float calculateWave(float startPoint, double elapsedSeconds, float duration) {
        float sineWave = (float) Math.sin(Math.PI * elapsedSeconds / duration);
        return sineWave * startPoint;
    }

}
