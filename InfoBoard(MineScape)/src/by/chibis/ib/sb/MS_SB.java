package by.chibis.ib.sb;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import by.chibis.ib.Main;

public class MS_SB 
{
	private Scoreboard sb = Main.inst.getServer().getScoreboardManager().getNewScoreboard();
	private HashMap<String, Integer> score = new HashMap<>();
	private String title = "";
	
	private int index;
	
	public void addLine(String line, int index)
	{
		if(line == null || line.length() == 0)
			score.put(line + " ", index);
		else
			score.put(line, index);
	}
	
	public void setTitle(String title) { this.title = title; }
	
	public void update(Player p)
	{
		if(((CraftPlayer)p).getHealth() >= 20.0)
			((CraftPlayer)p).setHealth(((CraftPlayer)p).getHealth() - 0.01);
		else
			((CraftPlayer)p).setHealth(((CraftPlayer)p).getHealth() + 0.01);
		
		if(sb.getObjective("health") == null)
		{
			Objective hp = sb.registerNewObjective("health", "health");
			hp.setDisplayName(Main.hpSymbol);
			hp.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		
		if(sb.getObjective("text") != null)
			sb.getObjective("text").unregister();
		
		Objective text = sb.registerNewObjective("text", "dummy");
		text.setDisplayName(title);
		text.setDisplaySlot(DisplaySlot.SIDEBAR);
			
		for(Map.Entry<String, Integer> map : score.entrySet())
			text.getScore(map.getKey()).setScore(map.getValue());
		
	}
	
	public void send(Player p) 
	{	
		p.setScoreboard(sb);
	}
	
	public void unreg(Player p)
	{
		if(p.getScoreboard() != null)
		{
			for(Objective o : p.getScoreboard().getObjectives())
			{
				if(o != null)
					o.unregister();
			}
		}
	}
}
