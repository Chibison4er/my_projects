package by.chibis.hack.nmshelper;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class HACK_NMS_Helper 
{
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
