package com.osroyale.content.consume;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.osroyale.game.task.impl.AntiVenomTask;
import com.osroyale.game.task.impl.SuperAntipoisonTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.PoisonType;
import com.osroyale.game.world.entity.combat.effect.CombatEffectType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendPoison;
import com.osroyale.net.packet.out.SendRunEnergy;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type managing consumable potion types.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author lare96 <http://github.com/lare96>
 */
public enum PotionData {
    STAMINA__POTION(12625, 12627, 12629, 12631) {
        @Override
        public void onEffect(Player player) {
            PotionData.onEnergyEffect(player, true);
            player.energyRate = 200;
        }
    },
    SUPER_COMBAT_POTION(12695, 12697, 12699, 12701) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.ATTACK, BoostType.SUPER);
            PotionData.onBasicEffect(player, Skill.STRENGTH, BoostType.SUPER);
            PotionData.onBasicEffect(player, Skill.DEFENCE, BoostType.SUPER);
        }
    },
    ZAMORAK_BREW(2450, 189, 191, 193) {
        @Override
        public void onEffect(Player player) {
            PotionData.onZamorakEffect(player);
        }
    },
    SARADOMIN_BREW(6685, 6687, 6689, 6691) {
        @Override
        public void onEffect(Player player) {
            PotionData.onSaradominEffect(player);
        }
    },
    ANTIDOTE_PLUS(5943, 5945, 5947, 5949) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAntiPoisonEffect(player, true, 1000);
        }
    },
    ANTIDOTE_PLUS_PLUS(5952, 5954, 5956, 5958) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAntiPoisonEffect(player, true, 1200);
        }
    },
    AGILITY_POTION(3032, 3034, 3036, 3038) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAgilityEffect(player);
        }
    },
    FISHING_POTION(2438, 151, 153, 155) {
        @Override
        public void onEffect(Player player) {
            PotionData.onFishingEffect(player);
        }
    },
    RANGE_POTIONS(2444, 169, 171, 173) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.RANGED, BoostType.RANGING);
        }
    },
    ENERGY_POTIONS(3008, 3010, 3012, 3014) {
        @Override
        public void onEffect(Player player) {
            PotionData.onEnergyEffect(player, false);
        }
    },
    SUPER_ENERGY_POTIONS(3016, 3018, 3020, 3022) {
        @Override
        public void onEffect(Player player) {
            PotionData.onEnergyEffect(player, true);
        }
    },
    MAGIC_POTIONS(3040, 3042, 3044, 3046) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.MAGIC, BoostType.MAGIC);
        }
    },
    DEFENCE_POTIONS(2432, 133, 135, 137) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.DEFENCE, BoostType.NORMAL);
        }
    },
    STRENGTH_POTIONS(113, 115, 117, 119) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.STRENGTH, BoostType.NORMAL);
        }
    },
    ATTACK_POTIONS(2428, 121, 123, 125) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.ATTACK, BoostType.NORMAL);
        }
    },
    SUPER_DEFENCE_POTIONS(2442, 163, 165, 167) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.DEFENCE, BoostType.SUPER);
        }
    },
    SUPER_ATTACK_POTIONS(2436, 145, 147, 149) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.ATTACK, BoostType.SUPER);
        }
    },
    SUPER_STRENGTH_POTIONS(2440, 157, 159, 161) {
        @Override
        public void onEffect(Player player) {
            PotionData.onBasicEffect(player, Skill.STRENGTH, BoostType.SUPER);
        }
    },
    RESTORE_POTIONS(2430, 127, 129, 131) {
        @Override
        public void onEffect(Player player) {
            onRestoreEffect(player, false);
        }
    },
    SUPER_RESTORE_POTIONS(3024, 3026, 3028, 3030) {
        @Override
        public void onEffect(Player player) {
            PotionData.onRestoreEffect(player, true);
            int realLevel = player.skills.getMaxLevel(Skill.PRAYER);
            player.skills.get(Skill.PRAYER).modifyLevel(level -> level + (int) Math.floor(8 + (realLevel * 0.25)));
            player.skills.refresh(Skill.PRAYER);
        }
    },
    PRAYER_POTIONS(2434, 139, 141, 143) {
        @Override
        public void onEffect(Player player) {
            PotionData.onPrayerEffect(player, false);
        }
    },
    SUPER_PRAYER_POTIONS(15328, 15329, 15330, 15331) {
        @Override
        public void onEffect(Player player) {
            PotionData.onPrayerEffect(player, true);
        }
    },
    ANTIFIRE_POTIONS(2452, 2454, 2456, 2458) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAntiFireEffect(player, false);
        }
    },
    SUPER_ANTIFIRE_POTIONS(15304, 15305, 15306, 15307) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAntiFireEffect(player, true);
        }
    },
    ANTIPOISON_POTIONS(2446, 175, 177, 179) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAntiPoisonEffect(player, false, 0);
        }
    },
    SUPER_ANTIPOISON_POTIONS(2448, 181, 183, 185) {
        @Override
        public void onEffect(Player player) {
            PotionData.onAntiPoisonEffect(player, true, 500);
        }
    },
    ANTI_VENOM(12905, 12907, 12909, 12911) {
        @Override
        public void onEffect(Player player) {
            player.unvenom();
            player.unpoison();

            if (player.getPoisonImmunity().get() <= 0) {
                player.send(new SendMessage("You have been granted immunity against poison."));
                World.schedule(new SuperAntipoisonTask(player).attach(player));
            } else if (player.getPoisonImmunity().get() > 0) {
                player.send(new SendMessage("Your immunity against poison has been restored!"));
            }
            player.getPoisonImmunity().set(1200);

            if (player.getVenomImmunity().get() <= 0) {
                player.send(new SendMessage("You have been granted immunity against venom."));
                World.schedule(new AntiVenomTask(player).attach(player));
            } else if (player.getVenomImmunity().get() > 0) {
                player.send(new SendMessage("Your immunity against venom has been restored!"));
            }
            player.getVenomImmunity().set(300);
        }
    },
    ANTI_VENOM_PLUS(12913, 12915, 12917, 12919) {
        @Override
        public void onEffect(Player player) {
            player.unvenom();
            player.unpoison();

            if (player.getPoisonImmunity().get() <= 0) {
                player.send(new SendMessage("You have been granted immunity against poison."));
                World.schedule(new SuperAntipoisonTask(player).attach(player));
            } else if (player.getPoisonImmunity().get() > 0) {
                player.send(new SendMessage("Your immunity against poison has been restored!"));
            }
            player.getPoisonImmunity().set(1500);

            if (player.getVenomImmunity().get() <= 0) {
                player.send(new SendMessage("You have been granted immunity against venom."));
                World.schedule(new AntiVenomTask(player).attach(player));
            } else if (player.getVenomImmunity().get() > 0) {
                player.send(new SendMessage("Your immunity against venom has been restored!"));
            }
            player.getVenomImmunity().set(3000);
        }
    };

    /**
     * Caches our enum values.
     */
    private static final ImmutableSet<PotionData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(PotionData.class));

    /**
     * The default item representing the final potion dose.
     */
    private static final Item VIAL = new Item(229);

    /**
     * The identifiers which represent this potion type.
     */
    private final int[] ids;

    /**
     * Create a new {@link PotionData}.
     *
     * @param ids the identifiers which represent this potion type.
     */
    PotionData(int... ids) {
        this.ids = ids;
    }

    /**
     * The method that executes the fishing potion action.
     *
     * @param player the player to do this action for.
     */
    private static void onFishingEffect(Player player) {
        player.skills.get(Skill.FISHING).modifyLevel(level -> level + 3);
        player.skills.refresh(Skill.FISHING);
    }

    /**
     * The method that executes the agility potion action.
     *
     * @param player the player to do this action for.
     */
    private static void onAgilityEffect(Player player) {
        player.skills.get(Skill.AGILITY).modifyLevel(level -> level + 3);
        player.skills.refresh(Skill.AGILITY);
    }

    /**
     * The method that executes the Saradomin brew action.
     *
     * @param player the player to do this action for.
     */
    private static void onSaradominEffect(Player player) {
        modifySkill(player, Skill.HITPOINTS, 0.15, 2);
        modifySkill(player, Skill.DEFENCE, 0.20, 2);
        modifySkill(player, Skill.ATTACK, -0.10, 2);
        modifySkill(player, Skill.STRENGTH, -0.10, 2);
        modifySkill(player, Skill.RANGED, -0.10, 2);
        modifySkill(player, Skill.MAGIC, -0.10, 2);
    }

    /**
     * The method that executes the Zamorak brew action.
     *
     * @param player the player to do this action for.
     */
    private static void onZamorakEffect(Player player) {
        modifySkill(player, Skill.ATTACK, 0.20, 2);
        modifySkill(player, Skill.STRENGTH, 0.12, 2);
        modifySkill(player, Skill.DEFENCE, -0.10, 2);
        modifySkill(player, Skill.HITPOINTS, -0.10, 2);
        modifySkill(player, Skill.PRAYER, 0.10, 0);
    }

    /**
     * The method that executes the prayer potion action.
     *
     * @param player      the player to do this action for.
     * @param superPrayer determines if this potion is a super prayer potion.
     */
    private static void onPrayerEffect(Player player, boolean superPrayer) {
        int realLevel = player.skills.get(Skill.PRAYER).getMaxLevel();
        player.skills.get(Skill.PRAYER).modifyLevel(level -> level + (int) Math.floor(7 + (realLevel * (superPrayer ? 0.35 : 0.25))));
        player.skills.refresh(Skill.PRAYER);

    }

    /**
     * The method that executes the anti-poison potion action.
     *
     * @param player      the player to do this action for.
     * @param superPotion {@code true} if this potion is a super potion, {@code
     *                    false} otherwise.
     * @param length      the length that the effect lingers for.
     */
    public static void onAntiPoisonEffect(Player player, boolean superPotion, int length) {
        if (player.isVenomed()) {
            player.getVenomDamage().set(0);
            CombatUtil.cancelEffect(player, CombatEffectType.VENOM);
            player.poison(PoisonType.WEAK_NPC);
            return;
        }

        if (player.isPoisoned()) {
            player.getPoisonDamage().set(0);
            CombatUtil.cancelEffect(player, CombatEffectType.POISON);
            player.send(new SendConfig(174, 0));
            player.send(new SendMessage("You have been cured of your poison!"));
            player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
        }

        if (superPotion) {
            if (length > 0 && player.getPoisonImmunity().get() <= 0) {
                player.send(new SendMessage("You have been granted immunity against poison."));
                player.getPoisonImmunity().incrementAndGet(length);
                World.schedule(new SuperAntipoisonTask(player).attach(player));
            } else if (player.getPoisonImmunity().get() > 0) {
                player.send(new SendMessage("Your immunity against poison has been restored!"));
                player.getPoisonImmunity().set(length);
            }
        }
    }

    /**
     * The method that executes the energy potion action.
     *
     * @param player      the player to do this action for.
     * @param superPotion {@code true} if this potion is a super potion, {@code
     *                    false} otherwise.
     */
    private static void onEnergyEffect(Player player, boolean superPotion) {
        int amount = superPotion ? 20 : 10;
        int energy = player.runEnergy;

        if (amount + energy > 100) {
            player.runEnergy = 100;
        } else {
            player.runEnergy += amount;
        }

        player.send(new SendRunEnergy());
    }

    /**
     * The method that executes the restore potion action.
     *
     * @param player the player to do this action for.
     */
    private static void onRestoreEffect(Player player, boolean superRestore) {
        for (int index = 0; index <= 6; index++) {
            if ((index == Skill.PRAYER) || (index == Skill.HITPOINTS)) {
                continue;
            }

            Skill skill = player.skills.get(index);
            int realLevel = skill.getMaxLevel();

            if (skill.getLevel() >= realLevel) {
                continue;
            }

            int formula = superRestore ? (int) Math.floor(8 + (realLevel * 0.25)) : (int) Math.floor(10 + (realLevel * 0.30));
            player.skills.get(index).modifyLevel(level -> level + formula);
            player.skills.refresh(index);
        }
    }

    /**
     * The method that executes the anti-fireRunes potion action.
     *
     * @param player       the player to do this action for.
     * @param superVariant determines if this potion is the super variant.
     */
    private static void onAntiFireEffect(Player player, boolean superVariant) {
        if (superVariant) {
            CombatUtil.effect(player, CombatEffectType.SUPER_ANTIFIRE_POTION);
        } else {
            CombatUtil.effect(player, CombatEffectType.ANTIFIRE_POTION);
        }
    }

    /**
     * The method that executes the basic effect potion action that will
     * append the level of {@code skill}.
     *
     * @param player the player to do this action for.
     */
    private static void onBasicEffect(Player player, int skill, BoostType type) {
        modifySkill(player, skill, type.amount, type.base);
    }

    /**
     * The method that executes the basic effect potion action that will
     * append the level of {@code skill}.
     *
     * @param player the player to do this action for.
     */
    private static void modifySkill(Player player, int skill, double percentage, int base) {
        Skill s = player.skills.get(skill);
        int realLevel = s.getMaxLevel();

        final int boostLevel = (int) (realLevel * percentage + base);

        int cap = s.getLevel();
        if (cap < realLevel + boostLevel) {
            cap = realLevel + boostLevel;
        }

        if (skill == Skill.HITPOINTS && boostLevel < 0) {
            int damage = boostLevel;
            if (player.getCurrentHealth() + damage <= 0) damage = -player.getCurrentHealth() + 1;
            player.damage(new Hit(-damage));
        } else {
            player.skills.get(skill).modifyLevel(level -> level + boostLevel, 0, cap);
        }

        player.skills.refresh(skill);
    }

    /**
     * Retrieves the replacement item for {@code item}.
     *
     * @param item the item to retrieve the replacement item for.
     * @return the replacement item wrapped in an optional, or an empty optional
     * if no replacement item is available.
     */
    public static Item getReplacementItem(Item item) {
        Optional<PotionData> potion = forId(item.getId());
        if (potion.isPresent()) {
            int length = potion.get().getIds().length;
            for (int index = 0; index < length; index++) {
                if (potion.get().getIds()[index] == item.getId() && index + 1 < length) {
                    return new Item(potion.get().getIds()[index + 1]);
                }
            }
        }
        return VIAL;
    }

    /**
     * Retrieves the potion consumable element for {@code id}.
     *
     * @param id the id that the potion consumable is attached to.
     * @return the potion consumable wrapped in an optional, or an empty
     * optional if no potion consumable was found.
     */
    public static Optional<PotionData> forId(int id) {
        for (PotionData potion : VALUES) {
            for (int potionId : potion.getIds()) {
                if (id == potionId) {
                    return Optional.of(potion);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * The method executed when this potion type activated.
     *
     * @param player the player to execute this effect for.
     */
    public abstract void onEffect(Player player);

    /**
     * The method which determines if the {@code player} can drink the potion.
     *
     * @param player the player to determine this for.
     */
    public boolean canDrink(Player player) {
        return true;
    }

    /**
     * Gets the identifiers which represent this potion type.
     *
     * @return the identifiers for this potion.
     */
    public final int[] getIds() {
        return ids;
    }

    /**
     * Gets the item id for the specified dose.
     *
     * @param dose the dose to get the item id from.
     * @return the item id.
     */
    public int getIdForDose(int dose) {
        return ids[ids.length - dose];
    }

    /**
     * The enumerated type whose elements represent the boost types for potions.
     *
     * @author Ryley Kimmel <ryley.kimmel@live.com>
     * @author lare96 <http://github.com/lare96>
     */
    public enum BoostType {
        NORMAL(3, .10F),
        RANGING(4, .10F),
        MAGIC(4, 0),
        SUPER(5, .15F);

        /**
         * The base which we will append by.
         */
        private final int base;

        /**
         * The amount this type will boost by.
         */
        private final float amount;

        /**
         * Creates a new {@link BoostType}.
         *
         * @param boostAmount the amount this type will boost by.
         */
        BoostType(int base, float boostAmount) {
            this.base = base;
            this.amount = boostAmount;
        }

        /**
         * Gets the base this type will boost by.
         *
         * @return the base amount.
         */
        public final int getBase() {
            return base;
        }

        /**
         * Gets the amount this type will boost by.
         *
         * @return the boost amount.
         */
        public final float getAmount() {
            return amount;
        }
    }
}