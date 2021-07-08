package by.chibis.msban.sql;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.groups.MsBanType;

public class MsPunished 
{
	String target, who;
	String time, reason;
	long t = 0;
	MsBanType type;
	
	public MsPunished(String t, String w, String ti, String r, long t1, MsBanType type) 
	{
		target = t;
		who = w;
		time = ti;
		reason = r;
		this.t = t1;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		time = (t - System.currentTimeMillis()) <= 0 ? "UNBANED" : MsBanMain.convertSecondsToHMmSs(t);
		
		return MsBanMain.getColored(String.format("&f[&c%s&f]&f[&c%s&f] - &f[&c%s&f] - &f[&c%s&f] - &f[&c%s&f]",type.toString(), target, time, who, reason));
	}
}
