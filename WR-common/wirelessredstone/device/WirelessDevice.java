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
package wirelessredstone.device;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import wirelessredstone.api.IWirelessDevice;
import wirelessredstone.api.IWirelessDeviceData;
import wirelessredstone.data.WirelessCoordinates;

/**
 * A wireless device.<br>
 * Contains the device data.
 * 
 * @author Eurymachus
 */
public abstract class WirelessDevice implements IWirelessDevice {

	protected int xCoord, yCoord, zCoord;
	protected EntityLiving owner;
	protected ItemStack item;
	
	protected WirelessDevice(World world, EntityLiving entity, ItemStack itemstack) {
		if (itemstack != null) {
			this.item = itemstack;
		} else {
			this.item = entity.getHeldItem();
		}
		this.owner = entity;
		this.setCoords((int)entity.posX, (int)entity.posY, (int)entity.posZ);
	}
	
	@Override
	public abstract String getName();

	@Override
	public EntityLiving getOwner() {
		return this.owner;
	}
	
	@Override
	public void setOwner(EntityLiving entity) {
		this.owner = entity;
	}

	@Override
	public abstract String getFreq();

	@Override
	public void setCoords(WirelessCoordinates coords) {
		int x = coords.getX(),
			y = coords.getY(),
			z = coords.getZ();
		this.setCoords(x, y, z);
	}

	@Override
	public void setCoords(int x, int y, int z) {
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
	}

	@Override
	public WirelessCoordinates getCoords() {
		return new WirelessCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}
	
	@Override
	public World getWorld() {
		return this.owner.worldObj;
	}

	@Override
	public abstract void setFreq(String freq);

	@Override
	public void activate(World world, Entity entity) {
		this.setDeviceState(true);
		if (!world.isRemote) {
			this.doActivateCommand();
		}
	}

	public abstract void setDeviceState(boolean state);

	@Override
	public void deactivate(World world, Entity entity, boolean isForced) {
		this.setDeviceState(false);
		if (!world.isRemote) {
			this.doDeactivateCommand();
		}
	}
	
	@Override
	public abstract void doActivateCommand();
	
	@Override
	public abstract void doDeactivateCommand();
	
	protected abstract String getActivateCommand();

	protected abstract String getDeactivateCommand();
	
	@Override
	public abstract boolean isBeingHeld();
	
}
