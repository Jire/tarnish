package com.osroyale;

import net.runelite.rs.api.RSCollisionMap;

final class CollisionMap implements RSCollisionMap {

	CollisionMap() {
		mapReionWidth = 104;
		mapRegionLength = 104;
		clipData = new int[mapReionWidth][mapRegionLength];
		method210();
	}

	void method210() {
		for (int i = 0; i < mapReionWidth; i++) {
			for (int j = 0; j < mapRegionLength; j++)
				if (i == 0 || j == 0 || i == mapReionWidth - 1 || j == mapRegionLength - 1)
					clipData[i][j] = 0xffffff;
				else
					clipData[i][j] = 0x1000000;

		}

	}

	// itemY, rotation, itemX, type, objectDefinition.impenetrable
	void clip(int x, int y, int type, int rotation, boolean impenetrable) {
		if (type == 0) {
			if (rotation == 0) {
				flag(x, y, WALL_WEST);
				flag(x - 1, y, WALL_EAST);
			}

			if (rotation == 1) {
				flag(x, y, WALL_NORTH);
				flag(x, y + 1, WALL_SOUTH);
			}

			if (rotation == 2) {
				flag(x, y, WALL_EAST);
				flag(x + 1, y, WALL_WEST);
			}

			if (rotation == 3) {
				flag(x, y, WALL_SOUTH);
				flag(x, y - 1, WALL_NORTH);
			}
		}

		if (type == 1 || type == 3) {
			if (rotation == 0) {
				flag(x, y, WALL_NORTH_WEST);
				flag(x - 1, y + 1, WALL_SOUTH_EAST);
			}

			if (rotation == 1) {
				flag(x, y, WALL_NORTH_EAST);
				flag(x + 1, y + 1, WALL_SOUTH_WEST);
			}

			if (rotation == 2) {
				flag(x, y, WALL_SOUTH_EAST);
				flag(x + 1, y - 1, WALL_NORTH_WEST);
			}

			if (rotation == 3) {
				flag(x, y, WALL_SOUTH_WEST);
				flag(x - 1, y - 1, WALL_NORTH_EAST);
			}
		}

		if (type == 2) {
			if (rotation == 0) {
				flag(x, y, WALL_WEST | WALL_NORTH);
				flag(x - 1, y, WALL_EAST);
				flag(x, y + 1, WALL_SOUTH);
			}

			if (rotation == 1) {
				flag(x, y, WALL_NORTH | WALL_EAST);
				flag(x, y + 1, WALL_SOUTH);
				flag(x + 1, y, WALL_WEST);
			}

			if (rotation == 2) {
				flag(x, y, WALL_SOUTH | WALL_EAST);
				flag(x + 1, y, WALL_WEST);
				flag(x, y - 1, WALL_NORTH);
			}

			if (rotation == 3) {
				flag(x, y, WALL_WEST | WALL_SOUTH);
				flag(x, y - 1, WALL_NORTH);
				flag(x - 1, y, WALL_EAST);
			}
		}

		if (impenetrable) {
			if (type == 0) {
				if (rotation == 0) {
					flag(x, y, 0x10000);
					flag(x - 1, y, 0x1000);
				}

				if (rotation == 1) {
					flag(x, y, 0x400);
					flag(x, y + 1, 0x4000);
				}

				if (rotation == 2) {
					flag(x, y, 0x1000);
					flag(x + 1, y, 0x10000);
				}
				if (rotation == 3) {
					flag(x, y, 0x4000);
					flag(x, y - 1, 0x400);
				}
			}

			if (type == 1 || type == 3) {
				if (rotation == 0) {
					flag(x, y, 0x200);
					flag(x - 1, y + 1, 0x2000);
				}

				if (rotation == 1) {
					flag(x, y, 0x800);
					flag(x + 1, y + 1, 0x8000);
				}

				if (rotation == 2) {
					flag(x, y, 0x2000);
					flag(x + 1, y - 1, 0x200);
				}

				if (rotation == 3) {
					flag(x, y, 0x8000);
					flag(x - 1, y - 1, 0x800);
				}
			}

			if (type == 2) {
				if (rotation == 0) {
					flag(x, y, 0x10400);
					flag(x - 1, y, 0x1000);
					flag(x, y + 1, 0x4000);
				}

				if (rotation == 1) {
					flag(x, y, 0x1400);
					flag(x, y + 1, 0x4000);
					flag(x + 1, y, 0x10000);
				}

				if (rotation == 2) {
					flag(x, y, 0x5000);
					flag(x + 1, y, 0x10000);
					flag(x, y - 1, 0x400);
				}

				if (rotation == 3) {
					flag(x, y, 0x14000);
					flag(x, y - 1, 0x400);
					flag(x - 1, y, 0x1000);
				}
			}
		}
	}

