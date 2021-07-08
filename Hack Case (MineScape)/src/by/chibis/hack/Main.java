package by.chibis.hack;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import by.chibis.hack.awards.HACK_AwardLoader;
import by.chibis.hack.cmds.HACK_Command;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener
{
	public static Main inst;
	FileConfiguration config;
	
	public void onEnable()
	{
		inst = this;
		
		config = this.getConfig();
		this.saveDefaultConfig();
		
		HACK_AwardLoader.loadAwards(config);
		
		this.getServer().getPluginManager().registerEvents(this, this);
		
		this.getCommand("hack").setExecutor(new HACK_Command());
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent e)
	{
		if(e.getInventory().getTitle().equalsIgnoreCase(HACK_AwardLoader.hackInvName))
			e.setCancelled(true);
	}
	
	public static String getColored(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
}