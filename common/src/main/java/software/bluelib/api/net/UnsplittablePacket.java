// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.api.net;

/**
 * Marks a packet as "unsplitable" to prevent NeoForge from altering buffers during encoding/decoding.
 * Buffers are allocated for encoding, and re-encoding the same instance multiple times may cause issues.
 * <br>
 * Even if NeoForge does not encode the packet twice, splitting the data could lead to problems,
 * such as having part of the buffer in one packet and the rest in another.
 */
public interface UnsplittablePacket {}
