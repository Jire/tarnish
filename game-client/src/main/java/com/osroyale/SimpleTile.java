package com.osroyale;

import net.runelite.rs.api.RSSceneTilePaint;

public final class SimpleTile implements RSSceneTilePaint {

	private final int northEastColor;
	private final int northColor;
	private final int centerColor;
	private final int eastColor;
	private final int texture;
	private final boolean flat;
	private final int colorRGB;

	public SimpleTile(int northEastColor, int northColor, int centerColor, int eastColor, int texture, int colorRGB, boolean flat) {
		this.northEastColor = northEastColor;
		this.northColor = northColor;
		this.centerColor = centerColor;
		this.eastColor = eastColor;
		this.texture = texture;
		this.colorRGB = colorRGB;
		this.flat = flat;
	}

	public int getNorthEastColor() {
		return northEastColor;
	}

	public int getNorthColor() {
		return northColor;
	}

	public int getCenterColor() {
		return centerColor;
	}

	public int getEastColor() {
		return eastColor;
	}

	public int getTexture() {
		return texture;
	}

	public boolean isFlat() {
		return flat;
	}

	public int getColourRGB() {
		return colorRGB;
	}

	public int bufferOffset = -1;
	public int uVBufferOffset = -1;
	public int bufferLength = -1;

	@Override
	public int getBufferOffset() {
		return bufferOffset;
	}

	@Override
	public void setBufferOffset(int bufferOffset) {
		this.bufferOffset = bufferOffset;
	}

	@Override
	public int getUvBufferOffset() {
		return uVBufferOffset;
	}

	@Override
	public void setUvBufferOffset(int bufferOffset) {
		uVBufferOffset = bufferOffset;
	}

	@Override
	public int getBufferLen() {
		return bufferLength;
	}

	@Override
	public void setBufferLen(int bufferLen) {
		bufferLength = bufferLen;
	}

	@Override
	public int getRBG() {
		return colorRGB;
	}

	@Override
	public int getSwColor() {
		return northEastColor;
	}

	@Override
	public int getSeColor() {
		return northColor;
	}

	@Override
	public int getNwColor() {
		return eastColor;
	}

	@Override
	public int getNeColor() {
		return centerColor;
	}

	@Override
	public boolean getIsFlat() {
		return false;
	}

	@Override
	public void setRBG(int val) {
	}

	@Override
	public void setSwColor(int val) {
	}

	@Override
	public void setSeColor(int val) {
	}

	@Override
	public void setNwColor(int val) {
	}

	@Override
	public void setNeColor(int val) {
	}

	@Override
	public void setIsFlat(boolean val) {
	}

	@Override
	public void setTexture(int val) {
	}
}