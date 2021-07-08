package by.chibis.megacase.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import by.chibis.megacase.parametrs.MegaParam;

public class MegaCase_Listener implements Listener 
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventory(InventoryClickEvent e)
	{
		if(e.getInventory().getTitle().equalsIgnoreCase(MegaParam.guiName))
			e.setCancelled(true);
	}
}