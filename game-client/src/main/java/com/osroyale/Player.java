package com.osroyale;

import com.osroyale.profile.Profile;
import com.osroyale.profile.ProfileManager;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.osroyale.Model.modelColors;

/**
 * The player class.
 *
 * @author Daniel
 */
public final class Player extends Entity implements RSPlayer {

    String title;
    String clanTag;
    String clanChannel;
    String cachedName;
    String clanTagColor;
    int titleColor;
    private long aLong1697;
    NpcDefinition desc;
    boolean aBoolean1699;
    final int[] appearanceColors;
    private int defaultParticleColor = 0xFFFFFF;
    private int maxCapeParticleColor = defaultParticleColor;
    int team;
    private int gender;
    public String name;
    static Cache mruNodes = new Cache(260);
    double combatLevel;
    int headIcon;
    int skullIcon;
    int bountyIcon;
    int hintIcon;
    int anInt1707;
    int anInt1708;
    int anInt1709;
    boolean visible;
    int anInt1711;
    int anInt1712;
    int anInt1713;
    Model aModel_1714;
    final int[] appearanceModels;
    private long appearanceHash;
    int anInt1719;
    int anInt1720;
    int anInt1721;
    int anInt1722;
    int skill;

    Player() {
        aLong1697 = -1L;
        aBoolean1699 = false;
        appearanceColors = new int[5];
        visible = false;
        appearanceModels = new int[12];
    }

    public Model getRotatedModel() {

        if (!visible) {
            return null;
        }

        Model animatedModel = getAnimatedModel();

        if (animatedModel == null) {
            return null;
        }

        super.height = animatedModel.modelBaseY;
        animatedModel.singleTile = true;

        if (aBoolean1699) {
            return animatedModel;
        }

        if (super.graphicId != -1 && super.currentAnimationId != -1) {
            Graphic spotAnim = Graphic.graphics[super.graphicId];

            Model spotAnimationModel = spotAnim.getModel();

            /**
             * MAKE SURE WE'VE LOADED THE GRAPHIC BEFORE ATTEMPTING TO DO IT.
             * Fixes graphics flickering.
             */
            if (FrameBase.frameBases[spotAnim.animationSequence.primaryFrameIds[0] >> 16].length == 0) {
                spotAnimationModel = null;
            }

            if (spotAnimationModel != null) {

                Model model_3 = new Model(true, Frame.hasAlphaTransform(super.currentAnimationId), false, spotAnimationModel);
                model_3.offsetBy(0, -super.graphicHeight, 0);
                model_3.generateBones();
                model_3.interpolate(spotAnim.animationSequence.primaryFrameIds[super.currentAnimationId]);
                model_3.groupedTriangleLabels = null;
                model_3.groupedVertexLabels = null;
                if (spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128)
                    model_3.scale(spotAnim.resizeXY, spotAnim.resizeXY, spotAnim.resizeZ);
                model_3.light(84 + spotAnim.modelBrightness, 1550 + spotAnim.modelShadow, -30, -50, -30, true);
                Model models[] = {animatedModel, model_3};
                animatedModel = new Model(models);
            }
        }

        if (aModel_1714 != null) {
            if (Client.tick >= anInt1708)
                aModel_1714 = null;
            if (Client.tick >= anInt1707 && Client.tick < anInt1708) {
                Model model_1 = aModel_1714;
                assert model_1 != null;
                model_1.offsetBy(anInt1711 - super.x, anInt1712 - anInt1709, anInt1713 - super.y);
                if (super.turnDirection == 512) {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                } else if (super.turnDirection == 1024) {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                } else if (super.turnDirection == 1536)
                    model_1.rotate90Degrees();
                Model models[] = {animatedModel, model_1};
                animatedModel = new Model(models);
                if (super.turnDirection == 512)
                    model_1.rotate90Degrees();
                else if (super.turnDirection == 1024) {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                } else if (super.turnDirection == 1536) {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                }
                model_1.offsetBy(super.x - anInt1711, anInt1709 - anInt1712, super.y - anInt1713);
            }
        }
        animatedModel.singleTile = true;
        return animatedModel;
    }

