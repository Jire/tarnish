package org.jire.tarnishps.kalphitequeen

import com.osroyale.game.Animation
import com.osroyale.game.Graphic
import com.osroyale.game.world.entity.combat.CombatType
import com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray
import com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy
import com.osroyale.game.world.entity.combat.attack.FightType
import com.osroyale.game.world.entity.combat.hit.Hit
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy
import com.osroyale.game.world.entity.mob.Mob
import com.osroyale.game.world.entity.mob.npc.Npc
import com.osroyale.util.RandomUtils
import org.jire.tarnishps.WorldTask.schedule

/**
 * @author Jire
 */
class KalphiteQueen : MultiStrategy() {

    private var transforming = false

    private var switchingStyles = false

    init {
        currentStrategy = melee
    }

    override fun canAttack(attacker: Npc, defender: Mob) =
        super.canAttack(attacker, defender) && !transforming

    override fun canOtherAttack(attacker: Mob, defender: Npc) =
        super.canOtherAttack(attacker, defender) && !transforming

    override fun performChecks(attacker: Npc, defender: Mob) {
        if(RandomUtils.inclusive(2) == 1) {
            val strategies = (if (attacker.id == PHASE1_ID) phase1Strats else phase2Strats)
                .filter {
                    it.canAttack(attacker, defender) && it.withinDistance(attacker, defender)
                }
            val strategy = RandomUtils.random(strategies) ?: return
            currentStrategy = strategy
            switchingStyles = true
        }
        super.performChecks(attacker, defender)
    }

    override fun attack(attacker: Npc?, defender: Mob?, hit: Hit?) {
        switchingStyles = false
        super.attack(attacker, defender, hit)
    }
    override fun block(attacker: Mob, defender: Npc, hit: Hit, combatType: CombatType) {
        val id = defender.id
        if ((id == PHASE1_ID && combatType.match(CombatType.MAGIC, CombatType.RANGED))
            || (id == PHASE2_ID && combatType.match(CombatType.MELEE))
        ) {
            val newDamage = (hit.damage * 0.4).toInt()
            hit.damage = newDamage
            if (newDamage < 1) hit.isAccurate = false
        }
        super.block(attacker, defender, hit, combatType)
    }

    override fun preDeath(attacker: Mob, defender: Npc, hit: Hit) {
        if (defender.getId() == PHASE1_ID) {
            defender.isDead = false
            defender.heal(255)
            attacker.combat.reset()
            defender.combat.reset(true)
            defender.canAttack = false
            defender.animate(6242)
            defender.locking.lock()
            transforming = true
            schedule(5) {
                currentStrategy = randomStrategy(phase2Strats)
                defender.transform(PHASE2_ID)
                defender.canAttack = false
                defender.graphic(PHASE_SWITCH_GFX)
                defender.animate(PHASE_SWITCH_ANIM)
                schedule(8) {
                    transforming = false
                    defender.combat.reset(true)
                    defender.canAttack = true
                    defender.locking.unlock()
                    defender.combat.attack(attacker)
                }
            }
            return
        }

        if (defender.getId() == PHASE2_ID) {
            schedule(8) {
                defender.transform(PHASE1_ID)
            }
        }

        super.preDeath(attacker, defender, hit)
    }

    override fun onDeath(attacker: Mob, defender: Npc, hit: Hit) {
        super.onDeath(attacker, defender, hit)
    }

    override fun getAttackDelay(attacker: Npc?, defender: Mob?, fightType: FightType?): Int {
        return if(switchingStyles) 7 else 4
    }

    companion object {
        const val PHASE1_ID = 963
        const val PHASE2_ID = 965

        val PHASE_SWITCH_GFX = Graphic(1055)
        val PHASE_SWITCH_ANIM = Animation(6270)

        val melee: NpcMeleeStrategy = NpcMeleeStrategy.get()

        val phase1Strats: Array<CombatStrategy<Npc>> =
            createStrategyArray(KalphiteQueenMagicStrategy.phase1, KalphiteQueenRangedStrategy.phase1, melee)
        val phase2Strats: Array<CombatStrategy<Npc>> =
            createStrategyArray(KalphiteQueenMagicStrategy.phase2, KalphiteQueenRangedStrategy.phase2, melee)

    }

}