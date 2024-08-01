package com.osroyale.content.lms;

import com.osroyale.Config;
import com.osroyale.content.lms.crate.LMSCrate;
import com.osroyale.content.lms.fog.Fog;
import com.osroyale.content.lms.loadouts.LMSLoadout;
import com.osroyale.content.lms.loadouts.LMSLoadoutManager;
import com.osroyale.content.lms.lobby.LMSLobby;
import com.osroyale.content.lms.lobby.LMSLobbyEvent;
import com.osroyale.content.lms.safezone.LMSSafezone;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.strategy.player.special.CombatSpecial;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerOption;
import com.osroyale.game.world.entity.mob.player.persist.PlayerSerializer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Boundary;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.StringUtils;
import com.osroyale.util.Utility;

import java.util.*;

public class LMSGame {

    /**
     * All the players within the LMS game
     */
    public static List<Player> gamePlayers = new ArrayList<>();

    /**
     * Checks if there is currently a game in progress
     */
    public static boolean gameInProgress;

    /**
     * Checks if the players are allowed to attack within the game
     */
    public static boolean canAttack = false;

    /**
     * The current fod within the LMS game
     */
    private static Fog currentFog = new Fog(-1, -1, -1, -1);

    private static int lastFogCycle;

    /**
     * The current amount of game ticks within the lms game
     */
    public static int gameTicks;

    /**
     * The amount of ticks before the real game starts
     */
    public static int startingTicks;

    /**
     * The current amount of crate ticks within the lms game
     */
    public static int crateTicks;

    /**
     * The lms crate within the game
     */
    public static LMSCrate lmsCrate;

    /**
     * The current safe zone within the game
     */
    private static LMSSafezone safeZone;

    /**
     * The safezone objects within the lms game
     */
    private static List<CustomGameObject> storedObjects = new ArrayList<>();

    /**
     * The current loadout of the game
     */
    private static LMSLoadout currentGameType;

    private static String getGameTypeName() {
        return currentGameType.getClass().getSimpleName();
    }

    /**
     * All the possible spawns of the lms game
     */
    private static List<Position> possible_spawns = Arrays.asList(
            new Position(3399, 5766, 0),
            new Position(3409, 5784, 0),
            new Position(3401, 5774, 0),
            new Position(3422, 5770, 0),
            new Position(3453, 5816, 0),
            new Position(3441, 5812, 0),
            new Position(3433, 5804, 0),
            new Position(3415, 5821, 0),
            new Position(3402, 5832, 0),
            new Position(3400, 5851, 0),
            new Position(3410, 5865, 0),
            new Position(3400, 5876, 0),
            new Position(3423, 5872, 0),
            new Position(3439, 5875, 0),
            new Position(3451, 5859, 0),
            new Position(3467, 5869, 0),
            new Position(3466, 5819, 0),
            new Position(3480, 5810, 0),
            new Position(3495, 5801, 0),
            new Position(3494, 5790, 0),
            new Position(3511, 5790, 0),
            new Position(3505, 5771, 0),
            new Position(3513, 5766, 0),
            new Position(3492, 5772, 0),
            new Position(3460, 5787, 0),
            new Position(3468, 5788, 0),
            new Position(3476, 5796, 0),
            new Position(3477, 5779, 0),
            new Position(3491, 5773, 0)

    );

    /**
     * Handles checking if the player is in the LMS game area
     * @param player
     * @return
     */
    public static boolean inGameArea(Player player) {
        return Boundary.isIn(player, new Boundary(3390, 5757, 3524, 5901));
    }

    /**
     * Checks if a player is a active player within the LMS game
     * @param player
     * @return
     */
    public static boolean isActivePlayer(Player player) {
        return gamePlayers != null && gamePlayers.contains(player) && inGameArea(player);
    }

    /**
     * Checks if the player is allowed to attack the target
     * @param player
     * @param target
     * @return
     */
    public static boolean canAttack(Player player, Player target) {
        if (!inGameArea(player)) {
            return false;
        }

        if (!canAttack) {
            player.message("The game has not started yet.");
            return false;
        }

        if (target.lmsImmunity > System.currentTimeMillis()) {
            player.message(target.getUsername() + " has temporary immunity from being attacked!");
            return false;
        }

        if (target.getCombat().isUnderAttack() && !target.getCombat().isUnderAttackBy(player)) {
            if (!Area.inMulti(player) || !Area.inMulti(target)) {
                player.send(new SendMessage(target.getName() + " is currently in combat and can not be attacked."));
                return false;
            }
        }

        return true;
    }

