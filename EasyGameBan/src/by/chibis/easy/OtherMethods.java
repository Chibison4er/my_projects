package by.chibis.easy;

import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.mysql.MySqlWorker;

public class OtherMethods
{
	public static void unBanPlayer(String who, String target) 
	{
		MySqlWorker.unBanPlayer(target);
		
		if(!who.equalsIgnoreCase("CONSOLE"))
			EasyPlayerPunishController.getPlayerObject(who).setUnBanLimit(-1);
	}
	
	public static String getStrWithNewLine(String s) { return s.replaceAll(" ","\n").replace("_", " "); }//\\s+
}
