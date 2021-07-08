package by.chibis.msban.users;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.groups.MsPexGroup;
import mspermissions.MSPermissions;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
//import ru.tehkode.permissions.PermissionGroup;
//import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MsUserManager 
{
	public static HashMap<String, MsPexUser> users = new HashMap<>();
	
	public static void addUser(String name)
	{			
		name = name.toLowerCase();
		
		if(users.containsKey(name))
		{
			if(MsBanMain.useMsPex)
			{
				String uG = MSPermissions.Companion.getPlayerGroup(name);
				users.get(name).setMSGroup(MsBanConfigStorage.groupPunish.get(uG));
				uG = null;
			}else
			{
				HashMap<String, Integer> userGroup = new HashMap<>();
				
				for(String group : PermissionsEx.getPermissionManager().getUser(name).getGroupNames())
					userGroup.put(group, MsBanConfigStorage.groupLadder.get(group));
				
				userGroup = (HashMap<String, Integer>) sortByValue(userGroup);
				
				for(String key : userGroup.keySet())
				{
					users.get(key).setMSGroup(MsBanConfigStorage.groupPunish.get(key));
					return;
				}
				
				userGroup.clear();
				userGroup = null;
			}
			
			return;
		}
		
		HashMap<String, Integer> userGroup = new HashMap<>();
		
		String uG = "default";
		
		if(MsBanMain.useMsPex)
		{
			uG = MSPermissions.Companion.getPlayerGroup(name);
			userGroup.put(uG, MsBanConfigStorage.groupLadder.get(uG));
		}
		else
		{
			for(String group : PermissionsEx.getPermissionManager().getUser(name).getGroupNames())
				userGroup.put(group, MsBanConfigStorage.groupLadder.get(group));
			
			userGroup = (HashMap<String, Integer>) sortByValue(userGroup);

		}
		
		//userGroup = (HashMap<String, Integer>) sortByValue(userGroup);
		MsPexGroup g;
		
		for(Map.Entry<String, Integer> map : userGroup.entrySet())
		{
			g = MsBanConfigStorage.groupPunish.get(map.getKey());
			
			users.put(name, new MsPexUser(g));
			return;
		}
	}
	
	public static MsPexUser getMsUser(String name)
	{
		name = name.toLowerCase();
		
		return users.containsKey(name) ? users.get(name) : null;
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
