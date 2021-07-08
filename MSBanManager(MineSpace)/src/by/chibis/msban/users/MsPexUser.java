package by.chibis.msban.users;

import java.util.HashMap;
import java.util.Map;

import by.chibis.msban.groups.MsPexGroup;

public class MsPexUser 
{
	MsPexGroup group;
	HashMap<String, Long> cdCmds = new HashMap<>();
	
	public MsPexUser(MsPexGroup group)
	{
		this.group = group;
	}
	
	public void updateCmdCd(String cmd)
	{
		cdCmds.put(cmd, System.currentTimeMillis() + group.getCmdCD(cmd));
	}
	
	public boolean isCmdDone(String cmd)
	{
		if(cdCmds.containsKey(cmd))
			return (cdCmds.get(cmd) - System.currentTimeMillis()) <= 0;
		else
			return true;
	}
	
	public void setMSGroup(MsPexGroup g) { group = g; }
	
	public String getConvertedTime(String cmd)
	{
		if(cdCmds.containsKey(cmd))
			return convertSecondsToHMmSs(cdCmds.get(cmd));
		else
			return convertSecondsToHMmSs(System.currentTimeMillis());
	}
	
	public String[] getCdCmdsInfo()
	{
		String[] cmds = new String[cdCmds.size()];
		int i = 0;
		
		for(Map.Entry<String, Long> map : cdCmds.entrySet())
		{
			cmds[i] =  "/" + map.getKey() + " " + ((map.getValue() - System.currentTimeMillis()) <= 0 ? "Готова" : convertSecondsToHMmSs(map.getValue() ));
			
			i ++;	
		}
		
		return cmds;
	}
	
	public MsPexGroup getMsGroup() { return group; }
	
	public static String convertSecondsToHMmSs(long mills) 
	{
		mills = mills - System.currentTimeMillis();
		
		long seconds = mills / 1000;
		
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
}
