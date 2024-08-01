package net.runelite.client.plugins.hd.scene.lights;

import net.runelite.api.GraphicsObject;
import net.runelite.api.NPC;
import net.runelite.api.Projectile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.hd.utils.HDUtils;

public class SceneLight extends Light
{
	public final int randomOffset = HDUtils.rand.nextInt();

	public int currentSize;
	public float currentStrength;
	/**
	 * Linear color space RGBA in the range [0, 1]
	 */
	public float[] currentColor;
	public float currentAnimation = 0.5f;
	public int currentFadeIn = 0;
	public boolean visible = true;

	public WorldPoint worldPoint;
	public int x;
	public int y;
	public int z;
	public int distance = 0;
	public boolean belowFloor = false;
	public boolean aboveFloor = false;

	public Projectile projectile;
	public NPC npc;
	public TileObject object;
	public GraphicsObject graphicsObject;

	public SceneLight(Light l) {
		super(
			l.description,
			l.worldX,
			l.worldY,
			l.plane,
			l.alignment,
			l.height,
			l.radius,
			l.strength,
			l.color,
			l.type,
			l.duration,
			l.range,
			l.fadeInDuration,
			l.npcIds,
			l.objectIds,
			l.projectileIds,
			l.graphicsObjectIds
		);

		currentSize = radius;
		currentStrength = strength;
		currentColor = color;
		if (type == LightType.PULSE)
			currentAnimation = (float) Math.random();
	}
}
