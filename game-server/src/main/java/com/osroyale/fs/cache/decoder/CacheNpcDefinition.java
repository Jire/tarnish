package com.osroyale.fs.cache.decoder;

import com.osroyale.fs.cache.archive.Archive;

import java.util.Arrays;

public final class CacheNpcDefinition {

    public static void unpackConfig(final Archive archive) {
        buffer = Buffer.fromByteBuffer(archive.getData("npc.dat"));
        final Buffer idx = Buffer.fromByteBuffer(archive.getData("npc.idx"));
        int highestFileId = idx.readUnsignedShort();
        //System.out.println("highest NPC=" + highestFileId);
        offsets = new int[highestFileId + 1];
        int offset = 0;
        for (int i = 0; i < highestFileId; i++) {
            final int size = idx.readUnsignedShort();
            if (size == -1 || size == 65535) {
                //System.err.println("BREAK AT " + j + " (size="+size+")");
                break;
            }
            offsets[i] = offset;
            offset += size;
            //System.err.println("j=" + j + ", i=" + i + ", size=" + size);
        }

        npcDefCache = new CacheNpcDefinition[20];

        for (int k = 0; k < 20; k++) {
            npcDefCache[k] = new CacheNpcDefinition();
        }
    }

    public static CacheNpcDefinition lookup(int npcId) {
        for (int cacheIndex = 0; cacheIndex < 20; cacheIndex++)
            if (npcDefCache[cacheIndex].interfaceType == (long) npcId)
                return npcDefCache[cacheIndex];

        nextNpcDefCacheIndex = (nextNpcDefCacheIndex + 1) % 20;
        CacheNpcDefinition entityDef = npcDefCache[nextNpcDefCacheIndex] = new CacheNpcDefinition();
        buffer.position = offsets[npcId];
        entityDef.interfaceType = npcId;
        entityDef.decode(buffer);
        if (entityDef.combatLevel == 0) {
            entityDef.combatLevel = 1;
        }

        switch (npcId) {
            case 9855:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";
                entityDef.actions[3] = "Skull";
                entityDef.name = "Darth Pker";
                entityDef.description = "The hunter of bounties".getBytes();
                break;
            case 5567:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Pick-up";
                entityDef.standingAnimation = 813;
                entityDef.walkingAnimation = 1146;
                entityDef.widthScale = 85;
                entityDef.heightScale = 85;
                entityDef.name = "Baby Darth";
                entityDef.description = "The baby hunter of bounties".getBytes();
                break;
            case 6773:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;
            case 7481:
                entityDef.name = "Tarnish Vote Agent";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";
                break;
            case 7601:
                entityDef.name = "Shady Insurance Agent";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Open";
                break;
            case 2462:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;
            case 3216:
                entityDef.name = "Melee store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;
            case 3217:
                entityDef.name = "Beginner Shops";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;
            case 3218:
                entityDef.name = "Magic store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            case 7062:
                entityDef.name = "Ensouled Hunter";
                break;
            /* Fishing */
            case 1518:
                entityDef.name = "Shimps & Anchovies";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Net";
                break;
            case 1526:
                entityDef.name = "Trout & Salmon";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Bait";
                break;
            case 311:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";
                entityDef.actions[3] = "Claim-armour";
                break;
            case 1519:
                entityDef.name = "Lobster & Swordfish";
                break;
            case 6533:
                entityDef.name = "Tarnish Skillermen";
                break;
            case 1520:
                entityDef.name = "Shark";
                break;
            case 1521:
                entityDef.name = "Manta ray";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Big Net";
                break;
            case 1534:
                entityDef.name = "Monkfish";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Net";
                break;
            case 1536:
                entityDef.name = "Dark crab";
                break;
            case 1600:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Bank";
                break;
            case 1603:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";

                break;
            case 326:
            case 321:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Dismiss";
                break;
            case 5419:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                entityDef.actions[3] = "Trade";
                break;
            /** Crafting master. */
            case 5811:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Tan";
                entityDef.actions[2] = "Trade";
                break;
            /* Clanmaster */
            case 3841:
                entityDef.name = "Clanmaster";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;
            /* Clothing */
            case 534:
                entityDef.name = "Clothing store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Pure */
            case 5440:
                entityDef.name = "Pure store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Mage */
            case 4400:
                entityDef.name = "Mage store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Range */
            case 1576:
                entityDef.name = "Range store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;


            case 1157:
            case 1158:
            case 1160:
                entityDef.actions = new String[5];
                entityDef.actions[1] = "Attack";
                break;

            /* Skill */
            case 505:
                entityDef.name = "Skilling store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Hunter */
            case 1504:
                entityDef.name = "Hunter store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Cook */
            case 1199:
                entityDef.name = "Consumable store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Farming */
            case 3258:
                entityDef.name = "Farming store";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Open";
                break;

            /* Achievement */
            case 5527:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";
                break;

            /* Banker */
            case 1480:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Bank";
                break;

            /* Voting */
            case 3531:
                entityDef.name = "Vote";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";
                entityDef.actions[3] = "Claim";
                break;

            /* Spellbook */
            case 4397:
                entityDef.name = "Spellbook";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Change";
                break;

            /* Royal Points */
            case 5523:
                entityDef.name = "The Donator King";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Open-store";
                entityDef.actions[3] = "Claim-purchase";
                entityDef.description = "What more is there to say about The Donator King?.".getBytes();
                break;

            case 306:
                entityDef.name = "Tarnish Guide";
                break;
            case 5366:
                entityDef.name = "Skilling Shop";
                break;
            case 364:
                entityDef.name = "Skilling Store";
                break;

            /* Clothing */
            case 1307:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Makeover";
                break;

            /* Thieving Stalls */
            case 3439:
                entityDef.name = "Merchant";
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Sell goods";
                break;

            case 5919:
            case 1755:
            case 2186:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Trade";
                break;
            case 1757:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                break;
            case 2180:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Exchange for firecape";
                break;

            /* Nieve */
            case 490:
            case 6797:
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Open-interface";
                entityDef.actions[3] = "Offer-items";
                break;
            case 1756:
                entityDef.name = "Void Knight";
                entityDef.combatLevel = 0;
                entityDef.actions = new String[5];
                entityDef.actions[1] = "Attack";
                break;
            case 7431:
                entityDef.name = "Skilling Store";
                entityDef.combatLevel = 0;
                entityDef.actions = new String[2];
                entityDef.actions[1] = "Talk-to";
                entityDef.actions[1] = "Trade";

                break;
            case 345:
                entityDef.name = "Polly";
                entityDef.description = "She takes pride in prestiging.".getBytes();
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.actions[2] = "Trade";
                entityDef.actions[3] = "Prestige-panel";
                entityDef.actions[4] = "Perk-information";
                entityDef.walkingAnimation = 819;
                entityDef.standingAnimation = 808;
                entityDef.modelIds = new int[10];
                entityDef.modelIds[0] = 391;
                entityDef.modelIds[1] = 414;
                entityDef.modelIds[2] = 18983;
                entityDef.modelIds[3] = 361;
                entityDef.modelIds[4] = 356;
                entityDef.modelIds[5] = 556;
                entityDef.modelIds[6] = 332;
                entityDef.modelIds[7] = 474;
                entityDef.modelIds[8] = 433;
                entityDef.modelIds[9] = 16348;
                entityDef.recolorTarget = new int[]{127, 127, 127, 127, 9168, -22419, 9143, 9168, 9143, 7744, 127, 127}; // Colour you want to change to//
                entityDef.recolorOriginal = new int[]{4626, 10128, 10004, 5018, 61, 10351, 57280, 54183, 54503, 6798, 8741, 25238}; // Original colour
                break;
            case 6481:
                entityDef.name = "Mac";
                entityDef.description = "Only the most knowledgeable players of Tarnish are worthy of such a cape.".getBytes();
                entityDef.combatLevel = 126;
                entityDef.walkingAnimation = 819;
                //entityDef.standingAnimation = 808;
                entityDef.actions = new String[5];
                entityDef.actions[0] = "Talk-to";
                entityDef.modelIds = new int[8];
                entityDef.modelIds[0] = 29615;
                entityDef.modelIds[1] = 29618;
                entityDef.modelIds[2] = 29621;
                entityDef.modelIds[3] = 29616;
                entityDef.modelIds[4] = 29620;
                entityDef.modelIds[5] = 176;
                entityDef.modelIds[6] = 29619;
                entityDef.modelIds[7] = 29622;
                break;
            case 367:
                entityDef.actions = new String[5]; // Actions for the npc
                entityDef.modelIds = new int[9]; //Number of models it uses
                entityDef.modelIds[0] = 27636; //Bandos chest
                entityDef.modelIds[1] = 27625; //Tassets
                entityDef.modelIds[2] = 13307; //Barrows gloves
                entityDef.modelIds[3] = 28826; //BCP (arms)
                entityDef.modelIds[4] = 29250; //Primordial boots
                entityDef.modelIds[5] = 32678; //Elder maul
                entityDef.modelIds[6] = 14424; //Serp helm
                entityDef.modelIds[7] = 31227; // Torture
                entityDef.modelIds[8] = 29616; //Max cape
                entityDef.standingAnimation = 7518; // Npc's Stand Emote
                entityDef.walkingAnimation = 7520;
                entityDef.recolorOriginal = new int[]{668, 675, 673, 815, 784};
                entityDef.recolorTarget = new int[]{947, 960, 7104, 8146, 0};
                entityDef.name = "Donator Guard";
                entityDef.combatLevel = 420;
                entityDef.description = "The protector of the donator zone.".getBytes(); // NPC description
                break;
        }

        return entityDef;
    }

