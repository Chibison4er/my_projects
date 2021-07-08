package by.chibis.hack.rotation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import by.chibis.hack.Main;
import by.chibis.hack.awards.HACK_Award;
import by.chibis.hack.awards.HACK_AwardLoader;
import by.chibis.hack.controller.HACK_Controller;
import by.chibis.hack.nmshelper.HACK_NMS_Helper;

public class HACK_Rotation
{
	Player p;
	
	ArrayList<HACK_Award> awards = new ArrayList<>();
	Inventory inv;
	
	int caseMaxSteps = 25, curCaseSteps = 1;
	int maxGenerateItems = (caseMaxSteps + 9) * 3;
	int successGeneratedItem = 0;
	
	public HACK_Rotation(Player p) 
	{
		p.sendMessage(HACK_AwardLoader.start);
		
		this.p = p;
		
		HACK_Controller.updateCD(p.getName());
		
		inv = Main.inst.getServer().createInventory(p, 27, HACK_AwardLoader.hackInvName);
		
		for(int i = 0; i < 9; i ++)
			inv.setItem(i, HACK_AwardLoader.firstLine);
		
		for(int i = 18; i < 27; i ++)
			inv.setItem(i, HACK_AwardLoader.secondLine);
		
		loadHackAwards();
		reBuildCase();
		
		startHackCase();
	}
	
	private void startHackCase()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.inst, new Runnable()
		{
			
			@Override
			public void run() 
			{
				if(curCaseSteps >= caseMaxSteps)
				{					
					ItemStack item = inv.getItem(13);
					String[] arr = HACK_NMS_Helper.showTags(item).split(":");
					
					if(arr.length == 1)
						p.sendMessage(HACK_AwardLoader.lose);
					else
					{
						p.sendMessage(String.format(HACK_AwardLoader.win, item.getItemMeta().getDisplayName()));
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format(arr[1] + " %s", p.getName()));
					}
					
					p.closeInventory();
					
					awards.clear();
					awards = null;
				}else
				{					
					reBuildCase();
					startHackCase();
					curCaseSteps ++;
				}
			}
			
		}, curCaseSteps / 2);
	}
	
	private void reBuildCase()
	{
		for(int i = 9; i < 18; i ++ )
			inv.setItem(i, awards.get(i + curCaseSteps).getItem() );
		
		p.openInventory(inv);
	}
	
	private void loadHackAwards()
	{
		for(int i = 0; i <= maxGenerateItems; i ++)
			awards.add(generateAward());
	}
	
	private HACK_Award generateAward()
	{
		HACK_Award a = null;
		
		int rndChance = getRandomChance();
		
		if(HACK_AwardLoader.hardMode)
		{
			while(a == null)
			{
				for(Map.Entry<String, HACK_Award> map : HACK_AwardLoader.hackAwards.entrySet())
				{
					if(map.getValue().getChance() >= rndChance)
						a = map.getValue();
				}
				
				rndChance = getRandomChance();
			}
		}else
		{
			while(a == null)
			{
				for(Map.Entry<String, HACK_Award> map : HACK_AwardLoader.hackAwards.entrySet())
				{
					if(map.getValue().getChance() <= rndChance)
						a = map.getValue();
				}
				
				rndChance = getRandomChance();
			}
		}
		
		return a;
	}
	
	private int getRandomChance() { return new Random().nextInt(100); }
}