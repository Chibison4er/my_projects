package by.chibis.msban.groups;

public enum MsPunishTimeType 
{
	s (0, "�������"),
	m (1, "������"),
	h (2, "����");
	
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
