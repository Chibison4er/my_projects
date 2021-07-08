package by.chibis.aa;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.aa.attempts.AA_Attempts;
import by.chibis.aa.bantypes.AA_Ban_Types;
import by.chibis.aa.listener.AA_Listener;
import by.chibis.aa.msql.AA_Mysql;
import fr.xephi.authme.AuthMe;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin
{
	public static Main inst;
	
	PluginManager pm;
	FileConfiguration config;
	
	public String reason = "";
	public int attempts = 5;
	int time = 1;
	public int recoilAttTime = 5;
	AA_Ban_Types type;
	
	Plugin pl;
	public AuthMe auth;
	
	public String attemptsLeft = "";
	String pluginName;
	
	File file;
	
	public void onEnable()
	{
		inst = this;
		
		config = this.getConfig();
		this.saveDefaultConfig();
		
		pluginName = this.getDescription().getName();
		
		reloadConfig();
		
		AA_Attempts aa = new AA_Attempts();
		Thread t = new Thread(aa);
		t.start();
		
		AA_Mysql.createTable(config);
		
		pm = this.getServer().getPluginManager();
		
		pl = pm.getPlugin("AuthMe");
		
		if(pl != null && pl instanceof AuthMe)
		{
			auth = (AuthMe) pl;
			
			pm.registerEvents(new AA_Listener(), this);
			this.getCommand("authprotect").setExecutor(new AA_Reload());
		}
	}
	
	public void reloadConfig()
	{
		file = new File( String.format(isWin() ? "plugins//%s//config.yml" : "plugins/%s/config.yml", pluginName) );
		
		if(!file.exists())
			return;
		
		reason = "";
		attempts = 5;
		time = 1;
		recoilAttTime = 5;
		attemptsLeft = "";
		
		config = YamlConfiguration.loadConfiguration(file);
		
		attempts = config.getInt("attemptsAuth");
		recoilAttTime = config.getInt("attemptsTimeRecoil");
		
		for(String s : config.getStringList("kickMsg"))
			reason += s + "\n";
		
		reason = ChatColor.translateAlternateColorCodes('&', reason);
		attemptsLeft = ChatColor.translateAlternateColorCodes('&', config.getString("attemptsLeft"));
		
		type = AA_Ban_Types.valueOf(config.getString("banTimeType"));
		time = config.getInt("banTime");
	}
	
	private boolean isWin() { return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0; }
	
	public long getBanTime()
	{
		switch(type)
		{
			case h: { return System.currentTimeMillis() + time * (1000*60*60); }
			case m: { return System.currentTimeMillis() + time * (1000*60); }
			case s: { return System.currentTimeMillis() + time * 1000; }	
		}
		
		return 0;
	}
}
