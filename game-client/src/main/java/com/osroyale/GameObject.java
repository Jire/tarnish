package com.osroyale;


import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.RSGameObject;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSRenderable;

import java.awt.*;

public final class GameObject implements RSGameObject {
	int zLoc;
	int tileHeight;
	int xPos;
	int yPos;
	public Renderable renderable;
	public int turnValue;
	int xLocLow;
	int xLocHigh;
	int yLocHigh;
	int yLocLow;
	int anInt527;
	int anInt528;
	public long uid;

	public RSModel getModel() {
		RSRenderable renderable = getRenderable();
		if (renderable == null)
		{
			return null;
		}

		if (renderable instanceof RSModel)
		{
			return (RSModel) renderable;
		}
		else
		{
			return renderable.getModel();
		}
	}

	@Override
	public int sizeX() {
		return xLocHigh - xLocLow;
	}

	@Override
	public int sizeY() {
		return yLocHigh - yLocLow;
	}

	@Override
	public Point getSceneMinLocation() {
		return new Point(this.getStartX(), this.getStartY());
	}

	@Override
	public Point getSceneMaxLocation() {
		return new Point(this.getEndX(), this.getEndY());
	}

	@Override
	public Shape getConvexHull() {
		RSModel model = getModel();
		if (model == null)
		{
			return null;
		}
		int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());
		return model.getConvexHull(getX(), getY(), getModelOrientation(), tileHeight);
	}

	@Override
	public int getOrientation() {
		int orientation = getModelOrientation();
		int rotation = (getFlags() >> 6) & 3;
		return rotation * 512 + orientation;
	}

	@Override
	public int getId() {
		return ObjectKeyUtil.getObjectId(uid);
	}

	@Override
	public Point getCanvasLocation() {
		return Perspective.localToCanvas(Client.instance, getLocalLocation(), getPlane(), 0);
	}

	@Override
	public Point getCanvasLocation(int zOffset) {
		return Perspective.localToCanvas(Client.instance, this.getLocalLocation(), this.getPlane(), zOffset);
	}

	@Override
	public Polygon getCanvasTilePoly() {
		return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
	}

	@Override
	public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
		return Perspective.getCanvasTextLocation(Client.instance, graphics, this.getLocalLocation(), text, zOffset);
	}

	@Override
	public Point getMinimapLocation() {
		return Perspective.localToMinimap(Client.instance, getLocalLocation());
	}

	@Override
	public Shape getClickbox() {
		return Perspective.getClickbox(Client.instance, this.getModel(), this.getModelOrientation(), getLocalLocation());
	}

	@Override
	public String getName() { return ObjectDefinition.forID(getId()).name; }

	@Override
	public String[] getActions() {
		return null;
	}

	@Override
	public WorldPoint getWorldLocation() {
		return WorldPoint.fromLocal(Client.instance, this.getX(), this.getY(), this.getPlane());
	}

	@Override
	public LocalPoint getLocalLocation() {
		return new LocalPoint(this.getX(), this.getY());
	}

	@Override
	public RSRenderable getRenderable() {
		return renderable;
	}

	@Override
	public int getStartX() {
		return xLocLow;
	}

	@Override
	public int getStartY() {
		return yLocLow;
	}

	@Override
	public int getEndX() {
		return xLocHigh;
	}

	@Override
	public int getEndY() {
		return yLocHigh;
	}

	@Override
	public int getX() {
		return xPos;
	}

	@Override
	public int getY() {
		return yPos;
	}

	@Override
	public int getHeight() {
		return tileHeight;
	}

	@Override
	public int getModelOrientation() {
		return turnValue;
	}

	@Override
	public int getConfig() {
		return tileHeight;
	}

	@Override
	public long getHash() {
		return uid;
	}

	@Override
	public int getFlags() {
		return 0;
	}

	@Override
	public int getPlane() {
		return zLoc;
	}

	@Override
	public void setPlane(int plane) {
		zLoc = plane;
	}

}
