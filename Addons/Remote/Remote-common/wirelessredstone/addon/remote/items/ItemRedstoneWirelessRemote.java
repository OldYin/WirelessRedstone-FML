/*    
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package wirelessredstone.addon.remote.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.core.WRemoteCore;
import wirelessredstone.addon.remote.core.WirelessRemote;
import wirelessredstone.addon.remote.core.lib.IconLib;
import wirelessredstone.addon.remote.core.lib.ReferenceLib;
import wirelessredstone.addon.remote.data.WirelessRemoteDevice;
import wirelessredstone.addon.remote.network.packets.PacketRemoteCommands;
import wirelessredstone.api.IWirelessDevice;
import wirelessredstone.client.network.handlers.ClientRedstoneEtherPacketHandler;
import wirelessredstone.core.NBTHelper;
import wirelessredstone.core.lib.GuiLib;
import wirelessredstone.core.lib.NBTLib;
import wirelessredstone.tileentity.TileEntityRedstoneWirelessR;

public class ItemRedstoneWirelessRemote extends Item {
	
	protected Icon[] iconList;
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		iconList = new Icon[2];
		iconList[0] = iconRegister.registerIcon(IconLib.WIRELESS_REMOTE_OFF);
		iconList[1] = iconRegister.registerIcon(IconLib.WIRELESS_REMOTE_ON);
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	public ItemRedstoneWirelessRemote(int i) {
		super(i);
		this.setNoRepair();
		this.setCreativeTab(CreativeTabs.tabRedstone);
		maxStackSize = 1;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer,
			World world, int i, int j, int k, int l, float a, float b, float c) {
		return true;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer,
			World world, int i, int j, int k, int l, float a, float b, float c) {
		if (entityplayer.isSneaking()) {
			TileEntity tileentity = world.getBlockTileEntity(i, j, k);
			if (tileentity != null && tileentity instanceof TileEntityRedstoneWirelessR) {
				if (world.isRemote) {
					ClientRedstoneEtherPacketHandler.sendRedstoneEtherPacket(
							PacketRemoteCommands.remoteCommands.updateReceiver.toString(),
							((TileEntityRedstoneWirelessR)tileentity).getBlockCoord(0), 
							((TileEntityRedstoneWirelessR)tileentity).getBlockCoord(1),
							((TileEntityRedstoneWirelessR)tileentity).getBlockCoord(2), 
							this.getFreq(itemstack, world), 
							false
					);
				}
				return true;
			}
			entityplayer.openGui(WirelessRemote.instance, GuiLib.GUIID_DEVICE, world, i, j, k);
			return true;
		}
		this.onItemRightClick(itemstack, world, entityplayer);
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		if (!entityplayer.isSneaking()) {
			//entityplayer.setItemInUse(itemstack, 72000);
			String side = world != null ? !world.isRemote ? "Server" : "Client" : "Null";
			System.out.println("Freq: " + this.getFreq(itemstack, world) + " | Side: " + side);
			if (!world.isRemote) {
				WirelessRemoteDevice.activateWirelessRemote(world, entityplayer);
			}
		} else {
			onItemUseFirst(itemstack, entityplayer, world,
					(int) Math.round(entityplayer.posX),
					(int) Math.round(entityplayer.posY),
					(int) Math.round(entityplayer.posZ), 0, 0, 0, 0);
		}
		return itemstack;
	}

	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}
	
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		WirelessRemoteDevice.deactivatePlayerWirelessRemote(par2World, par3EntityPlayer);
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean isHeld) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entity;
			if (itemstack != null && itemstack.getItem() instanceof ItemRedstoneWirelessRemote) {
				String freq = this.getFreq(itemstack, world).toString();
				if (!isHeld || (!WRemoteCore.proxy.isRemoteOn(world, entityplayer, freq) && !WRemoteCore.proxy.deactivateRemote(world, entityplayer))) {
				}
			}
		}
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public Icon getIconFromDamage(int i) {
		if (!this.getState(i))
			return iconList[0];
		return iconList[1];
	}
	
	public boolean getState(ItemStack itemstack) {
		return getState(itemstack.getItemDamage());
	}
	
	public boolean getState(int i) {
		return i > 0;
	}

	public String getName(ItemStack itemstack) {
		return NBTHelper.getString(itemstack, NBTLib.DEVICE_NAME, ReferenceLib.MOD_NAME);
	}
	
	public Object getFreq(ItemStack itemstack, World world) {
		return NBTHelper.getString(itemstack, NBTLib.DEVICE_FREQUENCY, "0");
	}

	public void setFreq(ItemStack itemstack, Object freq) {
		NBTHelper.setString(itemstack, NBTLib.DEVICE_FREQUENCY, freq.toString());
	}
	
	public void setState(ItemStack itemstack, boolean state) {
		this.setItemDamageForStack(itemstack, state ? 1 : 0);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}
}
