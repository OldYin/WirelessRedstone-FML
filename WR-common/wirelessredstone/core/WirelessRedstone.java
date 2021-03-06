/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * Lesser General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>
 */
package wirelessredstone.core;

import wirelessredstone.client.network.ClientPacketHandler;
import wirelessredstone.network.RedstoneWirelessConnectionHandler;
import wirelessredstone.network.ServerPacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;

/**
 * Wireless Redstone ModLoader initializing class.
 * 
 * @author ali4z
 */
@Mod(
		modid = "WirelessRedstoneCore",
		name = "Wireless Redstone",
		version = "1.7")
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = false,
		connectionHandler = RedstoneWirelessConnectionHandler.class,
		clientPacketHandlerSpec = @SidedPacketHandler(
				channels = { "WR" },
				packetHandler = ClientPacketHandler.class),
		serverPacketHandlerSpec = @SidedPacketHandler(
				channels = { "WR" },
				packetHandler = ServerPacketHandler.class))
/**
 * FML fascade class.
 * This class uses FML annotations and sorts initialization.
 * 
 * ConnectionHandler: RedstoneWirelessConnectionHandler
 * ClientPacketHandler: ClientPacketHandler
 * ServerPacketHandler: ServerPacketHandler
 * 
 * @author Eurymachus, ali4z
 */
public class WirelessRedstone {

	@Instance("WirelessRedstoneCore")
	public static WirelessRedstone instance;

	/**
	 * Pre-initialization
	 * 
	 * @param event
	 */
	@EventHandler
	public void WirelessRedstonePreInit(FMLPreInitializationEvent event) {
		WRCore.initialize();
	}

	/**
	 * Initialization
	 * 
	 * @param event
	 */
	@EventHandler
	public void WirelessRedstoneInit(FMLInitializationEvent event) {
	}

	/**
	 * Post-initialization
	 * 
	 * @param event
	 */
	@EventHandler
	public void WirelessRedstonePostInit(FMLPostInitializationEvent event) {
	}
}
