package com.osroyale;

import com.osroyale.engine.impl.MouseHandler;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.rs.api.*;

import java.util.HashSet;
import java.util.Set;

import static net.runelite.api.Constants.*;


final class SceneGraph implements RSScene {

	public static int viewDistance = 9;

	public static boolean pitchRelaxEnabled;
	public static boolean hdMinimapEnabled = false;
	public static int hoverX = -1;
	public static int hoverY = -1;
	public static int skyboxColor;
	public static int[] tmpX = new int[6];
	public static int[] tmpY = new int[6];
	public static int roofRemovalMode = 0;
	public static final Set<RSTile> tilesToRemove = new HashSet<RSTile>();

	public SceneGraph(int ai[][][]) {
		int i = 104;// was parameter
		int j = 104;// was parameter
		int k = 4;// was parameter
		aBoolean434 = true;
		gameObjectsCache = new GameObject[5000];
		anIntArray486 = new int[10000];
		anIntArray487 = new int[10000];
		sizeZ = k;
		sizeX = j;
		sizeY = i;
		tileArray = new Tile[k][j][i];
		anIntArrayArrayArray445 = new int[k][j + 1][i + 1];
		heightMap = ai;
		initToNull();
	}

	public static void nullLoader() {
		interactableObjects = null;
		sceneClusterCounts = null;
		sceneClusters = null;
		tileDeque = null;
		visibilityMap = null;
		renderArea = null;
	}

	public void initToNull() {
		for (int j = 0; j < sizeZ; j++) {
			for (int k = 0; k < sizeX; k++) {
				for (int i1 = 0; i1 < sizeY; i1++)
					tileArray[j][k][i1] = null;

			}

		}
		for (int l = 0; l < cullingClusterPlaneCount; l++) {
			for (int j1 = 0; j1 < sceneClusterCounts[l]; j1++)
				sceneClusters[l][j1] = null;

			sceneClusterCounts[l] = 0;
		}

		for (int k1 = 0; k1 < obj5CacheCurrPos; k1++)
			gameObjectsCache[k1] = null;

		obj5CacheCurrPos = 0;
		for (int l1 = 0; l1 < interactableObjects.length; l1++)
			interactableObjects[l1] = null;

	}

	public void method275(int i) {
		minLevel = i;
		for (int k = 0; k < sizeX; k++) {
			for (int l = 0; l < sizeY; l++)
				if (tileArray[i][k][l] == null)
					tileArray[i][k][l] = new Tile(i, k, l);

		}

		DrawCallbacks drawCallbacks = Client.instance.drawCallbacks;
		if (drawCallbacks != null) {
			drawCallbacks.loadScene(this);
			drawCallbacks.swapScene(this);
		}
	}

	public void method276(int i, int j) {
		Tile class30_sub3 = tileArray[0][j][i];
		for (int l = 0; l < 3; l++) {
			Tile class30_sub3_1 = tileArray[l][j][i] = tileArray[l + 1][j][i];
			if (class30_sub3_1 != null) {
				class30_sub3_1.z1AnInt1307--;
				for (int j1 = 0; j1 < class30_sub3_1.gameObjectIndex; j1++) {
					GameObject class28 = class30_sub3_1.gameObjects[j1];
					if ((class28.uid >> 29 & 0x3L) == 2L && class28.xLocLow == j && class28.yLocHigh == i)
						class28.zLoc--;
				}

			}
		}
		if (tileArray[0][j][i] == null)
			tileArray[0][j][i] = new Tile(0, j, i);
		tileArray[0][j][i].firstFloorTile = class30_sub3;
		tileArray[3][j][i] = null;
	}

	public static void method277(int i, int j, int k, int l, int i1, int j1, int l1, int i2) {
		CullingCluster class47 = new CullingCluster();
		class47.anInt787 = j / 128;
		class47.anInt788 = l / 128;
		class47.anInt789 = l1 / 128;
		class47.anInt790 = i1 / 128;
		class47.anInt791 = i2;
		class47.anInt792 = j;
		class47.anInt793 = l;
		class47.anInt794 = l1;
		class47.anInt795 = i1;
		class47.anInt796 = j1;
		class47.anInt797 = k;
		sceneClusters[i][sceneClusterCounts[i]++] = class47;
	}

	public void method278(int i, int j, int k, int l) {
		Tile class30_sub3 = tileArray[i][j][k];
		if (class30_sub3 != null) {
			tileArray[i][j][k].logicHeight = l;
		}
	}

	public void addTile(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4, int l4) {
		if (l == 0) {
			SimpleTile class43 = new SimpleTile(k2, l2, i3, j3, -1, k4, false);
			for (int i5 = i; i5 >= 0; i5--)
				if (tileArray[i5][j][k] == null)
					tileArray[i5][j][k] = new Tile(i5, j, k);

			tileArray[i][j][k].mySimpleTile = class43;
			return;
		}
		if (l == 1) {
			SimpleTile class43_1 = new SimpleTile(k3, l3, i4, j4, j1, l4, k1 == l1 && k1 == i2 && k1 == j2);
			for (int j5 = i; j5 >= 0; j5--)
				if (tileArray[j5][j][k] == null)
					tileArray[j5][j][k] = new Tile(j5, j, k);

			tileArray[i][j][k].mySimpleTile = class43_1;
			return;
		}
		ShapedTile class40 = new ShapedTile(k, k3, j3, i2, j1, i4, i1, k2, k4, i3, j2, l1, k1, l, j4, l3, l2, j, l4);
		for (int k5 = i; k5 >= 0; k5--)
			if (tileArray[k5][j][k] == null)
				tileArray[k5][j][k] = new Tile(k5, j, k);

		tileArray[i][j][k].myShapedTile = class40;
	}

	public void method280(int i, int j, int k, Renderable class30_sub2_sub4, long i1, int j1) {
		if (class30_sub2_sub4 == null)
			return;
		GroundDecoration class49 = new GroundDecoration();
		class49.renderable = class30_sub2_sub4;
		class49.tileHeights = j1 * 128 + 64;
		class49.xPos = k * 128 + 64;
		class49.zLoc = j;
		class49.uid = i1;
		if (tileArray[i][j1][k] == null)
			tileArray[i][j1][k] = new Tile(i, j1, k);
		tileArray[i][j1][k].groundDecoration = class49;
	}

	public void method281(int i, long j, Renderable class30_sub2_sub4, int k, Renderable class30_sub2_sub4_1, Renderable class30_sub2_sub4_2, int l, int i1) {
		GroundItemTile object4 = new GroundItemTile();
		object4.topNode = class30_sub2_sub4_2;
		object4.tileHeights = i * 128 + 64;
		object4.xPos = i1 * 128 + 64;
		object4.zLoc = k;
		object4.uid = j;
		object4.lowerNode = class30_sub2_sub4;
		object4.middleNode = class30_sub2_sub4_1;
		int j1 = 0;
		Tile class30_sub3 = tileArray[l][i][i1];
		if (class30_sub3 != null) {
			for (int k1 = 0; k1 < class30_sub3.gameObjectIndex; k1++)
				if ((class30_sub3.gameObjects[k1].uid & 0x400000L) == 4194304L && class30_sub3.gameObjects[k1].renderable instanceof Model) {
					int l1 = ((Model) class30_sub3.gameObjects[k1].renderable).itemDropHeight;
					if (l1 > j1)
						j1 = l1;
				}

		}
		object4.itemDropHeight = j1;
		if (tileArray[l][i][i1] == null)
			tileArray[l][i][i1] = new Tile(l, i, i1);
		tileArray[l][i][i1].groundItemTile = object4;
	}

	public void method282(int i, Renderable class30_sub2_sub4, long j, int k, int l, Renderable class30_sub2_sub4_1, int i1, int j1, int k1) {
		if (class30_sub2_sub4 == null && class30_sub2_sub4_1 == null)
			return;
		Wall object1 = new Wall();
		object1.uid = j;
		object1.tileHeights = l * 128 + 64;
		object1.xPos = k * 128 + 64;
		object1.zLoc = i1;
		object1.renderable1 = class30_sub2_sub4;
		object1.renderable2 = class30_sub2_sub4_1;
		object1.orientation1 = i;
		object1.orientation2 = j1;
		for (int l1 = k1; l1 >= 0; l1--)
			if (tileArray[l1][l][k] == null)
				tileArray[l1][l][k] = new Tile(l1, l, k);

		tileArray[k1][l][k].wallObject = object1;
	}

	public void method283(long i, int j, int k, int i1, int j1, int k1, Renderable class30_sub2_sub4, int l1, int i2, int j2) {
		if (class30_sub2_sub4 == null)
			return;
		WallDecoration class26 = new WallDecoration();
		class26.uid = i;
		class26.tileHeights = l1 * 128 + 64 + j1;
		class26.xPos = j * 128 + 64 + i2;
		class26.zLoc = k1;
		class26.renderable = class30_sub2_sub4;
		class26.orientation = j2;
		class26.orientation2 = k;
		for (int k2 = i1; k2 >= 0; k2--)
			if (tileArray[k2][l1][j] == null)
				tileArray[k2][l1][j] = new Tile(k2, l1, j);

		tileArray[i1][l1][j].wallDecoration = class26;
	}

	public boolean method284(long i, int j, int k, Renderable class30_sub2_sub4, int l, int i1, int j1, int k1, int l1) {
		if (class30_sub2_sub4 == null) {
			return true;
		} else {
			int i2 = l1 * 128 + 64 * l;
			int j2 = k1 * 128 + 64 * k;
			return method287(i1, l1, k1, l, k, i2, j2, j, class30_sub2_sub4, j1, false, i);
		}
	}

	public boolean method285(int i, int j, int k, long l, int i1, int j1, int k1, Renderable class30_sub2_sub4, boolean flag) {
		if (class30_sub2_sub4 == null)
			return true;
		int l1 = k1 - j1;
		int i2 = i1 - j1;
		int j2 = k1 + j1;
		int k2 = i1 + j1;
		if (flag) {
			if (j > 640 && j < 1408)
				k2 += 128;
			if (j > 1152 && j < 1920)
				j2 += 128;
			if (j > 1664 || j < 384)
				i2 -= 128;
			if (j > 128 && j < 896)
				l1 -= 128;
		}
		l1 /= 128;
		i2 /= 128;
		j2 /= 128;
		k2 /= 128;
		return method287(i, l1, i2, (j2 - l1) + 1, (k2 - i2) + 1, k1, i1, k, class30_sub2_sub4, j, true, l);
	}

	public boolean method286(int j, int k, Renderable class30_sub2_sub4, int l, int i1, int j1, int k1, int l1, int i2, long j2, int k2) {
		return class30_sub2_sub4 == null || method287(j, l1, k2, (i2 - l1) + 1, (i1 - k2) + 1, j1, k, k1, class30_sub2_sub4, l, true, j2);
	}

	private boolean method287(int i, int j, int k, int l, int i1, int j1, int k1, int l1, Renderable class30_sub2_sub4, int i2, boolean flag, long j2) {
		for (int k2 = j; k2 < j + l; k2++) {
			for (int l2 = k; l2 < k + i1; l2++) {
				if (k2 < 0 || l2 < 0 || k2 >= sizeX || l2 >= sizeY)
					return false;
				Tile class30_sub3 = tileArray[i][k2][l2];
				if (class30_sub3 != null && class30_sub3.gameObjectIndex >= 5)
					return false;
			}

		}

		GameObject class28 = new GameObject();
		class28.uid = j2;
		class28.zLoc = i;
		class28.xPos = j1;
		class28.yPos = k1;
		class28.tileHeight = l1;
		class28.renderable = class30_sub2_sub4;
		class28.turnValue = i2;
		class28.xLocLow = j;
		class28.yLocHigh = k;
		class28.xLocHigh = (j + l) - 1;
		class28.yLocLow = (k + i1) - 1;
		for (int i3 = j; i3 < j + l; i3++) {
			for (int j3 = k; j3 < k + i1; j3++) {
				int k3 = 0;
				if (i3 > j)
					k3++;
				if (i3 < (j + l) - 1)
					k3 += 4;
				if (j3 > k)
					k3 += 8;
				if (j3 < (k + i1) - 1)
					k3 += 2;
				for (int l3 = i; l3 >= 0; l3--)
					if (tileArray[l3][i3][j3] == null)
						tileArray[l3][i3][j3] = new Tile(l3, i3, j3);

				Tile class30_sub3_1 = tileArray[i][i3][j3];
				class30_sub3_1.gameObjects[class30_sub3_1.gameObjectIndex] = class28;
				class30_sub3_1.tiledObjectMasks[class30_sub3_1.gameObjectIndex] = k3;
				class30_sub3_1.totalTiledObjectMask |= k3;
				class30_sub3_1.gameObjectIndex++;
			}

		}

		if (flag)
			gameObjectsCache[obj5CacheCurrPos++] = class28;
		return true;
	}

	public void clearGameObjectCache() {
		for (int i = 0; i < obj5CacheCurrPos; i++) {
			GameObject object5 = gameObjectsCache[i];
			method289(object5);
			gameObjectsCache[i] = null;
		}

		obj5CacheCurrPos = 0;
	}

	private void method289(GameObject class28) {
		for (int j = class28.xLocLow; j <= class28.xLocHigh; j++) {
			for (int k = class28.yLocHigh; k <= class28.yLocLow; k++) {
				Tile class30_sub3 = tileArray[class28.zLoc][j][k];
				if (class30_sub3 != null) {
					for (int l = 0; l < class30_sub3.gameObjectIndex; l++) {
						if (class30_sub3.gameObjects[l] != class28)
							continue;
						class30_sub3.gameObjectIndex--;
						for (int i1 = l; i1 < class30_sub3.gameObjectIndex; i1++) {
							class30_sub3.gameObjects[i1] = class30_sub3.gameObjects[i1 + 1];
							class30_sub3.tiledObjectMasks[i1] = class30_sub3.tiledObjectMasks[i1 + 1];
						}

						class30_sub3.gameObjects[class30_sub3.gameObjectIndex] = null;
						break;
					}

					class30_sub3.totalTiledObjectMask = 0;
					for (int j1 = 0; j1 < class30_sub3.gameObjectIndex; j1++)
						class30_sub3.totalTiledObjectMask |= class30_sub3.tiledObjectMasks[j1];

				}
			}

		}

	}

	public void method290(int i, int k, int l, int i1) {
		Tile class30_sub3 = tileArray[i1][l][i];
		if (class30_sub3 == null)
			return;
		WallDecoration class26 = class30_sub3.wallDecoration;
		if (class26 != null) {
			int j1 = l * 128 + 64;
			int k1 = i * 128 + 64;
			class26.tileHeights = j1 + ((class26.tileHeights - j1) * k) / 16;
			class26.xPos = k1 + ((class26.xPos - k1) * k) / 16;
		}
	}

