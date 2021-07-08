package by.chibis.ib.tabls;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class msTable 
{
	String userNameColum;
	String userCountColum;
	String table;
	String formatter;
	
	boolean isUseMysql;
	Pattern pt;
	
	//HashMap<String, Integer> userData = new HashMap<>();
	HashMap<String, Object> userData = new HashMap<>();
	HashMap<String, Integer> sortedUserData = new HashMap<>();
	
	public msTable(String table, String userColum, String countColum, boolean useMysql, String formatter)
	{
		userCountColum = countColum;
		this.userNameColum = userColum;
		this.table = table;
		
		this.formatter = formatter;
		
		pt = Pattern.compile(formatter);
		
		isUseMysql = useMysql;
	}
	
	public boolean isUseMysql() { return isUseMysql; }
	public String getTable() { return table; }
	public String getUserColumn() { return userNameColum; }
	public String getUserCountColumn() { return userCountColum; }
	public String getPlaceHolder() { return formatter; }
	
	public HashMap<String, Integer> getPlayerMap() { return sortedUserData; }
	
	public void updateUserData(ResultSet result)
	{
		Object o;
		
		try
		{ 
			while(result.next())
			{
				o = result.getObject(userCountColum);
				userData.put(result.getString(userNameColum).toLowerCase(), o); 
				
				if(o instanceof Integer)
					sortedUserData.put(result.getString(userNameColum).toLowerCase(), (int) o);
				
			}
			
		}catch(Throwable e)
		{e.printStackTrace();}
		
		sortedUserData = (HashMap<String, Integer>) sortByValue(sortedUserData);
	}
	
	public int getSize() { return userData.size(); }
	
	public String replacePlaceHolders(String name, String text) 
	{ 
		if(pt.matcher(text).find())
			return pt.matcher(text).replaceAll(getUserData(name).toString());
		else
			return text;
	}
	
	public String getUserData(String user)
	{
		user = user.toLowerCase();
		return userData.containsKey(user) ? userData.get(user).toString() : "0";
	}
	
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map )
	{
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
		{
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2)
			{
				return (o2.getValue()).compareTo( o1.getValue() );
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
