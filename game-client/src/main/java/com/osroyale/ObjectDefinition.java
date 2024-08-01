package com.osroyale;

public final class ObjectDefinition {

	public static void unpackConfig(StreamLoader streamLoader) {
		stream = new Buffer(streamLoader.getFile("loc.dat"));
		Buffer idx = new Buffer(streamLoader.getFile("loc.idx"));
		int highestFileId = idx.readUnsignedShort();
		System.err.println("highest object = "+ highestFileId);
		streamIndices = new int[highestFileId + 1];
		int i = 0;
		for (int j = 0; j <= highestFileId; j++) {
			final int size = idx.readUnsignedShort();
			if (size == -1 || size == 65535) {
				//System.err.println("OBJECT BREAK AT " + j + " (size="+size+")");
				break;
			}
			streamIndices[j] = i;
			i += size;
		}

		cache = new ObjectDefinition[20];
		for (int index = 0; index < 20; index++)
			cache[index] = new ObjectDefinition();
	}

	static ObjectDefinition forID(int i) {
		if (i > streamIndices.length)
			i = streamIndices.length - 1;
		for (int j = 0; j < 20; j++)
			if (cache[j].type == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 20;
	    /* Seers village fix */
		if (i == 25913)
			i = 15552;
		if (i == 25916 || i == 25926)
			i = 15553;
		if (i == 25917)
			i = 15554;
		ObjectDefinition objectDefinition = cache[cacheIndex];
		stream.position = streamIndices[i];
		objectDefinition.type = i;
		objectDefinition.setDefaults();
		objectDefinition.decode(stream, i);

		/* Custom/Modified Objects */
		switch (i) {

			case 10081: /* Dead Ore */
			case 2194: /* Dead Ore */
			case 26756:
			case 1536://door
				objectDefinition.interactions = new String[5];
				break;

			case 27290:
				objectDefinition.name = "Blood Money Chest";
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Unlock";
				objectDefinition.interactions[1] = "Status";
				objectDefinition.interactions[2] = "Guide";
				objectDefinition.modifiedModelColors = new short[]{14499, 12698, 12818, 12480, 12492, 14554, 13545};
				objectDefinition.originalModelColors = new short[]{947, 45, 30, 100, 100, 100, 100};
				objectDefinition.description = "This chest can only be opened with a Blood key.".getBytes();
				break;

			case 2191:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Unlock";
				objectDefinition.name = "Crystal Chest";
				break;

			case 25029:
			case 25016:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Push-through";
				break;
			case 29102:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Cross";
				break;
			case 13607:
				objectDefinition.name = "Trading Table";
				objectDefinition.interactions[0] = "Open Trading Post";
				break;


			case 5249:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Add-logs";
				break;

			case 27215:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Attack";
				break;

			case 3994:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Smelt";
				break;
			case 29776:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "View";
				objectDefinition.name = "PvP Leaderboard";
				break;

			case 24099:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "View";
				objectDefinition.name = "Bot Loot";
				break;

			case 22472://Tab Creation
				objectDefinition.name = "Tablet";
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Open";
				break;


			case 14826:
			case 14827:
			case 14828:
			case 14829:
			case 14830:
			case 14831:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Activate";
				objectDefinition.interactions[1] = "Set Destination";
				break;

			case 23885:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Enter";
				objectDefinition.name = "Arena stairs";
				break;

			case 11338:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Access";
				objectDefinition.name = "Clan Bank";
				objectDefinition.description = "The bank of the clan.".getBytes();
				break;
			case 884:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Activate";
				objectDefinition.interactions[1] = "Check";
				objectDefinition.name = "Well of Goodwill";
				objectDefinition.description = "A well that is filled with will of the good.".getBytes();
				//                objectDefinition.modelIds = new int[]{10460};
				objectDefinition.solid = true;
				objectDefinition.clipped = false;
				break;

			case 33354:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Open";
				objectDefinition.name = "Teleportation Menu";
				objectDefinition.description = "Teleport around the world in ease!".getBytes();
				break;

			case 11546:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Access";
				objectDefinition.name = "Management machine";
				objectDefinition.description = "A machine that manages players.".getBytes();
				break;

			case 26043:
				objectDefinition.name = "Marketplace";
				objectDefinition.description = "Buy/sell items using the Player owned shops.".getBytes();
				objectDefinition.interactions = new String[]{"Access", null, null, null, null};
				////                objectDefinition.modelIds = new int[]{55555};
				objectDefinition.solid = true;
				objectDefinition.clipped = false;
				break;

			case 10090:
				objectDefinition.name = "Marketplace Information";
				objectDefinition.description = "Information regarding the marketplace.".getBytes();
				objectDefinition.interactions = new String[]{"Read", null, null, null, null};
				//                objectDefinition.modelIds = new int[]{55557};
				objectDefinition.solid = true;
				objectDefinition.clipped = false;
				break;

			case 26782:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Recharge";
				break;

			case 29241:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Drink";
				break;

            /* Donator deposit box */
			case 26254:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Deposit";
				objectDefinition.name = "Donator box";
				objectDefinition.description = "A donator-only feature which allows members to quick deposit items.".getBytes();
				break;

		/* Grand exchange desk */
			case 26044:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Open";
				objectDefinition.interactions[1] = "Manage";
				objectDefinition.name = "Marketplace";
				objectDefinition.description = "The marketplace desk.".getBytes();
				break;

		/* Wilderness Sign */
			case 14503:
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Read";
				break;
			case 11393:
				objectDefinition.name = "Empty wall";
				objectDefinition.interactions = new String[]{null, null, null, null, null};
				break;

			case 11389:
			case 11388:
				objectDefinition.name = "Amethyst crystal";
				objectDefinition.solid = true;
				objectDefinition.clipped = false;
				break;
		/* Magic Book Statue */
			case 576:
				objectDefinition.name = "Magic book";
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Change";
				break;

		/* Prayer Statue */
			case 575:
				objectDefinition.name = "Mage Book";
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Change";
				break;

		/* Fountain of Charges */
			case 12089:
				objectDefinition.name = "Fountain of Charges";
				objectDefinition.interactions = new String[5];
				objectDefinition.interactions[0] = "Inspect";
				break;
		}

		if (objectDefinition.name != null && objectDefinition.interactions != null && (objectDefinition.name.equals("Bank booth") || objectDefinition.name.equals("Bank chest"))) {
			objectDefinition.interactions = new String[]{"Bank", "Presets", null, null, null};
		}

		return objectDefinition;
	}

	public void setDefaults() {
		modelIds = null;
		modelTypes = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		originalTexture = null;
		modifiedTexture = null;
		width = 1;
		length = 1;
		solid = true;
		impenetrable = true;
		interactive = false;
		mergeNormals = false;
		nonFlatShading = false;
		modelClipped = false;
		animationId = -1;
		decorOffset = 16;
		ambient = 0;
		contrast = 0;
		interactions = null;
		mapIconId = -1;
		mapsceneId = -1;
		rotated = false;
		clipped = true;
		modelSizeX = 128;
		modelHeight = 128;
		modelSizeY = 128;
		orientation = 0;
		offsetX = 0;
		offsetHeight = 0;
		offsetY = 0;
		obstructsGround = false;
		removeClipping = false;
		mergeInteractState = -1;
		varbit = -1;
		varp = -1;
		childrenIDs = null;
	}

	public void method574(OnDemandFetcher provider) {
		if (modelIds == null)
			return;
		for (int j = 0; j < modelIds.length; j++)
			provider.writeRequest(modelIds[j] & 0xffff, 0);
	}

	public static void nullLoader() {
		cachedEntities = null;
		modelUnlitCache = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public static void clearCaches() {
		cachedEntities.unlinkAll();
		modelUnlitCache.unlinkAll();
	}

	public boolean method577(int i) {
		if (modelTypes == null) {
			if (modelIds == null)
				return true;
			if (i != 10)
				return true;
			boolean flag1 = true;
			for (int k = 0; k < modelIds.length; k++)
				flag1 &= Model.isCached(modelIds[k] & 0xffff);
			return flag1;
		}
		for (int j = 0; j < modelTypes.length; j++)
			if (modelTypes[j] == i)
				return Model.isCached(modelIds[j] & 0xffff);
		return true;
	}

	public Model getModelAnimated(int type, int rotation, int currentFrame, Animation sequence, int[][] heightMap,
								  int mean, int localX, int localY) {
		long uid;
		if (this.modelTypes == null) {
			uid = (long) ((this.type << 10) + rotation);
		} else {
			uid = (long) ((this.type << 10) + (type << 3) + rotation);
		}

		final Cache cache = modelCache;
		Model model = (Model) cache.get(uid);
		if (model == null) {
			model = model(type, rotation);
			if (model == null) {
				return null;
			}

			model.light(this.ambient + 64, this.contrast + 768, -50, -10, -50, false);
			cache.put(model, uid);
		}
		if (sequence == null && !mergeNormals) {
			return model;
		} else {
			if (sequence != null) {
				model = sequence.animateLoc(model, rotation, currentFrame);
			} else {
				model = model.toSharedSequenceModel(Model.objectModel, true);
			}
			if (mergeNormals) {
				model = model.hillskew(heightMap, localX, mean, localY, true, 0);
			}
		}
		return model;
	}

	public Model getModelSharelight(int type, int rotation, int[][] heightMap, int mean, int localX, int localY) {
		long uid;
		if (modelTypes == null) {
			uid = (long) ((this.type << 10) + rotation);
		} else {
			uid = (long) ((this.type << 10) + (type << 3) + rotation);
		}

		final Cache cache = cachedEntities;
		Model model = (Model) cache.get(uid);
		if (model == null) {
			model = model(type, rotation);
			if (model == null) {
				return null;
			}

			if (!nonFlatShading) {
				model.light(this.ambient + 64, this.contrast + 768, -50, -10, -50, false);
			} else {
				model.ambient = (short) (ambient + 64);
				model.contrast = (short) (contrast + 768);
				model.calculateVertexNormals();
			}

			cache.put(model, uid);
		}

		if (nonFlatShading) {
			model = model.copyLight();
		}

		if (mergeNormals) {
			if (!nonFlatShading) {
				model = model.hillskew(heightMap, localX, mean, localY, true, 0);
			} else {
				model = model.hillskew2(heightMap, localX, mean, localY, 0);
			}
		}

		if (!nonFlatShading) {
			model.isModel = true;
		}
		return model;
	}

	public Model getModel(int type, int rotation, int[][] tileHeights, int tileHeightMean, int localX, int localY) {
		long uid;
		if (this.modelTypes == null) {
			uid = (long) ((this.type << 10) + rotation);
		} else {
			uid = (long) ((this.type << 10) + (type << 3) + rotation);
		}

		final Cache cache = modelCache;
		Model model = (Model) cache.get(uid);
		if (model == null) {
			Model unlitModel = model(type, rotation);
			if (unlitModel == null) {
				return null;
			}

			unlitModel.light(ambient + 64, contrast + 768, -50, -10, -50, false);
			model = unlitModel;
			cache.put(unlitModel, uid);
		}

		if (mergeNormals) {
			model = model.hillskew(tileHeights, localX, tileHeightMean, localY, true, 0);
		}

		return model;
	}

	public boolean method579() {
		if (modelIds == null)
			return true;
		boolean flag1 = true;
		for (int i = 0; i < modelIds.length; i++)
			flag1 &= Model.isCached(modelIds[i] & 0xffff);
		return flag1;
	}

	public ObjectDefinition getTransformed() {
		int i = -1;
		if (varbit != -1) {
			VarBit varBit = VarBit.varBits[varbit];
			int index = varBit.index;
			int leastSignificantBit = varBit.leastSignificantBit;
			int mostSignificantBit = varBit.mostSignificantBit;
			int mask = Client.varBits[mostSignificantBit - leastSignificantBit];
			i = clientInstance.settings[index] >> leastSignificantBit & mask;
		} else if (varp != -1)
			i = clientInstance.settings[varp];
		if (i < 0 || i >= childrenIDs.length || childrenIDs[i] == -1)
			return null;
		else
			return forID(childrenIDs[i]);
	}

	public Model model(int objectType, int rotation) {
		Model model = null;
		if (modelTypes == null) {
			if (objectType != 10)
				return null;
			if (modelIds == null)
				return null;
			boolean flag1 = rotated ^ (rotation > 3);
			int k1 = modelIds.length;
			for (int i2 = 0; i2 < k1; i2++) {
				int l2 = modelIds[i2];
				if (flag1)
					l2 += 0x10000;
				model = (Model) modelUnlitCache.get(l2);
				if (model == null) {
					model = Model.getModel(l2 & 0xffff);
					if (model == null)
						return null;
					if (flag1)
						model.mirror();
					modelUnlitCache.put(model, l2);
				}
				if (k1 > 1)
					aModelArray741s[i2] = model;
			}

			if (k1 > 1)
				model = new Model(k1, aModelArray741s);
		} else {
			int i1 = -1;
			for (int j1 = 0; j1 < modelTypes.length; j1++) {
				if (modelTypes[j1] != objectType)
					continue;
				i1 = j1;
				break;
			}
			if (i1 == -1)
				return null;
			int j2 = modelIds[i1];
			boolean flag3 = rotated ^ (rotation > 3);
			if (flag3)
				j2 += 0x10000;
			model = (Model) modelUnlitCache.get(j2);
			if (model == null) {
				model = Model.getModel(j2 & 0xffff);
				if (model == null)
					return null;
				if (flag3)
					model.mirror();
				modelUnlitCache.put(model, j2);
			}
		}
		boolean flag = modelSizeX != 128 || modelHeight != 128 || modelSizeY != 128;
		boolean flag2 = offsetX != 0 || offsetHeight != 0 || offsetY != 0;
		Model model_3 = new Model(modifiedModelColors == null, true, rotation == 0 && !flag && !flag2, modifiedTexture == null, model);
		while (rotation-- > 0)
			model_3.rotate90Degrees();
		if (modifiedModelColors != null) {
			for (int k2 = 0; k2 < modifiedModelColors.length; k2++)
				model_3.recolor(modifiedModelColors[k2], originalModelColors[k2]);

		}
		if (modifiedTexture != null) {
			// ??? they backwards lmfao
			for (int k2 = 0; k2 < modifiedTexture.length; k2++)
				model_3.retexture(modifiedTexture[k2], originalTexture[k2]);

		}
		if (flag)
			model_3.scale(modelSizeX, modelSizeY, modelHeight);
		if (flag2)
			model_3.offsetBy(offsetX, offsetHeight, offsetY);
		if (mergeInteractState == 1)
			model_3.itemDropHeight = model_3.modelBaseY;
		return model_3;
	}

	private void decode(Buffer buffer, int objectId) {
		int actionIndex = -1;
		while (true) {
			int opcode = buffer.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			if (opcode == 1) {
				int len = buffer.readUnsignedByte();
				if (len > 0) {
					if (modelIds == null) {
						modelTypes = new int[len];
						modelIds = new int[len];

						for (int i = 0; i < len; i++) {
							modelIds[i] = buffer.readUnsignedShort();
							modelTypes[i] = buffer.readUnsignedByte();
						}
					} else {
						buffer.position += len * 3;
					}
				}
			} else if (opcode == 2) {
				name = buffer.readStringCp1252NullTerminated();
			} else if (opcode == 3) {
			//	description = buffer.readString();
			} else if (opcode == 5) {
				int len = buffer.readUnsignedByte();
				if (len > 0) {
					if (modelIds == null) {
						modelTypes = null;
						modelIds = new int[len];

						for (int i = 0; i < len; i++) {
							modelIds[i] = buffer.readUnsignedShort();
						}
					} else {
						buffer.position += len * 2;
					}
				}
			} else if (opcode == 14) {
				width = buffer.readUnsignedByte();
			} else if (opcode == 15) {
				length = buffer.readUnsignedByte();
			} else if (opcode == 17) {
				solid = false;
				impenetrable = false;
			} else if (opcode == 18) {
				impenetrable = false;
			} else if (opcode == 19) {
				actionIndex = buffer.readUnsignedByte();
				if (actionIndex == 1) {
					interactive = true;
				}
			} else if (opcode == 21) {
				mergeNormals = true;
			} else if (opcode == 22) {
				nonFlatShading = true;
			} else if (opcode == 23) {
				modelClipped = true;
			} else if (opcode == 24) {
				animationId = buffer.readUnsignedShort();
				if (animationId == 65535) {
					animationId = -1;
				}
			} else if (opcode == 28) {
				decorOffset = buffer.readUnsignedByte();
			} else if (opcode == 29) {
				ambient = buffer.readSignedByte();
			} else if (opcode == 39) {
				contrast = buffer.readSignedByte();
			} else if (opcode >= 30 && opcode < 35) {
				if (interactions == null) {
					interactions = new String[5];
				}
				interactions[opcode - 30] = buffer.readStringCp1252NullTerminated();
				if (interactions[opcode - 30].equalsIgnoreCase("hidden")) {
					interactions[opcode - 30] = null;
				}
			} else if (opcode == 40) {
				int length = buffer.readUnsignedByte();
				modifiedModelColors = new short[length];
				originalModelColors = new short[length];
				for (int i = 0; i < length; i++) {
					modifiedModelColors[i] = (short) buffer.readShort();
					originalModelColors[i] = (short) buffer.readShort();
				}
			} else if (opcode == 41) {
				int length = buffer.readUnsignedByte();
				modifiedTexture = new short[length];
				originalTexture = new short[length];
				for (int i = 0; i < length; i++) {
					modifiedTexture[i] = (short) buffer.readShort();
					originalTexture[i] = (short) buffer.readShort();
				}
			} else if (opcode == 61) {
				buffer.readUnsignedShort();
			} else if (opcode == 62) {
				rotated = true;
			} else if (opcode == 64) {
				clipped = false;
			} else if (opcode == 65) {
				modelSizeX = buffer.readUnsignedShort();
			} else if (opcode == 66) {
				modelHeight = buffer.readUnsignedShort();
			} else if (opcode == 67) {
				modelSizeY = buffer.readUnsignedShort();
			} else if (opcode == 68) {
				mapsceneId = buffer.readUnsignedShort();
				if (mapsceneId > 103) {
					//System.out.println("bro? " + objectId + " object has mapscene " + mapsceneId);
					mapsceneId = -1;
				}
			} else if (opcode == 69) {
				orientation = buffer.readSignedByte();
			} else if (opcode == 70) {
				offsetX = buffer.readUnsignedShort();
			} else if (opcode == 71) {
				offsetHeight = buffer.readUnsignedShort();
			} else if (opcode == 72) {
				offsetY = buffer.readUnsignedShort();
			} else if (opcode == 73) {
				obstructsGround = true;
			} else if (opcode == 74) {
				removeClipping = true;
			} else if (opcode == 75) {
				mergeInteractState = buffer.readUnsignedByte();
			} else if (opcode == 77) {
				varbit = buffer.readUnsignedShort();
				if (varbit == 0xFFFF) {
					varbit = -1;
				}

				varp = buffer.readUnsignedShort();
				if (varp == 0xFFFF) {
					varp = -1;
				}

				int length = buffer.readUnsignedByte();
				childrenIDs = new int[length + 2];
				for (int i = 0; i <= length; ++i) {
					childrenIDs[i] = buffer.readUnsignedShort();
					if (childrenIDs[i] == 0xFFFF) {
						childrenIDs[i] = -1;
					}
				}
				childrenIDs[length + 1] = -1;
			} else if (opcode == 78) {
				buffer.readUnsignedShort();
				buffer.readUnsignedByte();
			} else if (opcode == 79) {
				buffer.readUnsignedShort();
				buffer.readUnsignedShort();
				buffer.readUnsignedByte();

				int length = buffer.readUnsignedByte();
				for (int i = 0; i < length; ++i) {
					buffer.readUnsignedShort();
				}
			} else if (opcode == 81) {
				buffer.readUnsignedByte();
			} else if (opcode == 82) {
				mapIconId = buffer.readUnsignedShort();
			} else if (opcode == 89) {
				// randomize anim start
			} else if (opcode == 92) {
				varbit = buffer.readUnsignedShort();
				if (varbit == 0xFFFF) {
					varbit = -1;
				}

				varp = buffer.readUnsignedShort();
				if (varp == 0xFFFF) {
					varp = -1;
				}

				int var = buffer.readUnsignedShort();
				if (var == 0xFFFF) {
					var = -1;
				}

				int length = buffer.readUnsignedByte();
				childrenIDs = new int[length + 2];

				for (int i = 0; i <= length; ++i) {
					childrenIDs[i] = buffer.readUnsignedShort();
					if (childrenIDs[i] == 0xFFFF) {
						childrenIDs[i] = -1;
					}
				}

				childrenIDs[length + 1] = var;
			} else if (opcode == 249) {
				int length = buffer.readUnsignedByte();
				for (int i = 0; i < length; i++) {
					boolean isString = buffer.readUnsignedByte() == 1;
					buffer.readUnsignedTriByte();
					if (isString) {
						buffer.readStringCp1252NullTerminated();
					} else {
						buffer.readInt();
					}
				}
			}
		}

		if (actionIndex == -1) {
			interactive = modelIds != null && (modelTypes == null || modelTypes[0] == 10);
			if (interactions != null) {
				interactive = true;
			}
		}

		if (removeClipping) {
			solid = false;
			impenetrable = false;
		}

		if (mergeInteractState == -1) {
			mergeInteractState = solid ? 1 : 0;
		}
	}

	public ObjectDefinition() {
		type = -1;
	}

	private short[] originalTexture;
	private short[] modifiedTexture;
	public boolean obstructsGround;
	public byte ambient;
	public int offsetX;
	public String name;
	public int modelSizeY;
	public static final Model[] aModelArray741s = new Model[4];
	public byte contrast;
	public int width;
	public int offsetHeight;
	public int mapIconId;
	public short[] originalModelColors;
	public int modelSizeX;
	public int varp;
	public boolean rotated;
	public static boolean lowMem;
	public static Buffer stream;
	public int type;
	public static int[] streamIndices;
	public boolean impenetrable;
	public int mapsceneId;
	public int childrenIDs[];
	public int mergeInteractState;
	public int length;
	public boolean mergeNormals;
	public boolean modelClipped;
	public static Client clientInstance;
	public boolean removeClipping;
	public boolean solid;
	public int orientation;
	public boolean nonFlatShading;
	public static int cacheIndex;
	public int modelHeight;
	public int[] modelIds;
	public int varbit;
	public int decorOffset;
	public int[] modelTypes;
	public byte description[];
	public boolean interactive;
	public boolean clipped;
	public int animationId;
	public static ObjectDefinition[] cache;
	public int offsetY;
	public short[] modifiedModelColors;
	public String interactions[];

	// these are used by getModelSharelight
	public static Cache cachedEntities = new Cache(30);

	// these are used by getModelAnimated and getModel, getModel is used by objects which are spawned and does not adjust lighting to surrounding objects
	public static Cache modelCache = new Cache(30);

	// these are used by getModelUnlit
	public static Cache modelUnlitCache = new Cache(500);
}
