package by.chibis.easy.cmds;

import java.io.UnsupportedEncodingException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.easy.EasyBan;
import by.chibis.easy.EasyBanType;
import by.chibis.easy.OtherMethods;
import by.chibis.easy.checker.PlayerGroupChecker;
import by.chibis.easy.groups.EasyGroup;
import by.chibis.easy.groups.EasyGroupManager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyBanCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			EasyGroup g = EasyGroupManager.getGroup(PlayerGroupChecker.getUserG(p.getName()));
			
			if(g != null && g.isWhetherBan())
			{
				PlayerObject po = EasyPlayerPunishController.getPlayerObject(p.getName());
				
				if(po != null)
				{
					if(po.getBanLimit() > 0)
					{
						if(args.length < 2)
							p.sendMessage(EasyLang.BAN_USAGE.getMsg(true));
						else
						{
							if(p.getName().equalsIgnoreCase(args[0]))
								p.sendMessage(EasyLang.BAN_YOURSELF.getMsg(true));
							else
							{
								Player target = Bukkit.getPlayer(args[0]);
								String targetGroup = PlayerGroupChecker.getUserG(args[0]);
																
								if(po.getG().isWhetherPunishJunior())
								{
									if(EasyGroupManager.getGroupRank(targetGroup) > EasyGroupManager.getGroupRank(po.getG().getGroup()))
										crutchBan(p, args, target);
									else
										p.sendMessage(EasyLang.BAN_DONATE_HIGTH.getMsg(true));
								}else
									crutchBan(p, args, target);
							}
						}
					}else
						p.sendMessage(EasyLang.BAN_LIMIT_OUT.getMsg(true));
				}else
					p.sendMessage("Неизвесная группа! Обратитесь к администрации сервера для исправления ошибки!");
			}else
				p.sendMessage(EasyLang.BAN_NOPERM.getMsg(true));
			
			g = null;
		}else
		{
			if(args.length < 2)
				sender.sendMessage("Usage: /ban <player> <reason>");
			else
			{
				Player target = Bukkit.getPlayer(args[0]);
				
				String reason = "";
				
				for(int i = 1; i < args.length; i ++)
					reason += args[i] + " ";
				
				if(target != null)
					target.kickPlayer(String.format(EasyBan.plugin.banMsg, EasyBanType.BAN.getTag(), target.getName(), sender.getName(), reason ));
				
				Bukkit.broadcastMessage(EasyBanType.BAN.getTag() + String.format(EasyLang.BAN_BROADCAST.getMsg(false), args[0], sender.getName(), reason));
				
				sender.sendMessage("Player " + args[0] + " will be banned by reason: " + reason);
				
				banPlayer(args[0], sender.getName(), reason);
			}
		}
		
		return true;
	}
	
	void crutchBan(Player p, String[] args, Player target)
	{
		EasyPlayerPunishController.getPlayerObject(p.getName()).setBanLimit(-1);
		
		String reason = "";
		
		for(int i = 1; i < args.length; i ++)
			reason += args[i] + " ";
		
		if(target != null)
			target.kickPlayer(String.format(EasyBan.plugin.banMsg, EasyBanType.BAN.getTag(), target.getName(), p.getName(), reason ));
		
		Bukkit.broadcastMessage(EasyBanType.BAN.getTag() + String.format(EasyLang.BAN_BROADCAST.getMsg(false), args[0], p.getName(), reason));
		
		p.sendMessage(String.format(EasyLang.BAN_PLAYER.getMsg(true), args[0], reason));
		
		banPlayer(args[0], p.getName(), reason);
	}
	
	void banPlayer(String target, String whoBan, String reason) { MySqlWorker.addPlayerBan(target, whoBan, reason); }
}
