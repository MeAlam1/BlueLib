# 2.4.0

## Added
* Added exception handling across utility classes:
* Thrown and added specific exceptions for invalid inputs and failed I/O.
* Added error messages and retry cases where applicable.

## Changed
* Improved a bunch of niche edge cases with the Utility methods we provide.
* Cleaned up and standardized logging:
    * Removed noisy statements and added clearer messages for easier debugging.
* Improved edge-case handling:
    * Extra null/empty checks and safer default values.
    * better behavior for boundary values and concurrent access.
* Added more verification steps to make sure that there is a correct output:
    * Post-processing validation and format checks.
    * Safeguards to prevent partial or wrong outputs.
* General production improvements:
    * Input validation improved and fail-safe defaults introduced.
    * Small performance tweaks and refactors for maintainability.
* Miscellaneous minor fixes and cleanup.
    
## Updated
* A ton of Dependencies including but not limited to:
    * Modrinth to 2.8.10
    * Spotless to 8.1.0
    * Darkhax Curseforge to 1.1.28
    * NeoForge to 2.0.120