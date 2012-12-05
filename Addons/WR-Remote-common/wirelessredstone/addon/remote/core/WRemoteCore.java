package wirelessredstone.addon.remote.core;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.TreeMap;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import wirelessredstone.addon.remote.data.WirelessRemoteData;
import wirelessredstone.addon.remote.data.WirelessRemoteDevice;
import wirelessredstone.addon.remote.presentation.gui.GuiRedstoneWirelessRemote;
import wirelessredstone.api.ICommonProxy;
import wirelessredstone.core.WRCore;
import wirelessredstone.data.ConfigStoreRedstoneWireless;
import wirelessredstone.data.WirelessCoordinates;
import wirelessredstone.data.WirelessDeviceData;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class WRemoteCore {
	public static boolean isLoaded = false;
	public static Item itemRemote;
	public static int remoteID = 6245;

	@SideOnly(Side.CLIENT)
	public static WirelessRemoteDevice remoteTransmitter;
	@SideOnly(Side.CLIENT)
	public static GuiRedstoneWirelessRemote guiRemote;
	@SideOnly(Side.CLIENT)
	public static boolean mouseDown, wasMouseDown, remotePulsing;

	public static long pulseTime = 2500;
	public static boolean duraTogg = true;
	public static int maxPulseThreads = 2;
	public static int remoteon, remoteoff;

	@SidedProxy(
			clientSide = "wirelessredstone.addon.remote.proxy.WRemoteClientProxy",
			serverSide = "wirelessredstone.addon.remote.proxy.WRemoteCommonProxy")
	public static ICommonProxy proxy;

	/**
	 * Fires off all the canons.<br>
	 * Loads configurations and initializes objects. Loads ModLoader related
	 * stuff.
	 */
	public static boolean initialize() {

		loadConfig();

		proxy.init();

		proxy.initPacketHandlers();

		initItems();

		proxy.addOverrides();

		registerItems();

		proxy.registerRenderInformation();

		addRecipes();

		return true;
	}

	/**
	 * Loads configurations from the properties file.<br>
	 * - Remote item ID: (Remote.ID)<br>
	 */
	private static void loadConfig() {
		remoteID = (Integer) ConfigStoreRedstoneWireless
				.getInstance("Remote")
					.get("ID", Integer.class, new Integer(remoteID));
		duraTogg = (Boolean) ConfigStoreRedstoneWireless
				.getInstance("Remote")
					.get("Durability", Boolean.class, new Boolean(duraTogg));
		pulseTime = (Long) ConfigStoreRedstoneWireless
				.getInstance("Remote")
					.get("PulseDuration", Long.class, new Long(pulseTime));
		maxPulseThreads = (Integer) ConfigStoreRedstoneWireless.getInstance(
				"Remote").get(
				"MaxPulseThreads",
				Integer.class,
				new Integer(maxPulseThreads));
	}

	/**
	 * Initializes Item objects.
	 */
	private static void initItems() {

	}

	/**
	 * Registers the item names
	 */
	private static void registerItems() {
		LanguageRegistry.addName(itemRemote, "Wireless Remote");
		ModLoader.addName(itemRemote, "de_DE", "Drahtloser Funkfernbedienung");
	}

	/**
	 * Registers recipes with ModLoader<br>
	 * - Recipe for Remote.
	 */
	private static void addRecipes() {
		GameRegistry.addRecipe(new ItemStack(itemRemote, 1), new Object[] {
				"i",
				"#",
				Character.valueOf('i'),
				Block.torchRedstoneActive,
				Character.valueOf('#'),
				WRCore.blockWirelessT });
	}
}