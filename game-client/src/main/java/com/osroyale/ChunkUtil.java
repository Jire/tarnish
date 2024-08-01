package com.osroyale;

final class ChunkUtil {

	public static int method155(int plane, int j, int k) {
		plane &= 3;

		if (plane == 0)
			return k;
		if (plane == 1)
			return j;

		if (plane == 2) {
			return 7 - k;
		} else {
			return 7 - j;
		}
	}

	public static int method156(int i, int plane, int l) {
		plane &= 3;

		if (plane == 0)
			return i;
		if (plane == 1)
			return 7 - l;

		if (plane == 2) {
			return 7 - i;
		} else {
			return l;
		}
	}

	public static int method157(int plane, int j, int k, int l, int i1) {
		plane &= 3;

		if (plane == 0)
			return k;
		if (plane == 1)
			return l;

		if (plane == 2) {
			return 7 - k - (i1 - 1);
		} else {
			return 7 - l - (j - 1);
		}
	}

	public static int method158(int j, int k, int plane, int i1, int j1) {
		plane &= 3;

		if (plane == 0)
			return j;
		if (plane == 1)
			return 7 - j1 - (i1 - 1);

		if (plane == 2) {
			return 7 - j - (k - 1);
		} else {
			return j1;
		}
	}
}
