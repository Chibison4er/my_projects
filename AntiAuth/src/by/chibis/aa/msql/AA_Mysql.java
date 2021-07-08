package by.chibis.aa.msql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import by.chibis.aa.Main;

public class AA_Mysql 
{
	static Connection connection;
	
	static String dbUrl;
	static String dbName;
	static String dbTable;
	static String dbUser;
	static String dbPass;
	
	public static void createTable(FileConfiguration config)
	{
		dbUrl = config.getString("bdUrl");
		dbName = config.getString("bdName");
		dbTable = config.getString("bdTable");
		dbUser = config.getString("bdUser");
		dbPass = config.getString("bdPass");
		
		openConnection();
		
		String sqlCreate = String.format(
				"CREATE TABLE IF NOT EXISTS %s "
				+ "(id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,"
				+ "ip VARCHAR(255),"
				+ "attemts INTEGER,"
				+ "time BIGINT"
				+ ")", dbTable);
		
		try
		{
			connection.createStatement().executeUpdate(sqlCreate);
		}catch(Throwable ex){ ex.printStackTrace(); }
		
		closeConnection();
	}
	
	public synchronized static void openConnection()
	{
		try
		{
			if(connection == null || connection.isClosed())
			{
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(dbUrl + dbName, dbUser, dbPass);
			}
		}catch(Throwable e){e.printStackTrace();}
	}
	
	public synchronized static void closeConnection()
	{
		try
		{
			
			if(connection != null || !connection.isClosed())
				connection.close();
			
		}catch(Throwable ex){ ex.printStackTrace(); }
	}
	
	public synchronized static int getAttemptsByIp(String ip)
	{
		openConnection();
		
		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `attemts` FROM %s.%s WHERE `ip` = '%s'", dbName, dbTable, ip));
			
			if(result.next())
				return result.getInt("attemts");
			else
				return 0;
			
		}catch(Throwable e){e.printStackTrace();}
		
		return 0;
	}
	
	public synchronized static void setAttempts(String ip, int add)
	{
		openConnection();
		
		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `attemts` FROM %s.%s WHERE `ip` = '%s'", dbName, dbTable, ip));
			
			if(result.next())
				connection.createStatement().executeUpdate(String.format("UPDATE %s.%s SET `attemts` = %s WHERE `ip` = '%s'", dbName, dbTable, add, ip));
			else
				connection.createStatement().executeUpdate(String.format("INSERT INTO %s.%s (ip, attemts) VALUES ('%s', %s)", dbName, dbTable, ip, add));
			
			result.close();
			result = null;
			
		}catch(Throwable e){ e.printStackTrace(); }
	}
	
	public synchronized static void setBan(String ip, long time)
	{
		openConnection();
		
		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `ip` FROM %s.%s WHERE `ip` = '%s'", dbName, dbTable, ip));
			
			if(result.next())
				connection.createStatement().executeUpdate(String.format("UPDATE %s.%s SET `time` = %s WHERE `ip` = '%s'", dbName, dbTable, time, ip));
			else
				connection.createStatement().executeUpdate(String.format("INSERT INTO %s.%s (ip, attemts, time) VALUES ('%s', %s, %s)", dbName, dbTable, ip, Main.inst.attempts, time));
			
			result.close();
			result = null;
			
		}catch(Throwable e){e.printStackTrace();}
	}
	
	public synchronized static void unBan(String ip)
	{
		openConnection();
		
		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `ip` FROM %s.%s WHERE `ip` = '%s'", dbName, dbTable, ip));
			
			if(result.next())
				connection.createStatement().executeUpdate(String.format("DELETE FROM %s.%s WHERE `ip` = '%s'", dbName, dbTable, ip));
			
		}catch(Throwable e){e.printStackTrace();}
		
	}
	
	public synchronized static long getBanTime(String ip)
	{
		openConnection();
		
		try
		{
			
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `time` FROM %s.%s WHERE `ip` = '%s'", dbName, dbTable, ip));
			
			if(result.next())
			{
				return result.getLong("time");
			}
			
		}catch(Throwable e){e.printStackTrace();}
		
		return 0;
	}
	
	public synchronized static HashMap<String, Integer> getAllAtt()
	{
		openConnection();
		
		HashMap<String, Integer> ips = new HashMap<>();
		
		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM %s.%s", dbName, dbTable));			
			
			while(result.next())
				ips.put(result.getString("ip"), result.getInt("attemts"));
			
		}catch(Throwable e){e.printStackTrace();}
		
		return ips;
	}
}