    void updateAppearance(Buffer stream) {
        stream.position = 0;
        gender = stream.readUnsignedByte();
        headIcon = stream.readUnsignedByte();
        skullIcon = stream.readUnsignedByte();
        bountyIcon = stream.readUnsignedByte();
        desc = null;
        team = 0;
        for (int bodyPart = 0; bodyPart < 12; bodyPart++) {
            int reset = stream.readUnsignedByte();
            if (reset == 0) {
                appearanceModels[bodyPart] = 0;
                continue;
            }
            int id = stream.readUnsignedByte();

            appearanceModels[bodyPart] = (reset << 8) + id;
            if (bodyPart == 0 && appearanceModels[0] == 65535) {
                desc = NpcDefinition.lookup(stream.readUnsignedShort());
                break;
            }

            if (appearanceModels[bodyPart] >= 512 && appearanceModels[bodyPart] - 512 < ItemDefinition.highestFileId) {
                int team = ItemDefinition.lookup(appearanceModels[bodyPart] - 512).team;
                if (team != 0) {
                    this.team = team;
                }
            }
        }

        for (int part = 0; part < 5; part++) {
            int color = stream.readUnsignedByte();
            if (color < 0 || color >= Client.PLAYER_BODY_RECOLOURS[part].length) {
                color = 0;
            }
            appearanceColors[part] = color;
        }

        int equip = appearanceModels[1];
        if (equip >= 512) {
            ItemDefinition def = ItemDefinition.lookup(equip - 512);
            if (maxCapeIds(equip - 512)) {
                this.maxCapeParticleColor = (def.recolorFrom == null || def.recolorTo == null) ? defaultParticleColor : modelColors[def.recolorTo[2]];
            }

            if (maxCapeParticleColor == 10000525) {
                maxCapeParticleColor = 0;
            } else if (maxCapeParticleColor == 3288622) {
                maxCapeParticleColor = 7622056;
            }
            //System.out.println("particle color: "+maxCapeParticleColor);
        }

        super.seqStandID = stream.readUnsignedShort();
        if (super.seqStandID == 65535) {
            super.seqStandID = -1;
        }

        super.turnAnimation = stream.readUnsignedShort();
        if (super.turnAnimation == 65535) {
            super.turnAnimation = -1;
        }

        super.walkingAnimation = stream.readUnsignedShort();
        if (super.walkingAnimation == 65535) {
            super.walkingAnimation = -1;
        }

        super.halfTurnAnimation = stream.readUnsignedShort();
        if (super.halfTurnAnimation == 65535) {
            super.halfTurnAnimation = -1;
        }

        super.quarterClockwiseTurnAnimation = stream.readUnsignedShort();
        if (super.quarterClockwiseTurnAnimation == 65535) {
            super.quarterClockwiseTurnAnimation = -1;
        }

        super.quarterAnticlockwiseTurnAnimation = stream.readUnsignedShort();
        if (super.quarterAnticlockwiseTurnAnimation == 65535) {
            super.quarterAnticlockwiseTurnAnimation = -1;
        }

        super.runAnimation = stream.readUnsignedShort();
        if (super.runAnimation == 65535) {
            super.runAnimation = -1;
        }
        name = StringUtils.fixName(StringUtils.nameForLong(stream.readUnsignedLong()));
        title = StringUtils.fixName(stream.readString());
        titleColor = stream.readUnsignedInt();
        clanChannel = stream.readString();
        clanTag = stream.readString();
        clanTagColor = stream.readString();

        String tcolor = title.length() < 3 ? "" : "<col=" + Integer.toHexString(titleColor) + ">";
        cachedName = tcolor + title + "</col> " + name;
        combatLevel = Double.longBitsToDouble(stream.readUnsignedLong());
        privelage = stream.readUnsignedByte();
        skill = stream.readUnsignedShort();
        visible = true;
        appearanceHash = 0L;

        for (int k1 = 0; k1 < 12; k1++) {
            appearanceHash <<= 4;
            if (appearanceModels[k1] >= 256)
                appearanceHash += appearanceModels[k1] - 256;
        }

        if (appearanceModels[0] >= 256) {
            appearanceHash += appearanceModels[0] - 256 >> 4;
        }

        if (appearanceModels[1] >= 256) {
            appearanceHash += appearanceModels[1] - 256 >> 8;
        }

        for (int i2 = 0; i2 < 5; i2++) {
            appearanceHash <<= 3;
            appearanceHash += appearanceColors[i2];
        }

        appearanceHash <<= 1;
        appearanceHash += gender;

        appearanceHash += this.maxCapeParticleColor;
    }

