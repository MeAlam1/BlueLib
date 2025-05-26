/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
/*
 * Direct copy of https://github.com/shedaniel/cloth-basic-math/blob/master/src/main/java/me/shedaniel/math/Color.java under the unlicense.
 */
package software.bluelib.loader.util;

@SuppressWarnings("unused")
public record Color(int argbInt) {

    public static final Color WHITE = new Color(0xFFFFFFFF);
    public static final Color LIGHT_GRAY = new Color(0xFFC0C0C0);
    public static final Color GRAY = new Color(0xFF808080);
    public static final Color DARK_GRAY = new Color(0xFF404040);
    public static final Color BLACK = new Color(0xFF000000);
    public static final Color RED = new Color(0xFFFF0000);
    public static final Color PINK = new Color(0xFFFFAFAF);
    public static final Color ORANGE = new Color(0xFFFFC800);
    public static final Color YELLOW = new Color(0xFFFFFF00);
    public static final Color GREEN = new Color(0xFF00FF00);
    public static final Color MAGENTA = new Color(0xFFFF00FF);
    public static final Color CYAN = new Color(0xFF00FFFF);
    public static final Color BLUE = new Color(0xFF0000FF);

    public static Color ofOpaque(int pColor) {
        return new Color(0xFF000000 | pColor);
    }

    public static Color ofRGB(float pRed, float pGreen, float pBlue) {
        return ofRGBA(pRed, pGreen, pBlue, 1f);
    }

    public static Color ofRGB(int pRed, int pGreen, int pBlue) {
        return ofRGBA(pRed, pGreen, pBlue, 255);
    }

    public static Color ofRGBA(float pRed, float pGreen, float pBlue, float a) {
        return ofRGBA((int) (pRed * 255f + 0.5), (int) (pGreen * 255f + 0.5f), (int) (pBlue * 255f + 0.5f), (int) (a * 255f + 0.5f));
    }

    public static Color ofRGBA(int pRed, int pGreen, int pBlue, int a) {
        return new Color(((a & 0xFF) << 24) | ((pRed & 0xFF) << 16) | ((pGreen & 0xFF) << 8) | (pBlue & 0xFF));
    }

    public static Color ofARGB(float a, float pRed, float pGreen, float pBlue) {
        return ofARGB((int) (a * 255f + 0.5f), (int) (pRed * 255f + 0.5), (int) (pGreen * 255f + 0.5f), (int) (pBlue * 255f + 0.5f));
    }

    public static Color ofARGB(int a, int pRed, int pGreen, int pBlue) {
        return new Color(((a & 0xFF) << 24) | ((pRed & 0xFF) << 16) | ((pGreen & 0xFF) << 8) | (pBlue & 0xFF));
    }

    public static Color ofHSB(float hue, float saturation, float brightness) {
        return ofOpaque(HSBtoARGB(hue, saturation, brightness));
    }

    public static int HSBtoARGB(float hue, float saturation, float brightness) {
        int pRed = 0;
        int pGreen = 0;
        int pBlue = 0;

        if (saturation == 0) {
            pRed = pGreen = pBlue = (int) (brightness * 255f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1 - saturation);
            float q = brightness * (1 - saturation * f);
            float t = brightness * (1 - (saturation * (1 - f)));

            switch ((int) h) {
                case 0 -> {
                    pRed = (int) (brightness * 255f + 0.5f);
                    pGreen = (int) (t * 255f + 0.5f);
                    pBlue = (int) (p * 255f + 0.5f);
                }
                case 1 -> {
                    pRed = (int) (q * 255f + 0.5f);
                    pGreen = (int) (brightness * 255f + 0.5f);
                    pBlue = (int) (p * 255f + 0.5f);
                }
                case 2 -> {
                    pRed = (int) (p * 255f + 0.5f);
                    pGreen = (int) (brightness * 255f + 0.5f);
                    pBlue = (int) (t * 255f + 0.5f);
                }
                case 3 -> {
                    pRed = (int) (p * 255f + 0.5f);
                    pGreen = (int) (q * 255f + 0.5f);
                    pBlue = (int) (brightness * 255f + 0.5f);
                }
                case 4 -> {
                    pRed = (int) (t * 255f + 0.5f);
                    pGreen = (int) (p * 255f + 0.5f);
                    pBlue = (int) (brightness * 255f + 0.5f);
                }
                case 5 -> {
                    pRed = (int) (brightness * 255f + 0.5f);
                    pGreen = (int) (p * 255f + 0.5f);
                    pBlue = (int) (q * 255f + 0.5f);
                }
            }
        }

        return 0xFF000000 | (pRed << 16) | (pGreen << 8) | pBlue;
    }

    public int getColor() {
        return this.argbInt;
    }

    public int getAlpha() {
        return this.argbInt >> 24 & 0xFF;
    }

    public float getAlphaFloat() {
        return getAlpha() / 255f;
    }

    public int getRed() {
        return this.argbInt >> 16 & 0xFF;
    }

    public float getRedFloat() {
        return getRed() / 255f;
    }

    public int getGreen() {
        return this.argbInt >> 8 & 0xFF;
    }

    public float getGreenFloat() {
        return getGreen() / 255f;
    }

    public int getBlue() {
        return this.argbInt & 0xFF;
    }

    public float getBlueFloat() {
        return getBlue() / 255f;
    }

    public Color brighter(double factor) {
        int pRed = getRed();
        int pGreen = getGreen();
        int pBlue = getBlue();
        int i = (int) (1 / (1 - (1 / factor)));

        if (pRed == 0 && pGreen == 0 && pBlue == 0)
            return ofRGBA(i, i, i, getAlpha());

        if (pRed > 0 && pRed < i)
            pRed = i;

        if (pGreen > 0 && pGreen < i)
            pGreen = i;

        if (pBlue > 0 && pBlue < i)
            pBlue = i;

        return ofRGBA(Math.min((int) (pRed / (1 / factor)), 255), Math.min((int) (pGreen / (1 / factor)), 255),
                Math.min((int) (pBlue / (1 / factor)), 255), getAlpha());
    }

    public Color darker(float factor) {
        return ofRGBA(Math.max((int) (getRed() * (1 / factor)), 0), Math.max((int) (getGreen() * (1 / factor)), 0),
                Math.max((int) (getBlue() * (1 / factor)), 0), getAlpha());
    }

    @Override
    public boolean equals(Object pOther) {
        if (this == pOther)
            return true;

        if (getClass() != pOther.getClass())
            return false;

        return hashCode() == pOther.hashCode();
    }

    @Override
    public int hashCode() {
        return this.argbInt;
    }

    @Override
    public String toString() {
        return String.valueOf(this.argbInt);
    }
}
