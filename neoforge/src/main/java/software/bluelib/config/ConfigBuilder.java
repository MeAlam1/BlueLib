package software.bluelib.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigBuilder {

    public static ModConfigSpec.IntValue buildInt(ModConfigSpec.Builder pBuilder, String pName, int pDefaultValue, int pMin, int pMax, String pComment) {
        return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
    }

    public static ModConfigSpec.DoubleValue buildDouble(ModConfigSpec.Builder pBuilder, String pName, double pDefaultValue, double pMin, double pMax, String pComment) {
        return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
    }

    public static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder pBuilder, String pName, boolean pDefaultValue, String pComment) {
        return pBuilder.comment(pComment).translation(pName).define(pName, pDefaultValue);
    }

    public static ModConfigSpec.LongValue buildLong(ModConfigSpec.Builder pBuilder, String pName, long pDefaultValue, long pMin, long pMax, String pComment) {
        return pBuilder.comment(pComment).translation(pName).defineInRange(pName, pDefaultValue, pMin, pMax);
    }

    public static ModConfigSpec.ConfigValue<String> buildString(ModConfigSpec.Builder pBuilder, String pName, String pDefaultValue, String pComment) {
        return pBuilder.comment(pComment).translation(pName).define(pName, pDefaultValue);
    }
}
