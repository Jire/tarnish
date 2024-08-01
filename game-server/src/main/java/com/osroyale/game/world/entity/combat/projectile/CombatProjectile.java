package com.osroyale.game.world.entity.combat.projectile;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.engine.GameThread;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.mob.Mob;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

public final class CombatProjectile {

    public static final Map<String, CombatProjectile> definitions = new HashMap<>();

    private final String name;
    private final int maxHit;
    private final CombatImpact effect;
    private final Animation animation;
    private final Graphic start;
    private final Graphic end;
    private final Projectile projectile;

    private final int hitDelay;
    private final int hitsplatDelay;

    public CombatProjectile(String name, int maxHit, CombatImpact effect, Animation animation, Graphic start, Graphic end, Projectile projectile, int hitDelay, int hitsplatDelay) {
        this.name = name;
        this.maxHit = maxHit;
        this.effect = effect;
        this.animation = animation;
        this.start = start;
        this.end = end;
        this.projectile = projectile;
        this.hitDelay = hitDelay;
        this.hitsplatDelay = hitsplatDelay;
    }

    public CombatProjectile(String name, int maxHit, CombatImpact effect, Animation animation, Graphic start, Graphic end, Projectile projectile) {
        this(name, maxHit, effect, animation, start, end, projectile, -1, -1);
    }

/*    public static int getProjectileDuration(int distance, int delay) {
        return delay + (distance * 10);
    }

    public static int getProjectileDuration(Position from, Position to, int delay) {
        return getProjectileDuration(from.getChebyshevDistance(to), delay);
    }

    public static int getProjectileDuration(Mob attacker, Mob defender, int delay) {
        return getProjectileDuration(attacker.getPosition(), defender.getPosition(), delay);
    }

    public int getProjectileDelay() {
        return projectile == null ? 0 : projectile.getDelay();
    }*/

/*    public int getProjectileDuration(int distance) {
        return getProjectileDuration(distance, getProjectileDelay());
    }

    public int getProjectileDuration(Position from, Position to) {
        return getProjectileDuration(from, to, getProjectileDelay());
    }

    public int getProjectileDuration(Mob attacker, Mob defender) {
        return getProjectileDuration(attacker, defender, getProjectileDelay());
    }*/

    public int getClientTicks() {
        return projectile == null ? 0 : projectile.getClientTicks();
    }

    public int getServerTicks() {
        return GameThread.clientTicksToServerTicks(getClientTicks());
    }

    public int sendProjectile(Mob attacker, Mob defender) {
        if (projectile == null) return 0;

        /*final Projectile copy = projectile.copy();
        final int duration = getProjectileDuration(attacker, defender);
        copy.setDuration(duration);*/

        final String name = getName();
        if ("Ice Barrage".equals(name) || "Ice Burst".equals(name)
                || "Smoke Barrage".equals(name) || "Smoke Burst".equals(name)) {
            // special case: these are sent from defender pos to defender rather than normal!
            World.sendProjectile(defender.getPosition(), defender, projectile);
        } else {
            projectile.send(attacker, defender);
        }

        return getClientTicks();
    }

    public static CombatProjectile getDefinition(String name) {
        return definitions.get(name);
    }

    public String getName() {
        return name;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public Optional<CombatImpact> getEffect() {
        return Optional.ofNullable(effect);
    }

    public Optional<Animation> getAnimation() {
        return Optional.ofNullable(animation);
    }

    public Optional<Graphic> getStart() {
        return Optional.ofNullable(start);
    }

    public Optional<Graphic> getEnd() {
        return Optional.ofNullable(end);
    }

    public OptionalInt getHitDelay() {
        if (hitDelay == -1) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(hitDelay);
    }

    public OptionalInt getHitsplatDelay() {
        if (hitsplatDelay == -1) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(hitsplatDelay);
    }

    public Optional<Projectile> getProjectile() {
        return Optional.ofNullable(projectile);
    }

    public static final class ProjectileBuilder {
        public short id;
        public byte delay;
        public byte speed;
        public byte startHeight;
        public byte endHeight;
    }

}
