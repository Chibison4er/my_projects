package by.chibis.megacase.award;

import org.bukkit.inventory.ItemStack;

import by.chibis.megacase.utils.ItemUtils;
import by.chibis.megacase.utils.MegaUtils;

public class MegaAward 
{
	int chance;
	String donat;
	ItemStack item;
	
	public MegaAward(ItemStack item, String donat, int chance, boolean isGlow)
	{
		this.chance = chance;
		this.item = isGlow ? ItemUtils.addGlow(item) : item;
		this.donat = donat;
		
		item = MegaUtils.setNBTStack(item, donat);
	}
	
	public String getDonateGroup() { return donat; }
	public ItemStack getItem() { return item; }
	public int getChance() { return chance; }
}
