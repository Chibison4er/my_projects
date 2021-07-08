package by.chibis.hack.awards;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import by.chibis.hack.Main;

public class HACK_AwardLoader 
{
	public static HashMap<String, HACK_Award> hackAwards = new HashMap<>();
	public static String hackInvName = "";
	public static boolean hardMode = false;
	public static String win, lose, prefix, start, later;
	public static ItemStack firstLine, secondLine;
	public static int cdtime = 5;
	
	public static void loadAwards(FileConfiguration config)
	{
		hardMode = config.getBoolean("setHard");
		
		hackInvName = Main.getColored(config.getString("invName"));
		
		cdtime = config.getInt("cdtime");
		
		firstLine = new ItemStack(Material.getMaterial(config.getInt("firstLine")));
		secondLine = new ItemStack(Material.getMaterial(config.getInt("secondLine")));
		
		prefix = Main.getColored(config.getString("prefix"));
		later = prefix + Main.getColored(config.getString("later"));
		start = prefix + Main.getColored(config.getString("start"));
		win = prefix + Main.getColored(config.getString("win"));
		lose = prefix + Main.getColored(config.getString("lose"));
		
		for(String key : config.getConfigurationSection("items").getKeys(false))
			hackAwards.put(key, new HACK_Award(config, key));
	}
}