    public void decode(Buffer stream) {
        int lastOpcode = -1;
        do {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0)
                return;
            if (opcode == 1) {
                int j = stream.readUnsignedByte();
                modelIds = new int[j];
                for (int j1 = 0; j1 < j; j1++)
                    modelIds[j1] = stream.readUnsignedShort();

            } else if (opcode == 2) {
                name = stream.readStringCp1252NullTerminated();
            } else if (opcode == 3)
                description = stream.readBytes();
            else if (opcode == 12)
                size = stream.readSignedByte();
            else if (opcode == 13)
                standingAnimation = stream.readUnsignedShort();
            else if (opcode == 14)
                walkingAnimation = stream.readUnsignedShort();
            else if (opcode == 15)
                stream.readUnsignedShort();
            else if (opcode == 16)
                stream.readUnsignedShort();
            else if (opcode == 17) {
                walkingAnimation = stream.readUnsignedShort();
                halfTurnAnimation = stream.readUnsignedShort();
                quarterClockwiseTurnAnimation = stream.readUnsignedShort();
                quarterAnticlockwiseTurnAnimation = stream.readUnsignedShort();
                if (halfTurnAnimation == 65535) {
                    halfTurnAnimation = walkingAnimation;
                }
                if (quarterClockwiseTurnAnimation == 65535) {
                    quarterClockwiseTurnAnimation = walkingAnimation;
                }
                if (quarterAnticlockwiseTurnAnimation == 65535) {
                    quarterAnticlockwiseTurnAnimation = walkingAnimation;
                }
            } else if (opcode == 18) {
                stream.readUnsignedShort();
            } else if (opcode >= 30 && opcode < 35) {
                if (actions == null)
                    actions = new String[5];
                actions[opcode - 30] = stream.readStringCp1252NullTerminated();
                if (actions[opcode - 30].equalsIgnoreCase("hidden"))
                    actions[opcode - 30] = null;
            } else if (opcode == 40) {
                int colors = stream.readUnsignedByte();
                recolorOriginal = new int[colors];
                recolorTarget = new int[colors];
                for (int k1 = 0; k1 < colors; k1++) {
                    recolorOriginal[k1] = stream.readUnsignedShort();
                    recolorTarget[k1] = stream.readUnsignedShort();
                }
            } else if (opcode == 41) {
                int length = stream.readUnsignedByte();

                for (int index = 0; index < length; index++) {
                    stream.readUnsignedShort();
                    stream.readUnsignedShort();
                }
            } else if (opcode == 60) {
                int l = stream.readUnsignedByte();
                anIntArray73 = new int[l];
                for (int l1 = 0; l1 < l; l1++)
                    anIntArray73[l1] = stream.readUnsignedShort();

            } else if (opcode == 90)
                stream.readUnsignedShort();
            else if (opcode == 91)
                stream.readUnsignedShort();
            else if (opcode == 92)
                stream.readUnsignedShort();
            else if (opcode == 93)
                drawMapDot = false;
            else if (opcode == 95)
                combatLevel = stream.readUnsignedShort();
            else if (opcode == 97)
                widthScale = stream.readUnsignedShort();
            else if (opcode == 98)
                heightScale = stream.readUnsignedShort();
            else if (opcode == 99)
                isVisible = true;
            else if (opcode == 100)
                lightModifier = stream.readSignedByte();
            else if (opcode == 101)
                shadowModifier = stream.readSignedByte() * 5;
            else if (opcode == 102) {
                int bitfield = stream.readUnsignedByte();
                int len = 0;
                for (int var5 = bitfield; var5 != 0; var5 >>= 1) {
                    ++len;
                }

                int[] headIconArchiveIds = new int[len];
                short[] headIconSpriteIndex = new short[len];

                for (int i = 0; i < len; i++) {
                    if ((bitfield & 1 << i) == 0) {
                        headIconArchiveIds[i] = -1;
                        headIconSpriteIndex[i] = -1;
                    } else {
                        headIconArchiveIds[i] = stream.readBigSmart2();
                        headIconSpriteIndex[i] = (short) stream.readUnsignedShortSmartMinusOne();
                    }
                }

                headIconSprite = headIconSpriteIndex[0];
            } else if (opcode == 103) {
                rotation = stream.readUnsignedShort();
            } else if (opcode == 106) {
                transformVarbit = stream.readUnsignedShort();
                if (transformVarbit == 65535) {
                    transformVarbit = -1;
                }

                transformVarp = stream.readUnsignedShort();
                if (transformVarp == 65535) {
                    transformVarp = -1;
                }

                int var = -1;

                int length = stream.readUnsignedByte();
                childrenIDs = new int[length + 2];

                for (int index = 0; index <= length; index++) {
                    childrenIDs[index] = stream.readUnsignedShort();
                    if (childrenIDs[index] == 65535) {
                        childrenIDs[index] = -1;
                    }
                }

                childrenIDs[length + 1] = var;
            } else if (opcode == 107) {
                isInteractable = false;
            } else if (opcode == 109) {
//                rotationFlag = false;
            } else if (opcode == 111) {
//                isPet = true;
            } else if (opcode == 114) {
                stream.readUnsignedShort();
            } else if (opcode == 115) {
                stream.readUnsignedShort();
                stream.readUnsignedShort();
                stream.readUnsignedShort();
                stream.readUnsignedShort();
            } else if (opcode == 116) {
                stream.readUnsignedShort();
            } else if (opcode == 117) {
                stream.readUnsignedShort();
                stream.readUnsignedShort();
                stream.readUnsignedShort();
                stream.readUnsignedShort();
            } else if (opcode == 118) {
                transformVarbit = stream.readUnsignedShort();
                if (transformVarbit == 65535) {
                    transformVarbit = -1;
                }

                transformVarp = stream.readUnsignedShort();
                if (transformVarp == 65535) {
                    transformVarp = -1;
                }

                int var = stream.readUnsignedShort();
                if (var == 0xFFFF) {
                    var = -1;
                }

                int length = stream.readUnsignedByte();
                childrenIDs = new int[length + 2];

                for (int index = 0; index <= length; index++) {
                    childrenIDs[index] = stream.readUnsignedShort();
                    if (childrenIDs[index] == 65535) {
                        childrenIDs[index] = -1;
                    }
                }

                childrenIDs[length + 1] = var;
            } else if (opcode == 249) {
                int length = stream.readUnsignedByte();

                for (int i = 0; i < length; i++) {
                    boolean isString = stream.readUnsignedByte() == 1;
                    stream.readUnsignedTriByte();

                    if (isString) {
                        stream.readStringCp1252NullTerminated();
                    } else {
                        stream.readUnsignedInt();
                    }
                }
            } else {
                System.out.println("Unrecognized NPC opcode " + opcode + ", last=" + lastOpcode);
                return;
            }
            lastOpcode = opcode;
        } while (true);
    }

    public CacheNpcDefinition() {
        quarterAnticlockwiseTurnAnimation = -1;
        transformVarbit = -1;
        halfTurnAnimation = -1;
        transformVarp = -1;
        combatLevel = -1;
        anInt64 = 1834;
        walkingAnimation = -1;
        size = 1;
        headIconSprite = -1;
        standingAnimation = -1;
        interfaceType = -1L;
        rotation = 32;
        quarterClockwiseTurnAnimation = -1;
        isInteractable = true;
        heightScale = 128;
        drawMapDot = true;
        widthScale = 128;
        isVisible = false;
    }

    public int quarterAnticlockwiseTurnAnimation;
    public static int nextNpcDefCacheIndex;
    public int transformVarbit;
    public int halfTurnAnimation;
    public int transformVarp;
    public static Buffer buffer;
    public int combatLevel;
    public final int anInt64;
    public String name;
    public String[] actions;
    public int walkingAnimation;
    public byte size;
    public int[] recolorTarget;
    public static int[] offsets;
    public int[] anIntArray73;
    public int headIconSprite;
    public int[] recolorOriginal;
    public int standingAnimation;
    public long interfaceType;
    public int rotation;
    public static CacheNpcDefinition[] npcDefCache;
    public int quarterClockwiseTurnAnimation;
    public boolean isInteractable;
    public int lightModifier;
    public int heightScale;
    public boolean drawMapDot;
    public int[] childrenIDs;
    public byte[] description;
    public int widthScale;
    public int shadowModifier;
    public boolean isVisible;
    public int[] modelIds;
    public int interfaceZoom = 0;

    @Override
    public String toString() {
        return "NpcDefinition{" +
                "quarterAnticlockwiseTurnAnimation=" + quarterAnticlockwiseTurnAnimation +
                ", transformVarbit=" + transformVarbit +
                ", halfTurnAnimation=" + halfTurnAnimation +
                ", transformVarp=" + transformVarp +
                ", combatLevel=" + combatLevel +
                ", anInt64=" + anInt64 +
                ", name='" + name + '\'' +
                ", actions=" + Arrays.toString(actions) +
                ", walkingAnimation=" + walkingAnimation +
                ", size=" + size +
                ", recolorTarget=" + Arrays.toString(recolorTarget) +
                ", anIntArray73=" + Arrays.toString(anIntArray73) +
                ", anInt75=" + headIconSprite +
                ", recolorOriginal=" + Arrays.toString(recolorOriginal) +
                ", standingAnimation=" + standingAnimation +
                ", interfaceType=" + interfaceType +
                ", rotation=" + rotation +
                ", quarterClockwiseTurnAnimation=" + quarterClockwiseTurnAnimation +
                ", isInteractable=" + isInteractable +
                ", lightModifier=" + lightModifier +
                ", scaleY=" + heightScale +
                ", drawMapDot=" + drawMapDot +
                ", childrenIDs=" + Arrays.toString(childrenIDs) +
                ", description=" + Arrays.toString(description) +
                ", scaleXZ=" + widthScale +
                ", shadowModifier=" + shadowModifier +
                ", isVisible=" + isVisible +
                ", modelId=" + Arrays.toString(modelIds) +
                ", interfaceZoom=" + interfaceZoom +
                '}';
    }

}