	public void method291(int i, int j, int k, byte byte0) {
		Tile class30_sub3 = tileArray[j][i][k];
		if (byte0 != -119)
			aBoolean434 = !aBoolean434;
		if (class30_sub3 != null) {
			class30_sub3.wallObject = null;
		}
	}

	public void method292(int j, int k, int l) {
		Tile class30_sub3 = tileArray[k][l][j];
		if (class30_sub3 != null) {
			class30_sub3.wallDecoration = null;
		}
	}

	public void method293(int i, int k, int l) {
		Tile class30_sub3 = tileArray[i][k][l];
		if (class30_sub3 == null)
			return;
		for (int j1 = 0; j1 < class30_sub3.gameObjectIndex; j1++) {
			GameObject class28 = class30_sub3.gameObjects[j1];
			if ((class28.uid >> 29 & 0x3L) == 2L && class28.xLocLow == k && class28.yLocHigh == l) {
				method289(class28);
				return;
			}
		}

	}

	public void method294(int i, int j, int k) {
		Tile class30_sub3 = tileArray[i][k][j];
		if (class30_sub3 == null)
			return;
		class30_sub3.groundDecoration = null;
	}

	public void method295(int i, int j, int k) {
		Tile class30_sub3 = tileArray[i][j][k];
		if (class30_sub3 != null) {
			class30_sub3.groundItemTile = null;
		}
	}

	public Wall method296(int i, int j, int k) {
		Tile class30_sub3 = tileArray[i][j][k];
		if (class30_sub3 == null)
			return null;
		else
			return class30_sub3.wallObject;
	}

	public WallDecoration method297(int i, int k, int l) {
		Tile class30_sub3 = tileArray[l][i][k];
		if (class30_sub3 == null)
			return null;
		else
			return class30_sub3.wallDecoration;
	}

	public GameObject method298(int i, int j, int k) {
		Tile class30_sub3 = tileArray[k][i][j];
		if (class30_sub3 == null)
			return null;
		for (int l = 0; l < class30_sub3.gameObjectIndex; l++) {
			GameObject class28 = class30_sub3.gameObjects[l];
			if ((class28.uid >> 29 & 0x3L) == 2L && class28.xLocLow == i && class28.yLocHigh == j)
				return class28;
		}
		return null;
	}

	public GroundDecoration method299(int i, int j, int k) {
		Tile class30_sub3 = tileArray[k][j][i];
		if (class30_sub3 == null || class30_sub3.groundDecoration == null)
			return null;
		else
			return class30_sub3.groundDecoration;
	}

	public long method300(int i, int j, int k) {
		Tile class30_sub3 = tileArray[i][j][k];
		if (class30_sub3 == null || class30_sub3.wallObject == null)
			return 0L;
		else
			return class30_sub3.wallObject.uid;
	}

	public long method301(int i, int j, int l) {
		Tile class30_sub3 = tileArray[i][j][l];
		if (class30_sub3 == null || class30_sub3.wallDecoration == null)
			return 0L;
		else
			return class30_sub3.wallDecoration.uid;
	}

	public long method302(int i, int j, int k) {
		Tile class30_sub3 = tileArray[i][j][k];
		if (class30_sub3 == null)
			return 0L;
		for (int l = 0; l < class30_sub3.gameObjectIndex; l++) {
			GameObject class28 = class30_sub3.gameObjects[l];
			if ((class28.uid >> 29 & 0x3L) == 2L && class28.xLocLow == j && class28.yLocHigh == k)
				return class28.uid;
		}

		return 0L;
	}

	public long getTileGroundDecorationUID(final int plane, final int localX, final int localY) {
		final Tile tile = tileArray[plane][localX][localY];
		if (tile == null) return 0L;
		final GroundDecoration groundDecoration = tile.groundDecoration;
		if (groundDecoration == null) return 0L;
		return groundDecoration.uid;
	}

	public boolean method304(int i, int j, int k, long l) {
		Tile class30_sub3 = tileArray[i][j][k];
		if (class30_sub3 == null)
			return false;
		if (class30_sub3.wallObject != null && class30_sub3.wallObject.uid == l)
			return true;
		if (class30_sub3.wallDecoration != null && class30_sub3.wallDecoration.uid == l)
			return true;
		if (class30_sub3.groundDecoration != null && class30_sub3.groundDecoration.uid == l)
			return true;
		for (int i1 = 0; i1 < class30_sub3.gameObjectIndex; i1++)
			if (class30_sub3.gameObjects[i1].uid == l)
				return true;

		return false;
	}

	public void method305(int i, int k, int i1) {
		for (int l1 = 0; l1 < sizeZ; l1++) {
			for (int i2 = 0; i2 < sizeX; i2++) {
				for (int j2 = 0; j2 < sizeY; j2++) {
					Tile tile = tileArray[l1][i2][j2];
					if (tile != null) {
						WallDecoration decoration = tile.wallDecoration;
						if (decoration != null && decoration.renderable instanceof Model) {
							Model model = (Model) decoration.renderable;
							if (!model.isModel) {
								method307(l1, 1, 1, i2, j2, model);
								model.light(model.ambient, model.contrast, k, i, i1, false);
							}
						}

						Wall class10 = tile.wallObject;
						Model model;
						if (class10 != null && class10.renderable1 instanceof Model) {
							Model rootModel = (Model) class10.renderable1;
							if (!rootModel.isModel) {
								method307(l1, 1, 1, i2, j2, rootModel);
								if (class10.renderable2 instanceof Model) {
									model = (Model) class10.renderable2;
									if (!model.isModel) {
										method307(l1, 1, 1, i2, j2, model);
										mergeNormals(rootModel, model, 0, 0, 0, false);
										model.light(model.ambient, model.contrast, k, i, i1, false);
									}
								}
								rootModel.light(rootModel.ambient, rootModel.contrast, k, i, i1, false);
							}
						}
						for (int k2 = 0; k2 < tile.gameObjectIndex; k2++) {
							GameObject class28 = tile.gameObjects[k2];
							if (class28 != null && class28.renderable instanceof Model) {
								model = (Model) class28.renderable;
								if (!model.isModel) {
									method307(l1, (class28.xLocHigh - class28.xLocLow) + 1, (class28.yLocLow - class28.yLocHigh) + 1, i2, j2, model);
									model.light(model.ambient, model.contrast, k, i, i1, false);
								}
							}
						}

						GroundDecoration class49 = tile.groundDecoration;
						if (class49 != null && class49.renderable instanceof Model) {
							model = (Model) class49.renderable;
							if (!model.isModel) {
								method306(i2, l1, model, j2);
								model.light(model.ambient, model.contrast, k, i, i1, false);
							}
						}
					}
				}
			}
		}
	}

	private void method306(int i, int j, Model model, int k) {
		if (i < sizeX) {
			Tile class30_sub3 = tileArray[j][i + 1][k];
			if (class30_sub3 != null && class30_sub3.groundDecoration != null
					&& class30_sub3.groundDecoration.renderable instanceof Model) {
				mergeNormals(model, (Model) class30_sub3.groundDecoration.renderable, 128, 0, 0, true);
			}
		}
		if (k < sizeX) {
			Tile class30_sub3_1 = tileArray[j][i][k + 1];
			if (class30_sub3_1 != null && class30_sub3_1.groundDecoration != null
					&& class30_sub3_1.groundDecoration.renderable instanceof Model) {
				mergeNormals(model, (Model) class30_sub3_1.groundDecoration.renderable, 0, 0, 128, true);
			}
		}
		if (i < sizeX && k < sizeY) {
			Tile class30_sub3_2 = tileArray[j][i + 1][k + 1];
			if (class30_sub3_2 != null && class30_sub3_2.groundDecoration != null
					&& class30_sub3_2.groundDecoration.renderable instanceof Model) {
				mergeNormals(model, (Model) class30_sub3_2.groundDecoration.renderable, 128, 0, 128, true);
			}
		}
		if (i < sizeX && k > 0) {
			Tile class30_sub3_3 = tileArray[j][i + 1][k - 1];
			if (class30_sub3_3 != null && class30_sub3_3.groundDecoration != null
					&& class30_sub3_3.groundDecoration.renderable instanceof Model) {
				mergeNormals(model, (Model) class30_sub3_3.groundDecoration.renderable, 128, 0, -128, true);
			}
		}
	}

	private void method307(int i, int j, int k, int l, int i1, Model model) {
		boolean flag = true;
		int j1 = l;
		int k1 = l + j;
		int l1 = i1 - 1;
		int i2 = i1 + k;
		for (int j2 = i; j2 <= i + 1; j2++)
			if (j2 != sizeZ) {
				for (int k2 = j1; k2 <= k1; k2++)
					if (k2 >= 0 && k2 < sizeX) {
						for (int l2 = l1; l2 <= i2; l2++)
							if (l2 >= 0 && l2 < sizeY && (!flag || k2 >= k1 || l2 >= i2 || l2 < i1 && k2 != l)) {
								Tile class30_sub3 = tileArray[j2][k2][l2];
								if (class30_sub3 != null) {
									int i3 = (heightMap[j2][k2][l2] + heightMap[j2][k2 + 1][l2] + heightMap[j2][k2][l2 + 1] + heightMap[j2][k2 + 1][l2 + 1]) / 4 - (heightMap[i][l][i1] + heightMap[i][l + 1][i1] + heightMap[i][l][i1 + 1] + heightMap[i][l + 1][i1 + 1]) / 4;
									Wall class10 = class30_sub3.wallObject;
									if (class10 != null && class10.renderable1 instanceof Model) {
										Model m = (Model) class10.renderable1;
										if (!m.isModel) {
											mergeNormals(model, m, (k2 - l) * 128 + (1 - j) * 64, i3, (l2 - i1) * 128 + (1 - k) * 64, flag);
										}
									}
									if (class10 != null && class10.renderable2 instanceof Model) {
										Model m = (Model) class10.renderable2;
										if (!m.isModel) {
											mergeNormals(model, m, (k2 - l) * 128 + (1 - j) * 64, i3, (l2 - i1) * 128 + (1 - k) * 64, flag);
										}
									}
									for (int j3 = 0; j3 < class30_sub3.gameObjectIndex; j3++) {
										GameObject class28 = class30_sub3.gameObjects[j3];
										if (class28 != null && class28.renderable instanceof Model) {
											Model m = (Model) class28.renderable;
											if (!m.isModel) {
												int k3 = (class28.xLocHigh - class28.xLocLow) + 1;
												int l3 = (class28.yLocLow - class28.yLocHigh) + 1;
												mergeNormals(model, m, (class28.xLocLow - l) * 128 + (k3 - j) * 64, i3, (class28.yLocHigh - i1) * 128 + (l3 - k) * 64, flag);
											}
										}
									}
								}
							}

					}

				j1--;
				flag = false;
			}

	}

	private void mergeNormals(Model first, Model second, int dx, int dy, int dz, boolean flag) {
		first.calculateBounds();
		first.calculateVertexNormals();
		second.calculateBounds();
		second.calculateVertexNormals();
		anInt488++;
		int count = 0;
		int[] secondX = second.verticesX;
		int secondVertices = second.verticesCount;

		int var9;
		for (var9 = 0; var9 < first.verticesCount; ++var9) {
			VertexNormal var10 = first.vertexNormals[var9];
			if (var10.magnitude != 0) {
				int var11 = first.verticesY[var9] - dy;
				if (var11 <= second.maxY) {
					int var12 = first.verticesX[var9] - dx;
					if (var12 >= second.minX && var12 <= second.maxX) {
						int var13 = first.verticesZ[var9] - dz;
						if (var13 >= second.minZ && var13 <= second.maxZ) {
							for (int var14 = 0; var14 < secondVertices; ++var14) {
								VertexNormal var15 = second.vertexNormals[var14];
								if (var12 == secondX[var14] && var13 == second.verticesZ[var14] && var11 == second.verticesY[var14]
										&& var15.magnitude != 0) {
									if (first.vertexNormalsOffsets == null) {
										first.vertexNormalsOffsets = new VertexNormal[first.verticesCount];
									}

									if (second.vertexNormalsOffsets == null) {
										second.vertexNormalsOffsets = new VertexNormal[secondVertices];
									}

									VertexNormal var16 = first.vertexNormalsOffsets[var9];
									if (var16 == null) {
										var16 = first.vertexNormalsOffsets[var9] = new VertexNormal(var10);
									}

									VertexNormal var17 = second.vertexNormalsOffsets[var14];
									if (var17 == null) {
										var17 = second.vertexNormalsOffsets[var14] = new VertexNormal(var15);
									}

									var16.x += var15.x;
									var16.y += var15.y;
									var16.z += var15.z;
									var16.magnitude += var15.magnitude;
									var17.x += var10.x;
									var17.y += var10.y;
									var17.z += var10.z;
									var17.magnitude += var10.magnitude;
									++count;
									anIntArray486[var9] = anInt488;
									anIntArray487[var14] = anInt488;
								}
							}
						}
					}
				}
			}
		}

		if (count >= 3 && flag) {
			for (var9 = 0; var9 < first.triangleFaceCount; ++var9) {
				if (anIntArray486[first.trianglePointsX[var9]] == anInt488
						&& anIntArray486[first.trianglePointsY[var9]] == anInt488
						&& anIntArray486[first.trianglePointsZ[var9]] == anInt488) {
					if (first.faceRenderType == null) {
						first.faceRenderType = new int[first.triangleFaceCount];
					}

					first.faceRenderType[var9] = 2;
				}
			}

			for (var9 = 0; var9 < second.triangleFaceCount; ++var9) {
				if (anInt488 == anIntArray487[second.trianglePointsX[var9]] && anInt488 == anIntArray487[second.trianglePointsY[var9]]
						&& anInt488
						== anIntArray487[second.trianglePointsZ[var9]]) {
					if (second.faceRenderType == null) {
						second.faceRenderType = new int[second.triangleFaceCount];
					}

					second.faceRenderType[var9] = 2;
				}
			}
		}
	}

