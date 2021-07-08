package by.chibis.aa;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AA_Reload implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) 
	{
		
		if(!(sender instanceof Player))
		{
			Main.inst.reloadConfig();
			System.out.println("[AuthProtect] Config reloaded!");
		}
		
		return true;
	}

}
