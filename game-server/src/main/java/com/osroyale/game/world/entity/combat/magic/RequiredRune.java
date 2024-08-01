package com.osroyale.game.world.entity.combat.magic;

public class RequiredRune {
	
	private final MagicRune rune;
	private final int amount;
	
	public RequiredRune(MagicRune rune, int amount) {
		this.rune = rune;
		this.amount = amount;
	}
	
	public MagicRune getRune() {
		return rune;
	}
	
	public int getAmount() {
		return amount;
	}
	
	int getMainId() {
		return rune.getMainId();
	}
	
	int[] getCombos() {
		return rune.getCombos();
	}
	
	int[] getStaffs() {
		return rune.getStaffs();
	}
}
