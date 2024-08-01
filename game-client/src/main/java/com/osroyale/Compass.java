package com.osroyale;

public enum Compass {
	NORTH(0, 1, new int[]{1, 0, 31, 30}),
	NORTHWEST(-1, 1, new int[]{5, 4, 3, 2}),
	WEST(-2, 0, new int[]{9, 8, 7, 6}),
	SOUTHWEST(-1, -1, new int[]{13, 12, 11, 10}),
	SOUTH(0, -1, new int[]{17, 16, 15, 14}),
	SOUTHEAST(1, -1, new int[]{21, 20, 19, 18}),
	EAST(2, 0, new int[]{25, 24, 23, 22}),
	NORTHEAST(1, 1, new int[]{29, 28, 27, 26});

	private final int x;
	private final int y;
	private final int[] delta;

	Compass(int x, int y, int[] i) {
		this.x = x;
		this.y = y;
		this.delta = i;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Compass opposite() {
		Compass[][] c = new Compass[][]{{NORTH, SOUTH}, {WEST, EAST}, {NORTHWEST, SOUTHEAST}, {NORTHEAST, SOUTHWEST}};
		for (Compass[] _c : c) {
			if (this == _c[0]) {
				return _c[1];
			} else if (this == _c[1]) {
				return _c[0];
			}
		}
		return null;
	}

	public static Compass rotate(Compass compass, boolean right) {
		Compass[] values = Compass.values();
		int idx = compass.ordinal() + (right ? 1 : -1);
		if (idx >= values.length)
			idx = 0;
		if (idx < 0)
			idx = values.length - 1;
		return values[idx];
	}

	public static Compass getCurrentDirection(int direction) {
		direction = direction / 64;
		for (Compass c : Compass.values()) {
			for (int dir : c.delta) {
				if (direction == dir) {
					return c;
				}
			}
		}
		return Compass.NORTH;
	}

	@Override
	public String toString() {
		return this.name();
	}
}
