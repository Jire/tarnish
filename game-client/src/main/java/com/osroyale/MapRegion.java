package com.osroyale;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

import java.util.ArrayList;

final class MapRegion {

	private static final int JAGEX_CIRCULAR_ANGLE = 2048;
	private static final double ANGULAR_RATIO = 360D / JAGEX_CIRCULAR_ANGLE;
	private static final double JAGEX_RADIAN = Math.toRadians(ANGULAR_RATIO);


	private static final int[] SIN = new int[JAGEX_CIRCULAR_ANGLE];
	private static final int[] COS = new int[JAGEX_CIRCULAR_ANGLE];

	static {
		for (int i = 0; i < JAGEX_CIRCULAR_ANGLE; i++)
		{
			SIN[i] = (int) (65536.0D * Math.sin((double) i * JAGEX_RADIAN));
			COS[i] = (int) (65536.0D * Math.cos((double) i * JAGEX_RADIAN));
		}
	}

	public MapRegion(byte[][][] terrainAttributes, int[][][] ai) {
		Tiles_minPlane = 99;
		Tiles_renderFlags_countX = 104;
		Tiles_renderFlags_countY = 104;
		Tiles_heights = ai;
		settings = terrainAttributes;
		underlayId = new short[4][Tiles_renderFlags_countX][Tiles_renderFlags_countY];
		Tiles_overlays = new short[4][Tiles_renderFlags_countX][Tiles_renderFlags_countY];
		Tiles_shapes = new byte[4][Tiles_renderFlags_countX][Tiles_renderFlags_countY];
		Tiles_rotations = new byte[4][Tiles_renderFlags_countX][Tiles_renderFlags_countY];
		anIntArrayArrayArray135 = new int[4][Tiles_renderFlags_countX + 1][Tiles_renderFlags_countY + 1];
		Tiles_underlays2 = new byte[4][Tiles_renderFlags_countX + 1][Tiles_renderFlags_countY + 1];
		anIntArrayArray139 = new int[Tiles_renderFlags_countX + 1][Tiles_renderFlags_countY + 1];
		anIntArray124 = new int[Tiles_renderFlags_countY];
		anIntArray125 = new int[Tiles_renderFlags_countY];
		anIntArray126 = new int[Tiles_renderFlags_countY];
		anIntArray127 = new int[Tiles_renderFlags_countY];
		anIntArray128 = new int[Tiles_renderFlags_countY];
	}

	private static int method170(int i, int j) {
		int k = i + j * 57;
		k = k << 13 ^ k;
		int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
		return l >> 19 & 0xff;
	}

