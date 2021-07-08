package by.chibis.megacase.award;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import by.chibis.megacase.MegaCase;
import by.chibis.megacase.manager.MegaCaseManager;
import by.chibis.megacase.utils.ItemUtils;
import by.chibis.megacase.utils.MegaUtils;

public class MegaAwardLoader 
{
	@SuppressWarnings("deprecation")
	public static void loadAwards()
	{
		ItemStack item;
		int chance, id = 1;
		boolean isGlow;
		String name, i;
		String[] arr;
		List<String> lore;
		int r,g,b;
		
		for(String key : MegaCase.config.getConfigurationSection("items").getKeys(false))
		{
			chance = MegaCase.config.getInt("items." + key + ".chance");
			
			isGlow = MegaCase.config.getBoolean("items." + key + ".glow");
			name = MegaCase.config.getString("items." + key + ".name");
			lore = MegaCase.config.getStringList("items." + key + ".lore");
			
			i = MegaCase.config.getString("items." + key + ".item");
			
			item = new ItemStack(Material.getMaterial(id));
			
			if(i.indexOf(":") >= 0)
			{
				arr = i.split(":");
				
				if(arr[0].equalsIgnoreCase("299"))
				{
					item = new ItemStack(Material.LEATHER_CHESTPLATE);
				}
			}else
				item = new ItemStack(Material.getMaterial(Integer.parseInt(i)));
			
			item = ItemUtils.setLore(item, lore);
			item = ItemUtils.setName(item, name);
			item = MegaUtils.setNBTStack(item, key);
			
			if(i.indexOf(":") >= 0)
			{
				arr = i.split(":");
				
				if(arr[0].equalsIgnoreCase("299"))
				{
					r = Integer.parseInt(arr[1]);
					g = Integer.parseInt(arr[2]);
					b = Integer.parseInt(arr[3]);
					
					id = Integer.parseInt(arr[0]);
					item = MegaUtils.getColodArmor(item, r,g,b);
				}
			}
			
			MegaCaseManager.addAward(new MegaAward(item, key, chance, isGlow));
		}
	}
}
