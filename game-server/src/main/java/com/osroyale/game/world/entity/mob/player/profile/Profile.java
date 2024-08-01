package com.osroyale.game.world.entity.mob.player.profile;

import com.osroyale.game.world.entity.mob.player.PlayerRight;

import java.util.Set;

public final class Profile {
	
	private final String name;
	
	private final String lastHost;
	
	private final Set<String> host;
	
	private final PlayerRight rank;
	
	public Profile(String name, String lastHost, Set<String> host, PlayerRight rank) {
		this.name = name;
		this.lastHost = lastHost;
		this.host = host;
		this.rank = rank;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLastHost() {
		return lastHost;
	}
	
	public Set<String> getHost() {
		return host;
	}
	
	public PlayerRight getRank() {
		return rank;
	}

}