	public void method171(CollisionMap[] collisions, SceneGraph region) {
		for (int height = 0; height < 4; height++) {
			for (int mapX = 0; mapX < 104; mapX++) {
				for (int mapY = 0; mapY < 104; mapY++) {

					if ((settings[height][mapX][mapY] & 1) == 1) {
						int plane = height;

						if ((settings[1][mapX][mapY] & 2) == 2) {
							plane--;
						}

						if (plane >= 0) {
							collisions[plane].block(mapX, mapY);
						}
					}

				}
			}
		}

		for (int z = 0; z < 4; z++) {
			byte[][] shadowMap = this.Tiles_underlays2[z];
			int lightX = -50;
			int lightY = -10;
			int lightZ = -50;
			int lightLength = (int) Math.sqrt((lightX * lightX + lightY * lightY + lightZ * lightZ));
			int distribution = 768 * lightLength >> 8;

			for (int tileY = 1; tileY < Tiles_renderFlags_countY - 1; tileY++) {
				for (int tileX = 1; tileX < Tiles_renderFlags_countX - 1; tileX++) {
					int x = Tiles_heights[z][tileX + 1][tileY] - Tiles_heights[z][tileX - 1][tileY];
					int y = Tiles_heights[z][tileX][tileY + 1] - Tiles_heights[z][tileX][tileY - 1];
					int length = (int) Math.sqrt((x * x) + (256 * 256) + (y * y));

					int normalX = (x << 8) / length;
					int normalY = (256 << 8) / length;
					int normalZ = (y << 8) / length;

					int intensity = 96 + ((lightX * normalX) + (lightY * normalY) + (lightZ * normalZ)) / distribution;
					int subtraction =
							(shadowMap[tileX - 1][tileY] >> 2) + (shadowMap[tileX + 1][tileY] >> 3) + (shadowMap[tileX][tileY - 1] >> 2) + (
									shadowMap[tileX][tileY + 1] >> 3) + (shadowMap[tileX][tileY] >> 1);
					anIntArrayArray139[tileX][tileY] = intensity - subtraction;
				}
			}

			for (int k5 = 0; k5 < Tiles_renderFlags_countY; k5++) {
				anIntArray124[k5] = 0;
				anIntArray125[k5] = 0;
				anIntArray126[k5] = 0;
				anIntArray127[k5] = 0;
				anIntArray128[k5] = 0;
			}

			for (int l6 = -5; l6 < Tiles_renderFlags_countX + 5; l6++) {
				for (int i8 = 0; i8 < Tiles_renderFlags_countY; i8++) {
					int k9 = l6 + 5;
					if (k9 >= 0 && k9 < Tiles_renderFlags_countX) {
						int l12 = underlayId[z][k9][i8] & 0xFFFF;
						if (l12 > 0) {
							FloorDefinition flo = FloorDefinition.underlays[l12 - 1];
							anIntArray124[i8] += flo.blendHue;
							anIntArray125[i8] += flo.saturation;
							anIntArray126[i8] += flo.luminance;
							anIntArray127[i8] += flo.blendHueMultiplier;
							anIntArray128[i8]++;
						}
					}
					int i13 = l6 - 5;
					if (i13 >= 0 && i13 < Tiles_renderFlags_countX) {
						int i14 = underlayId[z][i13][i8] & 0xFFFF;
						if (i14 > 0) {
							FloorDefinition flo_1 = FloorDefinition.underlays[i14 - 1];
							anIntArray124[i8] -= flo_1.blendHue;
							anIntArray125[i8] -= flo_1.saturation;
							anIntArray126[i8] -= flo_1.luminance;
							anIntArray127[i8] -= flo_1.blendHueMultiplier;
							anIntArray128[i8]--;
						}
					}
				}

				if (l6 >= 1 && l6 < Tiles_renderFlags_countX - 1) {
					int l9 = 0;
					int j13 = 0;
					int j14 = 0;
					int k15 = 0;
					int k16 = 0;
					for (int k17 = -5; k17 < Tiles_renderFlags_countY + 5; k17++) {
						int j18 = k17 + 5;
						if (j18 >= 0 && j18 < Tiles_renderFlags_countY) {
							l9 += anIntArray124[j18];
							j13 += anIntArray125[j18];
							j14 += anIntArray126[j18];
							k15 += anIntArray127[j18];
							k16 += anIntArray128[j18];
						}
						int k18 = k17 - 5;
						if (k18 >= 0 && k18 < Tiles_renderFlags_countY) {
							l9 -= anIntArray124[k18];
							j13 -= anIntArray125[k18];
							j14 -= anIntArray126[k18];
							k15 -= anIntArray127[k18];
							k16 -= anIntArray128[k18];
						}
						if (k17 >= 1 && k17 < Tiles_renderFlags_countY - 1 && (!lowMem || (settings[0][l6][k17] & 2) != 0 || (settings[z][l6][k17] & 0x10) == 0 && method182(k17, z, l6) == anInt131)) {
							if (z < Tiles_minPlane)
								Tiles_minPlane = z;
							int l18 = underlayId[z][l6][k17] & 0xFFFF;
							int i19 = Tiles_overlays[z][l6][k17] & 0xFFFF;
							if (l18 > 0 || i19 > 0) {
								int j19 = Tiles_heights[z][l6][k17];
								int k19 = Tiles_heights[z][l6 + 1][k17];
								int l19 = Tiles_heights[z][l6 + 1][k17 + 1];
								int i20 = Tiles_heights[z][l6][k17 + 1];
								int j20 = anIntArrayArray139[l6][k17];
								int k20 = anIntArrayArray139[l6 + 1][k17];
								int l20 = anIntArrayArray139[l6 + 1][k17 + 1];
								int i21 = anIntArrayArray139[l6][k17 + 1];
								int j21 = -1;
								int k21 = -1;
								if (l18 > 0) {
									int l21 = (l9 * 256) / k15;
									int j22 = j13 / k16;
									int l22 = j14 / k16;
									j21 = method177(l21, j22, l22);
									k21 = method177(l21, j22, l22);
								}
								if (z > 0) {
									boolean flag = l18 != 0 || Tiles_shapes[z][l6][k17] == 0;
									if (i19 > 0 && !FloorDefinition.overlays[i19 - 1].hideUnderlay)
										flag = false;
									if (flag && j19 == k19 && j19 == l19 && j19 == i20)
										anIntArrayArrayArray135[z][l6][k17] |= 0x924;
								}
								int i22 = 0;
								if (j21 != -1)
									i22 = Rasterizer3D.hslToRgb[method187(k21, 96)];
								if (i19 == 0) {
									region.addTile(z, l6, k17, 0, 0, -1, j19, k19, l19, i20, method187(j21, j20), method187(j21, k20), method187(j21, l20), method187(j21, i21), 0, 0, 0, 0, i22, 0);
								} else {
									int k22 = Tiles_shapes[z][l6][k17] + 1;
									byte byte4 = Tiles_rotations[z][l6][k17];
									FloorDefinition overlay_flo = FloorDefinition.overlays[i19 - 1];
									int texture = overlay_flo.texture;
									int encodedTile;
									int minimapColor;
									int encodedMinimap;
									int lightness;
									if (texture >= 0) {
										minimapColor = Rasterizer3D.textureLoader.getAverageTextureRGB(texture);
										encodedTile = -1;
									} else if (overlay_flo.rgb == 16711935) {
										encodedTile = -2;
										texture = -1;
										minimapColor = -2;
									} else {
										encodedTile = encode(overlay_flo.hue, overlay_flo.saturation, overlay_flo.lightness);
										encodedMinimap = overlay_flo.hue;
										lightness = overlay_flo.lightness;
										if (lightness < 0) {
											lightness = 0;
										} else if (lightness > 255) {
											lightness = 255;
										}
										minimapColor = encode(encodedMinimap, overlay_flo.saturation, lightness);
									}
									encodedMinimap = 0;
									if (minimapColor != -2) {
										encodedMinimap = Rasterizer3D.hslToRgb[checkedLight(minimapColor, 96)];
									}
									if (overlay_flo.secondaryRgb != -1) {
										lightness = overlay_flo.secondaryHue;
										int secondaryLightness = overlay_flo.secondaryLightness;
										if (secondaryLightness < 0) {
											secondaryLightness = 0;
										} else if (secondaryLightness > 255) {
											secondaryLightness = 255;
										}
										minimapColor = encode(lightness, overlay_flo.secondarySaturation, secondaryLightness);
										encodedMinimap = Rasterizer3D.hslToRgb[checkedLight(minimapColor, 96)];
									}

									region.addTile(z, l6, k17, k22, byte4, texture, j19, k19, l19, i20, method187(j21, j20), method187(j21, k20), method187(j21, l20), method187(j21, i21), checkedLight(encodedTile, j20), checkedLight(encodedTile, k20), checkedLight(encodedTile, l20), checkedLight(encodedTile, i21), i22, encodedMinimap);

								}
							}
						}
					}
				}
			}

			for (int y = 1; y < Tiles_renderFlags_countY - 1; y++) {
				for (int x = 1; x < Tiles_renderFlags_countX - 1; x++) {
					region.method278(z, x, y, method182(y, z, x));
				}
			}

		}

		region.method305(-10, -50, -50);
		for (int j1 = 0; j1 < Tiles_renderFlags_countX; j1++) {
			for (int l1 = 0; l1 < Tiles_renderFlags_countY; l1++)
				if ((settings[1][j1][l1] & 2) == 2)
					region.method276(l1, j1);

		}

		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for (int l2 = 0; l2 < 4; l2++) {
			if (l2 > 0) {
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for (int i3 = 0; i3 <= l2; i3++) {
				for (int k3 = 0; k3 <= Tiles_renderFlags_countY; k3++) {
					for (int i4 = 0; i4 <= Tiles_renderFlags_countX; i4++) {
						if ((anIntArrayArrayArray135[i3][i4][k3] & i2) != 0) {
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for (; k4 > 0 && (anIntArrayArrayArray135[i3][i4][k4 - 1] & i2) != 0; k4--)
								;
							for (; l5 < Tiles_renderFlags_countY && (anIntArrayArrayArray135[i3][i4][l5 + 1] & i2) != 0; l5++)
								;
							label0:
							for (; i7 > 0; i7--) {
								for (int j10 = k4; j10 <= l5; j10++)
									if ((anIntArrayArrayArray135[i7 - 1][i4][j10] & i2) == 0)
										break label0;

							}

							label1:
							for (; k8 < l2; k8++) {
								for (int k10 = k4; k10 <= l5; k10++)
									if ((anIntArrayArrayArray135[k8 + 1][i4][k10] & i2) == 0)
										break label1;

							}

							int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
							if (l10 >= 8) {
								char c1 = '\360';
								int k14 = Tiles_heights[k8][i4][k4] - c1;
								int l15 = Tiles_heights[i7][i4][k4];
								SceneGraph.method277(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14, k4 * 128, 1);
								for (int l16 = i7; l16 <= k8; l16++) {
									for (int l17 = k4; l17 <= l5; l17++)
										anIntArrayArrayArray135[l16][i4][l17] &= ~i2;

								}

							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & j2) != 0) {
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for (; l4 > 0 && (anIntArrayArrayArray135[i3][l4 - 1][k3] & j2) != 0; l4--)
								;
							for (; i6 < Tiles_renderFlags_countX && (anIntArrayArrayArray135[i3][i6 + 1][k3] & j2) != 0; i6++)
								;
							label2:
							for (; j7 > 0; j7--) {
								for (int i11 = l4; i11 <= i6; i11++)
									if ((anIntArrayArrayArray135[j7 - 1][i11][k3] & j2) == 0)
										break label2;

							}

							label3:
							for (; l8 < l2; l8++) {
								for (int j11 = l4; j11 <= i6; j11++)
									if ((anIntArrayArrayArray135[l8 + 1][j11][k3] & j2) == 0)
										break label3;

							}

							int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
							if (k11 >= 8) {
								char c2 = '\360';
								int l14 = Tiles_heights[l8][l4][k3] - c2;
								int i16 = Tiles_heights[j7][l4][k3];
								SceneGraph.method277(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14, k3 * 128, 2);
								for (int i17 = j7; i17 <= l8; i17++) {
									for (int i18 = l4; i18 <= i6; i18++)
										anIntArrayArrayArray135[i17][i18][k3] &= ~j2;

								}

							}
						}
						if ((anIntArrayArrayArray135[i3][i4][k3] & k2) != 0) {
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for (; k7 > 0 && (anIntArrayArrayArray135[i3][i4][k7 - 1] & k2) != 0; k7--)
								;
							for (; i9 < Tiles_renderFlags_countY && (anIntArrayArrayArray135[i3][i4][i9 + 1] & k2) != 0; i9++)
								;
							label4:
							for (; i5 > 0; i5--) {
								for (int l11 = k7; l11 <= i9; l11++)
									if ((anIntArrayArrayArray135[i3][i5 - 1][l11] & k2) == 0)
										break label4;

							}

							label5:
							for (; j6 < Tiles_renderFlags_countX; j6++) {
								for (int i12 = k7; i12 <= i9; i12++)
									if ((anIntArrayArrayArray135[i3][j6 + 1][i12] & k2) == 0)
										break label5;

							}

							if (((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4) {
								int j12 = Tiles_heights[i3][i5][k7];
								SceneGraph.method277(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128, j12, k7 * 128, 4);
								for (int k13 = i5; k13 <= j6; k13++) {
									for (int i15 = k7; i15 <= i9; i15++)
										anIntArrayArrayArray135[i3][k13][i15] &= ~k2;

								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Encodes the hue, saturation, and luminance into a colour value.
	 *
	 * @param hue The hue.
	 * @param saturation The saturation.
	 * @param luminance The luminance.
	 * @return The colour.
	 */
	private int encode(int hue, int saturation, int luminance) {
		if (luminance > 179)
			saturation /= 2;
		if (luminance > 192)
			saturation /= 2;
		if (luminance > 217)
			saturation /= 2;
		if (luminance > 243)
			saturation /= 2;
		return (hue / 4 << 10) + (saturation / 32 << 7) + luminance / 2;
	}

	public static int noise(int x, int y)
	{
		int n = x + y * 57;
		n ^= n << 13;
		return ((n * (n * n * 15731 + 789221) + 1376312589) & Integer.MAX_VALUE) >> 19 & 255;
	}

	public static int smoothedNoise1(int x, int y)
	{
		int corners = noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, 1 + y) + noise(x + 1, y + 1);
		int sides = noise(x - 1, y) + noise(1 + x, y) + noise(x, y - 1) + noise(x, 1 + y);
		int center = noise(x, y);
		return center / 4 + sides / 8 + corners / 16;
	}

	public static int interpolate(int a, int b, int x, int y)
	{
		int f = 65536 - COS[1024 * x / y] >> 1;
		return (f * b >> 16) + (a * (65536 - f) >> 16);
	}

	public static int interpolateNoise(int x, int y, int frequency)
	{
		int intX = x / frequency;
		int fracX = x & frequency - 1;
		int intY = y / frequency;
		int fracY = y & frequency - 1;
		int v1 = smoothedNoise1(intX, intY);
		int v2 = smoothedNoise1(intX + 1, intY);
		int v3 = smoothedNoise1(intX, intY + 1);
		int v4 = smoothedNoise1(1 + intX, 1 + intY);
		int i1 = interpolate(v1, v2, fracX, frequency);
		int i2 = interpolate(v3, v4, fracX, frequency);
		return interpolate(i1, i2, fracY, frequency);
	}

	public static int calculate(int x, int y)
	{
		int n = interpolateNoise(x + 45365, y + 91923, 4) - 128
				+ (interpolateNoise(10294 + x, y + 37821, 2) - 128 >> 1)
				+ (interpolateNoise(x, y, 1) - 128 >> 2);
		n = 35 + (int) ((double) n * 0.3D);
		if (n < 10)
		{
			n = 10;
		}
		else if (n > 60)
		{
			n = 60;
		}

		return n;
	}

	public static void method173(Buffer stream, OnDemandFetcher class42_sub1) {
		label0:
		{
			int i = -1;
			do {
				int j = stream.readUSmart2();
				if (j == 0)
					break label0;
				i += j;
				ObjectDefinition class46 = ObjectDefinition.forID(i);
				class46.method574(class42_sub1);
				do {
					int k = stream.getUIncrementalSmart();
					if (k == 0)
						break;
					stream.readUnsignedByte();
				} while (true);
			} while (true);
		}
	}

	public void method174(int i, int j, int l, int i1) {
		for (int j1 = i; j1 <= i + j; j1++) {
			for (int k1 = i1; k1 <= i1 + l; k1++)
				if (k1 >= 0 && k1 < Tiles_renderFlags_countX && j1 >= 0 && j1 < Tiles_renderFlags_countY) {
					Tiles_underlays2[0][k1][j1] = 127;
					if (k1 == i1 && k1 > 0)
						Tiles_heights[0][k1][j1] = Tiles_heights[0][k1 - 1][j1];
					if (k1 == i1 + l && k1 < Tiles_renderFlags_countX - 1)
						Tiles_heights[0][k1][j1] = Tiles_heights[0][k1 + 1][j1];
					if (j1 == i && j1 > 0)
						Tiles_heights[0][k1][j1] = Tiles_heights[0][k1][j1 - 1];
					if (j1 == i + j && j1 < Tiles_renderFlags_countY - 1)
						Tiles_heights[0][k1][j1] = Tiles_heights[0][k1][j1 + 1];
				}

		}
	}

	private void addLocation(int y, SceneGraph region, CollisionMap collisionMap, int type, int z, int x, int objectId, int rotation) {
		if (lowMem && (settings[0][x][y] & 2) == 0) {
			if ((settings[z][x][y] & 0x10) != 0) {
				return;
			}

			if (method182(y, z, x) != anInt131) {
				return;
			}
		}

		if (z < Tiles_minPlane) {
			Tiles_minPlane = z;
		}

		ObjectDefinition objectDefinition = ObjectDefinition.forID(objectId);

		int sizeY;
		int sizeX;
		if (rotation != 1 && rotation != 3) {
			sizeX = objectDefinition.width;
			sizeY = objectDefinition.length;
		} else {
			sizeX = objectDefinition.length;
			sizeY = objectDefinition.width;
		}

		int modX;
		int modX1;
		if (x + sizeX <= 104) {
			modX = x + (sizeX >> 1);
			modX1 = x + (1 + sizeX >> 1);
		} else {
			modX = x;
			modX1 = 1 + x;
		}

		int modY;
		int modY1;
		if (sizeY + y <= 104) {
			modY = (sizeY >> 1) + y;
			modY1 = y + (1 + sizeY >> 1);
		} else {
			modY = y;
			modY1 = 1 + y;
		}

		int[][] tileHeights = this.Tiles_heights[z];
		int mean = tileHeights[modX1][modY] + tileHeights[modX][modY] + tileHeights[modX][modY1] + tileHeights[modX1][modY1] >> 2;
		int localX = (x << 7) + (sizeY << 6);
		int localY = (y << 7) + (sizeX << 6);

		long key = (long) rotation << 20 | (long) type << 14 | ((long) y << 7 | x) + 0x40000000;
		if (!objectDefinition.interactive) {
			key |= ~0x7fffffffffffffffL;
		}
		if(objectDefinition.mergeInteractState == 1) {
			key |= 0x400000L;
		}
		key |= (long) objectId << 32;

		if (type == 22) {
			if (!Settings.GROUND_DECORATIONS && !objectDefinition.interactive && !objectDefinition.obstructsGround) {
				return;
			}

			Renderable obj;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null) {
				obj = objectDefinition.getModelSharelight(22, rotation, tileHeights, mean, localX, localY);
			} else {
				obj = new SceneObject(objectId, rotation, 22, objectDefinition.animationId, true, x, y, z);
			}

			region.method280(z, mean, y, obj, key, x);

			if (objectDefinition.solid && objectDefinition.interactive && collisionMap != null) {
				collisionMap.block(x, y);
			}
			return;
		}

		if (type == 10 || type == 11) {
			Object obj1;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj1 = objectDefinition.getModelSharelight(10, rotation, tileHeights, mean, localX, localY);
			else
				obj1 = new SceneObject(objectId, rotation, 10, objectDefinition.animationId, true, x, y, z);
			if (obj1 != null) {
				int i5 = 0;
				if (type == 11)
					i5 += 256;
				int width;
				int length;
				if (rotation == 1 || rotation == 3) {
					width = objectDefinition.length;
					length = objectDefinition.width;
				} else {
					width = objectDefinition.width;
					length = objectDefinition.length;
				}

				if (region.method284(key, mean, length, ((Renderable) (obj1)), width, z, i5, y, x) && objectDefinition.clipped) {
					int l5 = 15;
					if (obj1 instanceof Model) {
						l5 = ((Model) obj1).getLowestX() / 4;
						if (l5 > 30) {
							l5 = 30;
						}
					}
					for (int j5 = 0; j5 <= width; j5++) {
						for (int k5 = 0; k5 <= length; k5++) {
							if (l5 > Tiles_underlays2[z][x + j5][y + k5]) {
								Tiles_underlays2[z][x + j5][y + k5] = (byte) l5;
							}
						}
					}
				}
			}
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clipObject(x, y, objectDefinition.width, objectDefinition.length, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type >= 12) {
			Object obj2;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj2 = objectDefinition.getModelSharelight(type, rotation, tileHeights, mean, localX, localY);
			else
				obj2 = new SceneObject(objectId, rotation, type, objectDefinition.animationId, true, x, y, z);
			region.method284(key, mean, 1, ((Renderable) (obj2)), 1, z, 0, y, x);
			if (type >= 12 && type <= 17 && type != 13 && z > 0)
				anIntArrayArrayArray135[z][x][y] |= 0x924;
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clipObject(x, y, objectDefinition.width, objectDefinition.length, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 0) {
			Object obj3;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj3 = objectDefinition.getModelSharelight(0, rotation, tileHeights, mean, localX, localY);
			else
				obj3 = new SceneObject(objectId, rotation, 0, objectDefinition.animationId, true, x, y, z);
			region.method282(anIntArray152[rotation], ((Renderable) (obj3)), key, y, x, null, mean, 0, z);
			if (rotation == 0) {
				if (objectDefinition.clipped) {
					Tiles_underlays2[z][x][y] = 50;
					Tiles_underlays2[z][x][y + 1] = 50;
				}
				if (objectDefinition.modelClipped)
					anIntArrayArrayArray135[z][x][y] |= 0x249;
			} else if (rotation == 1) {
				if (objectDefinition.clipped) {
					Tiles_underlays2[z][x][y + 1] = 50;
					Tiles_underlays2[z][x + 1][y + 1] = 50;
				}
				if (objectDefinition.modelClipped)
					anIntArrayArrayArray135[z][x][y + 1] |= 0x492;
			} else if (rotation == 2) {
				if (objectDefinition.clipped) {
					Tiles_underlays2[z][x + 1][y] = 50;
					Tiles_underlays2[z][x + 1][y + 1] = 50;
				}
				if (objectDefinition.modelClipped)
					anIntArrayArrayArray135[z][x + 1][y] |= 0x249;
			} else if (rotation == 3) {
				if (objectDefinition.clipped) {
					Tiles_underlays2[z][x][y] = 50;
					Tiles_underlays2[z][x + 1][y] = 50;
				}
				if (objectDefinition.modelClipped)
					anIntArrayArrayArray135[z][x][y] |= 0x492;
			}
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clip(x, y, type, rotation, objectDefinition.impenetrable);
			if (objectDefinition.decorOffset != 16)
				region.method290(y, objectDefinition.decorOffset, x, z);
			return;
		}
		if (type == 1) {
			Object obj4;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj4 = objectDefinition.getModelSharelight(1, rotation, tileHeights, mean, localX, localY);
			else
				obj4 = new SceneObject(objectId, rotation, 1, objectDefinition.animationId, true, x, y, z);
			region.method282(anIntArray140[rotation], ((Renderable) (obj4)), key, y, x, null, mean, 0, z);
			if (objectDefinition.clipped)
				if (rotation == 0)
					Tiles_underlays2[z][x][y + 1] = 50;
				else if (rotation == 1)
					Tiles_underlays2[z][x + 1][y + 1] = 50;
				else if (rotation == 2)
					Tiles_underlays2[z][x + 1][y] = 50;
				else if (rotation == 3)
					Tiles_underlays2[z][x][y] = 50;
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clip(x, y, type, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 2) {
			int i3 = rotation + 1 & 3;
			Object obj11;
			Object obj12;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null) {
				obj11 = objectDefinition.getModelSharelight(2, 4 + rotation, tileHeights, mean, localX, localY);
				obj12 = objectDefinition.getModelSharelight(2, i3, tileHeights, mean, localX, localY);
			} else {
				obj11 = new SceneObject(objectId, 4 + rotation, 2, objectDefinition.animationId, true, x, y, z);
				obj12 = new SceneObject(objectId, i3, 2, objectDefinition.animationId, true, x, y, z);
			}
			region.method282(anIntArray152[rotation], ((Renderable) (obj11)), key, y, x, ((Renderable) (obj12)), mean, anIntArray152[i3], z);
			if (objectDefinition.modelClipped)
				if (rotation == 0) {
					anIntArrayArrayArray135[z][x][y] |= 0x249;
					anIntArrayArrayArray135[z][x][y + 1] |= 0x492;
				} else if (rotation == 1) {
					anIntArrayArrayArray135[z][x][y + 1] |= 0x492;
					anIntArrayArrayArray135[z][x + 1][y] |= 0x249;
				} else if (rotation == 2) {
					anIntArrayArrayArray135[z][x + 1][y] |= 0x249;
					anIntArrayArrayArray135[z][x][y] |= 0x492;
				} else if (rotation == 3) {
					anIntArrayArrayArray135[z][x][y] |= 0x492;
					anIntArrayArrayArray135[z][x][y] |= 0x249;
				}
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clip(x, y, type, rotation, objectDefinition.impenetrable);
			if (objectDefinition.decorOffset != 16)
				region.method290(y, objectDefinition.decorOffset, x, z);
			return;
		}
		if (type == 3) {
			Object obj5;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj5 = objectDefinition.getModelSharelight(3, rotation, tileHeights, mean, localX, localY);
			else
				obj5 = new SceneObject(objectId, rotation, 3, objectDefinition.animationId, true, x, y, z);
			region.method282(anIntArray140[rotation], ((Renderable) (obj5)), key, y, x, null, mean, 0, z);
			if (objectDefinition.clipped)
				if (rotation == 0)
					Tiles_underlays2[z][x][y + 1] = 50;
				else if (rotation == 1)
					Tiles_underlays2[z][x + 1][y + 1] = 50;
				else if (rotation == 2)
					Tiles_underlays2[z][x + 1][y] = 50;
				else if (rotation == 3)
					Tiles_underlays2[z][x][y] = 50;
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clip(x, y, type, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 9) {
			Object obj6;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj6 = objectDefinition.getModelSharelight(type, rotation, tileHeights, mean, localX, localY);
			else
				obj6 = new SceneObject(objectId, rotation, type, objectDefinition.animationId, true, x, y, z);
			region.method284(key, mean, 1, ((Renderable) (obj6)), 1, z, 0, y, x);
			if (objectDefinition.solid && collisionMap != null)
				collisionMap.clipObject(x, y, objectDefinition.width, objectDefinition.length, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 4) {
			Object obj7;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj7 = objectDefinition.getModelSharelight(4, 0, tileHeights, mean, localX, localY);
			else
				obj7 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, z);
			region.method283(key, y, rotation * 512, z, 0, mean, ((Renderable) (obj7)), x, 0, anIntArray152[rotation]);
			return;
		}
		if (type == 5) {
			int i4 = 16;
			long k4 = region.method300(z, x, y);
			if (k4 > 0L)
				i4 = ObjectDefinition.forID((int) (k4 >>> 32) & 0x7fffffff).decorOffset;
			Object obj13;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj13 = objectDefinition.getModelSharelight(4, 0, tileHeights, mean, localX, localY);
			else
				obj13 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, z);
			region.method283(key, y, rotation * 512, z, anIntArray137[rotation] * i4, mean, ((Renderable) (obj13)), x, anIntArray144[rotation] * i4, anIntArray152[rotation]);
			return;
		}
		if (type == 6) {
			Object obj8;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj8 = objectDefinition.getModelSharelight(4, 0, tileHeights, mean, localX, localY);
			else
				obj8 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, z);
			region.method283(key, y, rotation, z, 0, mean, ((Renderable) (obj8)), x, 0, 256);
			return;
		}
		if (type == 7) {
			Object obj9;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj9 = objectDefinition.getModelSharelight(4, 0, tileHeights, mean, localX, localY);
			else
				obj9 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, z);
			region.method283(key, y, rotation, z, 0, mean, ((Renderable) (obj9)), x, 0, 512);
			return;
		}
		if (type == 8) {
			Object obj10;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj10 = objectDefinition.getModelSharelight(4, 0, tileHeights, mean, localX, localY);
			else
				obj10 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, z);
			region.method283(key, y, rotation, z, 0, mean, ((Renderable) (obj10)), x, 0, 768);
		}
	}

	private static int method176(int i, int j, int k) {
		int l = i / k;
		int i1 = i & k - 1;
		int j1 = j / k;
		int k1 = j & k - 1;
		int l1 = method186(l, j1);
		int i2 = method186(l + 1, j1);
		int j2 = method186(l, j1 + 1);
		int k2 = method186(l + 1, j1 + 1);
		int l2 = method184(l1, i2, i1, k);
		int i3 = method184(j2, k2, i1, k);
		return method184(l2, i3, k1, k);
	}

	private int method177(int i, int j, int k) {
		if (k > 179)
			j /= 2;
		if (k > 192)
			j /= 2;
		if (k > 217)
			j /= 2;
		if (k > 243)
			j /= 2;
		return (i / 4 << 10) + (j / 32 << 7) + k / 2;
	}

	public static boolean method178(int i, int j) {
		ObjectDefinition class46 = ObjectDefinition.forID(i);
		if (j == 11)
			j = 10;
		if (j >= 5 && j <= 8)
			j = 4;
		return class46.method577(j);
	}

	public void method179(int i, int height, CollisionMap[] collisionMap, int x, int i1, byte[] data, int j1, int collisionMapIndex, int y) {
		for (int localX = 0; localX < 8; localX++) {
			for (int localY = 0; localY < 8; localY++) {

				if (x + localX > 0 && x + localX < 103 && y + localY > 0 && y + localY < 103) {
					collisionMap[collisionMapIndex].clipData[x + localX][y + localY] &= 0xfeffffff;
				}

			}
		}

		Buffer stream = new Buffer(data);

		for (int plane = 0; plane < 4; plane++) {
			for (int yy = 0; yy < 64; yy++) {
				for (int xx = 0; xx < 64; xx++) {

					if (plane == i && yy >= i1 && yy < i1 + 8 && xx >= j1 && xx < j1 + 8) {
						loadTerrain(y + ChunkUtil.method156(xx & 7, height, yy & 7), 0, stream, x + ChunkUtil.method155(height, xx & 7, yy & 7), collisionMapIndex, height, 0);
					} else {
						loadTerrain(-1, 0, stream, -1, 0, 0, 0);
					}

				}
			}
		}

	}

	public void loadTerrain(byte[] data, int regionY, int regionX, int regionChunkX, int regionChunkY, CollisionMap[] aclass11) {
		for (int i1 = 0; i1 < 4; i1++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++)
					if (regionX + x > 0 && regionX + x < 103 && regionY + y > 0 && regionY + y < 103)
						aclass11[i1].clipData[regionX + x][regionY + y] &= 0xfeffffff;

			}

		}

		Buffer stream = new Buffer(data);
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 64; x++) {
				for (int y = 0; y < 64; y++)
					loadTerrain(y + regionY, regionChunkY, stream, x + regionX, plane, 0, regionChunkX);

			}

		}
	}

	public static boolean isValidMap(int z, int x, int y) {
		return z >= 0 && z < 4 && x >= 0 && x < 104 && y >= 0 && y < 104;
	}

	private void loadTerrain(int y, int regionChunkY, Buffer stream, int x, int z, int i1, int regionChunkX) {
		try {
			if (isValidMap(z, x, y)) {
				settings[z][x][y] = 0;
				while (true) {
					int attribute = stream.readUnsignedShort();
					if (attribute == 0) {
						if (z == 0) {
							Tiles_heights[0][x][y] = -calculate(0xe3b7b + x + regionChunkX, 0x87cce + y + regionChunkY) * 8;
						} else {
							Tiles_heights[z][x][y] = Tiles_heights[z - 1][x][y] - 240;
						}
						break;
					} else if (attribute == 1) {
						int height = stream.readUnsignedByte();

						if (height == 1) {
							height = 0;
						}

						if (z == 0) {
							Tiles_heights[0][x][y] = -height * 8;
						} else {
							Tiles_heights[z][x][y] = Tiles_heights[z - 1][x][y] - height * 8;
						}
						break;
					} else if (attribute <= 49) {
						Tiles_overlays[z][x][y] = (short) stream.readShortOSRS();
						Tiles_shapes[z][x][y] = (byte) ((attribute - 2) / 4);
						Tiles_rotations[z][x][y] = (byte) (attribute - 2 + i1 & 3);
					} else if (attribute <= 81) {
						settings[z][x][y] = (byte) (attribute - 49);
					} else {
						underlayId[z][x][y] = (short) (attribute - 81);
					}
				}
			} else {
				while (true) {
					int attribute = stream.readUnsignedShort();
					if (attribute == 0)
						break;
					if (attribute == 1) {
						stream.readUnsignedByte();
						break;
					}
					if (attribute <= 49) {
						stream.readShort();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int method182(int y, int plane, int x) {
		if ((settings[plane][x][y] & 8) != 0) {
			return 0;
		}

		if (plane > 0 && (settings[1][x][y] & 2) != 0) {
			return plane - 1;
		}

		return plane;
	}

	public void loadLocations(CollisionMap[] collisions, SceneGraph worldController, int mapZ, int mapX, int baseY, int plane, byte[] data, int baseX, int xPlane, int yPlane) {
		Buffer buf = new Buffer(data);

		int id = -1;
		int idOffset;

		while ((idOffset = buf.readUnsignedIntSmartShortCompat()) != 0) {
			id += idOffset;

			int position = 0;
			int positionOffset;

			while ((positionOffset = buf.readUnsignedShortSmart()) != 0) {
				position += positionOffset - 1;

				int localY = position & 0x3f;
				int localX = position >> 6 & 0x3f;
				int height = position >> 12;

				int attributes = buf.readUnsignedByte();
				int type = attributes >> 2;
				int orientation = attributes & 3;
				if (height == mapZ && localX >= baseX && localX < baseX + 8 && localY >= baseY && localY < baseY + 8) {
					ObjectDefinition def = ObjectDefinition.forID(id);
					int x = mapX + ChunkUtil.method157(xPlane, def.length, localX & 7, localY & 7, def.width);
					int y = yPlane + ChunkUtil.method158(localY & 7, def.length, xPlane, def.width, localX & 7);
					if (x > 0 && y > 0 && x < 103 && y < 103) {
						int collisionsHeight = height;
						if ((settings[1][x][y] & 2) == 2) {
							collisionsHeight--;
						}
						CollisionMap locationCollisions =
								collisionsHeight >= 0
										? collisions[collisionsHeight]
										: null;
						addLocation(y, worldController, locationCollisions, type, height/*targetHeight*/, id, id, orientation + xPlane & 3);
					}
				}
			}
		}
	}

	private static int method184(int i, int j, int k, int l) {
		int i1 = 0x10000 - Rasterizer3D.COSINE[(k * 1024) / l] >> 1;
		return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
	}

	private int checkedLight(int i, int j) {
		if (i == -2) {
			return 0xbc614e;
		}
		if (i == -1) {
			if (j < 0) {
				j = 0;
			} else if (j > 127) {
				j = 127;
			}
			j = 127 - j;
			return j;
		}
		j = (j * (i & 0x7f)) / 128;
		if (j < 2) {
			j = 2;
		} else if (j > 126) {
			j = 126;
		}
		return (i & 0xff80) + j;
	}

	private static int method186(int i, int j) {
		int k = method170(i - 1, j - 1) + method170(i + 1, j - 1) + method170(i - 1, j + 1) + method170(i + 1, j + 1);
		int l = method170(i - 1, j) + method170(i + 1, j) + method170(i, j - 1) + method170(i, j + 1);
		int i1 = method170(i, j);
		return k / 16 + l / 8 + i1 / 4;
	}

	private static int method187(int i, int j) {
		if (i == -1)
			return 0xbc614e;
		j = (j * (i & 0x7f)) / 128;
		if (j < 2)
			j = 2;
		else if (j > 126)
			j = 126;
		return (i & 0xff80) + j;
	}

	public static void method188(SceneGraph worldController, int rotation, int y, int type, int bridgeZ, CollisionMap matrix, int[][][] heightMap, int x, int objectId, int z) {
		ObjectDefinition objectDefinition = ObjectDefinition.forID(objectId);

		int sizeX;
		int sizeY;
		if (rotation != 1 && rotation != 3) {
			sizeX = objectDefinition.width;
			sizeY = objectDefinition.length;
		} else {
			sizeX = objectDefinition.length;
			sizeY = objectDefinition.width;
		}

		int modX;
		int modX1;
		if (sizeX + x <= 104) {
			modX = (sizeX >> 1) + x;
			modX1 = (sizeX + 1 >> 1) + x;
		} else {
			modX = x;
			modX1 = x + 1;
		}

		int modY;
		int modY1;
		if (sizeY + y <= 104) {
			modY = (sizeY >> 1) + y;
			modY1 = y + (sizeY + 1 >> 1);
		} else {
			modY = y;
			modY1 = y + 1;
		}

		int[][] tileHeights = heightMap[bridgeZ];
		int mean = tileHeights[modX1][modY1] + tileHeights[modX][modY1] + tileHeights[modX][modY] + tileHeights[modX1][modY] >> 2;
		int localX = (x << 7) + (sizeX << 6);
		int localY = (y << 7) + (sizeY << 6);

		long key = (long) rotation << 20 | (long) type << 14 | ((long) y << 7 | x) + 0x40000000;
		if (!objectDefinition.interactive) {
			key |= ~0x7fffffffffffffffL;
		}
		if(objectDefinition.mergeInteractState == 1) {
			key |= 0x400000L;
		}
		key |= (long) objectId << 32;

		if (type == 22) {
			Object obj;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj = objectDefinition.getModel(22, rotation, tileHeights, mean, localX, localY);
			else
				obj = new SceneObject(objectId, rotation, 22, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method280(z, mean, y, ((Renderable) (obj)), key, x);
			if (objectDefinition.solid && objectDefinition.interactive)
				matrix.block(x, y);
			return;
		}
		if (type == 10 || type == 11) {
			Object obj1;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj1 = objectDefinition.getModel(10, rotation, tileHeights, mean, localX, localY);
			else
				obj1 = new SceneObject(objectId, rotation, 10, objectDefinition.animationId, true, x, y, bridgeZ);
			if (obj1 != null) {
				int j5 = 0;
				if (type == 11)
					j5 += 256;
				int k4;
				int i5;
				if (rotation == 1 || rotation == 3) {
					k4 = objectDefinition.length;
					i5 = objectDefinition.width;
				} else {
					k4 = objectDefinition.width;
					i5 = objectDefinition.length;
				}
				worldController.method284(key, mean, i5, ((Renderable) (obj1)), k4, z, j5, y, x);
			}
			if (objectDefinition.solid)
				matrix.clipObject(x, y, objectDefinition.width, objectDefinition.length, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type >= 12) {
			Object obj2;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj2 = objectDefinition.getModel(type, rotation, tileHeights, mean, localX, localY);
			else
				obj2 = new SceneObject(objectId, rotation, type, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method284(key, mean, 1, ((Renderable) (obj2)), 1, z, 0, y, x);
			if (objectDefinition.solid)
				matrix.clipObject(x, y, objectDefinition.width, objectDefinition.length, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 0) {
			Object obj3;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj3 = objectDefinition.getModel(0, rotation, tileHeights, mean, localX, localY);
			else
				obj3 = new SceneObject(objectId, rotation, 0, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method282(anIntArray152[rotation], ((Renderable) (obj3)), key, y, x, null, mean, 0, z);
			if (objectDefinition.solid)
				matrix.clip(x, y, type, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 1) {
			Object obj4;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj4 = objectDefinition.getModel(1, rotation, tileHeights, mean, localX, localY);
			else
				obj4 = new SceneObject(objectId, rotation, 1, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method282(anIntArray140[rotation], ((Renderable) (obj4)), key, y, x, null, mean, 0, z);
			if (objectDefinition.solid)
				matrix.clip(x, y, type, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 2) {
			int j3 = rotation + 1 & 3;
			Object obj11;
			Object obj12;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null) {
				obj11 = objectDefinition.getModel(2, 4 + rotation, tileHeights, mean, localX, localY);
				obj12 = objectDefinition.getModel(2, j3, tileHeights, mean, localX, localY);
			} else {
				obj11 = new SceneObject(objectId, 4 + rotation, 2, objectDefinition.animationId, true, x, y, bridgeZ);
				obj12 = new SceneObject(objectId, j3, 2, objectDefinition.animationId, true, x, y, bridgeZ);
			}
			worldController.method282(anIntArray152[rotation], ((Renderable) (obj11)), key, y, x, ((Renderable) (obj12)), mean, anIntArray152[j3], z);
			if (objectDefinition.solid)
				matrix.clip(x, y, type, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 3) {
			Object obj5;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj5 = objectDefinition.getModel(3, rotation, tileHeights, mean, localX, localY);
			else
				obj5 = new SceneObject(objectId, rotation, 3, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method282(anIntArray140[rotation], ((Renderable) (obj5)), key, y, x, null, mean, 0, z);
			if (objectDefinition.solid)
				matrix.clip(x, y, type, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 9) {
			Object obj6;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj6 = objectDefinition.getModel(type, rotation, tileHeights, mean, localX, localY);
			else
				obj6 = new SceneObject(objectId, rotation, type, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method284(key, mean, 1, ((Renderable) (obj6)), 1, z, 0, y, x);
			if (objectDefinition.solid)
				matrix.clipObject(x, y, objectDefinition.width, objectDefinition.length, rotation, objectDefinition.impenetrable);
			return;
		}
		if (type == 4) {
			Object obj7;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj7 = objectDefinition.getModel(4, 0, tileHeights, mean, localX, localY);
			else
				obj7 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method283(key, y, rotation * 512, z, 0, mean, ((Renderable) (obj7)), x, 0, anIntArray152[rotation]);
			return;
		}
		if (type == 5) {
			int j4 = 16;
			long l4 = worldController.method300(z, x, y);
			if (l4 > 0)
				j4 = ObjectDefinition.forID((int) (l4 >>> 32) & 0x7fffffff).decorOffset;
			Object obj13;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj13 = objectDefinition.getModel(4, 0, tileHeights, mean, localX, localY);
			else
				obj13 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method283(key, y, rotation * 512, z, anIntArray137[rotation] * j4, mean, ((Renderable) (obj13)), x, anIntArray144[rotation] * j4, anIntArray152[rotation]);
			return;
		}
		if (type == 6) {
			Object obj8;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj8 = objectDefinition.getModel(4, 0, tileHeights, mean, localX, localY);
			else
				obj8 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method283(key, y, rotation, z, 0, mean, ((Renderable) (obj8)), x, 0, 256);
			return;
		}
		if (type == 7) {
			Object obj9;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj9 = objectDefinition.getModel(4, 0, tileHeights, mean, localX, localY);
			else
				obj9 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method283(key, y, rotation, z, 0, mean, ((Renderable) (obj9)), x, 0, 512);
			return;
		}
		if (type == 8) {
			Object obj10;
			if (objectDefinition.animationId == -1 && objectDefinition.childrenIDs == null)
				obj10 = objectDefinition.getModel(4, 0, tileHeights, mean, localX, localY);
			else
				obj10 = new SceneObject(objectId, 0, 4, objectDefinition.animationId, true, x, y, bridgeZ);
			worldController.method283(key, y, rotation, z, 0, mean, ((Renderable) (obj10)), x, 0, 768);
		}
	}

	public static boolean method189(int x, byte[] is, int y) {
		boolean bool = true;
		Buffer stream = new Buffer(is);
		int objId = -1;
		for (; ; ) {
			int objIdOffset = stream.readUnsignedIntSmartShortCompat();
			if (objIdOffset == 0)
				break;
			objId += objIdOffset;
			int objPosInfo = 0;
			boolean bool_255_ = false;
			for (; ; ) {
				if (bool_255_) {
					int objPosInfoOffset = stream.readUnsignedShortSmart();
					if (objPosInfoOffset == 0) {
						break;
					}
					stream.readUnsignedByte();
				} else {
					int objPosInfoOffset = stream.readUnsignedShortSmart();
					if (objPosInfoOffset == 0) {
						break;
					}
					objPosInfo += objPosInfoOffset - 1;
					int localY = objPosInfo & 0x3f;
					int localX = objPosInfo >> 6 & 0x3f;
					int type = stream.readUnsignedByte() >> 2;
					int mapX = localX + x;
					int mapY = localY + y;

					if (mapX > 0 && mapY > 0 && mapX < 103 && mapY < 103) {
						ObjectDefinition definition = ObjectDefinition.forID(objId);

						if (type != 22 || Settings.GROUND_DECORATIONS || definition.interactive || definition.obstructsGround) {//!lowMem
							bool &= definition.method579();
							bool_255_ = true;
						}
					}
				}
			}
		}
		return bool;
	}

	public void loadLocations(int x, CollisionMap[] collisions, int y, SceneGraph worldController, byte[] data) {
		main_loop:
		{
			Buffer stream = new Buffer(data);
			int objId = -1;
			do {
				int objIdOffset = stream.readUnsignedIntSmartShortCompat();
				if (objIdOffset == 0) {
					break main_loop;
				}
				objId += objIdOffset;
				int objPosInfo = 0;
				do {
					int objPosInfoOffset = stream.readUnsignedShortSmart();
					if (objPosInfoOffset == 0) {
						break;
					}
					objPosInfo += objPosInfoOffset - 1;
					int localY = objPosInfo & 0x3f;
					int localX = objPosInfo >> 6 & 0x3f;
					int height = objPosInfo >> 12;
					int attributes = stream.readUnsignedByte();
					int type = attributes >> 2;
					int face = attributes & 3;
					int mapX = localX + x;
					int mapY = localY + y;
					if (mapX > 0 && mapY > 0 && mapX < 103 && mapY < 103 && height >= 0 && height < 4) {
						int plane = height;

						if ((settings[1][mapX][mapY] & 2) == 2) {
							plane--;
						}

						CollisionMap collision = null;

						if (plane >= 0) {
							collision = collisions[plane];
						}

						addLocation(mapY, worldController, collision, type, height, mapX, objId, face);
					}
				} while (true);
			} while (true);
		}
	}

	public ArrayList<Integer> fogColorList = new ArrayList<Integer>();
	private final int[] anIntArray124;
	private final int[] anIntArray125;
	private final int[] anIntArray126;
	private final int[] anIntArray127;
	private final int[] anIntArray128;
	private final int[][][] Tiles_heights;
	public short[][][] Tiles_overlays;
	static int anInt131;
	private final byte[][][] Tiles_underlays2;
	private final int[][][] anIntArrayArrayArray135;
	public byte[][][] Tiles_shapes;
	private static final int[] anIntArray137 = {1, 0, -1, 0};
	private final int[][] anIntArrayArray139;
	private static final int[] anIntArray140 = {16, 32, 64, 128};
	public short[][][] underlayId;
	private static final int[] anIntArray144 = {0, -1, 0, 1};
	static int Tiles_minPlane = 99;
	private final int Tiles_renderFlags_countX;
	private final int Tiles_renderFlags_countY;
	private final byte[][][] Tiles_rotations;
	private final byte[][][] settings;
	static boolean lowMem = true;
	private static final int[] anIntArray152 = {1, 2, 4, 8};

}
