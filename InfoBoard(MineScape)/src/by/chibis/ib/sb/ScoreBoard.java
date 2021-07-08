package by.chibis.ib.sb;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import by.chibis.ib.Main;
import mspermissions.MSPermissions;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class ScoreBoard
{
	private Scoreboard scoreboard;

	private String title;
	private Map<String, Integer> scores;
	private HashMap<String, Integer> doubleScore;
	private List<Team> teams;

	public ScoreBoard(String title) 
	{
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.title = title;
		this.doubleScore = new HashMap<>();
		this.scores = Maps.newLinkedHashMap();
		this.teams = Lists.newArrayList();
	}

	public void addText(String text, Integer score, int pos)
	{
		doubleScore.put(text, score);
		scores.put(text + score.toString(), pos);
	}

	public void blankLine() {
		add(" ");
	}

	public void add(String text) {
		add(text, null);
	}

	public void add(String text, Integer score) {
		Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
		text = fixDuplicates(text);
		scores.put(text, score);
	}

	private String fixDuplicates(String text) {
		while (scores.containsKey(text))
			text += "§r";
		if (text.length() > 48)
			text = text.substring(0, 47);
		return text;
	}

	public void editScore(String text, int score)
	{
		if(scores.containsKey(text))
			scores.put(text, score);
		else
			add(text, score);
	}

	private Map.Entry<Team, String> createTeam(String text) {
		String result = "";
		if (text.length() <= 16)
			return new AbstractMap.SimpleEntry<>(null, text);
		Team team = scoreboard.registerNewTeam("text-" + scoreboard.getTeams().size());
		Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
		team.setPrefix(iterator.next());
		result = iterator.next();
		if (text.length() > 32)
			team.setSuffix(iterator.next());
		teams.add(team);
		return new AbstractMap.SimpleEntry<>(team, result);
	}

	public void build()
	{	
		update();
		
		Objective obj = scoreboard.registerNewObjective((title.length() > 16 ? title.substring(0, 15) : title), "dummy");
		obj.setDisplayName(title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		int index = scores.size();

		for (Map.Entry<String, Integer> text : scores.entrySet())
		{
			Map.Entry<Team, String> team = createTeam(text.getKey());
			Integer score = text.getValue() != null ? text.getValue() : index;
			
			OfflinePlayer player = Bukkit.getOfflinePlayer(team.getValue()+"§r");
			
			if (team.getKey() != null)
				team.getKey().addPlayer(player);
			obj.getScore(player).setScore(score);
			index -= 1;
		}
	}
	
	public void update()
	{
		if (this.scoreboard.getObjective("health") == null) 
		{
		//	this.scoreboard.getObjective("health").unregister();
			
			Objective health = scoreboard.registerNewObjective("health", "health");
			health.setDisplayName("/20 Health");
			health.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
	}

	public void reset() {
		title = null;
		scores.clear();
		for (Team t : teams)
			t.unregister();
		teams.clear();
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void send(Player... players) {
		for (Player p : players)
			p.setScoreboard(scoreboard);
	}

	public void send(Player p) 
	{
		if(scoreboard.getObjective("health") != null)
		{
			if(Main.inst.isPexEnable)
			{
				Team t = scoreboard.registerNewTeam(p.getName());
				t.setPrefix(MSPermissions.Companion.getPlayerPrefix(p.getName(), false));
				t.setNameTagVisibility(NameTagVisibility.ALWAYS);
				t.addPlayer(p);
				
				p.sendMessage("team set!");
			}
		}
		
		p.setScoreboard(scoreboard);
		
		//PacketPlayOutPlayerInfo i = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, new EntityPlayer[]{(EntityPlayer)p});
		((CraftPlayer) p).getHandle().triggerHealthUpdate();
	}

}