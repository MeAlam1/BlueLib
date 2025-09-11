# 2.3.7

## Warning:

* **This is a major update series with extensive changes. While everything has been tested privately, the sheer size
  means bugs
  may still occur. Please report any issues on GitHub or message `@me_alam` on Discord.**
* **The version will remain minor until stability and feature completeness are confirmed.**

## Added

* Added Preset Caches with Json Deserializers, Codecs, DataComponentType and CompoundTags for easy saving/loading of
  presets.
    * IntRange
    * FloatRange
    * DoubleRange
    * LongRange
    * ShortRange
    * ByteRange
    * BlockPos (With conversion from BlockPosCache to BlockPos)
    * Vector2 (as Float) (With conversion from Vector2Cache to Vector2f)
    * Vector3 (as Float) (With conversion from Vector3Cache to Vector3f)
    * RGBAColor (With conversion from RGBAColorCache to Color)
    * RGBColor (With conversion from RGBColorCache to Color)
* Added `DataComponentType` for easy registration of custom data components.

## Epilogue

* Thank you for your continued support and patience.
* Please keep reporting your issues so we can finalize these features and ensure a BugFree Experience.
* If you have any questions or need assistance, feel free to reach out on Discord (@me_alam) or GitHub (MeAlam1)