package wirelessredstone.network;

import wirelessredstone.network.handlers.ServerRedstoneEtherPacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class RedstoneWirelessConnectionHandler implements IConnectionHandler {

	@Override
	public void playerLoggedIn(Player player,
			NetHandler netHandler,
			NetworkManager manager) {
		if (player instanceof EntityPlayerMP) {
			System.out.println("Side: " + FMLCommonHandler.instance().getSide().toString());
			EntityPlayerMP entityplayermp = (EntityPlayerMP)player;
			ServerRedstoneEtherPacketHandler.sendEtherTilesTo(entityplayermp);
		}
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler,
			NetworkManager manager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			String server,
			int port,
			NetworkManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionOpened(NetHandler netClientHandler,
			MinecraftServer server,
			NetworkManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void connectionClosed(NetworkManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler,
			NetworkManager manager,
			Packet1Login login) {
		// TODO Auto-generated method stub

	}

}
