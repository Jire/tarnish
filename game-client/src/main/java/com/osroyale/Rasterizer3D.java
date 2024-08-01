package com.osroyale;

import com.osroyale.textures.TextureLoader;
import com.osroyale.textures.TextureProvider;

public final class Rasterizer3D extends Rasterizer2D {

	public static int fieldOfView = 512;
	public static boolean world = true;
	public static boolean renderOnGpu = false;

	public static void clear() {
		anIntArray1468 = null;
		SINE = null;
		COSINE = null;
		scanOffsets = null;
		hslToRgb = null;
	}

	public static void useViewport() {
		scanOffsets = new int[Rasterizer2D.height];

		for (int j = 0; j < Rasterizer2D.height; j++) {
			scanOffsets[j] = Rasterizer2D.width * j;
		}

		originViewX = Rasterizer2D.width / 2;
		originViewY = Rasterizer2D.height / 2;
	}

	public static void reposition(int width, int length) {
		scanOffsets = new int[length];
		for (int x = 0; x < length; x++) {
			scanOffsets[x] = width * x;
		}
		originViewX = width / 2;
		originViewY = length / 2;
	}

	public static TextureLoader textureLoader;

	public static final void setTextureLoader(TextureLoader var0) {
		textureLoader = var0; // L: 91
	} // L: 92

	public static final void setBrightness(double var0) {
		((TextureProvider)Rasterizer3D.textureLoader).setBrightness(var0);
		Rasterizer3D_buildPalette(var0, 0, 512); // L: 95
	} // L: 96


	static final void Rasterizer3D_buildPalette(double var0, int var2, int var3) {
		int var4 = var2 * 128; // L: 99

		for (int var5 = var2; var5 < var3; ++var5) { // L: 100
			double var6 = (double)(var5 >> 3) / 64.0D + 0.0078125D; // L: 101
			double var8 = (double)(var5 & 7) / 8.0D + 0.0625D; // L: 102

			for (int var10 = 0; var10 < 128; ++var10) { // L: 103
				double var11 = (double)var10 / 128.0D; // L: 104
				double var13 = var11; // L: 105
				double var15 = var11; // L: 106
				double var17 = var11; // L: 107
				if (var8 != 0.0D) { // L: 108
					double var19;
					if (var11 < 0.5D) { // L: 110
						var19 = var11 * (1.0D + var8);
					} else {
						var19 = var11 + var8 - var11 * var8; // L: 111
					}

					double var21 = 2.0D * var11 - var19; // L: 112
					double var23 = var6 + 0.3333333333333333D; // L: 113
					if (var23 > 1.0D) { // L: 114
						--var23;
					}

					double var27 = var6 - 0.3333333333333333D; // L: 116
					if (var27 < 0.0D) { // L: 117
						++var27;
					}

					if (6.0D * var23 < 1.0D) { // L: 118
						var13 = var21 + (var19 - var21) * 6.0D * var23;
					} else if (2.0D * var23 < 1.0D) { // L: 119
						var13 = var19;
					} else if (3.0D * var23 < 2.0D) { // L: 120
						var13 = var21 + (var19 - var21) * (0.6666666666666666D - var23) * 6.0D;
					} else {
						var13 = var21; // L: 121
					}

					if (6.0D * var6 < 1.0D) { // L: 122
						var15 = var21 + (var19 - var21) * 6.0D * var6;
					} else if (2.0D * var6 < 1.0D) { // L: 123
						var15 = var19;
					} else if (3.0D * var6 < 2.0D) { // L: 124
						var15 = var21 + (var19 - var21) * (0.6666666666666666D - var6) * 6.0D;
					} else {
						var15 = var21; // L: 125
					}

					if (6.0D * var27 < 1.0D) { // L: 126
						var17 = var21 + (var19 - var21) * 6.0D * var27;
					} else if (2.0D * var27 < 1.0D) { // L: 127
						var17 = var19;
					} else if (3.0D * var27 < 2.0D) { // L: 128
						var17 = var21 + (var19 - var21) * (0.6666666666666666D - var27) * 6.0D;
					} else {
						var17 = var21; // L: 129
					}
				}

				int var29 = (int)(var13 * 256.0D); // L: 131
				int var20 = (int)(var15 * 256.0D); // L: 132
				int var30 = (int)(var17 * 256.0D); // L: 133
				int var22 = var30 + (var20 << 8) + (var29 << 16); // L: 134
				var22 = adjustBrightness(var22, var0); // L: 135
				if (var22 == 0) { // L: 136
					var22 = 1;
				}

				hslToRgb[var4++] = var22; // L: 137
			}
		}

	} // L: 140


	public static int adjustBrightness(int rgb, double intensity) {
		double r = (rgb >> 16) / 256D;
		double g = (rgb >> 8 & 0xff) / 256D;
		double b = (rgb & 0xff) / 256D;
		r = Math.pow(r, intensity);
		g = Math.pow(g, intensity);
		b = Math.pow(b, intensity);
		int r_byte = (int) (r * 256D);
		int g_byte = (int) (g * 256D);
		int b_byte = (int) (b * 256D);
		return (r_byte << 16) + (g_byte << 8) + b_byte;
	}

	private static int adjust_brightness(int rgb, double intensity) {
		double r = (double) (rgb >> 16) / 256D;
		double g = (double) (rgb >> 8 & 0xff) / 256D;
		double b = (double) (rgb & 0xff) / 256D;
		r = Math.pow(r, intensity);
		g = Math.pow(g, intensity);
		b = Math.pow(b, intensity);
		int r_byte = (int) (r * 256D);
		int g_byte = (int) (g * 256D);
		int b_byte = (int) (b * 256D);
		return (r_byte << 16) + (g_byte << 8) + b_byte;
	}



