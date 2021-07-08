package by.chibis.ib.NTE.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Utils 
{
	  public static List<Player> getOnline()
	  {
	    List<Player> list = new ArrayList();
	    for (World world : Bukkit.getWorlds()) {
	      list.addAll(world.getPlayers());
	    }
	    return Collections.unmodifiableList(list);
	  }
}
