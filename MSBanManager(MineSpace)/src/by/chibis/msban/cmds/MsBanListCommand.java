package by.chibis.msban.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.msban.sql.MsPunishedPlayers;

public class MsBanListCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3)
	{
		
		if(sender instanceof Player)
			MsPunishedPlayers.showPlayer((Player) sender );
		
		return true;
	}

}
