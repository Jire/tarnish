package com.osroyale;

public class FadingScreen {

	/**
	 * The state of the fade on the screen
	 */
	private byte state;

	/**
	 * How many seconds the fade exists for
	 */
	private byte seconds;

	/**
	 * The string of text that will be displayed on the screen
	 */
	private String text;

	/**
	 * Controls how long it's been since the screen started its last fade
	 */
	private Stopwatch watch;

	/**
	 * The stall time.
	 */
	private int stall;

	private boolean stall_flag;

	/**
	 * Acts as a means to instance the local {@link FadingScreen} object. Since
	 * the value of state by default is 0, nothing will be drawn on the screen.
	 */
	public FadingScreen() {
	}

	/**
	 * Creates a new fading screen
	 *
	 * @param state   the state of the fade
	 * @param seconds the duration of the fade
	 */
	public FadingScreen(String text, byte state, byte seconds) {
		this.text = text;
		this.state = state;
		this.seconds = seconds;
		this.stall = 0;
		this.stall_flag = false;
		this.watch = new Stopwatch();
		this.watch.reset();
	}

	/**
	 * Draws the animation on the screen. If the state of the screen is
	 * currently 0 the animation will not be drawn.
	 */
	public void draw() {
		if (state == 0) {
			return;
		}

		boolean isFixed = !Client.instance.isResized();
		long end = watch.getStartTime() + (1000L * seconds);
		long increment = ((end - watch.getStartTime()) / 100);

		if (stall_flag) {
			Rasterizer2D.setDrawingArea(0, 0, isFixed ? 512 : Client.canvasWidth, isFixed ? 334 : Client.canvasHeight);
			Rasterizer2D.drawAlphaPixels(0, 0, isFixed ? 512 : Client.canvasWidth, isFixed ? 334 : Client.canvasHeight, 0x000000, 255, true);
			Client.instance.newSmallFont.drawCenteredString(text, (isFixed ? 512 : Client.canvasWidth) / 2, (isFixed ? 334 : Client.canvasHeight) / 2, 0xFFFFFF, 0);
			if (stall++ == 25) {
				state = 0;
			}
			return;
		}

		if (increment > 0) {
			long percentile = watch.elapsed() / increment;
			int opacity = (int) ((percentile * (Byte.MAX_VALUE / 100.0)) * 2);
			if (state < 0) {
				opacity = 255 - opacity;
			}
			if (percentile > -1 && percentile <= 100) {
				Rasterizer2D.setDrawingArea(0, 0, isFixed ? 512 : Client.canvasWidth, isFixed ? 334 : Client.canvasHeight);
				Rasterizer2D.drawAlphaPixels(0, 0, isFixed ? 512 : Client.canvasWidth, isFixed ? 334 : Client.canvasHeight, 0x000000, opacity, true);
				Client.instance.newSmallFont.drawCenteredString(text, (isFixed ? 512 : Client.canvasWidth) / 2, (isFixed ? 334 : Client.canvasHeight) / 2, 0xFFFFFF, 0);
				if (percentile == 100) {
					stall_flag = true;
				}
			}
		}
	}
}