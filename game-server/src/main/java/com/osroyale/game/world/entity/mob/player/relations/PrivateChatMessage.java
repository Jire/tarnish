package com.osroyale.game.world.entity.mob.player.relations;

public class PrivateChatMessage {

    private final String decompressed;

    private final byte[] compressed;

    public PrivateChatMessage(String decompressed, byte[] compressed) {
        this.decompressed = decompressed;
        this.compressed = compressed;
    }

    public String getDecompressed() {
        return decompressed;
    }

    public byte[] getCompressed() {
        return compressed;
    }

}