    /**
     * Handles rolling the loot chest
     * @param player
     * @param keyId
     * @param lootCrate
     */
    public static void rollChest(Player player, int keyId, boolean lootCrate) {
        if (player == null || !player.inventory.contains(keyId) && !lootCrate)
            return;

        LMSLoadout currentLoadout = currentGameType;

        boolean upgradedKey = keyId == 20608;

        int lootTable = Utility.random(1);

        int loot = -1;

        int amount = 1;

        int maximumUnlocks = currentLoadout.getOffensiveItem().length + currentLoadout.getDefensiveItem().length + currentLoadout.getOffensiveItemUpgrades().length;

        if (player.unlockedLMSItems.size() >= maximumUnlocks) return;

        if (!upgradedKey) {
            if (lootTable == 0)
                loot = currentLoadout.getOffensiveItem()[Utility.random(currentLoadout.getOffensiveItem().length - 1)];
            else if (lootTable == 1)
                loot = currentLoadout.getDefensiveItem()[Utility.random(currentLoadout.getDefensiveItem().length - 1)];
        } else
            loot = currentLoadout.getOffensiveItemUpgrades()[Utility.random(currentLoadout.getOffensiveItemUpgrades().length - 1)];

        //Handles duplicated items
        if (player.unlockedLMSItems.contains(loot)) {
            rollChest(player, keyId, lootCrate);
            return;
        }

        player.unlockedLMSItems.add(loot);

        //Add dragon arrows when its a dark bow
        if (loot == 11235) player.unlockedLMSItems.add(11212);
        //Add dragon javelins if its a balista
        if (loot == 19481) player.unlockedLMSItems.add(19484);
        //Add opal bolts e if its a arma cbow
        //if (loot == 11785) player.unlockedLMSItems.add(8723);

        if (!lootCrate) player.inventory.remove(keyId);

        //Set the amount of certain items
        if (loot == 22636 || loot == 299) amount = 1000;

        giveChestItems(loot, amount, player);

        if (lootCrate) {
            player.animate(832);
            World.sendObjectAnimation(86, lmsCrate.getLootCrate());
            lmsCrate.destroy();
            lmsCrate = null;
        }

        //Checks if the loot was javelins or dragon arrows

        int secondary = -1;

        if (loot == 11235) secondary = 11212;
        else if(loot == 19481) secondary = 19484;
        //else if(loot == 11785) secondary = 8723;

        if(secondary != -1)
            giveChestItems(secondary, getItemAmountForItem(secondary), player);
    }

    /**
     * Handles giving the chest loot to the player by inventory or ground
     * @param loot
     * @param amount
     * @param player
     */
    private static void giveChestItems(int loot, int amount, Player player) {
        boolean space = player.inventory.getFreeSlots() > 0;

        if (space) {
            player.inventory.add(loot, amount);
        } else
            GroundItem.create(player, new Item(loot, amount));

        player.message("You have received " + amount + "x " + ItemDefinition.get(loot).getName() + ". It has been added to " + (space ? "your inventory" : "the ground"));
    }

    /**
     * Handles moving all the players to the game
     * @param players
     */
    public static void moveToGame(List<Player> players) {
        currentGameType = LMSLobby.currentGameType;
        gamePlayers.addAll(players);
        gameInProgress = true;
        canAttack = false;
        start();

        //Handles setting a different loadout for next game
        LMSLoadout loadout = null;
        do {
            loadout = LMSLoadoutManager.getLmsLoadouts().get(Utility.random(LMSLoadoutManager.getLmsLoadouts().size()));
        } while(!(loadout != LMSLobby.currentGameType));
        LMSLobby.currentGameType = loadout;
    }

    /**
     * Handles starting the lms game
     */
    private static void start() {
        gearUpPlayers();
        startingTicks = 10;
        safeZone = null;
        gameTicks = 0;
        crateTicks = 0;
        currentFog.reset();
    }

