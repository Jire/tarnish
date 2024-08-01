package com.osroyale;

import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.RSItemLayer;
import net.runelite.rs.api.RSRenderable;

import java.awt.*;

final class GroundItemTile implements RSItemLayer {

	int zLoc;
	int tileHeights;
	int xPos;
	int yPos;
	Renderable topNode;
	Renderable lowerNode;
	Renderable middleNode;
	long uid;
	int itemDropHeight;

	@Override
	public net.runelite.api.Model getModelBottom() {
		Renderable renderable = (Renderable) getBottom();
		if (renderable == null)
		{
			return null;
		}

		if (renderable instanceof net.runelite.api.Model)
		{
			return (net.runelite.api.Model) renderable;
		}
		else
		{
			return renderable.getModel();
		}
	}

	@Override
	public net.runelite.api.Model getModelMiddle() {
		Renderable renderable = (Renderable) getMiddle();
		if (renderable == null)
		{
			return null;
		}

		if (renderable instanceof net.runelite.api.Model)
		{
			return (net.runelite.api.Model) renderable;
		}
		else
		{
			return renderable.getModel();
		}
	}

	@Override
	public net.runelite.api.Model getModelTop() {
		Renderable renderable = (Renderable) getTop();
		if (renderable == null)
		{
			return null;
		}

		if (renderable instanceof net.runelite.api.Model)
		{
			return (Model) renderable;
		}
		else
		{
			return renderable.getModel();
		}
	}

	@Override
	public int getPlane() {
		return zLoc;
	}

	@Override
	public int getId() {
		return itemDropHeight;
	}

	@Override
	public Point getCanvasLocation() {
		return Perspective.localToCanvas(Client.instance, getLocalLocation(), getPlane(), 0);
	}

	@Override
	public Point getCanvasLocation(int zOffset) {
		return Perspective.localToCanvas(Client.instance, getLocalLocation(), getPlane(), zOffset);
	}

	@Override
	public Polygon getCanvasTilePoly() {
		return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
	}

	@Override
	public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
		return Perspective.getCanvasTextLocation(Client.instance, graphics, getLocalLocation(), text, zOffset);
	}

	@Override
	public Point getMinimapLocation() {
		return Perspective.localToMinimap(Client.instance, getLocalLocation());
	}

	@Override
	public Shape getClickbox() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return ItemDefinition.lookup(getId()).name;
	}

	@Override
	public String[] getActions() {
		return ItemDefinition.lookup(getId()).itemActions;
	}

	@Override
	public WorldPoint getWorldLocation() {
		return WorldPoint.fromLocal(Client.instance, getX(), getY(), getPlane());
	}

	@Override
	public LocalPoint getLocalLocation() {
		return new LocalPoint(getX(), getY());
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
	public long getHash() {
		return uid;
	}

	@Override
	public int getHeight() {
		return tileHeights;
	}

	@Override
	public RSRenderable getBottom() {
		return topNode;
	}

	@Override
	public RSRenderable getMiddle() {
		return lowerNode;
	}

	@Override
	public RSRenderable getTop() {
		return middleNode;
	}

	@Override
	public void setPlane(int plane) {
		zLoc = plane;
	}

}
