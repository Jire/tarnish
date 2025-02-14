package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

/**
 * The DinoMaster boss that features three attack styles and a unique eye-hitting kill mechanic.
 * <p>
 * Attack Styles:
 * 1. Claw Slash - A powerful melee attack against a single target
 * 2. Tail Smash - AoE attack that knocks back nearby players
 * 3. Gnarling Roar - Magic attack that drains stats and applies movement debuff
 * <p>
 * Unique Kill Mechanic:
 * - Players must hit the boss's eye 10 times to defeat it, regardless of remaining health
 * - Each successful hit has a 10% chance to count as an eye hit
 * <p>
 * Created by Pradip Puri on 2025/02/14.
 */
public class DinoMaster extends MultiStrategy {
    // TODO: Replace proper animation IDs and graphic id used by the boss
    private static final int CLAW_SLASH_ANIM = 4500;
    private static final int TAIL_SMASH_ANIM = 4501;
    private static final int GNARLING_ROAR_ANIM = 4502;
    private static final int ROAR_GRAPHIC = 1248;

    // Constants for attack strategies
    private static final ClawSlashAttack CLAW_SLASH = new ClawSlashAttack();
    private static final TailSmashAttack TAIL_SMASH = new TailSmashAttack();
    private static final GnarlingRoarAttack GNARLING_ROAR = new GnarlingRoarAttack();
    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(CLAW_SLASH, TAIL_SMASH, GNARLING_ROAR);

    // Attribute key for tracking eye hits
    private static final String DINO_EYE_HITS = "DINO_EYE_HITS";
    private static final int REQUIRED_EYE_HITS = 10;

    // Tracks individual player contributions to eye hits
    private final Map<Player, Integer> eyeHitCounter = new HashMap<>();
    private boolean hasRoared = false;

    public DinoMaster() {
        currentStrategy = CLAW_SLASH;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        // Initialize eye hit counter if not present
        if (!attacker.attributes.has(DINO_EYE_HITS)) {
            attacker.attributes.set(DINO_EYE_HITS, 0);
        }

        // AI Strategy Selection:

        // 1. If surrounded by 3+ players, use tail smash
        List<Player> surroundingPlayers = getSurroundingPlayers(attacker);
        if (surroundingPlayers.size() >= 3) {
            currentStrategy = TAIL_SMASH;
            return true;
        }

        // 2. If at 50% health and hasn't roared yet, use gnarling roar
        if (attacker.getCurrentHealth() <= attacker.getMaximumHealth() / 2 && !hasRoared) {
            currentStrategy = GNARLING_ROAR;
            hasRoared = true;
            return true;
        }

        // 3. Choose attack based on distance to target
        if (defender.isPlayer()) {
            int distance = (int) attacker.getPosition().getDistance(defender.getPosition());
            if (distance <= 1) {
                currentStrategy = CLAW_SLASH;
            } else {
                currentStrategy = GNARLING_ROAR;
            }
        } else {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        }

        return currentStrategy.canAttack(attacker, defender);
    }

    /**
     * Gets a list of players that are directly adjacent to the attacker.
     * Used to determine when to use the Tail Smash attack.
     *
     * @param attacker The DinoMaster NPC
     * @return List of players within 1 tile of the attacker
     */
    private List<Player> getSurroundingPlayers(Npc attacker) {
        return World.getRegions().getLocalPlayers(attacker)
                .stream()
                .filter(p -> p.getPosition().isWithinDistance(attacker.getPosition(), 1))
                .toList();
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        super.finishOutgoing(attacker, defender);

        // Check if the boss should be defeated based on eye hits
        int eyeHits = attacker.attributes.get(DINO_EYE_HITS, Integer.class);
        if (eyeHits >= REQUIRED_EYE_HITS) {
            // Defeat the boss regardless of remaining health
            attacker.writeDamage(new Hit(attacker.getCurrentHealth()));
            attacker.speak("ROOOOAAAR! My eye! I'm finished!");
            // Reset for next spawn
            attacker.attributes.set(DINO_EYE_HITS, 0);
            eyeHitCounter.clear();
            hasRoared = false;
        }
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        super.block(attacker, defender, hit, combatType);

        // Process potential eye hits from players
        if (attacker.isPlayer()) {
            Player player = attacker.getPlayer();

            // 10% chance to hit the eye if hit is accurate and deals damage
            if (Utility.random(1, 10) == 1 && hit.isAccurate() && hit.getDamage() > 0) {
                int currentEyeHits = defender.attributes.get(DINO_EYE_HITS, Integer.class);
                defender.attributes.set(DINO_EYE_HITS, currentEyeHits + 1);

                // Track individual player contributions
                eyeHitCounter.put(player, eyeHitCounter.getOrDefault(player, 0) + 1);

                // Feedback to the player
                player.message(String.format("You struck the DinoMaster's eye! %d more hits needed to defeat it!",
                        REQUIRED_EYE_HITS - (currentEyeHits + 1)));
                defender.speak("Argh! My eye!");
            }
        }
    }

