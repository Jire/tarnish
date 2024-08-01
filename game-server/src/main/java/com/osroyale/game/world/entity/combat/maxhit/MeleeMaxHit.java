package com.osroyale.game.world.entity.combat.maxhit;

import com.osroyale.game.world.entity.combat.attack.FightStyle;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;

/**
 * @author Origin
 * @Since January 16 2022
 */
public class MeleeMaxHit {

    public static int maxHit(Mob player) {
        /**
         * Max Hit
         *
         */

        int maxHit = (int) Math.floor(getBaseDamage(player) * slayerPerkBonus(player));

        /**
         * Random Stuff
         */

       /* if (CombatFactory.fullDharoks(player)) {
            double lostHp = player.maxHp() - player.hp();
            maxHit *= (1 + ((lostHp /100) * (player.maxHp() / 100)));
        } */

        return (int) Math.floor(maxHit);
    }

    public static int getBaseDamage(Mob player) {
        return (int) (Math.floor(0.5 + (getEffectiveStrength(player)) * (getStrengthBonus(player) + 64) + 320) / 640.0);
    }

    public static int getStrengthBonus(Mob player) {
        return player.getBonus(9);
    }

    public static int getStrengthLevel(Mob player) {
        return player.skills.getLevel(Skill.STRENGTH);
    }

    private static double getPrayerBonus(Mob player) {
        /**
         * Prayer Bonus
         *
         */
        double prayerBonus = 1;
        if (player.prayer.isActive(Prayer.THICK_SKIN))
            prayerBonus += 1.05D; // 5% def level boost
        else if (player.prayer.isActive(Prayer.ROCK_SKIN))
            prayerBonus += 1.10D; // 10% def level boost
        else if (player.prayer.isActive(Prayer.STEEL_SKIN))
            prayerBonus += 1.15D; // 15% def level boost
        else if (player.prayer.isActive(Prayer.CHIVALRY))
            prayerBonus += 1.20D; // 20% def level boost
        else if (player.prayer.isActive(Prayer.PIETY))
            prayerBonus += 1.25D; // 25% def level boost
        return prayerBonus;
    }

    public static int getStyleBonus(Mob player) {
        FightStyle style = player.getCombat().getFightType().getStyle();
        return style.equals(FightStyle.AGGRESSIVE) ? 3 : style.equals(FightStyle.ACCURATE) ? 1 : 0;
    }

    public static double slayerPerkBonus(Mob player) {
     //   SlayerTask target = player.getPlayer().slayer.getTask();

        double slayerPerkBonus = 1.0;

        /**
         * Do slayer shit here
         */
        return slayerPerkBonus;
    }

    public static double getOtherBonus(Mob player, boolean includeNpcMax) {

        //FightStyle style = player.getCombat().getFightType().getStyle();
        double otherBonus = 1;
        //boolean target = player.getCombat().getDefender().isNpc();

        /**
         * Other bonuses
         *
         */

       /* if (FormulaUtils.voidMelee(player) || FormulaUtils.wearingEliteVoid(player)) {
            otherBonus *= 1.10;
        }

        var wearingAnyBlackMask = FormulaUtils.wearingBlackMask(player) || FormulaUtils.wearingBlackMaskImbued(player) || player.getEquipment().wearingSlayerHelm();

        if(wearingAnyBlackMask && target != null && target.isNpc() && includeNpcMax) {
            NPC npc = target.getAsNpc();
            if(npc.id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.1667;
            }

            if(Slayer.creatureMatches(player, npc.id())) {
                otherBonus *= 1.1667;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULET) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.16;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.16;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULETI) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULET_E) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULETEI) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, RED_SLAYER_HELMET_I) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.10;
        }

        if (FormulaUtils.wearingTwistedSlayerHelmetI(player) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.10;
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.70;
            }

            if(FormulaUtils.isDemon(target)) {
                otherBonus *= 1.70;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, DARKLIGHT) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.60;
            }

            if(FormulaUtils.isDemon(target)) {
                otherBonus *= 1.60;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, DARKLIGHT) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.175;
            }

            if(target.isNpc() && target.getAsNpc().def() != null && (target.getAsNpc().def().name.equalsIgnoreCase("Kurask") || target.getAsNpc().def().name.equalsIgnoreCase("Turoth"))) {
                otherBonus *= 1.175;
            }
        }

        if (FormulaUtils.obbyArmour(player) && FormulaUtils.hasObbyWeapon(player)) {
            otherBonus *= 1.10;
        }

        if(FormulaUtils.berserkerNecklace(player) && FormulaUtils.hasObbyWeapon(player)) {
            otherBonus *= 1.10;
        }

        if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
            if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                otherBonus *= 1.05;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_LANCE) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isDragon(target)) {
                otherBonus *= 1.20;
            }
        }

        //• Gadderhammer: 1.25 or 2.0 vs shades
        if(player.getEquipment().hasAt(EquipSlot.WEAPON, GADDERHAMMER)) {
            if(target != null && target.isNpc()) {
                NPC npc = target.getAsNpc();
                NpcDefinition def = npc.def();
                var isShade = def != null && def.name.equalsIgnoreCase("Shade");
                otherBonus *= isShade ? 1.25 : 2.00;
            }
        }

        if (FormulaUtils.hasViggorasChainMace(player) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.50;
        }*/

        return otherBonus;
    }

    public static int getEffectiveStrength(Mob player) {
        return (int) (Math.floor(((((getStrengthLevel(player)) * getPrayerBonus(player)) + getStyleBonus(player)) + 8) * getOtherBonus(player, true)));
    }
}
