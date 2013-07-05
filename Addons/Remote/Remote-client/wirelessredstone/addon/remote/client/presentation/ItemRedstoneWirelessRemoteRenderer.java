package wirelessredstone.addon.remote.client.presentation;

import wirelessredstone.addon.remote.items.ItemRedstoneWirelessRemote;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

public class ItemRedstoneWirelessRemoteRenderer implements IItemRenderer {
	
	private static RenderItem renderItem = new RenderItem();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == type.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO :: Auto-generated method stub
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (item.getItem() instanceof ItemRedstoneWirelessRemote) {
			ItemRedstoneWirelessRemote remote = (ItemRedstoneWirelessRemote) item.getItem();
			Icon icon = remote.getRemoteIcon(item);
			// Use vanilla code to render the icon in a 16x16 square of inventory slot
			renderItem.renderIcon(0, 0, icon, 16, 16);
		}
	}

}