    public boolean maxCapeIds(int itemId) {
        return ((itemId == 21285) || (itemId == 13329) || (itemId == 13280) || (itemId == 13331) || (itemId == 13335) || (itemId == 13337) || (itemId == 13333) || (itemId == 20760));
    }

    Model getAnimatedModel() {
        try {
            final boolean tween = Client.instance.isInterpolatePlayerAnimations();

            long bitset = appearanceHash;
            int nextFrame = -1;
            int j1 = -1;
            int k1 = -1;
            int primaryId = -1;
            int secondaryId = -1;
            int cycle1 = -1;
            int cycle2 = -1;
            if (desc != null) {
                Animation secondarySeq = null;
                if (super.primarySeqID >= 0 && super.primarySeqDelay == 0) {
                    final Animation primarySeq = Animation.animations[super.primarySeqID];
                    final boolean skeletal = primarySeq.isSkeletalAnimation();

                    if (!skeletal) {
                        if (tween && super.nextAnimationFrame < primarySeq.primaryFrameIds.length) {
                            nextFrame = primarySeq.primaryFrameIds[super.nextAnimationFrame];
                            cycle1 = primarySeq.frameDelays[super.primarySeqFrame];
                            cycle2 = super.primarySeqCycle;
                        }
                    }

                    primaryId = primarySeq.isSkeletalAnimation()
                            ? primarySeq.getSkeletalFrameId()
                            : primarySeq.primaryFrameIds[super.primarySeqFrame];

                    if (super.secondarySeqID >= 0 && super.secondarySeqID != super.seqStandID) {
                        secondarySeq = Animation.animations[super.secondarySeqID];
                        secondaryId = secondarySeq.isSkeletalAnimation()
                                ? secondarySeq.getSkeletalFrameId()
                                : secondarySeq.primaryFrameIds[super.secondarySeqFrame];
                    }

                    // double anim
                    if (primarySeq.isSkeletalAnimation() || (secondarySeq != null && secondarySeq.isSkeletalAnimation())) {
                        return desc.getAnimatedModel(secondaryId, primaryId, nextFrame, primarySeqFrame, secondarySeqFrame,
                                primarySeq.interleaveOrder, true);
                    }

                    return desc.getAnimatedModel(secondaryId, primaryId, nextFrame, cycle1, cycle2,
                            primarySeq.interleaveOrder, false);
                }
                if (super.secondarySeqID >= 0) {
                    secondarySeq = Animation.animations[super.secondarySeqID];
                    if (secondarySeq.isSkeletalAnimation()) {
                        return desc.getAnimatedModel(secondarySeq.getSkeletalFrameId(), -1, nextFrame,
                                primarySeqFrame, secondarySeqFrame, null, true);
                    }
                    primaryId = secondarySeq.primaryFrameIds[super.secondarySeqFrame];
                    if (tween && super.nextIdleFrame < secondarySeq.primaryFrameIds.length) {
                        nextFrame = secondarySeq.primaryFrameIds[super.nextIdleFrame];
                        cycle1 = secondarySeq.frameDelays[super.secondarySeqFrame];
                        cycle2 = super.secondarySeqCycle; // now to figure out what gay naming seven used
                    }
                }
                return desc.getAnimatedModel(-1, primaryId, nextFrame, cycle1, cycle2, null, false);
            }
            if (super.primarySeqID >= 0 && super.primarySeqDelay == 0) {
                Animation animation = Animation.animations[super.primarySeqID];
                primaryId = animation.primaryFrameIds[super.primarySeqFrame];
                if (tween && super.nextAnimationFrame < animation.primaryFrameIds.length) {
                    nextFrame = animation.primaryFrameIds[super.nextAnimationFrame];
                    cycle1 = animation.frameDelays[super.primarySeqFrame];
                    cycle2 = super.primarySeqCycle;
                }
                if (super.secondarySeqID >= 0 && super.secondarySeqID != super.seqStandID) {
                    secondaryId = Animation.animations[super.secondarySeqID].primaryFrameIds[super.secondarySeqFrame];
                }
                if (animation.shield >= 0) {
                    j1 = animation.shield;
                    bitset += j1 - appearanceModels[5] << 40;
                }
                if (animation.weapon >= 0) {
                    k1 = animation.weapon;
                    bitset += k1 - appearanceModels[3] << 48;
                }
            } else if (super.secondarySeqID >= 0) {
                final Animation seq = Animation.animations[super.secondarySeqID];
                primaryId = seq.primaryFrameIds[super.secondarySeqFrame];
                if (tween && super.nextIdleFrame < seq.primaryFrameIds.length) {
                    nextFrame = seq.primaryFrameIds[super.nextIdleFrame];
                    cycle1 = seq.frameDelays[super.secondarySeqFrame];
                    cycle2 = super.secondarySeqCycle;
                }
            }
            Model model_1 = (Model) mruNodes.get(bitset);
            if (model_1 == null) {
                boolean flag = false;
                for (int i2 = 0; i2 < 12; i2++) {
                    int k2 = appearanceModels[i2];
                    if (k1 >= 0 && i2 == 3)
                        k2 = k1;
                    if (j1 >= 0 && i2 == 5)
                        k2 = j1;
                    if (k2 >= 256 && k2 < 512 && !IdentityKit.kits[k2 - 256].bodyLoaded())
                        flag = true;
                    if (k2 >= 512 && !ItemDefinition.lookup(k2 - 512).method195(gender))
                        flag = true;
                }

                if (flag) {
                    if (aLong1697 != -1L)
                        model_1 = (Model) mruNodes.get(aLong1697);
                    if (model_1 == null)
                        return null;
                }
            }
            boolean hasMaxCape = false;
            if (model_1 == null) {
                Model aclass30_sub2_sub4_sub6s[] = new Model[12];
                int j2 = 0;
                for (int l2 = 0; l2 < 12; l2++) {
                    int i3 = appearanceModels[l2];
                    if (k1 >= 0 && l2 == 3)
                        i3 = k1;
                    if (j1 >= 0 && l2 == 5)
                        i3 = j1;
                    if (i3 >= 256 && i3 < 512) {
                        Model model_3 = IdentityKit.kits[i3 - 256].method538();
                        if (model_3 != null)
                            aclass30_sub2_sub4_sub6s[j2++] = model_3;
                    }
                    if (i3 >= 512) {
                        int itemId = i3 - 512;
                        Model model_4 = ItemDefinition.lookup(i3 - 512).method196(gender);
                        if (maxCapeIds(itemId)) {
                            hasMaxCape = true;
                        }
                        if (model_4 != null)
                            aclass30_sub2_sub4_sub6s[j2++] = model_4;
                    }
                }

                model_1 = new Model(j2, aclass30_sub2_sub4_sub6s);
                for (int j3 = 0; j3 < 5; j3++)
                    if (appearanceColors[j3] != 0) {
                        model_1.recolor(Client.PLAYER_BODY_RECOLOURS[j3][0], Client.PLAYER_BODY_RECOLOURS[j3][appearanceColors[j3]]);
                        if (j3 == 1)
                            model_1.recolor(Client.anIntArray1204[0], Client.anIntArray1204[appearanceColors[j3]]);
                    }

                model_1.generateBones();
                model_1.light(84, 850, -30, -50, -30, true);
                mruNodes.put(model_1, bitset);
                aLong1697 = bitset;
            }
            if (aBoolean1699)
                return model_1;
            Model model_2 = Model.emptyModel;
            model_2.buildSharedSequenceModel(model_1,
                    Frame.hasAlphaTransform(primaryId) && Frame.hasAlphaTransform(secondaryId));
            if (primaryId != -1 && secondaryId != -1) {
                model_2.applyAnimationFrames(
                        super.primarySeqID >= 0 ? Animation.animations[super.primarySeqID].interleaveOrder : null,
                        secondaryId, primaryId);
            } else if (primaryId != -1) {
                model_2.applyAnimationFrame(true, primaryId, nextFrame, cycle1, cycle2);
            }
            model_2.calculateBoundsCylinder();
            model_2.groupedTriangleLabels = null;
            model_2.groupedVertexLabels = null;

            return model_2;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public int privelage;

    public Model model() {
        if (!visible)
            return null;
        if (desc != null)
            return desc.method160();
        boolean flag = false;
        for (int i = 0; i < 12; i++) {
            int j = appearanceModels[i];
            if (j >= 256 && j < 512 && !IdentityKit.kits[j - 256].headLoaded())
                flag = true;
            if (j >= 512 && !ItemDefinition.lookup(j - 512).method192(gender))
                flag = true;
        }

        if (flag)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[12];
        int k = 0;
        for (int l = 0; l < 12; l++) {
            int i1 = appearanceModels[l];
            if (i1 >= 256 && i1 < 512) {
                Model model_1 = IdentityKit.kits[i1 - 256].headModel();
                if (model_1 != null)
                    aclass30_sub2_sub4_sub6s[k++] = model_1;
            }
            if (i1 >= 512) {
                Model model_2 = ItemDefinition.lookup(i1 - 512).getModelForGender(gender);
                if (model_2 != null)
                    aclass30_sub2_sub4_sub6s[k++] = model_2;
            }
        }

        Model model = new Model(k, aclass30_sub2_sub4_sub6s);
        for (int j1 = 0; j1 < 5; j1++)
            if (appearanceColors[j1] != 0) {
                model.recolor(Client.PLAYER_BODY_RECOLOURS[j1][0], Client.PLAYER_BODY_RECOLOURS[j1][appearanceColors[j1]]);
                if (j1 == 1)
                    model.recolor(Client.anIntArray1204[0], Client.anIntArray1204[appearanceColors[j1]]);
            }

        return model;
    }

    public void saveProfile(Client client) {
        if (!Settings.REMEMBER_ME) {
            return;
        }
        ProfileManager.add(new Profile(client.myUsername, client.myPassword, gender, appearanceModels, appearanceColors));
    }

    @Override
    public Polygon[] getPolygons() {
        RSModel model = getModel();

        if (model == null) {
            return null;
        }

        int[] x2d = new int[model.getVertexCount()];
        int[] y2d = new int[model.getVertexCount()];

        int localX = getX();
        int localY = getY();

        final int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(localX, localY), Client.instance.getPlane());

        Perspective.modelToCanvas(Client.instance, model.getVertexCount(), localX, localY, tileHeight, getOrientation(), model.getVerticesX(), model.getVerticesZ(), model.getVerticesY(), x2d, y2d);
        ArrayList polys = new ArrayList(model.getFaceCount());

        int[] trianglesX = model.getFaceIndices1();
        int[] trianglesY = model.getFaceIndices2();
        int[] trianglesZ = model.getFaceIndices3();

        for (int triangle = 0; triangle < model.getFaceCount(); ++triangle) {
            int[] xx =
                    {
                            x2d[trianglesX[triangle]], x2d[trianglesY[triangle]], x2d[trianglesZ[triangle]]
                    };

            int[] yy =
                    {
                            y2d[trianglesX[triangle]], y2d[trianglesY[triangle]], y2d[trianglesZ[triangle]]
                    };

            polys.add(new Polygon(xx, yy, 3));
        }

        return (Polygon[]) polys.toArray(new Polygon[0]);
    }

    @Nullable
    @Override
    public HeadIcon getOverheadIcon() {
        return null;
    }

    @Nullable
    @Override
    public SkullIcon getSkullIcon() {
        return null;
    }

    @Override
    public RSUsername getRsName() {
        return null;
    }

    @Override
    public int getPlayerId() {
        return 0;
    }

    @Override
    public RSPlayerComposition getPlayerComposition() {
        return null;
    }

    @Override
    public int getCombatLevel() {
        return 0;
    }

    @Override
    public int getTotalLevel() {
        return 0;
    }

    @Override
    public int getTeam() {
        return 0;
    }

    @Override
    public boolean isFriendsChatMember() {
        return false;
    }

    @Override
    public boolean isClanMember() {
        return false;
    }

    @Override
    public boolean isFriend() {
        return false;
    }

    @Override
    public boolean isFriended() {
        return false;
    }

    @Override
    public int getRsOverheadIcon() {
        return 0;
    }

    @Override
    public int getRsSkullIcon() {
        return 0;
    }

    @Override
    public int getRSSkillLevel() {
        return 0;
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    @Nullable
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Actor getInteracting() {
        return null;
    }

    @Override
    public int getHealthRatio() {
        return 0;
    }

    @Override
    public int getHealthScale() {
        return 0;
    }


    @Override
    public WorldPoint getWorldLocation() {
        return WorldPoint.fromLocal(Client.instance,
                this.getPathX()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
                this.getPathY()[0] * Perspective.LOCAL_TILE_SIZE + Perspective.LOCAL_TILE_SIZE / 2,
                Client.instance.getPlane());
    }

    @Override
    public LocalPoint getLocalLocation() {
        return new LocalPoint(this.x, this.y);
    }

    @Override
    public void setIdleRotateLeft(int animationID) {

    }

    @Override
    public void setIdleRotateRight(int animationID) {

    }

    @Override
    public void setWalkAnimation(int animationID) {

    }

    @Override
    public void setWalkRotateLeft(int animationID) {

    }

    @Override
    public void setWalkRotateRight(int animationID) {

    }

    @Override
    public void setWalkRotate180(int animationID) {

    }

    @Override
    public void setRunAnimation(int animationID) {

    }

    @Override
    public Polygon getCanvasTilePoly() {
        return null;
    }

    @Nullable
    @Override
    public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return null;
    }

    @Override
    public Point getCanvasImageLocation(BufferedImage image, int zOffset) {
        return null;
    }

    @Override
    public Point getCanvasSpriteLocation(SpritePixels sprite, int zOffset) {
        return null;
    }

    @Override
    public Point getMinimapLocation() {
        return Perspective.localToMinimap(Client.instance, getLocalLocation());
    }

    @Override
    public Shape getConvexHull() {
        RSModel model = getModel();
        if (model == null) {
            return null;
        }

        int tileHeight = Perspective.getTileHeight(Client.instance, new LocalPoint(getX(), getY()), Client.instance.getPlane());

        return model.getConvexHull(getX(), getY(), getOrientation(), tileHeight);
    }

    @Override
    public WorldArea getWorldArea() {
        return new WorldArea(getWorldLocation(), 1, 1);
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public int getRSInteracting() {
        return 0;
    }

    @Override
    public String getOverheadText() {
        return null;
    }

    @Override
    public void setOverheadText(String overheadText) {

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int[] getPathX() {
        return this.smallX;
    }

    @Override
    public int[] getPathY() {
        return this.smallY;
    }

    @Override
    public int getAnimation() {
        return 0;
    }

    @Override
    public void setAnimation(int animation) {

    }

    @Override
    public int getAnimationFrame() {
        return 0;
    }

    @Override
    public int getActionFrame() {
        return 0;
    }

    @Override
    public void setAnimationFrame(int frame) {

    }

    @Override
    public void setActionFrame(int frame) {

    }

    @Override
    public int getActionFrameCycle() {
        return 0;
    }

    @Override
    public int getGraphic() {
        return 0;
    }

    @Override
    public void setGraphic(int id) {

    }

    @Override
    public int getSpotAnimFrame() {
        return 0;
    }

    @Override
    public void setSpotAnimFrame(int id) {

    }

    @Override
    public int getSpotAnimationFrameCycle() {
        return 0;
    }

    @Override
    public int getIdlePoseAnimation() {
        return 0;
    }

    @Override
    public void setIdlePoseAnimation(int animation) {

    }

    @Override
    public int getPoseAnimation() {
        return 0;
    }

    @Override
    public void setPoseAnimation(int animation) {

    }

    @Override
    public int getPoseFrame() {
        return 0;
    }

    @Override
    public void setPoseFrame(int frame) {

    }

    @Override
    public int getPoseFrameCycle() {
        return 0;
    }

    @Override
    public int getLogicalHeight() {
        return 0;
    }

    @Override
    public int getOrientation() {
        return 0;
    }

    @Override
    public int getCurrentOrientation() {
        return 0;
    }

    @Override
    public RSIterableNodeDeque getHealthBars() {
        return null;
    }

    @Override
    public int[] getHitsplatValues() {
        return new int[0];
    }

    @Override
    public int[] getHitsplatTypes() {
        return new int[0];
    }

    @Override
    public int[] getHitsplatCycles() {
        return new int[0];
    }

    @Override
    public int getIdleRotateLeft() {
        return 0;
    }

    @Override
    public int getIdleRotateRight() {
        return 0;
    }

    @Override
    public int getWalkAnimation() {
        return 0;
    }

    @Override
    public int getWalkRotate180() {
        return 0;
    }

    @Override
    public int getWalkRotateLeft() {
        return 0;
    }

    @Override
    public int getWalkRotateRight() {
        return 0;
    }

    @Override
    public int getRunAnimation() {
        return 0;
    }

    @Override
    public void setDead(boolean dead) {

    }

    @Override
    public int getPathLength() {
        return 0;
    }

    @Override
    public int getOverheadCycle() {
        return 0;
    }

    @Override
    public void setOverheadCycle(int cycle) {

    }

    @Override
    public int getPoseAnimationFrame() {
        return 0;
    }

    @Override
    public void setPoseAnimationFrame(int frame) {

    }
}
