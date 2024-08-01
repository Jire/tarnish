package com.osroyale.content.activity.impl.duelarena;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public final class DuelUtils {

    private DuelUtils() {

    }

    public static boolean hasFunWeapon(Player player, Item item) {
        if (item == null) {
            return false;
        }

        if (item.getName() == null) {
            return false;
        }

        final String name = item.getName().toLowerCase();

        return name.equals("fixed device") || name.contains("flowers") || name.equals("gnomeball") || name.equals("mouse toy") || name.equals("noose wand") || name.equals("pet rock") || name.equals("rat pole")
                || name.equals("rubber chicken") || name.equals("scythe") || name.equals("snowball") || name.equals("trollweiss") || name.equals("undead chicken") || name.equals("large spade") || name.equals("stale baguette")
                || name.equals("hunting knife") || name.equals("giant present") || name.equals("hand fan");
    }

    public static String getRuleText(DuelRule rule) {
        switch(rule) {
            case LOCK_ITEMS:
                return "You can not switch items.";
            case NO_RANGED:
                return "You can not use ranged attacks.";
            case NO_MELEE:
                return "You can not use melee attacks.";
            case NO_MAGIC:
                return "You can not use magic attacks.";
            case NO_SPECIAL_ATTACK:
                return "You can not use special attacks.";
            case ONLY_FUN_WEAPONS:
                return "You can only attack with `fun` weapons.";
            case NO_FORFEIT:
                return "You can not forfeit the duel.";
            case NO_PRAYER:
                return "You can not use prayer.";
            case NO_DRINKS:
                return "You can not use drinks.";
            case NO_FOOD:
                return "You can not use food.";
            case NO_MOVEMENT:
                return "You can not move during the duel.";
            case OBSTACLES:
                return "There will be obsticals in the arena.";
            case ONLY_WHIP_DDS:
                return "You can only use whips and dragon daggers.";
            case NO_HEAD:
                return "You can not wear items on your head.";
            case NO_CAPE:
                return "You can not wear items on your back.";
            case NO_NECKLACE:
                return "You can not wear items on your neck.";
            case NO_AMMO:
                return "You can not wear items in your quiver.";
            case NO_WEAPON:
                return "You can not wield weapons.";
            case NO_BODY:
                return "You can not wear items on your chest.";
            case NO_SHIELD:
                return "You can not use shields or 2h swords.";
            case NO_LEGS:
                return "You can not wear items on your legs.";
            case NO_GLOVES:
                return "You can not wear items on your hands.";
            case NO_BOOTS:
                return "You can not wear items on your feet.";
            case NO_RINGS:
                return "You can not wear items on your fingers.";
        }
        return "";
    }

    public static String getItemNames(Item[] items) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            if (item == null) {
                continue;
            }

            builder.append("<col=ff9040>").append(item.getName()).append("<col=ffffff>").append(" x ").append(item.getAmount());

            if (i < items.length - 1) {
                builder.append("\\n");
            }

        }
        final String result = builder.toString();
        return result.isEmpty() ? "<col=ff9040>Absolutely nothing!" : result;
    }

}
