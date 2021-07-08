package by.chibis.megacase.parametrs;

import by.chibis.megacase.utils.MegaUtils;

public class MegaCmd 
{
	String perm, text;
	
	public MegaCmd(String perm, String text)
	{
		this.perm = perm;
		this.text = MegaUtils.getColored(text);
	}
	
	public String getPermission() { return perm; }
	public String getText() { return text; }
}
