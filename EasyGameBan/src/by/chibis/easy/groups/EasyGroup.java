package by.chibis.easy.groups;

public class EasyGroup 
{
	boolean whetherBan, whetherTempBan, whetherUnBan, whetherMute, whetherTempMute, whetherUnMute, whetherPunishJunior;
	int tempBanInMinutes, tempMuteInMinutes;
	int banLimit, tempBanLimit, unBanLimit, muteLimit, tempMuteLimit, unMuteLimit;
	String group;
	
	public EasyGroup(boolean ban, boolean tempBan, boolean unBan, boolean mute, boolean tempMute, boolean unMute, boolean punishJunior,
			int banTime, int muteTime,
			int banLimit, int tempBanLimit, int unBanLimit, int muteLimit, int tempMuteLimit, int unMuteLimit) 
	{
		whetherPunishJunior = punishJunior;
		whetherBan = ban;
		whetherTempBan = tempBan;
		whetherUnBan = unBan;
		whetherMute = mute;
		whetherTempMute = tempMute;
		whetherUnMute = unMute;
		//time
		tempBanInMinutes = banTime;
		tempMuteInMinutes = muteTime;
		//limits
		this.banLimit = banLimit;
		this.tempBanLimit = tempBanLimit;
		this.unBanLimit = unBanLimit;
		this.muteLimit = muteLimit;
		this.tempMuteLimit = tempMuteLimit;
		this.unMuteLimit = unMuteLimit;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isWhetherPunishJunior() {
		return whetherPunishJunior;
	}

	public void setWhetherPunishJunior(boolean whetherPunishJunior) {
		this.whetherPunishJunior = whetherPunishJunior;
	}

	public int getBanLimit() {
		return banLimit;
	}

	public void setBanLimit(int banLimit) {
		this.banLimit = banLimit;
	}

	public int getTempBanLimit() {
		return tempBanLimit;
	}

	public void setTempBanLimit(int tempBanLimit) {
		this.tempBanLimit = tempBanLimit;
	}

	public int getUnBanLimit() {
		return unBanLimit;
	}

	public void setUnBanLimit(int unBanLimit) {
		this.unBanLimit = unBanLimit;
	}

	public int getMuteLimit() {
		return muteLimit;
	}

	public void setMuteLimit(int muteLimit) {
		this.muteLimit = muteLimit;
	}

	public int getTempMuteLimit() {
		return tempMuteLimit;
	}

	public void setTempMuteLimit(int tempMuteLimit) {
		this.tempMuteLimit = tempMuteLimit;
	}

	public int getUnMuteLimit() {
		return unMuteLimit;
	}

	public void setUnMuteLimit(int unMuteLimit) {
		this.unMuteLimit = unMuteLimit;
	}

	public boolean isWhetherBan() {
		return whetherBan;
	}

	public void setWhetherBan(boolean whetherBan) {
		this.whetherBan = whetherBan;
	}

	public boolean isWhetherTempBan() {
		return whetherTempBan;
	}

	public void setWhetherTempBan(boolean whetherTempBan) {
		this.whetherTempBan = whetherTempBan;
	}

	public boolean isWhetherUnBan() {
		return whetherUnBan;
	}

	public void setWhetherUnBan(boolean whetherUnBan) {
		this.whetherUnBan = whetherUnBan;
	}

	public boolean isWhetherMute() {
		return whetherMute;
	}

	public void setWhetherMute(boolean whetherMute) {
		this.whetherMute = whetherMute;
	}

	public boolean isWhetherTempMute() {
		return whetherTempMute;
	}

	public void setWhetherTempMute(boolean whetherTempMute) {
		this.whetherTempMute = whetherTempMute;
	}

	public boolean isWhetherUnMute() {
		return whetherUnMute;
	}

	public void setWhetherUnMute(boolean whetherUnMute) {
		this.whetherUnMute = whetherUnMute;
	}

	public int getTempBanInMinutes() {
		return tempBanInMinutes;
	}

	public void setTempBanInMinutes(int tempBanInMinutes) {
		this.tempBanInMinutes = tempBanInMinutes;
	}

	public int getTempMuteInMinutes() {
		return tempMuteInMinutes;
	}

	public void setTempMuteInMinutes(int tempMuteInMinutes) {
		this.tempMuteInMinutes = tempMuteInMinutes;
	}
}
