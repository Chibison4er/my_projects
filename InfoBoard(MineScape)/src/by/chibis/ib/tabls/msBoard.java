package by.chibis.ib.tabls;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.entity.Player;
import by.chibis.ib.Main;
import by.chibis.ib.mainplaceholders.msPlaceHolders;
import by.chibis.ib.sb.MS_SB;

public class msBoard 
{
	String title;
	
	ArrayList<String> text = new ArrayList<>();
	
	int update = 1;
	MS_SB sb;
	
	//Top
	boolean isUseTop;
	int itemSize = 10;
	String topTable = "", topFormatter = "&a&l%number% &c%playername% &6- &b&l%score%";
	
	public msBoard(String title, ArrayList<String> text, int time, boolean top, int items, String table, String topFormatter)
	{
		this.title = Main.inst.getColored(title);
		
		sb = new MS_SB();
		sb.setTitle(title);
		
		for(String s : text)
			this.text.add(Main.inst.getColored(s));
		
		this.topFormatter = topFormatter;
		topTable = table;
		itemSize = items;
		isUseTop = top;
		
		update = time;
	}
	
	public String getTitle() { return title; }
	
	public void showInfoBord(Player p)
	{
		Main.curBoard = this;
		
		sb = new MS_SB();
		sb.setTitle(title);
		
		if(!isUseTop)
			for(int i = 0; i < text.size(); i ++)
				sb.addLine(msPlaceHolders.replacePH(replaceS(p.getName(), text.get(i)), p), text.size() -i);
		else
		{
			int sort = 0;
			
			for(Map.Entry<String, Integer> map : Main.inst.getTableByName(topTable).getPlayerMap().entrySet())
			{
				if((itemSize - sort) == 0)
					break;
				else
					sb.addLine(formatTop(sort+1, map.getKey(), map.getValue()), itemSize - sort);
				
				sort ++;
			}
		}
		
		sb.update(p);
		sb.send(p);
	}
	
	public MS_SB getMSSB() { return sb; }
	
	private String formatTop(Integer number, String name, Integer value)
	{
		return Main.inst.getColored(topFormatter.replaceAll("%number%", number+"").replaceAll("%playername%", name).replaceAll("%score%", value + ""));
	}
	
	private String replaceS(String name, String r)
	{
		String s = r;
		
		for(Map.Entry<String, String> f : Main.formattersTable.entrySet())
		{
			if(r.indexOf(f.getKey()) >= 0)
				s = Main.sqlTable.get(f.getValue()).replacePlaceHolders(name, r);
		}
		
		return s;
	}
	
	public int getSbUpdateTime() { return update; }
	
}
