package by.chibis.dc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import by.chibis.dc.storage.DC_Storage;

public class DC_Listener implements Listener 
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(e.getPlayer().hasPermission("msdc.chat"))
			e.getPlayer().sendMessage(DC_Storage.msg.get("uCan"));
	}
}
