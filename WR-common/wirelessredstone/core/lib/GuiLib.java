package wirelessredstone.core.lib;

import net.minecraft.util.ResourceLocation;

public class GuiLib {
	private static final String L_MOD_ID=CoreLib.MOD_ID.toLowerCase(); //Cludge to ensure lowercase path.
	private static final String GUI_PREFIX = "textures/gui/";
	public static final ResourceLocation GUI_ON =		new ResourceLocation(L_MOD_ID, GUI_PREFIX + "guiOn.png");
	public static final ResourceLocation GUI_OFF =		new ResourceLocation(L_MOD_ID, GUI_PREFIX + "guiOff.png");
	public static final ResourceLocation GUI_EXIT = 	new ResourceLocation(L_MOD_ID, GUI_PREFIX + "wifi_exit.png");
	public static final ResourceLocation GUI_SMALL = 	new ResourceLocation(L_MOD_ID, GUI_PREFIX + "wifi_small.png");
	public static final ResourceLocation GUI_MEDIUM =	new ResourceLocation(L_MOD_ID, GUI_PREFIX + "wifi_medium.png");
	public static final ResourceLocation GUI_LARGE =	new ResourceLocation(L_MOD_ID, GUI_PREFIX + "wifi_large.png");
	public static final ResourceLocation GUI_XLARGE =	new ResourceLocation(L_MOD_ID, GUI_PREFIX + "wifi_xlarge.png");
	public static final int GUIID_INVENTORY = 0;
	public static final int GUIID_DEVICE = 1;
}