	public static void drawShadedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2, int hsl3) {
		if (Client.instance.isGpu() && !renderOnGpu) {
			return;
		}

		int var9 = x2 - x1;
		int var10 = y2 - y1;
		int var11 = x3 - x1;
		int var12 = y3 - y1;
		int var13 = hsl2 - hsl1;
		int var14 = hsl3 - hsl1;
		int var15;
		if (y3 != y2) {
			var15 = (x3 - x2 << 14) / (y3 - y2);
		} else {
			var15 = 0;
		}

		int var16;
		if (y1 != y2) {
			var16 = (var9 << 14) / var10;
		} else {
			var16 = 0;
		}

		int var17;
		if (y1 != y3) {
			var17 = (var11 << 14) / var12;
		} else {
			var17 = 0;
		}

		int var18 = var9 * var12 - var11 * var10;
		if (var18 != 0) {
			int var19 = (var13 * var12 - var14 * var10 << 8) / var18;
			int var20 = (var14 * var9 - var13 * var11 << 8) / var18;
			if (y1 <= y2 && y1 <= y3) {
				if (y1 < Rasterizer2D.bottomY) {
					if (y2 > Rasterizer2D.bottomY) {
						y2 = Rasterizer2D.bottomY;
					}

					if (y3 > Rasterizer2D.bottomY) {
						y3 = Rasterizer2D.bottomY;
					}

					hsl1 = var19 + ((hsl1 << 8) - x1 * var19);
					if (y2 < y3) {
						x3 = x1 <<= 14;
						if (y1 < 0) {
							x3 -= y1 * var17;
							x1 -= y1 * var16;
							hsl1 -= y1 * var20;
							y1 = 0;
						}

						x2 <<= 14;
						if (y2 < 0) {
							x2 -= var15 * y2;
							y2 = 0;
						}

						if ((y1 == y2 || var17 >= var16) && (y1 != y2 || var17 <= var15)) {
							y3 -= y2;
							y2 -= y1;
							y1 = scanOffsets[y1];

							while (true) {
								--y2;
								if (y2 < 0) {
									while (true) {
										--y3;
										if (y3 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x2 >> 14, x3 >> 14, hsl1, var19);
										x3 += var17;
										x2 += var15;
										hsl1 += var20;
										y1 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x1 >> 14, x3 >> 14, hsl1, var19);
								x3 += var17;
								x1 += var16;
								hsl1 += var20;
								y1 += Rasterizer2D.width;
							}
						} else {
							y3 -= y2;
							y2 -= y1;
							y1 = scanOffsets[y1];

							while (true) {
								--y2;
								if (y2 < 0) {
									while (true) {
										--y3;
										if (y3 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x3 >> 14, x2 >> 14, hsl1, var19);
										x3 += var17;
										x2 += var15;
										hsl1 += var20;
										y1 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x3 >> 14, x1 >> 14, hsl1, var19);
								x3 += var17;
								x1 += var16;
								hsl1 += var20;
								y1 += Rasterizer2D.width;
							}
						}
					} else {
						x2 = x1 <<= 14;
						if (y1 < 0) {
							x2 -= y1 * var17;
							x1 -= y1 * var16;
							hsl1 -= y1 * var20;
							y1 = 0;
						}

						x3 <<= 14;
						if (y3 < 0) {
							x3 -= var15 * y3;
							y3 = 0;
						}

						if (y1 != y3 && var17 < var16 || y1 == y3 && var15 > var16) {
							y2 -= y3;
							y3 -= y1;
							y1 = scanOffsets[y1];

							while (true) {
								--y3;
								if (y3 < 0) {
									while (true) {
										--y2;
										if (y2 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x3 >> 14, x1 >> 14, hsl1, var19);
										x3 += var15;
										x1 += var16;
										hsl1 += var20;
										y1 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x2 >> 14, x1 >> 14, hsl1, var19);
								x2 += var17;
								x1 += var16;
								hsl1 += var20;
								y1 += Rasterizer2D.width;
							}
						} else {
							y2 -= y3;
							y3 -= y1;
							y1 = scanOffsets[y1];

							while (true) {
								--y3;
								if (y3 < 0) {
									while (true) {
										--y2;
										if (y2 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x1 >> 14, x3 >> 14, hsl1, var19);
										x3 += var15;
										x1 += var16;
										hsl1 += var20;
										y1 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y1, 0, 0, x1 >> 14, x2 >> 14, hsl1, var19);
								x2 += var17;
								x1 += var16;
								hsl1 += var20;
								y1 += Rasterizer2D.width;
							}
						}
					}
				}
			} else if (y2 <= y3) {
				if (y2 < Rasterizer2D.bottomY) {
					if (y3 > Rasterizer2D.bottomY) {
						y3 = Rasterizer2D.bottomY;
					}

					if (y1 > Rasterizer2D.bottomY) {
						y1 = Rasterizer2D.bottomY;
					}

					hsl2 = var19 + ((hsl2 << 8) - var19 * x2);
					if (y3 < y1) {
						x1 = x2 <<= 14;
						if (y2 < 0) {
							x1 -= var16 * y2;
							x2 -= var15 * y2;
							hsl2 -= var20 * y2;
							y2 = 0;
						}

						x3 <<= 14;
						if (y3 < 0) {
							x3 -= var17 * y3;
							y3 = 0;
						}

						if ((y3 == y2 || var16 >= var15) && (y3 != y2 || var16 <= var17)) {
							y1 -= y3;
							y3 -= y2;
							y2 = scanOffsets[y2];

							while (true) {
								--y3;
								if (y3 < 0) {
									while (true) {
										--y1;
										if (y1 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x3 >> 14, x1 >> 14, hsl2, var19);
										x1 += var16;
										x3 += var17;
										hsl2 += var20;
										y2 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x2 >> 14, x1 >> 14, hsl2, var19);
								x1 += var16;
								x2 += var15;
								hsl2 += var20;
								y2 += Rasterizer2D.width;
							}
						} else {
							y1 -= y3;
							y3 -= y2;
							y2 = scanOffsets[y2];

							while (true) {
								--y3;
								if (y3 < 0) {
									while (true) {
										--y1;
										if (y1 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x1 >> 14, x3 >> 14, hsl2, var19);
										x1 += var16;
										x3 += var17;
										hsl2 += var20;
										y2 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x1 >> 14, x2 >> 14, hsl2, var19);
								x1 += var16;
								x2 += var15;
								hsl2 += var20;
								y2 += Rasterizer2D.width;
							}
						}
					} else {
						x3 = x2 <<= 14;
						if (y2 < 0) {
							x3 -= var16 * y2;
							x2 -= var15 * y2;
							hsl2 -= var20 * y2;
							y2 = 0;
						}

						x1 <<= 14;
						if (y1 < 0) {
							x1 -= y1 * var17;
							y1 = 0;
						}

						if (var16 < var15) {
							y3 -= y1;
							y1 -= y2;
							y2 = scanOffsets[y2];

							while (true) {
								--y1;
								if (y1 < 0) {
									while (true) {
										--y3;
										if (y3 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x1 >> 14, x2 >> 14, hsl2, var19);
										x1 += var17;
										x2 += var15;
										hsl2 += var20;
										y2 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x3 >> 14, x2 >> 14, hsl2, var19);
								x3 += var16;
								x2 += var15;
								hsl2 += var20;
								y2 += Rasterizer2D.width;
							}
						} else {
							y3 -= y1;
							y1 -= y2;
							y2 = scanOffsets[y2];

							while (true) {
								--y1;
								if (y1 < 0) {
									while (true) {
										--y3;
										if (y3 < 0) {
											return;
										}

										drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x2 >> 14, x1 >> 14, hsl2, var19);
										x1 += var17;
										x2 += var15;
										hsl2 += var20;
										y2 += Rasterizer2D.width;
									}
								}

								drawGouraudScanline(Rasterizer2D.pixels, y2, 0, 0, x2 >> 14, x3 >> 14, hsl2, var19);
								x3 += var16;
								x2 += var15;
								hsl2 += var20;
								y2 += Rasterizer2D.width;
							}
						}
					}
				}
			} else if (y3 < Rasterizer2D.bottomY) {
				if (y1 > Rasterizer2D.bottomY) {
					y1 = Rasterizer2D.bottomY;
				}

				if (y2 > Rasterizer2D.bottomY) {
					y2 = Rasterizer2D.bottomY;
				}

				hsl3 = var19 + ((hsl3 << 8) - x3 * var19);
				if (y1 < y2) {
					x2 = x3 <<= 14;
					if (y3 < 0) {
						x2 -= var15 * y3;
						x3 -= var17 * y3;
						hsl3 -= var20 * y3;
						y3 = 0;
					}

					x1 <<= 14;
					if (y1 < 0) {
						x1 -= y1 * var16;
						y1 = 0;
					}

					if (var15 < var17) {
						y2 -= y1;
						y1 -= y3;
						y3 = scanOffsets[y3];

						while (true) {
							--y1;
							if (y1 < 0) {
								while (true) {
									--y2;
									if (y2 < 0) {
										return;
									}

									drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x2 >> 14, x1 >> 14, hsl3, var19);
									x2 += var15;
									x1 += var16;
									hsl3 += var20;
									y3 += Rasterizer2D.width;
								}
							}

							drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x2 >> 14, x3 >> 14, hsl3, var19);
							x2 += var15;
							x3 += var17;
							hsl3 += var20;
							y3 += Rasterizer2D.width;
						}
					} else {
						y2 -= y1;
						y1 -= y3;
						y3 = scanOffsets[y3];

						while (true) {
							--y1;
							if (y1 < 0) {
								while (true) {
									--y2;
									if (y2 < 0) {
										return;
									}

									drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x1 >> 14, x2 >> 14, hsl3, var19);
									x2 += var15;
									x1 += var16;
									hsl3 += var20;
									y3 += Rasterizer2D.width;
								}
							}

							drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x3 >> 14, x2 >> 14, hsl3, var19);
							x2 += var15;
							x3 += var17;
							hsl3 += var20;
							y3 += Rasterizer2D.width;
						}
					}
				} else {
					x1 = x3 <<= 14;
					if (y3 < 0) {
						x1 -= var15 * y3;
						x3 -= var17 * y3;
						hsl3 -= var20 * y3;
						y3 = 0;
					}

					x2 <<= 14;
					if (y2 < 0) {
						x2 -= var16 * y2;
						y2 = 0;
					}

					if (var15 < var17) {
						y1 -= y2;
						y2 -= y3;
						y3 = scanOffsets[y3];

						while (true) {
							--y2;
							if (y2 < 0) {
								while (true) {
									--y1;
									if (y1 < 0) {
										return;
									}

									drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x2 >> 14, x3 >> 14, hsl3, var19);
									x2 += var16;
									x3 += var17;
									hsl3 += var20;
									y3 += Rasterizer2D.width;
								}
							}

							drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x1 >> 14, x3 >> 14, hsl3, var19);
							x1 += var15;
							x3 += var17;
							hsl3 += var20;
							y3 += Rasterizer2D.width;
						}
					} else {
						y1 -= y2;
						y2 -= y3;
						y3 = scanOffsets[y3];

						while (true) {
							--y2;
							if (y2 < 0) {
								while (true) {
									--y1;
									if (y1 < 0) {
										return;
									}

									drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x3 >> 14, x2 >> 14, hsl3, var19);
									x2 += var16;
									x3 += var17;
									hsl3 += var20;
									y3 += Rasterizer2D.width;
								}
							}

							drawGouraudScanline(Rasterizer2D.pixels, y3, 0, 0, x3 >> 14, x1 >> 14, hsl3, var19);
							x1 += var15;
							x3 += var17;
							hsl3 += var20;
							y3 += Rasterizer2D.width;
						}
					}
				}
			}
		}
	}

	public static void drawGouraudScanline(int var0[], int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		if (!Client.instance.isResized()
				&& world && var1 <= 259086) { //(512+4)+(334+4)*765
			var1 += 3064; //4+4*765
		}

		if (textureOutOfDrawingBounds) {
			if (var5 > lastX) {
				var5 = lastX;
			}

			if (var4 < 0) {
				var4 = 0;
			}
		}

		if (var4 < var5) {
			var1 += var4;
			var6 += var4 * var7;
			int var8;
			int var9;
			int var10;
			if (aBoolean1464) {
				var3 = var5 - var4 >> 2;
				var7 <<= 2;
				if (alpha == 0) {
					if (var3 > 0) {
						do {
							var2 = hslToRgb[var6 >> 8];
							var6 += var7;
							drawAlpha(var0, var1++, var2, 255);
							drawAlpha(var0, var1++, var2, 255);
							drawAlpha(var0, var1++, var2, 255);
							drawAlpha(var0, var1++, var2, 255);
							--var3;
						} while(var3 > 0);
					}

					var3 = var5 - var4 & 3;
					if (var3 > 0) {
						var2 = hslToRgb[var6 >> 8];

						do {
							drawAlpha(var0, var1++, var2, 255);
							--var3;
						} while(var3 > 0);
					}
				} else {
					var8 = alpha;
					var9 = 256 - alpha;
					if (var3 > 0) {
						do {
							var2 = hslToRgb[var6 >> 8];
							var6 += var7;
							var2 = (var9 * (var2 & 65280) >> 8 & 65280) + (var9 * (var2 & 16711935) >> 8 & 16711935);
							var10 = var0[var1];
							drawAlpha(var0, var1++, ((var10 & 16711935) * var8 >> 8 & 16711935) + var2 + (var8 * (var10 & 65280) >> 8 & 65280), 255);
							var10 = var0[var1];
							drawAlpha(var0, var1++, ((var10 & 16711935) * var8 >> 8 & 16711935) + var2 + (var8 * (var10 & 65280) >> 8 & 65280), 255);
							var10 = var0[var1];
							drawAlpha(var0, var1++, ((var10 & 16711935) * var8 >> 8 & 16711935) + var2 + (var8 * (var10 & 65280) >> 8 & 65280), 255);
							var10 = var0[var1];
							drawAlpha(var0, var1++, ((var10 & 16711935) * var8 >> 8 & 16711935) + var2 + (var8 * (var10 & 65280) >> 8 & 65280), 255);
							--var3;
						} while(var3 > 0);
					}

					var3 = var5 - var4 & 3;
					if (var3 > 0) {
						var2 = hslToRgb[var6 >> 8];
						var2 = (var9 * (var2 & 65280) >> 8 & 65280) + (var9 * (var2 & 16711935) >> 8 & 16711935);

						do {
							var10 = var0[var1];
							drawAlpha(var0, var1++, ((var10 & 16711935) * var8 >> 8 & 16711935) + var2 + (var8 * (var10 & 65280) >> 8 & 65280), 255);
							--var3;
						} while(var3 > 0);
					}
				}

			} else {
				var3 = var5 - var4;
				if (alpha == 0) {
					do {
						drawAlpha(var0, var1++, hslToRgb[var6 >> 8], 255);
						var6 += var7;
						--var3;
					} while(var3 > 0);
				} else {
					var8 = alpha;
					var9 = 256 - alpha;

					do {
						var2 = hslToRgb[var6 >> 8];
						var6 += var7;
						var2 = (var9 * (var2 & 65280) >> 8 & 65280) + (var9 * (var2 & 16711935) >> 8 & 16711935);
						var10 = var0[var1];
						drawAlpha(var0, var1++, ((var10 & 16711935) * var8 >> 8 & 16711935) + var2 + (var8 * (var10 & 65280) >> 8 & 65280), 255);
						--var3;
					} while(var3 > 0);
				}

			}
		}
	}

	public static void drawFlatTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int k1) {
		if (Client.instance.isGpu() && !renderOnGpu) {
			return;
		}

		int a_to_b = 0;
		if (y_b != y_a) {
			a_to_b = (x_b - x_a << 16) / (y_b - y_a);
		}
		int b_to_c = 0;
		if (y_c != y_b) {
			b_to_c = (x_c - x_b << 16) / (y_c - y_b);
		}
		int c_to_a = 0;
		if (y_c != y_a) {
			c_to_a = (x_a - x_c << 16) / (y_a - y_c);
		}
		float b_aX = x_b - x_a;
		float b_aY = y_b - y_a;
		float c_aX = x_c - x_a;
		float c_aY = y_c - y_a;

		if (y_a <= y_b && y_a <= y_c) {
			if (y_a >= Rasterizer2D.bottomY)
				return;
			if (y_b > Rasterizer2D.bottomY)
				y_b = Rasterizer2D.bottomY;
			if (y_c > Rasterizer2D.bottomY)
				y_c = Rasterizer2D.bottomY;
			if (y_b < y_c) {
				x_c = x_a <<= 16;
				if (y_a < 0) {
					x_c -= c_to_a * y_a;
					x_a -= a_to_b * y_a;
					y_a = 0;
				}
				x_b <<= 16;
				if (y_b < 0) {
					x_b -= b_to_c * y_b;
					y_b = 0;
				}
				if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
					y_c -= y_b;
					y_b -= y_a;
					for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_c >> 16, x_a >> 16);
						x_c += c_to_a;
						x_a += a_to_b;
					}

					while (--y_c >= 0) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_c >> 16, x_b >> 16);
						x_c += c_to_a;
						x_b += b_to_c;
						y_a += Rasterizer2D.width;
					}
					return;
				}
				y_c -= y_b;
				y_b -= y_a;
				for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_a >> 16, x_c >> 16);
					x_c += c_to_a;
					x_a += a_to_b;
				}

				while (--y_c >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_b >> 16, x_c >> 16);
					x_c += c_to_a;
					x_b += b_to_c;
					y_a += Rasterizer2D.width;
				}
				return;
			}
			x_b = x_a <<= 16;
			if (y_a < 0) {
				x_b -= c_to_a * y_a;
				x_a -= a_to_b * y_a;
				y_a = 0;

			}
			x_c <<= 16;
			if (y_c < 0) {
				x_c -= b_to_c * y_c;
				y_c = 0;
			}
			if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
				y_b -= y_c;
				y_c -= y_a;
				for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_b >> 16, x_a >> 16);
					x_b += c_to_a;
					x_a += a_to_b;
				}

				while (--y_b >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_c >> 16, x_a >> 16);
					x_c += b_to_c;
					x_a += a_to_b;
					y_a += Rasterizer2D.width;
				}
				return;
			}
			y_b -= y_c;
			y_c -= y_a;
			for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_a >> 16, x_b >> 16);
				x_b += c_to_a;
				x_a += a_to_b;
			}

			while (--y_b >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_a, k1, x_a >> 16, x_c >> 16);
				x_c += b_to_c;
				x_a += a_to_b;
				y_a += Rasterizer2D.width;
			}
			return;
		}
		if (y_b <= y_c) {
			if (y_b >= Rasterizer2D.bottomY)
				return;
			if (y_c > Rasterizer2D.bottomY)
				y_c = Rasterizer2D.bottomY;
			if (y_a > Rasterizer2D.bottomY)
				y_a = Rasterizer2D.bottomY;
			if (y_c < y_a) {
				x_a = x_b <<= 16;
				if (y_b < 0) {
					x_a -= a_to_b * y_b;
					x_b -= b_to_c * y_b;
					y_b = 0;
				}
				x_c <<= 16;
				if (y_c < 0) {
					x_c -= c_to_a * y_c;
					y_c = 0;
				}
				if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
					y_a -= y_c;
					y_c -= y_b;
					for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_a >> 16, x_b >> 16);
						x_a += a_to_b;
						x_b += b_to_c;
					}

					while (--y_a >= 0) {
						drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_a >> 16, x_c >> 16);
						x_a += a_to_b;
						x_c += c_to_a;
						y_b += Rasterizer2D.width;
					}
					return;
				}
				y_a -= y_c;
				y_c -= y_b;
				for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_b >> 16, x_a >> 16);
					x_a += a_to_b;
					x_b += b_to_c;
				}

				while (--y_a >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_c >> 16, x_a >> 16);
					x_a += a_to_b;
					x_c += c_to_a;
					y_b += Rasterizer2D.width;
				}
				return;
			}
			x_c = x_b <<= 16;
			if (y_b < 0) {
				x_c -= a_to_b * y_b;
				x_b -= b_to_c * y_b;
				y_b = 0;
			}
			x_a <<= 16;
			if (y_a < 0) {
				x_a -= c_to_a * y_a;
				y_a = 0;
			}
			if (a_to_b < b_to_c) {
				y_c -= y_a;
				y_a -= y_b;
				for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_c >> 16, x_b >> 16);
					x_c += a_to_b;
					x_b += b_to_c;
				}

				while (--y_c >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_a >> 16, x_b >> 16);
					x_a += c_to_a;
					x_b += b_to_c;
					y_b += Rasterizer2D.width;
				}
				return;
			}
			y_c -= y_a;
			y_a -= y_b;
			for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_b >> 16, x_c >> 16);
				x_c += a_to_b;
				x_b += b_to_c;
			}

			while (--y_c >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_b, k1, x_b >> 16, x_a >> 16);
				x_a += c_to_a;
				x_b += b_to_c;
				y_b += Rasterizer2D.width;
			}
			return;
		}
		if (y_c >= Rasterizer2D.bottomY)
			return;
		if (y_a > Rasterizer2D.bottomY)
			y_a = Rasterizer2D.bottomY;
		if (y_b > Rasterizer2D.bottomY)
			y_b = Rasterizer2D.bottomY;
		if (y_a < y_b) {
			x_b = x_c <<= 16;
			if (y_c < 0) {
				x_b -= b_to_c * y_c;
				x_c -= c_to_a * y_c;
				y_c = 0;
			}
			x_a <<= 16;
			if (y_a < 0) {
				x_a -= a_to_b * y_a;
				y_a = 0;
			}
			if (b_to_c < c_to_a) {
				y_b -= y_a;
				y_a -= y_c;
				for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_b >> 16, x_c >> 16);
					x_b += b_to_c;
					x_c += c_to_a;
				}

				while (--y_b >= 0) {
					drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_b >> 16, x_a >> 16);
					x_b += b_to_c;
					x_a += a_to_b;
					y_c += Rasterizer2D.width;
				}
				return;
			}
			y_b -= y_a;
			y_a -= y_c;
			for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_c >> 16, x_b >> 16);
				x_b += b_to_c;
				x_c += c_to_a;
			}

			while (--y_b >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_a >> 16, x_b >> 16);
				x_b += b_to_c;
				x_a += a_to_b;
				y_c += Rasterizer2D.width;
			}
			return;
		}
		x_a = x_c <<= 16;
		if (y_c < 0) {
			x_a -= b_to_c * y_c;
			x_c -= c_to_a * y_c;
			y_c = 0;
		}
		x_b <<= 16;
		if (y_b < 0) {
			x_b -= a_to_b * y_b;
			y_b = 0;
		}
		if (b_to_c < c_to_a) {
			y_a -= y_b;
			y_b -= y_c;
			for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_a >> 16, x_c >> 16);
				x_a += b_to_c;
				x_c += c_to_a;
			}

			while (--y_a >= 0) {
				drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_b >> 16, x_c >> 16);
				x_b += a_to_b;
				x_c += c_to_a;
				y_c += Rasterizer2D.width;
			}
			return;
		}
		y_a -= y_b;
		y_b -= y_c;
		for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
			drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_c >> 16, x_a >> 16);
			x_a += b_to_c;
			x_c += c_to_a;
		}

		while (--y_a >= 0) {
			drawFlatTexturedScanline(Rasterizer2D.pixels, y_c, k1, x_c >> 16, x_b >> 16);
			x_b += a_to_b;
			x_c += c_to_a;
			y_c += Rasterizer2D.width;
		}
	}

	private static void drawFlatTexturedScanline(int dest[], int dest_off, int loops, int start_x, int end_x) {
		if (!Client.instance.isResized() && world && dest_off <= 259086) {
			dest_off += 3064; //4+4*765
		}

		int rgb;
		if (textureOutOfDrawingBounds) {
			if (end_x > Rasterizer2D.lastX)
				end_x = Rasterizer2D.lastX;
			if (start_x < 0)
				start_x = 0;
		}
		if (start_x >= end_x)
			return;
		dest_off += start_x;
		rgb = end_x - start_x >> 2;
		if (alpha == 0) {
			while (--rgb >= 0) {
				for (int i = 0; i < 4; i++) {
					drawAlpha(dest, dest_off, loops, 255);
					dest_off++;
				}
			}
			for (rgb = end_x - start_x & 3; --rgb >= 0;) {
				drawAlpha(dest, dest_off, loops, 255);
				dest_off++;
			}
			return;
		}
		int dest_alpha = alpha;
		int src_alpha = 256 - alpha;
		loops = ((loops & 0xff00ff) * src_alpha >> 8 & 0xff00ff) + ((loops & 0xff00) * src_alpha >> 8 & 0xff00);
		while (--rgb >= 0) {
			for (int i = 0; i < 4; i++) {
				drawAlpha(dest, dest_off, loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00), 255);
				dest_off++;
			}
		}
		for (rgb = end_x - start_x & 3; --rgb >= 0;) {
			drawAlpha(dest, dest_off, loops + ((dest[dest_off] & 0xff00ff) * dest_alpha >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * dest_alpha >> 8 & 0xff00), 255);
			dest_off++;
		}
	}

	public static void drawTexturedTriangle(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int textureID) {
		if (Client.instance.isGpu() && !renderOnGpu) {
			return;
		}
		int[] texturePixels = textureLoader.getTexturePixels(textureID);
		int averageRGB;
		if (texturePixels == null) {
			averageRGB = textureLoader.getAverageTextureRGB(textureID);
			drawShadedTriangle(var0, var1, var2, var3, var4, var5, light(averageRGB, var6), light(averageRGB, var7), light(averageRGB, var8));
		} else {
			lowMem = textureLoader.isLowDetail(textureID);
			isTransparent = textureLoader.isTransparent(textureID);
			int var21;
			var21 = var4 - var3;
			int var26 = var1 - var0;
			int var27 = var5 - var3;
			int var31 = var2 - var0;
			int var28 = var7 - var6;
			int var23 = var8 - var6;
			int var29 = 0;
			if(var1 != var0) {
				var29 = (var4 - var3 << 16) / (var1 - var0);
			}

			int var30 = 0;
			if(var2 != var1) {
				var30 = (var5 - var4 << 16) / (var2 - var1);
			}

			int var22 = 0;
			if(var2 != var0) {
				var22 = (var3 - var5 << 16) / (var0 - var2);
			}

			int var32 = var21 * var31 - var27 * var26;
			if(var32 != 0) {
				int var41 = (var28 * var31 - var23 * var26 << 9) / var32;
				int var20 = (var23 * var21 - var28 * var27 << 9) / var32;
				var10 = var9 - var10;
				var13 = var12 - var13;
				var16 = var15 - var16;
				var11 -= var9;
				var14 -= var12;
				var17 -= var15;
				final int FOV = (aBoolean1464 ? fieldOfView : 512);
				int var24 = var11 * var12 - var14 * var9 << 14;
				int var38 = (int)(((long)(var14 * var15 - var17 * var12) << 3 << 14) / (long)FOV);
				int var25 = (int)(((long)(var17 * var9 - var11 * var15) << 14) / (long)FOV);
				int var36 = var10 * var12 - var13 * var9 << 14;
				int var39 = (int)(((long)(var13 * var15 - var16 * var12) << 3 << 14) / (long)FOV);
				int var37 = (int)(((long)(var16 * var9 - var10 * var15) << 14) / (long)FOV);
				int var33 = var13 * var11 - var10 * var14 << 14;
				int var40 = (int)(((long)(var16 * var14 - var13 * var17) << 3 << 14) / (long)FOV);
				int var34 = (int)(((long)(var10 * var17 - var16 * var11) << 14) / (long)FOV);


				int var35;
				if(var0 <= var1 && var0 <= var2) {
					if(var0 < Rasterizer2D.bottomY) {
						if(var1 > Rasterizer2D.bottomY) {
							var1 = Rasterizer2D.bottomY;
						}

						if(var2 > Rasterizer2D.bottomY) {
							var2 = Rasterizer2D.bottomY;
						}

						var6 = (var6 << 9) - var41 * var3 + var41;
						if(var1 < var2) {
							var5 = var3 <<= 16;
							if(var0 < 0) {
								var5 -= var22 * var0;
								var3 -= var29 * var0;
								var6 -= var20 * var0;
								var0 = 0;
							}

							var4 <<= 16;
							if(var1 < 0) {
								var4 -= var30 * var1;
								var1 = 0;
							}

							var35 = var0 - originViewY;
							var24 += var25 * var35;
							var36 += var37 * var35;
							var33 += var34 * var35;
							if((var0 == var1 || var22 >= var29) && (var0 != var1 || var22 <= var30)) {
								var2 -= var1;
								var1 -= var0;
								var0 = scanOffsets[var0];

								while(true) {
									--var1;
									if(var1 < 0) {
										while(true) {
											--var2;
											if(var2 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var4 >> 16, var5 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
											var5 += var22;
											var4 += var30;
											var6 += var20;
											var0 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var3 >> 16, var5 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
									var5 += var22;
									var3 += var29;
									var6 += var20;
									var0 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							} else {
								var2 -= var1;
								var1 -= var0;
								var0 = scanOffsets[var0];

								while(true) {
									--var1;
									if(var1 < 0) {
										while(true) {
											--var2;
											if(var2 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var5 >> 16, var4 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
											var5 += var22;
											var4 += var30;
											var6 += var20;
											var0 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var5 >> 16, var3 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
									var5 += var22;
									var3 += var29;
									var6 += var20;
									var0 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							}
						} else {
							var4 = var3 <<= 16;
							if(var0 < 0) {
								var4 -= var22 * var0;
								var3 -= var29 * var0;
								var6 -= var20 * var0;
								var0 = 0;
							}

							var5 <<= 16;
							if(var2 < 0) {
								var5 -= var30 * var2;
								var2 = 0;
							}

							var35 = var0 - originViewY;
							var24 += var25 * var35;
							var36 += var37 * var35;
							var33 += var34 * var35;
							if((var0 == var2 || var22 >= var29) && (var0 != var2 || var30 <= var29)) {
								var1 -= var2;
								var2 -= var0;
								var0 = scanOffsets[var0];

								while(true) {
									--var2;
									if(var2 < 0) {
										while(true) {
											--var1;
											if(var1 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var3 >> 16, var5 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
											var5 += var30;
											var3 += var29;
											var6 += var20;
											var0 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var3 >> 16, var4 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
									var4 += var22;
									var3 += var29;
									var6 += var20;
									var0 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							} else {
								var1 -= var2;
								var2 -= var0;
								var0 = scanOffsets[var0];

								while(true) {
									--var2;
									if(var2 < 0) {
										while(true) {
											--var1;
											if(var1 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var5 >> 16, var3 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
											var5 += var30;
											var3 += var29;
											var6 += var20;
											var0 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var0, var4 >> 16, var3 >> 16, var6, var41, var24, var36, var33, var38, var39, var40);
									var4 += var22;
									var3 += var29;
									var6 += var20;
									var0 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							}
						}
					}
				} else if(var1 <= var2) {
					if(var1 < Rasterizer2D.bottomY) {
						if(var2 > Rasterizer2D.bottomY) {
							var2 = Rasterizer2D.bottomY;
						}

						if(var0 > Rasterizer2D.bottomY) {
							var0 = Rasterizer2D.bottomY;
						}

						var7 = (var7 << 9) - var41 * var4 + var41;
						if(var2 < var0) {
							var3 = var4 <<= 16;
							if(var1 < 0) {
								var3 -= var29 * var1;
								var4 -= var30 * var1;
								var7 -= var20 * var1;
								var1 = 0;
							}

							var5 <<= 16;
							if(var2 < 0) {
								var5 -= var22 * var2;
								var2 = 0;
							}

							var35 = var1 - originViewY;
							var24 += var25 * var35;
							var36 += var37 * var35;
							var33 += var34 * var35;
							if((var1 == var2 || var29 >= var30) && (var1 != var2 || var29 <= var22)) {
								var0 -= var2;
								var2 -= var1;
								var1 = scanOffsets[var1];

								while(true) {
									--var2;
									if(var2 < 0) {
										while(true) {
											--var0;
											if(var0 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var5 >> 16, var3 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
											var3 += var29;
											var5 += var22;
											var7 += var20;
											var1 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var4 >> 16, var3 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
									var3 += var29;
									var4 += var30;
									var7 += var20;
									var1 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							} else {
								var0 -= var2;
								var2 -= var1;
								var1 = scanOffsets[var1];

								while(true) {
									--var2;
									if(var2 < 0) {
										while(true) {
											--var0;
											if(var0 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var3 >> 16, var5 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
											var3 += var29;
											var5 += var22;
											var7 += var20;
											var1 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var3 >> 16, var4 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
									var3 += var29;
									var4 += var30;
									var7 += var20;
									var1 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							}
						} else {
							var5 = var4 <<= 16;
							if(var1 < 0) {
								var5 -= var29 * var1;
								var4 -= var30 * var1;
								var7 -= var20 * var1;
								var1 = 0;
							}

							var3 <<= 16;
							if(var0 < 0) {
								var3 -= var22 * var0;
								var0 = 0;
							}

							var35 = var1 - originViewY;
							var24 += var25 * var35;
							var36 += var37 * var35;
							var33 += var34 * var35;
							if(var29 < var30) {
								var2 -= var0;
								var0 -= var1;
								var1 = scanOffsets[var1];

								while(true) {
									--var0;
									if(var0 < 0) {
										while(true) {
											--var2;
											if(var2 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var3 >> 16, var4 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
											var3 += var22;
											var4 += var30;
											var7 += var20;
											var1 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var5 >> 16, var4 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
									var5 += var29;
									var4 += var30;
									var7 += var20;
									var1 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							} else {
								var2 -= var0;
								var0 -= var1;
								var1 = scanOffsets[var1];

								while(true) {
									--var0;
									if(var0 < 0) {
										while(true) {
											--var2;
											if(var2 < 0) {
												return;
											}

											drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var4 >> 16, var3 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
											var3 += var22;
											var4 += var30;
											var7 += var20;
											var1 += Rasterizer2D.width;
											var24 += var25;
											var36 += var37;
											var33 += var34;
										}
									}

									drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var1, var4 >> 16, var5 >> 16, var7, var41, var24, var36, var33, var38, var39, var40);
									var5 += var29;
									var4 += var30;
									var7 += var20;
									var1 += Rasterizer2D.width;
									var24 += var25;
									var36 += var37;
									var33 += var34;
								}
							}
						}
					}
				} else if(var2 < Rasterizer2D.bottomY) {
					if(var0 > Rasterizer2D.bottomY) {
						var0 = Rasterizer2D.bottomY;
					}

					if(var1 > Rasterizer2D.bottomY) {
						var1 = Rasterizer2D.bottomY;
					}

					var8 = (var8 << 9) - var41 * var5 + var41;
					if(var0 < var1) {
						var4 = var5 <<= 16;
						if(var2 < 0) {
							var4 -= var30 * var2;
							var5 -= var22 * var2;
							var8 -= var20 * var2;
							var2 = 0;
						}

						var3 <<= 16;
						if(var0 < 0) {
							var3 -= var29 * var0;
							var0 = 0;
						}

						var35 = var2 - originViewY;
						var24 += var25 * var35;
						var36 += var37 * var35;
						var33 += var34 * var35;
						if(var30 < var22) {
							var1 -= var0;
							var0 -= var2;
							var2 = scanOffsets[var2];

							while(true) {
								--var0;
								if(var0 < 0) {
									while(true) {
										--var1;
										if(var1 < 0) {
											return;
										}

										drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var4 >> 16, var3 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
										var4 += var30;
										var3 += var29;
										var8 += var20;
										var2 += Rasterizer2D.width;
										var24 += var25;
										var36 += var37;
										var33 += var34;
									}
								}

								drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var4 >> 16, var5 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
								var4 += var30;
								var5 += var22;
								var8 += var20;
								var2 += Rasterizer2D.width;
								var24 += var25;
								var36 += var37;
								var33 += var34;
							}
						} else {
							var1 -= var0;
							var0 -= var2;
							var2 = scanOffsets[var2];

							while(true) {
								--var0;
								if(var0 < 0) {
									while(true) {
										--var1;
										if(var1 < 0) {
											return;
										}

										drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var3 >> 16, var4 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
										var4 += var30;
										var3 += var29;
										var8 += var20;
										var2 += Rasterizer2D.width;
										var24 += var25;
										var36 += var37;
										var33 += var34;
									}
								}

								drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var5 >> 16, var4 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
								var4 += var30;
								var5 += var22;
								var8 += var20;
								var2 += Rasterizer2D.width;
								var24 += var25;
								var36 += var37;
								var33 += var34;
							}
						}
					} else {
						var3 = var5 <<= 16;
						if(var2 < 0) {
							var3 -= var30 * var2;
							var5 -= var22 * var2;
							var8 -= var20 * var2;
							var2 = 0;
						}

						var4 <<= 16;
						if(var1 < 0) {
							var4 -= var29 * var1;
							var1 = 0;
						}

						var35 = var2 - originViewY;
						var24 += var25 * var35;
						var36 += var37 * var35;
						var33 += var34 * var35;
						if(var30 < var22) {
							var0 -= var1;
							var1 -= var2;
							var2 = scanOffsets[var2];

							while(true) {
								--var1;
								if(var1 < 0) {
									while(true) {
										--var0;
										if(var0 < 0) {
											return;
										}

										drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var4 >> 16, var5 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
										var4 += var29;
										var5 += var22;
										var8 += var20;
										var2 += Rasterizer2D.width;
										var24 += var25;
										var36 += var37;
										var33 += var34;
									}
								}

								drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var3 >> 16, var5 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
								var3 += var30;
								var5 += var22;
								var8 += var20;
								var2 += Rasterizer2D.width;
								var24 += var25;
								var36 += var37;
								var33 += var34;
							}
						} else {
							var0 -= var1;
							var1 -= var2;
							var2 = scanOffsets[var2];

							while(true) {
								--var1;
								if(var1 < 0) {
									while(true) {
										--var0;
										if(var0 < 0) {
											return;
										}

										drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var5 >> 16, var4 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
										var4 += var29;
										var5 += var22;
										var8 += var20;
										var2 += Rasterizer2D.width;
										var24 += var25;
										var36 += var37;
										var33 += var34;
									}
								}

								drawTexturedLine(Rasterizer2D.pixels, texturePixels, 0, 0, var2, var5 >> 16, var3 >> 16, var8, var41, var24, var36, var33, var38, var39, var40);
								var3 += var30;
								var5 += var22;
								var8 += var20;
								var2 += Rasterizer2D.width;
								var24 += var25;
								var36 += var37;
								var33 += var34;
							}
						}
					}
				}
			}
		}
	}

	static final int light(int var0, int var1) {
		var1 = (var0 & 127) * var1 >> 7; // L: 2651
		if (var1 < 2) { // L: 2652
			var1 = 2;
		} else if (var1 > 126) { // L: 2653
			var1 = 126;
		}

		return (var0 & 65408) + var1; // L: 2654
	}

	static void drawTexturedLine(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14) {
		if (!Client.instance.isResized() && world && var4 <= 259086) {
			var4 += 3064; //4+4*765
		}

		if(textureOutOfDrawingBounds) {
			if(var6 > Rasterizer2D.lastX) {
				var6 = Rasterizer2D.lastX;
			}

			if(var5 < 0) {
				var5 = 0;
			}
		}

		if(var5 < var6) {
			var4 += var5;
			var7 += var8 * var5;
			int var17 = var6 - var5;
			int var15;
			int var16;
			int var18;
			int var19;
			int var20;
			int var21;
			int var22;
			int var23;
			if(lowMem) {
				var15 = var5 - originViewX;
				var9 += (var12 >> 3) * var15;
				var10 += (var13 >> 3) * var15;
				var11 += (var14 >> 3) * var15;
				var19 = var11 >> 12;
				if(var19 != 0) {
					var20 = var9 / var19;
					var18 = var10 / var19;
					if(var20 < 0) {
						var20 = 0;
					} else if(var20 > 4032) {
						var20 = 4032;
					}
				} else {
					var20 = 0;
					var18 = 0;
				}

				var9 += var12;
				var10 += var13;
				var11 += var14;
				var19 = var11 >> 12;
				if(var19 != 0) {
					var22 = var9 / var19;
					var16 = var10 / var19;
					if(var22 < 0) {
						var22 = 0;
					} else if(var22 > 4032) {
						var22 = 4032;
					}
				} else {
					var22 = 0;
					var16 = 0;
				}

				var2 = (var20 << 20) + var18;
				var23 = (var22 - var20 >> 3 << 20) + (var16 - var18 >> 3);
				var17 >>= 3;
				var8 <<= 3;
				var21 = var7 >> 8;
				if(isTransparent) {
					if(var17 > 0) {
						do {
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var20 = var22;
							var18 = var16;
							var9 += var12;
							var10 += var13;
							var11 += var14;
							var19 = var11 >> 12;
							if(var19 != 0) {
								var22 = var9 / var19;
								var16 = var10 / var19;
								if(var22 < 0) {
									var22 = 0;
								} else if(var22 > 4032) {
									var22 = 4032;
								}
							} else {
								var22 = 0;
								var16 = 0;
							}

							var2 = (var20 << 20) + var18;
							var23 = (var22 - var20 >> 3 << 20) + (var16 - var18 >> 3);
							var7 += var8;
							var21 = var7 >> 8;
							--var17;
						} while(var17 > 0);
					}

					var17 = var6 - var5 & 7;
					if(var17 > 0) {
						do {
							var3 = var1[(var2 & 4032) + (var2 >>> 26)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							--var17;
						} while(var17 > 0);

					}
				} else {
					if(var17 > 0) {
						do {
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var20 = var22;
							var18 = var16;
							var9 += var12;
							var10 += var13;
							var11 += var14;
							var19 = var11 >> 12;
							if(var19 != 0) {
								var22 = var9 / var19;
								var16 = var10 / var19;
								if(var22 < 0) {
									var22 = 0;
								} else if(var22 > 4032) {
									var22 = 4032;
								}
							} else {
								var22 = 0;
								var16 = 0;
							}

							var2 = (var20 << 20) + var18;
							var23 = (var22 - var20 >> 3 << 20) + (var16 - var18 >> 3);
							var7 += var8;
							var21 = var7 >> 8;
							--var17;
						} while(var17 > 0);
					}

					var17 = var6 - var5 & 7;
					if(var17 > 0) {
						do {
							if((var3 = var1[(var2 & 4032) + (var2 >>> 26)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							--var17;
						} while(var17 > 0);

					}
				}
			} else {
				var15 = var5 - originViewX;
				var9 += (var12 >> 3) * var15;
				var10 += (var13 >> 3) * var15;
				var11 += (var14 >> 3) * var15;
				var19 = var11 >> 14;
				if(var19 != 0) {
					var20 = var9 / var19;
					var18 = var10 / var19;
					if(var20 < 0) {
						var20 = 0;
					} else if(var20 > 16256) {
						var20 = 16256;
					}
				} else {
					var20 = 0;
					var18 = 0;
				}

				var9 += var12;
				var10 += var13;
				var11 += var14;
				var19 = var11 >> 14;
				if(var19 != 0) {
					var22 = var9 / var19;
					var16 = var10 / var19;
					if(var22 < 0) {
						var22 = 0;
					} else if(var22 > 16256) {
						var22 = 16256;
					}
				} else {
					var22 = 0;
					var16 = 0;
				}

				var2 = (var20 << 18) + var18;
				var23 = (var22 - var20 >> 3 << 18) + (var16 - var18 >> 3);
				var17 >>= 3;
				var8 <<= 3;
				var21 = var7 >> 8;
				if(isTransparent) {
					if(var17 > 0) {
						do {
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var20 = var22;
							var18 = var16;
							var9 += var12;
							var10 += var13;
							var11 += var14;
							var19 = var11 >> 14;
							if(var19 != 0) {
								var22 = var9 / var19;
								var16 = var10 / var19;
								if(var22 < 0) {
									var22 = 0;
								} else if(var22 > 16256) {
									var22 = 16256;
								}
							} else {
								var22 = 0;
								var16 = 0;
							}

							var2 = (var20 << 18) + var18;
							var23 = (var22 - var20 >> 3 << 18) + (var16 - var18 >> 3);
							var7 += var8;
							var21 = var7 >> 8;
							--var17;
						} while(var17 > 0);
					}

					var17 = var6 - var5 & 7;
					if(var17 > 0) {
						do {
							var3 = var1[(var2 & 16256) + (var2 >>> 25)];
							drawAlpha(var0, var4++, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							var2 += var23;
							--var17;
						} while(var17 > 0);

					}
				} else {
					if(var17 > 0) {
						do {
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var20 = var22;
							var18 = var16;
							var9 += var12;
							var10 += var13;
							var11 += var14;
							var19 = var11 >> 14;
							if(var19 != 0) {
								var22 = var9 / var19;
								var16 = var10 / var19;
								if(var22 < 0) {
									var22 = 0;
								} else if(var22 > 16256) {
									var22 = 16256;
								}
							} else {
								var22 = 0;
								var16 = 0;
							}

							var2 = (var20 << 18) + var18;
							var23 = (var22 - var20 >> 3 << 18) + (var16 - var18 >> 3);
							var7 += var8;
							var21 = var7 >> 8;
							--var17;
						} while(var17 > 0);
					}

					var17 = var6 - var5 & 7;
					if(var17 > 0) {
						do {
							if((var3 = var1[(var2 & 16256) + (var2 >>> 25)]) != 0) {
								drawAlpha(var0, var4, ((var3 & 16711935) * var21 & -16711936) + ((var3 & '\uff00') * var21 & 16711680) >> 8, 255);
							}

							++var4;
							var2 += var23;
							--var17;
						} while(var17 > 0);

					}
				}
			}
		}
	}

	public static boolean lowMem = false;
	public static boolean textureOutOfDrawingBounds;
	private static boolean isTransparent;
	public static boolean aBoolean1464 = true;
	public static int alpha;
	public static int originViewX;
	public static int originViewY;
	private static int[] anIntArray1468;
	public static final int[] anIntArray1469;
	public static int SINE[];
	public static int COSINE[];
	public static int scanOffsets[];
	public static int hslToRgb[] = new int[0x10000];

	static {
		anIntArray1468 = new int[512];
		anIntArray1469 = new int[2048];
		SINE = new int[2048];
		COSINE = new int[2048];
		for (int i = 1; i < 512; i++) {
			anIntArray1468[i] = 32768 / i;
		}
		for (int j = 1; j < 2048; j++) {
			anIntArray1469[j] = 0x10000 / j;
		}
		for (int k = 0; k < 2048; k++) {
			SINE[k] = (int) (65536D * Math.sin((double) k * 0.0030679614999999999D));
			COSINE[k] = (int) (65536D * Math.cos((double) k * 0.0030679614999999999D));
		}
	}

	public static final int method4025(int var0, int var1, int var2, int var3) {
		return var0 * var2 - var3 * var1 >> 16; // L: 2666
	}

	public static final int method4044(int var0, int var1, int var2, int var3) {
		return var3 * var0 + var2 * var1 >> 16; // L: 2670
	}

	public static final int method4045(int var0, int var1, int var2, int var3) {
		return var0 * var2 + var3 * var1 >> 16; // L: 2674
	}

	public static final int method4046(int var0, int var1, int var2, int var3) {
		return var2 * var1 - var3 * var0 >> 16; // L: 2678
	}
}