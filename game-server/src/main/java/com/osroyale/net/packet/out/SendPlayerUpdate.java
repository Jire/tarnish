package com.osroyale.net.packet.out;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.Viewport;
import com.osroyale.game.world.entity.mob.player.ForceMovement;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.appearance.Gender;
import com.osroyale.game.world.entity.mob.player.relations.ChatMessage;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.equipment.Equipment;
import com.osroyale.game.world.items.containers.equipment.EquipmentType;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.codec.AccessType;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketBuilder;
import com.osroyale.net.packet.PacketType;
import com.osroyale.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

public final class SendPlayerUpdate extends OutgoingPacket {

    private static final Logger logger = LogManager.getLogger();

    public SendPlayerUpdate() {
        super(81, PacketType.VAR_SHORT);
    }

    @Override
    public boolean encode(Player player) {
        if (player.regionChange) {
            player.send(new SendMapRegion());
        }

        final PacketBuilder blockBuf = PacketBuilder.alloc();
        try {
            builder.initializeAccess(AccessType.BIT);

            updateMovement(player, builder);

            if (player.isUpdateRequired()) {
                updatePlayer(blockBuf, player, player, UpdateState.UPDATE_SELF);
            }

            final Collection<Player> localPlayers =  World.getRegions().getLocalPlayers(player);

            builder.writeBits(8, player.viewport.getPlayersInViewport().size());

            for (Iterator<Player> itr = player.viewport.getPlayersInViewport().iterator(); itr.hasNext();) {
                Player other = itr.next();

                if (player.viewport.shouldRemove(other)) {
                    builder.writeBit(true);
                    builder.writeBits(2, 3);
                    itr.remove();
                } else {
                    updateMovement(other, builder);

                    if (other.isUpdateRequired()) {
                        updatePlayer(blockBuf, player, other, UpdateState.UPDATE_LOCAL);
                    }
                }

            }

            int added = 0;

            for (Player localPlayer : localPlayers) {

                if (player.viewport.getPlayersInViewport().size() >= Viewport.CAPACITY || added >= Viewport.ADD_THRESHOLD) {
                    break;
                }

                if (player.viewport.add(localPlayer)) {
                    added++;
                    addNewPlayer(builder, player, localPlayer);
                    updatePlayer(blockBuf, player, localPlayer, UpdateState.ADD_LOCAL);
                }

            }

            if (blockBuf.content().readableBytes() > 0) {
                builder.writeBits(11, 2047);
                builder.initializeAccess(AccessType.BYTE);
                builder.writeBuffer(blockBuf.content());
            } else {
                builder.initializeAccess(AccessType.BYTE);
            }
        } catch (Exception ex) {
            logger.error(String.format("error updating player=%s", player), ex);
        } finally {
            blockBuf.release();
        }
        return true;
    }

    private static void addNewPlayer(PacketBuilder packetBuf, Player player, Player other) {
        packetBuf.writeBits(11, other.getIndex())
        .writeBit(true) // isUpdateRequired
        .writeBit(true) // discardWalkingQueue
        .writeBits(5, other.getY() - player.getY())
        .writeBits(5, other.getX() - player.getX());
    }

