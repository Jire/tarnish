package com.osroyale.game.world.entity.mob.npc.definition;

import com.google.gson.JsonObject;
import com.osroyale.Config;
import com.osroyale.fs.cache.decoder.CacheNpcDefinition;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.parser.GsonParser;
import org.jire.tarnishps.OldToNew;
import org.jire.tarnishps.defs.MonsterDefLoader;

import java.util.*;

import static com.osroyale.game.world.entity.combat.CombatConstants.*;

/**
 * Contains the npc definitions.
 *
 * @author Michael | Chex
 */
public class NpcDefinition {

    /** The array of npc definitions. */
    public static final NpcDefinition[] DEFINITIONS = new NpcDefinition[Config.NPC_DEFINITION_LIMIT + 1];

    private final int id;
    private final String name;
    private int combatLevel;
    private int size;
    private int stand;
    private int walk;
    private int turn180;
    private int turn90CW;
    private int turn90CCW;
    private int attackAnim;
    private int blockAnim;
    private int deathAnim;
    private int deathTimer;
    private boolean attackable;
    private boolean aggressive;
    private boolean retaliate;
    private boolean poisonous;
    private boolean poisonImmunity;
    private boolean venomImmunity;
    private boolean flying;
    private int[] skills;
    private int[] bonuses;
    private int attackDelay;
    private int attackRadius;
    private int respawnTime;
    private CombatAttackData combatAttackData;
    private Player player;

    public NpcDefinition(int id, String name) {
        this.id = id;
        this.name = name;
        this.combatLevel = 0;
        this.size = 1;
        this.stand = -1;
        this.walk = -1;
        this.turn180 = -1;
        this.turn90CW = -1;
        this.turn90CCW = -1;
        this.attackAnim = -1;
        this.blockAnim = -1;
        this.deathAnim = -1;
        this.deathTimer = 3;
        this.flying = false;
        this.attackable = false;
        this.aggressive = false;
        this.retaliate = false;
        this.poisonous = false;
        this.poisonImmunity = false;
        this.venomImmunity = false;
        this.skills = EMPTY_SKILLS;
        this.bonuses = EMPTY_BONUSES;
        this.attackDelay = 4;
        this.attackRadius = 1;
        this.respawnTime = 30;
    }

    public static void main(String[] args) {
        GsonParser parser = createParser();
        parser.deserialize();
    }

