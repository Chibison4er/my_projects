package by.chibis.mysqlconnector.sqls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import by.chibis.mysqlconnector.MySqlConnector;

public class MySqlKeys 
{
	private static Connection connection;
	
	static String url = MySqlConnector.config.getString("keys.url");
	static String db =  MySqlConnector.config.getString("keys.db");
	static String table = MySqlConnector.config.getString("keys.table");
	static String user = MySqlConnector.config.getString("keys.user");
	static String pass = MySqlConnector.config.getString("keys.pass");
	
	public synchronized static void openConnection()
	{
		try
		{
			if(connection == null || connection.isClosed())
			{
				Class.forName(MySqlConnector.driver);
				connection = DriverManager.getConnection(url + db, user, pass);
			}
				
		}catch(Exception ex){}
	}
	
	public synchronized static void closeConnection()
	{
		try
		{
			if(connection != null || !connection.isClosed())
				connection.close();
			
		}catch(Exception ex){}
	}
	
	public synchronized static boolean isUserHasInKeysDB(String name)
	{
		boolean isHas = false;
		openConnection();
		
		Statement state;
		ResultSet result;
		
		try
		{
			state = connection.createStatement();
			result = state.executeQuery(String.format("SELECT `name` FROM `%s` WHERE `name` = '%s'", table,name));
			isHas = result.next();
			
			state.close();
			result.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
		
		return isHas;
	}
	
	public synchronized static void editPlayerKeysCountInKeysDB(String name, int keys)
	{
		openConnection();
		
		Statement state;
		ResultSet result;
		
		try
		{
			state = connection.createStatement();
			result = state.executeQuery(String.format("SELECT `name` FROM `%s` WHERE `name` = '%s'", table,name));
			
			if(!result.next())
				state.executeUpdate(String.format("INSERT INTO %s.%s (name, count) VALUES ('%s', %s)",db, table, name, keys));
			else
				state.executeUpdate(String.format("UPDATE %s.%s SET count=count+%s WHERE name='%s'",db, table, keys,name));
			
			state.close();
			result.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
	}
	
	public synchronized static int getPlayerKeysCountFromKeysDB(String name)
	{
		openConnection();
		
		int keys = 0;
		
		Statement state;
		ResultSet result;
		
		try
		{
			state = connection.createStatement();
			result = state.executeQuery(String.format("SELECT count FROM %s.%s WHERE name = '%s';", db, table, name));
			
			keys = result.next() ? result.getInt(1) : 0;
			
			state.close();
			result.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
		
		return keys;
	}
}