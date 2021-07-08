package by.chibis.megacase.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import by.chibis.megacase.MegaCase;
import by.chibis.megacase.utils.ItemUtils;

public class MegaCaseGui 
{
	static String guiName = MegaCase.config.getString("guiName");
	static int guiSize = 9*4;
	static Inventory gui;
	
	public static void openCase(Player p)
	{
		gui = MegaCase.instance.getServer().createInventory(p, guiSize, guiName); 
		
		gui.setItem(13, new ItemStack(Material.HOPPER));
		gui.setItem(22, ItemUtils.setName(new ItemStack(Material.CHEST), "&aВаш приз"));
		gui.setItem(21, new ItemStack(Material.WOOL, 1, (byte) 1));
		gui.setItem(23, new ItemStack(Material.WOOL, 1, (byte) 1));
		gui.setItem(31, new ItemStack(Material.LOG));
		gui.setItem(30, new ItemStack(Material.DIAMOND_BLOCK, 1));
		gui.setItem(32, new ItemStack(Material.DIAMOND_BLOCK, 1));
		gui.setItem(29, new ItemStack(Material.WOOL, 1, (byte) 1));
		gui.setItem(33, new ItemStack(Material.WOOL, 1, (byte) 1));
		
		for(int i = 9; i < 36; i ++)
			if(gui.getItem(i) == null)
				gui.setItem(i, new ItemStack(Material.VINE));
	}
}
