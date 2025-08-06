/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.loader.geckolib.constant.dataticket;

import java.util.Map;
import java.util.Objects;
import software.bluelib.api.annotations.WillBeDeprecated;

@WillBeDeprecated(since = "2.5.0", reason = "DataTicket will be made redundant with the new MoLang System.")
public class DataTicket<D> {

	private final String id;
	private final Class<? extends D> objectType;

	public DataTicket(String pId, Class<? extends D> pObjectType) {
		this.id = pId;
		this.objectType = pObjectType;
	}

	public String id() {
		return this.id;
	}

	public Class<? extends D> objectType() {
		return this.objectType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.objectType);
	}

	public <D> D getData(Map<? extends DataTicket<?>, ?> pDataMap) {
		return (D) pDataMap.get(this);
	}
}
