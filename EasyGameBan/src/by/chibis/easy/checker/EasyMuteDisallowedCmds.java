package by.chibis.easy.checker;

import java.util.ArrayList;

import by.chibis.easy.EasyBan;

public class EasyMuteDisallowedCmds 
{
	public static ArrayList<String> disallowCmds = (ArrayList<String>) EasyBan.plugin.config.getStringList("muteDisallowedCmds");
	
	public static boolean isCmdDisallow(String cmd)
	{
		return disallowCmds.contains(cmd);
	}
}
