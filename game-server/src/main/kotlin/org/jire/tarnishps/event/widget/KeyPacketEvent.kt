package org.jire.tarnishps.event.widget

import com.osroyale.game.world.entity.mob.data.PacketType
import com.osroyale.game.world.entity.mob.player.Player
import java.awt.event.KeyEvent

/**
 * @author Jire
 */
class KeyPacketEvent(val key: Int) : WidgetEvent {

    override fun canHandle(player: Player) = key >= 0 && !player.locking.locked(PacketType.KEY)

    override fun handle(player: Player) {
        when (key) {
            KeyEvent.VK_ESCAPE -> if (player.settings.ESC_CLOSE) {
                player.interfaceManager.close()
            }

            KeyEvent.VK_SPACE -> if (player.dialogueFactory.isActive) {
                player.dialogueFactory.execute()
            }

            KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> if (player.dialogueFactory.isActive) {
                if (player.optionDialogue.isPresent) {
                    player.dialogueFactory.executeOption(0, player.optionDialogue)
                    return
                }
            }

            KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> if (player.dialogueFactory.isActive) {
                if (player.optionDialogue.isPresent) {
                    player.dialogueFactory.executeOption(1, player.optionDialogue)
                    return
                }
            }

            KeyEvent.VK_3, KeyEvent.VK_NUMPAD3 -> if (player.dialogueFactory.isActive) {
                if (player.optionDialogue.isPresent) {
                    player.dialogueFactory.executeOption(2, player.optionDialogue)
                    return
                }
            }

            KeyEvent.VK_4, KeyEvent.VK_NUMPAD4 -> if (player.dialogueFactory.isActive) {
                if (player.optionDialogue.isPresent) {
                    player.dialogueFactory.executeOption(3, player.optionDialogue)
                    return
                }
            }

            KeyEvent.VK_5, KeyEvent.VK_NUMPAD5 -> if (player.dialogueFactory.isActive) {
                if (player.optionDialogue.isPresent) {
                    player.dialogueFactory.executeOption(4, player.optionDialogue)
                    return
                }
            }
        }
    }

}