	void clipObject(int x, int y, int width, int length, int rotation, boolean solid) {
		int flags = 256;
		if (solid)
			flags += 0x20000;

		if (rotation == 1 || rotation == 3) {
			int tmp = width;
			width = length;
			length = tmp;
		}

		for (int xx = x; xx < x + width; xx++)
			if (xx >= 0 && xx < mapReionWidth) {
				for (int yy = y; yy < y + length; yy++)
					if (yy >= 0 && yy < mapRegionLength)
						flag(xx, yy, flags);

			}
	}

	void block(int x, int y) {
		clipData[x][y] |= BLOCKED;
	}

	private void flag(int x, int y, int value) {
		clipData[x][y] |= value;
	}

	void method215(int x, int y, int type, int direction, boolean solid) {
		if (type == 0) {
			if (direction == 0) {
				method217(WALL_WEST, x, y);
				method217(WALL_EAST, x - 1, y);
			}
			if (direction == 1) {
				method217(WALL_NORTH, x, y);
				method217(WALL_SOUTH, x, y + 1);
			}
			if (direction == 2) {
				method217(WALL_EAST, x, y);
				method217(WALL_WEST, x + 1, y);
			}
			if (direction == 3) {
				method217(WALL_SOUTH, x, y);
				method217(WALL_NORTH, x, y - 1);
			}
		}
		if (type == 1 || type == 3) {
			if (direction == 0) {
				method217(WALL_NORTH_WEST, x, y);
				method217(WALL_SOUTH_EAST, x - 1, y + 1);
			}
			if (direction == 1) {
				method217(WALL_NORTH_EAST, x, y);
				method217(WALL_SOUTH_WEST, x + 1, y + 1);
			}
			if (direction == 2) {
				method217(WALL_SOUTH_EAST, x, y);
				method217(WALL_NORTH_WEST, x + 1, y - 1);
			}
			if (direction == 3) {
				method217(WALL_SOUTH_WEST, x, y);
				method217(WALL_NORTH_EAST, x - 1, y - 1);
			}
		}
		if (type == 2) {
			if (direction == 0) {
				method217(0x82, x, y);
				method217(WALL_EAST, x - 1, y);
				method217(WALL_SOUTH, x, y + 1);
			}
			if (direction == 1) {
				method217(0xa, x, y);
				method217(WALL_SOUTH, x, y + 1);
				method217(WALL_WEST, x + 1, y);
			}
			if (direction == 2) {
				method217(0x28, x, y);
				method217(WALL_WEST, x + 1, y);
				method217(WALL_NORTH, x, y - 1);
			}
			if (direction == 3) {
				method217(0b1010_0000, x, y);
				method217(WALL_NORTH, x, y - 1);
				method217(WALL_EAST, x - 1, y);
			}
		}
		if (solid) {
			if (type == 0) {
				if (direction == 0) {
					method217(0x10000, x, y);
					method217(0x1000, x - 1, y);
				}
				if (direction == 1) {
					method217(0x400, x, y);
					method217(0x4000, x, y + 1);
				}
				if (direction == 2) {
					method217(0x1000, x, y);
					method217(0x10000, x + 1, y);
				}
				if (direction == 3) {
					method217(0x4000, x, y);
					method217(0x400, x, y - 1);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					method217(0x200, x, y);
					method217(0x2000, x - 1, y + 1);
				}
				if (direction == 1) {
					method217(0x800, x, y);
					method217(0x8000, x + 1, y + 1);
				}
				if (direction == 2) {
					method217(0x2000, x, y);
					method217(0x200, x + 1, y - 1);
				}
				if (direction == 3) {
					method217(0x8000, x, y);
					method217(0x800, x - 1, y - 1);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					method217(0x10400, x, y);
					method217(0x1000, x - 1, y);
					method217(0x4000, x, y + 1);
				}
				if (direction == 1) {
					method217(0x1400, x, y);
					method217(0x4000, x, y + 1);
					method217(0x10000, x + 1, y);
				}
				if (direction == 2) {
					method217(0x5000, x, y);
					method217(0x10000, x + 1, y);
					method217(0x400, x, y - 1);
				}
				if (direction == 3) {
					method217(0x14000, x, y);
					method217(0x400, x, y - 1);
					method217(0x1000, x - 1, y);
				}
			}
		}
	}