	public void drawTileMinimap(int[] pixels, int pixelOffset, int z, int x, int y)
	{
		if (!Client.instance.isHdMinimapEnabled())
		{
			drawTileMinimapSD(pixels, pixelOffset, z, x, y);
			return;
		}
		RSTile tile = getTiles()[z][x][y];
		if (tile == null)
		{
			return;
		}
		SceneTilePaint sceneTilePaint = tile.getSceneTilePaint();
		if (sceneTilePaint != null)
		{
			int rgb = sceneTilePaint.getRBG();
			if (sceneTilePaint.getSwColor() != INVALID_HSL_COLOR)
			{
				// hue and saturation
				int hs = sceneTilePaint.getSwColor() & ~0x7F;
				// I know this looks dumb (and it probably is) but I don't feel like hunting down the problem
				int seLightness = sceneTilePaint.getNwColor() & 0x7F;
				int neLightness = sceneTilePaint.getNeColor() & 0x7F;
				int southDeltaLightness = (sceneTilePaint.getSwColor() & 0x7F) - seLightness;
				int northDeltaLightness = (sceneTilePaint.getSeColor() & 0x7F) - neLightness;
				seLightness <<= 2;
				neLightness <<= 2;
				for (int i = 0; i < 4; i++)
				{
					if (sceneTilePaint.getTexture() == -1)
					{
						pixels[pixelOffset] = Rasterizer3D.hslToRgb[hs | seLightness >> 2];
						pixels[pixelOffset + 1] = Rasterizer3D.hslToRgb[hs | seLightness * 3 + neLightness >> 4];
						pixels[pixelOffset + 2] = Rasterizer3D.hslToRgb[hs | seLightness + neLightness >> 3];
						pixels[pixelOffset + 3] = Rasterizer3D.hslToRgb[hs | seLightness + neLightness * 3 >> 4];
					}
					else
					{
						int lig = 0xFF - ((seLightness >> 1) * (seLightness >> 1) >> 8);
						pixels[pixelOffset] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
						lig = 0xFF - ((seLightness * 3 + neLightness >> 3) * (seLightness * 3 + neLightness >> 3) >> 8);
						pixels[pixelOffset + 1] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
						lig = 0xFF - ((seLightness + neLightness >> 2) * (seLightness + neLightness >> 2) >> 8);
						pixels[pixelOffset + 2] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
						lig = 0xFF - ((seLightness + neLightness * 3 >> 3) * (seLightness + neLightness * 3 >> 3) >> 8);
						pixels[pixelOffset + 3] = ((rgb & 0xFF00FF) * lig & ~0xFF00FF) + ((rgb & 0xFF00) * lig & 0xFF0000) >> 8;
					}
					seLightness += southDeltaLightness;
					neLightness += northDeltaLightness;

					pixelOffset += 512;
				}
			}
			else if (rgb != 0)
			{
				for (int i = 0; i < 4; i++)
				{
					pixels[pixelOffset] = rgb;
					pixels[pixelOffset + 1] = rgb;
					pixels[pixelOffset + 2] = rgb;
					pixels[pixelOffset + 3] = rgb;
					pixelOffset += 512;
				}
			}
			return;
		}

		SceneTileModel sceneTileModel = tile.getSceneTileModel();
		if (sceneTileModel != null)
		{
			int shape = sceneTileModel.getShape();
			int rotation = sceneTileModel.getRotation();
			int overlayRgb = sceneTileModel.getModelOverlay();
			int underlayRgb = sceneTileModel.getModelUnderlay();
			int[] points = getTileShape2D()[shape];
			int[] indices = getTileRotation2D()[rotation];

			int shapeOffset = 0;

			if (sceneTileModel.getOverlaySwColor() != INVALID_HSL_COLOR)
			{
				// hue and saturation
				int hs = sceneTileModel.getOverlaySwColor() & ~0x7F;
				int seLightness = sceneTileModel.getOverlaySeColor() & 0x7F;
				int neLightness = sceneTileModel.getOverlayNeColor() & 0x7F;
				int southDeltaLightness = (sceneTileModel.getOverlaySwColor() & 0x7F) - seLightness;
				int northDeltaLightness = (sceneTileModel.getOverlayNwColor() & 0x7F) - neLightness;
				seLightness <<= 2;
				neLightness <<= 2;
				for (int i = 0; i < 4; i++)
				{
					if (sceneTileModel.getTriangleTextureId() == null)
					{
						if (points[indices[shapeOffset++]] != 0)
						{
							pixels[pixelOffset] = Rasterizer3D.hslToRgb[hs | (seLightness >> 2)];
						}
						if (points[indices[shapeOffset++]] != 0)
						{
							pixels[pixelOffset + 1] = Rasterizer3D.hslToRgb[hs | (seLightness * 3 + neLightness >> 4)];
						}
						if (points[indices[shapeOffset++]] != 0)
						{
							pixels[pixelOffset + 2] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness >> 3)];
						}
						if (points[indices[shapeOffset++]] != 0)
						{
							pixels[pixelOffset + 3] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness * 3 >> 4)];
						}
					}
					else
					{
						if (points[indices[shapeOffset++]] != 0)
						{
							int lig = 0xFF - ((seLightness >> 1) * (seLightness >> 1) >> 8);
							pixels[pixelOffset] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
									((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
						}
						if (points[indices[shapeOffset++]] != 0)
						{
							int lig = 0xFF - ((seLightness * 3 + neLightness >> 3) *
									(seLightness * 3 + neLightness >> 3) >> 8);
							pixels[pixelOffset + 1] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
									((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
						}
						if (points[indices[shapeOffset++]] != 0)
						{
							int lig = 0xFF - ((seLightness + neLightness >> 2) *
									(seLightness + neLightness >> 2) >> 8);
							pixels[pixelOffset + 2] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
									((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
						}
						if (points[indices[shapeOffset++]] != 0)
						{
							int lig = 0xFF - ((seLightness + neLightness * 3 >> 3) *
									(seLightness + neLightness * 3 >> 3) >> 8);
							pixels[pixelOffset + 3] = ((overlayRgb & 0xFF00FF) * lig & ~0xFF00FF) +
									((overlayRgb & 0xFF00) * lig & 0xFF0000) >> 8;
						}
					}
					seLightness += southDeltaLightness;
					neLightness += northDeltaLightness;

					pixelOffset += 512;
				}
				if (underlayRgb != 0 && sceneTileModel.getUnderlaySwColor() != INVALID_HSL_COLOR)
				{
					pixelOffset -= 512 << 2;
					shapeOffset -= 16;
					hs = sceneTileModel.getUnderlaySwColor() & ~0x7F;
					seLightness = sceneTileModel.getUnderlaySeColor() & 0x7F;
					neLightness = sceneTileModel.getUnderlayNeColor() & 0x7F;
					southDeltaLightness = (sceneTileModel.getUnderlaySwColor() & 0x7F) - seLightness;
					northDeltaLightness = (sceneTileModel.getUnderlayNwColor() & 0x7F) - neLightness;
					seLightness <<= 2;
					neLightness <<= 2;
					for (int i = 0; i < 4; i++)
					{
						if (points[indices[shapeOffset++]] == 0)
						{
							pixels[pixelOffset] = Rasterizer3D.hslToRgb[hs | (seLightness >> 2)];
						}
						if (points[indices[shapeOffset++]] == 0)
						{
							pixels[pixelOffset + 1] = Rasterizer3D.hslToRgb[hs | (seLightness * 3 + neLightness >> 4)];
						}
						if (points[indices[shapeOffset++]] == 0)
						{
							pixels[pixelOffset + 2] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness >> 3)];
						}
						if (points[indices[shapeOffset++]] == 0)
						{
							pixels[pixelOffset + 3] = Rasterizer3D.hslToRgb[hs | (seLightness + neLightness * 3 >> 4)];
						}
						seLightness += southDeltaLightness;
						neLightness += northDeltaLightness;

						pixelOffset += 512;
					}
				}
			}
			else if (underlayRgb != 0)
			{
				for (int i = 0; i < 4; i++)
				{
					pixels[pixelOffset] = points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
					pixels[pixelOffset + 1] =
							points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
					pixels[pixelOffset + 2] =
							points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
					pixels[pixelOffset + 3] =
							points[indices[shapeOffset++]] != 0 ? overlayRgb : underlayRgb;
					pixelOffset += 512;
				}
			}
			else
			{
				for (int i = 0; i < 4; i++)
				{
					if (points[indices[shapeOffset++]] != 0)
					{
						pixels[pixelOffset] = overlayRgb;
					}
					if (points[indices[shapeOffset++]] != 0)
					{
						pixels[pixelOffset + 1] = overlayRgb;
					}
					if (points[indices[shapeOffset++]] != 0)
					{
						pixels[pixelOffset + 2] = overlayRgb;
					}
					if (points[indices[shapeOffset++]] != 0)
					{
						pixels[pixelOffset + 3] = overlayRgb;
					}
					pixelOffset += 512;
				}
			}
		}
	}

