package by.chibis.ib.mainplaceholders;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import by.chibis.ib.Main;
import mstabheader.MSTabHeader;

public class msPlaceHolders 
{
	static String[] place = 
		{
			"%ping:0",
			"%gamemode:1",
			"%x:2",
			"%y:3",
			"%z:4",
			"%worldname:5",
			"%level:6",
			"%hunger:7",
			"%playername:8",
			"%online:9"
		};
	
	public enum msMode
	{
		SURVIVAL ("Выживание"),
		ADVENTURE ("Приключение"),
		CREATIVE ("Творчество"),
		SPECTATOR ("Наблюдатель");
		
		private String mode;
		
		private msMode(String s) { mode = s; }
		
		public String getMode() { return mode; }
	}
	
	public static String replacePH(String s, Player p)
	{	
		String replace = s;
		
		String[] arr;
		
		for(String ph : place)
		{
			arr = ph.split(":");
			replace = replace.replaceAll(arr[0], getInfo(arr[1], p));
//			System.out.println(replace);
		}
		
		arr = null;
		
		return replace;
	}
	
	private static String getInfo(String part, Player p)
	{
		switch(part)
		{
			case "0": { return Integer.valueOf(((CraftPlayer) p).getHandle().playerConnection.player.ping).toString(); }
			case "1": { return msMode.valueOf(p.getGameMode().toString()).getMode(); }
			case "2": { return Integer.valueOf(p.getLocation().getBlockX()).toString();}
			case "3": { return Integer.valueOf(p.getLocation().getBlockY()).toString();}
			case "4": { return Integer.valueOf(p.getLocation().getBlockZ()).toString();}
			case "5": { return p.getWorld().getName();}
			case "6": { return Integer.valueOf(p.getLevel()).toString();}
			case "7": { return Integer.valueOf(p.getFoodLevel()).toString() + "";}
			case "8": { return p.getName();}
			case "9": { return (MSTabHeader.Companion.getBungeeOnline() == null ? + Main.inst.getServer().getOnlinePlayers().size() : MSTabHeader.Companion.getBungeeOnline()) + ""; }
		}
		
		return "";
	}
}
