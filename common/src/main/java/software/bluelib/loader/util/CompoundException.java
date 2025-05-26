package software.bluelib.loader.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.StringJoiner;


public class CompoundException extends RuntimeException {
    private final List<String> messages = new ObjectArrayList<>();

    public CompoundException(String message) {
        this.messages.add(message);
    }

    
    public CompoundException withMessage(String message) {
        this.messages.add(message);

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
        final String name = "Geckolib.CompoundException";
        final String message = getLocalizedMessage();

        return message != null ? name + ": " + message : name;
    }
}
