# 2.4.4

## Added

- New `AnimationExtraData` class for managing animation-related data with type-safe `DataTicket` support
- New `AnimationSnapshot` record to encapsulate immutable animation state (limbSwing, limbSwingAmount, partialTick,
  isMoving)
- New `InterpolationData` record replacing `AnimationPoint` with added `getProgress()` method for calculating normalized
  interpolation progress
- New `AnimationFrameVector` record to group X, Y, Z animation frames together
- Utility methods `isEmpty()` and `size()` added to `Animation` class
- `isWait()` helper method added to `Animation.Frame` record
- `toString()` method added to `Animation` and `AnimationState` for better debugging
- Input validation with `IllegalArgumentException` for negative ticks, non-positive play counts, and NaN animation
  values

## Changed

- Renamed `Animation.Stage` to `Animation.Frame` for clearer semantics
- Renamed `AnimationPoint` to `InterpolationData` with updated field names (`animationStartValue` → `startValue`,
  `animationEndValue` → `endValue`)
- Renamed `AnimationPointFrame` to `AnimationFrame` and moved to `frame` subpackage
- Renamed `getAnimationStages()` to `getAnimationFrames()` in `Animation` class
- Refactored `BoneAnimationFrame` to use `AnimationFrameVector` instead of individual X/Y/Z queues
- Renamed methods in `BoneAnimationFrame`: `addRotations` → `addNextRotation`, `addPositions` → `addNextPosition`,
  `addScales` → `addNextScale`
- `AnimationState` now uses composition with `AnimationSnapshot` and `AnimationExtraData` instead of individual fields
- `getAnimationFrames()` now returns an unmodifiable list
- `getController()` in `AnimationState` now throws `IllegalStateException` if controller is not set
- `setControllerSpeed()` parameter changed from `Double` to primitive `double`
- `animationTick` field in `AnimationState` is now private with getter/setter methods
- Improved `equals()` and `hashCode()` implementations for `Animation` and `Animation.Frame`
- Added `/fabric/out` to `.gitignore`
- Enabled example logging (`isExampleEnabled = true`)

## Deleted

- Removed `AnimationPoint` class (replaced by `InterpolationData`)
- Removed individual rotation/position/scale queue fields from `BoneAnimationFrame` (consolidated into
  `AnimationFrameVector`)
- Removed individual `addRotationXPoint`, `addRotationYPoint`, etc. methods (replaced with vector-based methods)

## Bug Fixes

- Fixed `equals()` method in `Animation` and `Animation.Frame` to properly compare field values instead of just hashCode
- Added proper null handling in `AnimationExtraData.set()` method