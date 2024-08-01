package com.osroyale.content.activity.impl.recipefordisaster;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendEntityHintArrow;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

/**
 * Handles the recipe for disaster activity.
 *
 * @author Daniel
 * @author Michael
 */
public class RecipeForDisaster extends Activity {

    /** The player instance of this activity. */
    private final Player player;

    /** The boss monster instance of this activity. */
    private Npc monster;

    /** The current wave of this activity. */
    private RecipeForDisasterData wave = RecipeForDisasterData.AGRITH_NA_NA;

    /** Constructs a new {@code SequencedMinigame} object. */
    private RecipeForDisaster(Player player, int instance) {
        super(10, instance);
        this.player = player;
    }

    /** Handles creating a recipe for disaster activity. */
    public static RecipeForDisaster create(Player player) {
        RecipeForDisaster minigame = new RecipeForDisaster(player, player.playerAssistant.instance());
        ActivityPanel.update(player, -1, "Recipe For Disaster", "Good Luck, " + player.getName() + "!", "Wave starting in 6 seconds");
        player.move(new Position(1899, 5356, 2));
        GroundItem.create(player, new Item(1580), player.getPosition());
        player.message("Ice gloves have been dropped for you to fight Flambleed!");
        player.gameRecord.start();
        player.playerAssistant.restore();
        minigame.add(player);
        minigame.resetCooldown();
        GroundItem.create(player, new Item(1580), player.getPosition());
        return minigame;
    }

    @Override
    public void update() {
        if (wave == null) {
            ActivityPanel.update(player, 100, "Recipe For Disaster", new Item(7462), "</col>Wave: <col=FF5500>" + RecipeForDisasterData.values().length + "/" + RecipeForDisasterData.values().length, "</col>Boss: <col=FF5500>None!", "</col>Time: <col=FF5500>" + Utility.getTime(System.currentTimeMillis() - player.gameRecord.time));
            return;
        }
        int progress = (int) Utility.getPercentageAmount(wave.ordinal(), RecipeForDisasterData.values().length);
        int display = 7453 + wave.gloves;
        String npc = monster == null ? "" : monster.getName();
        ActivityPanel.update(player, progress, "Recipe For Disaster", new Item(display), "</col>Wave: <col=FF5500>" + wave.ordinal() + "/" + RecipeForDisasterData.values().length, "</col>Boss: <col=FF5500>" + npc, "</col>Time: <col=FF5500>" + Utility.getTime(System.currentTimeMillis() - player.gameRecord.time));
    }

    @Override
    protected void start() {
        if (wave == null) {
            finishCooldown();
            return;
        }
        if (player.locking.locked()) {
            return;
        }
        Position spawn = new Position(1899, 5356, 2);
        Position[] boundaries = Utility.getInnerBoundaries(spawn, 5, 5);

        Npc npc = new Npc(wave.npc, RandomUtils.random(boundaries));
        npc.owner = player;
        add(monster = npc);

        player.send(new SendEntityHintArrow(npc));
        monster.getCombat().attack(player);
        pause();
    }

    @Override
    public void finish() {
        cleanup();
        int reward;
        if (wave == null) {
            long time = player.gameRecord.end(ActivityType.RECIPE_FOR_DISASTER);
            reward = RecipeForDisasterData.CULINAROMANCER.gloves;
            player.send(new SendMessage("You have completed the Recipe For Disaster activity. Final time: @red@" + Utility.getTime(time) + "</col>."));
        } else {
            reward = wave.gloves;
            player.send(new SendMessage("RIP"));
        }
        if (reward > player.glovesTier) {
            player.glovesTier = reward;
        }
        player.move(new Position(3080, 3498, 0));
        player.dialogueFactory.sendNpcChat(6526, "You have been rewarded for your efforts.", "Check my store to see your available gloves.").execute();
        remove(player);
    }

    @Override
    public void cleanup() {
        if (monster != null) {
            player.send(new SendEntityHintArrow(monster, true));
            remove(monster);
        }
        ActivityPanel.clear(player);
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inRFD(player)) {
            cleanup();
            remove(player);
        }
    }

    @Override
    public boolean canUsePrayer(Player player) {
        player.message("You can not use prayers here!");
        return false;
    }

    @Override
    public void onLogout(Player player) {
        finish();
    }


    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.PURCHASE;
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isPlayer()) {
            cleanup();
            remove(mob);
            mob.move(new Position(3080, 3498, 0));
            mob.animate(Animation.RESET, true);
            mob.graphic(Graphic.RESET, true);
            return;
        }
        if (mob.isNpc()) {
            player.send(new SendEntityHintArrow(mob, true));
            World.schedule(new NpcDeath(mob.getNpc()));
            wave = wave.getNext();
            resetCooldown();
        }
    }

    @Override
    public ActivityType getType() {
        return ActivityType.RECIPE_FOR_DISASTER;
    }
}
