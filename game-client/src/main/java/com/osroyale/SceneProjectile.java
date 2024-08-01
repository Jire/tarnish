package com.osroyale;

import net.runelite.api.Actor;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSProjectile;

public final class SceneProjectile extends Renderable implements RSProjectile {

	public final int startCycle;
	public final int stopCycle;
	private double xIncrement;
	private double yIncrement;
	private double diagonalIncrement;
	private double heightIncrement;
	private double aDouble1578;
	private boolean started;
	private final int projectileX;
	private final int projectileY;
	private final int startHeight;
	public final int endHeight;
	public double xPos;
	public double yPos;
	public double cnterHeight;
	private final int initialSlope;
	private final int initialDistance;
	public final int target;
	private final Graphic projectileGFX;
	private int gfxStage;
	private int gfxTickOfCurrentStage;
	public int turnValue;
	private int tiltAngle;
	public final int anInt1597;

	public void calculateIncrements(int currentCycle, int targetY, int targetCenterHeight, int targetX) {
		if(!started) {
			double xToGo = targetX - projectileX;
			double yToGo = targetY - projectileY;
			double distanceToGo = Math.sqrt(xToGo * xToGo + yToGo * yToGo);
			xPos = (double) projectileX + (xToGo * (double) initialDistance) / distanceToGo;
			yPos = (double) projectileY + (yToGo * (double) initialDistance) / distanceToGo;
			cnterHeight = startHeight;
		}
		double cyclesLeft = (stopCycle + 1) - currentCycle;
		xIncrement = ((double)targetX - xPos) / cyclesLeft;
		yIncrement = ((double)targetY - yPos) / cyclesLeft;
		diagonalIncrement = Math.sqrt(xIncrement * xIncrement + yIncrement * yIncrement);
		if(!started) {
			heightIncrement = -diagonalIncrement * Math.tan((double) initialSlope * 0.02454369D);
		}
		aDouble1578 = (2D * ((double)targetCenterHeight - cnterHeight - heightIncrement * cyclesLeft)) / (cyclesLeft * cyclesLeft);
		ProjectileMoved projectileMoved = new ProjectileMoved();
		projectileMoved.setProjectile(this);
		projectileMoved.setPosition(new LocalPoint((int) xPos, (int) yPos));
		Client.instance.getCallbacks().post(projectileMoved);
	}

	public Model getRotatedModel() {
		Model model = projectileGFX.getModel();
		if (model == null) {
			return null;
		}
		int j = -1;
		if (projectileGFX.animationSequence != null) {
			j = projectileGFX.animationSequence.primaryFrameIds[gfxStage];
		}
		Model model_1 = new Model(true, Frame.hasAlphaTransform(j), false, model);
		if (j != -1) {
			model_1.generateBones();
			model_1.interpolate(j);
			model_1.groupedTriangleLabels = null;
			model_1.groupedVertexLabels = null;
		}
		if (projectileGFX.resizeXY != 128 || projectileGFX.resizeZ != 128) {
			model_1.scale(projectileGFX.resizeXY, projectileGFX.resizeXY, projectileGFX.resizeZ);
		}
		model_1.rotateZ(tiltAngle);
		model_1.light(64 + projectileGFX.modelBrightness, 850 + projectileGFX.modelShadow, -30, -50, -30, false);
		return model_1;
	}

	public SceneProjectile(int i, int j, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2) {
		started = false;
		projectileGFX = Graphic.graphics[l2];
		anInt1597 = k1;
		projectileX = j2;
		projectileY = i2;
		startHeight = l1;
		startCycle = l;
		stopCycle = i1;
		initialSlope = i;
		initialDistance = j1;
		target = k2;
		endHeight = j;
		started = false;
	}

	public void progressCycles(int cyclesMissed) {
		started = true;
		xPos += xIncrement * (double)cyclesMissed;
		yPos += yIncrement * (double)cyclesMissed;
		cnterHeight += heightIncrement * (double)cyclesMissed + 0.5D * aDouble1578 * (double)cyclesMissed * (double)cyclesMissed;
		heightIncrement += aDouble1578 * (double)cyclesMissed;

		turnValue = (int)(Math.atan2(xIncrement, yIncrement) * 325.94900000000001D) + 1024 & 0x7ff;
		tiltAngle = (int)(Math.atan2(heightIncrement, diagonalIncrement) * 325.94900000000001D) & 0x7ff;
		if(projectileGFX.animationSequence != null) {
			for(gfxTickOfCurrentStage += cyclesMissed; gfxTickOfCurrentStage > projectileGFX.animationSequence.duration(gfxStage);) {
				gfxTickOfCurrentStage -= projectileGFX.animationSequence.duration(gfxStage) + 1;
				gfxStage++;
				if(gfxStage >= projectileGFX.animationSequence.frameCount) {
					gfxStage = 0;
				}
			}
		}
	}

	public int endX, endY;

	public void stillFocus(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	@Override
	public Actor getInteracting() {
		if(target < 0) {
			return Client.instance.playerArray[-target - 1];
		} else if(target > 0) {
			return Client.instance.getNpcs().get(target - 1);
		}
		return null;
	}

	@Override
	public int getRemainingCycles() {
		return stopCycle - Client.instance.getTickCount();
	}

	@Override
	public int getId() {
		return projectileGFX.animationId;
	}

	@Override
	public int getRsInteracting() {
		return target;
	}

	@Override
	public int getHeight() {
		return (int) cnterHeight;
	}

	@Override
	public int getEndHeight() {
		return endHeight;
	}

	@Override
	public int getX1() {
		return endX;
	}

	@Override
	public int getY1() {
		return endY;
	}

	@Override
	public int getFloor() {
		return (int) cnterHeight;
	}

	@Override
	public int getStartMovementCycle() {
		return startCycle;
	}

	@Override
	public int getEndCycle() {
		return stopCycle;
	}

	@Override
	public int getSlope() {
		return initialSlope;
	}

	@Override
	public int getStartHeight() {
		return startHeight;
	}

	@Override
	public double getX() {
		return xPos;
	}

	@Override
	public double getY() {
		return yPos;
	}

	@Override
	public double getZ() {
		return Client.instance.getPlane();
	}

	@Override
	public double getScalar() {
		return this.cnterHeight;
	}

	@Override
	public double getVelocityX() {
		return xIncrement;
	}

	@Override
	public double getVelocityY() {
		return yIncrement;
	}

	@Override
	public double getVelocityZ() {
		return diagonalIncrement;
	}

	@Override
	public RSNode getNext() {
		return null;
	}

	@Override
	public long getHash() {
		return 0;
	}

	@Override
	public RSNode getPrevious() {
		return null;
	}

	@Override
	public void onUnlink() {

	}

	@Override
	public int getModelHeight() {
		return 0;
	}

	@Override
	public void setModelHeight(int modelHeight) {
		modelBaseY = modelHeight;
	}

	@Override
	public RSModel getModel() {
		return null;
	}

	@Override
	public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {
		renderAtPoint(orientation, pitchSin, pitchCos, yawSin, yawCos, x, y, z, hash);
	}

}