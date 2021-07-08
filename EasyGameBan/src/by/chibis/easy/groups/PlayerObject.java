package by.chibis.easy.groups;

public class PlayerObject 
{
	EasyGroup g;
	int banLimit, tempBanLimit, unBanLimit, muteLimit, tempMuteLimit, unMuteLimit;
	
	public PlayerObject(EasyGroup g)
	{
		this.g = g;
		
		banLimit = g.getBanLimit();
		tempBanLimit = g.getTempBanLimit();
		unBanLimit = g.getUnBanLimit();
		muteLimit = g.getMuteLimit();
		tempMuteLimit = g.getTempMuteLimit();
		unMuteLimit = g.getUnMuteLimit();
	}

	public EasyGroup getG() {
		return g;
	}

	public void setG(EasyGroup g) {
		this.g = g;
	}

	public int getBanLimit() {
		return banLimit;
	}

	public void setBanLimit(int banLimit) {
		this.banLimit += banLimit;
	}

	public int getTempBanLimit() {
		return tempBanLimit;
	}

	public void setTempBanLimit(int tempBanLimit) {
		this.tempBanLimit += tempBanLimit;
	}

	public int getUnBanLimit() {
		return unBanLimit;
	}

	public void setUnBanLimit(int unBanLimit) {
		this.unBanLimit += unBanLimit;
	}

	public int getMuteLimit() {
		return muteLimit;
	}

	public void setMuteLimit(int muteLimit) {
		this.muteLimit += muteLimit;
	}

	public int getTempMuteLimit() {
		return tempMuteLimit;
	}

	public void setTempMuteLimit(int tempMuteLimit) {
		this.tempMuteLimit += tempMuteLimit;
	}

	public int getUnMuteLimit() {
		return unMuteLimit;
	}

	public void setUnMuteLimit(int unMuteLimit) {
		this.unMuteLimit += unMuteLimit;
	}
}
