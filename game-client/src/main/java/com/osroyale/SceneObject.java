package com.osroyale;

public final class SceneObject extends Renderable {

	private int animationFrame;
	private final int[] configs;
	private final int varbitId;
	private final int configId;
	private Animation animation;
	private int cycleDelay;
	public static Client clientInstance;
	private final int objectId;
	private final int type;
	private final int orientation;

	private final int sceneX;
	private final int sceneY;
	private final int plane;

	private ObjectDefinition getDefinition() {
		int index = -1;
		if (varbitId != -1) {
			try {
				VarBit varBit = VarBit.varBits[varbitId];
				int k = varBit.index;
				int l = varBit.leastSignificantBit;
				int i1 = varBit.mostSignificantBit;
				int j1 = Client.varBits[i1 - l];
				index = clientInstance.settings[k] >> l & j1;
			} catch (Exception ex) {
			}
		} else if (configId != -1) {
			index = clientInstance.settings[configId];
		}
		if (index < 0 || index >= configs.length || configs[index] == -1) {
			return null;
		} else {
			return ObjectDefinition.forID(configs[index]);
		}
	}

	@Override
	public Model getRotatedModel() {
		if (animation != null) {
			int step = Client.tick - cycleDelay;
			if (step > 100 && animation.frameStep > 0) {
				step = 100;
			}
			while (step > animation.duration(animationFrame)) {
				step -= animation.duration(animationFrame);
				animationFrame++;
				if (animationFrame < animation.frameCount)
					continue;
				animationFrame -= animation.frameStep;
				if (animationFrame >= 0 && animationFrame < animation.frameCount)
					continue;
				animation = null;
				break;
			}
			cycleDelay = Client.tick - step;
		}

		ObjectDefinition def = configs != null ? getDefinition() : ObjectDefinition.forID(objectId);
		if (def == null) {
			return null;
		}

		int var2;
		int var3;
		if (this.orientation != 1 && this.orientation != 3) {
			var2 = def.width;
			var3 = def.length;
		} else {
			var2 = def.length;
			var3 = def.width;
		}

		int var4 = (var2 >> 1) + this.sceneX;
		int var5 = (var2 + 1 >> 1) + this.sceneX;
		int var6 = (var3 >> 1) + this.sceneY;
		int var7 = (var3 + 1 >> 1) + this.sceneY;
		int[][] tileHeights = Client.instance.getTileHeights()[this.plane];
		int tileHeightsUnknown = tileHeights[var4][var7] + tileHeights[var4][var6] + tileHeights[var5][var6] + tileHeights[var5][var7] >> 2;
		int finalX = (this.sceneX << 7) + (var2 << 6);
		int finalY = (this.sceneY << 7) + (var3 << 6);

		return def.getModelAnimated(type, orientation, animationFrame, animation, tileHeights, tileHeightsUnknown, finalX, finalY);
	}

	public SceneObject(int objectId, int orientation, int type, int animationId, boolean animating, int sceneX, int sceneY, int plane) {
		this.objectId = objectId;
		this.type = type;
		this.orientation = orientation;
		if (animationId != -1) {
			animation = Animation.animations[animationId];
			animationFrame = 0;
			cycleDelay = Client.tick;
			if (animating && animation.frameStep != -1) {
				if (animation.isSkeletalAnimation()) {
					animationFrame = (int)(Math.random() * (double)this.animation.getSkeletalLength());
				} else {
					animationFrame = (int) (Math.random() * (double) animation.frameCount);
					cycleDelay -= (int) (Math.random() * (double) animation.duration(animationFrame));
				}
			}
		}
		ObjectDefinition def = ObjectDefinition.forID(this.objectId);
		varbitId = def.varbit;
		configId = def.varp;
		configs = def.childrenIDs;
		this.sceneX = sceneX;
		this.sceneY = sceneY;
		this.plane = plane;
	}
}