    private static void updatePlayer(PacketBuilder blockBuf, Player player, Player other, UpdateState state) {
        if (!other.isUpdateRequired()) {
            return;
        }

        int mask = 0;

        final EnumSet<UpdateFlag> updateFlags = other.updateFlags;
        for (UpdateFlag flag : UpdateFlag.playerOrder) {
            if (updateFlags.contains(flag) && flag.canApply(player, other, state)) {
                mask |= flag.playerMask;
            }
        }

        if (mask >= 0x100) {
            mask |= 0x40;
            blockBuf.writeByte(mask & 0xFF);
            blockBuf.writeByte(mask >> 8);
        } else {
            blockBuf.writeByte(mask);
        }

        // TODO replace the checks.
       /* for (UpdateFlag flag : UpdateFlag.playerOrder) {
            if (UpdateFlag.containsPlayer(mask, flag)) {

            }
        }*/

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.FORCE_MOVEMENT)) {
            appendForceMovementMask(player, other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.GRAPHICS)) {
            appendGraphicMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.ANIMATION)) {
            appendAnimationMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.FORCED_CHAT)) {
            appendForceChatMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.CHAT)) {
            appendChatMask(blockBuf, other);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.INTERACT)) {
            appendFaceEntityMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.APPEARANCE)) {
            appendAppearanceMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.FACE_COORDINATE)) {
            appendFaceCoordinteMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.FIRST_HIT)) {
            appendHitMask(other, blockBuf);
        }

        if (UpdateFlag.containsPlayer(mask, UpdateFlag.SECOND_HIT)) {
            appendSecondHitMask(other, blockBuf);
        }

    }

    private static void appendForceMovementMask(Player player, Player other, PacketBuilder blockBuf) {
        final ForceMovement fm = other.getForceMovement();

        final Position lastPosition = player.lastPosition;
        final Position otherPosition = other.getPosition();
        final int startX = otherPosition.getLocalX(lastPosition);
        final int startY = otherPosition.getLocalY(lastPosition);

        final Position end = fm.getEnd();
        final int endX = end.getX();
        final int endY = end.getY();

        blockBuf.writeByte(startX, ByteModification.SUB)
        .writeByte(startY, ByteModification.SUB)
        .writeByte(startX + endX, ByteModification.SUB)
        .writeByte(startY + endY, ByteModification.SUB)
        .writeShort(fm.getSpeed(), ByteModification.ADD, ByteOrder.LE)
        .writeShort(fm.getReverseSpeed(), ByteModification.ADD)
        .writeByte(fm.getDirection(), ByteModification.SUB);
    }

    private static void appendForceChatMask(Player other, PacketBuilder blockBuf) {
        blockBuf.writeString(other.forceChat);
    }

    private static void appendFaceEntityMask(Player other, PacketBuilder blockBuf) {
        Mob mob = other.interactingWith;
        if (mob != null) {
            int index = mob.getIndex();
            if (mob.isPlayer()) {
                index += -32768;
            }
            blockBuf.writeShort(index, ByteOrder.LE);
        } else {
            blockBuf.writeShort(65535, ByteOrder.LE);
        }
    }

    private static void appendFaceCoordinteMask(Player other, PacketBuilder blockBuf) {
        Position loc = other.facePosition;

        if (loc == null) {
            Position currentPos = other.getPosition();
            Direction currentDir = other.movement.lastDirection;
            blockBuf.writeShort(((currentPos.getX() + currentDir.getDirectionX()) << 1), ByteModification.ADD, ByteOrder.LE)
            .writeShort(((currentPos.getY() + currentDir.getDirectionY()) << 1) + 1, ByteOrder.LE);
        } else {
            blockBuf.writeShort((loc.getX() << 1) + 1, ByteModification.ADD, ByteOrder.LE)
            .writeShort((loc.getY() << 1) + 1, ByteOrder.LE);
        }
    }

    private static void appendAnimationMask(Player other, PacketBuilder maskBuf) {
        Animation anim = other.getAnimation().orElse(Animation.RESET);
        maskBuf.writeShort(anim.getId(), ByteOrder.LE);
        maskBuf.writeByte(anim.getDelay(), ByteModification.NEG);
    }

    private static void appendGraphicMask(Player other, PacketBuilder blockBuf) {
        Graphic graphic = other.getGraphic().orElse(Graphic.RESET);
        blockBuf.writeShort(graphic.getId(), ByteOrder.LE)
        .writeInt(graphic.getDelay() | graphic.getHeight());
    }

    private static void appendChatMask(PacketBuilder blockBuf, Player other) {
        final ChatMessage message = other.getChatMessage().orElse(ChatMessage.create("Cabbage"));

        final byte[] encoded = message.getEncoded();
        blockBuf.writeShort(((message.getColor().getCode() & 0xFF) << 8) | (message.getEffect().getCode() & 0xFF), ByteOrder.LE)
        .writeByte(other.right.getCrown())
        .writeByte(encoded.length, ByteModification.NEG)
        .writeBytesReverse(encoded);
    }

    private static void appendAppearanceMask(Player other, PacketBuilder blockBuf) {
        final PacketBuilder tempBuf = PacketBuilder.alloc();
        final var overrides = other.overrides;
        try {
            tempBuf.writeByte(other.appearance.getGender().ordinal())
                    .writeByte(other.headIcon)
                    .writeByte(other.skulling.getHeadIconType().getCode())
                    .writeByte(other.valueIcon);

            if (other.id != -1) {
                tempBuf.writeShort(-1);
                tempBuf.writeShort(other.id);
            } else {
                Item helm = other.equipment.get(Equipment.HEAD_SLOT);
                if (overrides.hasOverride(Equipment.HEAD_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.HELM_SLOT).getId());
                } else if (helm != null && helm.getId() > 1) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.HELM_SLOT).getId());
                } else {
                    tempBuf.writeByte(0);
                }

                if (overrides.hasOverride(Equipment.CAPE_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.CAPE_SLOT).getId());
                } else if (other.equipment.get(Equipment.CAPE_SLOT) != null) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.CAPE_SLOT).getId());
                } else {
                    tempBuf.writeByte(0);
                }

                if (overrides.hasOverride(Equipment.AMULET_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.AMULET_SLOT).getId());
                } else if (other.equipment.get(Equipment.AMULET_SLOT) != null) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.AMULET_SLOT).getId());
                } else {
                    tempBuf.writeByte(0);
                }

                if (overrides.hasOverride(Equipment.WEAPON_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.WEAPON_SLOT).getId());
                } else if (other.equipment.get(Equipment.WEAPON_SLOT) != null) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.WEAPON_SLOT).getId());
                } else {
                    tempBuf.writeByte(0);
                }

                Item torso = other.equipment.get(Equipment.CHEST_SLOT);
                if (overrides.hasOverride(Equipment.CHEST_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.CHEST_SLOT).getId());
                } else if (torso != null && torso.getId() > 1) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.CHEST_SLOT).getId());
                } else {
                    tempBuf.writeShort(0x100 + other.appearance.getTorso());
                }

                if (overrides.hasOverride(Equipment.SHIELD_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.SHIELD_SLOT).getId());
                } else if (other.equipment.get(Equipment.SHIELD_SLOT) != null) {
                    if (overrides.hasOverride(Equipment.WEAPON_SLOT) && overrides.get(Equipment.WEAPON_SLOT).isTwoHanded()) {
                        tempBuf.writeByte(0);
                    } else {
                        tempBuf.writeShort(0x200 + other.equipment.get(Equipment.SHIELD_SLOT).getId());
                    }
                } else {
                    tempBuf.writeByte(0);
                }

                if (torso != null && torso.getId() > 1 && torso.getEquipmentType().equals(EquipmentType.BODY) ||
                    overrides.hasOverride(Equipment.CHEST_SLOT) && overrides.get(Equipment.CHEST_SLOT).getEquipmentType().equals(EquipmentType.BODY)) {
                    tempBuf.writeByte(0);
                } else {
                    tempBuf.writeShort(0x100 + other.appearance.getArms());
                }

                if (overrides.hasOverride(Equipment.LEGS_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.LEGS_SLOT).getId());
                } else if (other.equipment.get(Equipment.LEGS_SLOT) != null) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.LEGS_SLOT).getId());
                } else {
                    tempBuf.writeShort(0x100 + other.appearance.getLegs());
                }

                boolean head = true;
                boolean beard = true;

                if (helm != null && helm.getId() > 1) {
                    EquipmentType type = helm.getEquipmentType();
                    head = type.equals(EquipmentType.MASK) || type.equals(EquipmentType.HAT);
                    beard = type.equals(EquipmentType.FACE) || type.equals(EquipmentType.HAT);
                }

                if (overrides.hasOverride(Equipment.HEAD_SLOT)) {
                    final var override = overrides.get(Equipment.HEAD_SLOT).getEquipmentType();
                    head = override.equals(EquipmentType.MASK) || override.equals(EquipmentType.HAT);
                    beard = override.equals(EquipmentType.FACE) || override.equals(EquipmentType.HAT);
                }

                if (head) {
                    tempBuf.writeShort(0x100 + other.appearance.getHead());
                } else {
                    tempBuf.writeByte(0);
                }

                if (overrides.hasOverride(Equipment.HANDS_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.HANDS_SLOT).getId());
                } else if (other.equipment.get(Equipment.HANDS_SLOT) != null) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.HANDS_SLOT).getId());
                } else {
                    tempBuf.writeShort(0x100 + other.appearance.getHands());
                }

                if (overrides.hasOverride(Equipment.FEET_SLOT)) {
                    tempBuf.writeShort(0x200 + overrides.get(Equipment.FEET_SLOT).getId());
                } else if (other.equipment.get(Equipment.FEET_SLOT) != null) {
                    tempBuf.writeShort(0x200 + other.equipment.get(Equipment.FEET_SLOT).getId());
                } else {
                    tempBuf.writeShort(0x100 + other.appearance.getFeet());
                }

                if (other.appearance.getGender().equals(Gender.MALE)) {
                    if (beard) {
                        tempBuf.writeShort(0x100 + other.appearance.getBeard());
                    } else {
                        tempBuf.writeByte(0);
                    }
                } else {
                    tempBuf.writeByte(0);
                }
            }

            tempBuf
                    .writeByte(other.appearance.getHairColor())
                    .writeByte(other.appearance.getTorsoColor())
                    .writeByte(other.appearance.getLegsColor())
                    .writeByte(other.appearance.getFeetColor())
                    .writeByte(other.appearance.getSkinColor())
                    .writeShort(other.mobAnimation.getStand())
                    .writeShort(other.mobAnimation.getTurn())
                    .writeShort(other.mobAnimation.getWalk())
                    .writeShort(other.mobAnimation.getTurn180())
                    .writeShort(other.mobAnimation.getTurn90CW())
                    .writeShort(other.mobAnimation.getTurn90CCW())
                    .writeShort(other.mobAnimation.getRun())
                    .writeLong(Utility.hash(other.getName()))
                    .writeString(other.playerTitle.getTitle())
                    .writeInt(other.playerTitle.getColor())
                    .writeString(other.clanChannel == null ? "" : other.clanChannel.getOwner())
                    .writeString(other.clanTag)
                    .writeString(other.clanTagColor)
                    .writeLong(Double.doubleToLongBits(other.skills.getCombatLevel()))
                    .writeByte(other.right.getCrown())
                    .writeShort(0);
            blockBuf.writeByte(tempBuf.content().writerIndex(), ByteModification.NEG);
            blockBuf.writeBytes(tempBuf.content());
        } finally {
            tempBuf.release();
        }
    }

    private static void appendHitMask(final Player player, final PacketBuilder blockBuf) {
        Hit hit = player.firstHit;

        boolean multipleHits = hit.getMultipleHits() != null;
        blockBuf.writeByte(multipleHits ? 1 : 0);


        int max = player.getMaximumHealth() >= 500 ? 200 : 100;
        int health = player.getCurrentHealth() * max / player.getMaximumHealth();
        if (health > max) health = max;

        if(multipleHits) {
            blockBuf.writeByte(hit.getMultipleHits().length);

            for(int index = 0; index < hit.getMultipleHits().length; index++) {
                Hit currentHit = hit.getMultipleHits()[index];

                int id = currentHit.getHitsplat().getId();

                if (currentHit.getHitsplat() == Hitsplat.NORMAL && currentHit.getDamage() > 0) {
                    id++;
                }

                blockBuf.writeByte(currentHit.getDamage());
                blockBuf.writeByte(id, ByteModification.ADD);
                blockBuf.writeByte(currentHit.getHitIcon().getId());
            }

        } else {

            int id = hit.getHitsplat().getId();

            if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
                id++;
            }

            blockBuf.writeByte(hit.getDamage());
            blockBuf.writeByte(id, ByteModification.ADD);
            blockBuf.writeByte(hit.getHitIcon().getId());
        }

        blockBuf.writeByte(health);
        blockBuf.writeByte(max, ByteModification.NEG);
    }

    private static void appendSecondHitMask(final Player player, final PacketBuilder blockBuf) {
        Hit hit = player.secondHit;

        boolean multipleHits = hit.getMultipleHits() != null;
        blockBuf.writeByte(multipleHits ? 1 : 0);

        int max = player.getMaximumHealth() >= 500 ? 200 : 100;
        int health = player.getCurrentHealth() * max / player.getMaximumHealth();
        if (health > max) health = max;

        if(multipleHits) {
            blockBuf.writeByte(hit.getMultipleHits().length);

            for(int index = 0; index < hit.getMultipleHits().length; index++) {
                Hit currentHit = hit.getMultipleHits()[index];

                int id = currentHit.getHitsplat().getId();

                if (currentHit.getHitsplat() == Hitsplat.NORMAL && currentHit.getDamage() > 0) {
                    id++;
                }

                blockBuf.writeByte(currentHit.getDamage());
                blockBuf.writeByte(id, ByteModification.ADD);
                blockBuf.writeByte(currentHit.getHitIcon().getId());
            }
        } else {
            int id = hit.getHitsplat().getId();

            if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
                id++;
            }

            blockBuf.writeByte(hit.getDamage());
            blockBuf.writeByte(id, ByteModification.SUB);
            blockBuf.writeByte(hit.getHitIcon().getId());
        }

        blockBuf.writeByte(health);
        blockBuf.writeByte(max, ByteModification.NEG);
    }

    private static void updateMovement(Player player, PacketBuilder packetBuf) {
        final boolean teleported = player.positionChange || player.teleportRegion;
        final boolean updateRequired = player.isUpdateRequired();
        if (teleported) {
            packetBuf.writeBit(true)
            .writeBits(2, 3)
            .writeBits(2, player.getHeight())
            .writeBits(1, player.regionChange ? 0 : 1)
            .writeBit(updateRequired)
            .writeBits(7, player.getPosition().getLocalY(player.lastPosition))
            .writeBits(7, player.getPosition().getLocalX(player.lastPosition));
        } else if (player.movement.getRunningDirection() != -1) {
            packetBuf.writeBit(true)
            .writeBits(2, 2)
            .writeBits(3, player.movement.getWalkingDirection())
            .writeBits(3, player.movement.getRunningDirection())
            .writeBit(player.isUpdateRequired());
        } else if (player.movement.getWalkingDirection() != -1) {
            packetBuf.writeBit(true)
            .writeBits(2, 1)
            .writeBits(3, player.movement.getWalkingDirection())
            .writeBit(player.isUpdateRequired());
        } else {
            if (updateRequired) {
                packetBuf.writeBit(true)
                .writeBits(2, 0);
            } else {
                packetBuf.writeBit(false);
            }
        }
    }

    public enum UpdateState {
        UPDATE_SELF,
        UPDATE_LOCAL,
        ADD_LOCAL
    }

}
