package by.chibis.hack.controller;

import java.util.HashMap;

import by.chibis.hack.awards.HACK_AwardLoader;

public class HACK_Controller 
{
	public static HashMap<String, Long> cd = new HashMap<>();
	private static int time = HACK_AwardLoader.cdtime;
	
	public static void updateCD(String name)
	{
		name = name.toLowerCase();
		
		if(cd.containsKey(name))
		{
			long t = cd.get(name) - System.currentTimeMillis();
			
			if(t <= 0)
				cd.put(name, System.currentTimeMillis() + (time * 1000));
		}else
			cd.put(name, System.currentTimeMillis() + (time * 1000));
	}
	
	public static boolean hasCD(String name)
	{
		name = name.toLowerCase();
		
		return cd.containsKey(name) ? (cd.get(name) - System.currentTimeMillis()) <= 0 : false;
	}
}
