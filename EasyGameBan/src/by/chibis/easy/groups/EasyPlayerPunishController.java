package by.chibis.easy.groups;

import java.util.HashMap;

public class EasyPlayerPunishController 
{
	public static HashMap<String, PlayerObject> players = new HashMap<>();
	
	public static void addPlayer(String name, PlayerObject po) { players.put(name, po); }
	public static PlayerObject getPlayerObject(String name) { return players.containsKey(name) ? players.get(name) : null; }
}
