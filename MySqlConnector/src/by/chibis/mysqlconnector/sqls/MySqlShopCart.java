package by.chibis.mysqlconnector.sqls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import by.chibis.mysqlconnector.MySqlConnector;

public class MySqlShopCart 
{
	private static Connection connection;
	
	static String url = MySqlConnector.config.getString("shop.url");
	static String db =  MySqlConnector.config.getString("shop.db");
	static String table = MySqlConnector.config.getString("shop.table");
	static String user = MySqlConnector.config.getString("shop.user");
	static String pass = MySqlConnector.config.getString("shop.pass");
	
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
	
	public synchronized static boolean isUserHasPrivileges(String name)
	{
		boolean isHas = false;
		
		openConnection();
		
		Statement state;
		ResultSet result;
		
		try
		{
			
			state = connection.createStatement();
			result = state.executeQuery(String.format("SELECT `player` FROM `%s` WHERE `player` = '%s'", table,name));
			
			isHas = result.next();
			
			state.close();
			result.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
		
		return isHas;
	}
	
	public synchronized static void editPlayerPrivileges(String name, String donat)
	{
		openConnection();
		
		Statement state;
		ResultSet result;
		
		try
		{
			
			state = connection.createStatement();
			result = state.executeQuery(String.format("SELECT `player` FROM `%s` WHERE `player` = '%s'", table,name));
			
			if(!result.next())
				state.executeUpdate(String.format("INSERT INTO %s.%s (player, type, item, amount, server) VALUES ('%s', 'permgroup', '%s', 1, 1)",db, table, name, donat));
			else
				state.executeUpdate(String.format("UPDATE %s.%s SET item='%s' WHERE player='%s'",db, table, donat, name));
			
			state.close();
			result.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
	}
	
	public synchronized static String getPlayerPrivileges(String name)
	{
		String donat = "";
		openConnection();
		
		Statement state;
		ResultSet result;
		
		try
		{
			state = connection.createStatement();
			result = state.executeQuery(String.format("SELECT `item` FROM `%s` WHERE `player` = '%s'", table,name));
			donat = result.next() ? result.getString(1) : "null";
			
			state.close();
			result.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
		
		return donat;
	}
	
	public synchronized static void removePlayer(String name)
	{
		openConnection();
		
		Statement state;
		
		try
		{
			
			state = connection.createStatement();
			state.executeUpdate(String.format("DELETE FROM %s.%s WHERE player = '%s'", db, table, name));
			
			state.close();
			
		}catch(Throwable ex){}
		
		closeConnection();
	}
}
