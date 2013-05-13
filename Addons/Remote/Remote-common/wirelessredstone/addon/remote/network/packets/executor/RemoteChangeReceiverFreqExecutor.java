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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import wirelessredstone.addon.remote.core.WRemoteCore;
import wirelessredstone.addon.remote.data.WirelessRemoteDevice;
import wirelessredstone.addon.remote.items.ItemRedstoneWirelessRemote;
import wirelessredstone.api.IWirelessDevice;
import wirelessredstone.network.handlers.ServerRedstoneEtherPacketHandler;
import wirelessredstone.network.packets.PacketWireless;
import wirelessredstone.network.packets.executor.EtherPacketChangeFreqExecutor;
import wirelessredstone.tileentity.TileEntityRedstoneWirelessR;

public class RemoteChangeReceiverFreqExecutor extends EtherPacketChangeFreqExecutor {

	@Override
	public void execute(PacketWireless packet, World world, EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.getHeldItem();
		if (itemstack != null && itemstack.getItem() instanceof ItemRedstoneWirelessRemote) {
			// Fetch the tile from the packet
			TileEntity entity = packet.getTarget(world);
	
			if (entity instanceof TileEntityRedstoneWirelessR) {
				// Assemble frequencies.
				IWirelessDevice device = new WirelessRemoteDevice(world, entityplayer, itemstack);
				

				// Set the frequency to the tile
				((TileEntityRedstoneWirelessR) entity).setFreq(device.getFreq());
				entity.onInventoryChanged();
	
				// Makr the block for update with the world.
				world.markBlockForRenderUpdate(
						packet.xPosition,
						packet.yPosition,
						packet.zPosition);
	
				// Broadcast change to all clients.
				ServerRedstoneEtherPacketHandler.sendEtherTileToAll(
						(TileEntityRedstoneWirelessR) entity,
						world);
			}
		}
	}
}
