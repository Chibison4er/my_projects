package by.chibis.mysqlconnector.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.mysqlconnector.sqls.MySqlShopCart;

public class MySqlRemoveCmd implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if(!(sender instanceof Player))
		{
			if(args.length > 0)
			{
				String target = args[0].toLowerCase();
				
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("pex user %s delete", target));
				MySqlShopCart.removePlayer(target);
				System.out.println(String.format("[MySqlConnector] User %s are removed from PeX & Shop DB", target));
			}
		}
		
		return true;
	}

}
