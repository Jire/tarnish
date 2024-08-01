package com.osroyale.game.service;

import com.osroyale.game.world.entity.mob.player.Player;

public class Highscores {

    private int[] experience = new int[26];

    private Player player;

    public Highscores(Player player) {
        this.player = player;
    }

    public void execute() {// test2
/*        switch (player.right) {
            case ULTIMATE_IRONMAN:
                com.everythingrs.hiscores.Hiscores.update("V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH",
                        "Ultimate Ironman", player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
                break;
            case IRONMAN:
                com.everythingrs.hiscores.Hiscores.update("V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH", "Ironman",
                        player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
                break;
            case HARDCORE_IRONMAN:
                com.everythingrs.hiscores.Hiscores.update(
                        "V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH", "Hardcore Ironman",
                        player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
                break;
            default:
                com.everythingrs.hiscores.Hiscores.update(
                        "V48tgd7OxwrvPMmZZbjkVt6qTYB2MrZ5sw6PD4DBJnM83zepnUlIT8DiFoND3BYEQjqxPamH", "Normal Mode",
                        player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
                break;

        }*/
    }

    private int[] getExperience() {
        for (int i = 0; i < player.skills.getSkills().length; i++) {
            // System.out.println("Experience: " + Skill.getExperienceForLevel(99));
            experience[i] = (int) player.skills.getSkills()[i].getExperience();
        }
        return experience;
    }
}



