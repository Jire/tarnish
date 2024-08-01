package com.osroyale;

public class AreaDefinition extends Cacheable {

    private static Cache recentUse = new Cache(64);
    private static Buffer buffer;
    private static int[] indices;

    public int id;
    public int spriteId;

    private AreaDefinition() {
        id = -1;
        spriteId = -1;
    }

    public static void nullLoader() {
        recentUse = null;
        buffer = null;
        indices = null;
    }

    public static void clear() {
        recentUse.unlinkAll();
    }

    public static void unpack(StreamLoader streamLoader) {
        buffer = new Buffer(streamLoader.getFile("areas.dat"));
        Buffer indexBuffer = new Buffer(streamLoader.getFile("areas.idx"));
        int size = indexBuffer.readUnsignedShort();
        indices = new int[size];
        int offset = 0;
        for (int i = 0; i < size; i++) {
            indices[i] = offset;
            offset += indexBuffer.readUnsignedShort();
        }
    }

    public static AreaDefinition get(int id) {
        if (id < 0 || id >= indices.length) return null;

        AreaDefinition area = (AreaDefinition) recentUse.get(id);
        if (area != null) {
            return area;
        }

        area = new AreaDefinition();
        area.id = id;

        buffer.position = indices[id];
        area.decode(buffer);

        recentUse.put(area, id);
        return area;
    }

    private void decode(Buffer buffer) {
        do {
            int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            }

            if (opcode == 1) {
                spriteId = buffer.readBigSmart2();
            } else if (opcode == 2) {
                buffer.readBigSmart2();
            } else if (opcode == 3) {
                buffer.readStringCp1252NullTerminated();
            } else if (opcode == 4) {
                buffer.read24Int();
            } else if (opcode == 5) {
                buffer.read24Int();
            } else if (opcode == 6) {
                buffer.readUnsignedByte();
            } else if (opcode == 7) {
                buffer.readUnsignedByte();
            } else if (opcode == 8) {
                buffer.readUnsignedByte();
            } else if (opcode >= 10 && opcode <= 14) {
                buffer.readStringCp1252NullTerminated();
            } else if (opcode == 15) {
                int var3 = buffer.readUnsignedByte();

                int var4;
                for (var4 = 0; var4 < var3 * 2; ++var4)
                {
                    buffer.readShort();
                }

                buffer.readInt();
                var4 = buffer.readUnsignedByte();

                int var5;
                for (var5 = 0; var5 < var4; ++var5)
                {
                    buffer.readInt();
                }

                for (var5 = 0; var5 < var3; ++var5)
                {
                    buffer.readSignedByte();
                }
            } else if (opcode == 16) {

            } else if (opcode == 17) {
                buffer.readStringCp1252NullTerminated();
            } else if (opcode == 18) {
                buffer.readBigSmart2();
            } else if (opcode == 19) {
                buffer.readUnsignedShort();
            } else if (opcode == 21) {
                buffer.readInt();
            } else if (opcode == 22) {
                buffer.readInt();
            } else if (opcode == 23) {
                buffer.readUnsignedByte();
                buffer.readUnsignedByte();
                buffer.readUnsignedByte();
            } else if (opcode == 24) {
                buffer.readShort();
                buffer.readShort();
            } else if (opcode == 25) {
                buffer.readBigSmart2();
            } else if (opcode == 28) {
                buffer.readUnsignedByte();
            } else if (opcode == 29) {
                buffer.readUnsignedByte(); // skip 1
            } else if (opcode == 30) {
                buffer.readUnsignedByte(); // skip 1
            }
        } while (true);
    }
}
