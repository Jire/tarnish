package com.osroyale.game.world.entity.mob;

import com.osroyale.game.world.entity.mob.npc.definition.NpcDefinition;

import java.util.EnumSet;

public class MobAnimation {

	public static final int PLAYER_STAND = 808;
	public static final int PLAYER_WALK = 819;
	public static final int PLAYER_TURN_180 = 820;
	public static final int PLAYER_TURN_90_CW = 821;
	public static final int PLAYER_TURN_90_CCW = 822;
	public static final int PLAYER_TURN = 823;
	public static final int PLAYER_RUN = 824;

	private int stand;
	private int turn;
	private int walk;
	private int turn180;
	private int turn90CW;
	private int turn90CCW;
	private int run;
	private final EnumSet<UpdateFlag> updateFlags;
	
	MobAnimation(EnumSet<UpdateFlag> updateFlags) {
		this.updateFlags = updateFlags;
	}
	
	public void set(int stand, int turn, int walk, int turn180, int turn90cw, int turn90ccw, int run) {
		this.stand = stand;
		this.turn = turn;
		this.walk = walk;
		this.turn180 = turn180;
		this.turn90CW = turn90cw;
		this.turn90CCW = turn90ccw;
		this.run = run;
		updateFlags.add(UpdateFlag.APPEARANCE);
	}

	public void reset() {
		this.stand = PLAYER_STAND;
		this.turn = PLAYER_TURN;
		this.walk = PLAYER_WALK;
		this.turn180 = PLAYER_TURN_180;
		this.turn90CW = PLAYER_TURN_90_CW;
		this.turn90CCW = PLAYER_TURN_90_CCW;
		this.run = PLAYER_RUN;
		updateFlags.add(UpdateFlag.APPEARANCE);
	}
	
	public void setNpcAnimations(NpcDefinition definition) {
		this.stand = definition.getStand();
		this.walk = definition.getWalk();
		this.turn180 = definition.getTurn180();
		this.turn90CW = definition.getTurn90CW();
		this.turn90CCW = definition.getTurn90CCW();
		updateFlags.add(UpdateFlag.APPEARANCE);
	}

	public int getStand() {
		return stand;
	}

	public int getTurn() {
		return turn;
	}

	public int getWalk() {
		return walk;
	}

	public int getTurn180() {
		return turn180 <= 0 || turn180 == PLAYER_TURN_180 ? getWalk() : turn180;
	}

	public int getTurn90CW() {
		return turn90CW <= 0 || turn90CW == PLAYER_TURN_90_CW ? getWalk() : turn90CW;
	}

	public int getTurn90CCW() {
		return turn90CCW <= 0 || turn90CCW == PLAYER_TURN_90_CCW ? getWalk() : turn90CCW;
	}

	public int getRun() {
		return run;
	}

	public void setStand(int stand) {
		if (this.stand != stand)
			updateFlags.add(UpdateFlag.APPEARANCE);
		this.stand = stand;
	}

	public void setTurn(int turn) {
		if (this.turn != turn)
			updateFlags.add(UpdateFlag.APPEARANCE);
		this.turn = turn;
	}

	public void setWalk(int walk) {
		if (this.walk != walk)
			updateFlags.add(UpdateFlag.APPEARANCE);
		this.walk = walk;
	}

	public void setTurn180(int turn180) {
		if (this.turn180 != turn180)
			updateFlags.add(UpdateFlag.APPEARANCE);
		this.turn180 = turn180;
	}

	public void setTurn90CW(int turn90cw) {
		if (turn90CW != turn90cw)
			updateFlags.add(UpdateFlag.APPEARANCE);
		turn90CW = turn90cw;
	}

	public void setTurn90CCW(int turn90ccw) {
		if (turn90CCW != turn90ccw)
			updateFlags.add(UpdateFlag.APPEARANCE);
		turn90CCW = turn90ccw;
	}

	public void setRun(int run) {
		if (this.run != run)
			updateFlags.add(UpdateFlag.APPEARANCE);
		this.run = run;
	}

	public MobAnimation copy() {
		MobAnimation other = new MobAnimation(updateFlags);
		other.stand = stand;
		other.turn = turn;
		other.walk = walk;
		other.turn180 = turn180;
		other.turn90CW = turn90CW;
		other.turn90CCW = turn90CCW;
		other.run = run;
		return other;
	}
}