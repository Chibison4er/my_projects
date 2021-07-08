package by.chibis.easy.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.easy.EasyBan;
import by.chibis.easy.EasyBanType;
import by.chibis.easy.OtherMethods;
import by.chibis.easy.checker.PlayerGroupChecker;
import by.chibis.easy.groups.EasyGroupManager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyTempBanCommand implements CommandExecutor 
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			PlayerObject po = EasyPlayerPunishController.getPlayerObject(p.getName());
			
			if(po != null && po.getG().isWhetherTempBan())
			{
				if(po.getTempBanLimit() > 0)
				{
					if(args.length < 3)
						p.sendMessage(EasyLang.TEMPBAN_USAGE.getMsg(true));
					else
					{
						if(args[0].equalsIgnoreCase(p.getName().toLowerCase()))
							p.sendMessage(EasyLang.TEMPBAN_YOURSELF.getMsg(true));
						else
						{
							int banTime = 1;
							
							try
							{								
								banTime = Integer.parseInt(args[1]);
								
								if(banTime > po.getG().getTempBanInMinutes())
									p.sendMessage(String.format(EasyLang.TEMPBAN_MAX_TIME.getMsg(true), po.getG().getTempBanInMinutes()));
								else if(banTime <= 0)
									p.sendMessage(EasyLang.SYSTEM_LESS_TIME.getMsg(true));
								else
								{
									Player target = Bukkit.getPlayer(args[0]);
									
									if(po.getG().isWhetherPunishJunior())
									{
										if(EasyGroupManager.getGroupRank(PlayerGroupChecker.getUserG(args[0])) > EasyGroupManager.getGroupRank(po.getG().getGroup()))
										{
											crutchBan(p, args, target, banTime);
										}else
											p.sendMessage(EasyLang.TEMPBAN_DONATE_HIGTH.getMsg(true));
									}else
										crutchBan(p, args, target, banTime);
								}
								
							}catch(Throwable ex) { ex.printStackTrace(); p.sendMessage(EasyLang.TEMPBAN_USAGE.getMsg(true)); }
						}
					}
				}else
					p.sendMessage(EasyLang.TEMPBAN_LIMIT_OUT.getMsg(true));
				
			}else
				p.sendMessage(EasyLang.TEMPBAN_NOPERM.getMsg(true));
			
		}else
		{
			if(args.length < 3)
				sender.sendMessage("Usage: /tempban <player> <timeInMin> <reason>");
			else
			{
				int banTime = 1;
				
				try
				{
					banTime = Integer.parseInt(args[1]);
					banTime = banTime <= 0 ? 1 : banTime ; 
				}catch(Throwable ex) { sender.sendMessage("Time can not be 0 or negative number!"); return true; }
				
				Player target = Bukkit.getPlayer(args[0]);
				String reason = "";
				
				for(int i = 2; i < args.length; i ++)
					reason += args[i] + " ";
				
				if(target != null)
					target.kickPlayer(String.format(EasyBan.plugin.tempBanMsg, EasyBanType.TEMPBAN.getTag(), target.getName(), sender.getName(), reason, banTime));
				
				Bukkit.broadcastMessage(EasyBanType.TEMPBAN.getTag() + String.format(EasyLang.TEMPBAN_BROADCAST.getMsg(false), args[0], sender.getName(), banTime, reason));
				long timeBd = System.currentTimeMillis() + ((1000 * 60) * banTime);
				
				banPlayer(sender.getName(), args[0], reason, timeBd);
				
				sender.sendMessage("You ban player " + args[0] + " on " + banTime + " minuts!");
			}
		}
		
		return true;
	}
	
	void crutchBan(Player p, String[] args, Player target, int banTime)
	{
		EasyPlayerPunishController.getPlayerObject(p.getName()).setTempBanLimit( -1);
		
		String reason = "";
		
		for(int i = 2; i < args.length; i ++)
			reason += args[i] + " ";
		
		if(target != null)
			target.kickPlayer(String.format(EasyBan.plugin.tempBanMsg, EasyBanType.TEMPBAN.getTag(), target.getName(), p.getName(), reason, banTime));
		
		Bukkit.broadcastMessage(EasyBanType.TEMPBAN.getTag() + String.format(EasyLang.TEMPBAN_BROADCAST.getMsg(false), args[0], p.getName(), banTime, reason));
		long timeBd = System.currentTimeMillis() + ((1000 * 60) * banTime);
		
		banPlayer(p.getName(), args[0], reason, timeBd);
		
		//p.sendMessage("Вы забанили " + args[0] + " на " + banTime + " минут!");
	}
	void banPlayer(String p, String target, String reason, long time) 
	{
		MySqlWorker.addPlayerTempBan(target, p, reason, time ); 
		if(!p.equalsIgnoreCase("CONSOLE"))
			EasyPlayerPunishController.getPlayerObject(p).setTempBanLimit(-1);
	}
}
