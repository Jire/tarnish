package com.osroyale.game.world.entity.combat.magic;

import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.items.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public enum CombatSpell {
    WIND_STRIKE(Spellbook.MODERN, 1152, 1, 5.5f, null,
            getDefinition("Wind Strike"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 1),
                    new RequiredRune(MagicRune.MIND_RUNE, 1)
            }
    ),

    WATER_STRIKE(Spellbook.MODERN, 1154, 5, 7.5f, null,
            getDefinition("Water Strike"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 1),
                    new RequiredRune(MagicRune.MIND_RUNE, 1),
                    new RequiredRune(MagicRune.WATER_RUNE, 1)
            }
    ),

    EARTH_STRIKE(Spellbook.MODERN, 1156, 9, 9.5f, null,
            getDefinition("Earth Strike"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 1),
                    new RequiredRune(MagicRune.MIND_RUNE, 1),
                    new RequiredRune(MagicRune.EARTH_RUNE, 2)
            }
    ),

    FIRE_STRIKE(Spellbook.MODERN, 1158, 13, 11.5f, null,
            getDefinition("Fire Strike"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 2),
                    new RequiredRune(MagicRune.MIND_RUNE, 1),
                    new RequiredRune(MagicRune.FIRE_RUNE, 3)
            }
    ),

    WIND_BOLT(Spellbook.MODERN, 1160, 17, 13.5f, null,
            getDefinition("Wind Bolt"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 1)
            }
    ),

    WATER_BOLT(Spellbook.MODERN, 1163, 23, 16.5f, null,
            getDefinition("Water Bolt"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 1),
                    new RequiredRune(MagicRune.WATER_RUNE, 2)
            }
    ),

    EARTH_BOLT(Spellbook.MODERN, 1166, 29, 19.5f, null,
            getDefinition("Earth Bolt"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 1),
                    new RequiredRune(MagicRune.EARTH_RUNE, 3)
            }
    ),

    FIRE_BOLT(Spellbook.MODERN, 1169, 35, 22.5f, null,
            getDefinition("Fire Bolt"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 3),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 1),
                    new RequiredRune(MagicRune.FIRE_RUNE, 4)
            }
    ),

    CRUMBLE_UNDEAD(Spellbook.MODERN, 1171, 39, 24.0f, null,
            getDefinition("Crumble Undead"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.EARTH_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 1)
            }),

    WIND_BLAST(Spellbook.MODERN, 1172, 41, 25.5f, null,
            getDefinition("Wind Blast"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 3),
                    new RequiredRune(MagicRune.DEATH_RUNE, 1)
            }
    ),

    WATER_BLAST(Spellbook.MODERN, 1175, 47, 28.5f, null,
            getDefinition("Water Blast"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 3),
                    new RequiredRune(MagicRune.WATER_RUNE, 3),
                    new RequiredRune(MagicRune.DEATH_RUNE, 1)
            }
    ),

    EARTH_BLAST(Spellbook.MODERN, 1177, 53, 31.5f, null,
            getDefinition("Earth Blast"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 3),
                    new RequiredRune(MagicRune.EARTH_RUNE, 4),
                    new RequiredRune(MagicRune.DEATH_RUNE, 1)
            }
    ),

    FIRE_BLAST(Spellbook.MODERN, 1181, 59, 34.5f, null,
            getDefinition("Fire Blast"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 4),
                    new RequiredRune(MagicRune.FIRE_RUNE, 5),
                    new RequiredRune(MagicRune.DEATH_RUNE, 1)
            }
    ),

    WIND_WAVE(Spellbook.MODERN, 1183, 62, 36.0f, null,
            getDefinition("Wind Wave"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 5),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 1)
            }
    ),

    WATER_WAVE(Spellbook.MODERN, 1185, 65, 37.5f, null,
            getDefinition("Water Wave"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 5),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 1),
                    new RequiredRune(MagicRune.WATER_RUNE, 7)
            }
    ),

    EARTH_WAVE(Spellbook.MODERN, 1188, 70, 40.0f, null,
            getDefinition("Earth Wave"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 5),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 1),
                    new RequiredRune(MagicRune.EARTH_RUNE, 7)
            }
    ),

    FIRE_WAVE(Spellbook.MODERN, 1189, 75, 42.5f, null,
            getDefinition("Fire Wave"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 5),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 1),
                    new RequiredRune(MagicRune.FIRE_RUNE, 7)
            }
    ),

    SARADOMIN_STRIKE(Spellbook.MODERN, 1190, 60, 20.0f, null,
            getDefinition("Saradomin Strike"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.FIRE_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 4)
            }
    ),

    CLAWS_OF_GUTHIX(Spellbook.MODERN, 1191, 60, 20.0f, null,
            getDefinition("Claws of Guthix"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.FIRE_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 4)
            }
    ),

    FLAMES_OF_ZAMORAK(Spellbook.MODERN, 1192, 60, 20.0f, null,
            getDefinition("Flames of Zamorak"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.FIRE_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 4)
            }
    ),

    BIND(Spellbook.MODERN, 1572, 20, 30.0f, null,
            getDefinition("Bind"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.NATURE_RUNE, 2),
                    new RequiredRune(MagicRune.EARTH_RUNE, 3),
                    new RequiredRune(MagicRune.WATER_RUNE, 3)
            }
    ),

    IBAN_BLAST(Spellbook.MODERN, 1539, 50, 30.0f, new Item(1409),
            getDefinition("Iban Blast"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.FIRE_RUNE, 5),
                    new RequiredRune(MagicRune.DEATH_RUNE, 1)
            }
    ),

    MAGIC_DART(Spellbook.MODERN, 12037, 50, 30.0f, new Item(4170),
            getDefinition("Magic Dart"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 1),
                    new RequiredRune(MagicRune.MIND_RUNE, 4),
            }
    ),

    SNARE(Spellbook.MODERN, 1582, 50, 60.0f, null,
            getDefinition("Snare"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.NATURE_RUNE, 3),
                    new RequiredRune(MagicRune.EARTH_RUNE, 4),
                    new RequiredRune(MagicRune.WATER_RUNE, 4)
            }
    ),

    ENTANGLE(Spellbook.MODERN, 1592, 79, 89.0f, null,
            getDefinition("Entangle"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.NATURE_RUNE, 4),
                    new RequiredRune(MagicRune.EARTH_RUNE, 5),
                    new RequiredRune(MagicRune.WATER_RUNE, 5)
            }
    ),

    CONFUSE(Spellbook.MODERN, 1153, 3, 13.0f, null,
            getDefinition("Confuse"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 3),
                    new RequiredRune(MagicRune.EARTH_RUNE, 2),
                    new RequiredRune(MagicRune.BODY_RUNE, 1)
            }
    ),

    WEAKEN(Spellbook.MODERN, 1157, 11, 20.5f, null,
            getDefinition("Weaken"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 3),
                    new RequiredRune(MagicRune.EARTH_RUNE, 2),
                    new RequiredRune(MagicRune.BODY_RUNE, 1)
            }
    ),

    CURSE(Spellbook.MODERN, 1161, 19, 29.0f, null,
            getDefinition("Curse"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 2),
                    new RequiredRune(MagicRune.EARTH_RUNE, 3),
                    new RequiredRune(MagicRune.BODY_RUNE, 1)
            }
    ),

    VULNERABILITY(Spellbook.MODERN, 1542, 66, 76.0f, null,
            getDefinition("Vulnerability"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 5),
                    new RequiredRune(MagicRune.EARTH_RUNE, 5),
                    new RequiredRune(MagicRune.SOUL_RUNE, 1)
            }
    ),

    ENFEEBLE(Spellbook.MODERN, 1543, 73, 83.0f, null,
            getDefinition("Enfeeble"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 8),
                    new RequiredRune(MagicRune.EARTH_RUNE, 8),
                    new RequiredRune(MagicRune.SOUL_RUNE, 1)
            }
    ),

    STUN(Spellbook.MODERN, 1562, 73, 90.0f, null,
            getDefinition("Stun"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 12),
                    new RequiredRune(MagicRune.EARTH_RUNE, 12),
                    new RequiredRune(MagicRune.SOUL_RUNE, 1)
            }
    ),

    TELE_BLOCK(Spellbook.MODERN, 12445, 85, 42.5f, null,
            getDefinition("Teleblock"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.LAW_RUNE, 1),
                    new RequiredRune(MagicRune.DEATH_RUNE, 1),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 1)
            }
    ),

    WIND_SURGE(Spellbook.MODERN, 40140, 82, 44.5f, null,
            getDefinition("Wind Surge"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 7),
                    new RequiredRune(MagicRune.WRATH_RUNE, 1)
            }
    ),

    WATER_SURGE(Spellbook.MODERN, 40150, 85, 46.5f, null,
            getDefinition("Water Surge"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 7),
                    new RequiredRune(MagicRune.WATER_RUNE, 10),
                    new RequiredRune(MagicRune.WRATH_RUNE, 1)
            }
    ),

    EARTH_SURGE(Spellbook.MODERN, 40170, 90, 48.5f, null,
            getDefinition("Earth Surge"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.AIR_RUNE, 7),
                    new RequiredRune(MagicRune.EARTH_RUNE, 10),
                    new RequiredRune(MagicRune.WRATH_RUNE, 1)
            }
    ),

    FIRE_SURGE(Spellbook.MODERN, 40190, 95, 50.5f, null,
            getDefinition("Fire Surge"),
            new RequiredRune[] {
                    new RequiredRune(MagicRune.AIR_RUNE, 7),
                    new RequiredRune(MagicRune.FIRE_RUNE, 10),
                    new RequiredRune(MagicRune.WRATH_RUNE, 1)
            }
    ),

    ICE_RUSH(Spellbook.ANCIENT, 12861, 58, 34.0f, null,
            getDefinition("Ice Rush"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 2),
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 2)
            }
    ),

    ICE_BLITZ(Spellbook.ANCIENT, 12871, 82, 46.0f, null,
            getDefinition("Ice Blitz"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 3),
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2)
            }
    ),

    ICE_BURST(Spellbook.ANCIENT, 12881, 70, 40.0f, null,
            getDefinition("Ice Burst"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.WATER_RUNE, 4),
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 4)
            }
    ),

    ICE_BARRAGE(Spellbook.ANCIENT, 12891, 94, 52.0f, null,
            getDefinition("Ice Barrage"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 4),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.WATER_RUNE, 6)
            }
    ),

    BLOOD_RUSH(Spellbook.ANCIENT, 12901, 56, 33.0f, null,
            getDefinition("Blood Rush"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.BLOOD_RUNE, 1),
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 2)
            }
    ),

    BLOOD_BLITZ(Spellbook.ANCIENT, 12911, 80, 45.0f, null,
            getDefinition("Blood Blitz"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 4)
            }
    ),

    BLOOD_BURST(Spellbook.ANCIENT, 12919, 68, 39.0f, null,
            getDefinition("Blood Burst"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 4)
            }
    ),

    BLOOD_BARRAGE(Spellbook.ANCIENT, 12929, 92, 51.0f, null,
            getDefinition("Blood Barrage"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 4),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 4),
                    new RequiredRune(MagicRune.SOUL_RUNE, 1)
            }
    ),

    SMOKE_RUSH(Spellbook.ANCIENT, 12939, 50, 30.0f, null,
            getDefinition("Smoke Rush"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 2),
                    new RequiredRune(MagicRune.FIRE_RUNE, 1),
                    new RequiredRune(MagicRune.AIR_RUNE, 1)
            }
    ),

    SMOKE_BLITZ(Spellbook.ANCIENT, 12951, 74, 42.0f, null,
            getDefinition("Smoke Blitz"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.FIRE_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 2)
            }
    ),

    SMOKE_BURST(Spellbook.ANCIENT, 12963, 62, 36.0f, null,
            getDefinition("Smoke Burst"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 4),
                    new RequiredRune(MagicRune.FIRE_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 2)
            }
    ),

    SMOKE_BARRAGE(Spellbook.ANCIENT, 12975, 86, 48.0f, null,
            getDefinition("Smoke Barrage"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 4),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.FIRE_RUNE, 4),
                    new RequiredRune(MagicRune.AIR_RUNE, 4)
            }
    ),

    SHADOW_RUSH(Spellbook.ANCIENT, 12987, 52, 31.0f, null,
            getDefinition("Shadow Rush"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 2),
                    new RequiredRune(MagicRune.SOUL_RUNE, 1),
                    new RequiredRune(MagicRune.AIR_RUNE, 1)
            }
    ),

    SHADOW_BLITZ(Spellbook.ANCIENT, 12999, 76, 43.0f, null,
            getDefinition("Shadow Blitz"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.SOUL_RUNE, 2),
                    new RequiredRune(MagicRune.AIR_RUNE, 2)
            }
    ),

    SHADOW_BURST(Spellbook.ANCIENT, 13011, 64, 37.0f, null,
            getDefinition("Shadow Burst"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.SOUL_RUNE, 2),
                    new RequiredRune(MagicRune.DEATH_RUNE, 2),
                    new RequiredRune(MagicRune.CHAOS_RUNE, 4),
                    new RequiredRune(MagicRune.AIR_RUNE, 1)
            }
    ),

    SHADOW_BARRAGE(Spellbook.ANCIENT, 13023, 88, 49.0f, null,
            getDefinition("Shadow Barrage"),
            new RequiredRune[]{
                    new RequiredRune(MagicRune.SOUL_RUNE, 3),
                    new RequiredRune(MagicRune.BLOOD_RUNE, 2),
                    new RequiredRune(MagicRune.DEATH_RUNE, 4),
                    new RequiredRune(MagicRune.AIR_RUNE, 4)
            }
    );

    private final int id;
    private final MagicSpell spell;
    private final CombatProjectile combatProjectile;
    private final Spellbook spellbook;

    private final static Map<Integer, CombatSpell> SPELLS;

    static {
        CombatSpell[] spells = CombatSpell.values();
        SPELLS = new HashMap<>(spells.length);

        for (CombatSpell spell : spells) {
            SPELLS.put(spell.id, spell);
        }
    }

    CombatSpell(Spellbook spellbook, int id, int level, float baseExperience, Item weapon, CombatProjectile combatProjectile, RequiredRune[] runes) {
        this.spellbook = spellbook;
        this.id = id;
        this.spell = new MagicSpell(level, baseExperience, runes) {
            @Override
            public Optional<Item[]> equipmentRequired() {
                return weapon == null ? Optional.empty() : Optional.of(new Item[]{weapon});
            }
        };
        this.combatProjectile = combatProjectile;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return combatProjectile.getName();
    }

    public int getLevel() {
        return spell.level;
    }

    public CombatProjectile getCombatProjectile() {
        return combatProjectile == null ? null : CombatProjectile.getDefinition(combatProjectile.getName());
    }

    public double getBaseExperience() {
        return spell.baseExperience;
    }

    public Optional<CombatImpact> getEffect() {
        return combatProjectile.getEffect();
    }

    public RequiredRune[] getRunes() {
        return spell.runes;
    }

    public Optional<Animation> getAnimation() {
        return CombatProjectile.getDefinition(combatProjectile.getName()).getAnimation();//combatProjectile.getAnimation();
    }

    public Optional<Graphic> getStart() {
        return CombatProjectile.getDefinition(combatProjectile.getName()).getStart();//combatProjectile.getStart();
    }

    public Optional<Graphic> getEnd() {
        return CombatProjectile.getDefinition(combatProjectile.getName()).getEnd();//combatProjectile.getEnd();
    }

    public Spellbook getSpellbook() {
        return spellbook;
    }

    public int sendProjectile(Mob attacker, Mob defender) {
        final CombatProjectile combatProjectile = getCombatProjectile();
        return combatProjectile == null ? 0 : combatProjectile.sendProjectile(attacker, defender);
    }

    public boolean canCast(Mob attacker, Mob defender) {
        return spell.canCast(attacker, Optional.ofNullable(defender));
    }

    public static CombatSpell get(int id) {
        return SPELLS.getOrDefault(id, WIND_STRIKE);
    }

}
