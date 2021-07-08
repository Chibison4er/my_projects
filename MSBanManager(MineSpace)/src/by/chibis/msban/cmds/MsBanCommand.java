package by.chibis.msban.cmds;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.groups.MsBanType;
import by.chibis.msban.groups.MsPexGroup;
import by.chibis.msban.groups.MsPunishTimeType;
import by.chibis.msban.groups.MsTime;
import by.chibis.msban.sql.MsSql;
import by.chibis.msban.users.MsPexUser;
import by.chibis.msban.users.MsUserManager;
import mspermissions.MSPermissions;

public class MsBanCommand implements CommandExecutor
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
						if(MsSql.getUserPunish(args[0]) == MsBanType.BAN)
							p.sendMessage(String.format(MsBanConfigStorage.msgs.get("alreadybanned"), args[0]));
						else
						{
							if(MSPermissions.Companion.hasPermission(args[0], "msmanager.ban.protect"))
								p.sendMessage(String.format(MsBanConfigStorage.msgs.get("antiban"), args[0]));
							else
							{
								String name = p.getName();
								
								MsPexGroup userGroup = user.getMsGroup();
								MsTime maxBanTime = userGroup.getBanTime();
								
								MsPunishTimeType maxPunishTimeTime = maxBanTime.getAvalableTimeType();
								int maxBan = maxBanTime.getTime();
								
								long banTime = MsBanMain.inst.getBanTime(maxBan, maxPunishTimeTime);
								
								String reason = "";
								reason =  MsBanConfigStorage.reasons.get( args.length > 1 ? args[1] : "0");
								
								if(reason == null || reason.length() == 0)
									reason = MsBanConfigStorage.reasons.get("0");
								
								user.updateCmdCd(lable);
								banUser(name, args[0], reason, banTime, maxBan, maxPunishTimeTime);
							}
						}
					}
				}
			}
		}else
		{
			if(args.length >= 3)
			{
				String reason = "";
				
				if(args.length > 3)
					for(int i = 3; i < args.length; i ++)
						reason += args[i];
				else
				{
					reason = MsBanConfigStorage.reasons.get(args[2]);
					
					if(reason == null)
					{
						sender.sendMessage("Avalable reasons for ban!");
						
						for(Map.Entry<String, String> avalableReasons : MsBanConfigStorage.reasons.entrySet())
							sender.sendMessage(avalableReasons.getKey() + " " + avalableReasons.getValue());
					}
				}
				
				MsPunishTimeType type = MsBanMain.getTimeType(args[1]);
				int timeArgs = Integer.parseInt(args[1].replaceAll(type.toString(), ""));
				
				long time = MsBanMain.inst.getBanTime(timeArgs, type);
				
				banUser(sender.getName(), args[0], reason, time, timeArgs, type);
			}else
				return false;
		}
		
		return true;
	}
	
	private void banUser(String who, String check, String reason, long time, int timeArgs, MsPunishTimeType typeArg)
	{
		Bukkit.broadcastMessage(String.format(MsBanConfigStorage.msgs.get("banned"), check, who, timeArgs + " " + typeArg.getName(), reason));
		MsSql.setPunish(check, who, reason, MsBanType.BAN, time);
		
		Player target = Bukkit.getPlayer(check);
		
		if(target != null)
		{
			String banMsg = MsBanConfigStorage.banMsg
					.replaceAll("%target%", check)
					.replaceAll("%who%", who)
					.replaceAll("%reason%", reason)
					.replaceAll("%time%", MsBanMain.convertSecondsToHMmSs(time));
			
			target.kickPlayer(banMsg);
		}
	}
}
