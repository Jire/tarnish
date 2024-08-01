package plugin.click.button;

import com.osroyale.Config;
import com.osroyale.content.activity.Activity;
import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.lms.lobby.LMSLobby;
import com.osroyale.content.skill.impl.magic.spell.impl.BonesToBananas;
import com.osroyale.content.skill.impl.magic.spell.impl.BonesToPeaches;
import com.osroyale.content.skill.impl.magic.spell.impl.MagicTeleports;
import com.osroyale.content.skill.impl.magic.spell.impl.Vengeance;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.event.impl.ItemContainerContextMenuEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.magic.MagicRune;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.net.packet.out.SendMessage;

public class MagicButtonPlugin extends PluginContext {

    private boolean canTele(Player player) {
        int wilderness = 20;
        if (player.getCombat().isUnderAttackByPlayer() && !PlayerRight.isDeveloper(player)) {
            player.message("You can't do this right now!");
            return true;
        }
        if (player.isTeleblocked()  && !PlayerRight.isDeveloper(player)) {
            player.message("You are currently under the affects of a teleblock spell and can not teleport!");
            return true;
        }
        if (player.wilderness > wilderness  && !PlayerRight.isDeveloper(player)) {
            player.send(new SendMessage("You can't teleport past " + wilderness + " wilderness!"));
            return true;
        }
        if(player.isGambleLocked()) {
            player.message("You cannot teleport while gambling!");
            return true;
        }
        player.interfaceManager.close(false);

        if (Activity.evaluate(player, it -> !it.canTeleport(player))) {
            return true;
        }

        if(LMSLobby.lobbyMembers.contains(player) || LMSGame.isActivePlayer(player)) {
            player.message("You cannot teleport while in the lobby or in a game!");
            return true;
        }

        if (player.locking.locked(PacketType.TELEPORT)) {
            return true;
        }
        return false;
    }
    @Override
    protected boolean onClick(Player player, int button) {

        // Lunar teleports
        if(MagicTeleports.forButtonId(button) != null) {

            MagicTeleports teleport = MagicTeleports.forButtonId(button);
            if (player.spellbook != teleport.getSpellbook()) {
                return false;
            }
            if (player.skills.getLevel(Skill.MAGIC) < teleport.getLevelRequirement()) {
                player.send(new SendMessage("You need a Magic level of " + teleport.getLevelRequirement() + " to do this!"));
                return false;
            }
            if(teleport.getName() == "Fishing" && player.skills.getLevel(Skill.FISHING) < 85) {
                player.send(new SendMessage("You need a Fishing level of 85 to do this!"));
                return false;
            }
            if (teleport.getRunes() != null) {
                if (!PlayerRight.isDeveloper(player) && !player.isBot && !MagicRune.hasRunes(player, teleport.getRunes())) {
                    player.send(new SendMessage("You do not have the required runes to do this!"));
                    return false;
                }
            }

            if(canTele(player)) {
                return true;
            }


            if(teleport.getWildernessLevel() > -1) {
                player.dialogueFactory.sendStatement("Are you sure you want teleport?", "This is level " + teleport.getWildernessLevel() + " wilderness!").sendOption("Yes!", () -> {
                    player.dialogueFactory.onAction(() -> {
                        player.locking.lock();
                        player.animate(new Animation(1816), true);
                        player.graphic(new Graphic(747, 0, 110), true);
                        World.schedule(4, () -> {
                            player.move(teleport.getPosition());
                            player.skills.addExperience(Skill.MAGIC, teleport.getExperience());
                            player.animate(Animation.RESET, true);
                            player.graphic(Graphic.RESET, true);
                            player.locking.unlock();
                        });
                    });
                }, "Nevermind", () -> player.dialogueFactory.onAction(() -> player.dialogueFactory.clear())).execute();
                return true;
            }

            player.locking.lock();
            player.animate(teleport.getAnimation(), true);
            player.graphic(teleport.getGraphics(), true);
            World.schedule(4, () -> {
                player.move(teleport.getPosition());
                player.skills.addExperience(Skill.MAGIC, teleport.getExperience());
                player.animate(Animation.RESET, true);
                player.graphic(Graphic.RESET, true);
                player.locking.unlock();
            });
            return true;
        }

        switch (button) {
                /*
                 * Misc spells
                 */
            case 19210:
            case 21741:
            case 30000:
            case -25436:
                if (player.getCombat().isUnderAttackByPlayer() && !PlayerRight.isDeveloper(player)) {
                    player.message("You can not teleport whilst in combat!");
                    return true;
                }
                Teleportation.teleport(player, Config.DEFAULT_POSITION);
                return true;
            case 1159:
                player.spellCasting.cast(new BonesToBananas(), null);
                return true;
            case 15877:
                player.spellCasting.cast(new BonesToPeaches(), null);
                return true;
            case 30306:
                player.spellCasting.cast(new Vengeance(), null);
                return true;
            case -25426:
                player.spellCasting.openBoltEnchant();
                return true;
        }
        return false;
    }

    @Override
    protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        if (event.getInterfaceId() == 42752) {
            player.spellCasting.enchant(event.getRemoveId());
            return true;
        }
        return false;
    }
}
