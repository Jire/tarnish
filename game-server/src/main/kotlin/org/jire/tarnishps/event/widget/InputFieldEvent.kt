package org.jire.tarnishps.event.widget

import com.osroyale.content.DropDisplay
import com.osroyale.content.DropDisplay.DropType
import com.osroyale.content.ProfileViewer
import com.osroyale.content.clanchannel.channel.ClanChannel
import com.osroyale.content.famehall.FameHandler
import com.osroyale.content.simulator.DropSimulator
import com.osroyale.content.store.impl.PersonalStore
import com.osroyale.game.world.World
import com.osroyale.game.world.entity.mob.player.Player
import com.osroyale.game.world.entity.mob.player.PlayerRight
import com.osroyale.net.packet.out.SendMessage
import com.osroyale.util.MessageColor

/**
 * @author Jire
 */
class InputFieldEvent(
    val component: Int,
    val context: String
) : WidgetEvent {

    override fun handle(player: Player) {
        if (component < 0) return

        if (PlayerRight.isDeveloper(player)) {
            player.send(
                SendMessage(
                    "[InputField] - Text: $context Component: $component",
                    MessageColor.DEVELOPER
                )
            )
        }

        when (component) {
            42102 -> player.forClan { clan: ClanChannel ->
                if (clan.canManage(clan.getMember(player.name).orElse(null))) {
                    clan.setName(player, context)
                }
            }

            42104 -> player.forClan { clan: ClanChannel ->
                if (clan.canManage(clan.getMember(player.name).orElse(null))) {
                    clan.setTag(player, context)
                }
            }

            42106 -> {
                player.forClan { clan: ClanChannel ->
                    if (clan.canManage(clan.getMember(player.name).orElse(null))) {
                        clan.setSlogan(player, context)
                    }
                }
                run {
                    player.forClan { clan: ClanChannel ->
                        if (clan.canManage(clan.getMember(player.name).orElse(null))) {
                            clan.management.password = context
                            if (context.isEmpty()) {
                                player.message("Your clan will no longer use a password.")
                            } else {
                                player.message("The new clan password is: $context.")
                            }
                        }
                    }
                }
            }

            42108 -> {
                player.forClan { clan: ClanChannel ->
                    if (clan.canManage(clan.getMember(player.name).orElse(null))) {
                        clan.management.password = context
                        if (context.isEmpty()) {
                            player.message("Your clan will no longer use a password.")
                        } else {
                            player.message("The new clan password is: $context.")
                        }
                    }
                }
            }

            38307 -> PersonalStore.changeName(player, context, false)
            38309 -> PersonalStore.changeName(player, context, true)
            26810 -> DropSimulator.drawList(player, context)
            48508 -> player.priceChecker.searchItem(context)
            58506 -> FameHandler.search(player, context)
            57021 -> player.presetManager.name(context)
            54506 -> DropDisplay.search(player, context, DropType.ITEM)
            54507 -> DropDisplay.search(player, context, DropType.NPC)
            353 -> {
                if (World.search(context).isPresent) {
                    ProfileViewer.open(player, World.search(context).get())
                    return
                }
                player.send(SendMessage("You can not view $context's profile as they are currently offline."))
            }

            354 -> if (PlayerRight.isModerator(player)) {
                if (World.search(context).isPresent) {
                    //StaffPanel.search(player, context);
                    return
                }
                player.send(SendMessage("You can not manage $context as they are currently offline."))
            }
        }
    }

}