package by.chibis.easy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import by.chibis.easy.EasyBan;
import by.chibis.easy.EasyBanType;

public class MySqlWorker 
{
	static Connection connection;
	static ResultSet result;
	static EasyBan plugin = EasyBan.plugin;
	
	static String host = plugin.config.getString("banUrl");
	static String dbName = plugin.config.getString("banDataBase");
	static String table = plugin.config.getString("banTable");
	static String user = plugin.config.getString("banUser");
	static String pass = plugin.config.getString("banPass");
	
	public synchronized static void openConnection()
	{
		try
		{
			if(connection == null || connection.isClosed())
			{
				Properties properties=new Properties();
		        properties.setProperty("user", user);
		        properties.setProperty("password", pass);
		        properties.setProperty("useUnicode","true");
		        properties.setProperty("characterEncoding","UTF-8");
				
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(host + dbName, properties);//user, pass
			}
				
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	public synchronized static void closeConnection()
	{
		try
		{
			if(connection != null || !connection.isClosed())
				connection.close();
			if(result != null)
				result.close();
			
		}catch(Exception ex){}
	}
	
	public synchronized static boolean isUserHas(String name)
	{
		openConnection();
		
		name = name.toLowerCase();
		boolean has = false;
		
		try 
		{
			result = connection.createStatement().executeQuery(String.format("SELECT `player` FROM %s WHERE `player` = '%s'", table, name));

			has = result.next();
			result.close();
			
			return has;
		} catch (SQLException e) {e.printStackTrace();}
		
		return has;
	}
	
	public synchronized static void addPlayerBan(String banPlayer, String whoSetBan, String reason)
	{
		openConnection();
		
		banPlayer = banPlayer.toLowerCase();
		
		try
		{
			
			if(isUserHas(banPlayer))
				connection.createStatement().executeUpdate(String.format("UPDATE %s.%s SET `whosetban` = '%s', `banreason` = '%s', `bantype` = '%s' WHERE `player` = '%s'", dbName, table, whoSetBan, reason, banPlayer, EasyBanType.BAN.toString()));
			else
				connection.createStatement().executeUpdate(String.format("INSERT INTO %s.%s (player, whosetban, banreason, bantype) VALUES ('%s', '%s', '%s', '%s')", dbName, table, banPlayer, whoSetBan, reason, EasyBanType.BAN.toString()));
			
			
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public synchronized static void addPlayerTempBan(String banPlayer, String whoSetBan, String reason, long endTime)
	{
		openConnection();
		 
		banPlayer = banPlayer.toLowerCase();
		
		try
		{
			
			if(isUserHas(banPlayer))
				connection.createStatement().executeUpdate(String.format("UPDATE %s.%s SET `whosettempban` = '%s', `tempbanreason` = '%s', `bantype` = '%s', `tempbanendtime` = %s WHERE `player` = '%s'", dbName, table, whoSetBan, reason, EasyBanType.TEMPBAN.toString(), endTime, banPlayer));
			else
				connection.createStatement().executeUpdate(String.format("INSERT INTO %s.%s (player, whosettempban, tempbanreason, bantype, tempbanendtime) VALUES ('%s', '%s', '%s', '%s', %s)", dbName, table, banPlayer, whoSetBan, reason, EasyBanType.TEMPBAN.toString(), endTime));
			
			
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public synchronized static void addPlayerMute(String banPlayer, String whoSetBan, String reason, EasyBanType type)
	{
		openConnection();
		 
		banPlayer = banPlayer.toLowerCase();
		
		try
		{
			
			if(isUserHas(banPlayer))
				connection.createStatement().executeUpdate(String.format("UPDATE %s.%s SET `whosetmute` = '%s', `mutereason` = '%s', `bantype` = '%s' WHERE `player` = '%s'", dbName, table, whoSetBan, reason, type.toString(), banPlayer));
			else
				connection.createStatement().executeUpdate(String.format("INSERT INTO %s.%s (player, whosetmute, mutereason, bantype) VALUES ('%s', '%s', '%s', '%s')", dbName, table, banPlayer, whoSetBan, reason, type.toString()));
			
			
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public synchronized static EasyBanType getBanType(String name)
	{
		openConnection();
		
		name = name.toString();
		
		try
		{
			result = connection.createStatement().executeQuery(String.format("SELECT bantype FROM %s.%s WHERE `player` = '%s';",dbName, table, name));
			String type = result.next() ? result.getString("bantype") : "NONE";
			result.close();
			
			return EasyBanType.valueOf(type);
		}catch(SQLException e){ e.printStackTrace(); }
		
		return EasyBanType.NONE;
	}
	
	public synchronized static String getWhoSetBan(EasyBanType type, String name)
	{
		openConnection();
		
		name = name.toLowerCase();
		String whoSet = "UNKNOW";
		
		try
		{
			switch(type)
			{
				case BAN:
				{
					result = connection.createStatement().executeQuery(String.format("SELECT `whosetban` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
					
					if(result.next())
						whoSet = result.getString("whosetban");
					
					break;
				}
				case TEMPBAN:
				{
					result = connection.createStatement().executeQuery(String.format("SELECT `whosettempban` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
					
					if(result.next())
						whoSet = result.getString("whosettempban");
					
					break;
				}
				case MUTE:
				{
					result = connection.createStatement().executeQuery(String.format("SELECT `whosetmute` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
					
					if(result.next())
						whoSet = result.getString("whosetmute");
					
					break;
				}
				default:
					break;
			}
			
			if(result != null)
				result.close();
			
		}catch(SQLException e){ e.printStackTrace(); }
		
		return whoSet;
	}
	
	public synchronized static long getEndTempBan(String name)
	{
		openConnection();
		name = name.toLowerCase();
		long time = 0;
		
		try
		{
			result = connection.createStatement().executeQuery(String.format("SELECT `tempbanendtime` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
			
			if(result.next())
				time = result.getBigDecimal("tempbanendtime").longValue();
			
			result.close();
			
			return time;
			
		}catch(SQLException e) { e.printStackTrace(); }
		
		return time;
	}
	
	public synchronized static String getReason(EasyBanType type, String name)
	{
		openConnection();
		
		name = name.toLowerCase();
		String whoSet = "UNKNOW";
		
		try
		{
			switch(type)
			{
				case BAN:
				{
					result = connection.createStatement().executeQuery(String.format("SELECT `banreason` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
					
					if(result.next())
						whoSet = result.getString("banreason");
					
					break;
				}
				case TEMPBAN:
				{
					result = connection.createStatement().executeQuery(String.format("SELECT `tempbanreason` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
					
					if(result.next())
						whoSet = result.getString("tempbanreason");
					
					break;
				}
				case MUTE:
				{
					result = connection.createStatement().executeQuery(String.format("SELECT `mutereason` FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
					
					if(result.next())
						whoSet = result.getString("mutereason");
					
					break;
				}
				default:
					break;
			}
			
			if(result != null)
				result.close();
			
		}catch(SQLException e){ e.printStackTrace(); }
		
		return whoSet;
	}
	
	public synchronized static void unBanPlayer(String name)
	{
		openConnection();
		
		name = name.toLowerCase();
		
		try
		{
			
			if(isUserHas(name))
				connection.createStatement().executeUpdate(String.format("DELETE FROM %s.%s WHERE `player` = '%s'", dbName, table, name));
		} catch (SQLException e) {e.printStackTrace();}
	}
}
