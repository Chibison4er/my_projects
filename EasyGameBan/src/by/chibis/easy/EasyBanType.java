package by.chibis.easy;

import org.bukkit.ChatColor;

public enum EasyBanType 
{
	BAN("&7[&4&cBAN&7]&r"),
	TEMPBAN("&7[&4&cTEMPBAN&7]&r"),
	UNBAN("&7[&4&lUNBAN&7]&r"),
	UNMUTE("&7[&4&UNMUTEc&7]"),
	MUTE("&7[&4&MUTEc&7]"),
	TEMPMUTE("&7[&4&lTEMPMUTE&7]"),
	KICK ("&7[KICK&7]"),
	NONE("&7[&4&cNONE&7]");
	
	String s;
	
	private EasyBanType(String s) {
		this.s = s;
	}
	
	public String getTag() { return ChatColor.translateAlternateColorCodes('&', s); }
}