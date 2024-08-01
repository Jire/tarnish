package com.osroyale;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class Entity extends Renderable {

	public final void setPos(int x, int y, boolean discardWalkingQueue) {
		if (primarySeqID != -1 && Animation.animations[primarySeqID].walkFlag == 1)
			primarySeqID = -1;

		if (!discardWalkingQueue) {
			int dx = x - smallX[0];
			int dy = y - smallY[0];

			if (dx >= -8 && dx <= 8 && dy >= -8 && dy <= 8) {
				if (waypointIndex < 9) {
					waypointIndex++;
				}

				for (int index = waypointIndex; index > 0; index--) {
					smallX[index] = smallX[index - 1];
					smallY[index] = smallY[index - 1];
					doubleSpeed[index] = doubleSpeed[index - 1];
				}

				smallX[0] = x;
				smallY[0] = y;
				doubleSpeed[0] = false;
				return;
			}
		}

		waypointIndex = 0;
		remainingSteps = 0;
		stepTracker = 0;
		smallX[0] = x;
		smallY[0] = y;

		this.x = (smallX[0] << 7) + (size << 6);
		this.y = (smallY[0] << 7) + (size << 6);
	}

	public final void resetPath() {
		waypointIndex = 0;
		remainingSteps = 0;
	}

	public double[] hitmarkMove = new double[4];
	public int[] hitmarkTrans = new int[4];
	public int[] hitIcon = new int[4];

	public final void damage(int markType, int damage, int tick, int icon) {
		int mark = markType;

		if (Settings.HITSPLAT == 0 || Settings.HITSPLAT == 1) {
			if (mark == 4) {
				mark = 1;
			}
		}

		for (int index = 0; index < 4; index++) {
			if (hitsLoopCycle[index] <= tick) {
				hitIcon[index] = icon;
				hitmarkMove[index] = 5;
				hitmarkTrans[index] = 230;
				hitArray[index] = Settings.DAMAGE_MULTIPLIER ? damage * 10 : damage;
				if (damage > 0 && Settings.DAMAGE_MULTIPLIER) {
					hitArray[index] += new java.util.Random().nextInt(9);
				}
				hitMarkTypes[index] = mark;
				hitsLoopCycle[index] = tick + 70;
				return;
			}
		}
	}

	public final void moveInDir(boolean moveFast, int direction) {
		int x = smallX[0];
		int y = smallY[0];

		if (direction == 0) {
			x--;
			y++;
		}

		if (direction == 1)
			y++;

		if (direction == 2) {
			x++;
			y++;
		}

		if (direction == 3)
			x--;

		if (direction == 4)
			x++;

		if (direction == 5) {
			x--;
			y--;
		}

		if (direction == 6)
			y--;

		if (direction == 7) {
			x++;
			y--;
		}

		if (primarySeqID != -1 && Animation.animations[primarySeqID].walkFlag == 1)
			primarySeqID = -1;

		if (waypointIndex < 9)
			waypointIndex++;

		for (int index = waypointIndex; index > 0; index--) {
			smallX[index] = smallX[index - 1];
			smallY[index] = smallY[index - 1];
			doubleSpeed[index] = doubleSpeed[index - 1];
		}

        smallX[0] = x;
        smallY[0] = y;
        doubleSpeed[0] = moveFast;
    }

	public int entScreenX;
	public int entScreenY;
	public final int index = -1;

	public boolean isVisible() {
		return false;
	}

	Entity() {
		smallX = new int[10];
		smallY = new int[10];
		interactingEntity = -1;
		rotation = 32;
		runAnimation = -1;
		height = 200;
		seqStandID = -1;
		turnAnimation = -1;
		hitArray = new int[4];
		hitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		secondarySeqID = -1;
		graphicId = -1;
		primarySeqID = -1;
		cycleStatus = -1000;
		textCycle = 100;
		size = 1;
		dynamic = false;
		doubleSpeed = new boolean[10];
		walkingAnimation = -1;
		halfTurnAnimation = -1;
		quarterClockwiseTurnAnimation = -1;
		quarterAnticlockwiseTurnAnimation = -1;
	}

	public final int[] smallX;
	public final int[] smallY;
	public int interactingEntity;
	int stepTracker;
	int rotation;
	int runAnimation;
	public String textSpoken;
	public int height;
	public int turnDirection;
	int seqStandID;
	int turnAnimation;
	int textColor;
	final int[] hitArray;
	final int[] hitMarkTypes;
	final int[] hitsLoopCycle;
	int secondarySeqID;
	int secondarySeqFrame;
	int secondarySeqCycle;
	int graphicId;
	int currentAnimationId;
	int currentAnimationTimeRemaining;
	int graphicDelay;
	int graphicHeight;
	int waypointIndex;
	public int primarySeqID;
	int primarySeqFrame;
	int primarySeqCycle;
	int primarySeqDelay;
	int animationLoops;
	int textEffect;
	public int cycleStatus;
	public int currentHealth;
	public int maximumHealth;
	int textCycle;
	int time;
	int faceX;
	int faceY;
	int size;
	boolean dynamic;
	int remainingSteps;
	int initialX;
	int destinationX;
	int initialY;
	int destinationY;
	int startForceMovement;
	int endForceMovement;
	int direction;
	public int x;
	public int y;
	int currentRotation;
	final boolean[] doubleSpeed;
	int walkingAnimation;
	int halfTurnAnimation;
	int quarterClockwiseTurnAnimation;
	int quarterAnticlockwiseTurnAnimation;

	public int nextAnimationFrame;
	public int nextGraphicFrame;
	public int nextIdleFrame;
}
