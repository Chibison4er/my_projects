package by.chibis.dc;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.dc.cmds.DC_Chat;
import by.chibis.dc.cmds.DC_Reload;
import by.chibis.dc.listener.DC_Listener;
import by.chibis.dc.storage.DC_Storage;

public class Main extends JavaPlugin
{
	public static Main inst;
	
	public String pluginName = "";
	PluginManager pm;
	public static boolean isPexEnable, isMsPexEnable;
	
	public void onEnable()
	{
		inst = this;
		pm = this.getServer().getPluginManager();
		
		pluginName = this.getDescription().getName();
		
		isPexEnable = pm.getPlugin("PermissionsEx") != null;
		isMsPexEnable = pm.getPlugin("MSPermissions") != null;
		
		DC_Storage.createFile();
		
		this.getCommand("d").setExecutor(new DC_Chat());
		this.getCommand("dc").setExecutor(new DC_Reload());
		
		pm.registerEvents(new DC_Listener(), this);
	}
}
