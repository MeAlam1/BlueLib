/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.net;

import software.bluelib.api.net.NetworkRegistry;
import software.bluelib.client.net.OpenLoggerPacketHandler;
import software.bluelib.client.net.loader.BlockEntityAnimTriggerPacketHandler;
import software.bluelib.client.net.loader.BlockEntityDataSyncPacketHandler;
import software.bluelib.client.net.loader.EntityAnimTriggerPacketHandler;
import software.bluelib.client.net.loader.EntityDataSyncPacketHandler;
import software.bluelib.client.net.loader.SingletonAnimTriggerPacketHandler;
import software.bluelib.client.net.loader.SingletonDataSyncPacketHandler;
import software.bluelib.client.net.loader.StopTriggeredBlockEntityAnimPacketHandler;
import software.bluelib.client.net.loader.StopTriggeredEntityAnimPacketHandler;
import software.bluelib.client.net.loader.StopTriggeredSingletonAnimPacketHandler;
import software.bluelib.net.messages.client.OpenLoggerPacket;
import software.bluelib.net.messages.client.loader.BlockEntityAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.BlockEntityDataSyncPacket;
import software.bluelib.net.messages.client.loader.EntityAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.EntityDataSyncPacket;
import software.bluelib.net.messages.client.loader.SingletonAnimTriggerPacket;
import software.bluelib.net.messages.client.loader.SingletonDataSyncPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredBlockEntityAnimPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredEntityAnimPacket;
import software.bluelib.net.messages.client.loader.StopTriggeredSingletonAnimPacket;
import software.bluelib.net.messages.server.TestPacket;
import software.bluelib.net.serverHandling.TestPacketHandler;

public final class FabricHandlerRegistrar {

	private FabricHandlerRegistrar() {}

	public static void registerClientHandlers() {
		// S2C handlers only
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), OpenLoggerPacket.ID, OpenLoggerPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), BlockEntityAnimTriggerPacket.ID, BlockEntityAnimTriggerPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), BlockEntityDataSyncPacket.ID, BlockEntityDataSyncPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), EntityAnimTriggerPacket.ID, EntityAnimTriggerPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), EntityDataSyncPacket.ID, EntityDataSyncPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), SingletonAnimTriggerPacket.ID, SingletonAnimTriggerPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), SingletonDataSyncPacket.ID, SingletonDataSyncPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), StopTriggeredEntityAnimPacket.ID, StopTriggeredEntityAnimPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), StopTriggeredBlockEntityAnimPacket.ID, StopTriggeredBlockEntityAnimPacketHandler::new);
		FabricPacketInfo.registerClientHandler(NetworkRegistry.getS2CPayloads(), StopTriggeredSingletonAnimPacket.ID, StopTriggeredSingletonAnimPacketHandler::new);
	}

	public static void registerServerHandlers() {
		// C2S handlers only
		FabricPacketInfo.registerServerHandler(NetworkRegistry.getC2SPayloads(), TestPacket.ID, TestPacketHandler::new);
	}
}
