package by.chibis.mysqlconnector;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.megacase.manager.MegaCaseManager;
import by.chibis.mysqlconnector.cmds.MySqlRemoveCmd;
import by.chibis.mysqlconnector.sqls.MySqlKeys;
import by.chibis.mysqlconnector.sqls.MySqlShopCart;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MySqlConnector extends JavaPlugin implements Listener
{
	public static FileConfiguration config;
	public static String driver = "com.mysql.jdbc.Driver";
	
	public void onEnable()
	{
		config = this.getConfig();
		this.saveDefaultConfig();
		
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("removeuser").setExecutor(new MySqlRemoveCmd());
	}
	
	public void onDisable()
	{
		MySqlKeys.closeConnection();
		MySqlShopCart.closeConnection();
	}
	
	@EventHandler
	public void onInter(PlayerJoinEvent e)
	{
		String g = MySqlShopCart.getPlayerPrivileges(e.getPlayer().getName().toLowerCase());
		
		if(!hasPlayerGroupByShopBD(e.getPlayer(), g))
		{
			e.getPlayer().sendMessage(String.format(config.getString("donateUpdate"), MegaCaseManager.getNameItemByGroup(g)));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format(config.getString("pexCmd"), e.getPlayer().getName(), g));
		}
		
		g = null;
	}
	
	@SuppressWarnings("deprecation")
	static boolean hasPlayerGroupByShopBD(Player p, String g)
	{
		if(g.equalsIgnoreCase("null"))
			return true;
		
		for(String s : PermissionsEx.getUser(p).getGroupNames())
			if(s.indexOf(g) >= 0)
				return true;
		
		return false;
	}
}