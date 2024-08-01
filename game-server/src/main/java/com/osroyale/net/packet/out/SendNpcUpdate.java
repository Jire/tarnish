package com.osroyale.net.packet.out;

import com.osroyale.Config;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.Viewport;
import com.osroyale.game.world.entity.mob.movement.Movement;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.codec.AccessType;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketBuilder;
import com.osroyale.net.packet.PacketType;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * The packet that's responsible for updating npcs.
 *
 * @author nshusa
 */
public final class SendNpcUpdate extends OutgoingPacket {

    public SendNpcUpdate() {
        super(65, PacketType.VAR_SHORT);
    }

    @Override
    public boolean encode(Player player) {
        PacketBuilder maskBuf = PacketBuilder.alloc();
        try {
            builder.initializeAccess(AccessType.BIT);
            builder.writeBits(8, player.viewport.getNpcsInViewport().size());

            for (Iterator<Npc> itr = player.viewport.getNpcsInViewport().iterator(); itr.hasNext(); ) {

                Npc npc = itr.next();

                if (player.viewport.shouldRemove(npc)) {
                    if (npc.atomicPlayerCount.decrementAndGet() < 0) {
                        npc.atomicPlayerCount.set(0);
                    }
                    itr.remove();
                    builder.writeBits(1, 1);
                    builder.writeBits(2, 3);
                } else {
                    updateMovement(builder, npc);

                    if (npc.isUpdateRequired()) {
                        updateNpc(maskBuf, npc);
                    }
                }

            }

            int npcsAdded = 0;

            for (Npc localNpc : World.getRegions().getLocalNpcs(player)) {

                if (player.viewport.getNpcsInViewport().size() >= Viewport.CAPACITY || npcsAdded == Viewport.ADD_THRESHOLD) {
                    break;
                }

                if (player.viewport.add(localNpc)) {
                    npcsAdded++;
                    addNewNpc(builder, player, localNpc);
                    updateNpc(maskBuf, localNpc);

                    if (localNpc.atomicPlayerCount.incrementAndGet() < 0) {
                        localNpc.atomicPlayerCount.set(0);
                    }
                }

            }

            if (maskBuf.content().readableBytes() > 0) {
                builder.writeBits(16, 65535);
                builder.initializeAccess(AccessType.BYTE);
                builder.writeBytes(maskBuf.content());
            } else {
                builder.initializeAccess(AccessType.BYTE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            maskBuf.release();
        }
        return true;
    }

    private static void addNewNpc(PacketBuilder packet, Player player, Npc npc) {
        packet.writeBits(16, npc.getIndex()); // 16 bits needed since server supports 32000 NPCs
        packet.writeBits(5, npc.getPosition().getY() - player.getPosition().getY());
        packet.writeBits(5, npc.getPosition().getX() - player.getPosition().getX());
        packet.writeBit(false); // discard walking queue
        packet.writeBits(Config.NPC_BITS, npc.id);
        packet.writeBit(npc.isUpdateRequired());
    }

    private static void updateMovement(PacketBuilder packet, Npc npc) {
        final boolean updateRequired = npc.isUpdateRequired();

        final Movement movement = npc.movement;
        final int runDirection = movement.getRunningDirection();
        final int walkDirection = movement.getWalkingDirection();

        if (runDirection != -1) {
            packet.writeBit(true)
                    .writeBits(2, 2)
                    .writeBits(3, walkDirection)
                    .writeBits(3, runDirection)
                    .writeBit(updateRequired);
        } else if (walkDirection != -1) {
            packet.writeBit(true)
                    .writeBits(2, 1)
                    .writeBits(3, walkDirection)
                    .writeBit(updateRequired);
        } else {
            packet.writeBit(updateRequired);
            if (updateRequired) {
                packet.writeBits(2, 0);
            }
        }
    }

    private static void updateNpc(PacketBuilder maskBuf, Npc npc) {
        if (!npc.isUpdateRequired()) {
            return;
        }

        int mask = 0;

        final EnumSet<UpdateFlag> updateFlags = npc.updateFlags;
        for (UpdateFlag flag : UpdateFlag.npcOrder) {
            if (updateFlags.contains(flag) && flag.canApply(npc)) {
                mask |= flag.npcMask;
            }
        }

        maskBuf.writeByte(mask);

        // TODO replace the checks.
       /* for (UpdateFlag flag : UpdateFlag.npcOrder) {
            if (UpdateFlag.containsNpc(mask, flag)) {

            }
        }*/
        
        if (UpdateFlag.containsNpc(mask, UpdateFlag.ANIMATION)) {
            appendAnimationMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.GRAPHICS)) {
            appendGfxMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.INTERACT)) {
            appendFaceEntityMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.FORCED_CHAT)) {
            appendForceChatMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.FIRST_HIT)) {
            appendFirstHitMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.SECOND_HIT)) {
            appendSecondHitMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.TRANSFORM)) {
            appendTransformationMask(npc, maskBuf);
        }

        if (UpdateFlag.containsNpc(mask, UpdateFlag.FACE_COORDINATE)) {
            appendFaceCoordinateMask(npc, maskBuf);
        }

    }

    private static void appendAnimationMask(Npc npc, PacketBuilder maskBuf) {
        Animation anim = npc.getAnimation().orElse(Animation.RESET);
        maskBuf.writeShort(anim.getId(), ByteOrder.LE)
                .writeByte(anim.getDelay());
    }

    private static void appendGfxMask(Npc npc, PacketBuilder maskBuf) {
        Graphic gfx = npc.getGraphic().orElse(Graphic.RESET);
        maskBuf.writeShort(gfx.getId())
                .writeInt(gfx.getDelay() | gfx.getHeight());
    }

    private static void appendFaceEntityMask(Npc npc, PacketBuilder maskBuf) {
        Mob mob = npc.interactingWith;
        int index = 65535;
        if (mob != null) {
            index = mob.getIndex();
            if (mob.isPlayer()) {
                index += 32768;
            }
            maskBuf.writeShort(index);
        } else {
            maskBuf.writeShort(index);
        }
    }

    private static void appendForceChatMask(Npc npc, PacketBuilder maskBuf) {
        maskBuf.writeString(npc.forceChat);
    }

    private static void appendFaceCoordinateMask(Npc npc, PacketBuilder maskBuf) {
        Position loc = npc.facePosition;
        if (loc == null) {
            Position currentPos = npc.getPosition();
            Direction currentDir = npc.movement.lastDirection;
            maskBuf.writeShort(((currentPos.getX() + currentDir.getDirectionX()) << 1) + 1, ByteOrder.LE)
                    .writeShort(((currentPos.getY() + currentDir.getDirectionY()) << 1) + 1, ByteOrder.LE);
        } else {
            maskBuf.writeShort((loc.getX() << 1) + 1, ByteOrder.LE)
                    .writeShort((loc.getY() << 1) + 1, ByteOrder.LE);
        }
    }

    private static void appendTransformationMask(Npc npc, PacketBuilder maskBuf) {
        maskBuf.writeShort(npc.id, ByteModification.ADD, ByteOrder.LE);
    }

    private static void appendFirstHitMask(final Npc npc, final PacketBuilder updateBlock) {
        Hit hit = npc.firstHit;

        boolean multipleHits = hit.getMultipleHits() != null;
        updateBlock.writeByte(multipleHits ? 1 : 0);

        int max = npc.getMaximumHealth() >= 500 ? 200 : 100;
        int health = npc.getCurrentHealth() * max / npc.getMaximumHealth();
        if (health > max) health = max;

        if(multipleHits) {
            System.out.println("total 1 = " + hit.getMultipleHits().length);
            updateBlock.writeByte(hit.getMultipleHits().length);

            for(int index = 0; index < hit.getMultipleHits().length; index++) {
                Hit currentHit = hit.getMultipleHits()[index];

                int id = currentHit.getHitsplat().getId();

                if (currentHit.getHitsplat() == Hitsplat.NORMAL && currentHit.getDamage() > 0) {
                    id++;
                }

                updateBlock.writeByte(currentHit.getDamage());
                updateBlock.writeByte(id);
                updateBlock.writeByte(currentHit.getHitIcon().getId());
            }

        } else {

            int id = hit.getHitsplat().getId();

            if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
                id++;
            }

            updateBlock.writeByte(hit.getDamage());
            updateBlock.writeByte(id);
            updateBlock.writeByte(hit.getHitIcon().getId());
        }

        updateBlock.writeByte(health);
        updateBlock.writeByte(max);

        /*int id = hit.getHitsplat().getId();
        int max = npc.getMaximumHealth() >= 500 ? 200 : 100;
        int health = max * npc.getCurrentHealth() / npc.getMaximumHealth();
        if (health > max) health = max;

        if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
            id++;
        }

        updateBlock.writeByte(hit.getDamage());
        updateBlock.writeByte(id, ByteModification.ADD);
        updateBlock.writeByte(hit.getHitIcon().getId());
        updateBlock.writeByte(health);
        updateBlock.writeByte(max, ByteModification.NEG);*/
    }

    private static void appendSecondHitMask(final Npc npc, final PacketBuilder updateBlock) {
        Hit hit = npc.secondHit;

        Hit[] multipleHits = hit.getMultipleHits();
        boolean isMultipleHits = multipleHits != null;
        Hit[] multipleHitsArray = isMultipleHits ? Arrays.copyOf(multipleHits, multipleHits.length) : null;
        updateBlock.writeByte(isMultipleHits ? 1 : 0);

        int max = npc.getMaximumHealth() >= 500 ? 200 : 100;
        int health = npc.getCurrentHealth() * max / npc.getMaximumHealth();
        if (health > max) health = max;

        if(isMultipleHits) {
            updateBlock.writeByte(multipleHitsArray.length);
            for (Hit currentHit : multipleHitsArray) {
                int id = currentHit.getHitsplat().getId();

                if (currentHit.getHitsplat() == Hitsplat.NORMAL
                        && currentHit.getDamage() > 0) {
                    id++;
                }

                updateBlock.writeByte(currentHit.getDamage());
                updateBlock.writeByte(id);
                updateBlock.writeByte(currentHit.getHitIcon().getId());
            }
        } else {

            int id = hit.getHitsplat().getId();

            if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
                id++;
            }

            updateBlock.writeByte(hit.getDamage());
            updateBlock.writeByte(id);
            updateBlock.writeByte(hit.getHitIcon().getId());
        }

        updateBlock.writeByte(health);
        updateBlock.writeByte(max);
        /*int id = hit.getHitsplat().getId();
        int max = npc.getMaximumHealth() >= 500 ? 200 : 100;
        int health = npc.getCurrentHealth() * max / npc.getMaximumHealth();
        if (health > max) health = max;

        if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
            id++;
        }

        updateBlock.writeByte(hit.getDamage());
        updateBlock.writeByte(id, ByteModification.SUB);
        updateBlock.writeByte(hit.getHitIcon().getId());
        updateBlock.writeByte(health);
        updateBlock.writeByte(max, ByteModification.NEG);*/
    }

}