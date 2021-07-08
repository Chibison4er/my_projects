package by.chibis.msban.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.groups.MsBanType;
import by.chibis.msban.groups.MsPexGroup;
import by.chibis.msban.groups.MsPunishTimeType;
import by.chibis.msban.groups.MsTime;
import by.chibis.msban.sql.MsSql;
import by.chibis.msban.users.MsPexUser;

public class MsBanConfigStorage 
{
	public static HashMap<String, String> mysql = new HashMap<>();
	public static HashMap<String, Integer> groupLadder = new HashMap<>();
	public static HashMap<String, MsPexGroup> groupPunish = new HashMap<>();
	public static HashMap<String, String> msgs = new HashMap<>();
	public static HashMap<String, String> reasons = new HashMap<>();
	public static String banMsg = "", reasonFormatter = "", start = "", end = "";

	private static String prefix;

	public static void loadConfig(YamlConfiguration config)
	{
		for(String dbKey : config.getConfigurationSection("db").getKeys(false))
			mysql.put(dbKey, config.getString("db." + dbKey));
		
		prefix = config.getString("messages.prefix");
		
		for(String reason : config.getConfigurationSection("reasons").getKeys(false))
			reasons.put(reason, config.getString("reasons." + reason));
		
		for(String s : config.getStringList("banmsg"))
			banMsg +=MsBanMain.getColored(s) + "\n";
		
		start = MsBanMain.getColored(config.getString("start"));
		end = MsBanMain.getColored(config.getString("end"));
		reasonFormatter = MsBanMain.getColored(config.getString("reasonFormatter"));
		
		String[] arr;
		
		for(String group : config.getStringList("ladder"))
		{
			arr = group.split(":");
			groupLadder.put(arr[0], Integer.parseInt(arr[1]));
		}
		
		for(String gKey : config.getConfigurationSection("groups").getKeys(false))
			groupPunish.put(gKey, new MsPexGroup(config, gKey));
		
		for(String msg : config.getConfigurationSection("messages").getKeys(false))
		{
			if(msg.equalsIgnoreCase("prefix"))
				continue;
			
			if(!(config.getString("messages." + msg).indexOf("messages.issues") >= 0))
				msgs.put(msg, MsBanMain.getColored(prefix + config.getString("messages." + msg)));
			else
				for(String msg2 : config.getConfigurationSection("messages.issues").getKeys(false))
				{
					if(!msgs.containsKey(msg2))
						msgs.put(msg2, MsBanMain.getColored(prefix + config.getString("messages.issues." + msg2)));
				}
		}
		
		MsSql.createTable();
	}
	
	public static void showAvalableReason(Player p)
	{
		p.sendMessage(msgs.get("noreason"));
		
		for(Map.Entry<String, String> map : reasons.entrySet())
			p.sendMessage(String.format(reasonFormatter, map.getKey(), map.getValue()));
	}
	
	public static void showAvalableTime(Player p, String unknowtime, MsPexUser user, MsBanType type)
	{
		p.sendMessage(String.format(msgs.get("unknowtime"), unknowtime));
		p.sendMessage(msgs.get("avalabletime"));
		
		switch(type)
		{
			case BAN: 
			{	
				for(String s : getCorrectFormats(user.getMsGroup().getBanTime()))
					p.sendMessage(s);
				
				break;
			}
			case MUTE:
			{ 
				for(String s : getCorrectFormats(user.getMsGroup().getMuteTime()))
					p.sendMessage(s);
				
				break;
			}
			default:
				break;		
		}
	}
	
	private static String[] getCorrectFormats(MsTime time)
	{
		String[] s = new String[time.getTimeTypes().size()];
		int i = 0;
		
		for(MsPunishTimeType tt : time.getTimeTypes())
		{
			s[i] = MsBanMain.getColored("&c" + tt.toString() + " &e" + tt.getName());
			
			i ++;
		}
		
		return s;
	}
}
