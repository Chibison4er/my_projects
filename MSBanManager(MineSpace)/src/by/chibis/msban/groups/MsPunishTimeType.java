package by.chibis.msban.groups;

public enum MsPunishTimeType 
{
	s (0, "секунды"),
	m (1, "минуты"),
	h (2, "часы");
	
	private int i;
	private String s1;
	
	private MsPunishTimeType(int i, String s1) 
	{
		this.i = i;
		this.s1 = s1;
	}
	
	public int getIndex() { return i; }
	public String getName() { return s1; }
}
