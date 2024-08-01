package com.osroyale;

import java.awt.*;

public class ColorUtility {
	public static int fadeStep = 1;
	public static int fadingToColor;
	public static boolean switchColor = false;

	private static float hue = 0.0f, sat = 1.0f, bri = 1.0f;

	public static int getHexColors() {
		hue += 0.0005;
		Color base = new Color(Color.HSBtoRGB(hue, sat, bri));
		Color color = new Color(base.getRed(), base.getGreen(), base.getBlue(), 125);
		return color.getRGB();
	}

	public static int fadeColors(Color color1, Color color2, float step) {
		float ratio = step / 100;
		int r = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
		int g = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
		int b = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
		return new Color(r, g, b).getRGB();
	}
}
