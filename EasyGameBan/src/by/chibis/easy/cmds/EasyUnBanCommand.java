package by.chibis.easy.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.easy.EasyBanType;
import by.chibis.easy.OtherMethods;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyUnBanCommand implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			PlayerObject po = EasyPlayerPunishController.getPlayerObject(p.getName());
			
			if(po != null && po.getG().isWhetherUnBan())
			{
				
				if(po.getUnBanLimit() > 0)
				{
					
					if(args.length == 0)
						p.sendMessage(EasyLang.UNBAN_USAGE.getMsg(true));
					else
					{
						if(p.getName().toLowerCase().equalsIgnoreCase(args[0]))
							p.sendMessage(EasyLang.UNBAN_YOURSELF.getMsg(true));
						else
						{
							if(MySqlWorker.isUserHas(args[0]))
							{
								EasyBanType type = MySqlWorker.getBanType(args[0]);
								
								if(type == EasyBanType.BAN || type == EasyBanType.TEMPBAN)
								{
									OtherMethods.unBanPlayer(p.getName(), args[0]);
									//p.sendMessage("Игрок " + args[0] + " был разбанен!");
									Bukkit.broadcastMessage(EasyBanType.UNBAN.getTag() + String.format(EasyLang.UNBAN_PLAYER.getMsg(false), args[0], p.getName()));
								}else
									p.sendMessage(EasyLang.UNBAN_NOBAN.getMsg(true));
								
							}else
								p.sendMessage(EasyLang.UNBAN_NOBAN.getMsg(true));
						}
					}
					
				}else
					p.sendMessage(EasyLang.UNBAN_LIMIT_OUT.getMsg(true));
				
			}else
				p.sendMessage(EasyLang.UNBAN_NOPERM.getMsg(true));
			
		}else
		{
			if(args.length == 0)
				sender.sendMessage("Usage: /unban <player>");
			else
			{
				if(MySqlWorker.isUserHas(args[0]))
				{
					OtherMethods.unBanPlayer(sender.getName(), args[0]);
					Bukkit.broadcastMessage(EasyBanType.UNBAN.getTag() + String.format(EasyLang.UNBAN_PLAYER.getMsg(false), args[0], sender.getName()));
					sender.sendMessage("User " + args[0] + " unbaned!");
				}else
					sender.sendMessage("This player are not banned!");
			}
		}
		
		return true;
	}
}
