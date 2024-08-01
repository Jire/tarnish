package com.osroyale.game.world.entity.combat.ranged;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.items.Item;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Optional;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public enum RangedAmmunition {
    BRONZE_JAVELIN(false, 825, 831, 5642, 5648) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze javelin");
        }
    },
    IRON_JAVELIN(false, 826, 832, 5643, 5649) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron javelin");
        }
    },
    STEEL_JAVELIN(false, 827, 833, 5644, 5650) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel javelin");
        }
    },
    MITHRIL_JAVELIN(false, 828, 834, 5645, 5651) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril javelin");
        }
    },
    ADAMANT_JAVELIN(false, 829, 835, 5646, 5652) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant javelin");
        }
    },
    RUNE_JAVELIN(false, 830, 836, 5647, 5653) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune javelin");
        }
    },
    DRAGON_JAVELIN(false, 19484, 19486, 19488, 19490) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon javelin");
        }
    },

    BRONZE_THROWNAXE(true, 800) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze thrownaxe");
        }
    },
    IRON_THROWNAXE(true, 801) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron thrownaxe");
        }
    },
    STEEL_THROWNAXE(true, 802) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel thrownaxe");
        }
    },
    MITHRIL_THROWNAXE(true, 803) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril thrownaxe");
        }
    },
    ADAMANT_THROWNAXE(true, 804) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant thrownaxe");
        }
    },
    RUNE_THROWNAXE(true, 805) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune thrownaxe");
        }
    },
    DRAGON_THROWNAXE(true, 20849) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon thrownaxe");
        }
    },

    BRONZE_DART(true, 806, 812, 5628, 5635) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze dart");
        }
    },
    CRAWS_BOW(false, 22550) {

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Craw's bow");
        }
    },
    HEFIN(false, 25867) { //RED

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Hefin");
        }
    },
    IORWERTH(false, 25886) { //BLACK 25886

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iorwerth");
        }
    },
    TRAHEAERN(false, 25888) { //PURPLE

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Traheaern");
        }
    },
    CADARN(false, 25890) { //GREEN

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Cadarn");
        }
    },
    CRWYS(false, 25892) { //YELLOW

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Crwys");
        }
    },
    AMLODD(false, 25896) { //BLUE

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Amlodd");
        }
    },
    MEILYR(false, 25884, 25865, 25894) { //WHITE

        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Meilyr");
        }
    },



    IRON_DART(true, 807, 813, 5629, 5636) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron dart");
        }
    },
    BLACK_DART(true, 3093, 3094, 5631, 5638) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Black dart");
        }
    },
    STEEL_DART(true, 808, 814, 5630, 5637) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel dart");
        }
    },
    MITHRIL_DART(true, 809, 815, 5632, 5639) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril dart");
        }
    },
    ADAMANT_DART(true, 810, 816, 5633, 5640) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant dart");
        }
    },
    RUNE_DART(true, 811, 817, 5634, 5641) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune dart");
        }
    },
    DRAGON_DART(true, 11230, 11231, 11233, 11234) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon dart");
        }
    },

    BRONZE_KNIFE(true, 864, 870, 5654, 5651) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze knife");
        }
    },
    IRON_KNIFE(true, 863, 871, 5655, 5662) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron knife");
        }
    },
    BLACK_KNIFE(true, 869, 874, 5658, 5665) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Black knife");
        }
    },
    STEEL_KNIFE(true, 865, 872, 5656, 5663) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel knife");
        }
    },
    MITHRIL_KNIFE(true, 866, 873, 5657, 5664) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril knife");
        }
    },
    ADAMANT_KNIFE(true, 867, 875, 5659, 5666) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant knife");
        }
    },
    DRAGON_BOLTS(true, 21932, 21934, 21936, 21938, 21940, 21942, 21944, 21946, 21948, 21950) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon Bolts");
        }
    },
    RUNE_KNIFE(true, 868, 876, 5660, 5667) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune knife");
        }
    },
    DRAGON_KNIFE(true, 22804, 22810) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon knife");
        }
    },

    DOUBLE_BRONZE_ARROW(true, darkBows(), 882, 883, 5616, 5622) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze arrow");
        }
    },
    DOUBLE_IRON_ARROW(true, darkBows(), 884, 885, 5617, 5623) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron arrow");
        }
    },
    DOUBLE_STEEL_ARROW(true, darkBows(), 886, 887, 5618, 5624) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel arrow");
        }
    },
    DOUBLE_MITHRIL_ARROW(true, darkBows(), 888, 889, 5619, 5625) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril arrow");
        }
    },
    DOUBLE_ADAMANT_ARROW(true, darkBows(), 890, 891, 5620, 5626) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant arrow");
        }
    },
    DOUBLE_RUNE_ARROW(true, darkBows(), 892, 893, 5621, 5627) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune arrow");
        }
    },
    DOUBLE_DRAGON_ARROW(true, darkBows(), 11212, 11227, 11228, 11229) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon arrow");
        }
    },

    BRONZE_ARROW(true, 882, 883, 5616, 5622) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze arrow");
        }
    },
    IRON_ARROW(true, 884, 885, 5617, 5623) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron arrow");
        }
    },
    STEEL_ARROW(true, 886, 887, 5618, 5624) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel arrow");
        }
    },
    MITHRIL_ARROW(true, 888, 889, 5619, 5625) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril arrow");
        }
    },
    ADAMANT_ARROW(true, 890, 891, 5620, 5626) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant arrow");
        }
    },
    RUNE_ARROW(true, 892, 893, 5621, 5627) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune arrow");
        }
    },
    DRAGON_ARROW(true, 11212, 11227, 11228, 11229) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Dragon arrow");
        }
    },

    BRONZE_BOLTS(true, 877, 878, 6061, 6062, 879, 9236) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze bolts");
        }
    },
    BLURITE_BOLTS(true, 9139, 9286, 9293, 9300, 9335, 9237) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Blurite bolts");
        }
    },
    IRON_BOLTS(true, 9140, 9287, 9294, 9301, 880, 9238) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron bolts");
        }
    },
    SILVER_BOLTS(true, 9145, 9292, 9299, 9306) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Silver bolts");
        }
    },
    STEEL_BOLTS(true, 9141, 9288, 9295, 9302, 9336, 9239) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel bolts");
        }
    },
    MITHRIL_BOLTS(true, 9142, 9289, 9296, 9303, 9337, 9240, 9338, 9241) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril bolts");
        }
    },
    ADAMANT_BOLTS(true, 9143, 9290, 9297, 9304, 9339, 9242, 9340, 9243) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant bolts");
        }
    },
    RUNITE_BOLTS(true, 9144, 9291, 9298, 9305, 9341, 9244, 9342, 9245) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Runite bolts");
        }
    },

    BRUTAL_BRONZE_ARROW(true, 4773) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bronze brutal");
        }
    },
    BRUTAL_IRON_ARROW(true, 4778) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Iron brutal");
        }
    },
    BRUTAL_BLACK_ARROW(true, 4783) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Steel brutal");
        }
    },
    BRUTAL_STEEL_ARROW(true, 4788) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Black brutal");
        }
    },
    BRUTAL_MITHRIL_ARROW(true, 4793) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Mithril brutal");
        }
    },
    BRUTAL_ADAMANT_ARROW(true, 4798) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Adamant brutal");
        }
    },
    BRUTAL_RUNE_ARROW(true, 4803) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Rune brutal");
        }
    },

    TOKTZ_XIL_UL(true, 6522) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Toktz-xil-ul");
        }
    },
    BONE_BOLT(true, 8882) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bone bolts");
        }
    },
    OGRE_ARROW(true, 2866) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Ogre arrow");
        }
    },
    ICE_ARROW(true, 78) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Ice arrows");
        }
    },

    TRAINING_ARROWS(true, 9706) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Training arrows");
        }
    },
    BOLT_RACK(false, 4740) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Bolt rack");
        }
    },

    BROAD_TIPPED_BOLTS(true, 13280) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Broad-tipped bolts");
        }
    },
    BROAD_ARROW(true, 4160) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Broad arrow");
        }
    },

    CHINCHOMPA(false, 10033) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Chinchompa");
        }
    },
    RED_CHINCHOMPA(false, 10034) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Red chinchompa");
        }
    },

    KEBBIT_BOLT(true, 10158) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Kebbit bolts");
        }
    },
    LONG_KEBBIT_BOLT(true, 10159) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Long kebbit bolts");
        }
    },

    GUAM_TAR(false, 10142) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Guam tar");
        }
    },
    MARRENTIL_TAR(false, 10143) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Marrentill tar");
        }
    },
    TARROMIN_TAR(false, 10144) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Tarromin tar");
        }
    },
    HARRALANDER_TAR(false, 10145) {
        @Override
        public CombatProjectile getProjectile() {
            return getDefinition("Harralander tar");
        }
    };

    private final boolean droppable;
    private final int[] ids;
    private final int[] weapons;

    RangedAmmunition(boolean droppable, int... ids) {
        this.droppable = droppable;
        this.ids = ids;
        this.weapons = new int[0];
    }

    RangedAmmunition(boolean droppable, int[] weapons, int... ids) {
        this.droppable = droppable;
        this.ids = ids;
        this.weapons = weapons;
    }

    private static final RangedAmmunition[] values = values();

    private static final Int2ObjectMap<RangedAmmunition> itemIdToType = new Int2ObjectArrayMap<>(values.length);

    static {
        for (final RangedAmmunition value : values) {
            final int[] ids = value.ids;
            if (ids != null) {
                for (int id : ids) {
                    itemIdToType.putIfAbsent(id, value);
                }
            }
        }
    }

    public static RangedAmmunition forItemId(final int itemId) {
        return itemIdToType.get(itemId);
    }

    public abstract CombatProjectile getProjectile();

    private static int[] darkBows() {
        return new int[] { 11235, 12765, 12766, 12767, 12768 };
    }

    public void sendProjectile(Mob attacker, Mob defender) {
        getProjectile().sendProjectile(attacker, defender);
        switch (this) {
            case DOUBLE_BRONZE_ARROW:
            case DOUBLE_IRON_ARROW:
            case DOUBLE_STEEL_ARROW:
            case DOUBLE_MITHRIL_ARROW:
            case DOUBLE_ADAMANT_ARROW:
            case DOUBLE_RUNE_ARROW:
            case DOUBLE_DRAGON_ARROW:
                Projectile second = getProjectile().getProjectile().get().copy();
                second.setDuration(15 + second.getDuration());
                second.setCurve(32);
                second.send(attacker, defender);
                break;
        }
    }

    public Optional<Graphic> getStart(int id) {
        switch (id) {
            case 9236:
            case 9237:
            case 9238:
            case 9239:
            case 9240:
            case 9241:
            case 9242:
            case 9243:
            case 9244:
            case 9245:
                return getDefinition(new Item(id).getName()).getStart();
        }
        int itemId;
        switch (this) {
            case DOUBLE_BRONZE_ARROW:
                itemId = 1104;
                break;
            case DOUBLE_IRON_ARROW:
                itemId = 1105;
                break;
            case DOUBLE_STEEL_ARROW:
                itemId = 1106;
                break;
            case DOUBLE_MITHRIL_ARROW:
                itemId = 1107;
                break;
            case DOUBLE_ADAMANT_ARROW:
                itemId = 1108;
                break;
            case DOUBLE_RUNE_ARROW:
                itemId = 1109;
                break;
            case DOUBLE_DRAGON_ARROW:
                itemId = 1111;
                break;
            default:
                return getProjectile().getStart();
        }
        return Optional.of(new Graphic(itemId, 0, true, UpdatePriority.HIGH));
    }

    public static RangedAmmunition find(Item weapon, Item item) {
        if (item == null) return null;
        for (RangedAmmunition ammo : values()) {
            if (ammo.weapons.length > 0 && weapon != null) {
                for (int weaponId : ammo.weapons) {
                    if (weaponId == weapon.getId()) {
                        for (int id : ammo.ids) {
                            if (id == item.getId()) {
                                return ammo;
                            }
                        }
                    }
                }
            } else {
                for (int id : ammo.ids) {
                    if (id == item.getId()) {
                        return ammo;
                    }
                }
            }
        }
        return null;
    }

    public Optional<Animation> getAnimation(int id) {
        switch (id) {
            case 9236:
            case 9237:
            case 9238:
            case 9239:
            case 9240:
            case 9241:
            case 9242:
            case 9243:
            case 9244:
            case 9245:
                return getDefinition(new Item(id).getName()).getAnimation();
        }
        return getProjectile().getAnimation();
    }

    public Optional<Graphic> getEnd(int id) {
        switch (id) {
            case 9236:
            case 9237:
            case 9238:
            case 9239:
            case 9240:
            case 9241:
            case 9242:
            case 9243:
            case 9244:
            case 9245:
                return getDefinition(new Item(id).getName()).getEnd();
        }
        return getProjectile().getEnd();
    }

    public Optional<CombatImpact> getEffect(int id) {
        switch (id) {
            case 9236:
            case 9237:
            case 9238:
            case 9239:
            case 9240:
            case 9241:
            case 9242:
            case 9243:
            case 9244:
            case 9245:
                return getDefinition(new Item(id).getName()).getEffect();
        }
        return getProjectile().getEffect();
    }

    public boolean isDroppable() {
        return droppable;
    }

    public boolean isDart() {
        switch (this) {
            case BRONZE_DART:
            case IRON_DART:
            case STEEL_DART:
            case BLACK_DART:
            case MITHRIL_DART:
            case ADAMANT_DART:
            case RUNE_DART:
            case DRAGON_DART:
            return true;
        }
        return false;
    }

    public int getRemoval() {
        switch (this) {
            case DOUBLE_BRONZE_ARROW:
            case DOUBLE_IRON_ARROW:
            case DOUBLE_STEEL_ARROW:
            case DOUBLE_MITHRIL_ARROW:
            case DOUBLE_ADAMANT_ARROW:
            case DOUBLE_RUNE_ARROW:
            case DOUBLE_DRAGON_ARROW:
                return 2;
            default:
                return 1;
        }
    }

}
