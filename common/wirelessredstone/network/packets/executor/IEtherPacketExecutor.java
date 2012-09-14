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
package wirelessredstone.network.packets.executor;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import wirelessredstone.network.packets.PacketRedstoneEther;
/**
 * A Redstone Ether Packet executor.<br>
 * Used by the Redstone Ether Packet handler to execute packet commands.
 * 
 * @author ali4z
 */
public interface IEtherPacketExecutor {
	/**
	 * Execute the packet.
	 * 
	 * @param packet The redstone ether packet.
	 * @param world The world object.
	 */
	public void execute(PacketRedstoneEther packet, World world, EntityPlayer entityplayer);
}
