package by.chibis.msban.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.groups.MsBanType;
import by.chibis.msban.groups.MsPunishTimeType;
import by.chibis.msban.groups.MsTime;
import by.chibis.msban.sql.MsSql;
import by.chibis.msban.users.MsPexUser;
import by.chibis.msban.users.MsUserManager;
import mspermissions.MSPermissions;

public class MsMuteCommand implements CommandExecutor 
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
						if(MSPermissions.Companion.hasPermission(args[0], "msmanager.mute.protect"))
							p.sendMessage(String.format(MsBanConfigStorage.msgs.get("antimute"), args[0]));
						else
						{
							MsTime maxMuteTime = user.getMsGroup().getMuteTime();
							
							MsPunishTimeType maxMuteTimeType = maxMuteTime.getAvalableTimeType();
							int maxTime = maxMuteTime.getTime();
							
							String reason = "";
							reason = MsBanConfigStorage.reasons.get(args.length > 1 ? args[1] : "0");
							
							mutePlayer(p.getName(), args[0], reason, MsBanMain.inst.getBanTime(maxTime, maxMuteTimeType), maxTime, maxMuteTimeType);
							user.updateCmdCd(lable);
						}
						
					//	MSPermissions.Companion.hasPermission("PlayerName", "lol");
						
						/*MsPunishTimeType curMuteTime = MsBanMain.getTimeType(args[1]);
						
						try
						{
							int max = maxMuteTime.getTime();
							int cur = Integer.parseInt(args[1].replaceAll(curMuteTime.toString(), ""));
							
							if(MsBanMain.inst.isTimeAvalable(maxMuteTimeType, max, curMuteTime, cur))
							{
								String reason = "";
								reason = MsBanConfigStorage.reasons.get(args[2]);
								
								if(reason == null || reason.length() == 0)
									MsBanConfigStorage.showAvalableReason(p);
								else
								{
									mutePlayer(p.getName(), args[0], reason, MsBanMain.inst.getBanTime(cur, curMuteTime), cur, curMuteTime);
									user.updateCmdCd(lable);
								}

							}else
								MsBanConfigStorage.showAvalableTime(p, args[1], user, MsBanType.MUTE);
						}catch(Throwable e) { MsBanConfigStorage.showAvalableTime(p, args[1], user, MsBanType.MUTE); }*/
					}
				}
			}
		}else
		{
			if(args.length < 3)
				sender.sendMessage("No args!");
			else
			{
				MsPunishTimeType type = MsBanMain.getTimeType(args[1]);
				int time = Integer.parseInt(args[1].replaceAll(type.toString(), ""));
				
				String reason = "";
				reason = MsBanConfigStorage.reasons.get(args[2]);
				
				if(reason == null || reason.length() == 0)
					sender.sendMessage("No reason!");
				else
				{
					mutePlayer(sender.getName(), args[0], reason, MsBanMain.inst.getBanTime(time, type), time, type);
				}
			}
		}
		
		return true;
	}
	
	private void mutePlayer(String who, String target, String reason, long muteTime, int timeArgs, MsPunishTimeType typeArgs)
	{
		Player p = Bukkit.getPlayer(target);
		
		if(p != null)
		{
			Bukkit.broadcastMessage(String.format(MsBanConfigStorage.msgs.get("mute"), target, who, timeArgs + typeArgs.getName(), reason));
			MsSql.setPunish(target, who, reason, MsBanType.MUTE, muteTime);
		}
	}
}
