package by.chibis.megacase.manager;

import java.util.ArrayList;
import java.util.HashMap;

import by.chibis.megacase.award.MegaAward;
import by.chibis.megacase.utils.MegaUtils;

public class MegaCaseManager
{
	public static HashMap<String, Boolean> players = new HashMap<>();
	public static ArrayList<MegaAward> awards = new ArrayList<>();
	
	public static void setPlayerRun(String playerName) { players.put(playerName, true); }
	public static boolean isPlayerRun(String playerName) { return players.containsKey(playerName) ? players.get(playerName) : false; }
	public static void removePlayer(String playerName) { players.remove(playerName); }
	
	public static void addAward(MegaAward ma) { awards.add(ma); }
	public static MegaAward getAward(int index) { return awards.get(index); }
	
	public static String getNameItemByGroup(String group)
	{
		for(MegaAward ma : awards)
			if(MegaUtils.showTags(ma.getItem()).equalsIgnoreCase(group))
				return ma.getItem().getItemMeta().getDisplayName();
		
		return "";
	}
}