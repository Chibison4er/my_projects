package by.chibis.easy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.easy.checker.PlayerGroupChecker;
import by.chibis.easy.cmds.EasyBanCommand;
import by.chibis.easy.cmds.EasyMuteCommand;
import by.chibis.easy.cmds.EasyTempBanCommand;
import by.chibis.easy.cmds.EasyTempMuteCommand;
import by.chibis.easy.cmds.EasyUnBanCommand;
import by.chibis.easy.cmds.EasyUnMuteCommand;
import by.chibis.easy.groups.EasyGroupManager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyBan extends JavaPlugin
{
	public static EasyBan plugin;
	public FileConfiguration config;
	public String tempBanMsg = "", banMsg = "";
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		config = this.getConfig();
		this.saveDefaultConfig();
		
		for(String s : config.getStringList("tempBanMsg"))
			tempBanMsg += s + "\n";
		for(String s : config.getStringList("banMsg"))
			banMsg += s + "\n";
		
		tempBanMsg = ChatColor.translateAlternateColorCodes('&', tempBanMsg);
		banMsg = ChatColor.translateAlternateColorCodes('&', banMsg);
		
		EasyGroupManager.loadGroupLadder();
		
		MySqlWorker.openConnection();
		
		this.getServer().getPluginManager().registerEvents(new EasyBanListener(), this);
		
		this.getCommand("ban").setExecutor(new EasyBanCommand());
		this.getCommand("tempban").setExecutor(new EasyTempBanCommand());
		this.getCommand("unban").setExecutor(new EasyUnBanCommand());
		this.getCommand("mute").setExecutor(new EasyMuteCommand());
		this.getCommand("tempmute").setExecutor(new EasyTempMuteCommand());
		this.getCommand("unmute").setExecutor(new EasyUnMuteCommand());
		
		String group ;
		
		for(Player p : this.getServer().getOnlinePlayers())
		{
			group = PlayerGroupChecker.getUserG(p.getName());
			
			if(group.equalsIgnoreCase("default"))
				return;
			else
			{
				if(EasyPlayerPunishController.getPlayerObject(p.getName()) == null)
					EasyPlayerPunishController.addPlayer(p.getName(), new PlayerObject(EasyGroupManager.getGroup(group)));
				else
					EasyPlayerPunishController.getPlayerObject(p.getName()).setG(EasyGroupManager.getGroup(group));
			}
			
			p = null;
			group = null;
		}
	}
	
	public void onDisable()
	{
		MySqlWorker.closeConnection();
	}
}
