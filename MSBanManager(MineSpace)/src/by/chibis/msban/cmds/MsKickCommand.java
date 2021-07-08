package by.chibis.msban.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.users.MsPexUser;
import by.chibis.msban.users.MsUserManager;
import mspermissions.MSPermissions;

public class MsKickCommand implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			MsPexUser user = MsUserManager.getMsUser(p.getName());
			
			if(user == null)
				p.sendMessage(MsBanConfigStorage.msgs.get("noperm"));
			else
			{
				if(args.length < 1)
					p.sendMessage(MsBanConfigStorage.msgs.get("noargs"));
				else
				{
					if(!user.isCmdDone(lable))
						p.sendMessage(String.format(MsBanConfigStorage.msgs.get("cd"), user.getConvertedTime(lable)));
					else
					{
						if(MSPermissions.Companion.hasPermission(args[0], "msmanager.kick.protect"))
							p.sendMessage(String.format(MsBanConfigStorage.msgs.get("antikick"), args[0]));
						else
						{
							String reason = "";
							reason =  MsBanConfigStorage.reasons.get( args.length > 1 ? args[1] : "0");
							
							kickUser(p.getName(), args[0], reason);
							user.updateCmdCd(lable);
						}
					}
				}
			}
		}else
		{
			if(args.length < 2)
				sender.sendMessage("No args!");
			else
			{
				String reason = "";
				reason = MsBanConfigStorage.reasons.get(args[1]);
				
				if(reason == null || reason.length() == 0)
					sender.sendMessage("No reason!");
				else
					kickUser(sender.getName(), args[0], reason);
			}
		}
		
		return true;
	}
	
	private void kickUser(String who, String target, String reason)
	{
		Player user = Bukkit.getPlayer(target);
		
		if(user != null)
		{
			user.kickPlayer(reason);
			Bukkit.broadcastMessage(String.format(MsBanConfigStorage.msgs.get("kick"), target, who, reason));
		}
	}

}
