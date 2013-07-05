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
package wirelessredstone.addon.remote.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.items.ItemRedstoneWirelessRemote;
import wirelessredstone.addon.remote.network.packets.PacketRemoteCommands;
import wirelessredstone.addon.remote.overrides.RedstoneWirelessRemoteOverride;
import wirelessredstone.api.IWirelessDevice;
import wirelessredstone.client.network.ClientPacketHandler;
import wirelessredstone.data.WirelessCoordinates;
import wirelessredstone.device.WirelessTransmitterDevice;
import wirelessredstone.network.packets.PacketWirelessDevice;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Eurymachus
 * 
 */
public class WirelessRemoteDevice extends WirelessTransmitterDevice {
	
	@SideOnly(Side.CLIENT)
	public static WirelessRemoteDevice remoteTransmitter;
	
	public static HashMap<EntityLiving, IWirelessDevice> remoteTransmitters;
	public static TreeMap<WirelessCoordinates, IWirelessDevice> remoteWirelessCoords;
	
	protected int slot;
	protected static List<RedstoneWirelessRemoteOverride> overrides = new ArrayList();

	protected WirelessRemoteDevice(World world, EntityLiving entity) {
		super(world, entity, null);
		if (entity instanceof EntityPlayer) {
			this.slot = ((EntityPlayer)entity).inventory.currentItem;
			ItemStack itemstack = ((EntityPlayer)entity).inventory.getStackInSlot(this.slot);
		}
	}
	
	public WirelessRemoteDevice(World world, EntityLiving entityliving, ItemStack itemstack) {
		super(world, entityliving, itemstack);
	}
	
	/**
	 * Adds a Remote override to the Remote.
	 * 
	 * @param override
	 *            Remote override.
	 */
	public static void addOverride(RedstoneWirelessRemoteOverride override) {
		overrides.add(override);
	}

	@Override
	public String getName() {
		if (this.item != null && this.item.getItem() instanceof ItemRedstoneWirelessRemote) {
			return ((ItemRedstoneWirelessRemote)this.item.getItem()).getName(this.item);
		}
		return "Wireless Remote";
	}
	
	@Override
	public void doActivateCommand() {
		super.doActivateCommand();
	}
	
	@Override
	public void doDeactivateCommand() {
		super.doDeactivateCommand();
	}
	
	@Override
	public void activate(World world, Entity entity) {
		if (entity instanceof EntityPlayer) {
			activateWirelessRemote(world, (EntityPlayer) entity);//actisuper.activate(world, entity);
		}
	}
	
	@Override
	public void deactivate(World world, Entity entity, boolean isForced) {
		if (entity instanceof EntityPlayer) {
			deactivateWirelessRemote(world, (EntityPlayer) entity);//super.deactivate(world, entity, false);
		}
		//if (!world.isRemote && isForced && remoteTransmitters.containsKey(entity)) {
		//	deactivateWirelessRemote(world, (EntityLiving)entity);
		//}
	}

