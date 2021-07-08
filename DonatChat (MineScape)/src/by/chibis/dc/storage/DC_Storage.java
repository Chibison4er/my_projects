package by.chibis.dc.storage;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import by.chibis.dc.Main;
import net.md_5.bungee.api.ChatColor;

public class DC_Storage 
{
	private static File file;
	private static YamlConfiguration config;
	
	private static String[] path = {"plugins//%s//config.yml", "plugins/%s/config.yml"};
	
	private static String prefix = "[Ms-DonatChat]";
	private static String[] msgs = 
		{
			"noPerm:&4No permission!",
			"cd: &6Wait %s seconds!",
			"format: [%s]%s say: %s",
			"noMsg: use /d <message>",
			"uCan:Вы можете использовать донат чат /d",
			"reload: Config reloaded!"
		};
	
	public static int cmdCd = 5;
	
	public static HashMap<String, String> msg = new HashMap<>();
	
	public static void createFile()
	{
		file = getFile();
		config = YamlConfiguration.loadConfiguration(file);
		
		if(!file.exists())
		{
			try { file.createNewFile(); }catch(Throwable e){}
			
			String[] arr;
			
			config.set("prefix", prefix);
			config.set("cdCmd", cmdCd);
			
			for(String s : msgs)
			{
				arr = s.split(":");
				config.set("chat." + arr[0], arr[1]);
			}
			
			
			try { config.save(file); }catch(Throwable e){}
		}
		
		reloadConfig();
	}
	
	public static void reloadConfig()
	{
		file = getFile();
		config = YamlConfiguration.loadConfiguration(file);
		
		prefix = getColored(config.getString("prefix"));
		cmdCd = config.getInt("cdCmd");
		
		for(String key : config.getConfigurationSection("chat").getKeys(false))
			msg.put(key, prefix + getColored(config.getString("chat." + key)));
	}
	
	public static String getColored(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
	
	static File getFile() { return new File( String.format(isWin() ? path[0] : path[1], Main.inst.pluginName) ); }
	static boolean isWin() { return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0; }
}
