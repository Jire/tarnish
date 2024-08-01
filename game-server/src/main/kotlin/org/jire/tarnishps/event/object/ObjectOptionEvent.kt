package org.jire.tarnishps.event.`object`

import com.osroyale.content.combat.cannon.CannonManager
import com.osroyale.content.event.EventDispatcher
import com.osroyale.content.event.impl.ObjectInteractionEvent
import com.osroyale.game.event.impl.ObjectClickEvent
import com.osroyale.game.plugin.PluginManager
import com.osroyale.game.world.World
import com.osroyale.game.world.entity.mob.data.PacketType
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.game.world.`object`.GameObject
import com.osroyale.game.world.`object`.GameObjectDefinition
import com.osroyale.game.world.position.Position
import org.jire.tarnishps.WorldTask
import java.util.*

/**
 * @author Jire
 */
class ObjectOptionEvent(
    val option: Int,
    val id: Int,
    val x: Int, val y: Int,
    val createInteractionEvent: (GameObject) -> ObjectInteractionEvent?,
) : ObjectEvent {

    override fun canHandle(player: Player) = super.canHandle(player)
            && !player.locking.locked(PacketType.CLICK_OBJECT)

    override fun handle(player: Player) {
        GameObjectDefinition.forId(id) ?: return

        val position = Position(x, y, player.height)

        var objectId = id
        for (playerBirdHouseData in player.birdHouseData) {
            if (playerBirdHouseData.birdhousePosition == position) {
                objectId = playerBirdHouseData.oldObjectId
                break
            }
        }

        /* Dialogues */
        if (player.dialogue.isPresent) {
            player.dialogue = Optional.empty()
        }

        /* Dialogue factory */
        if (!player.dialogueFactory.chain.isEmpty()) {
            player.dialogueFactory.clear()
        }

        /* Dialogue options */
        if (player.optionDialogue.isPresent) {
            player.optionDialogue = Optional.empty()
        }

        if (!player.interfaceManager.isMainClear) {
            player.interfaceManager.close()
        }

        if (!player.interfaceManager.isDialogueClear) {
            player.dialogueFactory.clear()
        }

        val region = World.getRegions().getRegion(position)

        when (objectId) {
            6 -> {
                when(option) {
                    1 -> CannonManager.load(player)
                    2 -> CannonManager.pickup(player)
                    3 -> CannonManager.empty(player)
                }

            };
        }

        val obj = region.getGameObject(objectId, position) ?: region.getCustomObject(objectId, position) ?: return
        handleObj(player, obj)
    }

    private fun handleObj(player: Player, obj: GameObject) = player.walkTo(obj) {
        player.movement.reset()
        player.locking.lock(1)
        player.face(obj)

        WorldTask.schedule {
            player.face(obj)
            if (!EventDispatcher.execute(player, createInteractionEvent(obj))) {
                PluginManager.getDataBus().publish(player, ObjectClickEvent(option, obj))
            }
        }
    }

}