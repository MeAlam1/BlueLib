/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.exception;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.StringJoiner;

public class CompoundException extends RuntimeException {

    private final List<String> messages = new ObjectArrayList<>();

    public CompoundException(String pMessage) {
        this.messages.add(pMessage);
    }

    public CompoundException withMessage(String pMessage) {
        this.messages.add(pMessage);

        return this;
    }

    @Override
    public String getLocalizedMessage() {
        final StringJoiner joiner = new StringJoiner("\n");
        final int count = this.messages.size() - 1;

        for (int i = count; i >= 0; i--) {
            joiner.add((i == count ? "" : "\t".repeat(Math.max(0, count - i)) + "-> ") + this.messages.get(i));
        }

        return joiner.toString();
    }

    @Override
    public String toString() {
        final String name = "BlueLib.CompoundException";
        final String message = getLocalizedMessage();

        return message != null ? name + ": " + message : name;
    }
}
