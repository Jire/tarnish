package com.osroyale.game.world.entity.combat.magic;

import com.osroyale.Config;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.MessageColor;
import com.osroyale.util.StringUtils;

public class Autocast {
    private final static int[] ANCIENT_STAFFS = {21006, 4675, 4710, 11791, 12904, 6914};
    private final static int[] NO_AUTOCAST = {12899, 11907, 11908};

    public static boolean clickButton(Player player, int button) {
        switch (button) {
            case 2004:
            case 6161:
            case -20754:
            case -21584:
                player.interfaceManager.setSidebar(Config.ATTACK_TAB, 328);
                player.getCombat().reset();
                return true;
            case 24111:
            case 349:
                if (!player.isAutocast()) {
                    Item weapon = player.equipment.getWeapon();

                    if (weapon != null) {
                        sendSelectionInterface(player, weapon.getId());
                    }
                } else {
                    reset(player);
                }
                player.getCombat().reset();
                return true;

                //Strike spells
            case 1830:
            case -20753:
                setAutocast(player, 1152);
                return true;
            case 1834:
            case -20752:
                setAutocast(player, 1154);
                return true;
            case 1838:
            case -20751:
                setAutocast(player, 1156);
                return true;
            case 1842:
            case -20750:
                setAutocast(player, 1158);
                return true;

                //Bolt
            case 1831:
            case -20749:
                setAutocast(player, 1160);
                return true;
            case 1835:
            case -20748:
                setAutocast(player, 1163);
                return true;
            case 1839:
            case -20747:
                setAutocast(player, 1166);
                return true;
            case 1843:
            case -20746:
                setAutocast(player, 1169);
                return true;

                //Blast spells
            case 1832:
            case -20745:
                setAutocast(player, 1172);
                return true;
            case 1836:
            case -20744:
                setAutocast(player, 1175);
                return true;
            case 1840:
            case -20743:
                setAutocast(player, 1177);
                return true;
            case 1844:
            case -20742:
                setAutocast(player, 1181);
                return true;

                //Wave
            case 1833:
            case -20741:
                setAutocast(player, 1183);
                return true;
            case 1837:
            case -20740:
                setAutocast(player, 1185);
                return true;
            case 1841:
            case -20739:
                setAutocast(player, 1188);
                return true;
            case 1845:
            case -20738:
                setAutocast(player, 1189);
                return true;

                //Surge spells
            case -20737:
                setAutocast(player, 40140);
                return true;
            case -20736:
                setAutocast(player, 40150);
                return true;
            case -20735:
                setAutocast(player, 40170);
                return true;
            case -20734:
                setAutocast(player, 40190);
                return true;

                //Smoke rush
            case 13189:
            case -21583:
                setAutocast(player, 12939);
                return true;
                //Smoke burst
            case 13215:
            case -21579:
                setAutocast(player, 12963);
                return true;
            case 13202:
                //Smoke blitz
            case -21575:
                setAutocast(player, 12951);
                return true;
                //Smoke barrage
            case 13228:
            case -21571:
                setAutocast(player, 12975);
                return true;

                //Shadow rush
            case 13241:
            case -21582:
                setAutocast(player, 12987);
                return true;
                //Shadow burst
            case 13267:
            case -21578:
                setAutocast(player, 13011);
                return true;
                //Shadow blitz
            case 13254:
            case -21574:
                setAutocast(player, 12999);
                return true;
                //Smoke barrage
            case 13280:
            case -21570:
                setAutocast(player, 13023);
                return true;

                //Blood rush
            case 13147:
            case -21581:
                setAutocast(player, 12901);
                return true;
                //Blood burst
            case 13167:
            case -21577:
                setAutocast(player, 12919);
                return true;
                //Blood blitz
            case 13158:
            case -21573:
                setAutocast(player, 12911);
                return true;
                //Blood barrage
            case 13178:
            case -21569:
                setAutocast(player, 12929);
                return true;

                //Ice rush
            case 6162:
            case -21580:
                setAutocast(player, 12861);
                return true;
                //Ice burst
            case 13125:
            case -21576:
                setAutocast(player, 12881);
                return true;
                //Ice blitz
            case 13114:
            case -21572:
                setAutocast(player, 12871);
                return true;
                //Ice barrage
            case 13136:
            case -21568:
                setAutocast(player, 12891);
                return true;
        }
        return false;
    }

    public static void reset(Player player) {
        if (player.isAutocast()) {
            player.setAutocast(null);
            player.send(new SendConfig(108, 0));
            player.send(new SendString("Spell", 18584));
        }
    }

    private static void sendSelectionInterface(Player player, int weaponId) {
        for (int id : NO_AUTOCAST) {
            if (weaponId == id) {
                player.send(new SendMessage("You can't autocast with this weapon!"));
                return;
            }
        }

        if (player.spellbook == null) {
            player.spellbook = Spellbook.MODERN;
        }

        switch (player.spellbook) {
            case ANCIENT:
                if (!player.equipment.containsAny(ANCIENT_STAFFS)) {
                    if (!player.equipment.hasWeapon()) {
                        return;
                    }

                    String def = player.equipment.getWeapon().getName().toLowerCase();
                    player.send(new SendMessage("You can't autocast ancient magicks with " + StringUtils.getAOrAn(def) + " " + def + "."));
                    return;
                }
                player.interfaceManager.setSidebar(Config.ATTACK_TAB, 43950); //1689
                break;
            case LUNAR:
                player.send(new SendMessage("You can't autocast lunar magicks!"));
                break;
            case MODERN:
                if (player.equipment.contains(4675)) {
                    player.send(new SendMessage("You can't autocast normal magicks with this staff!"));
                    return;
                }
                player.interfaceManager.setSidebar(Config.ATTACK_TAB, 44780); //1829
                break;
        }
    }

    private static void setAutocast(Player player, int id) {
        CombatSpell spell = CombatSpell.get(id);

        if (spell == null) {
            player.send(new SendMessage("[ERROR] No spell definition found.", MessageColor.LIGHT_RED));
            return;
        }

        if (!PlayerRight.isAdministrator(player) && !MagicRune.hasRunes(player, spell.getRunes())) {
            player.send(new SendMessage("You don't have the runes required for this spell!"));
            return;
        }

        player.setAutocast(spell);
        player.send(new SendConfig(43, 3));
        player.send(new SendConfig(108, 1));
        player.interfaceManager.setSidebar(Config.ATTACK_TAB, 328);
    }
}
