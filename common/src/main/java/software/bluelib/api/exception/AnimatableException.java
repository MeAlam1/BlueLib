/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import org.jetbrains.annotations.NotNull;

public class AnimatableException extends NullPointerException {

	private final List<String> messages;

	public AnimatableException(String pMessage) {
		super(pMessage);
		this.messages = Collections.singletonList(pMessage);
	}

	public AnimatableException(List<String> pMessages) {
		super(pMessages != null && !pMessages.isEmpty() ? pMessages.getFirst() : null);
		this.messages = pMessages == null ? Collections.emptyList() : Collections.unmodifiableList(pMessages);
	}

	@NotNull
	public AnimatableException withMessage(String pMessage) {
		List<String> newMessages = new ArrayList<>(this.messages);
		newMessages.add(pMessage);
		return new AnimatableException(newMessages);
	}

	@Override
	public String getLocalizedMessage() {
		StringJoiner joiner = new StringJoiner("\n");
		for (int i = 0; i < messages.size(); i++) {
			joiner.add((i == 0 ? "" : "-> ") + messages.get(i));
		}
		return joiner.toString();
	}

	@Override
	public String toString() {
		return "AnimatableException: " + getLocalizedMessage();
	}
}
