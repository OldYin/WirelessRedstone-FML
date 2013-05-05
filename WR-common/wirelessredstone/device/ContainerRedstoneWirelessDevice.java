package wirelessredstone.device;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import wirelessredstone.api.IWirelessDevice;

public class ContainerRedstoneWirelessDevice extends Container {
	
	public IWirelessDevice wirelessDevice;
	
	public ContainerRedstoneWirelessDevice(IWirelessDevice device) {
		super();
		this.wirelessDevice = device;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
