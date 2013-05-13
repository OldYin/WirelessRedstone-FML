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
package wirelessredstone.addon.remote.network.packets.executor;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.data.WirelessRemoteDevice;
import wirelessredstone.api.IWirelessDevice;
import wirelessredstone.api.IWirelessDeviceData;
import wirelessredstone.network.packets.PacketWireless;
import wirelessredstone.network.packets.PacketWirelessDevice;
import wirelessredstone.network.packets.executor.DevicePacketDeactivateExecutor;

public class DeactivateRemoteExecutor extends DevicePacketDeactivateExecutor {
	
	@Override
	public void execute(PacketWireless packet, World world, EntityPlayer entityplayer) {
		IWirelessDevice device = this.getDevice(world, entityplayer, packet);
		if (device != null) {
			device.deactivate(world, entityplayer, ((PacketWirelessDevice)packet).isForced());
		}
	}

	@Override
	protected IWirelessDevice getDevice(World world, EntityLiving entityliving, PacketWireless packet) {
		return WirelessRemoteDevice.getRemoteDeviceForPlayer(entityliving);
	}
}
