package com.osroyale.content.lms.lobby;

import com.osroyale.content.lms.LMSGame;
import com.osroyale.content.lms.loadouts.LMSLoadout;
import com.osroyale.game.world.entity.combat.weapon.WeaponInterface;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class LMSLobby {

    public static boolean DEVELOPMENT_MODE = false;

    /**
     * The position the player gets put on when finishing a game
     */
    public static Position finish = new Position(3141, 3634, 0);

    /**
     * A list with all the players in the lobby
     */
    public static List<Player> lobbyMembers = new ArrayList<>();

    /**
     * The amount of players in the lobby required
     */
    public static int requiredPlayers = DEVELOPMENT_MODE ? 2 : 4;

    /**
     * The max amount of players allowed in a single game
     */
    public static int maxPlayers = 24;

    /**
     * The current game type
     */
    public static LMSLoadout currentGameType;

    public static String getGameTypeName() {
        return currentGameType.getClass().getSimpleName();
    }

    /**
     * Handles joining the lobby
     * @param player
     */
    public static void joinLobby(Player player) {

        if(lobbyMembers.size() >= maxPlayers) {
            //TODO: Make a dialogue
            return;
        }

        if (LMSGame.isActivePlayer(player)) return;

        if (player.pet != null) {
            //TODO: Make a dialogue
            return;
        }

        if(!lobbyMembers.contains(player))
            lobbyMembers.add(player);

        player.move(player.getPosition().north());

        if(LMSGame.gameInProgress)
            player.message("A game is currently on-going and should end shortly.");

        //Handles making a copy of the spellbook
        player.spellbook_copy = player.spellbook;

        //Handles making a copy of the inventory
        player.inventory_copy.set(player.inventory.getItems().clone());
        player.inventory.clear();
        player.inventory.refresh();

        //Handles making a copy of the equipment
        player.equipment_copy.set(player.equipment.getItems().clone());
        player.equipment.clear();
        player.equipment.refresh();
        player.equipment.updateAnimation();
        WeaponInterface.execute(player, player.equipment.getWeapon());

        //Handles making a copy of the rune pouch
        player.runePouch_copy.clear(false);
        for(Item rune : player.runePouch.runes)
            player.runePouch_copy.runes.add(new Item(rune.getId(), rune.getAmount()));
        player.runePouch.clear(false);

        //Handles making a copy of the skills
        for(int index = 0; index < player.skills.getSkills().length; index++) {
            Skill skill = player.skills_copy.get(index);
            Skill real_skill = player.skills.get(index);

            skill.setLevel(real_skill.getLevel());
            skill.setMaxLevel(real_skill.getMaxLevel());
            skill.setExperience(skill.getExperienceForLevel(real_skill.getLevel()));
        }
        player.skills_copy.experienceCounter = player.skills.experienceCounter;

        player.updateFlags.add(UpdateFlag.APPEARANCE);
    }

    /**
     * Handles leaving the lobby
     * @param player
     */
    public static void leaveLobby(Player player, boolean logout) {
        lobbyMembers.remove(player);

        //Sets back the players old inventory
        player.inventory.set(player.inventory_copy.getItems().clone());
        player.inventory_copy.clear();
        player.inventory.refresh();

        //Sets back the players old equipment
        player.equipment.set(player.equipment_copy.getItems().clone());
        player.equipment_copy.clear();
        player.equipment.refresh();
        player.equipment.updateAnimation();
        WeaponInterface.execute(player, player.equipment.getWeapon());

        //Sets back the old rune pouch
        player.runePouch.clear();
        for(Item rune : player.runePouch_copy.runes)
            player.runePouch.runes.add(new Item(rune.getId(), rune.getAmount()));
        player.runePouch_copy.clear(false);

        player.updateFlags.add(UpdateFlag.APPEARANCE);

        if(logout)
            player.move(new Position(finish.getX() + Utility.random(1), finish.getY() + Utility.random(3), 0));
        else {
            int diffX = player.getPosition().getX() >= 3142 ? 3142 - player.getPosition().getX() : 3141 - player.getPosition().getX();
            int diffY = 3638 - player.getPosition().getY();
            player.move(player.getPosition().copy().transform(diffX, diffY, 0));
        }
    }
}