	@SideOnly(Side.CLIENT)
	public static void activatePlayerWirelessRemote(World world, EntityLiving entityliving) {
		if (entityliving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer)entityliving;
			if (remoteTransmitter != null) {
				boolean isHeld = remoteTransmitter.isBeingHeld(world, entityliving);
				if (isHeld) {
					return;
				}
				deactivatePlayerWirelessRemote(world, entityplayer);
			}
			remoteTransmitter = new WirelessRemoteDevice(world, entityplayer);
			
			PacketWirelessDevice packet = new PacketWirelessDevice(remoteTransmitter.getName());
			packet.setDeviceFreq(remoteTransmitter.getFreq());
			packet.setDeviceState(true);
			packet.setDeviceDimension(world);
			packet.setPosition(remoteTransmitter.xCoord, remoteTransmitter.yCoord, remoteTransmitter.zCoord, 0);
			packet.setCommand(PacketRemoteCommands.remoteCommands.activate.toString());
			packet.isForced(true);
			ClientPacketHandler.sendPacket(packet.getPacket());
		}
	}

	@SideOnly(Side.CLIENT)
	public static void deactivatePlayerWirelessRemote(World world, EntityLiving entityliving) {
		if (entityliving instanceof EntityPlayer) {
			if (remoteTransmitter != null) {
				//System.out.println("deactivatePlayerWirelessRemote");
				PacketWirelessDevice packet = new PacketWirelessDevice(remoteTransmitter.getName());
				packet.setDeviceFreq(remoteTransmitter.getFreq());
				packet.setDeviceState(false);
				packet.setDeviceDimension(world);
				packet.setPosition(remoteTransmitter.xCoord, remoteTransmitter.yCoord, remoteTransmitter.zCoord, 0);
				packet.setCommand(PacketRemoteCommands.remoteCommands.deactivate.toString());
				packet.isForced(true);
				ClientPacketHandler.sendPacket(packet.getPacket());
				remoteTransmitter = null;
			}
		}
	}

	public static void activateWirelessRemote(World world, EntityLiving entityliving) {
		if (remoteTransmitters.containsKey(entityliving)) {
			IWirelessDevice remote = remoteTransmitters.get(entityliving);
			remoteWirelessCoords.put(remote.getCoords(), remote);
			remote.setState(true);
			remote.doActivateCommand();
		}
	}

	public static void deactivateWirelessRemote(World world,
			EntityLiving entityliving) {
		if (remoteTransmitters.containsKey(entityliving)) {
			IWirelessDevice remote = remoteTransmitters.get(entityliving);
			remote.setState(false);
			remote.doActivateCommand();
			remoteTransmitters.remove(entityliving);
		}
	}

	@Override
	public PacketWirelessDevice getDevicePacket(World world, ItemStack itemstack) {
		return new PacketWirelessDevice(world, this);
	}

	@Override
	protected String getActivateCommand() {
		return PacketRemoteCommands.remoteCommands.activate.toString();
	}

	@Override
	protected String getDeactivateCommand() {
		return PacketRemoteCommands.remoteCommands.deactivate.toString();
	}

	@Override
	public boolean getState() {
		if (this.item != null && this.item.getItem() instanceof ItemRedstoneWirelessRemote) {
			return ((ItemRedstoneWirelessRemote)this.item.getItem()).getState(this.item);
		}
		return false;
	}

	@Override
	public void setState(boolean state) {
		if (this.item != null && this.item.getItem() instanceof ItemRedstoneWirelessRemote) {
			((ItemRedstoneWirelessRemote)this.item.getItem()).setState(this.item, state);
		}
	}

	@Override
	public String getFreq() {
		if (this.item != null && this.item.getItem() instanceof ItemRedstoneWirelessRemote) {
			return ((ItemRedstoneWirelessRemote)this.item.getItem()).getFreq(this.item, this.getWorld()).toString();
		}
		return "0";
	}

	@Override
	public void setFreq(String freq) {
		if (this.item.getItem() instanceof ItemRedstoneWirelessRemote) {
			((ItemRedstoneWirelessRemote)this.item.getItem()).setFreq(this.item, freq);
		}
	}

	@Override
	public void setDeviceState(boolean state) {
		this.setState(state);
	}
	
	@Override
	public boolean isBeingHeld(World world, EntityLiving entityliving) {
		if (entityliving != null && entityliving.getEntityName().equals(this.owner.getEntityName())) {
			ItemStack itemstack = entityliving.getHeldItem();
			if (itemstack != null && this.item != null) {
				return ((ItemRedstoneWirelessRemote)itemstack.getItem()).getFreq(itemstack, world).equals(((ItemRedstoneWirelessRemote)itemstack.getItem()).getFreq(this.item, this.getWorld()));
				/*IWirelessDevice comparator = new WirelessRemoteDevice(world, entityliving, itemstack);
				if (this.item.getItem() instanceof ItemRedstoneWirelessRemote && itemstack.getItem() instanceof ItemRedstoneWirelessRemote) {
					if (comparator.getFreq().equals(this.getFreq())) {
						//System.out.println("remote.isBeingHeld : @world[" + world.isRemote + "]");
						return true;
					}	
				}*/
			}
		}
		//System.out.println("remote.notBeingHeld");
		return false;
	}

	public static IWirelessDevice getRemoteDeviceForPlayer(EntityLiving entityliving) {
		//System.out.println("Retreiving Device");
		return remoteTransmitters.containsKey(entityliving) ? remoteTransmitters.get(entityliving) : null;
	}
}
