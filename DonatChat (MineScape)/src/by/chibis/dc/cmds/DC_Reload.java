package by.chibis.dc.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.dc.storage.DC_Storage;

public class DC_Reload implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
	{
		if(!(sender instanceof Player))
		{
			DC_Storage.reloadConfig();
			sender.sendMessage(DC_Storage.msg.get("reload"));
		}
		
		return true;
	}

}