	void method216(int i, int j, int k, int l, int i1, boolean flag) {
		int j1 = 256;
		if (flag)
			j1 += 0x20000;
		if (i == 1 || i == 3) {
			int k1 = j;
			j = i1;
			i1 = k1;
		}
		for (int l1 = k; l1 < k + j; l1++)
			if (l1 >= 0 && l1 < mapReionWidth) {
				for (int i2 = l; i2 < l + i1; i2++)
					if (i2 >= 0 && i2 < mapRegionLength)
						method217(j1, l1, i2);

			}

	}

	private void method217(int i, int j, int k) {
		clipData[j][k] &= 0xffffff - i;
	}

	void method218(int j, int k) {
		clipData[k][j] &= 0xdfffff;
	}

	boolean withinDistanceWalls(int startX, int startY, int targetX, int targetY, int type, int direction) {
		if (startX == targetX && startY == targetY)
			return true;
		if (type == 1)
			if (direction == 0) {
				if (startX == targetX - 1 && startY == targetY)
					return true;
				if (startX == targetX && startY == targetY + 1 && (clipData[startX][startY] & 0x1280120) == 0)
					return true;
				if (startX == targetX && startY == targetY - 1 && (clipData[startX][startY] & 0x1280102) == 0)
					return true;
			} else if (direction == 1) {
				if (startX == targetX && startY == targetY + 1)
					return true;
				if (startX == targetX - 1 && startY == targetY && (clipData[startX][startY] & 0x1280108) == 0)
					return true;
				if (startX == targetX + 1 && startY == targetY && (clipData[startX][startY] & 0x1280180) == 0)
					return true;
			} else if (direction == 2) {
				if (startX == targetX + 1 && startY == targetY)
					return true;
				if (startX == targetX && startY == targetY + 1 && (clipData[startX][startY] & 0x1280120) == 0)
					return true;
				if (startX == targetX && startY == targetY - 1 && (clipData[startX][startY] & 0x1280102) == 0)
					return true;
			} else if (direction == 3) {
				if (startX == targetX && startY == targetY - 1)
					return true;
				if (startX == targetX - 1 && startY == targetY && (clipData[startX][startY] & 0x1280108) == 0)
					return true;
				if (startX == targetX + 1 && startY == targetY && (clipData[startX][startY] & 0x1280180) == 0)
					return true;
			}
		if (type == 3)
			if (direction == 0) {
				if (startX == targetX - 1 && startY == targetY)
					return true;
				if (startX == targetX && startY == targetY + 1)
					return true;
				if (startX == targetX + 1 && startY == targetY && (clipData[startX][startY] & 0x1280180) == 0)
					return true;
				if (startX == targetX && startY == targetY - 1 && (clipData[startX][startY] & 0x1280102) == 0)
					return true;
			} else if (direction == 1) {
				if (startX == targetX - 1 && startY == targetY && (clipData[startX][startY] & 0x1280108) == 0)
					return true;
				if (startX == targetX && startY == targetY + 1)
					return true;
				if (startX == targetX + 1 && startY == targetY)
					return true;
				if (startX == targetX && startY == targetY - 1 && (clipData[startX][startY] & 0x1280102) == 0)
					return true;
			} else if (direction == 2) {
				if (startX == targetX - 1 && startY == targetY && (clipData[startX][startY] & 0x1280108) == 0)
					return true;
				if (startX == targetX && startY == targetY + 1 && (clipData[startX][startY] & 0x1280120) == 0)
					return true;
				if (startX == targetX + 1 && startY == targetY)
					return true;
				if (startX == targetX && startY == targetY - 1)
					return true;
			} else if (direction == 3) {
				if (startX == targetX - 1 && startY == targetY)
					return true;
				if (startX == targetX && startY == targetY + 1 && (clipData[startX][startY] & 0x1280120) == 0)
					return true;
				if (startX == targetX + 1 && startY == targetY && (clipData[startX][startY] & 0x1280180) == 0)
					return true;
				if (startX == targetX && startY == targetY - 1)
					return true;
			}
		if (type == 10) {
			if (startX == targetX && startY == targetY + 1 && (clipData[startX][startY] & WALL_SOUTH) == 0)
				return true;
			if (startX == targetX && startY == targetY - 1 && (clipData[startX][startY] & WALL_NORTH) == 0)
				return true;
			if (startX == targetX - 1 && startY == targetY && (clipData[startX][startY] & WALL_EAST) == 0)
				return true;
			if (startX == targetX + 1 && startY == targetY && (clipData[startX][startY] & WALL_WEST) == 0)
				return true;
		}
		return false;
	}

