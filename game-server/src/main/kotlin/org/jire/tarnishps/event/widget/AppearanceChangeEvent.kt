package org.jire.tarnishps.event.widget

import com.osroyale.content.achievement.AchievementHandler
import com.osroyale.content.achievement.AchievementKey
import com.osroyale.game.world.entity.mob.UpdateFlag
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.game.world.entity.mob.player.appearance.Appearance
import com.osroyale.game.world.entity.mob.player.appearance.Gender
import com.osroyale.net.packet.`in`.AppearanceChangePacketListener

/**
 * @author Jire
 */
class AppearanceChangeEvent(
    val gender: Int,
    val head: Int,
    val jaw: Int,
    val torso: Int,
    val arms: Int,
    val hands: Int,
    val legs: Int,
    val feet: Int,
    val hairColor: Int,
    val torsoColor: Int,
    val legsColor: Int,
    val feetColor: Int,
    val skinColor: Int
) : WidgetEvent {

    override fun handle(player: Player) {
        val appearance = Appearance(
            if (gender == 0) Gender.MALE else Gender.FEMALE,
            head,
            if (gender == 0) jaw else -1,
            torso,
            arms,
            hands,
            legs,
            feet,
            hairColor,
            torsoColor,
            legsColor,
            feetColor,
            skinColor
        )
        if (AppearanceChangePacketListener.isValid(player, appearance)) {
            player.appearance = appearance
            player.updateFlags.add(UpdateFlag.APPEARANCE)
            player.interfaceManager.close()

            AchievementHandler.activate(player, AchievementKey.CHANGE_APPEARANCE, 1)
        }
    }

}