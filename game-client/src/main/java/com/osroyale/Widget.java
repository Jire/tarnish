package com.osroyale;

public class Widget {

	public static final Widget[] widgets = new Widget[10];

	private final int spriteId;
	private int seconds, dx, dy;
	private boolean disableAlpha;

	public Widget(int seconds, int spriteId) {
		if (seconds < 0)
			seconds = 0;
		this.seconds = seconds;
		this.spriteId = spriteId;
	}

	public void decrement() {
		seconds--;
		if (seconds < 0)
			seconds = 0;
	}

	public boolean terminated() {
		return seconds <= 0;
	}

	public static void draw() {
		int x = 445;
		int y = 250;

		if (Client.instance.isResized()) {
			x = Client.canvasWidth - 305;
			y = Client.canvasHeight - 190;
		}

		for (int index = 0; index < widgets.length - 1; index++) {
			Widget widget = widgets[index];

			if (widget == null) {
				continue;
			}

			Sprite sprite = Client.spriteCache.get(widget.spriteId);

			double ratio = Math.abs(widget.dy / 24.0);
			int alpha = widget.disableAlpha ? 200 : (int) (200 - 200 * ratio);
			int textAlpha = widget.disableAlpha ? 256 : (int) (256 - 256 * ratio);

			if (widget.dx > 0) {
				ratio = Math.abs(widget.dx / 24.0);
				alpha = (int) (200 * ratio);
				textAlpha = (int) (256 * ratio);
			}

			int dy = widget.dy;

			Rasterizer2D.drawAlphaPixels(x, y - dy, 65, 22, 0x473F35, alpha, true);
			Rasterizer2D.drawRectangle(x, y - dy, 65, 22, 0x000000, (alpha - 80) > 0 ? alpha - 80 : 0);
			sprite.drawSprite1(x + 3, y - dy - sprite.height / 2 + 19 / 2 + 2, alpha);

			Client.instance.newSmallFont.drawCenteredString(Utility.getFormattedTime(widget.seconds), x + 38, y + 16 - dy, 0xFF8C00, 0, textAlpha);

			y -= 24;
		}
	}

	private static void add(Widget widget) {
		int index = 0;

		if (widget.seconds == 0) {
			return;
		}

		for (int i = 0; i < widgets.length; i++) {
			if (widgets[i] == null || widgets[i].spriteId == widget.spriteId) {
				index = i;
				break;
			}
		}

		widget.dy = -24;
		widgets[index] = widget;
	}

	public static void tick() {
		for (int index = 0; index < widgets.length; index++) {
			Widget widget = widgets[index];

			if (widget == null) {
				continue;
			}

			widget.decrement();

			if (widget.terminated() && widget.dx == 0) {
				remove(widget);
				continue;
			}

			if (widget.dx != 0 || widget.dy != 0) {
				if (widget.dx != 0) {
					if (widget.dx > 0) {
						widget.dx--;
					}

					if (widget.dx == 0)
						remove2(index);
				} else {
					if (widget.dy > 0) {
						widget.dy--;
					} else if (widget.dy < 0) {
						widget.dy++;
					}

					if (widget.dy == 0)
						widget.disableAlpha = true;
				}
			}
		}
	}

	private static void remove(Widget widget) {
		for (Widget next : widgets) {
			if (next == null) {
				return;
			}

			if (next.spriteId == widget.spriteId) {
				next.dx = 24;
				break;
			}
		}
	}

	private static void remove2(int index) {
		widgets[index] = null;

		for (int next = index; next < widgets.length - 1; next++) {
			Widget temp = widgets[next];

			widgets[next] = widgets[next + 1];
			widgets[next + 1] = temp;

			if (widgets[next] != null)
				widgets[next].dy = 18;
			if (widgets[next + 1] != null)
				widgets[next + 1].dy = 18;
		}
	}

	public static void place(int type, int time) {
		switch (type) {
			case 1:
				submit(new Widget(time, 105));
				break;
			case 2:
				submit(new Widget(time, 106));
				break;
			case 3:
				submit(new Widget(time, 107));
				break;
			case 4:
				submit(new Widget(time, 108));
				break;
			case 5:
				submit(new Widget(time, 407));
				break;
			case 6:
				submit(new Widget(time, 458));
				break;
			case 7:
				submit(new Widget(time, 79));
				break;
		}
	}

	private static void submit(Widget widget) {
		if (widget.seconds <= 0) {
			remove(widget);
		} else {
			add(widget);
		}
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", seconds, spriteId);
	}

}
