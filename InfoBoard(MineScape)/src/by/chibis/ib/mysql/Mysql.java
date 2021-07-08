package by.chibis.ib.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import by.chibis.ib.Main;
import by.chibis.ib.tabls.msTable;

public class Mysql implements Runnable
{
	static FileConfiguration config = Main.inst.getConfig();
	
	static String url = config.getString("mysql.url");//"jdbc:mysql://ds-9b9349.minecraft-hosting.ru:3306";
	static String dbname = config.getString("mysql.name");//"db2";
	static String dbuser = config.getString("mysql.user");//"db2";
	static String dbpass = config.getString("mysql.pass");//"wfBASRPyqUNKNmzV";
	
	static Connection connection;
	
	static String request = "";
	
	@Override
	public void run()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.inst, new Runnable()
		{
			@Override
			public void run() { loadTables(); }
			
		}, 0, 5 * 20);
	}
	
	public synchronized static void loadTables()
	{
		
		openConnection();
		
		try
		{
			msTable mst;
			
			for(Map.Entry<String, msTable> map : Main.sqlTable.entrySet())
			{
				mst = map.getValue();
				
				if(mst.isUseMysql())
				{
					request = String.format("SELECT * FROM %s.%s", dbname, mst.getTable());
					mst.updateUserData(connection.createStatement().executeQuery(request));
				}
			}
			
		}catch(Throwable e) { e.printStackTrace(); }
		
	//	Main.inst.show();
	}
	
	public synchronized static void openConnection()
	{
		try
		{
			
			if(connection == null || connection.isClosed())
			{
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(url, dbuser, dbpass);
			}
			
		}catch(Throwable e){e.printStackTrace();}
	}
}
