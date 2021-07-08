package by.chibis.msban.groups;

import java.util.ArrayList;

import by.chibis.msban.MsBanMain;

public class MsTime 
{
	MsPunishTimeType max;
	int time;
	
	ArrayList<MsPunishTimeType> availableTimeTypes = new ArrayList<>();
	
	public MsTime(String time) 
	{
		time = time.toLowerCase();
		
		MsPunishTimeType type = MsBanMain.getTimeType(time);
		max = type;
		this.time = Integer.parseInt(time.replaceAll(type.toString(), ""));
		
		int index = type.getIndex();
		
		for(int i = MsPunishTimeType.values().length-1; i >= 0; i --)
		{
			type = MsPunishTimeType.values()[i];
		
			if(type.getIndex() <= index)
				availableTimeTypes.add(type);
		}
		
//		System.out.println("Time: " + time + " TimeType: " + type.toString());
		
	}
	
	public MsPunishTimeType getAvalableTimeType() { return max; }
	public ArrayList<MsPunishTimeType> getTimeTypes() { return availableTimeTypes; }
	public Integer getTime() { return time; }
}