    /**
     * Powerful melee attack that deals high single-target damage.
     */
    private static class ClawSlashAttack extends NpcMeleeStrategy {
        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(CLAW_SLASH_ANIM, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMeleeHit(attacker, defender, 25);
            return new CombatHit[]{hit};
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("SLASH!");
            super.start(attacker, defender, hits);
        }
    }

    /**
     * AoE attack that knocks back nearby players and deals damage to all targets within range.
     */
    private static class TailSmashAttack extends NpcMeleeStrategy {
        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(TAIL_SMASH_ANIM, UpdatePriority.HIGH);
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("SMASH!");
            attacker.animate(getAttackAnimation(attacker, defender));

            // Get all players in range (2 tiles) for AoE effect
            List<Player> targets = World.getRegions().getLocalPlayers(attacker)
                    .stream()
                    .filter(p -> p.getPosition().isWithinDistance(attacker.getPosition(), 2))
                    .toList();

            // Apply knockback and damage to all nearby players
            for (Player target : targets) {
                // Calculate knockback direction (away from dino)
                Position knockbackPos = calculateKnockbackPosition(attacker, target);

                // Apply damage
                target.writeDamage(new Hit(Utility.random(15, 22)));

                // Apply knock-back if position is valid and within game boundaries
                if (isValidPosition(target, knockbackPos)) {
                    target.movement.reset();
                    target.walk(knockbackPos, true);
                    target.message("You've been knocked back by the DinoMaster's tail!");
                }
            }
        }

        /**
         * Checks if the given position is valid for player movement.
         * This is a basic check that should ideally use the game's collision map.
         *
         * @param player The player being moved
         * @param position The target position to check
         * @return true if the position appears valid
         */
        private boolean isValidPosition(Player player, Position position) {
            // Basic check to see if position is within reasonable game bounds
            //TODO:Implement game collision map here.
            return position.getX() >= 0 && position.getX() < 4000 &&
                    position.getY() >= 0 && position.getY() < 4000 &&
                    position.getHeight() == player.getHeight();
        }

        /**
         * Calculates the position where a player should be knocked back to.
         * Knocks the player back 2 tiles directly away from the attacker.
         *
         * @param attacker The DinoMaster NPC
         * @param target The player being knocked back
         * @return The calculated position for knockback
         */
        private Position calculateKnockbackPosition(Npc attacker, Player target) {
            int dx = target.getX() - attacker.getX();
            int dy = target.getY() - attacker.getY();

            // Normalize direction
            int distance = 2; // Knock back 2 tiles
            if (dx != 0 || dy != 0) {
                double magnitude = Math.sqrt(dx * dx + (double)dy * dy);
                dx = (int) Math.round(dx / magnitude * distance);
                dy = (int) Math.round(dy / magnitude * distance);
            } else {
                // If somehow on same tile, knock back in random direction
                dx = Utility.random(-1, 1) * distance;
                dy = Utility.random(-1, 1) * distance;
            }

            return new Position(target.getX() + dx, target.getY() + dy, target.getHeight());
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            // Base damage for the primary target
            CombatHit hit = nextMeleeHit(attacker, defender, 22);
            return new CombatHit[]{hit};
        }
    }

    /**
     * Magic attack that drains combat stats and applies a movement debuff.
     * Triggered at 50% health for the first time.
     */
    private static class GnarlingRoarAttack extends NpcMagicStrategy {
        GnarlingRoarAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(GNARLING_ROAR_ANIM, UpdatePriority.HIGH);
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.speak("ROOOOAAARRR!");
            attacker.animate(getAttackAnimation(attacker, defender));
            World.sendGraphic(new Graphic(ROAR_GRAPHIC), attacker.getPosition(), attacker.instance);

            if (defender.isPlayer()) {
                Player player = defender.getPlayer();

                // Drain some combat stats
                for (int skill : new int[]{Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.RANGED, Skill.MAGIC}) {
                    Skill playerSkill = player.skills.get(skill);
                    playerSkill.modifyLevel(level -> level - 3, 0, playerSkill.getLevel());
                }
                player.skills.refresh();
                player.message("The DinoMaster's roar drains your combat abilities!");

                // Apply movement debuff by marking player with an attribute
                player.attributes.set("DINO_ROAR_DEBUFF", Boolean.TRUE);

                // Schedule recovery after 15 seconds
                World.schedule(new Task(25) {
                    @Override
                    public void execute() {
                        if (!player.isDead() && player.isValid()) {
                            player.attributes.remove("DINO_ROAR_DEBUFF");
                            player.message("You've recovered from the DinoMaster's roar!");
                            cancel();
                        }
                    }
                });
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 15);
            hit.setIcon(HitIcon.MAGIC);
            return new CombatHit[]{hit};
        }
    }
}
