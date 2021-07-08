package by.chibis.megacase.groups;

import by.chibis.megacase.MegaCase;
import by.chibis.megacase.parametrs.MegaParam;
import by.chibis.mysqlconnector.sqls.MySqlShopCart;

public class MegaCaseGroups 
{
	public static void editGroup(String winGroup, String playerName)
	{
		String curPlayerGroup = MySqlShopCart.getPlayerPrivileges(playerName);
		
		int curGroupRank = MegaParam.getGroupRank(curPlayerGroup);
		int winGroupRank = MegaParam.getGroupRank(winGroup);
		
		if(winGroupRank < curGroupRank || curPlayerGroup.equalsIgnoreCase("null"))
		{
			MySqlShopCart.editPlayerPrivileges(playerName, winGroup);
			MegaCase.instance.getServer().dispatchCommand(MegaCase.instance.getServer().getConsoleSender(), String.format(MegaParam.pexCmd, playerName, winGroup));
		}
	}
}
