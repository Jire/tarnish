package com.osroyale;

import com.osroyale.engine.impl.MouseHandler;
import com.osroyale.math.MathUtils;
import com.osroyale.math.Matrix4f;
import com.osroyale.skeletal.AB;
import com.osroyale.skeletal.ABW;
import com.osroyale.skeletal.SkeletalFrame;
import com.osroyale.skeletal.TO;
import net.runelite.api.Perspective;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.model.Jarvis;
import net.runelite.api.model.Triangle;
import net.runelite.api.model.Vertex;
import net.runelite.rs.api.RSFrames;
import net.runelite.rs.api.RSModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model extends Renderable implements RSModel {

	public boolean DEBUG_MODELS = false;

	public static void clear() {
		modelHeaders = null;
		hasAnEdgeToRestrict = null;
		outOfReach = null;
		vertexScreenY = null;
		vertexScreenZ = null;
		vertexMovedX = null;
		vertexMovedY = null;
		vertexMovedZ = null;
		depth = null;
		faceLists = null;
		anIntArray1673 = null;
		anIntArrayArray1674 = null;
		anIntArray1675 = null;
		anIntArray1676 = null;
		anIntArray1677 = null;
		SINE = null;
		COSINE = null;
		modelColors = null;
		modelLocations = null;
	}

	private Model(int modelId) {

		this.verticesCount = 0;
		this.triangleFaceCount = 0;
		this.priority = 0;

		byte[] data = modelHeaders[modelId].data;
		if (data[data.length - 1] == -3 && data[data.length - 2] == -1) {
			ModelLoader.decodeType3(this, data);
		} else if (data[data.length - 1] == -2 && data[data.length - 2] == -1) {
			ModelLoader.decodeType2(this, data);
		} else if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
			ModelLoader.decodeType1(this, data);
		} else {
			ModelLoader.decodeOldFormat(this, data);
		}
	}

	public static void loadModel(byte[] modelData, final int modelId) {
		/*try {
			modelData = Files.readAllBytes(Path.of("index1", modelId + ".gz"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		if (modelData == null) {
			final ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader();
			modelHeader.vertexCount = 0;
			modelHeader.triangleCount = 0;
			modelHeader.texturedTriangleCount = 0;
			return;
		}
		final Buffer stream = new Buffer(modelData);
		stream.position = modelData.length - 18;
		final ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader();
		modelHeader.data = modelData;
		modelHeader.vertexCount = stream.readUShort();
		modelHeader.triangleCount = stream.readUShort();
		modelHeader.texturedTriangleCount = stream.readUnsignedByte();
		final int useTextures = stream.readUnsignedByte();
		final int useTrianglePriority = stream.readUnsignedByte();
		final int useAlpha = stream.readUnsignedByte();
		final int useTriangleSkins = stream.readUnsignedByte();
		final int useVertexSkins = stream.readUnsignedByte();
		final int dataLengthX = stream.readUShort();
		final int dataLengthY = stream.readUShort();
		final int dataLengthZ = stream.readUShort();
		final int dataLengthTriangle = stream.readUShort();
		int offset = 0;
		modelHeader.vertexDirectionOffset = offset;
		offset += modelHeader.vertexCount;
		modelHeader.triangleTypeOffset = offset;
		offset += modelHeader.triangleCount;
		modelHeader.trianglePriorityOffset = offset;
		if (useTrianglePriority == 255) {
			offset += modelHeader.triangleCount;
		} else {
			modelHeader.trianglePriorityOffset = -useTrianglePriority - 1;
		}
		modelHeader.triangleSkinOffset = offset;
		if (useTriangleSkins == 1) {
			offset += modelHeader.triangleCount;
		} else {
			modelHeader.triangleSkinOffset = -1;
		}
		modelHeader.texturePointerOffset = offset;
		if (useTextures == 1) {
			offset += modelHeader.triangleCount;
		} else {
			modelHeader.texturePointerOffset = -1;
		}
		modelHeader.vertexSkinOffset = offset;
		if (useVertexSkins == 1) {
			offset += modelHeader.vertexCount;
		} else {
			modelHeader.vertexSkinOffset = -1;
		}
		modelHeader.triangleAlphaOffset = offset;
		if (useAlpha == 1) {
			offset += modelHeader.triangleCount;
		} else {
			modelHeader.triangleAlphaOffset = -1;
		}
		modelHeader.triangleDataOffset = offset;
		offset += dataLengthTriangle;
		modelHeader.colourDataOffset = offset;
		offset += modelHeader.triangleCount * 2;
		modelHeader.texturedTriangleOffset = offset;
		offset += modelHeader.texturedTriangleCount * 6;
		modelHeader.dataOffsetX = offset;
		offset += dataLengthX;
		modelHeader.dataOffsetY = offset;
		offset += dataLengthY;
		modelHeader.dataOffsetZ = offset;
		offset += dataLengthZ;
	}

	public static void init() {
		modelHeaders = new ModelHeader[90000];
	}

	public static void resetModel(final int model) {
		modelHeaders[model] = null;
	}

	public static Model getModel(int file) {
		if (modelHeaders == null) {
			return null;
		}

		ModelHeader header = modelHeaders[file];
		if (header == null) {
			Client.instance.onDemandFetcher.loadData(0, file);
			return null;
		} else {
			return new Model(file);
		}
	}

	public static boolean isCached(int file) {
		if (modelHeaders == null)
			return false;

		ModelHeader mdl = modelHeaders[file];
		if (mdl == null) {
			Client.instance.onDemandFetcher.loadData(0,file);
			return false;
		} else {
			return true;
		}
	}

	Model() {
		verticesCount = 0;
		triangleFaceCount = 0;
		texturesCount = 0;
		priority = 0;
		singleTile = true;
	}

	public Model(int length, Model[] parts) {
		try {
			singleTile = false;
			boolean var3 = false;
			boolean var4 = false;
			boolean var5 = false;
			boolean var6 = false;
			boolean var7 = false;
			boolean var8 = false;
			boolean var11 = false;
			verticesCount = 0;
			triangleFaceCount = 0;
			texturesCount = 0;
			priority = -1;

			int var9;
			Model var10;
			for (var9 = 0; var9 < length; var9++) {
				var10 = parts[var9];
				if (var10 != null) {
					verticesCount += var10.verticesCount;
					triangleFaceCount += var10.triangleFaceCount;
					texturesCount += var10.texturesCount;
					if (var10.faceRenderPriorities != null) {
						var4 = true;
					} else {
						if (priority == -1) {
							priority = var10.priority;
						}

						if (priority != var10.priority) {
							var4 = true;
						}
					}

					var3 |= var10.faceRenderType != null;
					var5 |= var10.faceTransparencies != null;
					var6 |= var10.triangleSkinValues != null;
					var7 |= var10.faceTextures != null;
					var8 |= var10.textureCoords != null;
					var11 |= var10.animayaGroups != null;
				}
			}
			verticesX = new int[verticesCount];
			verticesY = new int[verticesCount];
			verticesZ = new int[verticesCount];
			vertexSkins = new int[verticesCount];
			trianglePointsX = new int[triangleFaceCount];
			trianglePointsY = new int[triangleFaceCount];
			trianglePointsZ = new int[triangleFaceCount];

			if (var3) {
				faceRenderType = new int[triangleFaceCount];
			}

			if (var4) {
				faceRenderPriorities = new byte[triangleFaceCount];
			}

			if (var5) {
				faceTransparencies = new byte[triangleFaceCount];
			}

			if (var6) {
				triangleSkinValues = new int[triangleFaceCount];
			}

			if (var7) {
				faceTextures = new short[triangleFaceCount];
			}

			if (var8) {
				textureCoords = new byte[triangleFaceCount];
			}

			if (var11) {
				this.animayaGroups = new int[this.verticesCount][];
				this.animayaScales = new int[this.verticesCount][];
			}

			faceColors = new short[triangleFaceCount];
			if (texturesCount > 0) {
				textureRenderTypes = new byte[texturesCount];
				texTriangleX = new short[texturesCount];
				texTriangleY = new short[texturesCount];
				texTriangleZ = new short[texturesCount];
			}
			verticesCount = 0;
			triangleFaceCount = 0;
			texturesCount = 0;
			for (var9 = 0; var9 < length; var9++) {
				var10 = parts[var9];
				if (var10 != null) {
					for (int face = 0; face < var10.triangleFaceCount; face++) {
						if (var3 && var10.faceRenderType != null) {
							faceRenderType[triangleFaceCount] = var10.faceRenderType[face];
						}

						if (var4) {
							if (var10.faceRenderPriorities == null) {
								faceRenderPriorities[triangleFaceCount] = var10.priority;
							} else {
								faceRenderPriorities[triangleFaceCount] = var10.faceRenderPriorities[face];
							}
						}

						if (var5 && var10.faceTransparencies != null) {
							faceTransparencies[triangleFaceCount] = var10.faceTransparencies[face];
						}

						if (var6 && var10.triangleSkinValues != null) {
							triangleSkinValues[triangleFaceCount] = var10.triangleSkinValues[face];
						}

						if (var7) {
							if (var10.faceTextures != null) {
								faceTextures[triangleFaceCount] = var10.faceTextures[face];
							} else {
								faceTextures[triangleFaceCount] = -1;
							}
						}
						if (var8) {
							if (var10.textureCoords != null && var10.textureCoords[face] != -1) {
								textureCoords[triangleFaceCount] = (byte) (texturesCount + var10.textureCoords[face]);
							} else {
								textureCoords[triangleFaceCount] = -1;
							}
						}

						faceColors[triangleFaceCount] = var10.faceColors[face];
						trianglePointsX[triangleFaceCount] = getFirstIdenticalVertexId(var10, var10.trianglePointsX[face]);
						trianglePointsY[triangleFaceCount] = getFirstIdenticalVertexId(var10, var10.trianglePointsY[face]);
						trianglePointsZ[triangleFaceCount] = getFirstIdenticalVertexId(var10, var10.trianglePointsZ[face]);
						triangleFaceCount++;
					}
					for (int texture_edge = 0; texture_edge < var10.texturesCount; texture_edge++) {
						final byte var12 = textureRenderTypes[texturesCount] = var10.textureRenderTypes[texture_edge];
						if (var12 == 0) {
							texTriangleX[texturesCount] = (short) getFirstIdenticalVertexId(var10, var10.texTriangleX[texture_edge]);
							texTriangleY[texturesCount] = (short) getFirstIdenticalVertexId(var10, var10.texTriangleY[texture_edge]);
							texTriangleZ[texturesCount] = (short) getFirstIdenticalVertexId(var10, var10.texTriangleZ[texture_edge]);
						}
						texturesCount++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Model(final Model[] models) {
		final int count = 2;
		singleTile = false;
		boolean var3 = false;
		boolean var4 = false;
		boolean var5 = false;
		boolean var6 = false;

		this.verticesCount = 0;
		this.triangleFaceCount = 0;
		this.texturesCount = 0;
		this.priority = -1;

		int var7;
		Model model_1;
		for (var7 = 0; var7 < count; ++var7) {
			model_1 = models[var7];
			if (model_1 != null) {
				this.verticesCount += model_1.verticesCount;
				this.triangleFaceCount += model_1.triangleFaceCount;
				this.texturesCount += model_1.texturesCount;
				if (model_1.faceRenderPriorities != null) {
					var3 = true;
				} else {
					if (this.priority == -1) {
						this.priority = model_1.priority;
					}
					if (this.priority != model_1.priority) {
						var3 = true;
					}
				}
				var4 |= model_1.faceTransparencies != null;
				var5 |= model_1.faceTextures != null;
				var6 |= model_1.textureCoords != null;
			}
		}

		this.verticesX = new int[this.verticesCount];
		this.verticesY = new int[this.verticesCount];
		this.verticesZ = new int[this.verticesCount];
		this.trianglePointsX = new int[this.triangleFaceCount];
		this.trianglePointsY = new int[this.triangleFaceCount];
		this.trianglePointsZ = new int[this.triangleFaceCount];
		this.colorsX = new int[this.triangleFaceCount];
		this.colorsY = new int[this.triangleFaceCount];
		this.colorsZ = new int[this.triangleFaceCount];
		if (var3) {
			this.faceRenderPriorities = new byte[this.triangleFaceCount];
		}

		if (var4) {
			this.faceTransparencies = new byte[this.triangleFaceCount];
		}

		if (var5) {
			this.faceTextures = new short[this.triangleFaceCount];
		}

		if (var6) {
			this.textureCoords = new byte[this.triangleFaceCount];
		}

		if (this.texturesCount > 0) {
			this.texTriangleX = new short[this.texturesCount];
			this.texTriangleY = new short[this.texturesCount];
			this.texTriangleZ = new short[this.texturesCount];
		}

		this.verticesCount = 0;
		this.triangleFaceCount = 0;
		this.texturesCount = 0;

		for (var7 = 0; var7 < count; ++var7) {
			model_1 = models[var7];
			if (model_1 != null) {
				for (int var9 = 0; var9 < model_1.triangleFaceCount; ++var9) {
					this.trianglePointsX[this.triangleFaceCount] = this.verticesCount + model_1.trianglePointsX[var9];
					this.trianglePointsY[this.triangleFaceCount] = this.verticesCount + model_1.trianglePointsY[var9];
					this.trianglePointsZ[this.triangleFaceCount] = this.verticesCount + model_1.trianglePointsZ[var9];
					this.colorsX[this.triangleFaceCount] = model_1.colorsX[var9];
					this.colorsY[this.triangleFaceCount] = model_1.colorsY[var9];
					this.colorsZ[this.triangleFaceCount] = model_1.colorsZ[var9];
					if (var3) {
						if (model_1.faceRenderPriorities != null) {
							this.faceRenderPriorities[this.triangleFaceCount] = model_1.faceRenderPriorities[var9];
						} else {
							this.faceRenderPriorities[this.triangleFaceCount] = model_1.priority;
						}
					}

					if (var4 && model_1.faceTransparencies != null) {
						this.faceTransparencies[this.triangleFaceCount] = model_1.faceTransparencies[var9];
					}

					if (var5) {
						if (model_1.faceTextures != null) {
							this.faceTextures[this.triangleFaceCount] = model_1.faceTextures[var9];
						} else {
							this.faceTextures[this.triangleFaceCount] = -1;
						}
					}

					if (var6) {
						if (model_1.textureCoords != null && model_1.textureCoords[var9] != -1) {
							this.textureCoords[this.triangleFaceCount] = (byte) (this.texturesCount + model_1.textureCoords[var9]);
						} else {
							this.textureCoords[this.triangleFaceCount] = -1;
						}
					}

					++this.triangleFaceCount;
				}

				for (int var9 = 0; var9 < model_1.texturesCount; ++var9) {
					this.texTriangleX[this.texturesCount] = (short) (this.verticesCount + model_1.texTriangleX[var9]);
					this.texTriangleY[this.texturesCount] = (short) (this.verticesCount + model_1.texTriangleY[var9]);
					this.texTriangleZ[this.texturesCount] = (short) (this.verticesCount + model_1.texTriangleZ[var9]);
					++this.texturesCount;
				}

				for (int var9 = 0; var9 < model_1.verticesCount; ++var9) {
					this.verticesX[this.verticesCount] = model_1.verticesX[var9];
					this.verticesY[this.verticesCount] = model_1.verticesY[var9];
					this.verticesZ[this.verticesCount] = model_1.verticesZ[var9];
					++this.verticesCount;
				}
			}
		}

		vertexNormals(true);
	}

	public Model(boolean colorFlag, boolean alphaFlag, boolean animated, Model model) {
		this(colorFlag, alphaFlag, animated, false, model);
	}

	public Model(boolean colorFlag, boolean alphaFlag, boolean animated, boolean textureFlag, Model model) {
		singleTile = false;
		verticesCount = model.verticesCount;
		triangleFaceCount = model.triangleFaceCount;
		texturesCount = model.texturesCount;
		if (animated) {
			verticesX = model.verticesX;
			verticesY = model.verticesY;
			verticesZ = model.verticesZ;
		} else {
			verticesX = new int[verticesCount];
			verticesY = new int[verticesCount];
			verticesZ = new int[verticesCount];
			for (int point = 0; point < verticesCount; point++) {
				verticesX[point] = model.verticesX[point];
				verticesY[point] = model.verticesY[point];
				verticesZ[point] = model.verticesZ[point];
			}

		}
		if (colorFlag) {
			faceColors = model.faceColors;
		} else {
			faceColors = new short[triangleFaceCount];
			System.arraycopy(model.faceColors, 0, faceColors, 0, triangleFaceCount);
		}

		if (textureFlag) {
			faceTextures = model.faceTextures;
		} else {
			if (model.faceTextures != null) {
				faceTextures = new short[triangleFaceCount];

				System.arraycopy(model.faceTextures, 0, faceTextures, 0, triangleFaceCount);
			}
		}

		if (alphaFlag) {
			faceTransparencies = model.faceTransparencies;
		} else {
			faceTransparencies = new byte[triangleFaceCount];
			if (model.faceTransparencies == null) {
				for (int l = 0; l < triangleFaceCount; l++) {
					faceTransparencies[l] = 0;
				}

			} else {
				System.arraycopy(model.faceTransparencies, 0, faceTransparencies, 0, triangleFaceCount);
			}
		}
		vertexSkins = model.vertexSkins;
		triangleSkinValues = model.triangleSkinValues;
		faceRenderType = model.faceRenderType;
		trianglePointsX = model.trianglePointsX;
		trianglePointsY = model.trianglePointsY;
		trianglePointsZ = model.trianglePointsZ;
		faceRenderPriorities = model.faceRenderPriorities;
		textureCoords = model.textureCoords;
		priority = model.priority;
		textureRenderTypes = model.textureRenderTypes;
		texTriangleX = model.texTriangleX;
		texTriangleY = model.texTriangleY;
		texTriangleZ = model.texTriangleZ;
		vertexNormals = model.vertexNormals;
		faceNormals = model.faceNormals;
		vertexNormalsOffsets = model.vertexNormalsOffsets;
		faceTextureUVCoordinates = model.faceTextureUVCoordinates;
		animayaScales = model.animayaScales;
		animayaGroups = model.animayaGroups;
		ambient = model.ambient;
		contrast = model.contrast;
	}

	public Model copyLight() {
		Model var1 = new Model();
		if (this.faceRenderType != null) {
			var1.faceRenderType = new int[this.triangleFaceCount];

			for (int var2 = 0; var2 < this.triangleFaceCount; ++var2) {
				var1.faceRenderType[var2] = this.faceRenderType[var2];
			}
		}

		var1.verticesCount = this.verticesCount;
		var1.triangleFaceCount = this.triangleFaceCount;
		var1.texturesCount = this.texturesCount;
		var1.verticesX = this.verticesX;
		var1.verticesY = this.verticesY;
		var1.verticesZ = this.verticesZ;
		var1.trianglePointsX = this.trianglePointsX;
		var1.trianglePointsY = this.trianglePointsY;
		var1.trianglePointsZ = this.trianglePointsZ;
		var1.faceRenderPriorities = this.faceRenderPriorities;
		var1.faceTransparencies = this.faceTransparencies;
		var1.textureCoords = this.textureCoords;
		var1.faceColors = this.faceColors;
		var1.faceTextures = this.faceTextures;
		var1.priority = this.priority;
		var1.textureRenderTypes = this.textureRenderTypes;
		var1.texTriangleX = this.texTriangleX;
		var1.texTriangleY = this.texTriangleY;
		var1.texTriangleZ = this.texTriangleZ;
		var1.vertexSkins = this.vertexSkins;
		var1.triangleSkinValues = this.triangleSkinValues;
		var1.groupedVertexLabels = this.groupedVertexLabels;
		var1.groupedTriangleLabels = this.groupedTriangleLabels;
		var1.vertexNormals = this.vertexNormals;
		var1.faceNormals = this.faceNormals;
		var1.ambient = this.ambient;
		var1.contrast = this.contrast;
		var1.faceTextureUVCoordinates = this.faceTextureUVCoordinates;
		var1.animayaScales = this.animayaScales;
		var1.animayaGroups = this.animayaGroups;
		return var1;
	}

	public Model hillskew(int[][] tileHeights, int localX, int tileHeightsUnknown, int localY, boolean copy, int contouredGround) {
		this.calculateBoundsCylinder();
		int var7 = localX - this.diagonal2DAboveOrigin;
		int var8 = localX + this.diagonal2DAboveOrigin;
		int var9 = localY - this.diagonal2DAboveOrigin;
		int var10 = localY + this.diagonal2DAboveOrigin;
		if (var7 >= 0 && var8 + 128 >> 7 < tileHeights.length && var9 >= 0 && var10 + 128 >> 7 < tileHeights[0].length) {
			var7 >>= 7;
			var8 = var8 + 127 >> 7;
			var9 >>= 7;
			var10 = var10 + 127 >> 7;
			if (tileHeightsUnknown == tileHeights[var7][var9] && tileHeightsUnknown == tileHeights[var8][var9]
					&& tileHeightsUnknown == tileHeights[var7][var10] && tileHeightsUnknown == tileHeights[var8][var10]) {
				return this;
			} else {
				Model var11;
				if (copy) {
					var11 = new Model();
					var11.verticesCount = this.verticesCount;
					var11.triangleFaceCount = this.triangleFaceCount;
					var11.texturesCount = this.texturesCount;
					var11.verticesX = this.verticesX;
					var11.verticesZ = this.verticesZ;
					var11.trianglePointsX = this.trianglePointsX;
					var11.trianglePointsY = this.trianglePointsY;
					var11.trianglePointsZ = this.trianglePointsZ;
					var11.colorsX = this.colorsX;
					var11.colorsY = this.colorsY;
					var11.colorsZ = this.colorsZ;
					var11.faceRenderPriorities = this.faceRenderPriorities;
					var11.faceTransparencies = this.faceTransparencies;
					var11.textureCoords = this.textureCoords;
					var11.faceTextures = this.faceTextures;
					var11.priority = this.priority;
					var11.texTriangleX = this.texTriangleX;
					var11.texTriangleY = this.texTriangleY;
					var11.texTriangleZ = this.texTriangleZ;
					var11.groupedVertexLabels = this.groupedVertexLabels;
					var11.groupedTriangleLabels = this.groupedTriangleLabels;
					var11.singleTile = this.singleTile;
					var11.verticesY = new int[var11.verticesCount];
					var11.faceTextureUVCoordinates = this.faceTextureUVCoordinates;
					var11.animayaScales = this.animayaScales;
					var11.animayaGroups = this.animayaGroups;

					var11.vertexNormals = this.vertexNormals;
					var11.faceNormals = this.faceNormals;
					var11.vertexNormalsX = this.vertexNormalsX;
					var11.vertexNormalsY = this.vertexNormalsY;
					var11.vertexNormalsZ = this.vertexNormalsZ;
				} else {
					var11 = this;
				}

				int var12;
				int var13;
				int var14;
				int var15;
				int var16;
				int x;
				int y;
				int var19;
				int var20;
				int var21;
				if (contouredGround == 0) {
					for (var12 = 0; var12 < var11.verticesCount; ++var12) {
						var13 = localX + this.verticesX[var12];
						var14 = localY + this.verticesZ[var12];
						var15 = var13 & 127;
						var16 = var14 & 127;
						x = var13 >> 7;
						y = var14 >> 7;
						var19 = tileHeights[x][y] * (128 - var15) + tileHeights[x + 1][y] * var15 >> 7;
						var20 = tileHeights[x][y + 1] * (128 - var15) + var15 * tileHeights[x + 1][y + 1] >> 7;
						var21 = var19 * (128 - var16) + var20 * var16 >> 7;
						var11.verticesY[var12] = var21 + this.verticesY[var12] - tileHeightsUnknown;
					}
				} else {
					for (var12 = 0; var12 < var11.verticesCount; ++var12) {
						var13 = (-this.verticesY[var12] << 16) / super.modelBaseY;
						if (var13 < contouredGround) {
							var14 = localX + this.verticesX[var12];
							var15 = localY + this.verticesZ[var12];
							var16 = var14 & 127;
							x = var15 & 127;
							y = var14 >> 7;
							var19 = var15 >> 7;
							var20 = tileHeights[y][var19] * (128 - var16) + tileHeights[y + 1][var19] * var16 >> 7;
							var21 = tileHeights[y][var19 + 1] * (128 - var16) + var16 * tileHeights[y + 1][var19 + 1] >> 7;
							int var22 = var20 * (128 - x) + var21 * x >> 7;
							var11.verticesY[var12] =
									(contouredGround - var13) * (var22 - tileHeightsUnknown) / contouredGround + this.verticesY[var12];
						}
					}
				}

				var11.resetBounds();
				return var11;
			}
		} else {
			return this;
		}
	}

	public Model hillskew2(int[][] heightMap, int x, int heightMapUnknown, int y, int contourGround) {
		this.calculateBounds();
		int var7 = x + this.diagonal2DAboveOrigin;
		int var8 = x + this.diagonal3D;
		int var9 = y + this.minZ;
		int var10 = y + this.maxZ;
		if (var7 >= 0 && var8 + 128 >> 7 < heightMap.length && var9 >= 0 && var10 + 128 >> 7 < heightMap[0].length) {
			var7 >>= 7;
			var8 = var8 + 127 >> 7;
			var9 >>= 7;
			var10 = var10 + 127 >> 7;
			if (heightMapUnknown == heightMap[var7][var9] && heightMapUnknown == heightMap[var8][var9] && heightMapUnknown == heightMap[var7][var10] && heightMapUnknown == heightMap[var8][var10]) {
				return this;
			} else {
				Model var11 = new Model();
				var11.verticesCount = this.verticesCount;
				var11.triangleFaceCount = this.triangleFaceCount;
				var11.texturesCount = this.texturesCount;
				var11.verticesX = this.verticesX;
				var11.verticesZ = this.verticesZ;
				var11.trianglePointsX = this.trianglePointsX;
				var11.trianglePointsY = this.trianglePointsY;
				var11.trianglePointsZ = this.trianglePointsZ;
				var11.faceRenderType = this.faceRenderType;
				var11.faceRenderPriorities = this.faceRenderPriorities;
				var11.faceTransparencies = this.faceTransparencies;
				var11.textureCoords = this.textureCoords;
				var11.faceColors = this.faceColors;
				var11.faceTextures = this.faceTextures;
				var11.priority = this.priority;
				var11.textureRenderTypes = this.textureRenderTypes;
				var11.texTriangleX = this.texTriangleX;
				var11.texTriangleY = this.texTriangleY;
				var11.texTriangleZ = this.texTriangleZ;
				var11.vertexSkins = this.vertexSkins;
				var11.triangleSkinValues = this.triangleSkinValues;
				var11.groupedVertexLabels = this.groupedVertexLabels;
				var11.groupedTriangleLabels = this.groupedTriangleLabels;
				var11.ambient = this.ambient;
				var11.contrast = this.contrast;
				var11.verticesY = new int[var11.verticesCount];
				var11.faceTextureUVCoordinates = this.faceTextureUVCoordinates;
				var11.animayaScales = this.animayaScales;
				var11.animayaGroups = this.animayaGroups;

				//var11.vertexNormals = this.vertexNormals;
				var11.faceNormals = this.faceNormals;
				var11.vertexNormalsX = this.vertexNormalsX;
				var11.vertexNormalsY = this.vertexNormalsY;
				var11.vertexNormalsZ = this.vertexNormalsZ;

				int var12;
				int var13;
				int var14;
				int var15;
				int var16;
				int var17;
				int var18;
				int var19;
				int var20;
				int var21;
				if (contourGround == 0) {
					for (var12 = 0; var12 < var11.verticesCount; ++var12) {
						var13 = x + this.verticesX[var12];
						var14 = y + this.verticesZ[var12];
						var15 = var13 & 127;
						var16 = var14 & 127;
						var17 = var13 >> 7;
						var18 = var14 >> 7;
						var19 = heightMap[var17][var18] * (128 - var15) + heightMap[var17 + 1][var18] * var15 >> 7;
						var20 = heightMap[var17][var18 + 1] * (128 - var15) + var15 * heightMap[var17 + 1][var18 + 1] >> 7;
						var21 = var19 * (128 - var16) + var20 * var16 >> 7;
						var11.verticesY[var12] = var21 + this.verticesY[var12] - heightMapUnknown;
					}
				} else {
					for (var12 = 0; var12 < var11.verticesCount; ++var12) {
						var13 = (-this.verticesY[var12] << 16) / super.modelBaseY;
						if (var13 < contourGround) {
							var14 = x + this.verticesX[var12];
							var15 = y + this.verticesZ[var12];
							var16 = var14 & 127;
							var17 = var15 & 127;
							var18 = var14 >> 7;
							var19 = var15 >> 7;
							var20 = heightMap[var18][var19] * (128 - var16) + heightMap[var18 + 1][var19] * var16 >> 7;
							var21 = heightMap[var18][var19 + 1] * (128 - var16) + var16 * heightMap[var18 + 1][var19 + 1] >> 7;
							int var22 = var20 * (128 - var17) + var21 * var17 >> 7;
							var11.verticesY[var12] =
									(contourGround - var13) * (var22 - heightMapUnknown) / contourGround + this.verticesY[var12];
						}
					}
				}

				var11.resetBounds();
				return var11;
			}
		} else {
			return this;
		}
	}

	public void buildSharedSequenceModel(Model model, boolean replaceAlpha) {
		verticesCount = model.verticesCount;
		triangleFaceCount = model.triangleFaceCount;
		texturesCount = model.texturesCount;

		if (sharedVerticesX.length < verticesCount) {
			sharedVerticesX = new int[verticesCount + 100];
			sharedVerticesY = new int[verticesCount + 100];
			sharedVerticesZ = new int[verticesCount + 100];
		}

		verticesX = sharedVerticesX;
		verticesY = sharedVerticesY;
		verticesZ = sharedVerticesZ;
		for (int point = 0; point < verticesCount; point++) {
			verticesX[point] = model.verticesX[point];
			verticesY[point] = model.verticesY[point];
			verticesZ[point] = model.verticesZ[point];
		}

		if (replaceAlpha) {
			faceTransparencies = model.faceTransparencies;
		} else {
			if (sharedTriangleAlpha.length < triangleFaceCount) {
				sharedTriangleAlpha = new byte[triangleFaceCount + 100];
			}
			faceTransparencies = sharedTriangleAlpha;
			if (model.faceTransparencies == null) {
				for (int face = 0; face < triangleFaceCount; face++) {
					faceTransparencies[face] = 0;
				}
			} else {
				if (triangleFaceCount >= 0) System.arraycopy(model.faceTransparencies, 0, faceTransparencies, 0, triangleFaceCount);
			}
		}

		faceTextures = model.faceTextures;
		faceRenderType = model.faceRenderType;
		faceColors = model.faceColors;
		faceRenderPriorities = model.faceRenderPriorities;
		textureCoords = model.textureCoords;
		priority = model.priority;
		textureRenderTypes = model.textureRenderTypes;
		groupedTriangleLabels = model.groupedTriangleLabels;
		groupedVertexLabels = model.groupedVertexLabels;
		trianglePointsX = model.trianglePointsX;
		trianglePointsY = model.trianglePointsY;
		trianglePointsZ = model.trianglePointsZ;
		colorsX = model.colorsX;
		colorsY = model.colorsY;
		colorsZ = model.colorsZ;
		texTriangleX = model.texTriangleX;
		texTriangleY = model.texTriangleY;
		texTriangleZ = model.texTriangleZ;
		faceNormals = model.faceNormals;
		vertexNormals = model.vertexNormals;
		vertexNormalsOffsets = model.vertexNormalsOffsets;
		faceTextureUVCoordinates = model.faceTextureUVCoordinates;
		vertexNormalsX = model.vertexNormalsX;
		vertexNormalsY = model.vertexNormalsY;
		vertexNormalsZ = model.vertexNormalsZ;
		singleTile = model.singleTile;

		this.animayaGroups = model.animayaGroups;
		this.animayaScales = model.animayaScales;

		resetBounds();
	}

	private int getFirstIdenticalVertexId(final Model model, final int vertex) {
		int vertexId = -1;
		final int x = model.verticesX[vertex];
		final int y = model.verticesY[vertex];
		final int z = model.verticesZ[vertex];
		for (int v = 0; v < this.verticesCount; v++) {
			if (x != this.verticesX[v] || y != this.verticesY[v] || z != this.verticesZ[v]) {
				continue;
			}
			vertexId = v;
			break;
		}

		if (vertexId == -1) {
			this.verticesX[this.verticesCount] = x;
			this.verticesY[this.verticesCount] = y;
			this.verticesZ[this.verticesCount] = z;
			if (model.vertexSkins != null) {
				this.vertexSkins[this.verticesCount] = model.vertexSkins[vertex];
			}

			if (model.animayaGroups != null) {
				this.animayaGroups[this.verticesCount] = model.animayaGroups[vertex];
				this.animayaScales[this.verticesCount] = model.animayaScales[vertex];
			}
			vertexId = this.verticesCount++;

		}
		return vertexId;
	}

	public void calculateBoundsCylinder() {
		if (this.boundsType != 1) {
			this.boundsType = 1;
			super.modelBaseY = 0;
			diagonal2DAboveOrigin = 0;
			maxY = 0;
			for (int vertex = 0; vertex < verticesCount; vertex++) {
				final int x = verticesX[vertex];
				final int y = verticesY[vertex];
				final int z = verticesZ[vertex];
				if (-y > super.modelBaseY) {
					super.modelBaseY = -y;
				}
				if (y > maxY) {
					maxY = y;
				}
				final int bounds = x * x + z * z;
				if (bounds > diagonal2DAboveOrigin) {
					diagonal2DAboveOrigin = bounds;
				}
			}

			diagonal2DAboveOrigin = (int)(Math.sqrt(diagonal2DAboveOrigin) + 0.98999999999999999);
			diagonal3DAboveOrigin = (int)(Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + super.modelBaseY * super.modelBaseY) + 0.98999999999999999);
			diagonal3D = diagonal3DAboveOrigin + (int)(Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + maxY * maxY) + 0.98999999999999999);
		}
	}

	void normalise() {
		if (this.boundsType != 2) {
			this.boundsType = 2;
			this.diagonal2DAboveOrigin = 0;

			for (int count = 0; count < this.verticesCount; ++count) {
				int x = this.verticesX[count];
				int y = this.verticesY[count];
				int z = this.verticesZ[count];
				int bounds = x * x + z * z + y * y;
				if (bounds > this.diagonal2DAboveOrigin) {
					this.diagonal2DAboveOrigin = bounds;
				}
			}

			this.diagonal2DAboveOrigin = (int) (Math.sqrt(this.diagonal2DAboveOrigin) + 0.99D);
			this.diagonal3DAboveOrigin = this.diagonal2DAboveOrigin;
			this.diagonal3D = this.diagonal2DAboveOrigin + this.diagonal2DAboveOrigin;
		}
	}

	public void calculateBounds() {
		if (boundsType != 3) {
			boundsType = 3;
			super.modelBaseY = 0;
			maxY = 0;
			minX = 999999;
			maxX = -999999;
			maxZ = -999999;
			minZ = 999999;
			for (int vertex = 0; vertex < verticesCount; vertex++) {
				final int x = verticesX[vertex];
				final int y = verticesY[vertex];
				final int z = verticesZ[vertex];
				if (x < minX) {
					minX = x;
				}
				if (x > maxX) {
					maxX = x;
				}
				if (z < minZ) {
					minZ = z;
				}
				if (z > maxZ) {
					maxZ = z;
				}
				if (-y > super.modelBaseY) {
					super.modelBaseY = -y;
				}
				if (y > maxY) {
					maxY = y;
				}
			}
		}
	}

	public void generateBones() {
		if (vertexSkins != null) {
			int[] ai = new int[256];
			int j = 0;
			for (int l = 0; l < verticesCount; l++) {
				int j1 = vertexSkins[l];
				ai[j1]++;
				if (j1 > j)
					j = j1;
			}
			groupedVertexLabels = new int[j + 1][];
			for (int k1 = 0; k1 <= j; k1++) {
				groupedVertexLabels[k1] = new int[ai[k1]];
				ai[k1] = 0;
			}
			for (int j2 = 0; j2 < verticesCount; j2++) {
				int l2 = vertexSkins[j2];
				groupedVertexLabels[l2][ai[l2]++] = j2;
			}
			vertexSkins = null;
		}
		if (triangleSkinValues != null) {
			int[] ai1 = new int[256];
			int k = 0;
			for (int i1 = 0; i1 < triangleFaceCount; i1++) {
				int l1 = triangleSkinValues[i1];
				ai1[l1]++;
				if (l1 > k)
					k = l1;
			}
			groupedTriangleLabels = new int[k + 1][];
			for (int uid = 0; uid <= k; uid++) {
				groupedTriangleLabels[uid] = new int[ai1[uid]];
				ai1[uid] = 0;
			}
			for (int k2 = 0; k2 < triangleFaceCount; k2++) {
				int i3 = triangleSkinValues[k2];
				groupedTriangleLabels[i3][ai1[i3]++] = k2;
			}
			triangleSkinValues = null;
		}
	}

	private void transform(int animationType, int[] skinArray, int x, int y, int z) {

		int length = skinArray.length;
		if (animationType == 0) {
			int i = 0;
			transformTempX = 0;
			transformTempY = 0;
			transformTempZ = 0;
			for (int k2 = 0; k2 < length; k2++) {
				int skin = skinArray[k2];
				if (skin < groupedVertexLabels.length) {
					int[] group = groupedVertexLabels[skin];
					for (int i5 = 0; i5 < group.length; i5++) {
						int offset = group[i5];
						transformTempX += verticesX[offset];
						transformTempY += verticesY[offset];
						transformTempZ += verticesZ[offset];
						i++;
					}

				}
			}

			if (i > 0) {
				transformTempX = transformTempX / i + x;
				transformTempY = transformTempY / i + y;
				transformTempZ = transformTempZ / i + z;
				return;
			} else {
				transformTempX = x;
				transformTempY = y;
				transformTempZ = z;
				return;
			}
		}
		if (animationType == 1) {
			for (int k1 = 0; k1 < length; k1++) {
				int skin = skinArray[k1];
				if (skin < groupedVertexLabels.length) {
					int[] group = groupedVertexLabels[skin];
					for (int i4 = 0; i4 < group.length; i4++) {
						int offset = group[i4];
						verticesX[offset] += x;
						verticesY[offset] += y;
						verticesZ[offset] += z;
					}

				}
			}

			return;
		}
		if (animationType == 2) {
			for (int l1 = 0; l1 < length; l1++) {
				int skin = skinArray[l1];
				if (skin < groupedVertexLabels.length) {
					int[] group = groupedVertexLabels[skin];
					for (int j4 = 0; j4 < group.length; j4++) {
						int offset = group[j4];
						verticesX[offset] -= transformTempX;
						verticesY[offset] -= transformTempY;
						verticesZ[offset] -= transformTempZ;
						int xOff = (x & 0xff) * 8;
						int yOff = (y & 0xff) * 8;
						int zOff = (z & 0xff) * 8;
						if (zOff != 0) {
							int sin = SINE[zOff];
							int cos = COSINE[zOff];
							int loc = verticesY[offset] * sin + verticesX[offset] * cos >> 16;
							verticesY[offset] = verticesY[offset] * cos - verticesX[offset] * sin >> 16;
							verticesX[offset] = loc;
						}
						if (xOff != 0) {
							int sin = SINE[xOff];
							int cos = COSINE[xOff];
							int loc = verticesY[offset] * cos - verticesZ[offset] * sin >> 16;
							verticesZ[offset] = verticesY[offset] * sin + verticesZ[offset] * cos >> 16;
							verticesY[offset] = loc;
						}
						if (yOff != 0) {
							int sin = SINE[yOff];
							int cos = COSINE[yOff];
							int loc = verticesZ[offset] * sin + verticesX[offset] * cos >> 16;
							verticesZ[offset] = verticesZ[offset] * cos - verticesX[offset] * sin >> 16;
							verticesX[offset] = loc;
						}
						verticesX[offset] += transformTempX;
						verticesY[offset] += transformTempY;
						verticesZ[offset] += transformTempZ;
					}

				}
			}

			return;
		}
		if (animationType == 3) {
			for (int uid = 0; uid < length; uid++) {
				int skin = skinArray[uid];
				if (skin < groupedVertexLabels.length) {
					int[] group = groupedVertexLabels[skin];
					for (int k4 = 0; k4 < group.length; k4++) {
						int offset = group[k4];
						verticesX[offset] -= transformTempX;
						verticesY[offset] -= transformTempY;
						verticesZ[offset] -= transformTempZ;
						verticesX[offset] = (verticesX[offset] * x) / 128;
						verticesY[offset] = (verticesY[offset] * y) / 128;
						verticesZ[offset] = (verticesZ[offset] * z) / 128;
						verticesX[offset] += transformTempX;
						verticesY[offset] += transformTempY;
						verticesZ[offset] += transformTempZ;
					}

				}
			}

			return;
		}
		if (animationType == 5 && groupedTriangleLabels != null && faceTransparencies != null) {
			for (int j2 = 0; j2 < length; j2++) {
				int skin = skinArray[j2];
				if (skin < groupedTriangleLabels.length) {
					int[] group = groupedTriangleLabels[skin];
					for (int l4 = 0; l4 < group.length; l4++) {
						int var13 = group[l4];
						int var14 = (this.faceTransparencies[var13] & 255) + x * 8;
						if (var14 < 0) {
							var14 = 0;
						} else if (var14 > 255) {
							var14 = 255;
						}

						this.faceTransparencies[var13] = (byte)var14;
					}

				}
			}
		}
	}

	public void playSkeletal(final int frameId, final int tick) {
		//System.err.println("playSkeletal " + tick);
		SkeletalFrame skeletalFrameset = SkeletalFrame.getSkeletalFrame(frameId);
		transformSkeletal(skeletalFrameset, tick);
	}

	public void playSkeletalDouble(Animation primary, Animation secondary, int primaryTick, int secondaryTick) {
		int primarySkeletalId = primary.getSkeletalFrameId();
		int secondarySkeletalId = secondary.getSkeletalFrameId();
		SkeletalFrame primarySkeletalFrameset = SkeletalFrame.getSkeletalFrame(primarySkeletalId);
		if (primarySkeletalFrameset == null) return;
		SkeletalFrame secondarySkeletalFrameset = SkeletalFrame.getSkeletalFrame(secondarySkeletalId);
		if (secondarySkeletalFrameset == null) return;
		boolean[] mask = primary.getBooleanMasks();
		FrameBase base = primarySkeletalFrameset.getFrameBase();

		transformSkeletalMultiple(base, primarySkeletalFrameset, primaryTick, mask, false, !secondary.isSkeletalAnimation());
		transformSkeletalMultiple(base, secondarySkeletalFrameset, secondaryTick, mask, true, true);
	}

	public void transformSkeletalMultiple(FrameBase base, SkeletalFrame skeletalFrame, int tick, boolean[] mask, boolean state, boolean hasSkeletalAnimations) {
		ABW boneAnimationWrapper = base.getAbw();
		if (boneAnimationWrapper != null) {
			boneAnimationWrapper.ut(skeletalFrame, tick, mask, state);
			if (hasSkeletalAnimations) {
				this.transformSkeletal(boneAnimationWrapper, skeletalFrame.getFid());
			}
		}
		if (!state && skeletalFrame.hExisting()) {
			this.applyAlphaTransforms(skeletalFrame, tick);
		}
	}

	public void transformSkeletal(SkeletalFrame skeletalFrame, int tick) {
		if (skeletalFrame == null) return;

		FrameBase base = skeletalFrame.getFrameBase();
		ABW abw = base.getAbw();
		//tick = 0;
		if (abw != null) {
			abw.ut(skeletalFrame, tick);
			this.transformSkeletal(abw, skeletalFrame.getFid());
		}
		if (skeletalFrame.hExisting()) {
			this.applyAlphaTransforms(skeletalFrame, tick);
		}
	}

	private void transformSkeletal(ABW animBoneWrapper, int frameId) {
		if (animayaGroups == null) {
			return;
		}

		for (int vertex = 0; vertex < verticesCount; vertex++) {
			int[] bonesForVertex = animayaGroups[vertex];
			if (bonesForVertex != null && bonesForVertex.length != 0) {
				int[] scalesForVertex = animayaScales[vertex];
				fbm.zero(); // instead of identity() likely because of the scaling below
				for (int i = 0; i < bonesForVertex.length; i++) {
					int bi = bonesForVertex[i];
					AB bone = animBoneWrapper.getAB(bi);
					if (bone != null) {
						sm.sc(((float) (scalesForVertex[i])) / 255.0F);
						cbm.sf(bone.gcbm(frameId));
						cbm.mp(sm);
						fbm.a(cbm);
					}
				}
				this.transformVertex(vertex, fbm);
			}
		}
	}

	private void transformVertex(int vertex, Matrix4f bm) {
		float x = ((float) (verticesX[vertex]));
		float y = ((float) (-verticesY[vertex]));
		float z = ((float) (-verticesZ[vertex]));

		// The position is treated as a 4D vector with it's w-component being 1.0, that means that it will represent a position/location in 3d space
		// rather than a direction
		float w = 1f;
		int newX = ((int) (bm.values[0] * x + bm.values[4] * y + bm.values[8] * z + bm.values[12] * w));
		int newY = -((int) (bm.values[1] * x + bm.values[5] * y + bm.values[9] * z + bm.values[13] * w));
		int newZ = -((int) (bm.values[2] * x + bm.values[6] * y + bm.values[10] * z + bm.values[14] * w));

		verticesX[vertex] = newX;
		verticesY[vertex] = newY;
		verticesZ[vertex] = newZ;
	}

	private void applyAlphaTransforms(SkeletalFrame skeletalFrame, int tick) {
		FrameBase base = skeletalFrame.getFrameBase();
		for (int baseIndex = 0; baseIndex < base.getLength(); baseIndex++) {
			int type = base.getTypes()[baseIndex];
			if (type == 5 && skeletalFrame.tt != null && skeletalFrame.tt[baseIndex] != null && skeletalFrame.tt[baseIndex][0] != null && groupedTriangleLabels != null && faceTransparencies != null) {
				TO TO = skeletalFrame.tt[baseIndex][0];
				int[] vertexLabels = base.getFrameMaps()[baseIndex];
				for (int label : vertexLabels) {
					if (label < groupedTriangleLabels.length) {
						int[] triangleLabels = groupedTriangleLabels[label];
						for (int triangleIndex : triangleLabels) {
							int currentAlpha = faceTransparencies[triangleIndex];
							int alpha = ((int) (((float) (currentAlpha)) + TO.gv(tick) * 255.0F));
							alpha = MathUtils.clamp(alpha, 0, 255);
							faceTransparencies[triangleIndex] = (byte) alpha;
						}
					}
				}
			}
		}
	}

	public void interpolate(int frameId) {
		if (groupedVertexLabels == null)
			return;

		if (frameId == -1)
			return;

		NormalFrame frame = (NormalFrame) Frame.getFrame(frameId);
		if (frame == null)
			return;

		FrameBase base = frame.getFrameBase();
		transformTempX = 0;
		transformTempY = 0;
		transformTempZ = 0;
		for (int k = 0; k < frame.translatorCount; k++) {
			int l = frame.indexFrameIds[k];
			transform(base.types[l],
				base.frameMaps[l],
				frame.translatorX[k],
				frame.translatorY[k],
				frame.translatorZ[k]);
		}

		this.resetBounds();
	}

	public void applyAnimationFrames(int[] ai, int j, int k) {
		if (k == -1)
			return;
		if (ai == null || j == -1) {
			applyTransform(k);
			return;
		}
		NormalFrame class36 = (NormalFrame) Frame.getFrame(k);
		if (class36 == null)
			return;
		NormalFrame class36_1 = (NormalFrame) Frame.getFrame(j);
		if (class36_1 == null) {
			applyTransform(k);
			return;
		}
		FrameBase class18 = class36.getFrameBase();
		transformTempX = 0;
		transformTempY = 0;
		transformTempZ = 0;
		int l = 0;
		int i1 = ai[l++];
		for (int j1 = 0; j1 < class36.translatorCount; j1++) {
			int k1;
			for (k1 = class36.indexFrameIds[j1]; k1 > i1; i1 = ai[l++])
				;
			if (k1 != i1 || class18.types[k1] == 0)
				transformSkin(class18.types[k1], class18.frameMaps[k1], class36.translatorX[j1], class36.translatorY[j1], class36.translatorZ[j1]);
		}

		transformTempX = 0;
		transformTempY = 0;
		transformTempZ = 0;
		l = 0;
		i1 = ai[l++];
		for (int l1 = 0; l1 < class36_1.translatorCount; l1++) {
			int i2;
			for (i2 = class36_1.indexFrameIds[l1]; i2 > i1; i1 = ai[l++])
				;
			if (i2 == i1 || class18.types[i2] == 0)
				transformSkin(class18.types[i2], class18.frameMaps[i2], class36_1.translatorX[l1], class36_1.translatorY[l1], class36_1.translatorZ[l1]);
		}
		this.resetBounds();
	}

	public void applyAnimationFrame(boolean tween, int frame, int nextFrame, int end, int cycle) {
		if (!tween) {
			applyTransform(frame);
			return;
		}
		interpolateFrames(frame, nextFrame, end, cycle);
	}

	void transformSkin(int i, int[] ai, int j, int k, int l) {

		int i1 = ai.length;
		if (i == 0) {
			int j1 = 0;
			transformTempX = 0;
			transformTempY = 0;
			transformTempZ = 0;
			for (int k2 = 0; k2 < i1; k2++) {
				int l3 = ai[k2];
				if (l3 < groupedVertexLabels.length) {
					int[] ai5 = groupedVertexLabels[l3];
					for (int i5 = 0; i5 < ai5.length; i5++) {
						int j6 = ai5[i5];
						transformTempX += verticesX[j6];
						transformTempY += verticesY[j6];
						transformTempZ += verticesZ[j6];
						j1++;
					}

				}
			}

			if (j1 > 0) {
				transformTempX = transformTempX / j1 + j;
				transformTempY = transformTempY / j1 + k;
				transformTempZ = transformTempZ / j1 + l;
				return;
			} else {
				transformTempX = j;
				transformTempY = k;
				transformTempZ = l;
				return;
			}
		}
		if (i == 1) {
			for (int k1 = 0; k1 < i1; k1++) {
				int l2 = ai[k1];
				if (l2 < groupedVertexLabels.length) {
					int[] ai1 = groupedVertexLabels[l2];
					for (int i4 = 0; i4 < ai1.length; i4++) {
						int j5 = ai1[i4];
						verticesX[j5] += j;
						verticesY[j5] += k;
						verticesZ[j5] += l;
					}

				}
			}

			return;
		}
		if (i == 2) {
			for (int l1 = 0; l1 < i1; l1++) {
				int i3 = ai[l1];
				if (i3 < groupedVertexLabels.length) {
					int[] ai2 = groupedVertexLabels[i3];
					for (int j4 = 0; j4 < ai2.length; j4++) {
						int k5 = ai2[j4];
						verticesX[k5] -= transformTempX;
						verticesY[k5] -= transformTempY;
						verticesZ[k5] -= transformTempZ;
						int k6 = (j & 0xff) * 8;
						int l6 = (k & 0xff) * 8;
						int i7 = (l & 0xff) * 8;
						if (i7 != 0) {
							int j7 = SINE[i7];
							int i8 = COSINE[i7];
							int l8 = verticesY[k5] * j7 + verticesX[k5] * i8 >> 16;
							verticesY[k5] = verticesY[k5] * i8 - verticesX[k5] * j7 >> 16;
							verticesX[k5] = l8;
						}
						if (k6 != 0) {
							int k7 = SINE[k6];
							int j8 = COSINE[k6];
							int i9 = verticesY[k5] * j8 - verticesZ[k5] * k7 >> 16;
							verticesZ[k5] = verticesY[k5] * k7 + verticesZ[k5] * j8 >> 16;
							verticesY[k5] = i9;
						}
						if (l6 != 0) {
							int l7 = SINE[l6];
							int k8 = COSINE[l6];
							int j9 = verticesZ[k5] * l7 + verticesX[k5] * k8 >> 16;
							verticesZ[k5] = verticesZ[k5] * k8 - verticesX[k5] * l7 >> 16;
							verticesX[k5] = j9;
						}
						verticesX[k5] += transformTempX;
						verticesY[k5] += transformTempY;
						verticesZ[k5] += transformTempZ;
					}

				}
			}
			return;
		}
		if (i == 3) {
			for (int i2 = 0; i2 < i1; i2++) {
				int j3 = ai[i2];
				if (j3 < groupedVertexLabels.length) {
					int[] ai3 = groupedVertexLabels[j3];
					for (int k4 = 0; k4 < ai3.length; k4++) {
						int l5 = ai3[k4];
						verticesX[l5] -= transformTempX;
						verticesY[l5] -= transformTempY;
						verticesZ[l5] -= transformTempZ;
						verticesX[l5] = (verticesX[l5] * j) / 128;
						verticesY[l5] = (verticesY[l5] * k) / 128;
						verticesZ[l5] = (verticesZ[l5] * l) / 128;
						verticesX[l5] += transformTempX;
						verticesY[l5] += transformTempY;
						verticesZ[l5] += transformTempZ;
					}
				}
			}
			return;
		}
		if (i == 5 && groupedTriangleLabels != null && faceTransparencies != null) {
			for (int j2 = 0; j2 < i1; j2++) {
				int k3 = ai[j2];
				if (k3 < groupedTriangleLabels.length) {
					int[] ai4 = groupedTriangleLabels[k3];
					for (int l4 = 0; l4 < ai4.length; l4++) {
						int i6 = ai4[l4];
						int var14 = (this.faceTransparencies[i6] & 255) + j * 8;
						if (var14 < 0) {
							var14 = 0;
						} else if (var14 > 255) {
							var14 = 255;
						}

						this.faceTransparencies[i6] = (byte)var14;
					}
				}
			}
		}
	}

	public void applyTransform(int id) {
		if (groupedVertexLabels == null)
			return;
		if (id == -1)
			return;
		NormalFrame animationFrame = (NormalFrame) Frame.getFrame(id);
		if (animationFrame == null)
			return;
		FrameBase skin = animationFrame.getFrameBase();
		transformTempX = 0;
		transformTempY = 0;
		transformTempZ = 0;
		for (int k = 0; k < animationFrame.translatorCount; k++) {
			int l = animationFrame.indexFrameIds[k];
			transformSkin(skin.types[l], skin.frameMaps[l], animationFrame.translatorX[k], animationFrame.translatorY[k], animationFrame.translatorZ[k]);
		}
		this.resetBounds();
	}

	public void interpolateFrames(int frame, int nextFrame, int end, int cycle) {

		if ((groupedVertexLabels != null && frame != -1)) {
			NormalFrame currentAnimation = (NormalFrame) Frame.getFrame(frame);
			if (currentAnimation == null)
				return;
			FrameBase currentList = currentAnimation.getFrameBase();
			transformTempX = 0;
			transformTempY = 0;
			transformTempZ = 0;
			NormalFrame nextAnimation = null;
			FrameBase nextList = null;
			if (nextFrame != -1) {
				nextAnimation = (NormalFrame) Frame.getFrame(nextFrame);
				if (nextAnimation == null || nextAnimation.getFrameBase() == null)
					return;
				FrameBase nextSkin = nextAnimation.getFrameBase();
				if (nextSkin != currentList)
					nextAnimation = null;
				nextList = nextSkin;
			}
			if (nextAnimation == null || nextList == null) {
				for (int opcodeLinkTableIdx = 0; opcodeLinkTableIdx < currentAnimation.translatorCount; opcodeLinkTableIdx++) {
					int i_264_ = currentAnimation.indexFrameIds[opcodeLinkTableIdx];
					transformSkin(currentList.types[i_264_], currentList.frameMaps[i_264_], currentAnimation.translatorX[opcodeLinkTableIdx], currentAnimation.translatorY[opcodeLinkTableIdx], currentAnimation.translatorZ[opcodeLinkTableIdx]);
				}
			} else {

				for (int i1 = 0; i1 < currentAnimation.translatorCount; i1++) {
					int n1 = currentAnimation.indexFrameIds[i1];
					int opcode = currentList.types[n1];
					int[] skin = currentList.frameMaps[n1];
					int x = currentAnimation.translatorX[i1];
					int y = currentAnimation.translatorY[i1];
					int z = currentAnimation.translatorZ[i1];
					boolean found = false;
					label0: for (int i2 = 0; i2 < nextAnimation.translatorCount; i2++) {
						int n2 = nextAnimation.indexFrameIds[i2];
						if (nextList.frameMaps[n2].equals(skin)) {
							//Opcode 3 = Rotation
							if (opcode != 2) {
								x += (nextAnimation.translatorX[i2] - x) * cycle / end;
								y += (nextAnimation.translatorY[i2] - y) * cycle / end;
								z += (nextAnimation.translatorZ[i2] - z) * cycle / end;
							} else {
								x &= 0x7ff;
								y &= 0x7ff;
								z &= 0x7ff;
								int dx = nextAnimation.translatorX[i2] - x & 0x7ff;
								int dy = nextAnimation.translatorY[i2] - y & 0x7ff;
								int dz = nextAnimation.translatorZ[i2] - z & 0x7ff;
								if (dx >= 1024) {
									dx -= 2048;
								}
								if (dy >= 1024) {
									dy -= 2048;
								}
								if (dz >= 1024) {
									dz -= 2048;
								}
								x = x + dx * cycle / end & 0x7ff;
								y = y + dy * cycle / end & 0x7ff;
								z = z + dz * cycle / end & 0x7ff;
							}
							found = true;
							break;
						}
					}
					if (!found) {
						if (opcode != 3 && opcode != 2) {
							x = x * (end - cycle) / end;
							y = y * (end - cycle) / end;
							z = z * (end - cycle) / end;
						} else if (opcode == 3) {
							x = (x * (end - cycle) + (cycle << 7)) / end;
							y = (y * (end - cycle) + (cycle << 7)) / end;
							z = (z * (end - cycle) + (cycle << 7)) / end;
						} else {
							x &= 0x7ff;
							y &= 0x7ff;
							z &= 0x7ff;
							int dx = -x & 0x7ff;
							int dy = -y & 0x7ff;
							int dz = -z & 0x7ff;
							if (dx >= 1024) {
								dx -= 2048;
							}
							if (dy >= 1024) {
								dy -= 2048;
							}
							if (dz >= 1024) {
								dz -= 2048;
							}
							x = x + dx * cycle / end & 0x7ff;
							y = y + dy * cycle / end & 0x7ff;
							z = z + dz * cycle / end & 0x7ff;
						}
					}
					transformSkin(opcode, skin, x, y, z);
				}
			}
		}
	}


	public void mix(int[] label, int idle, int current) {
		if (current == -1)
			return;

		if (label == null || idle == -1) {
			interpolate(current);
			return;
		}
		NormalFrame anim = (NormalFrame) Frame.getFrame(current);
		if (anim == null)
			return;

		NormalFrame skin = (NormalFrame) Frame.getFrame(idle);
		if (skin == null) {
			interpolate(current);
			return;
		}
		FrameBase list = anim.getFrameBase();
		transformTempX = 0;
		transformTempY = 0;
		transformTempZ = 0;
		int id = 0;
		int table = label[id++];
		for (int index = 0; index < anim.translatorCount; index++) {
			int condition;
			for (condition = anim.indexFrameIds[index]; condition > table; table = label[id++]) {
			}

			if (condition != table || list.types[condition] == 0) {
				this.transform(list.types[condition], list.frameMaps[condition], anim.translatorX[index], anim.translatorY[index], anim.translatorZ[index]);
			}
		}

		transformTempX = 0;
		transformTempY = 0;
		transformTempZ = 0;
		id = 0;
		table = label[id++];
		for (int index = 0; index < skin.translatorCount; index++) {
			int condition;
			for (condition = skin.indexFrameIds[index]; condition > table; table = label[id++]) {
			}

			if (condition == table || list.types[condition] == 0) {
				this.transform(list.types[condition], list.frameMaps[condition], skin.translatorX[index], skin.translatorY[index], skin.translatorZ[index]);
			}
		}

		this.resetBounds();
	}

	public void rotate90Degrees() {
		for (int vertex = 0; vertex < verticesCount; vertex++) {
			int x = verticesX[vertex];
			verticesX[vertex] = verticesZ[vertex];
			verticesZ[vertex] = -x;
		}

		this.resetBounds();
	}

	public void rotateBy180() {
		for (int i = 0; i < verticesCount; i++) {
			verticesX[i] = -verticesX[i];
			verticesZ[i] = -verticesZ[i];
		}

		resetBounds();
	}

	public void rotateBy270() {
		for (int i = 0; i < verticesCount; i++) {
			int tmp = verticesZ[i];
			verticesZ[i] = verticesX[i];
			verticesX[i] = -tmp;
		}

		resetBounds();
	}

	public void rotateZ(int factor) {
		int sin = SINE[factor];
		int cos = COSINE[factor];
		for (int point = 0; point < verticesCount; point++) {
			int y = verticesY[point] * cos - verticesZ[point] * sin >> 16;
			verticesZ[point] = verticesY[point] * sin + verticesZ[point] * cos >> 16;
			verticesY[point] = y;
		}

		this.resetBounds();
	}

	public void offsetBy(int x, int y, int z) {
		for (int point = 0; point < verticesCount; point++) {
			verticesX[point] += x;
			verticesY[point] += y;
			verticesZ[point] += z;
		}

		this.resetBounds();
	}

	public void recolor(short found, short replace) {
		if(faceColors != null) {
			for (int face = 0; face < triangleFaceCount; face++) {
				if (faceColors[face] == found) {
					faceColors[face] = replace;
				}
			}
		}
	}

	public void recolor(int found, int replace) {
		recolor((short) found, (short) replace);
	}

	public void retexture(short found, short replace) {
		if(faceTextures != null) {
			for (int face = 0; face < triangleFaceCount; face++) {
				if (faceTextures[face] == found) {
					faceTextures[face] = replace;
				}
			}
		}
	}

	public void mirror() {
		for (int vertex = 0; vertex < verticesCount; vertex++)
			verticesZ[vertex] = -verticesZ[vertex];

		for (int face = 0; face < triangleFaceCount; face++) {
			int newTriangleZ = trianglePointsX[face];
			trianglePointsX[face] = trianglePointsZ[face];
			trianglePointsZ[face] = newTriangleZ;
		}
	}

	public void scale(int x, int z, int y) {
		for (int index = 0; index < verticesCount; index++) {
			verticesX[index] = (verticesX[index] * x) / 128;
			verticesY[index] = (verticesY[index] * y) / 128;
			verticesZ[index] = (verticesZ[index] * z) / 128;
		}

		this.resetBounds();
	}

	public void calculateVertexNormals() {
		if (vertexNormals == null) {
			vertexNormals = new VertexNormal[verticesCount];

			int var1;
			for (var1 = 0; var1 < verticesCount; ++var1) {
				vertexNormals[var1] = new VertexNormal();
			}

			for (var1 = 0; var1 < triangleFaceCount; ++var1) {
				final int var2 = trianglePointsX[var1];
				final int var3 = trianglePointsY[var1];
				final int var4 = trianglePointsZ[var1];
				final int var5 = verticesX[var3] - verticesX[var2];
				final int var6 = verticesY[var3] - verticesY[var2];
				final int var7 = verticesZ[var3] - verticesZ[var2];
				final int var8 = verticesX[var4] - verticesX[var2];
				final int var9 = verticesY[var4] - verticesY[var2];
				final int var10 = verticesZ[var4] - verticesZ[var2];
				int var11 = var6 * var10 - var9 * var7;
				int var12 = var7 * var8 - var10 * var5;

				int var13;
				for (var13 = var5 * var9 - var8 * var6;
					 var11 > 8192 || var12 > 8192 || var13 > 8192 || var11 < -8192 || var12 < -8192 || var13 < -8192; var13 >>= 1) {
					var11 >>= 1;
					var12 >>= 1;
				}

				int var14 = (int) Math.sqrt(var11 * var11 + var12 * var12 + var13 * var13);
				if (var14 <= 0) {
					var14 = 1;
				}

				var11 = var11 * 256 / var14;
				var12 = var12 * 256 / var14;
				var13 = var13 * 256 / var14;
				final int var15;
				if (faceRenderType == null) {
					var15 = 0;
				} else {
					var15 = faceRenderType[var1];
				}

				if (var15 == 0) {
					VertexNormal var16 = vertexNormals[var2];
					var16.x += var11;
					var16.y += var12;
					var16.z += var13;
					++var16.magnitude;
					var16 = vertexNormals[var3];
					var16.x += var11;
					var16.y += var12;
					var16.z += var13;
					++var16.magnitude;
					var16 = vertexNormals[var4];
					var16.x += var11;
					var16.y += var12;
					var16.z += var13;
					++var16.magnitude;
				} else if (var15 == 1) {
					if (faceNormals == null) {
						faceNormals = new FaceNormal[triangleFaceCount];
					}

					final FaceNormal var17 = faceNormals[var1] = new FaceNormal();
					var17.x = var11;
					var17.y = var12;
					var17.z = var13;
				}
			}
		}
	}

	public void light(int ambient, int contrast, int x, int y, int z, boolean entity) {
		this.ambient = (short) ambient;
		this.contrast = (short) contrast;

		this.calculateVertexNormals();
		final int magnitude = (int) Math.sqrt(x * x + y * y + z * z);
		final int var7 = contrast * magnitude >> 8;
		colorsX = new int[this.triangleFaceCount];
		colorsY = new int[this.triangleFaceCount];
		colorsZ = new int[this.triangleFaceCount];

		for (int var16 = 0; var16 < this.triangleFaceCount; ++var16) {
			int var17; //should be byte
			if (this.faceRenderType == null) {
				var17 = 0;
			} else {
				var17 = this.faceRenderType[var16];
			}

			byte var18;
			if (this.faceTransparencies == null) {
				var18 = 0;
			} else {
				var18 = this.faceTransparencies[var16];
			}

			short var12;
			if (this.faceTextures == null) {
				var12 = -1;
			} else {
				var12 = this.faceTextures[var16];
			}

			if (var18 == -2) {
				var17 = 3;
			}

			if (var18 == -1) {
				var17 = 2;
			}

			VertexNormal var13;
			int var14;
			FaceNormal var19;
			if (var12 == -1) {
				if (var17 != 0) {
					if (var17 == 1) {
						var19 = this.faceNormals[var16];
						var14 = (y * var19.y + z * var19.z + x * var19.x) / (var7 / 2 + var7) + ambient;
						colorsX[var16] = method2792(this.faceColors[var16] & '\uffff', var14);
						colorsZ[var16] = -1;
					} else if (var17 == 3) {
						colorsX[var16] = 128;
						colorsZ[var16] = -1;
					} else {
						colorsZ[var16] = -2;
					}
				} else {
					int var15 = this.faceColors[var16] & '\uffff';
					if (this.vertexNormalsOffsets != null && this.vertexNormalsOffsets[this.trianglePointsX[var16]] != null) {
						var13 = this.vertexNormalsOffsets[this.trianglePointsX[var16]];
					} else {
						var13 = this.vertexNormals[this.trianglePointsX[var16]];
					}

					var14 = (y * var13.y + z * var13.z + x * var13.x) / (var7 * var13.magnitude) + ambient;
					colorsX[var16] = method2792(var15, var14);
					if (this.vertexNormalsOffsets != null && this.vertexNormalsOffsets[this.trianglePointsY[var16]] != null) {
						var13 = this.vertexNormalsOffsets[this.trianglePointsY[var16]];
					} else {
						var13 = this.vertexNormals[this.trianglePointsY[var16]];
					}

					var14 = (y * var13.y + z * var13.z + x * var13.x) / (var7 * var13.magnitude) + ambient;
					colorsY[var16] = method2792(var15, var14);
					if (this.vertexNormalsOffsets != null && this.vertexNormalsOffsets[this.trianglePointsZ[var16]] != null) {
						var13 = this.vertexNormalsOffsets[this.trianglePointsZ[var16]];
					} else {
						var13 = this.vertexNormals[this.trianglePointsZ[var16]];
					}

					var14 = (y * var13.y + z * var13.z + x * var13.x) / (var7 * var13.magnitude) + ambient;
					colorsZ[var16] = method2792(var15, var14);
				}
			} else if (var17 != 0) {
				if (var17 == 1) {
					var19 = this.faceNormals[var16];
					var14 = (y * var19.y + z * var19.z + x * var19.x) / (var7 / 2 + var7) + ambient;
					colorsX[var16] = method2820(var14);
					colorsZ[var16] = -1;
				} else {
					colorsZ[var16] = -2;
				}
			} else {
				if (this.vertexNormalsOffsets != null && this.vertexNormalsOffsets[this.trianglePointsX[var16]] != null) {
					var13 = this.vertexNormalsOffsets[this.trianglePointsX[var16]];
				} else {
					var13 = this.vertexNormals[this.trianglePointsX[var16]];
				}

				var14 = (y * var13.y + z * var13.z + x * var13.x) / (var7 * var13.magnitude) + ambient;
				colorsX[var16] = method2820(var14);
				if (this.vertexNormalsOffsets != null && this.vertexNormalsOffsets[this.trianglePointsY[var16]] != null) {
					var13 = this.vertexNormalsOffsets[this.trianglePointsY[var16]];
				} else {
					var13 = this.vertexNormals[this.trianglePointsY[var16]];
				}

				var14 = (y * var13.y + z * var13.z + x * var13.x) / (var7 * var13.magnitude) + ambient;
				colorsY[var16] = method2820(var14);
				if (this.vertexNormalsOffsets != null && this.vertexNormalsOffsets[this.trianglePointsZ[var16]] != null) {
					var13 = this.vertexNormalsOffsets[this.trianglePointsZ[var16]];
				} else {
					var13 = this.vertexNormals[this.trianglePointsZ[var16]];
				}

				var14 = (y * var13.y + z * var13.z + x * var13.x) / (var7 * var13.magnitude) + ambient;
				colorsZ[var16] = method2820(var14);
			}
		}

		generateBones();

		if (entity) {
			calculateBoundsCylinder();
		} else {
			vertexNormalsOffsets = new VertexNormal[verticesCount];
			for (int point = 0; point < verticesCount; point++) {
				VertexNormal norm = this.vertexNormals[point];
				VertexNormal merge = vertexNormalsOffsets[point] = new VertexNormal();
				merge.x = norm.x;
				merge.y = norm.y;
				merge.z = norm.z;
				merge.magnitude = norm.magnitude;
			}

			calculateBounds();
		}

		vertexNormals(entity);
	}

	private int method2820(int var0) {
		if (var0 < 2) {
			var0 = 2;
		} else if (var0 > 126) {
			var0 = 126;
		}

		return var0;
	}

	private int method2792(final int var0, int var1) {
		var1 = (var0 & 127) * var1 >> 7;
		if (var1 < 2) {
			var1 = 2;
		} else if (var1 > 126) {
			var1 = 126;
		}

		return (var0 & '\uff80') + var1;
	}

	public void renderModel(final int rotationY, final int rotationZ, final int rotationXW, final int translationX, final int translationY, final int translationZ) {
		if (this.boundsType != 2 && this.boundsType != 1) {
			this.normalise();
		}

		final int centerX = Rasterizer3D.originViewX;
		final int centerY = Rasterizer3D.originViewY;
		final int sineY = SINE[rotationY];
		final int cosineY = COSINE[rotationY];
		final int sineZ = SINE[rotationZ];
		final int cosineZ = COSINE[rotationZ];
		final int sineXW = SINE[rotationXW];
		final int cosineXW = COSINE[rotationXW];
		final int transformation = translationY * sineXW + translationZ * cosineXW >> 16;
		for (int vertex = 0; vertex < verticesCount; vertex++) {
			int x = this.verticesX[vertex];
			int y = this.verticesY[vertex];
			int z = this.verticesZ[vertex];
			if (rotationZ != 0) {
				final int newX = y * sineZ + x * cosineZ >> 16;
				y = y * cosineZ - x * sineZ >> 16;
				x = newX;
			}
			if (rotationY != 0) {
				final int newX = z * sineY + x * cosineY >> 16;
				z = z * cosineY - x * sineY >> 16;
				x = newX;
			}
			x += translationX;
			y += translationY;
			z += translationZ;
			final int newY = y * cosineXW - z * sineXW >> 16;
			z = y * sineXW + z * cosineXW >> 16;
			y = newY;
			vertexScreenZ[vertex] = z - transformation;
			vertexScreenX[vertex] = centerX + (x << 9) / z;
			vertexScreenY[vertex] = centerY + (y << 9) / z;
			if (texturesCount > 0) {
				vertexMovedX[vertex] = x;
				vertexMovedY[vertex] = y;
				vertexMovedZ[vertex] = z;
			}
		}

		try {
			this.withinObject(false, false, false,0);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	private static boolean mouseInViewport = false;

	public static void cursorCalculations() {
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
		if (MouseHandler.lastButton != 0) {
			mouseX = MouseHandler.saveClickX;
			mouseY = MouseHandler.saveClickY;
		}

		if (mouseX >= Client.instance.getViewportXOffset() && mouseX < Client.instance.getViewportXOffset() + Client.instance.getViewportWidth() && mouseY >= Client.instance.getViewportYOffset() && mouseY < Client.instance.getViewportHeight() + Client.instance.getViewportYOffset()) {
			cursorX = mouseX - Client.instance.getViewportXOffset();
			cursorY = mouseY - Client.instance.getViewportYOffset();
			mouseInViewport = true;
		} else {
			mouseInViewport = false;
		}
		objectsHovering = 0;
	}

	public void calculateBoundingBox(int orientation) {
		if (!this.aabb.containsKey(orientation)) {
			int minX = 0;
			int minZ = 0;
			int minY = 0;
			int maxX = 0;
			int maxZ = 0;
			int maxY = 0;
			int cosine = COSINE[orientation];
			int sine = SINE[orientation];

			for(int vert = 0; vert < this.verticesCount; ++vert) {
				int x = Rasterizer3D.method4046(this.verticesX[vert], this.verticesZ[vert], cosine, sine);
				int y = this.verticesY[vert];
				int z = Rasterizer3D.method4046(this.verticesX[vert], this.verticesZ[vert], cosine, sine);
				if (x < minX) {
					minX = x;
				}

				if (x > maxX) {
					maxX = x;
				}

				if (y < minZ) {
					minZ = y;
				}

				if (y > maxZ) {
					maxZ = y;
				}

				if (z < minY) {
					minY = z;
				}

				if (z > maxY) {
					maxY = z;
				}
			}

			AABB aabb = new AABB((maxX + minX) / 2, (maxZ + minZ) / 2, (maxY + minY) / 2, (maxX - minX + 1) / 2, (maxZ - minZ + 1) / 2, (maxY - minY + 1) / 2);

			if (aabb.xMidOffset < 32) {
				aabb.xMidOffset = 32;
			}

			if (aabb.zMidOffset < 32) {
				aabb.zMidOffset = 32;
			}

			if (this.singleTile) {
				aabb.xMidOffset += 8;
				aabb.zMidOffset += 8;
			}

			this.aabb.put(orientation, aabb);
		}

	}

	//Scene models
	@Override
	public final void renderAtPoint(int orientation, int pitchSine, int pitchCos, int yawSin, int yawCos, int offsetX, int offsetY, int offsetZ, long uid) {
		if (this.boundsType != 1) {
			this.calculateBoundsCylinder();
		}

		calculateBoundingBox(orientation);
		int sceneX = offsetZ * yawCos - offsetX * yawSin >> 16;
		int sceneY = offsetY * pitchSine + sceneX * pitchCos >> 16;
		int dimensionSinY = diagonal2DAboveOrigin * pitchCos >> 16;
		int pos = sceneY + dimensionSinY;
		final boolean gpu = Client.instance.isGpu() && Rasterizer3D.world;
		if (pos <= 50 || (sceneY >= 3500 && !gpu))
			return;
		int xRot = offsetZ * yawSin + offsetX * yawCos >> 16;
		int objX = (xRot - diagonal2DAboveOrigin) * Rasterizer3D.fieldOfView;
		if (objX / pos >= Rasterizer2D.viewportCenterX)
			return;

		int objWidth = (xRot + diagonal2DAboveOrigin) * Rasterizer3D.fieldOfView;
		if (objWidth / pos <= -Rasterizer2D.viewportCenterX)
			return;

		int yRot = offsetY * pitchCos - sceneX * pitchSine >> 16;
		int dimensionCosY = diagonal2DAboveOrigin * pitchSine >> 16;

		int var20 = (pitchCos * this.maxY >> 16) + dimensionCosY;
		int objHeight = (yRot + var20) * Rasterizer3D.fieldOfView;
		if (objHeight / pos <= -Rasterizer2D.viewportCenterY)
			return;

		int offset = dimensionCosY + (super.modelBaseY * pitchCos >> 16);
		int objY = (yRot - offset) * Rasterizer3D.fieldOfView;
		if (objY / pos >= Rasterizer2D.viewportCenterY)
			return;

		int size = dimensionSinY + (super.modelBaseY * pitchSine >> 16);

		boolean var25 = false;
		boolean nearSight = sceneY - size <= 50;

		boolean inView = nearSight || this.texturesCount > 0;

		boolean highlighted = false;


		if (DEBUG_MODELS) {
			int x = ObjectKeyUtil.getObjectX(uid);
			int y = ObjectKeyUtil.getObjectY(uid);
			int opcode = ObjectKeyUtil.getObjectOpcode(uid);
			int id = ObjectKeyUtil.getObjectId(uid);

			System.out.println("Render at Point , ID: "+id+" at "+x+", "+y+" = "+uid);
		}

		if (uid > 0 && mouseInViewport) { // var32 should replace (uid > 0) in osrs, but does not work for older maps (cox pillars "null" have menus, agility obstacles/levers don't)
			if (DEBUG_MODELS) {
				int x = ObjectKeyUtil.getObjectX(uid);
				int y = ObjectKeyUtil.getObjectY(uid);
				int opcode = ObjectKeyUtil.getObjectOpcode(uid);
				int id = ObjectKeyUtil.getObjectId(uid);

				System.out.println("Render at Point Hover, ID: "+id+" at "+x+", "+y+" = "+uid);
			}

			boolean withinBounds = false;

			byte distanceMin = 50;
			short distanceMax = 3500;
			int var43 = (cursorX - Rasterizer3D.originViewX) * distanceMin / Rasterizer3D.fieldOfView;
			int var44 = (cursorY - Rasterizer3D.originViewY) * distanceMin / Rasterizer3D.fieldOfView;
			int var45 = (cursorX - Rasterizer3D.originViewX) * distanceMax / Rasterizer3D.fieldOfView;
			int var46 = (cursorY - Rasterizer3D.originViewY) * distanceMax / Rasterizer3D.fieldOfView;
			int var47 = Rasterizer3D.method4045(var44, distanceMin, SceneGraph.camUpDownX, SceneGraph.camUpDownY);
			int var53 = Rasterizer3D.method4046(var44, distanceMin, SceneGraph.camUpDownX, SceneGraph.camUpDownY);
			var44 = var47;
			var47 = Rasterizer3D.method4045(var46, distanceMax, SceneGraph.camUpDownX, SceneGraph.camUpDownY);
			int var54 = Rasterizer3D.method4046(var46, distanceMax, SceneGraph.camUpDownX, SceneGraph.camUpDownY);
			var46 = var47;
			var47 = Rasterizer3D.method4025(var43, var53, SceneGraph.camLeftRightX, SceneGraph.camLeftRightY);
			var53 = Rasterizer3D.method4044(var43, var53, SceneGraph.camLeftRightX, SceneGraph.camLeftRightY);
			var43 = var47;
			var47 = Rasterizer3D.method4025(var45, var54, SceneGraph.camLeftRightX, SceneGraph.camLeftRightY);
			var54 = Rasterizer3D.method4044(var45, var54, SceneGraph.camLeftRightX, SceneGraph.camLeftRightY);
			int ViewportMouse_field2588 = (var43 + var47) / 2;
			int GZipDecompressor_field4821 = (var46 + var44) / 2;
			int class340_field4138 = (var54 + var53) / 2;
			int ViewportMouse_field2589 = (var47 - var43) / 2;
			int ItemComposition_field2148 = (var46 - var44) / 2;
			int User_field4308 = (var54 - var53) / 2;
			int class421_field4607 = Math.abs(ViewportMouse_field2589);
			int ViewportMouse_field2590 = Math.abs(ItemComposition_field2148);
			int class136_field1612 = Math.abs(User_field4308);

			AABB var50 = (AABB)this.aabb.get(orientation);
			int var37 = offsetX + var50.xMid;
			int var38 = offsetY + var50.yMid;
			int var39 = offsetZ + var50.zMid;
			var43 = ViewportMouse_field2588 - var37;
			var44 = GZipDecompressor_field4821 - var38;
			var45 = class340_field4138 - var39;
			if (Math.abs(var43) > var50.xMidOffset + class421_field4607) {
				withinBounds = false;
			} else if (Math.abs(var44) > var50.yMidOffset + ViewportMouse_field2590) {
				withinBounds = false;
			} else if (Math.abs(var45) > var50.zMidOffset + class136_field1612) {
				withinBounds = false;
			} else if (Math.abs(var45 * ItemComposition_field2148 - var44 * User_field4308) > var50.yMidOffset * class136_field1612 + var50.zMidOffset * ViewportMouse_field2590) {
				withinBounds = false;
			} else if (Math.abs(var43 * User_field4308 - var45 * ViewportMouse_field2589) > var50.zMidOffset * class421_field4607 + var50.xMidOffset * class136_field1612) {
				withinBounds = false;
			} else withinBounds = Math.abs(var44 * ViewportMouse_field2589 - var43 * ItemComposition_field2148) <= var50.xMidOffset * ViewportMouse_field2590 + var50.yMidOffset * class421_field4607;

			if (withinBounds) {
				if (this.singleTile) {
					hoveringObjects[objectsHovering++] = uid;
				} else {
					highlighted = true;
				}
			}
		}

		int sineX = 0;
		int cosineX = 0;
		if (orientation != 0) {
			sineX = SINE[orientation];
			cosineX = COSINE[orientation];
		}

		for (int index = 0; index < this.verticesCount; ++index) {
			int positionX = this.verticesX[index];
			int rasterY = this.verticesY[index];
			int positionZ = this.verticesZ[index];
			if (orientation != 0) {
				int rotatedX = positionZ * sineX + positionX * cosineX >> 16;
				positionZ = positionZ * cosineX - positionX * sineX >> 16;
				positionX = rotatedX;
			}

			positionX += offsetX;
			rasterY += offsetY;
			positionZ += offsetZ;

			int positionY = positionZ * yawSin + yawCos * positionX >> 16;
			positionZ = yawCos * positionZ - positionX * yawSin >> 16;
			positionX = positionY;
			positionY = pitchCos * rasterY - positionZ * pitchSine >> 16;
			positionZ = rasterY * pitchSine + pitchCos * positionZ >> 16;
			vertexScreenZ[index] = positionZ - sceneY;
			if (positionZ >= 50) {
				vertexScreenX[index] = positionX * Rasterizer3D.fieldOfView / positionZ + Rasterizer3D.originViewX;
				vertexScreenY[index] = positionY * Rasterizer3D.fieldOfView / positionZ + Rasterizer3D.originViewY;
			} else {
				vertexScreenX[index] = -5000;
				var25 = true;
			}

			if (inView) {
				vertexMovedX[index] = positionX;
				vertexMovedY[index] = positionY;
				vertexMovedZ[index] = positionZ;
			}
		}

		try {
			if (!gpu || (highlighted && !(Math.sqrt(offsetX * offsetX + offsetZ * offsetZ) > 35 * Perspective.LOCAL_TILE_SIZE))) {
				withinObject(var25, highlighted, singleTile, uid);
			}
			if (gpu) {
				Client.instance.getDrawCallbacks().draw(this, orientation, pitchSine, pitchCos, yawSin, yawCos, offsetX, offsetY, offsetZ, uid);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private boolean inBounds(int x, int y, int z, int screenX, int screenY, int screenZ, int size) {
		int height = cursorY + size;
		if (height < x && height < y && height < z) {
			return false;
		} else {
			height = cursorY - size;
			if (height > x && height > y && height > z) {
				return false;
			} else {
				height = cursorX + size;
				if (height < screenX && height < screenY && height < screenZ) {
					return false;
				} else {
					height = cursorX - size;
					return height <= screenX || height <= screenY || height <= screenZ;
				}
			}
		}
	}

	final void withinObject(boolean var25, boolean highlighted, boolean bigSize, long uid) {
		final boolean gpu = Client.instance.isGpu() && Rasterizer3D.world;

		if (diagonal3D < 6000) {
			for (int diagonalIndex = 0; diagonalIndex < diagonal3D; diagonalIndex++) {
				depth[diagonalIndex] = 0;
			}

			int size = bigSize ? 20 : 5;

			int var15;
			int var16;
			int var18;
			for (int currentTriangle = 0; currentTriangle < this.triangleFaceCount; ++currentTriangle) {
				if (this.colorsZ[currentTriangle] != -2) {
					int triX = this.trianglePointsX[currentTriangle];
					int triY = this.trianglePointsY[currentTriangle];
					int triZ = this.trianglePointsZ[currentTriangle];
					int screenXX = vertexScreenX[triX];
					int screenXY = vertexScreenX[triY];
					int screenXZ = vertexScreenX[triZ];
					int index;

					if (gpu) {
						if (screenXX == -5000 || screenXY == -5000 || screenXZ == -5000) {
							continue;
						}
						if (highlighted && inBounds(vertexScreenY[triX], vertexScreenY[triY], vertexScreenY[triZ], screenXX, screenXY, screenXZ,size)) {
							hoveringObjects[objectsHovering++] = uid;
						}
						continue;
					}

					if (!var25 || screenXX != -5000 && screenXY != -5000 && screenXZ != -5000) {
						if (highlighted && inBounds(vertexScreenY[triX], vertexScreenY[triY], vertexScreenY[triZ], screenXX, screenXY, screenXZ, size)) {
							hoveringObjects[objectsHovering++] = uid;
							highlighted = false;
						}

						if ((screenXX - screenXY) * (vertexScreenY[triZ] - vertexScreenY[triY]) - (screenXZ - screenXY) * (vertexScreenY[triX] - vertexScreenY[triY]) > 0) {
							outOfReach[currentTriangle] = false;
							hasAnEdgeToRestrict[currentTriangle] = screenXX < 0 || screenXY < 0 || screenXZ < 0 || screenXX > Rasterizer3D.lastX || screenXY > Rasterizer3D.lastX || screenXZ > Rasterizer3D.lastX;

							index = (vertexScreenZ[triX] + vertexScreenZ[triY] + vertexScreenZ[triZ]) / 3 + this.diagonal3DAboveOrigin;
							if (index < 0)
								index = 0;

							faceLists[index][depth[index]++] = currentTriangle;
						}
					} else {
						index = vertexMovedX[triX];
						var15 = vertexMovedX[triY];
						var16 = vertexMovedX[triZ];
						int var30 = vertexMovedY[triX];
						var18 = vertexMovedY[triY];
						int var19 = vertexMovedY[triZ];
						int var20 = vertexMovedZ[triX];
						int var21 = vertexMovedZ[triY];
						int var22 = vertexMovedZ[triZ];
						index -= var15;
						var16 -= var15;
						var30 -= var18;
						var19 -= var18;
						var20 -= var21;
						var22 -= var21;
						int var23 = var30 * var22 - var20 * var19;
						int var24 = var20 * var16 - index * var22;
						int var25a = index * var19 - var30 * var16;
						if (var15 * var23 + var18 * var24 + var21 * var25a > 0) {
							outOfReach[currentTriangle] = true;
							int var26 = (vertexScreenZ[triX] + vertexScreenZ[triY] + vertexScreenZ[triZ]) / 3 + this.diagonal3DAboveOrigin;
							if (var26 >= 0) {
								faceLists[var26][depth[var26]++] = currentTriangle;
							}
						}
					}
				}
			}
			if (gpu) {
				return;
			}
			if (this.faceRenderPriorities == null) {
				for (int faceIndex = this.diagonal3D - 1; faceIndex >= 0; --faceIndex) {
					int depth = Model.depth[faceIndex];
					if (depth > 0) {
						for (int index = 0; index < depth; ++index) {
							this.drawFace(faceLists[faceIndex][index]);
						}
					}
				}

			} else {
				for (int currentIndex = 0; currentIndex < 12; ++currentIndex) {
					anIntArray1673[currentIndex] = 0;
					anIntArray1677[currentIndex] = 0;
				}

				for (int depthIndex = this.diagonal3D - 1; depthIndex >= 0; --depthIndex) {
					int var8 = depth[depthIndex];
					if (var8 > 0) {

						for (int var10 = 0; var10 < var8; ++var10) {
							int var11 = faceLists[depthIndex][var10];
							byte var31 = this.faceRenderPriorities[var11];
							int var28 = anIntArray1673[var31]++;
							anIntArrayArray1674[var31][var28] = var11;
							if (var31 < 10) {
								anIntArray1677[var31] += depthIndex;
							} else if (var31 == 10) {
								anIntArray1675[var28] = depthIndex;
							} else {
								anIntArray1676[var28] = depthIndex;
							}
						}
					}
				}

				int var7 = 0;
				if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0) {
					var7 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
				}

				int var8 = 0;
				if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0) {
					var8 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
				}

				int var9 = 0;
				if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0) {
					var9 = (anIntArray1677[8] + anIntArray1677[6]) / (anIntArray1673[8] + anIntArray1673[6]);
				}

				int var11 = 0;
				int var12 = anIntArray1673[10];
				int[] var13 = anIntArrayArray1674[10];
				int[] var14 = anIntArray1675;
				if (var11 == var12) {
					var11 = 0;
					var12 = anIntArray1673[11];
					var13 = anIntArrayArray1674[11];
					var14 = anIntArray1676;
				}

				int var10;
				if (var11 < var12) {
					var10 = var14[var11];
				} else {
					var10 = -1000;
				}

				for (var15 = 0; var15 < 10; ++var15) {
					while (var15 == 0 && var10 > var7) {
						this.drawFace(var13[var11++]);
						if (var11 == var12 && var13 != anIntArrayArray1674[11]) {
							var11 = 0;
							var12 = anIntArray1673[11];
							var13 = anIntArrayArray1674[11];
							var14 = anIntArray1676;
						}

						if (var11 < var12) {
							var10 = var14[var11];
						} else {
							var10 = -1000;
						}
					}

					while (var15 == 3 && var10 > var8) {
						this.drawFace(var13[var11++]);
						if (var11 == var12 && var13 != anIntArrayArray1674[11]) {
							var11 = 0;
							var12 = anIntArray1673[11];
							var13 = anIntArrayArray1674[11];
							var14 = anIntArray1676;
						}

						if (var11 < var12) {
							var10 = var14[var11];
						} else {
							var10 = -1000;
						}
					}

					while (var15 == 5 && var10 > var9) {
						this.drawFace(var13[var11++]);
						if (var11 == var12 && var13 != anIntArrayArray1674[11]) {
							var11 = 0;
							var12 = anIntArray1673[11];
							var13 = anIntArrayArray1674[11];
							var14 = anIntArray1676;
						}

						if (var11 < var12) {
							var10 = var14[var11];
						} else {
							var10 = -1000;
						}
					}

					var16 = anIntArray1673[var15];
					int[] var17 = anIntArrayArray1674[var15];

					for (var18 = 0; var18 < var16; ++var18) {
						this.drawFace(var17[var18]);
					}
				}

				while (var10 != -1000) {
					this.drawFace(var13[var11++]);
					if (var11 == var12 && var13 != anIntArrayArray1674[11]) {
						var11 = 0;
						var13 = anIntArrayArray1674[11];
						var12 = anIntArray1673[11];
						var14 = anIntArray1676;
					}

					if (var11 < var12) {
						var10 = var14[var11];
					} else {
						var10 = -1000;
					}
				}

			}
		}
	}

	public void drawFace(int face) { //method484
		DrawCallbacks callbacks = Client.instance.getDrawCallbacks();
		if (callbacks == null || !callbacks.drawFace(this, face))
		{
			if (outOfReach[face]) {
				faceRotation(face);
				return;
			}
			int triX = trianglePointsX[face];
			int triY = trianglePointsY[face];
			int triZ = trianglePointsZ[face];
			Rasterizer3D.textureOutOfDrawingBounds = hasAnEdgeToRestrict[face];
			if (faceTransparencies == null)
				Rasterizer3D.alpha = 0;
			else
				Rasterizer3D.alpha = faceTransparencies[face] & 0xff;

			int type;
			if (faceRenderType == null)
				type = 0;
			else
				type = faceRenderType[face] & 3;

			if (faceTextures != null && faceTextures[face] != -1) {
				int textureA = triX;
				int textureB = triY;
				int textureC = triZ;
				if (textureCoords != null && textureCoords[face] != -1) {
					int coordinate = textureCoords[face] & 0xff;
					textureA = texTriangleX[coordinate];
					textureB = texTriangleY[coordinate];
					textureC = texTriangleZ[coordinate];
				}

				if (colorsZ[face] == -1 || type == 3) {
					Rasterizer3D.drawTexturedTriangle(
						vertexScreenY[triX], vertexScreenY[triY], vertexScreenY[triZ],
						vertexScreenX[triX], vertexScreenX[triY], vertexScreenX[triZ],
						colorsX[face], colorsX[face], colorsX[face],
						vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
						vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
						vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
						faceTextures[face]);
				} else {
					Rasterizer3D.drawTexturedTriangle(
						vertexScreenY[triX], vertexScreenY[triY], vertexScreenY[triZ],
						vertexScreenX[triX], vertexScreenX[triY], vertexScreenX[triZ],
						colorsX[face], colorsY[face], colorsZ[face],
						vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
						vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
						vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
						faceTextures[face]);
				}

			} else if(colorsZ[face] == -1) {//From old to new clause
				Rasterizer3D.drawFlatTriangle(
					vertexScreenY[triX], vertexScreenY[triY], vertexScreenY[triZ],
					vertexScreenX[triX], vertexScreenX[triY], vertexScreenX[triZ],
					modelColors[colorsX[face]]);

			} else {
				if (type == 0) {
					Rasterizer3D.drawShadedTriangle(vertexScreenY[triX], vertexScreenY[triY],
						vertexScreenY[triZ], vertexScreenX[triX], vertexScreenX[triY],
						vertexScreenX[triZ], colorsX[face], colorsY[face], colorsZ[face]);
				}
				if (type == 1) {
					Rasterizer3D.drawFlatTriangle(vertexScreenY[triX], vertexScreenY[triY],
						vertexScreenY[triZ], vertexScreenX[triX], vertexScreenX[triY],
						vertexScreenX[triZ], modelColors[colorsX[face]]);
				}
			}

		}
	}

	private final void faceRotation(int triangle) {
		int centreX = Rasterizer3D.originViewX;
		int centreY = Rasterizer3D.originViewY;
		int counter = 0;
		int x = trianglePointsX[triangle];
		int y = trianglePointsY[triangle];
		int z = trianglePointsZ[triangle];
		int movedX = vertexMovedZ[x];
		int movedY = vertexMovedZ[y];
		int movedZ = vertexMovedZ[z];
		if (movedX >= 50) {
			xPosition[counter] = vertexScreenX[x];
			yPosition[counter] = vertexScreenY[x];
			zPosition[counter++] = colorsX[triangle];
		} else {
			int movedX2 = vertexMovedX[x];
			int movedY2 = vertexMovedY[x];
			int colour = colorsX[triangle];
			if (movedZ >= 50) {
				int k5 = (50 - movedX) * modelLocations[movedZ - movedX];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[z] - movedX2) * k5 >> 16)) * Rasterizer3D.fieldOfView / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[z] - movedY2) * k5 >> 16)) * Rasterizer3D.fieldOfView / 50;
				zPosition[counter++] = colour + ((colorsZ[triangle] - colour) * k5 >> 16);
			}
			if (movedY >= 50) {
				int l5 = (50 - movedX) * modelLocations[movedY - movedX];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[y] - movedX2) * l5 >> 16)) * Rasterizer3D.fieldOfView / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[y] - movedY2) * l5 >> 16)) * Rasterizer3D.fieldOfView / 50;
				zPosition[counter++] = colour + ((colorsY[triangle] - colour) * l5 >> 16);
			}
		}
		if (movedY >= 50) {
			xPosition[counter] = vertexScreenX[y];
			yPosition[counter] = vertexScreenY[y];
			zPosition[counter++] = colorsY[triangle];
		} else {
			int movedX2 = vertexMovedX[y];
			int movedY2 = vertexMovedY[y];
			int colour = colorsY[triangle];
			if (movedX >= 50) {
				int i6 = (50 - movedY) * modelLocations[movedX - movedY];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[x] - movedX2) * i6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[x] - movedY2) * i6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				zPosition[counter++] = colour + ((colorsX[triangle] - colour) * i6 >> 16);
			}
			if (movedZ >= 50) {
				int j6 = (50 - movedY) * modelLocations[movedZ - movedY];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[z] - movedX2) * j6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[z] - movedY2) * j6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				zPosition[counter++] = colour + ((colorsZ[triangle] - colour) * j6 >> 16);
			}
		}
		if (movedZ >= 50) {
			xPosition[counter] = vertexScreenX[z];
			yPosition[counter] = vertexScreenY[z];
			zPosition[counter++] = colorsZ[triangle];
		} else {
			int movedX2 = vertexMovedX[z];
			int movedY2 = vertexMovedY[z];
			int colour = colorsZ[triangle];
			if (movedY >= 50) {
				int k6 = (50 - movedZ) * modelLocations[movedY - movedZ];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[y] - movedX2) * k6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[y] - movedY2) * k6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				zPosition[counter++] = colour + ((colorsY[triangle] - colour) * k6 >> 16);
			}
			if (movedX >= 50) {
				int l6 = (50 - movedZ) * modelLocations[movedX - movedZ];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[x] - movedX2) * l6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[x] - movedY2) * l6 >> 16)) * Rasterizer3D.fieldOfView / 50;
				zPosition[counter++] = colour + ((colorsX[triangle] - colour) * l6 >> 16);
			}
		}
		int xA = xPosition[0];
		int xB = xPosition[1];
		int xC = xPosition[2];
		int yA = yPosition[0];
		int yB = yPosition[1];
		int yC = yPosition[2];
		if ((xA - xB) * (yC - yB) - (yA - yB) * (xC - xB) > 0) {
			Rasterizer3D.textureOutOfDrawingBounds = false;
			int textureA = x;
			int textureB = y;
			int textureC = z;
			if (counter == 3) {
				if (xA < 0 || xB < 0 || xC < 0 || xA > Rasterizer2D.lastX || xB > Rasterizer2D.lastX || xC > Rasterizer2D.lastX) {
					Rasterizer3D.textureOutOfDrawingBounds = true;
				}

				int drawType;
				if (this.faceRenderType == null) {
					drawType = 0;
				} else {
					drawType = this.faceRenderType[triangle] & 3;
				}

				if (faceTextures != null && faceTextures[triangle] != -1) {

					if (textureCoords != null && textureCoords[triangle] != -1) {
						int coordinate = textureCoords[triangle] & 0xff;
						textureA = texTriangleX[coordinate];
						textureB = texTriangleY[coordinate];
						textureC = texTriangleZ[coordinate];
					}

					if (colorsZ[triangle] == -1) {
						Rasterizer3D.drawTexturedTriangle(
							yA, yB, yC,
							xA, xB, xC,
							colorsX[triangle], colorsX[triangle], colorsX[triangle],
							vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
							vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
							vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
							faceTextures[triangle]);
					} else {
						Rasterizer3D.drawTexturedTriangle(
							yA, yB, yC,
							xA, xB, xC,
							zPosition[0], zPosition[1], zPosition[2],
							vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
							vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
							vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
							faceTextures[triangle]);
					}
				} else {
					if (drawType == 0) {
						Rasterizer3D.drawShadedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2]);
					} else if (drawType == 1) {
						Rasterizer3D.drawFlatTriangle(yA, yB, yC, xA, xB, xC, modelColors[colorsX[triangle]]);
					}
				}
			}
			if (counter == 4) {
				if (xA < 0 || xB < 0 || xC < 0 || xA > Rasterizer2D.lastX || xB > Rasterizer2D.lastX || xC > Rasterizer2D.lastX || xPosition[3] < 0 || xPosition[3] > Rasterizer2D.lastX) {
					Rasterizer3D.textureOutOfDrawingBounds = true;
				}
				int drawType;
				if (this.faceRenderType == null) {
					drawType = 0;
				} else {
					drawType = this.faceRenderType[triangle] & 3;
				}

				if (faceTextures != null && faceTextures[triangle] != -1) {
					if (textureCoords != null && textureCoords[triangle] != -1) {
						int coordinate = textureCoords[triangle] & 0xff;
						textureA = texTriangleX[coordinate];
						textureB = texTriangleY[coordinate];
						textureC = texTriangleZ[coordinate];
					}
					if (colorsZ[triangle] == -1) {
						Rasterizer3D.drawTexturedTriangle(
							yA, yB, yC,
							xA, xB, xC,
							colorsX[triangle], colorsX[triangle], colorsX[triangle],
							vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
							vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
							vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
							faceTextures[triangle]);
						Rasterizer3D.drawTexturedTriangle(
							yA, yC, yPosition[3],
							xA, xC, xPosition[3],
							colorsX[triangle], colorsX[triangle], colorsX[triangle],
							vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
							vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
							vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
							faceTextures[triangle]);
					} else {
						Rasterizer3D.drawTexturedTriangle(
							yA, yB, yC,
							xA, xB, xC,
							zPosition[0], zPosition[1], zPosition[2],
							vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
							vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
							vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
							faceTextures[triangle]);
						Rasterizer3D.drawTexturedTriangle(
							yA, yC, yPosition[3],
							xA, xC, xPosition[3],
							zPosition[0], zPosition[2], zPosition[3],
							vertexMovedX[textureA], vertexMovedX[textureB], vertexMovedX[textureC],
							vertexMovedY[textureA], vertexMovedY[textureB], vertexMovedY[textureC],
							vertexMovedZ[textureA], vertexMovedZ[textureB], vertexMovedZ[textureC],
							faceTextures[triangle]);
					}
				} else {
					if (drawType == 0) {
						Rasterizer3D.drawShadedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2]);
						Rasterizer3D.drawShadedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], zPosition[0], zPosition[2], zPosition[3]);
						return;
					}
					if (drawType == 1) {
						int l8 = modelColors[colorsX[triangle]];
						Rasterizer3D.drawFlatTriangle(yA, yB, yC, xA, xB, xC, l8);
						Rasterizer3D.drawFlatTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], l8);
					}
				}
			}
		}
	}

	private int boundsType;

	public int[][] animayaGroups;
	public int[][] animayaScales;

	private float[] faceTextureUVCoordinates;
	private int[] vertexNormalsX, vertexNormalsY, vertexNormalsZ;

	public short[] faceTextures;
	public byte[] textureCoords;
	public byte[] textureRenderTypes;

	public boolean isModel = false;
	public short ambient;
	public short contrast;
	public VertexNormal[] vertexNormals;
	public VertexNormal[] vertexNormalsOffsets;
	public FaceNormal[] faceNormals;

	public static int anInt1620;
	public static Model objectModel = new Model();
	public static Model emptyModel = new Model();
	private static int[] sharedVerticesX = new int[6500];
	private static int[] sharedVerticesY = new int[6500];
	private static int[] sharedVerticesZ = new int[6500];
	private static byte[] sharedTriangleAlpha = new byte[6500];
	public int verticesCount;
	public int[] verticesX;
	public int[] verticesY;
	public int[] verticesZ;
	public int triangleFaceCount;
	public int[] trianglePointsX;
	public int[] trianglePointsY;
	public int[] trianglePointsZ;
	public int[] colorsX;
	public int[] colorsY;
	public int[] colorsZ;
	public int[] faceRenderType;
	public byte[] faceRenderPriorities;
	public byte[] faceTransparencies;
	public short[] faceColors;
	public byte priority = 0;
	public int texturesCount;
	public short[] texTriangleX;
	public short[] texTriangleY;
	public short[] texTriangleZ;
	public int minX;
	public int maxX;
	public int maxZ;
	public int minZ;
	public int diagonal2DAboveOrigin;
	public int maxY;
	public int diagonal3D;
	public int diagonal3DAboveOrigin;
	public int itemDropHeight;
	public int[] vertexSkins;
	public int[] triangleSkinValues;
	public int[][] groupedVertexLabels;
	public int[][] groupedTriangleLabels;
	public boolean singleTile;
	static ModelHeader[] modelHeaders;
	static boolean[] hasAnEdgeToRestrict = new boolean[6500];
	static boolean[] outOfReach = new boolean[6500];
	static int[] vertexScreenX = new int[6500];
	static int[] vertexScreenY = new int[6500];
	static int[] vertexScreenZ = new int[6500];
	static int[] vertexMovedX = new int[6500];
	static int[] vertexMovedY = new int[6500];
	static int[] vertexMovedZ = new int[6500];
	static int[] depth = new int[6000];
	static int[][] faceLists = new int[6000][512];
	static int[] anIntArray1673 = new int[12];
	static int[][] anIntArrayArray1674 = new int[12][2000];
	static int[] anIntArray1676 = new int[2000];
	static int[] anIntArray1675 = new int[2000];
	static int[] anIntArray1677 = new int[12];
	static int[] xPosition = new int[10];
	static int[] yPosition = new int[10];
	static int[] zPosition = new int[10];
	static int transformTempX;
	static int transformTempY;
	static int transformTempZ;
	public static boolean objectExist;
	public static int cursorX;
	public static int cursorY;
	public static int objectsHovering;
	public static final long[] hoveringObjects = new long[6500];
	public static int[] SINE;
	public static int[] COSINE;
	static int[] modelColors;
	static int[] modelLocations;

	public int lastOrientation = -1;
	public boolean shadeModel;

	HashMap<Integer, net.runelite.api.AABB> aabb = new HashMap<Integer, net.runelite.api.AABB>();

	static {
		SINE = Rasterizer3D.SINE;
		COSINE = Rasterizer3D.COSINE;
		modelColors = Rasterizer3D.hslToRgb;
		modelLocations = Rasterizer3D.anIntArray1469;
	}


	public int bufferOffset;
	public int uvBufferOffset;

	@Override
	public java.util.List<net.runelite.api.model.Vertex> getVertices() {
		int[] verticesX = getVerticesX();
		int[] verticesY = getVerticesY();
		int[] verticesZ = getVerticesZ();
		ArrayList<net.runelite.api.model.Vertex> vertices = new ArrayList<>(getVertexCount());
		for (int i = 0; i < getVertexCount(); i++) {
			net.runelite.api.model.Vertex vertex = new net.runelite.api.model.Vertex(verticesX[i], verticesY[i], verticesZ[i]);
			vertices.add(vertex);
		}
		return vertices;
	}

	@Override
	public List<Triangle> getTriangles() {
		int[] trianglesX = getFaceIndices1();
		int[] trianglesY = getFaceIndices2();
		int[] trianglesZ = getFaceIndices3();

		List<Vertex> vertices = getVertices();
		List<Triangle> triangles = new ArrayList<>(getFaceCount());

		for (int i = 0; i < getFaceCount(); ++i)
		{
			int triangleX = trianglesX[i];
			int triangleY = trianglesY[i];
			int triangleZ = trianglesZ[i];

			Triangle triangle = new Triangle(vertices.get(triangleX),vertices.get(triangleY),vertices.get(triangleZ));
			triangles.add(triangle);
		}

		return triangles;
	}

	@Override
	public int getVertexCount() {
		return verticesCount;
	}

	@Override
	public int[] getVerticesX() {
		return verticesX;
	}

	@Override
	public int[] getVerticesY() {
		return verticesY;
	}

	@Override
	public int[] getVerticesZ() {
		return verticesZ;
	}

	@Override
	public int getFaceCount() {
		return this.triangleFaceCount;
	}

	@Override
	public int[] getFaceIndices1() {
		return trianglePointsX;
	}

	@Override
	public int[] getFaceIndices2() {
		return trianglePointsY;
	}

	@Override
	public int[] getFaceIndices3() {
		return trianglePointsZ;
	}

	@Override
	public int[] getFaceColors1() {
		return this.colorsX;
	}

	@Override
	public int[] getFaceColors2() {
		return colorsY;
	}

	@Override
	public int[] getFaceColors3() {
		return colorsZ;
	}

	@Override
	public byte[] getFaceTransparencies() {
		return faceTransparencies;
	}

	private int sceneId;

	@Override
	public int getSceneId() {
		return sceneId;
	}

	@Override
	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	@Override
	public int getBufferOffset() {
		return bufferOffset;
	}

	@Override
	public void setBufferOffset(int bufferOffset) {
		this.bufferOffset = bufferOffset;
	}

	@Override
	public int getUvBufferOffset() {
		return uvBufferOffset;
	}

	@Override
	public void setUvBufferOffset(int uvBufferOffset) {
		this.uvBufferOffset = uvBufferOffset;
	}

	@Override
	public int getModelHeight() {
		return modelBaseY;
	}

	@Override
	public void animate(int type, int[] list, int x, int y, int z) {

	}

	@Override
	public byte[] getFaceRenderPriorities() {
		return this.faceRenderPriorities;
	}

	@Override
	public int[][] getGroupedVertexLabels() {
		return new int[0][];
	}

	@Override
	public int getRadius() {
		return diagonal3DAboveOrigin;
	}

	@Override
	public int getDiameter() {
		return diagonal3D;
	}

	@Override
	public short[] getFaceTextures() {
		return faceTextures;
	}

	@Override
	public void calculateExtreme(int orientation) {
		calculateBoundingBox(orientation);
	}

	public void resetBounds() {
		this.boundsType = 0;
		this.aabb.clear();
	}

	@Override
	public RSModel toSharedModel(boolean b) {
		return null;
	}

	@Override
	public RSModel toSharedSpotAnimModel(boolean b) {
		return null;
	}

	@Override
	public void rotateY90Ccw() {
		rotate90Degrees();
	}

	@Override
	public void rotateY180Ccw() {
		for (int vert = 0; vert < this.verticesCount; ++vert)
		{
			this.verticesX[vert] = -this.verticesX[vert];
			this.verticesZ[vert] = -this.verticesZ[vert];
		}

		this.resetBounds();
	}

	@Override
	public void rotateY270Ccw() {
		for (int vert = 0; vert < this.verticesCount; ++vert)
		{
			int var2 = this.verticesZ[vert];
			this.verticesZ[vert] = this.verticesX[vert];
			this.verticesX[vert] = -var2;
		}

		this.resetBounds();
	}

	@Override
	public int getXYZMag() {
		return diagonal2DAboveOrigin;
	}

	@Override
	public boolean isClickable() {
		return singleTile;
	}

	@Override
	public void interpolateFrames(RSFrames frames, int frameId, RSFrames nextFrames, int nextFrameId, int interval, int intervalCount) {

	}

	@Override
	public int[] getVertexNormalsX() {
		if(vertexNormalsX == null)
			return getVerticesX();
		return vertexNormalsX;
	}

	@Override
	public void setVertexNormalsX(int[] vertexNormalsX) {
		this.vertexNormalsX = vertexNormalsX;
	}

	@Override
	public int[] getVertexNormalsY() {
		if(vertexNormalsY == null)
			return getVerticesY();
		return vertexNormalsY;
	}

	@Override
	public void setVertexNormalsY(int[] vertexNormalsY) {
		this.vertexNormalsY = vertexNormalsY;
	}

	@Override
	public int[] getVertexNormalsZ() {
		if(vertexNormalsZ == null)
			return getVerticesZ();
		return vertexNormalsZ;
	}

	@Override
	public void setVertexNormalsZ(int[] vertexNormalsZ) {
		this.vertexNormalsZ = vertexNormalsZ;
	}

	@Override
	public byte getOverrideAmount() {
		return 0;
	}

	@Override
	public byte getOverrideHue() {
		return 0;
	}

	@Override
	public byte getOverrideSaturation() {
		return 0;
	}

	@Override
	public byte getOverrideLuminance() {
		return 0;
	}

	@Override
	public HashMap<Integer, net.runelite.api.AABB> getAABBMap() {
		return aabb;
	}

	@Override
	public Shape getConvexHull(int localX, int localY, int orientation, int tileHeight) {
		int[] x2d = new int[getVertexCount()];
		int[] y2d = new int[getVertexCount()];

		Perspective.modelToCanvas(Client.instance, getVertexCount(), localX, localY, tileHeight, orientation, getVerticesX(), getVerticesZ(), getVerticesY(), x2d, y2d);

		return Jarvis.convexHull(x2d, y2d);
	}

	@Override
	public int getBottomY() {
		return maxY;
	}

	private void vertexNormals(boolean entity)
	{

		if (vertexNormalsX == null)
		{
			int verticesCount = getVertexCount();

			vertexNormalsX = new int[verticesCount];
			vertexNormalsY = new int[verticesCount];
			vertexNormalsZ = new int[verticesCount];

			if (entity) {
				int[] trianglesX = getFaceIndices1();
				int[] trianglesY = getFaceIndices2();
				int[] trianglesZ = getFaceIndices3();
				int[] verticesX = getVerticesX();
				int[] verticesY = getVerticesY();
				int[] verticesZ = getVerticesZ();

				for (int i = 0;
					 i < triangleFaceCount;
					 ++i) {
					int var9 = trianglesX[i];
					int var10 = trianglesY[i];
					int var11 = trianglesZ[i];

					int var12 = verticesX[var10] - verticesX[var9];
					int var13 = verticesY[var10] - verticesY[var9];
					int var14 = verticesZ[var10] - verticesZ[var9];
					int var15 = verticesX[var11] - verticesX[var9];
					int var16 = verticesY[var11] - verticesY[var9];
					int var17 = verticesZ[var11] - verticesZ[var9];

					int var18 = var13 * var17 - var16 * var14;
					int var19 = var14 * var15 - var17 * var12;

					int var20;
					for (var20 = var12 * var16 - var15 * var13;
						 var18 > 8192 || var19 > 8192 || var20 > 8192 || var18 < -8192 || var19 < -8192 || var20 < -8192;
						 var20 >>= 1) {
						var18 >>= 1;
						var19 >>= 1;
					}

					int var21 = (int) Math.sqrt(var18 * var18 + var19 * var19 + var20 * var20);
					if (var21 <= 0) {
						var21 = 1;
					}

					var18 = var18 * 256 / var21;
					var19 = var19 * 256 / var21;
					var20 = var20 * 256 / var21;

					vertexNormalsX[var9] += var18;
					vertexNormalsY[var9] += var19;
					vertexNormalsZ[var9] += var20;

					vertexNormalsX[var10] += var18;
					vertexNormalsY[var10] += var19;
					vertexNormalsZ[var10] += var20;

					vertexNormalsX[var11] += var18;
					vertexNormalsY[var11] += var19;
					vertexNormalsZ[var11] += var20;
				}
			}
		}
	}

	@Override
	public int getLastOrientation() {
		return lastOrientation;
	}

	@Override
	public byte[] getTextureFaces() {
		return this.textureCoords;
	}

	@Override
	public short[] getTexIndices1() {
		return texTriangleX;
	}

	@Override
	public short[] getTexIndices2() {
		return texTriangleY;
	}

	@Override
	public short[] getTexIndices3() {
		return texTriangleZ;
	}

	@Override
	public AABB getAABB(int orientation) {
		calculateExtreme(orientation);
		lastOrientation = orientation;
		return (AABB) getAABBMap().get(lastOrientation);
	}

	public int getShadowIntensity() {
		calculateBoundsCylinder();
		return diagonal2DAboveOrigin;
	}

	private static final Matrix4f fbm = new Matrix4f();
	private static final Matrix4f sm = new Matrix4f();
	private static final Matrix4f cbm = new Matrix4f();

	public Model toSharedSequenceModel(Model model, boolean hasAlpha) {
		model.verticesCount = verticesCount;
		model.triangleFaceCount = triangleFaceCount;
		model.texturesCount = texturesCount;
		if (model.verticesX == null || model.verticesX.length < verticesCount) {
			model.verticesX = new int[verticesCount + 100];
			model.verticesY = new int[verticesCount + 100];
			model.verticesZ = new int[verticesCount + 100];
		}

		for (int i = 0; i < verticesCount; i++) {
			model.verticesX[i] = verticesX[i];
			model.verticesY[i] = verticesY[i];
			model.verticesZ[i] = verticesZ[i];
		}

		if (hasAlpha) {
			model.faceTransparencies = faceTransparencies;
		} else {
			if (sharedTriangleAlpha.length < triangleFaceCount) {
				sharedTriangleAlpha = new byte[triangleFaceCount + 100];
			}
			model.faceTransparencies = sharedTriangleAlpha;
			if (faceTransparencies == null) {
				for (int i = 0; i < triangleFaceCount; i++) {
					model.faceTransparencies[i] = 0;
				}
			} else {
				System.arraycopy(faceTransparencies, 0, model.faceTransparencies, 0, triangleFaceCount);
			}
		}
		model.faceTextures = faceTextures;
		model.textureRenderTypes = textureRenderTypes;
		model.textureCoords = textureCoords;
		model.faceTextureUVCoordinates = faceTextureUVCoordinates;
		model.faceRenderType = faceRenderType;
		model.faceColors = faceColors;
		model.faceRenderPriorities = faceRenderPriorities;
		model.priority = priority;
		model.groupedTriangleLabels = groupedTriangleLabels;
		model.groupedVertexLabels = groupedVertexLabels;
		model.trianglePointsX = trianglePointsX;
		model.trianglePointsY = trianglePointsY;
		model.trianglePointsZ = trianglePointsZ;
		model.colorsX = colorsX;
		model.colorsY = colorsY;
		model.colorsZ = colorsZ;
		model.texTriangleX = texTriangleX;
		model.texTriangleY = texTriangleY;
		model.texTriangleZ = texTriangleZ;

		model.vertexNormalsX = vertexNormalsX;
		model.vertexNormalsY = vertexNormalsY;
		model.vertexNormalsZ = vertexNormalsZ;

		model.singleTile = singleTile;

		model.animayaGroups = animayaGroups;
		model.animayaScales = animayaScales;

		model.resetBounds();
		return model;
	}

	public int getLowestX() {
		calculateBoundsCylinder();
		return diagonal2DAboveOrigin;
	}
}