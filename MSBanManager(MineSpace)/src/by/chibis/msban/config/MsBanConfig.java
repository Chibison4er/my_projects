package by.chibis.msban.config;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

import by.chibis.msban.MsBanMain;
//import ru.tehkode.permissions.PermissionGroup;
//import ru.tehkode.permissions.bukkit.PermissionsEx;
import mspermissions.MSPermissions;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class MsBanConfig 
{
	private static File file;
	private static YamlConfiguration config;
	private static String path = "plugins/%s/config.yml";
	
	private static ArrayList<String> pexGroups = new ArrayList<>();
	private static String[] reasons = {"Гриферство", "Мат", "Реклама"};
	
	private static String[] banMsg = 
		{
			"Вы были забанены на сервере &4YourServer&r",
			"Ваш ник: %target%",
			"Кто забанил; %who%",
			"Причина бана: %reason%",
			"Время окончания бана: %time%",
		};
	
	private static String startBanList = "======[START]======";
	private static String endBanList = "======[END]======";
	
	public static void createFile()
	{
		file = getFile();
		config = YamlConfiguration.loadConfiguration(file);
		
		if(!file.exists())
		{
			try{ file.createNewFile(); }catch(Throwable e){}
			
			if(MsBanMain.useMsPex)
			{
				for(String g : MSPermissions.Companion.getGroups())
					pexGroups.add(g + ":" + 0);
			}
			else
			{
				for(PermissionGroup g : PermissionsEx.getPermissionManager().getGroupList())
					pexGroups.add(g.getIdentifier() + ":" + 0);
			}
				
			config.set("db.host", "localhost");
			config.set("db.port", "3306");
			config.set("db.base", "db1");
			config.set("db.table", "MSBanManager");
			config.set("db.user", "root");
			config.set("db.pass", "123456");
			
			config.set("ladder", pexGroups);
			
			for(String s : pexGroups)
			{
				s = s.replaceAll(":0", "");
				
				config.set("groups." + s + ".banTime", "5m");
				config.set("groups." + s + ".muteTime", "5m");
				
				config.set("groups." + s + ".cooldowns", new String[]{"ban:10m","mute:10m","unban:10m","kick:10m"});
			}
			
			config.set("banmsg", banMsg);
			
			config.set("start", startBanList);
			config.set("end", endBanList);
			
			config.set("reasonFormatter", "&c%s &b%s");
			
			config.set("messages.prefix", "&7[&9MSBanManager&7] ");
			config.set("messages.noperm", "&eУ вас нет прав на использование этой команды!");
			config.set("messages.cd", "&eПодожди &c%s &eсекунд!");
			config.set("messages.unknowtime", "&eНеверный формат времени &c%s &e!");
			config.set("messages.avalabletime", "&eДоступные вам форматы времени!");
			config.set("messages.noargs", "&eНе достаточно аргументов для выполнения!");
			config.set("messages.alreadybanned", "&eИгрок &c%s&e уже забанен!");
			config.set("messages.antikick", "&eИгрок &c%s&e имеет иммунитет к кику!");
			config.set("messages.antiban", "&eИгрок &c%s&e имеет иммунитет к бану!");
			config.set("messages.antimute", "&eИгрок &c%s&e имеет иммунитет к муту!");
			config.set("messages.notbanned", "&eИгрок &c%s&e не забанен!");
			config.set("messages.noreason", "&eУказанной причины несуществует в списке причин!");
			config.set("messages.issues.banned", "&eИгрок &c%s &eбыл забанен игроком &c%s на &c%s &e(Мин.) &7Причина: &b%s");
			config.set("messages.issues.unbanned", "&eИгрок &c%s &eбыл разбанен игроком &c%s");
			config.set("messages.issues.inmute", "&eВы ещё будете молчать: &c%s");
			config.set("messages.issues.unmute", "&eИгрок &c%s &eбыл размучен игроком &c%s");
			config.set("messages.issues.kick", "&eИгрок &c%s &eбыл кикнут игроком &c%s &7Причина: &b%s");
			config.set("messages.issues.mute", "&eИгрок &c%s &eбыл замучен игроком &c%s на &c%s &e(Мин.) &7Причина: &b%s");
			
			for(int i = 0; i < reasons.length; i ++)
				config.set("reasons." + i, reasons[i]);
			
			try{ config.save(file); }catch(Throwable e){}
		}
		
		MsBanConfigStorage.loadConfig(config);
	}
	
	private static File getFile() { return new File( String.format(isWin() ? path : linuxPathSlash(path), MsBanMain.inst.name) ); }
	
	private static boolean isWin() { return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0; }
	
	private static String linuxPathSlash(String s) { return s.replaceAll("[\\*]", "\\"); }
}
