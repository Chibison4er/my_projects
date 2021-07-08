package by.chibis.ib.NTE.data;

import java.util.ArrayList;

public class FakeTeam
{
  public void setName(String name)
  {
    this.name = name;
  }
  
  public int hashCode()
  {
    int PRIME = 59;int result = 1;Object $members = getMembers();result = result * 59 + ($members == null ? 0 : $members.hashCode());Object $name = getName();result = result * 59 + ($name == null ? 0 : $name.hashCode());Object $prefix = getPrefix();result = result * 59 + ($prefix == null ? 0 : $prefix.hashCode());Object $suffix = getSuffix();result = result * 59 + ($suffix == null ? 0 : $suffix.hashCode());return result;
  }
  
  protected boolean canEqual(Object other)
  {
    return other instanceof FakeTeam;
  }
  
  public boolean equals(Object o)
  {
    if (o == this) {
      return true;
    }
    if (!(o instanceof FakeTeam)) {
      return false;
    }
    FakeTeam other = (FakeTeam)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Object this$members = getMembers();Object other$members = other.getMembers();
    if (this$members == null ? other$members != null : !this$members.equals(other$members)) {
      return false;
    }
    Object this$name = getName();Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
      return false;
    }
    Object this$prefix = getPrefix();Object other$prefix = other.getPrefix();
    if (this$prefix == null ? other$prefix != null : !this$prefix.equals(other$prefix)) {
      return false;
    }
    Object this$suffix = getSuffix();Object other$suffix = other.getSuffix();return this$suffix == null ? other$suffix == null : this$suffix.equals(other$suffix);
  }
  
  public void setSuffix(String suffix)
  {
    this.suffix = suffix;
  }
  
  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }
  
  public String toString()
  {
    return "FakeTeam(members=" + getMembers() + ", name=" + getName() + ", prefix=" + getPrefix() + ", suffix=" + getSuffix() + ")";
  }
  
  private static int ID = 0;
  
  public ArrayList<String> getMembers()
  {
    return this.members;
  }
  
  private final ArrayList<String> members = new ArrayList();
  private String name;
  
  public String getName()
  {
    return this.name;
  }
  
  public String getPrefix()
  {
    return this.prefix;
  }
  
  private String prefix = "";
  
  public String getSuffix()
  {
    return this.suffix;
  }
  
  private String suffix = "";
  
  public FakeTeam(String prefix, String suffix, String name)
  {
    this.name = (name + ++ID);
    this.prefix = prefix;
    this.suffix = suffix;
  }
  
  public void addMember(String player)
  {
    if (!this.members.contains(player)) {
      this.members.add(player);
    }
  }
  
  public boolean isSimilar(String prefix, String suffix)
  {
    return (this.prefix.equals(prefix)) && (this.suffix.equals(suffix));
  }
}
