package by.chibis.megacase.runner;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import by.chibis.megacase.MegaCase;
import by.chibis.megacase.award.MegaAward;
import by.chibis.megacase.groups.MegaCaseGroups;
import by.chibis.megacase.manager.MegaCaseManager;
import by.chibis.megacase.parametrs.MegaParam;
import by.chibis.megacase.utils.ItemUtils;
import by.chibis.megacase.utils.MegaUtils;

public class MegaCaseRunner
{
	Player user;
	String name;
	private int time = 1;
	int b = 0;
	Integer count = 0;
	MegaCase instance;
	ArrayList<MegaAward> awards;
	Boolean isUserDC = false;
	Boolean isEnd = false;
	
	Inventory gui;
	String guiName = MegaParam.guiName;
	Integer guiSize = 9*4;
	
	public MegaCaseRunner(Player p, MegaCase plugin) 
	{
		MegaCaseManager.setPlayerRun(name);
		user = p;
		name = p.getName().toLowerCase();
		instance = plugin;
		awards = new ArrayList<>();
		
		instance.getServer().broadcastMessage(MegaParam.prefix + String.format(MegaParam.getMsg("useropen"), user.getDisplayName()));
		
		init();
	}
	
	void init()
	{
	//	if(isEnd) return;
		
		if(time > MegaParam.rollTimeEnd)
		{
	//		isEnd = true;
			
			String donat = !isUserDC ? MegaUtils.showTags(gui.getItem(4)) : "vip";
			String formatGroup = !isUserDC ? MegaUtils.getDisplayName(gui.getItem(4)) : MegaCaseManager.getNameItemByGroup(donat);
			
			if(user.isOnline())
			{
				MegaUtils.launchFirework(user);
				user.sendMessage(MegaParam.prefix + String.format(MegaParam.getMsg("uwin"), formatGroup));
			}
			
			MegaCaseManager.removePlayer(name);
			MegaCaseGroups.editGroup(donat, name);
			
			MegaParam.broadcastWinMsg(user.getDisplayName(), formatGroup);
			
			donat = null;
			user = null;
			name = null;
			count = null;
			instance = null;
			awards = null;
			isUserDC = null;
			gui = null;
			guiName = null;
			guiSize = null;
			
			return;
		}else
		{
			instance.getServer().getScheduler().scheduleSyncDelayedTask(instance, new Runnable()
			{
				@Override
				public void run()
				{	
					init();
					
					if(user != null)
					{
						if(user.isOnline())
							open();
					}
					else
					{
						isUserDC = true;
						time++; //= time + MegaParam.rollTimeEnd;
						return;
					}
					
					count ++;
					time+=(long) MegaParam.rollTimeAdd;
				}
				
			}, (long) (time/2));//MegaParam.rollTimeDrop  MegaParam.d
		}
	}
	
	private void open()
	{
		gui = instance.getServer().createInventory(user, guiSize, guiName); 
		add();
		user.playSound(user.getLocation(), MegaParam.sound, MegaParam.isRndValue ? getRndFloat() : MegaParam.value, MegaParam.isRndPitch ? getRndFloat() : MegaParam.pitch);
		
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
		
		user.openInventory(gui);
	}
	
	void add()
	{
		if(awards.size() == 0)
		{
			for (int i = 0; i < 9; i++)
			{
				getRandomAward();
				gui.setItem(i, awards.get(i).getItem());
			}
			
			return;
		}else
		{
			getRandomAward();
			
			for(int i = 0; i < 9; i++)
			{
				gui.setItem(i, awards.get(i + count).getItem());
			}
		}
	}
	
	MegaAward getRandomAward() 
	{
		MegaAward ma = null; 
		
		b ++;

		while(ma == null)
		{
			for (MegaAward a : MegaCaseManager.awards)
				if (roll(a.getChance()) && ma == null)
					ma = a;
		}

		awards.add(ma);
		
		return ma;

	}
	
	boolean roll( int chance ) { return new Random().nextInt(100) < chance; }
	float getRndFloat() { return new Random().nextFloat(); }
}
