package by.chibis.megacase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.megacase.award.MegaAwardLoader;
import by.chibis.megacase.cmds.CaseCommand;
import by.chibis.megacase.listener.MegaCase_Listener;
import by.chibis.megacase.parametrs.MegaParam;
import by.chibis.mysqlconnector.MySqlConnector;

public class MegaCase extends JavaPlugin
{
	public static MegaCase instance;
	public static FileConfiguration config;
	
	PluginManager pm;
	Plugin plugin;
	
	public void onEnable()
	{
		instance = this;
		
		pm = this.getServer().getPluginManager();
		plugin = pm.getPlugin("MySqlConnector");
		
		if(plugin != null && plugin instanceof MySqlConnector)
		{
			config = this.getConfig();
			this.saveDefaultConfig();
			
			MegaAwardLoader.loadAwards();
			MegaParam.loadParams();
			
			pm.registerEvents(new MegaCase_Listener(), this);
			
			this.getCommand("case").setExecutor(new CaseCommand());
		}else
		{
			setEnabled(false);
			this.getLogger().info(
					"===================================\n"
					+ "MySqlConnector not detected!\n"
					+ "You use pirate MegaCase version\n"
					+ "Buy MegaCase at author in skype: zlodei910"
					+ "===================================\n"
					);
		}
	}
}
