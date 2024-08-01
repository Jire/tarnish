package org.jire.tarnishps.kalphitequeen

import com.osroyale.game.Animation
import com.osroyale.game.Graphic
import com.osroyale.game.Projectile
import com.osroyale.game.world.entity.combat.hit.CombatHit
import com.osroyale.game.world.entity.combat.hit.Hit
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy
import com.osroyale.game.world.entity.mob.Mob
import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.game.world.region.RegionManager

/**
 * @author Jire
 */
internal class KalphiteQueenMagicStrategy(
    anim: Int,
    graphic: Int,
) : NpcMagicStrategy(
    CombatProjectile(
        "KalphiteQueen-MagicStrategy-$anim-$graphic", 31, null,
        Animation(anim), Graphic(graphic), Graphic(281),
        Projectile(280, 70, 90, 60, 43, 16, 128)
    )
) {

    override fun getAttackAnimation(attacker: Npc, defender: Mob): Animation = Animation.RESET

    override fun start(attacker: Npc, defender: Mob, hits: Array<out Hit>) {
        super.start(attacker, defender, hits)
        sendAttack(attacker, defender, hits, true)
    }

    override fun sendProjectile(
        attacker: Npc,
        hits: Array<out Hit>,
        from: Mob,
        to: Mob,
        onProjectileLand: Runnable?
    ) = 0

    private fun sendAttack(attacker: Npc, target: Mob, hits: Array<out Hit>, first: Boolean) {
        super.sendProjectile(attacker, hits, attacker, target) {
            if (!first) target.damage(*hits)
            RegionManager.forNearbyPlayer(target, 1) {
                if (target != it && canAttack(attacker, it)) {
                    sendAttack(attacker, it, hits, false)
                }
            }
        }
    }

    override fun isAlwaysAccurate() = true

    override fun getHits(attacker: Npc, defender: Mob): Array<CombatHit> {
        val hit = nextMagicHit(attacker, defender, combatProjectile)
        hit.isAccurate = true
        return arrayOf(hit)
    }

    companion object {
        val phase1 = KalphiteQueenMagicStrategy(1173, 278)
        val phase2 = KalphiteQueenMagicStrategy(6234, 279)
    }

}