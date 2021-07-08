package by.chibis.megacase.utils;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class MegaUtils 
{
	public static String getColored(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
	public static int getRandom( int max ) { return new Random().nextInt(max); }
	public static void launchFirework(Player p) { p.getWorld().spawn(p.getEyeLocation(), Firework.class) ;}
	public static String getDisplayName(ItemStack item)	{ return item.getItemMeta().getDisplayName(); }
	
	public static ItemStack getColodArmor(ItemStack is, int r, int g, int b)
	{
		if(is.getTypeId() == 299)
		{
			LeatherArmorMeta am = (LeatherArmorMeta) is.getItemMeta();
			am.setColor(Color.fromRGB(r, g, b));
			is.setItemMeta(am);
			am = null;
		}
		
		return is;
	}
	
	public static ItemStack setNBTStack(ItemStack stack, String donat)
	{
		net.minecraft.server.v1_8_R3.ItemStack is = CraftItemStack.asNMSCopy(stack);
		
		NBTTagCompound nbttag = is.getTag();
		
		if(!nbttag.hasKey("save"))
			nbttag.set("save", new NBTTagCompound());
		
		NBTTagList list = nbttag.getList("save", 0);
		
		list.add(new NBTTagString(donat));
		nbttag.set("save", list);
		
		is.save(nbttag);
		
		return CraftItemStack.asBukkitCopy(is);
	}
	
	public static String showTags(ItemStack item)
	{	
		net.minecraft.server.v1_8_R3.ItemStack is = CraftItemStack.asNMSCopy(item);
		 
		if(is == null) return null;
		
		NBTTagCompound nbt = is.getTag();
		
		if(nbt == null) return null;
		
		if(nbt.hasKey("save"))
		{
			String s = nbt.get("save").toString().replaceAll("[\\[\\]\\\"]", "");
			return s.substring(2);
		}
		
		return null;
	}
}