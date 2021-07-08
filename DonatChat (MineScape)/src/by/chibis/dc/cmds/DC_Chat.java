package by.chibis.dc.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.dc.Main;
import by.chibis.dc.controller.DC_ChatController;
import by.chibis.dc.storage.DC_Storage;
import mspermissions.MSPermissions;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DC_Chat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(!p.hasPermission("msdc.chat"))
				p.sendMessage(DC_Storage.msg.get("noPerm"));
			else
			{
				if(args.length == 0)
					p.sendMessage(DC_Storage.msg.get("noMsg"));
				else
				{
					long time = DC_ChatController.isTimeEnd(p.getName());
					
					if(time <= 0 )
					{
						DC_ChatController.updateCd(p);
						
						String msg = "";
						
						for(int i = 0; i < args.length; i ++)
							msg += args[i] + " ";
						
						for(Player z : Main.inst.getServer().getOnlinePlayers())
							if(z.hasPermission("msdc.chat"))
								if(Main.isMsPexEnable)
									z.sendMessage(DC_Storage.getColored(
											String.format(DC_Storage.msg.get("format"),
											MSPermissions.Companion.getPlayerPrefix(p.getName(), false), 
											p.getName(), msg)));
								else if(Main.isPexEnable)
									z.sendMessage(DC_Storage.getColored(
											String.format(DC_Storage.msg.get("format"),
											PermissionsEx.getUser(p).getPrefix(), 
											p.getName(), msg)));
					}else
						p.sendMessage(String.format(DC_Storage.msg.get("cd"), time/1000));
				}
			}
		}
		
		return true;
	}

}
