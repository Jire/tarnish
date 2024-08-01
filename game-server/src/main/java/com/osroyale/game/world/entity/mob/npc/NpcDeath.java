package com.osroyale.game.world.entity.mob.npc;

import com.osroyale.content.ActivityLog;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.impl.warriorguild.WarriorGuildUtility;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.collectionlog.CollectionLog;
import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.OnKillEvent;
import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoUtility;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.MobDeath;
import com.osroyale.game.world.entity.mob.npc.drop.NpcDropManager;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.util.Utility;

import java.util.Arrays;

/**
 * Handles a npc dying. (Used mostly for custom statements).
 *
 * @author Daniel
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class NpcDeath extends MobDeath<Npc> {
    private Runnable runnable;

    /** Creates a new {@link MobDeath}. */
    public NpcDeath(Npc mob) {
        this(mob, () -> {
        });
    }

    public NpcDeath(Npc mob, Runnable runnable) {
        super(mob, mob.getDeathTime());
        this.runnable = runnable;
    }

    @Override
    public void preDeath(Mob killer) {
        if (mob.definition == null) {
            return;
        }

        mob.animate(new Animation(mob.definition.getDeathAnimation(), UpdatePriority.VERY_HIGH));
    }

    @Override
    public void death() {
        if (mob.owner != null || mob.definition == null || mob.definition.getRespawnTime() == -1) {
            mob.unregister();
            return;
        }

        mob.setVisible(false);
        World.schedule(mob.definition.getRespawnTime(), () -> {
            mob.move(mob.spawnPosition);
            mob.npcAssistant.respawn();
            mob.unvenom();
            mob.unpoison();
        });
    }

    @Override
    public void postDeath(Mob killer) {
        if (killer == null)
            return;

		/* Npc name. */
        String name = mob.getName().toUpperCase().replace(" ", "_");

        runnable.run();

        switch (killer.getType()) {
            case PLAYER:
                Player playerKiller = killer.getPlayer();

                CollectionLog.onNpcKill(playerKiller, mob.id);

				/* Npc drop. */
                NpcDropManager.drop(playerKiller, mob);

				/* The slayer kill activator. */
                playerKiller.slayer.activate(mob, 1);

				/* The followers. */
                if (playerKiller.followers.contains(mob.getNpc())) {
                    playerKiller.followers.remove(mob);
                }

                /* Activity. */
                EventDispatcher.execute(playerKiller, new OnKillEvent(mob));

                /* Warrior Guild */
                if (Arrays.stream(WarriorGuildUtility.CYCLOPS).anyMatch(cyclop -> cyclop == mob.id) && (Utility.random(20) == 0)) {
                    Item defender = new Item(WarriorGuildUtility.getDefender(playerKiller), 1);
                    GroundItem.create(playerKiller, defender, mob.getPosition());
                    return;
                }

				/* Switch statement. */
                switch (name) {
                    case "SKOTIZO":
                        SkotizoUtility.defeated(mob, playerKiller);
                        playerKiller.activityLogger.add(ActivityLog.SKOTIZO);
                        return;

                    case "ZULRAH":
                        playerKiller.activityLogger.add(ActivityLog.ZULRAH);
                        AchievementHandler.activate(playerKiller, AchievementKey.ZULRAH, 1);
                        return;

                    case "CERBERUS":
                        playerKiller.activityLogger.add(ActivityLog.CERBERUS);
                        AchievementHandler.activate(playerKiller, AchievementKey.CERBERUS, 1);
                        return;

                    case "GENERAL_GRAARDOR":
                        playerKiller.activityLogger.add(ActivityLog.GENERAL_GRAARDOR);
                        AchievementHandler.activate(playerKiller, AchievementKey.GRAARDOR, 1);
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.GENERAL_GRAARDOR, playerKiller.getName()));
                        return;

                    case "LAVA_DRAGON":
                        playerKiller.activityLogger.add(ActivityLog.LAVA_DRAGON);
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DRAGON, playerKiller.getName()));
                        return;

                    case "KRAKEN":
                        playerKiller.activityLogger.add(ActivityLog.KRAKEN);
                        return;

                    case "COMMANDER_ZILYANA":
                        playerKiller.activityLogger.add(ActivityLog.COMMANDER_ZILYANA);
                        AchievementHandler.activate(playerKiller, AchievementKey.ZILYANA, 1);
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.COMMANDER_ZILYANA, playerKiller.getName()));
                        return;

                    case "KREE'ARRA":
                        playerKiller.activityLogger.add(ActivityLog.KREE_ARRA);
                        AchievementHandler.activate(playerKiller, AchievementKey.KREE, 1);
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.KREEARRA, playerKiller.getName()));
                        return;

                    case "K'RIL TSUTSAROTH":
                        playerKiller.activityLogger.add(ActivityLog.KRIL_TSUTSAROTH);
                        AchievementHandler.activate(playerKiller, AchievementKey.KRIL, 1);
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.KRIL_TSUTSAROTH, playerKiller.getName()));
                        return;

                    case "CORPOREAL_BEAST":
                        playerKiller.activityLogger.add(ActivityLog.CORPOREAL_BEAST);
                        return;

                    case "LIZARD_SHAMAN":
                        playerKiller.activityLogger.add(ActivityLog.LIZARD_SHAMAN);
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.LIZARD_SHAMAN, playerKiller.getName()));
                        return;

                    case "MITHRIL_DRAGON":
                        playerKiller.activityLogger.add(ActivityLog.MITHRIL_DRAGON);
                        return;

                    case "DARK_BEAST":
                        playerKiller.activityLogger.add(ActivityLog.DARK_BEAST);
                        return;

                    case "ANGRY_BARBARIAN_SPIRIT":
                        GroundItem.create(playerKiller, new Item(2404));
                        return;

                    case "HILL_GIANT":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.HILL_GIANT, playerKiller.getName()));
                        return;

                    case "BLACK_DEMON":
                    case "BLACK_DEMONS":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DEMON, playerKiller.getName()));
                        return;

                    case "GREATER_DEMON":
                    case "GREATER_DEMONS":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.GREATER_DEMON, playerKiller.getName()));
                        return;

                    case "ROCK_CRAB":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.ROCK_CRAB, playerKiller.getName()));
                        return;

                    case "SAND_CRAB":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SAND_CRAB, playerKiller.getName()));
                        return;

                    case "BLUE_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLUE_DRAGON, playerKiller.getName()));
                        return;

                    case "RED_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.RED_DRAGON, playerKiller.getName()));
                        return;

                    case "GREEN_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.GREEN_DRAGON, playerKiller.getName()));
                        return;

                    case "BLACK_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DRAGON, playerKiller.getName()));
                        return;

                    case "KING_BLACK_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DRAGON, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.KING_BLACK_DRAGON);
                        AchievementHandler.activate(playerKiller, AchievementKey.KING_BLACK_DRAGON);
                        return;

                    case "CHAOS_ELEMENTAL":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CHAOS_ELEMENTAL, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.CHAOS_ELEMENTAL);
                        return;

                    case "CHAOS_FANATIC":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CHAOS_FANATIC, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.CHAOS_FANATIC);
                        return;

                    case "CRAZY_ARCHAEOLOGIST":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CRAZY_ARCHAEOLOGIST, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.CRAZY_ARCHAEOLOGIST);
                        return;

                    case "CALLISTO":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CALLISTO, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.CALLISTO);
                        return;

                    case "SCORPIA":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SCORPIA, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.SCORPIA);
                        return;

                    case "VET'ION":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.VETION, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.VETION);
                        return;

                    case "VENANTIS":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.VENNANTIS, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.VENANTIS);
                        return;

                    case "SKELETAL_WYVERN":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SKELETAL_WYVERN, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.SKELETAL_WYVERN);
                        return;

                    case "ABYSSAL_DEMON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.ABYSSAL_DEMON, playerKiller.getName()));
                        playerKiller.activityLogger.add(ActivityLog.ABYSSAL_DEMON);
                        return;

                    case "GHOST":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.GHOST, playerKiller.getName()));
                        return;

                    case "SKELETON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SKELETON, playerKiller.getName()));
                        return;

                    case "BLACK_KNIGHT":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_KNIGHT, playerKiller.getName()));
                        return;

                    case "BABY_BLUE_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BABY_BLUE_DRAGON, playerKiller.getName()));
                        return;

                    case "BAT":
                    case "GIANT_BAT":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BAT, playerKiller.getName()));
                        return;

                    case "CHAOS_DWARF":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CHAOS_DWARF, playerKiller.getName()));
                        return;

                    case "MAGIC_AXE":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.MAGIC_AXE, playerKiller.getName()));
                        return;

                    case "CAVE_CRAWLER":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CAVE_CRAWLER, playerKiller.getName()));
                        return;

                    case "CRAWLING_HAND":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CRAWLING_HAND, playerKiller.getName()));
                        return;

                    case "BANSHEE":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BANSHEE, playerKiller.getName()));
                        return;

                    case "ICE_FIEND":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.ICE_FIEND, playerKiller.getName()));
                        return;

                    case "HELLHOUND":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.HELLHOUND, playerKiller.getName()));
                        return;

                    case "CAVE_HORROR":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CAVE_HORROR, playerKiller.getName()));
                        return;

                    case "STEEL_DRAGON":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.STEEL_DRAGON, playerKiller.getName()));
                        return;

                    case "PYRE_FIEND":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.PYRE_FIEND, playerKiller.getName()));
                        return;

                    case "FIRE_GIANT":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.FIRE_GIANT, playerKiller.getName()));
                        return;

                    case "BASILISK":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BASILISK, playerKiller.getName()));
                        return;

                    case "COCKATRICE":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.COCKATRICE, playerKiller.getName()));
                        return;

                    case "DUST_DEVIL":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.DUST_DEVIL, playerKiller.getName()));
                        return;

                    case "SPIRITUAL_RANGER":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SPIRITUAL_RANGER, playerKiller.getName()));
                        return;

                    case "SPIRITUAL_WARRIOR":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SPIRITUAL_WARRIOR, playerKiller.getName()));
                        return;

                    case "BLOODVELD":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLOODVELD, playerKiller.getName()));
                        return;

                    case "SPIRITUAL_MAGE":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SPIRITUAL_MAGE, playerKiller.getName()));
                        return;

                    case "NECHRYAEL":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.NECHRYAEL, playerKiller.getName()));
                        return;

                    case "SMOKE_DEVIL":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SMOKE_DEVIL, playerKiller.getName()));
                        return;
                    case "REVENANT_IMP":
                    case "REVENANT_GOBLIN":
                    case "REVENANT_PYREFIEND":
                    case "REVENANT_HOBGOBLIN":
                    case "REVENANT_CYCLOPS":
                    case "REVENANT_HELLHOUND":
                    case "REVENANT_DEMON":
                    case "REVENANT_ORK":
                    case "REVENANT_DARK_BEAST":
                    case "REVENANT_DRAGON":
                        AchievementHandler.activate(playerKiller, AchievementKey.REVS);
                        return;
                    case "DEMONIC_GORILLA":
                        playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.DEMONIC_GORILLA, playerKiller.getName()));
                        return;
                }
                break;
            case NPC:
//                Npc npcKiller = killer.getNpc();
                break;
        }
    }
}
