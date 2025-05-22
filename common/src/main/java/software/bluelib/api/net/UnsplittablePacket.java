/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.net;

/**
 * Marks a packet as "unsplitable" to prevent NeoForge from altering buffers during encoding/decoding.
 * Buffers are allocated for encoding, and re-encoding the same instance multiple times may cause issues.
 * <br>
 * Even if NeoForge does not encode the packet twice, splitting the data could lead to problems,
 * such as having part of the buffer in one packet and the rest in another.
 */
public interface UnsplittablePacket {}
