/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.exception.nulls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({ "unused" })
public class ModelNullException extends NullPointerException {

	private final List<String> messages;

	public ModelNullException(String pMessage) {
		super(pMessage);
		this.messages = Collections.singletonList(pMessage);
	}

	public ModelNullException(List<String> pMessages) {
		super(pMessages != null && !pMessages.isEmpty() ? pMessages.getFirst() : null);
		this.messages = pMessages == null ? Collections.emptyList() : Collections.unmodifiableList(pMessages);
	}

	@NotNull
	public ModelNullException withMessage(String pMessage) {
		List<String> newMessages = new ArrayList<>(this.messages);
		newMessages.add(pMessage);
		return new ModelNullException(newMessages);
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
		return "ModelNullException: " + getLocalizedMessage();
	}
}
