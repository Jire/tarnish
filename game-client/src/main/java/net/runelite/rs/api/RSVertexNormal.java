package net.runelite.rs.api;

import net.runelite.mapping.Import;

public interface RSVertexNormal
{
	@Import("x")
	int getX();

	@Import("y")
	int getY();

	@Import("z")
	int getZ();

	static long bitpack(final int x, final int y, final int z,
						final int magnitude) {
		if (/*x > 0x1FFFF || */y > 0x1FFFF || z > 0x1FFFF || magnitude > 0x3FFF) {
			throw new IllegalArgumentException("Too big arg: x=" + x + ", y=" + y + ", z=" + z + ", magnitude=" + magnitude);
		}

		long bitpack = ((long) x) & 0x1FFFFFFFFL;
/*		bitpack |= (((long) y) & 0x1FFFF) << 17;
		bitpack |= (((long) z) & 0x1FFFF) << 34;*/
		bitpack |= (((long) magnitude) & 0x3FFF) << 51;
		return bitpack;
	}

	static int x(final long bitpack) {
		return (int) (bitpack & 0x1FFFFFFFFL);
	}

	static int y(final long bitpack) {
		return 0;//(int) ((bitpack >>> 17) & 0x1FFFF);
	}

	static int z(final long bitpack) {
		return 0;//(int) ((bitpack >>> 34) & 0x1FFFF);
	}

	static int magnitude(final long bitpack) {
		return (int) ((bitpack >>> 51) & 0x3FFF);
	}

	static long bitpackIncrease(final long bitpack,
								final int x, final int y, final int z,
								final int magnitude) {
		return bitpack(x(bitpack) + x,
				y(bitpack) + y,
				z(bitpack) + z,

				magnitude(bitpack) + magnitude);
	}


	static long bitpackIncrease(final long bitpack, final long otherBitpack) {
		return bitpackIncrease(bitpack,
				x(otherBitpack),
				y(otherBitpack),
				z(otherBitpack),
				magnitude(otherBitpack));
	}

}
