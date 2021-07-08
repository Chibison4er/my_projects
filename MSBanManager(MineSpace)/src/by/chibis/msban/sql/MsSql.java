package by.chibis.msban.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.groups.MsBanType;

public class MsSql 
{
	static Connection connection;

	static String url = MsBanConfigStorage.mysql.get("host");
	static String port = MsBanConfigStorage.mysql.get("port");
	static String dbname = MsBanConfigStorage.mysql.get("base");
	static String table = MsBanConfigStorage.mysql.get("table");
	static String user = MsBanConfigStorage.mysql.get("user");
	static String pass = MsBanConfigStorage.mysql.get("pass");

	public synchronized static void createTable()
	{
		openConnection();

		try
		{
			String sqlCreate = String.format(
					"CREATE TABLE IF NOT EXISTS %s "
							+ "(id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,"
							+ "user VARCHAR(255),"
							+ "whobanned VARCHAR(255),"
							+ "bantype VARCHAR(255),"
							+ "reason VARCHAR(255),"
							+ "time BIGINT"
							+ ")", table);

			connection.createStatement().executeUpdate(sqlCreate);

		}catch(Throwable e){}
	}

	public synchronized static void loadPunished()
	{
		openConnection();
		
		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM %s.%s", dbname, table));

			String user = "", target = "", reason = "", time = "";
			long t = 0;
			MsBanType type = MsBanType.NONE;
			
			while(result.next())
			{
				type = MsBanType.valueOf(result.getString("bantype"));

				if(type == MsBanType.BAN || type == MsBanType.MUTE)
				{
					user = result.getString("whobanned");
					target = result.getString("user");
					reason = result.getString("reason");
					t = result.getLong("time");
					type = MsBanType.valueOf(result.getString("bantype"));
					
					time = (t - System.currentTimeMillis()) <= 0 ? "UNBANED" : MsBanMain.convertSecondsToHMmSs(t);
					
					
					
					MsPunishedPlayers.punish.add(new MsPunished(target, user, time, reason, t, type));
				}
			}

		}catch(Throwable e){ e.printStackTrace(); }
	}

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

				String con = String.format("jdbc:mysql://%s:%s/%s", url, port, dbname);

				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(con, properties);//user, pass
			}
		}catch(Throwable e){e.printStackTrace();}
	}

	public synchronized static void closeConnection()
	{
		try
		{
			if(connection != null || !connection.isClosed())
				connection.close();
		}catch(Throwable e){e.printStackTrace();}
	}

	public synchronized static MsBanType getUserPunish(String user)
	{
		user = user.toLowerCase();

		openConnection();

		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM %s.%s WHERE `user` = '%s'", dbname, table, user));

			MsBanType btype = MsBanType.NONE;

			if(result.next())
			{
				String type = result.getString("bantype");

				if(type == null || type.isEmpty())
					btype = MsBanType.NONE;
				else
					btype = MsBanType.valueOf(type);
			}

			return btype;
		}catch(Throwable e){e.printStackTrace();}

		return MsBanType.NONE;
	}

	public synchronized static long getPunishTime(String user)
	{
		user = user.toLowerCase();

		openConnection();

		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM %s.%s WHERE `user` = '%s'", dbname, table, user));

			if(result.next())
				return result.getLong("time");
			else
				return 0;

		}catch(Throwable e){e.printStackTrace();}

		return 0;
	}

	public synchronized static void unBan(String user)
	{
		openConnection();

		try
		{
			connection.createStatement().executeUpdate(String.format("DELETE FROM %s.%s WHERE `user` = '%s'", dbname, table, user));
		}catch(Throwable e){e.printStackTrace();}
	}

	public synchronized static void setPunish(String user, String whoBanned, String reason, MsBanType type, long time)
	{
		openConnection();

		user = user.toLowerCase();
		whoBanned = whoBanned.toLowerCase();

		try
		{

			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `user` FROM %s.%s WHERE `user` = '%s'", dbname, table, user));

			if(result.next())
				connection.createStatement().executeUpdate(String.format("UPDATE %s.%s SET `user` = '%s', `whobanned` = '%s', `reason` = '%s', `bantype` = '%s', `time` = %s", dbname, table, user, whoBanned, reason, type.toString(), time));
			else
				connection.createStatement().executeUpdate(String.format("INSERT INTO %s.%s (user, whobanned, reason, bantype, time) VALUES ('%s', '%s', '%s', '%s', %s)", dbname, table, user, whoBanned, reason, type.toString(), time));

			if(type == MsBanType.BAN || type == MsBanType.MUTE)
				MsPunishedPlayers.punish.add(new MsPunished(user, whoBanned, MsBanMain.convertSecondsToHMmSs(time), reason, time, type));

		}catch(Throwable e){e.printStackTrace();}
	}

	public synchronized static String getReason(String user)
	{
		user = user.toLowerCase();

		openConnection();

		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT `reason` FROM %s.%s WHERE `user` = '%s'", dbname, table, user));

			return result.next() ? result.getString("reason") : "No Reason!";

		}catch(Throwable e){e.printStackTrace();}

		return "No Reason!";
	}

	public synchronized static String getWhoSetBan(String user)
	{
		user = user.toLowerCase();

		openConnection();

		String who = "CONSOLE";

		try
		{
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM %s.%s WHERE `user` = '%s'", dbname, table, user));

			if(result.next())
				who = result.getString("whobanned");
			
		}catch(Throwable e) { e.printStackTrace(); }

		return who;
	}
}
