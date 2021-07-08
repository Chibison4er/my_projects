package by.chibis.msban.groups;

import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import by.chibis.msban.MsBanMain;

public class MsPexGroup 
{
	MsTime banTime, muteTime;
	HashMap<String, Long> cmds = new HashMap<>();
	
	public MsPexGroup(YamlConfiguration config, String group)
	{
		banTime = new MsTime(config.getString("groups." + group + ".banTime"));
		muteTime = new MsTime(config.getString("groups." + group + ".muteTime"));
		
		String[] arr;
		
		long punishTime;
		MsTime time;
		
		for(String cdCmd : config.getStringList("groups." + group + ".cooldowns"))
		{
			arr = cdCmd.split(":");
			
			time = new MsTime(arr[1]);
			punishTime = MsBanMain.inst.getBanTime(time.getTime(), time.getAvalableTimeType()) - System.currentTimeMillis();
			
			cmds.put(arr[0], punishTime);
		}
	}
	
	public MsTime getBanTime() { return banTime; }
	public MsTime getMuteTime() { return muteTime; }
	
	public Long getCmdCD(String cmd) { return cmds.containsKey(cmd) ? cmds.get(cmd) : 0; }
}