	public void drawTileMinimapSD(int pixels[], int drawIndex, int zLoc, int xLoc, int yLoc) {
		int leftOverWidth = 512;// was parameter
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return;
		SimpleTile simpleTile = tile.mySimpleTile;
		if (simpleTile != null) {
			int tileRGB = simpleTile.getColourRGB();
			if (tileRGB == 0)
				return;
			for (int i = 0; i < 4; i++) {
				pixels[drawIndex] = tileRGB;
				pixels[drawIndex + 1] = tileRGB;
				pixels[drawIndex + 2] = tileRGB;
				pixels[drawIndex + 3] = tileRGB;
				drawIndex += leftOverWidth;
			}
			return;
		}
		ShapedTile shapedTile = tile.myShapedTile;

		if (shapedTile == null) {
			return;
		}

		int shape = shapedTile.shape;
		int rotation = shapedTile.rotation;
		int underlayRGB = shapedTile.colourRGB;
		int overlayRGB = shapedTile.colourRGBA;
		int shapePoints[] = tileVertices[shape];
		int shapePointIndices[] = tileVertexIndices[rotation];
		int shapePtr = 0;
		if (underlayRGB != 0) {
			for (int i = 0; i < 4; i++) {
				pixels[drawIndex] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				pixels[drawIndex + 1] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				pixels[drawIndex + 2] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				pixels[drawIndex + 3] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				drawIndex += leftOverWidth;
			}
			return;
		}
		for (int i = 0; i < 4; i++) {
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex] = overlayRGB;
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex + 1] = overlayRGB;
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex + 2] = overlayRGB;
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex + 3] = overlayRGB;
			drawIndex += leftOverWidth;
		}
	}


	public static void buildVisibilityMap(int i, int j, int k, int l, int ai[]) {
		xMin = 0;
		yMin = 0;
		xMax = k;
		yMax = l;
		viewportHalfWidth = k / 2;
		viewportHalfHeight = l / 2;
		boolean aflag[][][][] = new boolean[9][32][53][53];
		for (int i1 = 128; i1 <= 384; i1 += 32) {
			for (int j1 = 0; j1 < 2048; j1 += 64) {
				camUpDownY = Model.SINE[i1];
				camUpDownX = Model.COSINE[i1];
				camLeftRightY = Model.SINE[j1];
				camLeftRightX = Model.COSINE[j1];
				int l1 = (i1 - 128) / 32;
				int j2 = j1 / 64;
				for (int l2 = -26; l2 <= 26; l2++) {
					for (int j3 = -26; j3 <= 26; j3++) {
						int k3 = l2 * 128;
						int i4 = j3 * 128;
						boolean flag2 = false;
						for (int k4 = -i; k4 <= j; k4 += 128) {
							if (!method311(ai[l1] + k4, i4, k3))
								continue;
							flag2 = true;
							break;
						}

						aflag[l1][j2][l2 + 25 + 1][j3 + 25 + 1] = flag2;
					}

				}

			}

		}

		for (int k1 = 0; k1 < 8; k1++) {
			for (int i2 = 0; i2 < 32; i2++) {
				for (int k2 = -25; k2 < 25; k2++) {
					for (int i3 = -25; i3 < 25; i3++) {
						boolean flag1 = false;
						label0:
						for (int l3 = -1; l3 <= 1; l3++) {
							for (int j4 = -1; j4 <= 1; j4++) {
								if (aflag[k1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
									flag1 = true;
								else if (aflag[k1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
									flag1 = true;
								else if (aflag[k1 + 1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1]) {
									flag1 = true;
								} else {
									if (!aflag[k1 + 1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
										continue;
									flag1 = true;
								}
								break label0;
							}

						}

						visibilityMap[k1][i2][k2 + 25][i3 + 25] = flag1;
					}

				}

			}

		}

	}

	private static boolean method311(int i, int j, int k) {
		int l = j * camLeftRightY + k * camLeftRightX >> 16;
		int i1 = j * camLeftRightX - k * camLeftRightY >> 16;
		int j1 = i * camUpDownY + i1 * camUpDownX >> 16;
		int k1 = i * camUpDownX - i1 * camUpDownY >> 16;
		if (j1 < 50 || j1 > 3500)
			return false;
		int l1 = viewportHalfWidth + l * Rasterizer3D.fieldOfView / j1;
		int i2 = viewportHalfHeight + k1 * Rasterizer3D.fieldOfView / j1;
		return l1 >= xMin && l1 <= xMax && i2 >= yMin && i2 <= yMax;
	}

	public void method312(int i, int j) {
		clicked = true;
		clickScreenX = j;
		clickScreenY = i;
		clickedTileX = -1;
		clickedTileY = -1;
	}

	private static final int INVALID_HSL_COLOR = 12345678;
	private static final int DEFAULT_DISTANCE = 25;
	private static final int PITCH_LOWER_LIMIT = 128;
	private static final int PITCH_UPPER_LIMIT = 383;

	/**
	 * Renders the terrain.
	 * The coordinates use the WorldCoordinate Axes but the modelWorld coordinates.
	 * @param cameraXPos The cameraViewpoint's X-coordinate.
	 * @param cameraYPos The cameraViewpoint's Y-coordinate.
	 * @param camAngleXY The cameraAngle in the XY-plain.
	 * @param cameraZPos The cameraViewpoint's X-coordinate.
	 * @param planeZ The plain the camera's looking at.
	 * @param camAngleZ  The cameraAngle on the Z-axis.
	 */
	public void render(int cameraXPos, int cameraYPos, int camAngleXY, int cameraZPos, int planeZ, int camAngleZ) {

		final DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();
		if (drawCallbacks != null)
		{
			Client.instance.getDrawCallbacks().drawScene(cameraXPos, cameraZPos, cameraYPos, camAngleZ, camAngleXY, planeZ);
		}

		final boolean isGpu = Client.instance.isGpu();
		final boolean checkClick = Client.instance.isCheckClick();
		final boolean menuOpen = Client.instance.isMenuOpen();

		if (!menuOpen && !checkClick)
		{
			Client.instance.getScene().menuOpen(Client.instance.getPlane(), Client.instance.getMouseX() - Client.instance.getViewportXOffset(), Client.instance.getMouseY() - Client.instance.getViewportYOffset(), false);
		}

		if (!isGpu && skyboxColor != 0)
		{
			Client.instance.rasterizerFillRectangle(
					Client.instance.getViewportXOffset(),
					Client.instance.getViewportYOffset(),
					Client.instance.getViewportWidth(),
					Client.instance.getViewportHeight(),
					skyboxColor
			);
		}

		final int maxX = getMaxX();
		final int maxY = getMaxY();
		final int maxZ = getMaxZ();

		final int minLevel = getMinLevel();

		final RSTile[][][] tiles = getTiles();
		final int distance = isGpu ? drawDistance : DEFAULT_DISTANCE;

		if (cameraXPos < 0)
		{
			cameraXPos = 0;
		}
		else if (cameraXPos >= maxX * Perspective.LOCAL_TILE_SIZE)
		{
			cameraXPos = maxX * Perspective.LOCAL_TILE_SIZE - 1;
		}

		if (cameraYPos < 0)
		{
			cameraYPos = 0;
		}
		else if (cameraYPos >= maxZ * Perspective.LOCAL_TILE_SIZE)
		{
			cameraYPos = maxZ * Perspective.LOCAL_TILE_SIZE - 1;
		}


		// we store the uncapped pitch for setting camera angle for the pitch relaxer
		// we still have to cap the pitch in order to access the visibility map, though
		int realPitch = camAngleZ;
		if (camAngleZ < PITCH_LOWER_LIMIT)
		{
			camAngleZ = PITCH_LOWER_LIMIT;
		}
		else if (camAngleZ > PITCH_UPPER_LIMIT)
		{
			camAngleZ = PITCH_UPPER_LIMIT;
		}
		if (!pitchRelaxEnabled)
		{
			realPitch = camAngleZ;
		}


		Client.instance.setCycle(Client.instance.getCycle() + 1);
		Client.instance.setPitchSin(Perspective.SINE[realPitch]);
		Client.instance.setPitchCos(Perspective.COSINE[realPitch]);
		Client.instance.setYawSin(Perspective.SINE[camAngleXY]);
		Client.instance.setYawCos(Perspective.COSINE[camAngleXY]);


		final int[][][] tileHeights = Client.instance.getTileHeights();
		boolean[][] renderArea = Client.instance.getVisibilityMaps()[(camAngleZ - 128) / 32][camAngleXY / 64];
		Client.instance.setRenderArea(renderArea);

		Client.instance.setCameraX2(cameraXPos);
		Client.instance.setCameraY2(cameraZPos);
		Client.instance.setCameraZ2(cameraYPos);

		int screenCenterX = cameraXPos / Perspective.LOCAL_TILE_SIZE;
		int screenCenterZ = cameraYPos / Perspective.LOCAL_TILE_SIZE;

		Client.instance.setScreenCenterX(screenCenterX);
		Client.instance.setScreenCenterZ(screenCenterZ);
		Client.instance.setScenePlane(planeZ);

		int minTileX = screenCenterX - distance;
		if (minTileX < 0)
		{
			minTileX = 0;
		}

		int minTileZ = screenCenterZ - distance;
		if (minTileZ < 0)
		{
			minTileZ = 0;
		}

		int maxTileX = screenCenterX + distance;
		if (maxTileX > maxX)
		{
			maxTileX = maxX;
		}

		int maxTileZ = screenCenterZ + distance;
		if (maxTileZ > maxZ)
		{
			maxTileZ = maxZ;
		}

		Client.instance.setMinTileX(minTileX);
		Client.instance.setMinTileZ(minTileZ);
		Client.instance.setMaxTileX(maxTileX);
		Client.instance.setMaxTileZ(maxTileZ);

		updateOccluders();

		Client.instance.setTileUpdateCount(0);

		if (roofRemovalMode != 0)
		{
			tilesToRemove.clear();
			RSPlayer localPlayer = Client.instance.getLocalPlayer();
			if (localPlayer != null && (roofRemovalMode & ROOF_FLAG_POSITION) != 0)
			{
				LocalPoint localLocation = localPlayer.getLocalLocation();
				if (localLocation.isInScene())
				{
					tilesToRemove.add(tileArray[Client.instance.getPlane()][localLocation.getSceneX()][localLocation.getSceneY()]);
				}
			}

			if (hoverX >= 0 && hoverX < 104 && hoverY >= 0 && hoverY < 104 && (roofRemovalMode & ROOF_FLAG_HOVERED) != 0)
			{
				tilesToRemove.add(tileArray[Client.instance.getPlane()][hoverX][hoverY]);
			}

			LocalPoint localDestinationLocation = Client.instance.getLocalDestinationLocation();
			if (localDestinationLocation != null && localDestinationLocation.isInScene() && (roofRemovalMode & ROOF_FLAG_DESTINATION) != 0)
			{
				tilesToRemove.add(tileArray[Client.instance.getPlane()][localDestinationLocation.getSceneX()][localDestinationLocation.getSceneY()]);
			}

			if (Client.instance.getCameraPitch() < 310 && (roofRemovalMode & ROOF_FLAG_BETWEEN) != 0 && localPlayer != null)
			{
				int playerX = localPlayer.getX() >> 7;
				int playerY = localPlayer.getY() >> 7;
				int var29 = Client.instance.getCameraX() >> 7;
				int var30 = Client.instance.getCameraY() >> 7;
				if (playerX >= 0 && playerY >= 0 && var29 >= 0 && var30 >= 0 && playerX < 104 && playerY < 104 && var29 < 104 && var30 < 104)
				{
					int var31 = Math.abs(playerX - var29);
					int var32 = Integer.compare(playerX, var29);
					int var33 = -Math.abs(playerY - var30);
					int var34 = Integer.compare(playerY, var30);
					int var35 = var31 + var33;

					while (var29 != playerX || var30 != playerY)
					{
						if (blocking(Client.instance.getPlane(), var29, var30))
						{
							tilesToRemove.add(tileArray[Client.instance.getPlane()][var29][var30]);
						}

						int var36 = 2 * var35;
						if (var36 >= var33)
						{
							var35 += var33;
							var29 += var32;
						}
						else
						{
							var35 += var31;
							var30 += var34;
						}
					}
				}
			}
		}

		if (!menuOpen)
		{
			Client.instance.setHoverTileX(-1);
			Client.instance.setHoverTileY(-1);
		}

		for (int z = minLevel; z < maxY; ++z)
		{
			RSTile[][] planeTiles = tileArray[z];



			for (int x = minTileX; x < maxTileX; ++x)
			{
				for (int y = minTileZ; y < maxTileZ; ++y)
				{
					RSTile tile = planeTiles[x][y];
					if (tile != null)
					{

						RSTile var30 = tileArray[Client.instance.getPlane()][x][y];
						if (tile.getPhysicalLevel() > planeZ && roofRemovalMode == 0
								|| !isGpu && !renderArea[x - screenCenterX + DEFAULT_DISTANCE][y - screenCenterZ + DEFAULT_DISTANCE]
								&& tileHeights[z][x][y] - cameraYPos < 2000
								|| roofRemovalMode != 0 && Client.instance.getPlane() < tile.getPhysicalLevel()
								&& tilesToRemove.contains(var30))
						{
							tile.setDraw(false);
							tile.setVisible(false);
							tile.setWallCullDirection(0);
						}
						else
						{
							tile.setDraw(true);
							tile.setVisible(true);
							tile.setDrawEntities(true);
							Client.instance.setTileUpdateCount(Client.instance.getTileUpdateCount() + 1);
						}
					}
				}
			}
		}

		for (int z = minLevel; z < maxY; ++z)
		{
			RSTile[][] planeTiles = tileArray[z];

			for (int x = -distance; x <= 0; ++x)
			{
				int var10 = x + screenCenterX;
				int var16 = screenCenterX - x;
				if (var10 >= minTileX || var16 < maxTileX)
				{
					for (int y = -distance; y <= 0; ++y)
					{
						int var13 = y + screenCenterZ;
						int var14 = screenCenterZ - y;
						if (var10 >= minTileX)
						{
							if (var13 >= minTileZ)
							{
								RSTile tile = planeTiles[var10][var13];
								if (tile != null && tile.isDraw())
								{
									draw(tile, true);
								}
							}

							if (var14 < maxTileZ)
							{
								RSTile tile = planeTiles[var10][var14];
								if (tile != null && tile.isDraw())
								{
									draw(tile, true);
								}
							}
						}

						if (var16 < maxTileX)
						{
							if (var13 >= minTileZ)
							{
								RSTile tile = planeTiles[var16][var13];
								if (tile != null && tile.isDraw())
								{
									draw(tile, true);
								}
							}

							if (var14 < maxTileZ)
							{
								RSTile tile = planeTiles[var16][var14];
								if (tile != null && tile.isDraw())
								{
									draw(tile, true);
								}
							}
						}

						if (Client.instance.getTileUpdateCount() == 0)
						{
							if (!isGpu && (Client.instance.getOculusOrbState() != 0 && !Client.instance.getComplianceValue("orbInteraction")))
							{
								Client.instance.setEntitiesAtMouseCount(0);
							}
							Client.instance.setCheckClick(false);
							Client.instance.getCallbacks().drawScene();

							if (Client.instance.getDrawCallbacks() != null)
							{
								Client.instance.getDrawCallbacks().postDrawScene();
							}

							return;
						}
					}
				}
			}
		}

		outer:
		for (int z = minLevel; z < maxY; ++z)
		{
			RSTile[][] planeTiles = tiles[z];

			for (int x = -distance; x <= 0; ++x)
			{
				int var10 = x + screenCenterX;
				int var16 = screenCenterX - x;
				if (var10 >= minTileX || var16 < maxTileX)
				{
					for (int y = -distance; y <= 0; ++y)
					{
						int var13 = y + screenCenterZ;
						int var14 = screenCenterZ - y;
						if (var10 >= minTileX)
						{
							if (var13 >= minTileZ)
							{
								RSTile tile = planeTiles[var10][var13];
								if (tile != null && tile.isDraw())
								{
									draw(tile, false);
								}
							}

							if (var14 < maxTileZ)
							{
								RSTile tile = planeTiles[var10][var14];
								if (tile != null && tile.isDraw())
								{
									draw(tile, false);
								}
							}
						}

						if (var16 < maxTileX)
						{
							if (var13 >= minTileZ)
							{
								RSTile tile = planeTiles[var16][var13];
								if (tile != null && tile.isDraw())
								{
									draw(tile, false);
								}
							}

							if (var14 < maxTileZ)
							{
								RSTile tile = planeTiles[var16][var14];
								if (tile != null && tile.isDraw())
								{
									draw(tile, false);
								}
							}
						}

						if (Client.instance.getTileUpdateCount() == 0)
						{
							// exit the loop early and go straight to "if (!isGpu && (Client.instance..."
							break outer;
						}
					}
				}
			}
		}

		if (!isGpu && (Client.instance.getOculusOrbState() != 0 && !Client.instance.getComplianceValue("orbInteraction")))
		{
			Client.instance.setEntitiesAtMouseCount(0);
		}
		Client.instance.setCheckClick(false);
		Client.instance.getCallbacks().drawScene();
		if (Client.instance.getDrawCallbacks() != null)
		{
			Client.instance.getDrawCallbacks().postDrawScene();
		}
	}

	public static boolean blocking(int plane, int x, int y)
	{
		return (Client.instance.getTileSettings()[plane][x][y] & 4) != 0;
	}


	private void drawTile(Tile tile, boolean flag) {
		tileDeque.insertHead(tile);
		do {
			Tile class30_sub3_1;
			do {
				class30_sub3_1 = (Tile) tileDeque.popHead();
				if (class30_sub3_1 == null)
					return;
			} while (!class30_sub3_1.aBoolean1323);
			int i = class30_sub3_1.x;
			int j = class30_sub3_1.y;
			int k = class30_sub3_1.z1AnInt1307;
			int l = class30_sub3_1.anInt1310;
			Tile aclass30_sub3[][] = tileArray[k];
			if (class30_sub3_1.aBoolean1322) {
				if (flag) {
					if (k > 0) {
						Tile class30_sub3_2 = tileArray[k - 1][i][j];
						if (class30_sub3_2 != null && class30_sub3_2.aBoolean1323)
							continue;
					}
					if (i <= screenCenterX && i > minTileX) {
						Tile class30_sub3_3 = aclass30_sub3[i - 1][j];
						if (class30_sub3_3 != null && class30_sub3_3.aBoolean1323 && (class30_sub3_3.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 1) == 0))
							continue;
					}
					if (i >= screenCenterX && i < maxTileX - 1) {
						Tile class30_sub3_4 = aclass30_sub3[i + 1][j];
						if (class30_sub3_4 != null && class30_sub3_4.aBoolean1323 && (class30_sub3_4.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 4) == 0))
							continue;
					}
					if (j <= screenCenterZ && j > minTileZ) {
						Tile class30_sub3_5 = aclass30_sub3[i][j - 1];
						if (class30_sub3_5 != null && class30_sub3_5.aBoolean1323 && (class30_sub3_5.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 8) == 0))
							continue;
					}
					if (j >= screenCenterZ && j < maxTileZ - 1) {
						Tile class30_sub3_6 = aclass30_sub3[i][j + 1];
						if (class30_sub3_6 != null && class30_sub3_6.aBoolean1323 && (class30_sub3_6.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 2) == 0))
							continue;
					}
				} else {
					flag = true;
				}
				class30_sub3_1.aBoolean1322 = false;
				if (class30_sub3_1.firstFloorTile != null) {
					Tile class30_sub3_7 = class30_sub3_1.firstFloorTile;
					if (class30_sub3_7.mySimpleTile != null) {
						if (!method320(0, i, j))
							drawTileUnderlay(class30_sub3_7.mySimpleTile, 0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i, j);
					} else if (class30_sub3_7.myShapedTile != null && !method320(0, i, j))
						drawTileOverlay(i, camUpDownY, camLeftRightY, class30_sub3_7.myShapedTile, camUpDownX, j, camLeftRightX);
					Wall class10 = class30_sub3_7.wallObject;
					if (class10 != null)
						class10.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class10.tileHeights - xCameraPos, class10.zLoc - zCameraPos, class10.xPos - yCameraPos, class10.uid);
					for (int i2 = 0; i2 < class30_sub3_7.gameObjectIndex; i2++) {
						GameObject class28 = class30_sub3_7.gameObjects[i2];
						if (class28 != null)
							class28.renderable.renderAtPoint(class28.turnValue, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class28.xPos - xCameraPos, class28.tileHeight - zCameraPos, class28.yPos - yCameraPos, class28.uid);
					}

				}
				boolean flag1 = false;
				if (class30_sub3_1.mySimpleTile != null) {
					if (!method320(l, i, j)) {
						flag1 = true;
						drawTileUnderlay(class30_sub3_1.mySimpleTile, l, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i, j);
					}
				} else if (class30_sub3_1.myShapedTile != null && !method320(l, i, j)) {
					flag1 = true;
					drawTileOverlay(i, camUpDownY, camLeftRightY, class30_sub3_1.myShapedTile, camUpDownX, j, camLeftRightX);
				}
				int j1 = 0;
				int j2 = 0;
				Wall class10_3 = class30_sub3_1.wallObject;
				WallDecoration class26_1 = class30_sub3_1.wallDecoration;
				if (class10_3 != null || class26_1 != null) {
					if (screenCenterX == i)
						j1++;
					else if (screenCenterX < i)
						j1 += 2;
					if (screenCenterZ == j)
						j1 += 3;
					else if (screenCenterZ > j)
						j1 += 6;
					j2 = anIntArray478[j1];
					class30_sub3_1.anInt1328 = anIntArray480[j1];
				}
				if (class10_3 != null) {
					if ((class10_3.orientation1 & anIntArray479[j1]) != 0) {
						if (class10_3.orientation1 == 16) {
							class30_sub3_1.wallCullDirection = 3;
							class30_sub3_1.anInt1326 = anIntArray481[j1];
							class30_sub3_1.anInt1327 = 3 - class30_sub3_1.anInt1326;
						} else if (class10_3.orientation1 == 32) {
							class30_sub3_1.wallCullDirection = 6;
							class30_sub3_1.anInt1326 = anIntArray482[j1];
							class30_sub3_1.anInt1327 = 6 - class30_sub3_1.anInt1326;
						} else if (class10_3.orientation1 == 64) {
							class30_sub3_1.wallCullDirection = 12;
							class30_sub3_1.anInt1326 = anIntArray483[j1];
							class30_sub3_1.anInt1327 = 12 - class30_sub3_1.anInt1326;
						} else {
							class30_sub3_1.wallCullDirection = 9;
							class30_sub3_1.anInt1326 = anIntArray484[j1];
							class30_sub3_1.anInt1327 = 9 - class30_sub3_1.anInt1326;
						}
					} else {
						class30_sub3_1.wallCullDirection = 0;
					}
					if ((class10_3.orientation1 & j2) != 0 && !method321(l, i, j, class10_3.orientation1))
						class10_3.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class10_3.tileHeights - xCameraPos, class10_3.zLoc - zCameraPos, class10_3.xPos - yCameraPos, class10_3.uid);
					if ((class10_3.orientation2 & j2) != 0 && !method321(l, i, j, class10_3.orientation2))
						class10_3.renderable2.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class10_3.tileHeights - xCameraPos, class10_3.zLoc - zCameraPos, class10_3.xPos - yCameraPos, class10_3.uid);
				}
				if (class26_1 != null && !method322(l, i, j, class26_1.renderable.modelBaseY))
					if ((class26_1.orientation & j2) != 0)
						class26_1.renderable.renderAtPoint(class26_1.orientation2, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class26_1.tileHeights - xCameraPos, class26_1.zLoc - zCameraPos, class26_1.xPos - yCameraPos, class26_1.uid);
					else if ((class26_1.orientation & 0x300) != 0) {
						int j4 = class26_1.tileHeights - xCameraPos;
						int l5 = class26_1.zLoc - zCameraPos;
						int k6 = class26_1.xPos - yCameraPos;
						int i8 = class26_1.orientation2;
						int k9;
						if (i8 == 1 || i8 == 2)
							k9 = -j4;
						else
							k9 = j4;
						int k10;
						if (i8 == 2 || i8 == 3)
							k10 = -k6;
						else
							k10 = k6;
						if ((class26_1.orientation & 0x100) != 0 && k10 < k9) {
							int i11 = j4 + anIntArray463[i8];
							int k11 = k6 + anIntArray464[i8];
							class26_1.renderable.renderAtPoint(i8 * 512 + 256, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i11, l5, k11, class26_1.uid);
						}
						if ((class26_1.orientation & 0x200) != 0 && k10 > k9) {
							int j11 = j4 + anIntArray465[i8];
							int l11 = k6 + anIntArray466[i8];
							class26_1.renderable.renderAtPoint(i8 * 512 + 1280 & 0x7ff, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, j11, l5, l11, class26_1.uid);
						}
					}
				if (flag1) {
					GroundDecoration class49 = class30_sub3_1.groundDecoration;
					if (class49 != null)
						class49.renderable.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class49.tileHeights - xCameraPos, class49.zLoc - zCameraPos, class49.xPos - yCameraPos, class49.uid);
					GroundItemTile object4_1 = class30_sub3_1.groundItemTile;
					if (object4_1 != null && object4_1.itemDropHeight == 0) {
						if (object4_1.lowerNode != null)
							object4_1.lowerNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, object4_1.tileHeights - xCameraPos, object4_1.zLoc - zCameraPos, object4_1.xPos - yCameraPos, object4_1.uid);
						if (object4_1.middleNode != null)
							object4_1.middleNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, object4_1.tileHeights - xCameraPos, object4_1.zLoc - zCameraPos, object4_1.xPos - yCameraPos, object4_1.uid);
						if (object4_1.topNode != null)
							object4_1.topNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, object4_1.tileHeights - xCameraPos, object4_1.zLoc - zCameraPos, object4_1.xPos - yCameraPos, object4_1.uid);
					}
				}
				int k4 = class30_sub3_1.totalTiledObjectMask;
				if (k4 != 0) {
					if (i < screenCenterX && (k4 & 4) != 0) {
						Tile class30_sub3_17 = aclass30_sub3[i + 1][j];
						if (class30_sub3_17 != null && class30_sub3_17.aBoolean1323)
							tileDeque.insertHead(class30_sub3_17);
					}
					if (j < screenCenterZ && (k4 & 2) != 0) {
						Tile class30_sub3_18 = aclass30_sub3[i][j + 1];
						if (class30_sub3_18 != null && class30_sub3_18.aBoolean1323)
							tileDeque.insertHead(class30_sub3_18);
					}
					if (i > screenCenterX && (k4 & 1) != 0) {
						Tile class30_sub3_19 = aclass30_sub3[i - 1][j];
						if (class30_sub3_19 != null && class30_sub3_19.aBoolean1323)
							tileDeque.insertHead(class30_sub3_19);
					}
					if (j > screenCenterZ && (k4 & 8) != 0) {
						Tile class30_sub3_20 = aclass30_sub3[i][j - 1];
						if (class30_sub3_20 != null && class30_sub3_20.aBoolean1323)
							tileDeque.insertHead(class30_sub3_20);
					}
				}
			}
			if (class30_sub3_1.wallCullDirection != 0) {
				boolean flag2 = true;
				for (int k1 = 0; k1 < class30_sub3_1.gameObjectIndex; k1++) {
					if (class30_sub3_1.gameObjects[k1].anInt528 == cycle || (class30_sub3_1.tiledObjectMasks[k1] & class30_sub3_1.wallCullDirection) != class30_sub3_1.anInt1326)
						continue;
					flag2 = false;
					break;
				}

				if (flag2) {
					Wall class10_1 = class30_sub3_1.wallObject;
					if (!method321(l, i, j, class10_1.orientation1))
						class10_1.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class10_1.tileHeights - xCameraPos, class10_1.zLoc - zCameraPos, class10_1.xPos - yCameraPos, class10_1.uid);
					class30_sub3_1.wallCullDirection = 0;
				}
			}
			if (class30_sub3_1.aBoolean1324)
				try {
					int i1 = class30_sub3_1.gameObjectIndex;
					class30_sub3_1.aBoolean1324 = false;
					int l1 = 0;
					label0:
					for (int k2 = 0; k2 < i1; k2++) {
						GameObject class28_1 = class30_sub3_1.gameObjects[k2];
						if (class28_1.anInt528 == cycle)
							continue;
						for (int k3 = class28_1.xLocLow; k3 <= class28_1.xLocHigh; k3++) {
							for (int l4 = class28_1.yLocHigh; l4 <= class28_1.yLocLow; l4++) {
								Tile class30_sub3_21 = aclass30_sub3[k3][l4];
								if (class30_sub3_21.aBoolean1322) {
									class30_sub3_1.aBoolean1324 = true;
								} else {
									if (class30_sub3_21.wallCullDirection == 0)
										continue;
									int l6 = 0;
									if (k3 > class28_1.xLocLow)
										l6++;
									if (k3 < class28_1.xLocHigh)
										l6 += 4;
									if (l4 > class28_1.yLocHigh)
										l6 += 8;
									if (l4 < class28_1.yLocLow)
										l6 += 2;
									if ((l6 & class30_sub3_21.wallCullDirection) != class30_sub3_1.anInt1327)
										continue;
									class30_sub3_1.aBoolean1324 = true;
								}
								continue label0;
							}

						}

						interactableObjects[l1++] = class28_1;
						int i5 = screenCenterX - class28_1.xLocLow;
						int i6 = class28_1.xLocHigh - screenCenterX;
						if (i6 > i5)
							i5 = i6;
						int i7 = screenCenterZ - class28_1.yLocHigh;
						int j8 = class28_1.yLocLow - screenCenterZ;
						if (j8 > i7)
							class28_1.anInt527 = i5 + j8;
						else
							class28_1.anInt527 = i5 + i7;
					}

					while (l1 > 0) {
						int i3 = -50;
						int l3 = -1;
						for (int j5 = 0; j5 < l1; j5++) {
							GameObject class28_2 = interactableObjects[j5];
							if (class28_2.anInt528 != cycle)
								if (class28_2.anInt527 > i3) {
									i3 = class28_2.anInt527;
									l3 = j5;
								} else if (class28_2.anInt527 == i3) {
									int j7 = class28_2.xPos - xCameraPos;
									int k8 = class28_2.yPos - yCameraPos;
									int l9 = interactableObjects[l3].xPos - xCameraPos;
									int l10 = interactableObjects[l3].yPos - yCameraPos;
									if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10)
										l3 = j5;
								}
						}

						if (l3 == -1)
							break;
						GameObject class28_3 = interactableObjects[l3];
						class28_3.anInt528 = cycle;
						if (!method323(l, class28_3.xLocLow, class28_3.xLocHigh, class28_3.yLocHigh, class28_3.yLocLow, class28_3.renderable.modelBaseY))
							class28_3.renderable.renderAtPoint(class28_3.turnValue, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class28_3.xPos - xCameraPos, class28_3.tileHeight - zCameraPos, class28_3.yPos - yCameraPos, class28_3.uid);
						for (int k7 = class28_3.xLocLow; k7 <= class28_3.xLocHigh; k7++) {
							for (int l8 = class28_3.yLocHigh; l8 <= class28_3.yLocLow; l8++) {
								Tile class30_sub3_22 = aclass30_sub3[k7][l8];
								if (class30_sub3_22.wallCullDirection != 0)
									tileDeque.insertHead(class30_sub3_22);
								else if ((k7 != i || l8 != j) && class30_sub3_22.aBoolean1323)
									tileDeque.insertHead(class30_sub3_22);
							}

						}

					}
					if (class30_sub3_1.aBoolean1324)
						continue;
				} catch (Exception _ex) {
					class30_sub3_1.aBoolean1324 = false;
				}
			if (!class30_sub3_1.aBoolean1323 || class30_sub3_1.wallCullDirection != 0)
				continue;
			if (i <= screenCenterX && i > minTileX) {
				Tile class30_sub3_8 = aclass30_sub3[i - 1][j];
				if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
					continue;
			}
			if (i >= screenCenterX && i < maxTileX - 1) {
				Tile class30_sub3_9 = aclass30_sub3[i + 1][j];
				if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
					continue;
			}
			if (j <= screenCenterZ && j > minTileZ) {
				Tile class30_sub3_10 = aclass30_sub3[i][j - 1];
				if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
					continue;
			}
			if (j >= screenCenterZ && j < maxTileZ - 1) {
				Tile class30_sub3_11 = aclass30_sub3[i][j + 1];
				if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
					continue;
			}
			class30_sub3_1.aBoolean1323 = false;
			tileUpdateCount--;
			GroundItemTile object4 = class30_sub3_1.groundItemTile;
			if (object4 != null && object4.itemDropHeight != 0) {
				if (object4.lowerNode != null)
					object4.lowerNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, object4.tileHeights - xCameraPos, object4.zLoc - zCameraPos - object4.itemDropHeight, object4.xPos - yCameraPos, object4.uid);
				if (object4.middleNode != null)
					object4.middleNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, object4.tileHeights - xCameraPos, object4.zLoc - zCameraPos - object4.itemDropHeight, object4.xPos - yCameraPos, object4.uid);
				if (object4.topNode != null)
					object4.topNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, object4.tileHeights - xCameraPos, object4.zLoc - zCameraPos - object4.itemDropHeight, object4.xPos - yCameraPos, object4.uid);
			}
			if (class30_sub3_1.anInt1328 != 0) {
				WallDecoration class26 = class30_sub3_1.wallDecoration;
				if (class26 != null && !method322(l, i, j, class26.renderable.modelBaseY))
					if ((class26.orientation & class30_sub3_1.anInt1328) != 0)
						class26.renderable.renderAtPoint(class26.orientation2, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class26.tileHeights - xCameraPos, class26.zLoc - zCameraPos, class26.xPos - yCameraPos, class26.uid);
					else if ((class26.orientation & 0x300) != 0) {
						int l2 = class26.tileHeights - xCameraPos;
						int j3 = class26.zLoc - zCameraPos;
						int i4 = class26.xPos - yCameraPos;
						int k5 = class26.orientation2;
						int j6;
						if (k5 == 1 || k5 == 2)
							j6 = -l2;
						else
							j6 = l2;
						int l7;
						if (k5 == 2 || k5 == 3)
							l7 = -i4;
						else
							l7 = i4;
						if ((class26.orientation & 0x100) != 0 && l7 >= j6) {
							int i9 = l2 + anIntArray463[k5];
							int i10 = i4 + anIntArray464[k5];
							class26.renderable.renderAtPoint(k5 * 512 + 256, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i9, j3, i10, class26.uid);
						}
						if ((class26.orientation & 0x200) != 0 && l7 <= j6) {
							int j9 = l2 + anIntArray465[k5];
							int j10 = i4 + anIntArray466[k5];
							class26.renderable.renderAtPoint(k5 * 512 + 1280 & 0x7ff, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, j9, j3, j10, class26.uid);
						}
					}
				Wall class10_2 = class30_sub3_1.wallObject;
				if (class10_2 != null) {
					if ((class10_2.orientation2 & class30_sub3_1.anInt1328) != 0 && !method321(l, i, j, class10_2.orientation2))
						class10_2.renderable2.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class10_2.tileHeights - xCameraPos, class10_2.zLoc - zCameraPos, class10_2.xPos - yCameraPos, class10_2.uid);
					if ((class10_2.orientation1 & class30_sub3_1.anInt1328) != 0 && !method321(l, i, j, class10_2.orientation1))
						class10_2.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class10_2.tileHeights - xCameraPos, class10_2.zLoc - zCameraPos, class10_2.xPos - yCameraPos, class10_2.uid);
				}
			}
			if (k < sizeZ - 1) {
				Tile class30_sub3_12 = tileArray[k + 1][i][j];
				if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
					tileDeque.insertHead(class30_sub3_12);
			}
			if (i < screenCenterX) {
				Tile class30_sub3_13 = aclass30_sub3[i + 1][j];
				if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
					tileDeque.insertHead(class30_sub3_13);
			}
			if (j < screenCenterZ) {
				Tile class30_sub3_14 = aclass30_sub3[i][j + 1];
				if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
					tileDeque.insertHead(class30_sub3_14);
			}
			if (i > screenCenterX) {
				Tile class30_sub3_15 = aclass30_sub3[i - 1][j];
				if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
					tileDeque.insertHead(class30_sub3_15);
			}
			if (j > screenCenterZ) {
				Tile class30_sub3_16 = aclass30_sub3[i][j - 1];
				if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
					tileDeque.insertHead(class30_sub3_16);
			}
		} while (true);
	}

	private void drawTileUnderlaySD(SimpleTile simpleTile, int z, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y) {
		int l1;
		int i2 = l1 = (x << 7) - xCameraPos;
		int j2;
		int k2 = j2 = (y << 7) - yCameraPos;
		int l2;
		int i3 = l2 = i2 + 128;
		int j3;
		int k3 = j3 = k2 + 128;
		int l3 = heightMap[z][x][y] - zCameraPos;
		int i4 = heightMap[z][x + 1][y] - zCameraPos;
		int j4 = heightMap[z][x + 1][y + 1] - zCameraPos;
		int k4 = heightMap[z][x][y + 1] - zCameraPos;
		int l4 = k2 * yawSin + i2 * yawCos >> 16;
		k2 = k2 * yawCos - i2 * yawSin >> 16;
		i2 = l4;
		l4 = l3 * pitchCos - k2 * pitchSin >> 16;
		k2 = l3 * pitchSin + k2 * pitchCos >> 16;
		l3 = l4;
		if (k2 < 50)
			return;
		l4 = j2 * yawSin + i3 * yawCos >> 16;
		j2 = j2 * yawCos - i3 * yawSin >> 16;
		i3 = l4;
		l4 = i4 * pitchCos - j2 * pitchSin >> 16;
		j2 = i4 * pitchSin + j2 * pitchCos >> 16;
		i4 = l4;
		if (j2 < 50)
			return;
		l4 = k3 * yawSin + l2 * yawCos >> 16;
		k3 = k3 * yawCos - l2 * yawSin >> 16;
		l2 = l4;
		l4 = j4 * pitchCos - k3 * pitchSin >> 16;
		k3 = j4 * pitchSin + k3 * pitchCos >> 16;
		j4 = l4;
		if (k3 < 50)
			return;
		l4 = j3 * yawSin + l1 * yawCos >> 16;
		j3 = j3 * yawCos - l1 * yawSin >> 16;
		l1 = l4;
		l4 = k4 * pitchCos - j3 * pitchSin >> 16;
		j3 = k4 * pitchSin + j3 * pitchCos >> 16;
		k4 = l4;
		if (j3 < 50)
			return;
		int i5 = Rasterizer3D.originViewX + i2 * Rasterizer3D.fieldOfView / k2;
		int j5 = Rasterizer3D.originViewY + l3 * Rasterizer3D.fieldOfView / k2;
		int k5 = Rasterizer3D.originViewX + i3 * Rasterizer3D.fieldOfView / j2;
		int l5 = Rasterizer3D.originViewY + i4 * Rasterizer3D.fieldOfView / j2;
		int i6 = Rasterizer3D.originViewX + l2 * Rasterizer3D.fieldOfView / k3;
		int j6 = Rasterizer3D.originViewY + j4 * Rasterizer3D.fieldOfView / k3;
		int k6 = Rasterizer3D.originViewX + l1 * Rasterizer3D.fieldOfView / j3;
		int l6 = Rasterizer3D.originViewY + k4 * Rasterizer3D.fieldOfView / j3;
		Rasterizer3D.alpha = 0;
		if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
			Rasterizer3D.textureOutOfDrawingBounds = i6 < 0 || k6 < 0 || k5 < 0 || i6 > Rasterizer2D.lastX || k6 > Rasterizer2D.lastX || k5 > Rasterizer2D.lastX;
			if (clicked && inBounds(clickScreenX, clickScreenY, j6, l6, l5, i6, k6, k5)) {
				clickedTileX = x;
				clickedTileY = y;
			}
			if (inBounds(MouseHandler.mouseX, MouseHandler.mouseY, j6, l6, l5, i6, k6, k5)) {
				Client.instance.setHoverTileX(x);
				Client.instance.setHoverTileY(y);
			}
			if (simpleTile.getTexture() == -1) {
				if (simpleTile.getCenterColor() != 0xbc614e)
					Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor());
			} else if (!lowMem) {
				if (simpleTile.isFlat())
					Rasterizer3D.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.getTexture());
				else
					Rasterizer3D.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), l2, l1, i3, j4, k4, i4, k3, j3, j2, simpleTile.getTexture());
			} else {
				int textureColor = TEXTURE_COLORS[simpleTile.getTexture()];
				Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, light(textureColor, simpleTile.getCenterColor()), light(textureColor, simpleTile.getEastColor()), light(textureColor, simpleTile.getNorthColor()));
			}
		}
		if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
			Rasterizer3D.textureOutOfDrawingBounds = i5 < 0 || k5 < 0 || k6 < 0 || i5 > Rasterizer2D.lastX || k5 > Rasterizer2D.lastX || k6 > Rasterizer2D.lastX;
			if (clicked && inBounds(clickScreenX, clickScreenY, j5, l5, l6, i5, k5, k6)) {
				clickedTileX = x;
				clickedTileY = y;
			}
			if (inBounds(MouseHandler.mouseX, MouseHandler.mouseY, j5, l5, l6, i5, k5, k6)) {
				Client.instance.setHoverTileX(x);
				Client.instance.setHoverTileY(y);
			}
			if (simpleTile.getTexture() == -1) {
				if (simpleTile.getNorthEastColor() != 0xbc614e) {
					Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, simpleTile.getNorthEastColor(), simpleTile.getNorthColor(),
							simpleTile.getEastColor());
				}
			} else {
				if (!lowMem) {
					Rasterizer3D.drawTexturedTriangle(j5, l5, l6, i5, k5, k6, simpleTile.getNorthEastColor(), simpleTile.getNorthColor(),
							simpleTile.getEastColor(), i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.getTexture());
					return;
				}
				int j7 = TEXTURE_COLORS[simpleTile.getTexture()];
				Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, light(j7, simpleTile.getNorthEastColor()), light(j7, simpleTile.getNorthColor()), light(j7, simpleTile.getEastColor()));
			}
		}
	}

	private void drawTileOverlay(int tileX, int pitchSin, int yawSin, ShapedTile tile, int pitchCos, int tileY, int yawCos) {
		final RSTile rsTile = getTiles()[Client.instance.getPlane()][tileX][tileY];
		final boolean checkClick = Client.instance.isCheckClick();

		if (!Client.instance.isGpu())
		{
			drawTileOverlaySD(tileX,pitchSin,yawSin,tile,pitchCos,tileY, yawCos);

			if (roofRemovalMode == 0 || !checkClick || rsTile == null || rsTile.getSceneTileModel() != tile || rsTile.getPhysicalLevel() != Client.instance.getPlane())
			{
				return;
			}
		}

		final DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();

		if (drawCallbacks == null)
		{
			return;
		}

		try
		{
			final int cameraX2 = Client.instance.getCameraX2();
			final int cameraY2 = Client.instance.getCameraY2();
			final int cameraZ2 = Client.instance.getCameraZ2();
			final int zoom = Client.instance.get3dZoom();
			final int centerX = Client.instance.getCenterX();
			final int centerY = Client.instance.getCenterY();

			drawCallbacks.drawSceneModel(0, pitchSin, pitchCos, yawSin, yawCos, -cameraX2, -cameraY2, -cameraZ2,
					tile, Client.instance.getPlane(), tileX, tileY,
					zoom, centerX, centerY);



			RSSceneTileModel tileModel = tile;

			final int[] faceX = tileModel.getFaceX();
			final int[] faceY = tileModel.getFaceY();
			final int[] faceZ = tileModel.getFaceZ();

			final int[] vertexX = tileModel.getVertexX();
			final int[] vertexY = tileModel.getVertexY();
			final int[] vertexZ = tileModel.getVertexZ();

			final int vertexCount = vertexX.length;
			final int faceCount = faceX.length;

			final int mouseX2 = Client.instance.getMouseX2();
			final int mouseY2 = Client.instance.getMouseY2();

			for (int i = 0; i < vertexCount; ++i)
			{
				int vx = vertexX[i] - cameraX2;
				int vy = vertexY[i] - cameraY2;
				int vz = vertexZ[i] - cameraZ2;

				int rotA = vz * yawSin + vx * yawCos >> 16;
				int rotB = vz * yawCos - vx * yawSin >> 16;

				int var13 = vy * pitchCos - rotB * pitchSin >> 16;
				int var12 = vy * pitchSin + rotB * pitchCos >> 16;
				if (var12 < 50)
				{
					return;
				}

				int ax = rotA * zoom / var12 + centerX;
				int ay = var13 * zoom / var12 + centerY;

				tmpX[i] = ax;
				tmpY[i] = ay;
			}

			for (int i = 0; i < faceCount; ++i)
			{
				int va = faceX[i];
				int vb = faceY[i];
				int vc = faceZ[i];

				int x1 = tmpX[va];
				int x2 = tmpX[vb];
				int x3 = tmpX[vc];

				int y1 = tmpY[va];
				int y2 = tmpY[vb];
				int y3 = tmpY[vc];

				if ((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0)
				{

					if (checkClick && Client.instance.containsBounds(mouseX2, mouseY2, y1, y2, y3, x1, x2, x3)) {
						setTargetTile(tileX, tileY);
					}
					if (rsTile != null && Client.instance.containsBounds(MouseHandler.mouseX, MouseHandler.mouseY, y1, y2, y3, x1, x2, x3)) {
						hoverTile(tileX, tileY, rsTile.getPhysicalLevel());
					}

				}
			}
		}
		catch (Exception ex)
		{
			Client.instance.getLogger().warn("error during overlay rendering", ex);
		}
	}

	private void drawTileUnderlay(SimpleTile tile, int z, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y) {
		byte[][][] tileSettings = Client.instance.getTileSettings();
		final boolean checkClick = Client.instance.isCheckClick();

		int tilePlane = z;
		if ((tileSettings[1][x][x] & 2) != 0)
		{
			tilePlane = z - 1;
		}

		if (!Client.instance.isGpu())
		{
			try {
				drawTileUnderlaySD(tile, z, pitchSin, pitchCos, yawSin, yawCos, x, y);
			}
			catch (Exception ex)
			{
				Client.instance.getLogger().warn("error during tile underlay rendering", ex);
			}

			if (roofRemovalMode == 0 || !checkClick || Client.instance.getPlane() != tilePlane)
			{
				return;
			}
		}

		final DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();

		if (drawCallbacks == null)
		{
			return;
		}

		try
		{
			final int[][][] tileHeights = getTileHeights();

			final int cameraX2 = Client.instance.getCameraX2();
			final int cameraY2 = Client.instance.getCameraY2();
			final int cameraZ2 = Client.instance.getCameraZ2();

			final int zoom = Client.instance.get3dZoom();
			final int centerX = Client.instance.getCenterX();
			final int centerY = Client.instance.getCenterY();

			final int mouseX2 = Client.instance.getMouseX2();
			final int mouseY2 = Client.instance.getMouseY2();

			int var9;
			int var10 = var9 = (x << 7) - cameraX2;
			int var11;
			int var12 = var11 = (y << 7) - cameraZ2;
			int var13;
			int var14 = var13 = var10 + 128;
			int var15;
			int var16 = var15 = var12 + 128;
			int var17 = tileHeights[z][x][y] - cameraY2;
			int var18 = tileHeights[z][x + 1][y] - cameraY2;
			int var19 = tileHeights[z][x + 1][y + 1] - cameraY2;
			int var20 = tileHeights[z][x][y + 1] - cameraY2;
			int var21 = var10 * yawCos + yawSin * var12 >> 16;
			var12 = var12 * yawCos - yawSin * var10 >> 16;
			var10 = var21;
			var21 = var17 * pitchCos - pitchSin * var12 >> 16;
			var12 = pitchSin * var17 + var12 * pitchCos >> 16;
			var17 = var21;
			if (var12 >= 50)
			{
				var21 = var14 * yawCos + yawSin * var11 >> 16;
				var11 = var11 * yawCos - yawSin * var14 >> 16;
				var14 = var21;
				var21 = var18 * pitchCos - pitchSin * var11 >> 16;
				var11 = pitchSin * var18 + var11 * pitchCos >> 16;
				var18 = var21;
				if (var11 >= 50)
				{
					var21 = var13 * yawCos + yawSin * var16 >> 16;
					var16 = var16 * yawCos - yawSin * var13 >> 16;
					var13 = var21;
					var21 = var19 * pitchCos - pitchSin * var16 >> 16;
					var16 = pitchSin * var19 + var16 * pitchCos >> 16;
					var19 = var21;
					if (var16 >= 50)
					{
						var21 = var9 * yawCos + yawSin * var15 >> 16;
						var15 = var15 * yawCos - yawSin * var9 >> 16;
						var9 = var21;
						var21 = var20 * pitchCos - pitchSin * var15 >> 16;
						var15 = pitchSin * var20 + var15 * pitchCos >> 16;
						if (var15 >= 50)
						{
							int dy = var10 * zoom / var12 + centerX;
							int dx = var17 * zoom / var12 + centerY;
							int cy = var14 * zoom / var11 + centerX;
							int cx = var18 * zoom / var11 + centerY;
							int ay = var13 * zoom / var16 + centerX;
							int ax = var19 * zoom / var16 + centerY;
							int by = var9 * zoom / var15 + centerX;
							int bx = var21 * zoom / var15 + centerY;

							drawCallbacks.drawScenePaint(0, pitchSin, pitchCos, yawSin, yawCos,
									-cameraX2, -cameraY2, -cameraZ2,
									tile, z, x, y,
									zoom, centerX, centerY);

							if ((ay - by) * (cx - bx) - (ax - bx) * (cy - by) > 0)
							{

								if (checkClick && Client.instance.containsBounds(mouseX2, mouseY2, ax, bx, cx, ay, by, cy)) {
									setTargetTile(x, y);
								}
								if (Client.instance.containsBounds(MouseHandler.mouseX, MouseHandler.mouseY, ax, bx, cx, ay, by, cy)) {
									hoverTile(x, y, tilePlane);
								}

							}

							if ((dy - cy) * (bx - cx) - (dx - cx) * (by - cy) > 0)
							{

								if (checkClick && inBounds(clickScreenX, clickScreenY, dx, cx, bx, dy, cy, by)) {
									setTargetTile(x, y);
								}
								if (inBounds(MouseHandler.mouseX, MouseHandler.mouseY, dx, cx, bx, dy, cy, by)) {
									hoverTile(x, y, tilePlane);
								}

							}

						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			Client.instance.getLogger().warn("error during underlay rendering", ex);
		}
	}


	public static void hoverTile(int x, int y, int plane)
	{
		if (plane == Client.instance.getPlane() && !Client.instance.isMenuOpen())
		{
			Client.instance.setHoverTileX(x);
			Client.instance.setHoverTileY(y);
		}
	}


	private static void setTargetTile(int targetX, int targetY)
	{
		Client.instance.setSelectedSceneTileX(targetX);
		Client.instance.setSelectedSceneTileY(targetY);
	}


	private void drawTileOverlaySD(int tileX, int pitchSin, int yawSin, ShapedTile tile, int pitchCos, int tileY, int yawCos) {
		int k1 = tile.origVertexX.length;
		for (int l1 = 0; l1 < k1; l1++) {
			int i2 = tile.origVertexX[l1] - xCameraPos;
			int k2 = tile.origVertexY[l1] - zCameraPos;
			int i3 = tile.origVertexZ[l1] - yCameraPos;
			int k3 = i3 * yawSin + i2 * yawCos >> 16;
			i3 = i3 * yawCos - i2 * yawSin >> 16;
			i2 = k3;
			k3 = k2 * pitchCos - i3 * pitchSin >> 16;
			i3 = k2 * pitchSin + i3 * pitchCos >> 16;
			k2 = k3;
			if (i3 < 50)
				return;
			if (tile.triangleTexture != null) {
				ShapedTile.anIntArray690[l1] = i2;
				ShapedTile.anIntArray691[l1] = k2;
				ShapedTile.anIntArray692[l1] = i3;
			}
			ShapedTile.anIntArray688[l1] = Rasterizer3D.originViewX + i2 * Rasterizer3D.fieldOfView / i3;
			ShapedTile.anIntArray689[l1] = Rasterizer3D.originViewY + k2 * Rasterizer3D.fieldOfView / i3;;
		}

		Rasterizer3D.alpha = 0;
		k1 = tile.triangleA.length;
		for (int j2 = 0; j2 < k1; j2++) {
			int l2 = tile.triangleA[j2];
			int j3 = tile.triangleB[j2];
			int l3 = tile.triangleC[j2];
			int i4 = ShapedTile.anIntArray688[l2];
			int j4 = ShapedTile.anIntArray688[j3];
			int k4 = ShapedTile.anIntArray688[l3];
			int l4 = ShapedTile.anIntArray689[l2];
			int i5 = ShapedTile.anIntArray689[j3];
			int j5 = ShapedTile.anIntArray689[l3];
			if ((i4 - j4) * (j5 - i5) - (l4 - i5) * (k4 - j4) > 0) {
				Rasterizer3D.textureOutOfDrawingBounds = i4 < 0 || j4 < 0 || k4 < 0 || i4 > Rasterizer2D.lastX
						|| j4 > Rasterizer2D.lastX || k4 > Rasterizer2D.lastX;
				if (clicked && inBounds(clickScreenX, clickScreenY, l4, i5, j5, i4, j4, k4)) {
					clickedTileX = tileX;
					clickedTileY = tileY;
				}
				if (inBounds(MouseHandler.mouseX, MouseHandler.mouseY, l4, i5, j5, i4, j4, k4)) {
					Client.instance.setHoverTileX(tileX);
					Client.instance.setHoverTileY(tileY);
				}
				if (tile.triangleTexture == null || tile.triangleTexture[j2] == -1) {
					if (tile.triangleHslA[j2] != 0xbc614e)
						Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
								tile.triangleHslB[j2], tile.triangleHslC[j2]);
				} else if (!lowMem) {
					if (tile.flat)
						Rasterizer3D.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
								tile.triangleHslB[j2], tile.triangleHslC[j2], ShapedTile.anIntArray690[0],
								ShapedTile.anIntArray690[1], ShapedTile.anIntArray690[3], ShapedTile.anIntArray691[0],
								ShapedTile.anIntArray691[1], ShapedTile.anIntArray691[3], ShapedTile.anIntArray692[0],
								ShapedTile.anIntArray692[1], ShapedTile.anIntArray692[3], tile.triangleTexture[j2]);
					else
						Rasterizer3D.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
								tile.triangleHslB[j2], tile.triangleHslC[j2], ShapedTile.anIntArray690[l2],
								ShapedTile.anIntArray690[j3], ShapedTile.anIntArray690[l3], ShapedTile.anIntArray691[l2],
								ShapedTile.anIntArray691[j3], ShapedTile.anIntArray691[l3], ShapedTile.anIntArray692[l2],
								ShapedTile.anIntArray692[j3], ShapedTile.anIntArray692[l3], tile.triangleTexture[j2]);
				} else {
					int k5 = TEXTURE_COLORS[tile.triangleTexture[j2]];
					Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, light(k5, tile.triangleHslA[j2]),
							light(k5, tile.triangleHslB[j2]), light(k5, tile.triangleHslC[j2]));
				}
			}
		}
	}

	private int light(int j, int k) {
		k = 127 - k;
		k = (k * (j & 0x7f)) / 160;
		if (k < 2)
			k = 2;
		else if (k > 126)
			k = 126;
		return (j & 0xff80) + k;
	}


	private int method317(int j, int k) {
		k = 127 - k;
		k = (k * (j & 0x7f)) / 160;
		if (k < 2)
			k = 2;
		else if (k > 126)
			k = 126;
		return (j & 0xff80) + k;
	}

	public boolean inBounds(int i, int j, int k, int l, int i1, int j1, int k1, int l1) {
		if (j < k && j < l && j < i1)
			return false;
		if (j > k && j > l && j > i1)
			return false;
		if (i < j1 && i < k1 && i < l1)
			return false;
		if (i > j1 && i > k1 && i > l1)
			return false;
		int i2 = (j - k) * (k1 - j1) - (i - j1) * (l - k);
		int j2 = (j - i1) * (j1 - l1) - (i - l1) * (k - i1);
		int k2 = (j - l) * (l1 - k1) - (i - k1) * (i1 - l);
		return i2 * k2 > 0 && k2 * j2 > 0;
	}

	private void occlude() {
		int j = sceneClusterCounts[currentRenderPlane];
		CullingCluster aclass47[] = sceneClusters[currentRenderPlane];
		anInt475 = 0;
		for (int k = 0; k < j; k++) {
			CullingCluster class47 = aclass47[k];
			if (class47.anInt791 == 1) {
				int l = (class47.anInt787 - screenCenterX) + 25;
				if (l < 0 || l > 50)
					continue;
				int k1 = (class47.anInt789 - screenCenterZ) + 25;
				if (k1 < 0)
					k1 = 0;
				int j2 = (class47.anInt790 - screenCenterZ) + 25;
				if (j2 > 50)
					j2 = 50;
				boolean flag = false;
				while (k1 <= j2)
					if (renderArea[l][k1++]) {
						flag = true;
						break;
					}
				if (!flag)
					continue;
				int j3 = xCameraPos - class47.anInt792;
				if (j3 > 32) {
					class47.anInt798 = 1;
				} else {
					if (j3 >= -32)
						continue;
					class47.anInt798 = 2;
					j3 = -j3;
				}
				class47.anInt801 = (class47.anInt794 - yCameraPos << 8) / j3;
				class47.anInt802 = (class47.anInt795 - yCameraPos << 8) / j3;
				class47.anInt803 = (class47.anInt796 - zCameraPos << 8) / j3;
				class47.anInt804 = (class47.anInt797 - zCameraPos << 8) / j3;
				aClass47Array476[anInt475++] = class47;
				continue;
			}
			if (class47.anInt791 == 2) {
				int i1 = (class47.anInt789 - screenCenterZ) + 25;
				if (i1 < 0 || i1 > 50)
					continue;
				int l1 = (class47.anInt787 - screenCenterX) + 25;
				if (l1 < 0)
					l1 = 0;
				int k2 = (class47.anInt788 - screenCenterX) + 25;
				if (k2 > 50)
					k2 = 50;
				boolean flag1 = false;
				while (l1 <= k2)
					if (renderArea[l1++][i1]) {
						flag1 = true;
						break;
					}
				if (!flag1)
					continue;
				int k3 = yCameraPos - class47.anInt794;
				if (k3 > 32) {
					class47.anInt798 = 3;
				} else {
					if (k3 >= -32)
						continue;
					class47.anInt798 = 4;
					k3 = -k3;
				}
				class47.anInt799 = (class47.anInt792 - xCameraPos << 8) / k3;
				class47.anInt800 = (class47.anInt793 - xCameraPos << 8) / k3;
				class47.anInt803 = (class47.anInt796 - zCameraPos << 8) / k3;
				class47.anInt804 = (class47.anInt797 - zCameraPos << 8) / k3;
				aClass47Array476[anInt475++] = class47;
			} else if (class47.anInt791 == 4) {
				int j1 = class47.anInt796 - zCameraPos;
				if (j1 > 128) {
					int i2 = (class47.anInt789 - screenCenterZ) + 25;
					if (i2 < 0)
						i2 = 0;
					int l2 = (class47.anInt790 - screenCenterZ) + 25;
					if (l2 > 50)
						l2 = 50;
					if (i2 <= l2) {
						int i3 = (class47.anInt787 - screenCenterX) + 25;
						if (i3 < 0)
							i3 = 0;
						int l3 = (class47.anInt788 - screenCenterX) + 25;
						if (l3 > 50)
							l3 = 50;
						boolean flag2 = false;
						label0:
						for (int i4 = i3; i4 <= l3; i4++) {
							for (int j4 = i2; j4 <= l2; j4++) {
								if (!renderArea[i4][j4])
									continue;
								flag2 = true;
								break label0;
							}

						}

						if (flag2) {
							class47.anInt798 = 5;
							class47.anInt799 = (class47.anInt792 - xCameraPos << 8) / j1;
							class47.anInt800 = (class47.anInt793 - xCameraPos << 8) / j1;
							class47.anInt801 = (class47.anInt794 - yCameraPos << 8) / j1;
							class47.anInt802 = (class47.anInt795 - yCameraPos << 8) / j1;
							aClass47Array476[anInt475++] = class47;
						}
					}
				}
			}
		}

	}

	private boolean method320(int i, int j, int k) {
		int l = anIntArrayArrayArray445[i][j][k];
		if (l == -cycle)
			return false;
		if (l == cycle)
			return true;
		int i1 = j << 7;
		int j1 = k << 7;
		if (method324(i1 + 1, heightMap[i][j][k], j1 + 1) && method324((i1 + 128) - 1, heightMap[i][j + 1][k], j1 + 1) && method324((i1 + 128) - 1, heightMap[i][j + 1][k + 1], (j1 + 128) - 1) && method324(i1 + 1, heightMap[i][j][k + 1], (j1 + 128) - 1)) {
			anIntArrayArrayArray445[i][j][k] = cycle;
			return true;
		} else {
			anIntArrayArrayArray445[i][j][k] = -cycle;
			return false;
		}
	}

	private boolean method321(int i, int j, int k, int l) {
		if (!method320(i, j, k))
			return false;
		int i1 = j << 7;
		int j1 = k << 7;
		int k1 = heightMap[i][j][k] - 1;
		int l1 = k1 - 120;
		int i2 = k1 - 230;
		int j2 = k1 - 238;
		if (l < 16) {
			if (l == 1) {
				if (i1 > xCameraPos) {
					if (!method324(i1, k1, j1))
						return false;
					if (!method324(i1, k1, j1 + 128))
						return false;
				}
				if (i > 0) {
					if (!method324(i1, l1, j1))
						return false;
					if (!method324(i1, l1, j1 + 128))
						return false;
				}
				return method324(i1, i2, j1) && method324(i1, i2, j1 + 128);
			}
			if (l == 2) {
				if (j1 < yCameraPos) {
					if (!method324(i1, k1, j1 + 128))
						return false;
					if (!method324(i1 + 128, k1, j1 + 128))
						return false;
				}
				if (i > 0) {
					if (!method324(i1, l1, j1 + 128))
						return false;
					if (!method324(i1 + 128, l1, j1 + 128))
						return false;
				}
				return method324(i1, i2, j1 + 128) && method324(i1 + 128, i2, j1 + 128);
			}
			if (l == 4) {
				if (i1 < xCameraPos) {
					if (!method324(i1 + 128, k1, j1))
						return false;
					if (!method324(i1 + 128, k1, j1 + 128))
						return false;
				}
				if (i > 0) {
					if (!method324(i1 + 128, l1, j1))
						return false;
					if (!method324(i1 + 128, l1, j1 + 128))
						return false;
				}
				return method324(i1 + 128, i2, j1) && method324(i1 + 128, i2, j1 + 128);
			}
			if (l == 8) {
				if (j1 > yCameraPos) {
					if (!method324(i1, k1, j1))
						return false;
					if (!method324(i1 + 128, k1, j1))
						return false;
				}
				if (i > 0) {
					if (!method324(i1, l1, j1))
						return false;
					if (!method324(i1 + 128, l1, j1))
						return false;
				}
				return method324(i1, i2, j1) && method324(i1 + 128, i2, j1);
			}
		}
		if (!method324(i1 + 64, j2, j1 + 64))
			return false;
		if (l == 16)
			return method324(i1, i2, j1 + 128);
		if (l == 32)
			return method324(i1 + 128, i2, j1 + 128);
		if (l == 64)
			return method324(i1 + 128, i2, j1);
		if (l == 128) {
			return method324(i1, i2, j1);
		} else {
			System.out.println("Warning unsupported wall type");
			return true;
		}
	}

	private boolean method322(int i, int j, int k, int l) {
		if (!method320(i, j, k))
			return false;
		int i1 = j << 7;
		int j1 = k << 7;
		return method324(i1 + 1, heightMap[i][j][k] - l, j1 + 1) && method324((i1 + 128) - 1, heightMap[i][j + 1][k] - l, j1 + 1) && method324((i1 + 128) - 1, heightMap[i][j + 1][k + 1] - l, (j1 + 128) - 1) && method324(i1 + 1, heightMap[i][j][k + 1] - l, (j1 + 128) - 1);
	}

	private boolean method323(int i, int j, int k, int l, int i1, int j1) {
		if (j == k && l == i1) {
			if (!method320(i, j, l))
				return false;
			int k1 = j << 7;
			int i2 = l << 7;
			return method324(k1 + 1, heightMap[i][j][l] - j1, i2 + 1) && method324((k1 + 128) - 1, heightMap[i][j + 1][l] - j1, i2 + 1) && method324((k1 + 128) - 1, heightMap[i][j + 1][l + 1] - j1, (i2 + 128) - 1) && method324(k1 + 1, heightMap[i][j][l + 1] - j1, (i2 + 128) - 1);
		}
		for (int l1 = j; l1 <= k; l1++) {
			for (int j2 = l; j2 <= i1; j2++)
				if (anIntArrayArrayArray445[i][l1][j2] == -cycle)
					return false;

		}

		int k2 = (j << 7) + 1;
		int l2 = (l << 7) + 2;
		int i3 = heightMap[i][j][l] - j1;
		if (!method324(k2, i3, l2))
			return false;
		int j3 = (k << 7) - 1;
		if (!method324(j3, i3, l2))
			return false;
		int k3 = (i1 << 7) - 1;
		return method324(k2, i3, k3) && method324(j3, i3, k3);
	}

	private boolean method324(int i, int j, int k) {
		for (int l = 0; l < anInt475; l++) {
			CullingCluster class47 = aClass47Array476[l];
			if (class47.anInt798 == 1) {
				int i1 = class47.anInt792 - i;
				if (i1 > 0) {
					int j2 = class47.anInt794 + (class47.anInt801 * i1 >> 8);
					int k3 = class47.anInt795 + (class47.anInt802 * i1 >> 8);
					int l4 = class47.anInt796 + (class47.anInt803 * i1 >> 8);
					int i6 = class47.anInt797 + (class47.anInt804 * i1 >> 8);
					if (k >= j2 && k <= k3 && j >= l4 && j <= i6)
						return true;
				}
			} else if (class47.anInt798 == 2) {
				int j1 = i - class47.anInt792;
				if (j1 > 0) {
					int k2 = class47.anInt794 + (class47.anInt801 * j1 >> 8);
					int l3 = class47.anInt795 + (class47.anInt802 * j1 >> 8);
					int i5 = class47.anInt796 + (class47.anInt803 * j1 >> 8);
					int j6 = class47.anInt797 + (class47.anInt804 * j1 >> 8);
					if (k >= k2 && k <= l3 && j >= i5 && j <= j6)
						return true;
				}
			} else if (class47.anInt798 == 3) {
				int k1 = class47.anInt794 - k;
				if (k1 > 0) {
					int l2 = class47.anInt792 + (class47.anInt799 * k1 >> 8);
					int i4 = class47.anInt793 + (class47.anInt800 * k1 >> 8);
					int j5 = class47.anInt796 + (class47.anInt803 * k1 >> 8);
					int k6 = class47.anInt797 + (class47.anInt804 * k1 >> 8);
					if (i >= l2 && i <= i4 && j >= j5 && j <= k6)
						return true;
				}
			} else if (class47.anInt798 == 4) {
				int l1 = k - class47.anInt794;
				if (l1 > 0) {
					int i3 = class47.anInt792 + (class47.anInt799 * l1 >> 8);
					int j4 = class47.anInt793 + (class47.anInt800 * l1 >> 8);
					int k5 = class47.anInt796 + (class47.anInt803 * l1 >> 8);
					int l6 = class47.anInt797 + (class47.anInt804 * l1 >> 8);
					if (i >= i3 && i <= j4 && j >= k5 && j <= l6)
						return true;
				}
			} else if (class47.anInt798 == 5) {
				int i2 = j - class47.anInt796;
				if (i2 > 0) {
					int j3 = class47.anInt792 + (class47.anInt799 * i2 >> 8);
					int k4 = class47.anInt793 + (class47.anInt800 * i2 >> 8);
					int l5 = class47.anInt794 + (class47.anInt801 * i2 >> 8);
					int i7 = class47.anInt795 + (class47.anInt802 * i2 >> 8);
					if (i >= j3 && i <= k4 && k >= l5 && k <= i7)
						return true;
				}
			}
		}

		return false;
	}

	private boolean aBoolean434;
	public static boolean lowMem = true;
	private final int sizeZ;
	private final int sizeX;
	private final int sizeY;
	private final int[][][] heightMap;
	private final Tile[][][] tileArray;
	private int minLevel;
	private int obj5CacheCurrPos;
	private final GameObject[] gameObjectsCache;
	private final int[][][] anIntArrayArrayArray445;
	public int tileUpdateCount;
	public int currentRenderPlane;
	public int cycle;
	public int minTileX;
	public int maxTileX;
	public int minTileZ;
	public int maxTileZ;
	public int screenCenterX;
	public int screenCenterZ;
	public static int xCameraPos;
	public static int zCameraPos;
	public static int yCameraPos;
	public static int camUpDownY;
	public static int camUpDownX;
	public static int camLeftRightY;
	public static int camLeftRightX;
	private static GameObject[] interactableObjects = new GameObject[100];
	private static final int[] anIntArray463 = {53, -53, -53, 53};
	private static final int[] anIntArray464 = {-53, -53, 53, 53};
	private static final int[] anIntArray465 = {-45, 45, 45, -45};
	private static final int[] anIntArray466 = {45, 45, -45, -45};
	public static boolean clicked;
	public static int clickScreenX;
	public static int clickScreenY;
	public static int clickedTileX = -1;
	public static int clickedTileY = -1;
	private static final int cullingClusterPlaneCount;
	private static int[] sceneClusterCounts;
	private static CullingCluster[][] sceneClusters;
	private static int anInt475;
	private static final CullingCluster[] aClass47Array476 = new CullingCluster[500];
	private static Deque tileDeque = new Deque();
	private static final int[] anIntArray478 = {19, 55, 38, 155, 255, 110, 137, 205, 76};
	private static final int[] anIntArray479 = {160, 192, 80, 96, 0, 144, 80, 48, 160};
	private static final int[] anIntArray480 = {76, 8, 137, 4, 0, 1, 38, 2, 19};
	private static final int[] anIntArray481 = {0, 0, 2, 0, 0, 2, 1, 1, 0};
	private static final int[] anIntArray482 = {2, 0, 0, 2, 0, 0, 0, 4, 4};
	private static final int[] anIntArray483 = {0, 4, 4, 8, 0, 0, 8, 0, 0};
	private static final int[] anIntArray484 = {1, 1, 0, 0, 0, 8, 0, 0, 8};
	private static final int[] TEXTURE_COLORS = {41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41};
	private final int[] anIntArray486;
	private final int[] anIntArray487;
	private int anInt488;
	private final int[][] tileVertices = {new int[16], {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1}, {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1}, {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
	private final int[][] tileVertexIndices = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3}, {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
	public static boolean[][][][] visibilityMap = new boolean[8][32][51][51];
	public static boolean[][] renderArea;
	private static int viewportHalfWidth;
	private static int viewportHalfHeight;
	private static int xMin;
	private static int yMin;
	private static int xMax;
	private static int yMax;

	static {
		cullingClusterPlaneCount = 4;
		sceneClusterCounts = new int[cullingClusterPlaneCount];
		sceneClusters = new CullingCluster[cullingClusterPlaneCount][500];
	}

	/**
	 * Runelite
	 */
	private int drawDistance = 25;

	@Override
	public void addItem(int id, int quantity, WorldPoint point) {
		final int sceneX = point.getX() - Client.instance.getBaseX();
		final int sceneY = point.getY() - Client.instance.getBaseY();
		final int plane = point.getPlane();

		if (sceneX < 0 || sceneY < 0 || sceneX >= 104 || sceneY >= 104)
		{
			return;
		}

		RSTileItem item = Client.instance.newTileItem();
		item.setId(id);
		item.setQuantity(quantity);
		RSNodeDeque[][][] groundItems = Client.instance.getGroundItemDeque();

		if (groundItems[plane][sceneX][sceneY] == null)
		{
			groundItems[plane][sceneX][sceneY] = Client.instance.newNodeDeque();
		}

		groundItems[plane][sceneX][sceneY].addFirst(item);

		if (plane == Client.instance.getPlane())
		{
			Client.instance.updateItemPile(sceneX, sceneY);
		}
	}

	@Override
	public void removeItem(int id, int quantity, WorldPoint point) {
		final int sceneX = point.getX() - Client.instance.getBaseX();
		final int sceneY = point.getY() - Client.instance.getBaseY();
		final int plane = point.getPlane();

		if (sceneX < 0 || sceneY < 0 || sceneX >= 104 || sceneY >= 104)
		{
			return;
		}

		RSNodeDeque items = Client.instance.getGroundItemDeque()[plane][sceneX][sceneY];

		if (items == null)
		{
			return;
		}

		for (RSTileItem item = (RSTileItem) items.last(); item != null; item = (RSTileItem) items.previous())
		{
			if (item.getId() == id && quantity == 1)
			{
				item.unlink();
				break;
			}
		}

		if (items.last() == null)
		{
			Client.instance.getGroundItemDeque()[plane][sceneX][sceneY] = null;
		}

		Client.instance.updateItemPile(sceneX, sceneY);
	}

	@Override
	public int getDrawDistance() {
		return drawDistance;
	}

	@Override
	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}

	@Override
	public void generateHouses() {

	}

	@Override
	public void setRoofRemovalMode(int flags) {
		roofRemovalMode = flags;
	}

	@Override
	public RSGameObject[] getObjects() {
		return gameObjectsCache;
	}

	@Override
	public RSTile[][][] getTiles() {
		return tileArray;
	}

	@Override
	public int[][] getTileShape2D() {
		return tileVertices;
	}

	@Override
	public int[][] getTileRotation2D() {
		return tileVertexIndices;
	}

	@Override
	public void draw(net.runelite.api.Tile tile, boolean var2) {
		drawTile((Tile) tile, false);
	}

	@Override
	public int[][][] getTileHeights() {
		return heightMap;
	}

	@Override
	public int getBaseX() {
		return Client.instance.getBaseX();
	}

	@Override
	public int getBaseY() {
		return Client.instance.getBaseY();
	}

	@Override
	public boolean isInstance() {
		return Client.instance.isInInstancedRegion();
	}

	@Override
	public int[][][] getInstanceTemplateChunks() {
		return Client.instance.getInstanceTemplateChunks();
	}

	@Override
	public void drawTile(int[] pixels, int pixelOffset, int width, int z, int x, int y) {

	}

	@Override
	public void updateOccluders() {
		occlude();
	}

	@Override
	public int getMaxX() {
		return sizeX;
	}

	@Override
	public int getMaxY() {
		return sizeZ;
	}

	@Override
	public int getMaxZ() {
		return sizeY;
	}

	@Override
	public int getMinLevel() {
		return minLevel;
	}

	@Override
	public void setMinLevel(int lvl) {
		this.minLevel = lvl;
	}

	@Override
	public void newGroundItemPile(int plane, int x, int y, int hash, RSRenderable var5, long var6, RSRenderable var7, RSRenderable var8) {

	}

	@Override
	public boolean newGameObject(int plane, int startX, int startY, int var4, int var5, int centerX, int centerY,
								 int height, RSRenderable entity, int orientation, boolean tmp, long tag, int flags) {
		return false;
	}

	@Override
	public void removeGameObject(net.runelite.api.GameObject gameObject) {
		removeGameObject(gameObject.getPlane(),gameObject.getX(),gameObject.getY());
	}

	@Override
	public void removeGameObject(int plane, int x, int y) {

	}


	public void removeWallObject(WallObject wallObject)
	{
		final RSTile[][][] tiles = getTiles();

		for (int y = 0; y < 104; ++y)
		{
			for (int x = 0; x < 104; ++x)
			{
				RSTile tile = tiles[Client.instance.getPlane()][x][y];
				if (tile != null && tile.getWallObject() == wallObject)
				{
					tile.setWallObject(null);
				}
			}
		}
	}

	@Override
	public void removeWallObject(int plane, int x, int y) {

	}

	@Override
	public void removeDecorativeObject(DecorativeObject decorativeObject)
	{
		final RSTile[][][] tiles = getTiles();

		for (int y = 0; y < 104; ++y)
		{
			for (int x = 0; x < 104; ++x)
			{
				RSTile tile = tiles[Client.instance.getPlane()][x][y];
				if (tile != null && tile.getDecorativeObject() == decorativeObject)
				{
					tile.setDecorativeObject(null);
				}
			}
		}
	}

	@Override
	public void removeDecorativeObject(int plane, int x, int y) {

	}


	@Override
	public void removeGroundObject(GroundObject groundObject)
	{
		final RSTile[][][] tiles = getTiles();

		for (int y = 0; y < 104; ++y)
		{
			for (int x = 0; x < 104; ++x)
			{
				RSTile tile = tiles[Client.instance.getPlane()][x][y];
				if (tile != null && tile.getGroundObject() == groundObject)
				{
					tile.setGroundObject(null);
				}
			}
		}
	}

	@Override
	public void removeGroundObject(int plane, int x, int y) {
	}

	@Override
	public short[][][] getUnderlayIds() {
		return Client.instance.currentMapRegion.underlayId;
	}

	@Override
	public void setUnderlayIds(short[][][] underlayIds) {
		Client.instance.currentMapRegion.underlayId = underlayIds;
	}

	@Override
	public short[][][] getOverlayIds() {
		return Client.instance.currentMapRegion.Tiles_overlays;
	}

	@Override
	public void setOverlayIds(short[][][] overlayIds) {
		Client.instance.currentMapRegion.Tiles_overlays = overlayIds;
	}

	@Override
	public byte[][][] getTileShapes() {
		return Client.instance.currentMapRegion.Tiles_shapes;
	}

	@Override
	public void setTileShapes(byte[][][] tileShapes) {
		Client.instance.currentMapRegion.Tiles_shapes = tileShapes;
	}

	@Override
	public void menuOpen(int selectedPlane, int screenX, int screenY, boolean viewportWalking) {

	}

	public Tile getSelectedSceneTile() {
		int tileX = hoverX;
		int tileY = hoverY;

		if (tileX == -1 || tileY == -1) {
			return null;
		}

		return getTile(Client.instance.getPlane(), tileX, tileY);
	}

	public Tile getTile(int z, int x, int y) {
		return tileArray[z][x][y];
	}
}