    /**
     * Handles gearing up all the players
     */
    private static void gearUpPlayers() {
        List<Position> copySpawns = new ArrayList<>();
        copySpawns.addAll(possible_spawns);

        gamePlayers.stream().filter(Objects::nonNull).forEach(player -> {
            //Turns of all the prayers
            player.prayer.reset();
            //Clears all unlocked items
            player.unlockedLMSItems = new ArrayList<>();
            //Toggles the fog on
            player.send(new SendLMSFog(true));
            //Sets the levels
            setLevels(player);
            //Gives the items & equipment
            giveItems(player, false);
            //Reset kills
            player.lmsKills = 0;
            //Pick a random spawn
            Collections.shuffle(copySpawns);
            int randomPick = Utility.random(copySpawns.size() - 1);
            player.move(copySpawns.get(randomPick));
            copySpawns.remove(randomPick);
        });
    }

    /**
     * Handles setting the correct stats of the player
     * It also makes a copy of the player his stats
     * @param player
     */
    public static void setLevels(Player player) {
        for(int index = 0; index < currentGameType.getSkills().length; index++) {
            int level = currentGameType.getSkills()[index];
            Skill skill = player.skills.get(index);

            skill.setLevel(level);
            skill.setMaxLevel(level);
            skill.setExperience(skill.getExperienceForLevel(level));
        }

        player.skills.refresh();
    }

    /**
     * Handles giving the player correct inventory and equipment
     * @param player
     * @param clear
     */
    public static void giveItems(Player player, boolean clear) {
        LMSLoadout loadout = currentGameType;

        if(clear) {
            player.equipment.clear();
            player.inventory.clear();
            player.runePouch.clear();
        }

        //Handles setting the correct spellbook
        player.spellbook = loadout.getSpellbook();
        player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());

        //Handles setting up the inventory
        player.inventory.set(loadout.getInventorySetup().getItems().clone());

        //Handles setting up the equipment
        player.equipment.clear();
        player.equipment.set(loadout.getEquipmentSetup().getItems().clone());
        player.equipment.updateAnimation();
        WeaponInterface.execute(player, player.equipment.getWeapon());

        //Handles the rune pouch
        for(int index = 0; index < loadout.getRunePouchRunes().length; index++)
            player.runePouch.runes.add(new Item(loadout.getRunePouchRunes()[index][0], loadout.getRunePouchRunes()[index][1]));

        player.inventory.refresh();
        player.equipment.refresh();
        player.equipment.updateRangedEquipment();
        player.runePouch.refresh();


        //Call the appearance update flag
        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    /**
     * Handles resetting the player that was in the game and either left by dying or other means
     * @param player
     */
    public static void reset(Player player) {
        player.send(new SendHintArrow(0));

        player.face(Direction.SOUTH);
        player.equipment.updateAnimation();
        player.animate(Animation.RESET, true);
        player.graphic(Graphic.RESET, true);

        player.inventory.clear();
        player.equipment.clear();
        player.runePouch.clear(false);

        player.inventory.set(player.inventory_copy.getItems().clone());

        player.equipment.set(player.equipment_copy.getItems().clone());
        for(int index = 0; index < player.runePouch_copy.runes.size(); index++)
            player.runePouch.runes.add(player.runePouch_copy.runes.get(index));
        player.equipment.updateAnimation();
        WeaponInterface.execute(player, player.equipment.getWeapon());


        //Handles clearing the copies of the containers
        player.inventory_copy.clear();
        player.equipment_copy.clear();
        player.runePouch_copy.clear(false);

        player.inventory.refresh();
        player.equipment.refresh();
        player.runePouch.refresh();

        //Handles reseting the spellbook
        player.spellbook = player.spellbook_copy;
        player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());

        player.prayer.reset();
        player.send(new SendPlayerOption(PlayerOption.ATTACK, false, true));

