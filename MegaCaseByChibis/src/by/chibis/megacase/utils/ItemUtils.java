package by.chibis.megacase.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

	public static ItemStack addGlow(ItemStack item) {
		EnchantGlow.addGlow(item);
		return item;
	}

	public static ItemStack setName(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name.replace('&', '�'));
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack setLore(ItemStack item, String... lore) {
		return setLore(item, Arrays.asList(lore));
	}

	public static ItemStack setLore(ItemStack item, List<String> lore) 
	{
		ItemMeta meta = item.getItemMeta();
		
		String line;
		
		for(int i = 0; i < lore.size(); i ++)
		{
			line = lore.get(i);
			lore.set(i, MegaUtils.getColored(line));
		}
		
		line = null;
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		meta = null;

		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack fromString(String item_string) {
		String[] parts = item_string.split(":");

		String mat_string = parts[0];
		String data_string = (parts.length > 1 ? parts[1] : null);
		String amount_string = (parts.length > 2 ? parts[2] : null);
		String ench_string = (parts.length > 3 ? parts[3] : null);

		class Ench {
			private int level;
			private Enchantment ench;

			public Ench(Enchantment ench, int level) {
				this.level = level;
				this.ench = ench;
			}

			public int getLevel() {
				return level;
			}

			public Enchantment getEnchantment() {
				return ench;
			}
		}

		List<Ench> enchantments = new ArrayList<Ench>();
		if (ench_string != null) {
			String[] enchs_string = ench_string.split(";");

			for (String str : enchs_string) {
				String enchant = str.replaceAll("[^A-Za-z_]", "");
				String level = str.replaceAll("[^\\d]", "");

				Enchantment enchantment = null;
				for (Enchantment ench : Enchantment.values()) {
					if (enchant.equalsIgnoreCase(ench.getName())) {
						enchantment = ench;
						break;
					}
				}

				int lvl = -1;
				try {
					lvl = Integer.valueOf(level);
				} catch (NumberFormatException e) {
				}

				if (lvl >= 0 && enchantment != null) {
					Ench ench = new Ench(enchantment, lvl);
					enchantments.add(ench);
				}
			}
		}

		try {
			Material mat = Material.getMaterial(mat_string);

			if (mat == null) {
				Bukkit.getLogger().warning("Unknown Material: " + mat_string);
				return null;
			}

			Byte data = (data_string != null ? Byte.valueOf(data_string) : null);
			Integer amount = (amount_string != null ? Integer.valueOf(amount_string) : null);

			ItemStack item = new ItemStack(mat);
			if (data != null)
				item = new ItemStack(mat, 1, (short) 1, data);

			if (amount != null)
				item.setAmount(amount);

			for (Ench ench : enchantments)
				item.addUnsafeEnchantment(ench.getEnchantment(), ench.getLevel());

			return item;
		} catch (NumberFormatException e) {
			return null;
		}

	}
}
