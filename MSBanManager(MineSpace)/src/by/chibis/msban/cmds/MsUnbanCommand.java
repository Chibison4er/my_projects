package by.chibis.msban.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.groups.MsBanType;
import by.chibis.msban.sql.MsSql;
import by.chibis.msban.users.MsPexUser;
import by.chibis.msban.users.MsUserManager;

public class MsUnbanCommand implements CommandExecutor
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
						if(MsSql.getUserPunish(args[0]) != MsBanType.BAN)
							p.sendMessage(String.format(MsBanConfigStorage.msgs.get("notbanned"), args[0]));
						else
						{
							unBanUser(p.getName(), args[0]);
							user.updateCmdCd(lable);
						}
					}
				}
			}
		}else
		{
			if(args.length == 0)
				sender.sendMessage("no args!");
			else
				unBanUser(sender.getName(), args[0]);
		}
		
		return true;
	}
	
	private void unBanUser(String who, String target)
	{
		Bukkit.broadcastMessage(String.format(MsBanConfigStorage.msgs.get("unbanned"), target, who));
		
		MsSql.unBan(target);
	}
}
