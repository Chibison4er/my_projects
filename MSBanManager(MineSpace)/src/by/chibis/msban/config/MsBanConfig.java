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
	private static String[] reasons = {"����������", "���", "�������"};
	
	private static String[] banMsg = 
		{
			"�� ���� �������� �� ������� &4YourServer&r",
			"��� ���: %target%",
			"��� �������; %who%",
			"������� ����: %reason%",
			"����� ��������� ����: %time%",
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
			config.set("messages.noperm", "&e� ��� ��� ���� �� ������������� ���� �������!");
			config.set("messages.cd", "&e������� &c%s &e������!");
			config.set("messages.unknowtime", "&e�������� ������ ������� &c%s &e!");
			config.set("messages.avalabletime", "&e��������� ��� ������� �������!");
			config.set("messages.noargs", "&e�� ���������� ���������� ��� ����������!");
			config.set("messages.alreadybanned", "&e����� &c%s&e ��� �������!");
			config.set("messages.antikick", "&e����� &c%s&e ����� ��������� � ����!");
			config.set("messages.antiban", "&e����� &c%s&e ����� ��������� � ����!");
			config.set("messages.antimute", "&e����� &c%s&e ����� ��������� � ����!");
			config.set("messages.notbanned", "&e����� &c%s&e �� �������!");
			config.set("messages.noreason", "&e��������� ������� ������������ � ������ ������!");
			config.set("messages.issues.banned", "&e����� &c%s &e��� ������� ������� &c%s �� &c%s &e(���.) &7�������: &b%s");
			config.set("messages.issues.unbanned", "&e����� &c%s &e��� �������� ������� &c%s");
			config.set("messages.issues.inmute", "&e�� ��� ������ �������: &c%s");
			config.set("messages.issues.unmute", "&e����� &c%s &e��� �������� ������� &c%s");
			config.set("messages.issues.kick", "&e����� &c%s &e��� ������ ������� &c%s &7�������: &b%s");
			config.set("messages.issues.mute", "&e����� &c%s &e��� ������� ������� &c%s �� &c%s &e(���.) &7�������: &b%s");
			
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
