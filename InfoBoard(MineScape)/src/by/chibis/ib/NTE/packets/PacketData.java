package by.chibis.ib.NTE.packets;

enum PacketData
{
 v1_7("e", "c", "d", "a", "f", "g", "b"),  v1_8("g", "c", "d", "a", "h", "i", "b"),  v1_9("h", "c", "d", "a", "i", "j", "b"),  v1_10("h", "c", "d", "a", "i", "j", "b");
 
 private String members;
 private String prefix;
 private String suffix;
 private String teamName;
 private String paramInt;
 private String packOption;
 private String displayName;
 
 private PacketData(String members, String prefix, String suffix, String teamName, String paramInt, String packOption, String displayName)
 {
   this.members = members;this.prefix = prefix;this.suffix = suffix;this.teamName = teamName;this.paramInt = paramInt;this.packOption = packOption;this.displayName = displayName;
 }
 
 public String getMembers()
 {
   return this.members;
 }
 
 public String getPrefix()
 {
   return this.prefix;
 }
 
 public String getSuffix()
 {
   return this.suffix;
 }
 
 public String getTeamName()
 {
   return this.teamName;
 }
 
 public String getParamInt()
 {
   return this.paramInt;
 }
 
 public String getPackOption()
 {
   return this.packOption;
 }
 
 public String getDisplayName()
 {
   return this.displayName;
 }
}
