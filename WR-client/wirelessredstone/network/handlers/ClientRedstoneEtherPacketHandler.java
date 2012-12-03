package wirelessredstone.network.handlers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import wirelessredstone.data.LoggerRedstoneWireless;
import wirelessredstone.network.ClientPacketHandler;
import wirelessredstone.network.packets.PacketRedstoneEther;
import wirelessredstone.network.packets.executor.IEtherPacketExecutor;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientRedstoneEtherPacketHandler implements IPacketHandler {
	private static Map<Integer, IEtherPacketExecutor> executors = new HashMap<Integer, IEtherPacketExecutor>();
	/**
	 * Register an executor with the client-side packet sub-handler.
	 * 
	 * @param commandID Command ID for the executor to handle.
	 * @param executor The executor
	 */
	public static void registerPacketHandler(int commandID, IEtherPacketExecutor executor) {
		executors.put(commandID, executor);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		EntityPlayer entityplayer = (EntityPlayer)player;
		World world = entityplayer.worldObj;
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			int packetID = data.read();
			PacketRedstoneEther pRE = new PacketRedstoneEther();
			pRE.readData(data);
			handlePacket(pRE, world, entityplayer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void handlePacket(PacketRedstoneEther packet, World world, EntityPlayer entityplayer) {
		LoggerRedstoneWireless.getInstance(
				"ClientRedstoneEtherPacketHandler"
		).write(
				world.isRemote,
				"handlePacket("+packet.toString()+")",
				LoggerRedstoneWireless.LogLevel.DEBUG
		);
		
		// Fetch the command.
		int command = packet.getCommand();
		
		// Execute the command.
		if ( executors.containsKey(command)) {
			executors.get(command).execute(packet, world, entityplayer);
		} else {
			LoggerRedstoneWireless.getInstance(
					"ClientRedstoneEtherPacketHandler"
			).write(
					world.isRemote,
					"handlePacket(" + entityplayer.username + "," + packet.toString()+") - UNKNOWN COMMAND",
					LoggerRedstoneWireless.LogLevel.WARNING
			);
		}
	}

	public static void sendRedstoneEtherPacket(int command, int i, int j, int k, Object freq, boolean state) {
		PacketRedstoneEther packet = new PacketRedstoneEther(command);
		packet.setPosition(i, j, k, 0);
		packet.setFreq(freq);
		packet.setState(state);
		LoggerRedstoneWireless.getInstance(
				"ClientRedstoneEtherPacketHandler"
		).write(
				true,
				"sendRedstoneEtherPacket(" + packet.toString()+")",
				LoggerRedstoneWireless.LogLevel.DEBUG
		);
		ClientPacketHandler.sendPacket((Packet250CustomPayload) packet.getPacket());
	}
}