	public boolean withinDistanceCorners(int startX, int startY, int targetX, int targetY, int type, int direction) {
		if (targetX == startX && targetY == startY)
			return true;

		if (type == 7 || type == 8) {
			if (type == 8)
				direction = direction + 2 & 3;

			if (direction == 0) {
				if (targetX == startX + 1 && targetY == startY && (clipData[targetX][targetY] & WALL_WEST) == 0)
					return true;
				if (targetX == startX && targetY == startY - 1 && (clipData[targetX][targetY] & WALL_NORTH) == 0)
					return true;
			} else if (direction == 1) {
				if (targetX == startX - 1 && targetY == startY && (clipData[targetX][targetY] & WALL_EAST) == 0)
					return true;
				if (targetX == startX && targetY == startY - 1 && (clipData[targetX][targetY] & WALL_NORTH) == 0)
					return true;
			} else if (direction == 2) {
				if (targetX == startX - 1 && targetY == startY && (clipData[targetX][targetY] & WALL_EAST) == 0)
					return true;
				if (targetX == startX && targetY == startY + 1 && (clipData[targetX][targetY] & WALL_SOUTH) == 0)
					return true;
			} else if (direction == 3) {
				if (targetX == startX + 1 && targetY == startY && (clipData[targetX][targetY] & WALL_WEST) == 0)
					return true;
				if (targetX == startX && targetY == startY + 1 && (clipData[targetX][targetY] & WALL_SOUTH) == 0)
					return true;
			}
		}

		if (type == 9) {
			if (targetX == startX && targetY == startY + 1 && (clipData[targetX][targetY] & WALL_SOUTH) == 0)
				return true;
			if (targetX == startX && targetY == startY - 1 && (clipData[targetX][targetY] & WALL_NORTH) == 0)
				return true;
			if (targetX == startX - 1 && targetY == startY && (clipData[targetX][targetY] & WALL_EAST) == 0)
				return true;
			if (targetX == startX + 1 && targetY == startY && (clipData[targetX][targetY] & WALL_WEST) == 0)
				return true;
		}
		return false;
	}

	boolean withinDistanceTarget(int startX, int startY, int targetX, int targetY, int targetWidth, int targetLength, int face) {
		int targetTopX = targetX + targetWidth - 1;
		int targetTopY = targetY + targetLength - 1;

		if (startX >= targetX && startX <= targetTopX && startY >= targetY && startY <= targetTopY)
			return true;

		if (startX == targetX - 1 && startY >= targetY && startY <= targetTopY && (clipData[startX][startY] & WALL_EAST) == 0 && (face & 8) == 0)
			return true;

		if (startX == targetTopX + 1 && startY >= targetY && startY <= targetTopY && (clipData[startX][startY] & WALL_WEST) == 0 && (face & 2) == 0)
			return true;

		if (startY == targetY - 1 && startX >= targetX && startX <= targetTopX && (clipData[startX][startY] & WALL_NORTH) == 0 && (face & 4) == 0)
			return true;

		if (startY == targetTopY + 1 && startX >= targetX && startX <= targetTopX && (clipData[startX][startY] & WALL_SOUTH) == 0 && (face & 1) == 0)
			return true;

		return false;
	}

	private final int mapReionWidth;
	private final int mapRegionLength;
	final int[][] clipData;
	private static final int BLOCKED = 0x200000;
	private static final int WALL_NORTH = 0x2;
	private static final int WALL_SOUTH = 0x20;
	private static final int WALL_EAST = 0x8;
	private static final int WALL_WEST = 0x80;
	private static final int WALL_NORTH_EAST = 0x4;
	private static final int WALL_NORTH_WEST = 0x1;
	private static final int WALL_SOUTH_EAST = 0x10;
	private static final int WALL_SOUTH_WEST = 0x40;

	@Override
	public int[][] getFlags() {
		return clipData;
	}
}