    public static GsonParser createParser() {
        return new GsonParser("def/npc/npc_definitions", false) {

            @Override
            protected void parse(JsonObject data) {
                int id = data.get("id").getAsInt();
                boolean convertId = true;
                if (data.has("convert-id")) {
                    convertId = data.get("convert-id").getAsBoolean();
                }
                if (convertId) {
                    int newId = OldToNew.get(id);
                    if (newId != -1) {
                        id = newId;
                    }
                }

                final CacheNpcDefinition cacheDef = CacheNpcDefinition.lookup(id);
                final String cacheDefName = cacheDef.name;
                final NpcDefinition definition = new NpcDefinition(id, cacheDefName == null ? "" : cacheDefName);
                definition.size = cacheDef.size;
                definition.combatLevel = cacheDef.combatLevel;
                definition.stand = cacheDef.standingAnimation;
                definition.walk = cacheDef.walkingAnimation;
                definition.turn180 = cacheDef.halfTurnAnimation;
                definition.turn90CW = cacheDef.quarterClockwiseTurnAnimation;
                definition.turn90CCW = cacheDef.quarterAnticlockwiseTurnAnimation;

                definition.deathTimer = 3;
                if (data.has("death-timer")) {
                    definition.deathTimer = data.get("death-timer").getAsInt();
                }

                if (data.has("attack-animation")) {
                    definition.attackAnim = data.get("attack-animation").getAsInt();
                }

                if (data.has("block-animation")) {
                    definition.blockAnim = data.get("block-animation").getAsInt();
                }

                if (data.has("death-animation")) {
                    definition.deathAnim = data.get("death-animation").getAsInt();
                }

                if (data.has("attackable")) {
                    definition.attackable = data.get("attackable").getAsBoolean();
                }

                if (data.has("aggressive")) {
                    definition.aggressive = data.get("aggressive").getAsBoolean();
                }

                definition.retaliate = definition.isAttackable();
                if (data.has("retaliate")) {
                    definition.retaliate = data.get("retaliate").getAsBoolean();
                }

                if (data.has("poisonous")) {
                    definition.poisonous = data.get("poisonous").getAsBoolean();
                }

                if (data.has("flying")) {
                    definition.flying = data.get("flying").getAsBoolean();
                }

                if (data.has("poison-immunity")) {
                    definition.poisonImmunity = data.get("poison-immunity").getAsBoolean();
                }

                if (data.has("venom-immunity")) {
                    definition.venomImmunity = data.get("venom-immunity").getAsBoolean();
                }

                if (data.has("attack-cooldown")) {
                    definition.attackDelay = data.get("attack-cooldown").getAsInt();
                }

                if (data.has("attack-radius")) {
                    definition.attackRadius = data.get("attack-radius").getAsInt();
                }

                if (data.has("combat-type") && data.has("projectile-key")) {
                    CombatType type = CombatType.valueOf(data.get("combat-type").getAsString());
                    String key = data.get("projectile-key").getAsString();
                    definition.combatAttackData = new CombatAttackData(type, key);
                }

                if (data.has("respawn")) {
                    definition.respawnTime = data.get("respawn").getAsInt();
                }

                for (int index = 0; index < SKILL_FIELD_NAMES.length; index++) {
                    String skillName = SKILL_FIELD_NAMES[index];
                    if (data.has(skillName)) {
                        if (definition.skills == EMPTY_SKILLS) {
                            definition.skills = new int[EMPTY_SKILLS.length];
                        }
                        definition.skills[index] = data.get(skillName).getAsInt();
                    }
                }

                for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
                    String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
                    if (data.has(bonusName)) {
                        if (definition.bonuses == EMPTY_BONUSES) {
                            definition.bonuses = new int[EMPTY_BONUSES.length];
                        }
                        definition.bonuses[index] = data.get(bonusName).getAsInt();
                    }
                }

                DEFINITIONS[id] = definition;
            }

            @Override
            protected void onEnd() {
                MonsterDefLoader.load();
            }

        };
    }

    /** Gets a npc definition from the definition array. */
    public static NpcDefinition get(int id) {
        if (id > Config.NPC_DEFINITION_LIMIT)
            return DEFINITIONS[Config.NPC_DEFINITION_LIMIT];

        if (id < 0)
            id = 0;

        if (DEFINITIONS[id] == null)
            return new NpcDefinition(id, "null");

        return DEFINITIONS[id];
    }

    public static NpcDefinition get(String name) {
        for (NpcDefinition definition : DEFINITIONS) {
            if (definition != null && definition.getName().equals(name)) {
                return definition;
            }
        }
        return null;
    }

    public static String getCombatLevelRange(String name) {
        List<Integer> levels = new ArrayList<>();

        for (NpcDefinition definition : DEFINITIONS) {
            if (definition != null && definition.getName().contains(name)) {
                levels.add(definition.getCombatLevel());
            }
        }

        if (levels.isEmpty()) {
            return "Unknown";
        }

        Collections.sort(levels);

        return levels.get(0) + "-" + levels.get(levels.size() - 1);
    }

    /** @return the id. */
    public int getId() {
        return id;
    }

    /** @return the name. */
    public String getName() {
        return name;
    }

    /** @return the sze. */
    public int getSize() {
        return size;
    }

    /** @return the combat level. */
    public int getCombatLevel() {
        return combatLevel;
    }

    /** @return the stand anim. */
    public int getStand() {
        return stand;
    }

    /** @return the walk anim. */
    public int getWalk() {
        return walk;
    }

    /** @return the turn 180 anim. */
    public int getTurn180() {
        return turn180;
    }

    /** @return the turn 90 CW anim. */
    public int getTurn90CW() {
        return turn90CW;
    }

    /** @return the turn 90 CCW anim. */
    public int getTurn90CCW() {
        return turn90CCW;
    }

    /** @return the attack anim. */
    public int getAttackAnimation() {
        return attackAnim;
    }

    /** @return the block anim. */
    public int getBlockAnimation() {
        return blockAnim;
    }

    /** @return the death anim. */
    public int getDeathAnimation() {
        return deathAnim;
    }

    public int getDeathTimer() {
        return deathTimer;
    }

    /** @return the attackable flag. */
    public boolean isAttackable() {
        return attackable;
    }

    /** @return the aggressive flag. */
    public boolean isAggressive() {
        return aggressive;
    }

    /** @return the retaliate flag. */
    public boolean isRetaliate() {
        return retaliate;
    }

    /** @return the poisonous flag. */
    public boolean isPoisonous() {
        return poisonous;
    }

    /** @return the poison immunity flag. */
    public boolean hasPoisonImmunity() {
        return poisonImmunity;
    }

    /** @return the venom immunity flag. */
    public boolean hasVenomImmunity() {
        return venomImmunity;
    }

    public boolean isFlying() {
        return flying;
    }

    /** @return the attack delay. */
    public int getAttackDelay() {
        return attackDelay;
    }

    /** @return the attack radius. */
    public int getAttackRadius() {
        return attackRadius;
    }

    /** @return the respawn time in ticks. */
    public int getRespawnTime() {
        return respawnTime;
    }

    /** @return the skill level array. */
    public int[] getSkills() {
        return skills;
    }

    /** @return the equipment bonus array. */
    public int[] getBonuses() {
        return bonuses;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public void setRetaliate(boolean retaliate) {
        this.retaliate = retaliate;
    }

    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setStand(int stand) {
        this.stand = stand;
    }

    public void setWalk(int walk) {
        this.walk = walk;
    }

    public void setTurn180(int turn180) {
        this.turn180 = turn180;
    }

    public void setTurn90CW(int turn90CW) {
        this.turn90CW = turn90CW;
    }

    public void setTurn90CCW(int turn90CCW) {
        this.turn90CCW = turn90CCW;
    }

    public int getAttackAnim() {
        return attackAnim;
    }

    public void setAttackAnim(int attackAnim) {
        this.attackAnim = attackAnim;
    }

    public int getBlockAnim() {
        return blockAnim;
    }

    public void setBlockAnim(int blockAnim) {
        this.blockAnim = blockAnim;
    }

    public int getDeathAnim() {
        return deathAnim;
    }

    public void setDeathAnim(int deathAnim) {
        this.deathAnim = deathAnim;
    }

    public void setDeathTimer(int deathTimer) {
        this.deathTimer = deathTimer;
    }

    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    public void setPoisonous(boolean poisonous) {
        this.poisonous = poisonous;
    }

    public boolean isPoisonImmunity() {
        return poisonImmunity;
    }

    public void setPoisonImmunity(boolean poisonImmunity) {
        this.poisonImmunity = poisonImmunity;
    }

    public boolean isVenomImmunity() {
        return venomImmunity;
    }

    public void setVenomImmunity(boolean venomImmunity) {
        this.venomImmunity = venomImmunity;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public void setSkills(int[] skills) {
        this.skills = skills;
    }

    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public void setAttackRadius(int attackRadius) {
        this.attackRadius = attackRadius;
    }

    public void setCombatAttackData(CombatAttackData combatAttackData) {
        this.combatAttackData = combatAttackData;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Optional<CombatAttackData> getCombatAttackData() {
        return Optional.ofNullable(combatAttackData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof NpcDefinition && ((NpcDefinition) obj).id == id);
    }

    public static final class CombatAttackData {

        public final CombatType type;

        public final String key;

        public CombatAttackData(CombatType type, String key) {
            this.type = type;
            this.key = key;
        }

        public CombatProjectile getDefinition() {
            return CombatProjectile.getDefinition(key);
        }
    }
}
