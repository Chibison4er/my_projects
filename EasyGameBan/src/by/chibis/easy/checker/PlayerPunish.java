package by.chibis.easy.checker;

import by.chibis.easy.EasyBanType;

public class PlayerPunish 
{
	String whoSetMute;
	long endMuteTime;
	EasyBanType type;
	
	public PlayerPunish(String whoSetMute, long time) 
	{
		this.whoSetMute = whoSetMute;
		endMuteTime = (time * 60 * 1000) + System.currentTimeMillis();
		
		if(time != 0)
			this.type = EasyBanType.TEMPMUTE;
		else
			this.type = EasyBanType.MUTE;
	}

	public String getWhoSetMute() {
		return whoSetMute;
	}

	public void setWhoSetMute(String whoSetMute) {
		this.whoSetMute = whoSetMute;
	}

	public long getEndMuteTime() {
		return endMuteTime;
	}

	public void setEndMuteTime(long endMuteTime) {
		this.endMuteTime = endMuteTime;
	}	
	
	public EasyBanType getType() { return type; }
}
