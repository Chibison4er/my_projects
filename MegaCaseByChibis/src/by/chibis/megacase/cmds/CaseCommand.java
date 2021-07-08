package by.chibis.megacase.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.megacase.MegaCase;
import by.chibis.megacase.manager.MegaCaseManager;
import by.chibis.megacase.parametrs.MegaParam;
import by.chibis.megacase.runner.MegaCaseRunner;
import by.chibis.mysqlconnector.sqls.MySqlKeys;

public class CaseCommand implements CommandExecutor
{
	MegaCase instance = MegaCase.instance;
	int time = 20;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			String name = p.getName().toLowerCase();
			
			if(args.length == 0 || args[0].equalsIgnoreCase("help"))
			{
				MegaParam.sendCmds(p);
				return true;
			}else
			{
				switch(args[0])
				{
					case "open":
					{
						if(p.hasPermission(MegaParam.getCmd("open").getPermission()))
						{
							int curKeys = MySqlKeys.getPlayerKeysCountFromKeysDB(name);
							
							if(curKeys <= 0)
							{
								//MegaParam.sendMsg(p, String.format(MegaParam.getMsg("keys"), curKeys));
								MegaParam.sendNoKeys(p);
								break;
							}else
							{
								if(!MegaCaseManager.isPlayerRun(name))
								{
									new MegaCaseRunner(p, instance);
									MySqlKeys.editPlayerKeysCountInKeysDB(name, -1);
								}
							}
							
						}else
							p.sendMessage(MegaParam.getMsg("noperm"));
						
						break;
					}
					case "count":
					{
						if(p.hasPermission(MegaParam.getCmd("count").getPermission()))
						{
							int keys = MySqlKeys.getPlayerKeysCountFromKeysDB(name);
							MegaParam.sendMsg(p, String.format(MegaParam.getMsg("keys"), keys));
						}else
							p.sendMessage(MegaParam.getMsg("noperm"));
						
						break;
					}
					case "addkeys":
					{
						if(p.hasPermission(MegaParam.getCmd("addkeys").getPermission()))
						{
							try
							{
								if(args.length >= 2)
								{	
									String target = args[1];
									Integer keys = Integer.parseInt(args[2]);
									
									MySqlKeys.editPlayerKeysCountInKeysDB(target, keys);
									p.sendMessage(String.format("Gived %s keys to %s!", keys, target));
									instance.getLogger().info(String.format("Gived %s keys to %s!", keys, target));
								}
							}catch(Throwable ex)
							{
								p.sendMessage("[ERROR] Key must be digit!");
							}
						}else
							p.sendMessage(MegaParam.getMsg("noperm"));
						
						break;
					}
					case "removekeys":
					{
						if(p.hasPermission(MegaParam.getCmd("removekeys").getPermission()))
						{
							
						}else
							p.sendMessage(MegaParam.getMsg("noperm"));
						
						break;
					}
				}
			}
		}else
		{
			if(args[0].equalsIgnoreCase("setkeys"))
			{
				try
				{
					if(args.length >= 2)
					{	
						String target = args[1];
						Integer keys = Integer.parseInt(args[2]);
						
						MySqlKeys.editPlayerKeysCountInKeysDB(target, keys);
						instance.getLogger().info(String.format("Gived %s keys to %s!", keys, target));
					}
				}catch(Throwable ex)
				{
					sender.sendMessage("[ERROR] Key must be digit!");
				}
			}
		}
		
		return true;
	}

}
