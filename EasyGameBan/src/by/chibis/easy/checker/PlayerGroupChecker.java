package by.chibis.easy.checker;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerGroupChecker 
{
	/*public static String getUserG(Player p)
	{
		String[] gLen = PermissionsEx.getPermissionManager().getUser(p).getGroupNames();
		HashMap<String, Integer> groups = new HashMap<>();
		
		if(gLen.length != 1)
		{
			int rank;
			
			for(String g : PermissionsEx.getPermissionManager().getUser(p).getGroupNames())
			{
				rank = Integer.parseInt(PermissionsEx.getPermissionManager().getGroup(g).getOption("rank"));
				groups.put(g, rank);
			}
			
			groups = (HashMap<String, Integer>) sortByValue(groups);
			
			int i = 0;
			
			for(Map.Entry<String, Integer> map : groups.entrySet())
			{
				if(i == 0)
					return map.getKey();
			}
			
			groups = null;
				
		}

		return gLen[0];
	}*/
	
	public static String getUserG(String name)
	{
		String[] gLen = PermissionsEx.getPermissionManager().getUser(name).getGroupNames();
		HashMap<String, Integer> groups = new HashMap<>();
		
		if(gLen.length != 1)
		{
			int rank;
			
			for(String g : PermissionsEx.getPermissionManager().getUser(name).getGroupNames())
			{
				rank = Integer.parseInt(PermissionsEx.getPermissionManager().getGroup(g).getOption("rank"));
				groups.put(g, rank);
			}
			
			groups = (HashMap<String, Integer>) sortByValue(groups);
			
			int i = 0;
			
			for(Map.Entry<String, Integer> map : groups.entrySet())
			{
				if(i == 0)
					return map.getKey();
			}
			
			groups = null;
				
		}

		return gLen[0];
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map )
	{
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
		{
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
			{
				return (o1.getValue()).compareTo( o2.getValue() );
			}
		} );

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
