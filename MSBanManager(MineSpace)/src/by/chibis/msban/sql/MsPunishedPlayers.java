package by.chibis.msban.sql;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import by.chibis.msban.config.MsBanConfigStorage;

public class MsPunishedPlayers 
{
	public static ArrayList<MsPunished> punish = new ArrayList<>();
	
	public static void showPlayer(Player p)
	{
		p.sendMessage(MsBanConfigStorage.start);
		
		for(MsPunished m : punish)
			p.sendMessage(m.toString());
		
		p.sendMessage(MsBanConfigStorage.end);
	}
}