# 2.3.3.2 MoLang Additions

### We will be gradually releasing Minor versions with a ton of MoLang additions.

**Feel Free to Suggest MoLangs that you would like to see added in the future.**

## Warning:

* **This is a major update with extensive changes. While everything has been tested privately, the sheer size means bugs
  may still occur. Please report any issues on GitHub or message `@me_alam` on Discord.**
* **The version will remain minor until stability and feature completeness are confirmed.**

## Added

* Added all the MoLang for:
    * Animal
    * ArmorStand
    * Bee

## Changed

* Changed the MoLang Versions to BETA in stead of RELEASE, due to the small amount of changes.
* Made the JSONMerger Static, so it can be used without instantiation.
* Minor Cleanup in the EntityMoLang Registration.
* **IMPORTANT:** Updated MoLang execution behavior:
    * When executing a void function, it now returns `True` if executed successfully, and `False` otherwise.
    * If a void function has a corresponding getter (e.g., `SetInLove`/`IsInLove`), it will return the value from the
      getter (such as `IsInLove`), regardless of execution success.

## Epilogue

* Thank you for your continued support and patience.
* Please keep reporting your issues so we can finalize these features and ensure a Bug-Free Experience.
* If you have any questions or need assistance, feel free to reach out on Discord (@me_alam) or GitHub (MeAlam1)