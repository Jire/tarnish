package com.osroyale.profile;

import com.osroyale.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import static net.runelite.client.RuneLite.PROFILES_DIR;

public class Profile {

    private static final int DEFAULT_GENDER = 0;
    private static final int[] DEFAULT_EQUIPMENT = {0, 0, 2241, 1911, 274, 0, 282, 292, 259, 289, 298, 266};
    private static final int[] DEFAULT_RECOLOURS = {0, 3, 2, 0, 0};

    public static Cache modelCache = new Cache(260);

    private final String username;
    private final String password;
    private final int gender;
    private final int[] equipment;
    private final int[] recolours;

    public Profile() {
        this.username = "";
        this.password = "";
        this.gender = DEFAULT_GENDER;
        this.recolours = DEFAULT_RECOLOURS;
        this.equipment = DEFAULT_EQUIPMENT;
    }

    public Profile(String username, String password, int gender, int[] equipment, int[] recolours) {
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.equipment = equipment;
        this.recolours = recolours;
    }

    /**
     * Draws the profile head at the specified coordinates.
     *
     * @param x The x-coordinate to draw the profile head.
     * @param y The y-coordinate to draw the profile head.
     */
    public void drawProfileHead(int x, int y) {
        if (!username.isEmpty()) {
            final String imagePath = PROFILES_DIR + "/" + username + ".png";
            final Sprite sprite = new Sprite(imagePath, 50, 50);
            if (sprite.raster != null) {
                if(sprite.raster.length != 0) {
                    sprite.drawSprite(x, y);
                }
            }
        }
    }

    public Sprite convertModelToSprite(Model model) {
        int spriteWidth = 50;
        int spriteHeight = 50;
        Sprite sprite = new Sprite(spriteWidth, spriteHeight);
        int origX = Rasterizer3D.originViewX;
        int origY = Rasterizer3D.originViewY;
        int[] scanline = Rasterizer3D.scanOffsets;
        int[] raster = Rasterizer2D.pixels;
        int w = Rasterizer2D.width;
        int h = Rasterizer2D.height;
        int x1 = Rasterizer2D.leftX;
        int x2 = Rasterizer2D.bottomX;
        int y1 = Rasterizer2D.topY;
        int y2 = Rasterizer2D.bottomY;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer3D.world = false;
        Rasterizer2D.initDrawingArea(sprite.raster, spriteWidth, spriteHeight);
        Rasterizer3D.useViewport();

        int rotationX = 40;
        int rotationY = 2020;
        int zoom = 1600;
        int sine = Model.SINE[rotationY] * zoom >> 16;
        int cosine = Model.COSINE[rotationY] * zoom >> 16;
        Rasterizer3D.renderOnGpu = true;
        model.renderModel(rotationX, 0, rotationY, 0, sine, cosine);
        Rasterizer3D.renderOnGpu = false;
        Rasterizer2D.initDrawingArea(raster, w, h);
        Rasterizer2D.setDrawingArea(x1, y1, x2, y2);
        Rasterizer3D.originViewX = origX;
        Rasterizer3D.originViewY = origY;
        Rasterizer3D.scanOffsets = scanline;
        Rasterizer3D.aBoolean1464 = true;
        Rasterizer3D.world = true;
        return sprite;
    }

    public Model getModel() {
        Model model = getHeadModel();
        model.light(64, 850, -30, -50, -30, true);
        return model;
    }

    private Model getHeadModel() {
        Model[] headModels = new Model[equipment.length];

        int headModelsOffset = 0;
        for (int appearanceId : equipment) {
            if (appearanceId >= 256 && appearanceId < 512) {
                final IdentityKit kit = IdentityKit.kits[appearanceId - 256];
                kit.headModel(); // prefetch
                Model subModel = kit.headModel();
                headModels[headModelsOffset++] = subModel;
            }
            if (appearanceId >= 512) {
                ItemDefinition itemDef = ItemDefinition.lookup(appearanceId - 512);
                itemDef.getModelForGender(gender); // prefetch
                Model subModel = itemDef.getModelForGender(gender);
                if (itemDef.recolorFrom != null && subModel != null) {
                    for (int l = 0; l < itemDef.recolorFrom.length; l++) {
                        if(itemDef.recolorTo != null) {
                            subModel.recolor(itemDef.recolorFrom[l], itemDef.recolorTo[l]);
                        }
                    }
                }
                if (subModel != null) {
                    headModels[headModelsOffset++] = subModel;
                }
            }
        }

        Model headModel = new Model(headModelsOffset, headModels);
        for (int i = 0; i < 5; i++) {
            if (recolours[i] != 0) {
                headModel.recolor(Client.PLAYER_BODY_RECOLOURS[i][0], Client.PLAYER_BODY_RECOLOURS[i][recolours[i]]);
                if (i == 1) {
                    headModel.recolor(Client.anIntArray1204[0], Client.anIntArray1204[recolours[i]]);
                }
            }
        }
        return headModel;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getGender() {
        return gender;
    }

    public int[] getEquipment() {
        return equipment;
    }

    public int[] getRecolours() {
        return recolours;
    }

    public boolean emptySlot() {
        return username.isEmpty();
    }

}
