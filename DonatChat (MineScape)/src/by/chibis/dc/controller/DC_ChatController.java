package by.chibis.dc.controller;

import java.util.HashMap;

import org.bukkit.entity.Player;

import by.chibis.dc.storage.DC_Storage;

public class DC_ChatController 
{
	public static HashMap<String, Long> playerChat = new HashMap<>();
	private static long cdTime = DC_Storage.cmdCd * 1000;
	
	public static long isTimeEnd(String name)
	{
		name = name.toLowerCase();
		return playerChat.containsKey(name) ? playerChat.get(name) - System.currentTimeMillis() : 0;
	}
	
	public static void updateCd(Player p)
	{
		String name = p.getName().toLowerCase();
		
		if(playerChat.containsKey(name))
		{
			long end = playerChat.get(name) - System.currentTimeMillis();
			
			if(end <= 0)
				playerChat.put(name, System.currentTimeMillis() + cdTime);
		}else
			playerChat.put(name, System.currentTimeMillis() + cdTime);
		
		name = null;
	}
}
