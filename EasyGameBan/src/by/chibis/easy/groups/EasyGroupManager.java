package by.chibis.easy.groups;

import java.util.HashMap;

import by.chibis.easy.EasyBan;

public class EasyGroupManager 
{
	public static HashMap<String, EasyGroup> groupSettings = new HashMap<>();
	public static HashMap<String, Integer> groupLadder = new HashMap<>();
	
	public static void loadGroupLadder()
	{
		String[] arr;
		
		for(String s : EasyBan.plugin.config.getStringList("groups"))
		{
			arr = s.split(":");
			groupLadder.put(arr[0], Integer.parseInt(arr[1]));
		}
		
		System.out.println("[EasyBan] Group ladder enabled & groups are loaded!");
		
		for(String groupKey : EasyBan.plugin.config.getConfigurationSection("groupPunishment").getKeys(false))
		{
			boolean ban, tempban, unban, mute, tempmute, unmute, pushjunior;
			int tempbantime, tempmutetime;
			int banlimit, tempbanlimit, unbanlimit, mutelimit, tempmutelimit, unmutelimit;
			
			//Group access
			ban = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherBan");
			tempban = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherTempBan");
			unban = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherUnBan");
			mute = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherMute");
			tempmute = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherTempMute");
			unmute = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherUnMute");
			pushjunior = EasyBan.plugin.config.getBoolean("groupPunishment." + groupKey + ".whetherPushJuniorGroup");
			//Punish time
			tempbantime = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".tempBanMaxTime");
			tempmutetime = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".tempMutemaxTime");
			//Punish limits
			banlimit = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".banLimit");
			tempbanlimit = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".tempBanLimit");
			unbanlimit = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".unBanLimit");
			mutelimit = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".muteLimit");
			tempmutelimit = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".tempMuteLimit");
			unmutelimit = EasyBan.plugin.config.getInt("groupPunishment." + groupKey + ".unMuteLimit");
			
			EasyGroup g = new EasyGroup(ban, tempban, unban, mute, tempmute, unmute, pushjunior, tempbantime, tempmutetime, banlimit, tempbanlimit, unbanlimit, mutelimit, tempmutelimit, unmutelimit);
			g.setGroup(groupKey);
			groupSettings.put(groupKey, g);
		}
		
		System.out.println("[EasyBan] Group settings are loaded!");
	}
	
	public static int getGroupRank(String group) { return groupLadder.containsKey(group) ? groupLadder.get(group) : Integer.MAX_VALUE; }
	public static EasyGroup getGroup(String gName) { return groupSettings.containsKey(gName) ? groupSettings.get(gName) : null; }
}