        resetLevels(player);
        player.send(new SendLMSFog(false));
        if (gamePlayers.contains(player))
            gamePlayers.remove(player);
        updateInterface();
        player.move(new Position(LMSLobby.finish.getX() + Utility.random(1), LMSLobby.finish.getY() + Utility.random(3), 0));
        PlayerSerializer.save(player);
    }

    /**
     * Handles resetting the copied stats to the player
     * @param player
     */
    private static void resetLevels(Player player) {
        for(int index = 0; index < player.skills.getSkills().length; index++) {
            Skill skill = player.skills.get(index);
            Skill copy_skill = player.skills_copy.get(index);

            skill.setLevel(copy_skill.getLevel());
            skill.setMaxLevel(copy_skill.getMaxLevel());
            skill.setExperience(skill.getExperienceForLevel(copy_skill.getLevel()));
        }

        player.skills.refresh();
    }

    /**
     * Handles setting up the safezone
     */
    public static void setupSafezone() {
        safeZone = LMSSafezone.values()[Utility.random(LMSSafezone.values().length - 1)];
        gamePlayers.stream().forEach(player -> player.message("@red@Fog approaching. Get to the safezone at "+ StringUtils.capitalize(safeZone.name().toLowerCase().replaceAll("_", " ")+"!")));

        for (int i = safeZone.boundsYSW; i < safeZone.boundsYSW + 10; i++) {
            CustomGameObject gameObject = new CustomGameObject(34905, new Position(safeZone.boundsXSW, i));
            gameObject.register();
            storedObjects.add(gameObject);
        }
        for (int i = safeZone.boundsXSW; i < safeZone.boundsXSW + 10; i++) {
            CustomGameObject gameObject = new CustomGameObject(34905, new Position(i, safeZone.boundsYSW));
            gameObject.register();
            storedObjects.add(gameObject);
        }
        for (int i = safeZone.boundsXNE - 10; i < safeZone.boundsXNE; i++) {
            CustomGameObject gameObject = new CustomGameObject(34905, new Position(i, safeZone.boundsYNE));
            gameObject.register();
            storedObjects.add(gameObject);
        }
        for (int i = safeZone.boundsYNE - 10; i <= safeZone.boundsYNE; i++) {
            CustomGameObject gameObject = new CustomGameObject(34905, new Position(safeZone.boundsXNE, i));
            gameObject.register();
            storedObjects.add(gameObject);
        }

        currentFog = new Fog(safeZone.boundsXSW - 100, safeZone.boundsYSW - 100, safeZone.boundsXNE + 100, safeZone.boundsYNE + 100);
        updateInterface();
        gamePlayers.stream().forEach(player -> player.send(new SendHintArrow(new Position(safeZone.boundsXSW + 5, safeZone.boundsYSW + 5, 0), 7)));
    }

    /**
     * Handles processing the player if hes a active player within the LMS game
     * @param player
     */
    public static void processPlayer(Player player) {

        if (!isActivePlayer(player))
            return;

        handleFogDamage(player);
    }

    /**
     * Handles the fog damage for a player
     * @param player
     */
    private static void handleFogDamage(Player player) {
        if(safeZone == null) return;

        if(safeZone.inSafeZone(player)) return;

        if(!Boundary.isIn(player, new Boundary(currentFog.getLowX(), currentFog.getLowY(), currentFog.getHighX(), currentFog.getHighY()))) {
            int distanceX = player.getPosition().getDistances(new Position(currentFog.getLowX(), currentFog.getLowY()));
            int distanceY = player.getPosition().getDistances(new Position(currentFog.getHighX(), currentFog.getHighY()));

            int damage = (distanceX > distanceY) ? distanceY / 10 : distanceX / 10;
            if (damage <= 0)
                damage = 1;

            if (gameTicks > 600)
                damage *= 2;
            else if (gameTicks > 900)
                damage *= 3;

            int fogStrength = (distanceX > distanceY) ? distanceY * 3 : distanceX * 3;
            player.lastFogSent = fogStrength;
            player.send(new SendLMSFog(fogStrength > 155 ? 155 : fogStrength));
            player.damage(new Hit(damage > 10 ? 10 : damage, Hitsplat.NORMAL, HitIcon.NONE, true));
        } else if (player.lastFogSent != 0)
            player.send(new SendLMSFog(0));
    }

    /**
     * Handles the fog closing in on the save zone
     */
    public static void handleFog() {
        lastFogCycle++;
        if (lastFogCycle == 10) {
            if (currentFog == null) {
                lastFogCycle = 0;
                return;
            }

            if (safeZone.boundsXSW == currentFog.getLowX() && safeZone.boundsYSW == currentFog.getLowY() && safeZone.boundsXNE == currentFog.getHighX() && safeZone.boundsYNE == currentFog.getHighY()) {
                lastFogCycle = 0;
                return;
            }

            currentFog.decrease();
            lastFogCycle = 0;
        }
    }

    /**
     * Handles updating the player's there interface
     */
    public static void updateInterface() {
        gamePlayers.stream().filter(Objects::nonNull).forEach(player -> {
            player.send(new SendString("Survivors: " + gamePlayers.size() + "/" + LMSLobby.maxPlayers, 44664));
            player.send(new SendString("Kills: " + player.lmsKills, 44665));
            player.send(new SendString("Fog: " + (currentFog.isSafe() ? "Safe" : "Approaching"), 44666));
        });
    }

    /**
     * Handles the killers stuff
     * @param killer
     */
    public static void onKill(Player killer) {

        killer.lmsImmunity = System.currentTimeMillis() + 20_000;
        killer.runEnergy = 100;
        CombatSpecial.restore(killer, 100);
        killer.skills.get(Skill.HITPOINTS).modifyLevel(level -> level + 99, 0, 99);

        killer.lmsKills++;
        killer.lmsTotalKills++;
        updateInterface();

        if (killer.lmsKills == 5)
            killer.lmsPoints += 2;
        else if (killer.lmsKills == 3)
            killer.lmsPoints += 1;

        boolean inventory =  killer.inventory.getFreeSlots() > 0;

        Item key = new Item(getKeyId(), 1);
        if (inventory)
            killer.inventory.add(key);
        else
            GroundItem.create(killer, key);

        for(int index = 0; index < killer.inventory.getItems().length; index++) {
            if(killer.inventory.get(index) == null) continue;
            Item potion = killer.inventory.get(index);

            ItemDefinition def = ItemDefinition.get(potion.getId());
            if(def == null || !def.isPotion()) continue;

            killer.inventory.remove(potion);
        }

        resupplyKiller(killer);
    }

    /**
     * Handles resupplying the killer
     * @param player
     */
    public static void resupplyKiller(Player player) {
        LMSLoadout loadout = currentGameType;
        for (int i = 6; i < loadout.getInventory().length; i++) {
            int itemId = loadout.getInventory()[i];
            if(itemId == -1) continue;

            ItemDefinition def = ItemDefinition.get(itemId);
            if(def == null || !def.isPotion()) continue;

            player.inventory.add(itemId, 1);
        }
        player.inventory.add(385, player.inventory.getFreeSlots());

        player.inventory.refresh();
    }

    /**
     * Handles when a player within the LMS game dies
     * @param player
     * @param forceRemove
     */
    public static void onDeath(Player player, boolean forceRemove) {
        player.lmsTotalDeaths++;
        if (!forceRemove) {
            Player killer = (Player) player.getCombat().getDamageCache().calculateProperKiller().orElse(null);

            int containerSize = player.unlockedLMSItems.size();
            if(containerSize != 0) {
                for (int i = 0; i < containerSize; i++) {
                    int itemId = player.unlockedLMSItems.get(i);
                    Item item = new Item(itemId, getItemAmountForItem(itemId));
                    if (killer == null)
                        GroundItem.createGlobal(player, item);
                    else
                        GroundItem.create(killer, item);
                }
            }

            player.lmsPoints += getPointsForPlacement(gamePlayers.size());
            player.message("You have been awarded: @red@"+getPointsForPlacement(gamePlayers.size())+" @bla@points for placing @red@#"+gamePlayers.size());
        }

        reset(player);

        if(gamePlayers.size() == 1) {
            gameInProgress = false;
            LMSLobbyEvent.lobbyTicks = LMSLobbyEvent.defaultLobbyTime;
            Player winner = gamePlayers.get(0);
            winner.lmsPoints+= 5;
            winner.lmsWins++;
            winner.message("Congratulations! You've won the LMS Game!");
            winner.message("You have been awarded: @red@5 points@bla@ for your win!");
            reset(winner);
            if (lmsCrate != null) {
                lmsCrate.destroy();
                lmsCrate = null;
            }
            for(CustomGameObject gameObject : storedObjects)
                gameObject.unregister();
            storedObjects.clear();
            gamePlayers.clear();
        }
    }

    /**
     * Gets the amount of the item its supposed to drop when a player dies/logouts with there upgrades
     * @param loot
     * @return
     */
    private static int getItemAmountForItem(int loot) {
        switch(loot){
            case 22636:
            case 299:
            case 11212:
            case 19481:
                return 1000;
            default:
                return 1;
        }
    }

    /**
     * The amount of points the player receives when the player leaves/dies
     * @param remaining
     * @return
     */
    private static int getPointsForPlacement(int remaining) {
        if (remaining == 2)
            return 4;
        else if (remaining == 3 || remaining == 4)
            return 3;
        else if (remaining >= 5 && remaining <= 9)
            return 2;
        else if (remaining >= 10 && remaining <= 19)
            return 1;

        return 0;
    }

    /**
     * The key id the player gets after a kill
     * @return
     */
    public static int getKeyId() {
        return gamePlayers.size() <= 4 ? 20608 : 20526;
    }
}
