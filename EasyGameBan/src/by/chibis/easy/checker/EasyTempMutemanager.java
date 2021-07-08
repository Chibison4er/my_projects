package by.chibis.easy.checker;

import java.util.HashMap;

public class EasyTempMutemanager 
{
	public static HashMap<String, PlayerPunish> playerMuted = new HashMap<>();
	
	public static void addPlayer(String name, PlayerPunish pp) { playerMuted.put(name, pp); }
	public static PlayerPunish getPlayerPunish(String name) { return playerMuted.containsKey(name) ? playerMuted.get(name) : null; }
	public static void removePlayer(String name) { playerMuted.remove(name); }
}
