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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.data.WirelessRemoteDevice;
import wirelessredstone.addon.remote.items.ItemRedstoneWirelessRemote;
import wirelessredstone.addon.remote.network.packets.PacketRemoteCommands;
import wirelessredstone.api.IDevicePacketExecutor;
import wirelessredstone.api.IWirelessDevice;
import wirelessredstone.network.ServerPacketHandler;
import wirelessredstone.network.packets.PacketWireless;
import wirelessredstone.network.packets.PacketWirelessDevice;

public class RemoteChangeFreqExecutor implements IDevicePacketExecutor {

	@Override
	public void execute(PacketWireless p, World world, EntityPlayer entityplayer) {
		if (p instanceof PacketWirelessDevice) {
			PacketWirelessDevice packet = (PacketWirelessDevice) p;
			ItemStack itemstack= entityplayer.getHeldItem();
			if (itemstack != null && itemstack.getItem() instanceof ItemRedstoneWirelessRemote) {
				ItemRedstoneWirelessRemote remote = (ItemRedstoneWirelessRemote) itemstack.getItem();
				int freq = Integer.parseInt(packet.getDeviceFreq());
				int oldFreq = Integer.parseInt(remote.getFreq(itemstack, world).toString());
				remote.setFreq(itemstack, Integer.toString(oldFreq + freq));
				//PacketWirelessDevice remotePacket = new PacketWirelessDevice(world, device);
				//remotePacket.setCommand(PacketRemoteCommands.remoteCommands.changeFreq.toString());
				//ServerPacketHandler.sendPacketTo((EntityPlayerMP) entityplayer, remotePacket.getPacket());
			}
		}
	}
}