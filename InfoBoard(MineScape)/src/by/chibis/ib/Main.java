package by.chibis.ib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.ib.NTE.NameTagManager;
import by.chibis.ib.mysql.Mysql;
import by.chibis.ib.sb.MS_SB;
import by.chibis.ib.sb.SB_ShowManager;
import by.chibis.ib.tabls.msBoard;
import by.chibis.ib.tabls.msTable;
import mspermissions.MSPermissions;
import mspermissions.event.GroupChangedEvent;
import mspermissions.event.PermissionsReloadedEvent;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener
{
	public static Main inst;
	public static HashMap<String, msTable> sqlTable = new HashMap<>();
	public static HashMap<Integer, msBoard> tableBoard = new HashMap<>();
	public static HashMap<String, String> formattersTable = new HashMap<>();
	 
	String welcome;
	
	FileConfiguration config;
	
	public boolean isPexEnable = false;
	
	public static msBoard curBoard;
	public static MS_SB mssb;
	
	public static NameTagManager nte;
	public static String hpSymbol = ChatColor.translateAlternateColorCodes('&', "<3");
	
	public PluginManager pm;
	
	public void onEnable()
	{
		inst = this;
		
		config = this.getConfig();
		this.saveDefaultConfig();

		String table, user, column, title, formatter;
		int time;
		ArrayList<String> text;
		boolean isMysql;
		
		nte = new NameTagManager();
		pm = this.getServer().getPluginManager();
		
		if(pm.getPlugin("MSPermissions") != null)
			isPexEnable = true;
		
		welcome = config.getString("welcome");
		hpSymbol = getColored(config.getString("symbol"));
		
		for(String key : config.getConfigurationSection("tables").getKeys(false))
		{
			table = config.getString("tables."+key+".table");
			user = config.getString("tables."+key+".user");
			column = config.getString("tables."+key+".count");
			formatter = config.getString("tables."+key+".formatter");

			isMysql = config.getBoolean( "tables." + key + ".isUseMysql");
			
			formattersTable.put(formatter, table);
			sqlTable.put(table, new msTable(table, user, column, isMysql, formatter));
			System.out.println("Load formatter " + formatter + " for " + table + " table!");
		}
		
		int bCount = 0;
		
		//board top
		String topTable;
		boolean top;
		int items = 0;
		String topFormatter;
		
		for(String key : config.getConfigurationSection("boards").getKeys(false))
		{
			title = config.getString("boards." + key + ".title");
			time = config.getInt("boards." + key + ".updatetime");
			text = (ArrayList<String>) config.getStringList("boards." + key + ".board");
			
			topTable = config.getString("boards." + key + ".useTableForTop");
			topFormatter = config.getString("boards." + key + ".topFormatter");
			top = config.getBoolean("boards." + key + ".useTop");
			items = config.getInt("boards." + key + ".items");
			
			tableBoard.put(bCount, new msBoard(title, text, time, top, items, topTable, topFormatter));
			
			bCount ++;
		}

		Thread sqlTread = new Thread(new Mysql());
		sqlTread.start();

		Thread showTread = new Thread(new SB_ShowManager());
		showTread.start();
		
		nte.updatePlayersPrefixes();
		
		pm.registerEvents(this, this);
		
		showIB();
	}
	
	private void showIB()
	{
		for(Player p : this.getServer().getOnlinePlayers())
		{
			if(curBoard != null)
				curBoard.showInfoBord(p);
		}
	}
	
	public void onDisable() 
	{
		for(Player p : this.getServer().getOnlinePlayers())
		{
			if(curBoard != null)
			{
				curBoard.getMSSB().unreg(p);
			}
		}
		
		nte.reset();
			
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(final PlayerJoinEvent e)
	{
		
		if(curBoard != null)
			curBoard.showInfoBord(e.getPlayer());
		
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			
			@Override
			public void run() 
			{
				nte.sendTeams(e.getPlayer());
				
				if(nte.getFakeTeam(e.getPlayer().getName()) == null)
					nte.setNametag(e.getPlayer().getName(), ChatColor.translateAlternateColorCodes('&', MSPermissions.Companion.getPlayerPrefix(e.getPlayer().getName(), false)), "");
			}
		}, 1l);
		
		if(isPexEnable)
		{
			if(!MSPermissions.Companion.getPlayerGroup(e.getPlayer().getName()).equalsIgnoreCase("default"))
				e.setJoinMessage(getColored(String.format(welcome, MSPermissions.Companion.getPlayerPrefix(e.getPlayer().getName(), false), e.getPlayer().getName())));
			else
				e.setJoinMessage(null);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGroupChange(GroupChangedEvent e)
	{
		nte.reset(e.getUsername());
		nte.setNametag(e.getUsername(), ChatColor.translateAlternateColorCodes('&', MSPermissions.Companion.getPlayerPrefix(e.getUsername(), false)), "");
	}
	
	@EventHandler
	public void onmsPexReload(PermissionsReloadedEvent e)
	{
		nte.reset();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			
			@Override
			public void run() 
			{
				nte.updatePlayersPrefixes();			
			}
		}, 10l);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) { e.setDeathMessage(null); }
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) { e.setQuitMessage(null); }

	public String getColored(String s){ return ChatColor.translateAlternateColorCodes('&', s); }

	public msTable getTableByName(String name) { return sqlTable.containsKey(name) ? sqlTable.get(name) : null; }
	public msBoard getBoardByindex(int index) { return tableBoard.containsKey(index) ? tableBoard.get(index) : null; }

	public void show()
	{
		for(Map.Entry<String, msTable> mapTable : sqlTable.entrySet())
		{
			System.out.println(mapTable.getKey() + " size: " + mapTable.getValue().getSize());
		}

	}
}
