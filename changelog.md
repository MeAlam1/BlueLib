# 2.3.1

## Added

* Added more Annotations!
* Added more Logic to the `PlatformHelper`
    * `isPhysicalClient` & `getGameDir`
* Added More Network Sending Utils
    * `sendToAllPlayersTrackingEntity` & `sendToAllPlayersTrackingBlock`
* Created the `@WillBeDeprecated` Annotation
    * Unlike Deprecated, which communicates that an element is already discouraged for use, this annotation signals an
      early notice that the element may be deprecated in the future. The element is still fully supported and no active
      deprecation or removal work has begun. Use this to communicate upcoming API changes and allow users to prepare.

### Added a Complete Bedrock Model/Animation/Texture Loader!

* Introduced a Bedrock Loader that supports Bedrock models, animations, and textures. No need for
  Blockbench plugins.
* Supports Geckolib assets, while also promoting direct use of Bedrock-format resources.
* Features DataDriven Animation Controllers, Enabling datapack and resource pack creators to inject custom animations
  and immersive behaviors.
* Rendering is powered by a new Context System, cleaning up rendering logic for maintainability and
  flexibility.

### Added Full MoLang Support!

* Integrated a MoLang engine with built-in support for MoLang queries and expressions.
* Developers can easily extend MoLang with custom queries, allowing for advanced animation logic and entity behaviors.

_Note: The Wiki is currently a work in progress and will be expanded gradually to cover all aspects of this major
update._

## Changed

* `ResourceCache` now has an `Client` and `Server` side!

## Bug Fixes

* Fixed an Language Missing Issue.
* Fixed the Translation issues in the Console Logging.
* Fixed some Minor Parameter Naming.
* Fixed some incorrect Annotations.

## Deleted

* Deleted Deprecated Markdown Code from `2.2.0`
* Deleted the `JSONParser`