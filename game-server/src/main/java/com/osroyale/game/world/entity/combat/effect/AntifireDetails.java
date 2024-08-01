package com.osroyale.game.world.entity.combat.effect;

import com.osroyale.util.MutableNumber;

public final class AntifireDetails {
	
	private final MutableNumber antifireDelay = new MutableNumber(600);
	
	private final AntifireType type;
	
	public AntifireDetails(AntifireType type) {
		this.type = type;
	}
	
	public MutableNumber getAntifireDelay() {
		return antifireDelay;
	}
	
	public AntifireType getType() {
		return type;
	}
	
	public enum AntifireType {
		REGULAR(45),
		SUPER(90);
		
		final int reduction;
		
		AntifireType(int reduction) {
			this.reduction = reduction;
		}
		
		public int getReduction() {
			return reduction;
		}
	}

}