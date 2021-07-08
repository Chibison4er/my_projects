package by.chibis.megacase.parametrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import by.chibis.megacase.MegaCase;
import by.chibis.megacase.utils.MegaUtils;

public class MegaParam 
{
	static HashMap<String, MegaCmd> cmds = new HashMap<>();
	static HashMap<String, String> msgs = new HashMap<>();
	static HashMap<String, Integer> groups = new HashMap<>();
	static ArrayList<String> fotterUp = new ArrayList<>();
	static ArrayList<String> winMsgs = new ArrayList<>();
	static ArrayList<String> noKeys = new ArrayList<>();
	static String fotterEnd;
	public static int rollTimeDrop, rollTimeAdd, rollTimeEnd;
	public static boolean isRndPitch, isRndValue;
	public static float pitch, value;
	public static Sound sound;
	public static String guiName;
	public static String pexCmd;
	public static String prefix;
	public static double d;
	public static String cmd;
	
	public static void loadParams()
	{
		String[] arr;
		
		for(String s : MegaCase.config.getStringList("cmds"))
		{
			arr = s.split(":");
			cmds.put(arr[0], new MegaCmd(arr[1], arr[2]));
		}
		
		for(String s : MegaCase.config.getStringList("msgs"))
		{
			arr = s.split(";");
			msgs.put(arr[0], MegaUtils.getColored(arr[1]));
		}
		
		for(String s : MegaCase.config.getStringList("groups"))
		{
			arr = s.split(":");
			groups.put(arr[0], Integer.parseInt(arr[1]));
		}
		
		for(String s : MegaCase.config.getStringList("footers.up"))
			fotterUp.add(MegaUtils.getColored(s));
		
		for(String s : MegaCase.config.getStringList("winMsgs"))
			winMsgs.add(MegaUtils.getColored(s));
		
		for(String s : MegaCase.config.getStringList("noKeys"))
			noKeys.add(MegaUtils.getColored(s));
		
		fotterEnd = MegaUtils.getColored(MegaCase.config.getString("footers.end"));
		//rollTimeDrop
		d = MegaCase.config.getDouble("rollTimeDrop");
		rollTimeAdd = MegaCase.config.getInt("rollTimeAdd");
		rollTimeEnd = MegaCase.config.getInt("rollTimeEnd");
		
		pexCmd = MegaCase.config.getString("pexCommand");
		guiName = MegaUtils.getColored(MegaCase.config.getString("guiName"));
		prefix = MegaUtils.getColored(MegaCase.config.getString("prefix"));
		
		isRndValue = MegaCase.config.getBoolean("useRandomValue");
		isRndPitch = MegaCase.config.getBoolean("useRandomPitch");
		
		sound = Sound.valueOf(MegaCase.config.getString("soundname"));
		
		value = (float) MegaCase.config.getDouble("value");
		pitch = (float) MegaCase.config.getDouble("pitch");
		cmd = MegaUtils.getColored(MegaCase.config.getString("cmd"));
		
		arr = null;
	}
	
	public static MegaCmd getCmd( String cmd ) { return cmds.get(cmd); }
	public static HashMap<String, MegaCmd> getAllCmds() { return cmds; }
	public static String getMsg(String key) { return msgs.get(key); }
	public static int getGroupRank(String group) { return groups.containsKey(group) ? groups.get(group) : Integer.MAX_VALUE; }
	
	public static void sendMsg(Player p, String msg)
	{
		for(String s : fotterUp)
			p.sendMessage(s);
		
		p.sendMessage(msg + "\n" + fotterEnd);
	}
	
	public static void sendCmds(Player p)
	{
		for(String s : fotterUp)
			p.sendMessage(s);
		
		for(Map.Entry<String, MegaCmd> map : MegaParam.getAllCmds().entrySet())
			if(p.hasPermission(map.getValue().getPermission()))
				p.sendMessage(cmd + map.getKey() + map.getValue().getText());
		
		p.sendMessage(fotterEnd);
	}
	
	public static void broadcastWinMsg(String winner, String formatGroup)
	{
		for(String s : winMsgs)
			MegaCase.instance.getServer().broadcastMessage(MegaParam.prefix + String.format(s, winner, formatGroup));
	}
	
	public static void sendNoKeys(Player p)
	{
		for(String s : fotterUp)
			p.sendMessage(s);
		
		for(String s : noKeys)
			p.sendMessage(s);
		
		p.sendMessage(fotterEnd);
	}
}