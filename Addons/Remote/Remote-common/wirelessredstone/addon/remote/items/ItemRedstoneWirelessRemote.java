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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.core.WRemoteCore;
import wirelessredstone.addon.remote.core.WirelessRemote;
import wirelessredstone.addon.remote.core.lib.IconLib;
import wirelessredstone.addon.remote.data.WirelessRemoteDevice;
import wirelessredstone.addon.remote.network.packets.PacketRemoteCommands;
import wirelessredstone.client.network.handlers.ClientRedstoneEtherPacketHandler;
import wirelessredstone.core.lib.GuiLib;
import wirelessredstone.core.lib.NBTHelper;
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
		//WirelessRemoteData remote = (WirelessRemoteData) WirelessDeviceData.getDeviceData(WirelessRemoteData.class, "Wireless Remote", itemstack,
		//		world, entityplayer);
		if (entityplayer.isSneaking()) {
			TileEntity tileentity = world.getBlockTileEntity(i, j, k);
			if (tileentity != null) {
				if (tileentity instanceof TileEntityRedstoneWirelessR) {
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
			//this.setState(itemstack, true);
			entityplayer.setItemInUse(itemstack, 72000);
			entityplayer.setEating(false);
			//WRemoteCore.proxy.activateRemote(world, entityplayer);
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
		System.out.println("Stopped Using : " + par2World != null ? !par2World.isRemote ? "Server" : "Client" : "Null");
		WirelessRemoteDevice.deactivatePlayerWirelessRemote(par2World, par3EntityPlayer);
		//WRemoteCore.proxy.deactivateRemote(par2World, par3EntityPlayer);
		//this.setState(par1ItemStack, false);
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

/*	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity,
			int i, boolean isHeld) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entity;
		
			//WirelessRemoteData data = (WirelessRemoteData) WirelessDeviceData.getDeviceData(WirelessRemoteData.class, "Wireless Remote", itemstack,
			//		world, entityplayer);
			//String freq = String.valueOf(this.getFreq(itemstack, world));//data.getDeviceFreq();
			//if ((!isHeld || !WRemoteCore.proxy.isRemoteOn(world, entityplayer, freq)) && WRemoteCore.proxy.deactivateRemote(world, entityplayer)) {
			//}
		}
	}*/
	
	public boolean getState(ItemStack itemstack) {
		return getState(itemstack.getItemDamage());
	}
	
	public boolean getState(int i) {
		return i > 0;
	}

	public String getName(ItemStack itemstack) {
		return NBTHelper.getString(itemstack, "devicename", "Wireless Remote");
	}
	
	public Object getFreq(ItemStack itemstack, World world) {
		return NBTHelper.getString(itemstack, "devicefreq", "0");
	}

	public void setFreq(ItemStack itemstack, Object freq) {
		NBTHelper.setString(itemstack, "devicefreq", freq.toString());
	}
	
	public void setState(ItemStack itemstack, boolean state) {
		this.setItemDamageForStack(itemstack, state ? 1 : 0);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}
}
