package by.chibis.easy.lang;

import org.bukkit.ChatColor;

import by.chibis.easy.EasyBan;

public enum EasyLang 
{
	SYSTEM_PLAYER_OFFLINE("playerOffline"),
	SYSTEM_LESS_TIME("timeNotZero"),
	SYSTEM_AUTO_UNMUTE("autoUnMute"),
	SYSTEM_GLOBAL_MUTE("globalMute"),
	//
	BAN_USAGE ("banUsage"),
	BAN_YOURSELF ("banYourSelf"),
	BAN_PLAYER("banPlayer"),
	BAN_BROADCAST("banBroadcast"),
	BAN_PLAYER_KICK_MSG("banPlayerKickMsg"),
	BAN_DONATE_HIGTH("banDonateHight"),
	BAN_LIMIT_OUT("banLimit"),
	BAN_NOPERM("banPerm"),
	//
	TEMPBAN_USAGE ("tempBanUsage"),
	TEMPBAN_YOURSELF ("tempBanYourSelf"),
	TEMPBAN_MAX_TIME("tempBanMaxTime"),
	TEMPBAN_PLAYER("tempBanPlayerKickMsg"),
	TEMPBAN_BROADCAST("tempBanBroadcast"),
	TEMPBAN_DONATE_HIGTH("tempBanDonateHight"),
	TEMPBAN_LIMIT_OUT("tempBanLimit"),
	TEMPBAN_NOPERM("tempBanPerm"),
	//
	UNBAN_USAGE ("unBanUsage"),
	UNBAN_PLAYER("unBanPlayer"),
	UNBAN_NOBAN("unBanNoBan"),
	UNBAN_YOURSELF ("unBanYourSelf"),
	UNBAN_LIMIT_OUT("unBanLimit"),
	UNBAN_NOPERM("unBanPerm"),
	//
	MUTE_USAGE ("muteUsage"),
	MUTE_YOURSELF ("muteYourSelf"),
	MUTE_PLAYER("mutePlayer"),
	MUTE_ALREADY("muteAlready"),
	MUTE_BROADCAST("mutePlayerBroadCast"),
	MUTE_DONATE_HIGTH("muteDonateHight"),
	MUTE_LIMIT_OUT("muteLimit"),
	MUTE_NOPERM("mutePerm"),
	//
	TEMPMUTE_USAGE ("tempMuteUsage"),
	TEMPMUTE_YOURSELF ("tempMuteYourSelf"),
	TEMPMUTE_PLAYER("tempMutePlayer"),
	TEMPMUTE_MAX_TIME("tempMuteMaxTime"),
	TEMPMUTE_BROADCAST("tempMuteBroadcast"),
	TEMPMUTE_DONATE_HIGTH("tempMuteDonateHight"),
	TEMPMUTE_LIMIT_OUT("tempMuteLimit"),
	TEMPMUTE_NOPERM("tempMutePerm"),
	//
	UNMUTE_USAGE ("unMuteUsage"),
	UNMUTE_PLAYER("unMutePlayer"),
	UNMUTE_NOMUTE("unMuteNoMute"),
	UNMUTE_LIMIT_OUT("unMuteLimit"),
	UNMUTE_NOPERM("unMutePerm");
	
	String path;
	
	private EasyLang(String s) {
		path = s;
	}
	
	public String getMsg(boolean prefix) { return prefix ? getColor(EasyBan.plugin.config.getString("banPrefix") + EasyBan.plugin.config.getString(path)) : getColor(EasyBan.plugin.config.getString(path)); }
	private String getColor(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
}
