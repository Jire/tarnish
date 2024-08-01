package com.osroyale;

import static com.osroyale.Animation.animations;

public final class Graphic {

    public static Graphic[] graphics;
    public static Cache aMRUNodes_415 = new Cache(30);
    private final int[] originalModelColours;
    private final int[] modifiedModelColours;
    public int modelId;
    public Animation animationSequence;
    public int resizeXY;
    public int resizeZ;
    public int rotation;
    public int modelBrightness;
    public int modelShadow;
    private final int graphicID;
    public int animationId;

    private Graphic(final int id) {
        this.graphicID = id;

        animationId = -1;
        originalModelColours = new int[6];
        modifiedModelColours = new int[6];
        resizeXY = 128;
        resizeZ = 128;
    }

    static void unpackConfig(final StreamLoader streamLoader) {
        final Buffer buffer = new Buffer(streamLoader.getFile("spotanim.dat"));

        final int graphicCount = buffer.readUnsignedShort();
        System.out.println("graphicCount=" + graphicCount);

        Graphic[] graphics = Graphic.graphics;
        if (graphics == null) {
            graphics = Graphic.graphics = new Graphic[graphicCount + 1];
        }

        for (int i = 0; i < graphicCount + 1; i++) {
            final int id = buffer.readUnsignedShort();
            if (id == 65535) break;

            Graphic graphic = graphics[id];
            if (graphic == null) {
                graphic = graphics[id] = new Graphic(id);
            }

            final int dataSize = buffer.readUnsignedShort();
            final byte[] data = buffer.readBytes(dataSize);
            final Buffer dataBuffer = new Buffer(data);

            graphic.decode(dataBuffer);
        }
    }

    public void decode(final Buffer buffer) {
        int last = -1;
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                modelId = buffer.readUnsignedShort();
            } else if (opcode == 2) {
                final int animationId = this.animationId = buffer.readUnsignedShort();
                if (animationId != 65535) {
                    animationSequence = animations[animationId];
                }
            } else if (opcode == 4) {
                resizeXY = buffer.readUnsignedShort();
            } else if (opcode == 5) {
                resizeZ = buffer.readUnsignedShort();
            } else if (opcode == 6) {
                rotation = buffer.readUnsignedShort();
            } else if (opcode == 7) {
                modelBrightness = buffer.readUnsignedByte();
            } else if (opcode == 8) {
                modelShadow = buffer.readUnsignedByte();
            } else if (opcode == 40) {
                int length = buffer.readUnsignedByte();
                for (int i = 0; i < length; i++) {
                    originalModelColours[i] = buffer.readUnsignedShort();
                    modifiedModelColours[i] = buffer.readUnsignedShort();
                }
            } else if (opcode == 41) {
                int length = buffer.readUnsignedByte();
                for (int i = 0; i < length; i++) {
                    buffer.readUnsignedShort();
                    buffer.readUnsignedShort();
                }
            } else {
                throw new RuntimeException("Error graphics opcode: " + opcode + ", last=" + last);
            }
            last = opcode;
        }
    }

    public Model getModel() {
        Model model = (Model) aMRUNodes_415.get(graphicID);
        if (model != null) {
            return model;
        }
        model = Model.getModel(modelId);
        if (model == null) {
            return null;
        }
        for (int i = 0; i < 6; i++) {
            if (originalModelColours[0] != 0) {
                model.recolor(originalModelColours[i], modifiedModelColours[i]);
            }
        }
        aMRUNodes_415.put(model, graphicID);
        return model;
    }
}
