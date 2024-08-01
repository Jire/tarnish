package com.osroyale.content.activity.impl.barrows;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.ActivityDeathType;
import com.osroyale.content.activity.ActivityType;
import com.osroyale.content.activity.panel.ActivityPanel;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.event.impl.NpcInteractionEvent;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.npc.NpcDeath;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendEntityHintArrow;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

public class Barrows extends Activity {

    private Player player;

    private Npc brotherNpc;

    private Barrows(Player player) {
        super(10, Mob.DEFAULT_INSTANCE);
        this.player = player;
    }

    public static Barrows create(Player player) {
        Barrows minigame = new Barrows(player);
        minigame.add(player);
        return minigame;
    }

    private void summon(BrotherData brother) {
        if (player.barrowKills[brother.ordinal()]) {
            player.dialogueFactory.sendPlayerChat("I have already killed this brother.").execute();
            return;
        }
        if (brotherNpc != null) {
            player.dialogueFactory.sendPlayerChat("Maybe I should finish killing the other one first.").execute();
            return;
        }
        if (player.hiddenBrother == brother && player.barrowsKillCount != 5) {
            player.dialogueFactory.sendPlayerChat("I should return when I've killed the others.").execute();
            return;
        } else if (player.hiddenBrother == brother && player.barrowsKillCount == 5) {
            player.dialogueFactory.sendOption("Enter the tunnel.", () -> {
                player.move(new Position(3551, 9691, 0));
            }, "No, I'm not ready yet.", () -> {
                player.dialogueFactory.clear();
            }).execute();
            return;
        }
        brotherNpc = new Npc(brother.getNpcId(), player.getPosition());
        add(brotherNpc);
        brotherNpc.speak("How dare you disturb my rest!");
        brotherNpc.getCombat().attack(player);
        brotherNpc.owner = player;
        player.send(new SendEntityHintArrow(brotherNpc));
    }

    private void move(Position position) {
        if (brotherNpc != null && !brotherNpc.isDead()) {
            remove(brotherNpc);
            brotherNpc = null;
        }
        player.move(position);
    }

    @Override
    public void onDeath(Mob mob) {
        if (mob.isPlayer() && mob.equals(player)) {
            finish();
            return;
        }
        if (mob.isNpc()) {
            BrotherData bro = BrotherData.getBarrowsBrother(mob.getNpc());
            if (bro != null) {
                brotherNpc = null;
                player.barrowsKillCount += 1;
                player.barrowKills[bro.ordinal()] = true;
                if (player.barrowsKillCount == 1) {
                    player.hiddenBrother = BarrowsUtility.getHiddenBrother(player);
                }
                World.schedule(new NpcDeath(mob.getNpc()));
            }
        }
    }

    @Override
    protected void start() {

    }

    @Override
    public void finish() {
        cleanup();
        remove(player);
        ActivityPanel.clear(player);
    }

    @Override
    public void cleanup() {
        if (brotherNpc != null) {
            remove(brotherNpc);
        }
    }

    @Override
    public void onLogout(Player player) {
        finish();
    }

    @Override
    public void onRegionChange(Player player) {
        if (!Area.inBarrows(player)) {
            finish();
        }
    }

    private void reset() {
        player.barrowsKillCount = 0;
        player.barrowKills = new boolean[BrotherData.values().length];
        player.hiddenBrother = null;
        update();
    }

    @Override
    public void update() {
        int killed = player.barrowsKillCount;
        int total = BrotherData.values().length;
        int percentage = (int) Utility.getPercentageAmount(killed, total);
        String ahrim = player.barrowKills[BrotherData.AHRIM.ordinal()] ? "@red@Ahrim the Blighted" : "@gre@Ahrim the Blighted";
        String dharok = player.barrowKills[BrotherData.DHAROK.ordinal()] ? "@red@Dharok the Wretched" : "@gre@Dharok the Wretched";
        String guthan = player.barrowKills[BrotherData.GUTHAN.ordinal()] ? "@red@Guthan the Infested" : "@gre@Guthan the Infested";
        String karil = player.barrowKills[BrotherData.KARIL.ordinal()] ? "@red@Karil the Tainted" : "@gre@Karil the Tainted";
        String torag = player.barrowKills[BrotherData.TORAG.ordinal()] ? "@red@Torag the Corrupted" : "@gre@Torag the Corrupted";
        String verac = player.barrowKills[BrotherData.VERAC.ordinal()] ? "@red@Verac the Defiled" : "@gre@Verac the Defiled";
        ActivityPanel.update(player, percentage, "Barrows", new Item(19629, 0), "Killed: <col=FF5500>" + killed + "</col> - Remaining: <col=FF5500>" + (total - killed) + "</col>", ahrim, dharok, guthan, karil, torag, verac);
    }

    @Override
    public boolean canTeleport(Player player) {
        return true;
    }

    @Override
    protected boolean clickNpc(Player player, NpcInteractionEvent event) {
        if (event.getOpcode() == 0 && event.getNpc().id == 1671) {
            DialogueFactory factory = player.dialogueFactory;

            factory.sendNpcChat(1671, "Hello #name,", "would you like me to reset your barrows?");
            factory.sendOption("Yes please", () -> {
                    reset();
                    factory.sendNpcChat(1671, "I have reset your barrows!");
            }, "No thanks", factory::execute);
            factory.execute();
            return true;
        }
        return false;
    }

    @Override
    protected boolean clickObject(Player player, ObjectInteractionEvent event) {
        int id = event.getObject().getId();
        switch (id) {
            case 20973:
                if (player.barrowsKillCount == 6) {
                    if (player.inventory.getFreeSlots() < 5) {
                        player.dialogueFactory.sendPlayerChat("I should free up some inventory slots first.").execute();
                        return true;
                    }
                    reset();
                    BarrowsUtility.generateRewards(player);
                    player.send(new SendMessage("You have completed the barrows minigame, well done!"));
                    finish();
                } else if (player.barrowsKillCount == 5) {
                    if (brotherNpc == null) {
                        brotherNpc = new Npc(player.hiddenBrother.getNpcId(), player.getPosition());
                        add(brotherNpc);
                        brotherNpc.speak("How dare you disturb my slumber!");
                        brotherNpc.getCombat().attack(player);
                        brotherNpc.owner = player;
                        player.send(new SendEntityHintArrow(brotherNpc));
                    }
                    return true;
                }
                break;
            case 20667:
                move(BrotherData.AHRIM.getHillPosition());
                return true;
            case 20770:
                summon(BrotherData.AHRIM);
                return true;
            case 20672:
                move(BrotherData.VERAC.getHillPosition());
                return true;
            case 20772:
                summon(BrotherData.VERAC);
                return true;
            case 20668:
                move(BrotherData.DHAROK.getHillPosition());
                return true;
            case 20720:
                summon(BrotherData.DHAROK);
                return true;
            case 20671:
                move(BrotherData.TORAG.getHillPosition());
                return true;
            case 20721:
                summon(BrotherData.TORAG);
                return true;
            case 20669:
                move(BrotherData.GUTHAN.getHillPosition());
                return true;
            case 20722:
                summon(BrotherData.GUTHAN);
                return true;
            case 20670:
                move(BrotherData.KARIL.getHillPosition());
                return true;
            case 20771:
                summon(BrotherData.KARIL);
                return true;
        }
        return false;
    }

    @Override
    public ActivityDeathType deathType() {
        return ActivityDeathType.NORMAL;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.BARROWS;
    }
}
