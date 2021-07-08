package by.chibis.ib.NTE;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import by.chibis.ib.Main;
import by.chibis.ib.NTE.data.FakeTeam;
import by.chibis.ib.NTE.packets.PacketWrapper;
import mspermissions.MSPermissions;
import net.md_5.bungee.api.ChatColor;

public class NameTagManager
{
	private final HashMap<String, FakeTeam> TEAMS = new HashMap();
	private final HashMap<String, FakeTeam> CACHED_FAKE_TEAMS = new HashMap();

	private String getNameFromInput(int input)
	{
		if (input < 0) {
			return null;
		}
		String letter = String.valueOf((char)(input / 13 + 65));
		int repeat = input % 13 + 1;
		return StringUtils.repeat(letter, repeat);
	}

	private FakeTeam getFakeTeam(String prefix, String suffix)
	{
		for (FakeTeam fakeTeam : this.TEAMS.values()) {
			if (fakeTeam.isSimilar(prefix, suffix)) {
				return fakeTeam;
			}
		}
		return null;
	}

	private void addPlayerToTeam(String player, String prefix, String suffix, int sortPriority)
	{
		FakeTeam previous = getFakeTeam(player);
		if ((previous != null) && (previous.isSimilar(prefix, suffix)))
		{
			return;
		}
		reset(player);

		FakeTeam joining = getFakeTeam(prefix, suffix);
		if (joining != null)
		{
			joining.addMember(player);
		}
		else
		{
			joining = new FakeTeam(prefix, suffix, getNameFromInput(sortPriority));
			joining.addMember(player);
			this.TEAMS.put(joining.getName(), joining);
			addTeamPackets(joining);
		}
		Player adding = Bukkit.getPlayerExact(player);
		if (adding != null)
		{
			addPlayerToTeamPackets(joining, adding.getName());
			cache(adding.getName(), joining);
		}
		else
		{
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
			addPlayerToTeamPackets(joining, offlinePlayer.getName());
			cache(offlinePlayer.getName(), joining);
		}
	}

	public FakeTeam reset(String player)
	{
		return reset(player, decache(player));
	}

	private FakeTeam reset(String player, FakeTeam fakeTeam)
	{
		if ((fakeTeam != null) && (fakeTeam.getMembers().remove(player)))
		{
			Player removing = Bukkit.getPlayerExact(player);
			boolean delete;
			if (removing != null)
			{
				delete = removePlayerFromTeamPackets(fakeTeam, new String[] { removing.getName() });
			}
			else
			{
				OfflinePlayer toRemoveOffline = Bukkit.getOfflinePlayer(player);
				delete = removePlayerFromTeamPackets(fakeTeam, new String[] { toRemoveOffline.getName() });
			}
			if (delete)
			{
				removeTeamPackets(fakeTeam);
				this.TEAMS.remove(fakeTeam.getName());
			}
		}
		return fakeTeam;
	}

	private FakeTeam decache(String player)
	{
		return (FakeTeam)this.CACHED_FAKE_TEAMS.remove(player);
	}

	public FakeTeam getFakeTeam(String player)
	{
		return (FakeTeam)this.CACHED_FAKE_TEAMS.get(player);
	}

	private void cache(String player, FakeTeam fakeTeam)
	{
		this.CACHED_FAKE_TEAMS.put(player, fakeTeam);
	}

	public void setNametag(String player, String prefix, String suffix)
	{
		setNametag(player, prefix, suffix, -1);
	}

	void setNametag(String player, String prefix, String suffix, int sortPriority)
	{
		addPlayerToTeam(player, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
	}

	public void sendTeams(Player player)
	{
		for (FakeTeam fakeTeam : this.TEAMS.values()) {
			new PacketWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers()).send(player);
		}
	}

	public void reset()
	{
		for (FakeTeam fakeTeam : this.TEAMS.values())
		{
			removePlayerFromTeamPackets(fakeTeam, fakeTeam.getMembers());
			removeTeamPackets(fakeTeam);
		}
		this.CACHED_FAKE_TEAMS.clear();
		this.TEAMS.clear();
	}
	
	public void updatePlayersPrefixes()
	{
		if(!Main.inst.isPexEnable)
			return;
		
		for(Player p : Main.inst.getServer().getOnlinePlayers())
		{
			reset(p.getName());
			setNametag(p.getName(), ChatColor.translateAlternateColorCodes('&', MSPermissions.Companion.getPlayerPrefix(p.getName(), false)), "");
		}
	}
	
	private void removeTeamPackets(FakeTeam fakeTeam)
	{
		new PacketWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 1, new ArrayList()).send();
	}

	private boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, String... players)
	{
		return removePlayerFromTeamPackets(fakeTeam, Arrays.asList(players));
	}

	private boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, List<String> players)
	{
		new PacketWrapper(fakeTeam.getName(), 4, players).send();
		fakeTeam.getMembers().removeAll(players);
		return fakeTeam.getMembers().isEmpty();
	}

	private void addTeamPackets(FakeTeam fakeTeam)
	{
		new PacketWrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers()).send();
	}

	private void addPlayerToTeamPackets(FakeTeam fakeTeam, String player)
	{
		new PacketWrapper(fakeTeam.getName(), 3, Collections.singletonList(player)).send();
	}
}
