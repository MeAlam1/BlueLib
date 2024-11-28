package software.bluelib.utils.conversion;

public class ColorConverterUtils {

    public static int rgbToHex(int pRed, int pGreen, int pBlue) {
        return (pRed << 16) | (pGreen << 8) | pBlue;
    }

    public static int argbToHex(int pAlpha, int pRed, int pGreen, int pBlue) {
        return (pAlpha << 24) | (pRed << 16) | (pGreen << 8) | pBlue;
    }

    public static int hexStringToHex(String pHexString) {
        return Integer.parseUnsignedInt(pHexString.replace("#", ""), 16);
    }

    public static int hslToHex(float pHue, float pSaturation, float pLightness) {
        float c = (1 - Math.abs(2 * pLightness - 1)) * pSaturation;
        float x = c * (1 - Math.abs((pHue / 60) % 2 - 1));
        float m = pLightness - c / 2;

        return calculateRGB(pHue, c, x, m);
    }

    private static int calculateRGB(float pHue, float pC, float pX, float pM) {
        float r, g, b;
        if (pHue < 60) {
            r = pC; g = pX; b = 0;
        } else if (pHue < 120) {
            r = pX; g = pC; b = 0;
        } else if (pHue < 180) {
            r = 0; g = pC; b = pX;
        } else if (pHue < 240) {
            r = 0; g = pX; b = pC;
        } else if (pHue < 300) {
            r = pX; g = 0; b = pC;
        } else {
            r = pC; g = 0; b = pX;
        }

        int red = Math.round((r + pM)) * 255;
        int green = Math.round((g + pM)) * 255;
        int blue = Math.round((b + pM)) * 255;

        return rgbToHex(red, green, blue);
    }

    public static int hsvToHex(float pHue, float pSaturation, float pValue) {
        float c = pValue * pSaturation;
        float x = c * (1 - Math.abs((pHue / 60) % 2 - 1));
        float m = pValue - c;

        return calculateRGB(pHue, c, x, m);
    }
}
