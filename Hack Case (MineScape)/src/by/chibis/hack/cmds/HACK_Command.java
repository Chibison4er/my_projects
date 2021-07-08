package by.chibis.hack.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.hack.controller.HACK_Controller;
import by.chibis.hack.rotation.HACK_Rotation;

public class HACK_Command implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			if(!p.hasPermission("hack"))
				p.sendMessage("No permissoin: \"hack\" ");
			else
			{
				if(!HACK_Controller.hasCD(p.getName()))
					new HACK_Rotation(p);
				else
					p.sendMessage("Попробуй взломать позже!");
			}
		}
		
		return true;
	}

}
