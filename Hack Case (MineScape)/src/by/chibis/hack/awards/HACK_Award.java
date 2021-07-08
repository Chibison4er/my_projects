package by.chibis.hack.awards;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import by.chibis.hack.Main;
import by.chibis.hack.nmshelper.EnchantGlow;
import by.chibis.hack.nmshelper.HACK_NMS_Helper;
import by.chibis.hack.nmshelper.ItemUtils;

public class HACK_Award 
{
	String name, useCmd, key;
	ArrayList<String> lore = new ArrayList<>();

	int chance;
	
	ItemStack item;
	
	public HACK_Award(FileConfiguration config, String key)
	{
		this.key = key;
		
		name = Main.getColored(config.getString("items." + key + ".name"));
		chance = config.getInt("items." + key + ".chance");
		item = new ItemStack(Material.getMaterial(config.getInt("items." + key + ".item")));
		
		for(String s : config.getStringList("items." + key + ".lore"))
			lore.add(Main.getColored(s));
		
		useCmd = config.getString("items." + key + ".command");
		
		if(item == null)
			System.out.println("[HACK] Item " + config.getInt("items." + key + ".item") +  " from config key " + key + " is incorrect!");
		else
		{
			item = ItemUtils.setName(item, name);
			item = ItemUtils.setLore(item, lore);
			
			item = HACK_NMS_Helper.setNBTStack(item, key + ":" + useCmd);
		}
		
		if(config.getBoolean("items." + key + ".glow"))
			item = ItemUtils.addGlow(item);
	}
	
	public String getConfigKey() { return key; }
	public int getChance() { return chance; }
	public ItemStack getItem() { return item; }
}
