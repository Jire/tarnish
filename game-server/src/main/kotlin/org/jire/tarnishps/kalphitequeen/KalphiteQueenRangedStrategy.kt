package org.jire.tarnishps.kalphitequeen

import com.osroyale.game.Animation
import com.osroyale.game.Projectile
import com.osroyale.game.world.entity.combat.hit.CombatHit
import com.osroyale.game.world.entity.combat.hit.Hit
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy
import com.osroyale.game.world.entity.mob.Mob
import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.game.world.entity.skill.Skill
import com.osroyale.game.world.region.RegionManager

/**
 * @author Jire
 */
internal class KalphiteQueenRangedStrategy(anim: Int, proj: Int) : NpcRangedStrategy(
    CombatProjectile(
        "KalphiteQueen-RangedStrategy-$anim-$proj", 31, null,
        Animation(anim), null, null,
        Projectile(proj, 30, 60, 25, 0, 16, 128)
    )
) {

    private val attackAnimation = Animation(anim)

    override fun getAttackAnimation(attacker: Npc, defender: Mob): Animation = attackAnimation

    override fun start(attacker: Npc, defender: Mob, hits: Array<out Hit>) {
        super.start(attacker, defender, hits)
        sendAttack(attacker, defender, hits, true)
    }

    override fun sendProjectile(from: Mob, to: Mob, onProjectileLand: Runnable?) = 0

    private fun sendAttack(attacker: Npc, target: Mob, hits: Array<out Hit>, first: Boolean) {
        super.sendProjectile(attacker, target) {
            if (!first) target.damage(*hits)
            if (target is Player && hits.any { it.isAccurate }) {
                target.skills.get(Skill.PRAYER).modifyLevel { it - 1 }
                target.skills.refresh(Skill.PRAYER)
                //target.message("Your prayer has been drained!")
            }
            RegionManager.forNearbyPlayer(target, 1) {
                if (target != it && canAttack(attacker, it)) {
                    sendAttack(attacker, it, hits, false)
                }
            }
        }
    }

    override fun isAlwaysAccurate() = true

    override fun getHits(attacker: Npc?, defender: Mob?): Array<CombatHit> {
        val hit = nextRangedHit(attacker, defender, 31, combatProjectile)
        hit.isAccurate = true
        return arrayOf(hit)
    }

    companion object {
        val phase1 = KalphiteQueenRangedStrategy(6240, 288)
        val phase2 = KalphiteQueenRangedStrategy(6234, 289)
    }

}