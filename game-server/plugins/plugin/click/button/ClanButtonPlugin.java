package plugin.click.button;

import com.osroyale.Config;
import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.ClanRank;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.clanchannel.channel.ClanChannelHandler;
import com.osroyale.content.clanchannel.content.ClanMemberComporator;
import com.osroyale.content.clanchannel.content.ClanViewer;
import com.osroyale.content.dialogue.impl.ClanRankDialogue;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendInputMessage;
import com.osroyale.net.packet.out.SendMessage;

import java.util.List;

public class ClanButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        if (button >= -32003 && button <= -31907) {
            ClanChannelHandler.manageMember(player, button);
            return true;
        }
        if (button >= -12334 && button <= -12310) {
            List<ClanChannel> clans = player.clanViewer.clanList;
            if (clans.isEmpty())
                return true;
            int index = (button - -12334) / 2;
            if (index < clans.size()) {
                player.clanViewer.clanIndex = index;
                player.clanViewer.open(clans.get(index), ClanViewer.ClanTab.OVERVIEW);
            }
            return true;
        }
        if (button >= -10484 && button <= -10456) {
            List<ClanMember> members = player.clanViewer.clanMembers;
            if (members.isEmpty())
                return true;
            int index = (button - -10484) / 2;
            if (index < members.size()) {
                player.clanViewer.memberIndex = index;
                player.clanViewer.open(ClanViewer.ClanTab.MEMBERS);
            }
            return true;
        }
        if (button >= -14225 && button <= -14214) {
            player.forClan(channel -> {
                if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                    channel.unban(player, -14225 - button);
                }
            });
            return true;
        }
        switch (button) {
            case -32018:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getManagement().toggleLootshare();
                    }
                });
                return true;

            case -32011:
                player.settings.clanMemberComporator = ClanMemberComporator.PRIVILAGE;
                player.send(new SendConfig(394, player.settings.clanMemberComporator.ordinal()));
                player.forClan(channel -> channel.getMember(player.getName()).ifPresent(member -> channel.getHandler().updateMemberList(member)));
                return true;
            case -32010:
                player.settings.clanMemberComporator = ClanMemberComporator.NAME;
                player.send(new SendConfig(394, player.settings.clanMemberComporator.ordinal()));
                player.forClan(channel -> channel.getMember(player.getName()).ifPresent(member -> channel.getHandler().updateMemberList(member)));
                return true;
            case -32009:
                player.settings.clanMemberComporator = ClanMemberComporator.RANK;
                player.send(new SendConfig(394, player.settings.clanMemberComporator.ordinal()));
                player.forClan(channel -> channel.getMember(player.getName()).ifPresent(member -> channel.getHandler().updateMemberList(member)));
                return true;

            case -23406:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getManagement().showBanned(player);
                    }
                });
                return true;
            case -23416:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getShowcase().openShowcase(player, 0);
                    }
                });
                return true;
            case -23419:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getShowcase().openShowcase(player, 1);
                    }
                });
                return true;
            case -23413:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getShowcase().openShowcase(player, 2);
                    }
                });
                return true;
            //clan join
            case -32029:
                if (player.clanChannel == null) {
                    player.send(new SendInputMessage("Enter the channel owner's name you would like to join:", 12, input -> ClanChannelHandler.connect(player, input)));
                    return true;
                }

                player.send(new SendMessage("You are already in a clan chat channel!"));
                return true;
            //clan leave
            case -32026:
                if (!ClanChannelHandler.disconnect(player, false)) {
                    player.send(new SendMessage("You are currently not in a clan chat channel!"));
                }
                return true;
            case -23409:
            case -23408: {
                ClanChannel channel = player.clanChannel;
                if (channel != null && channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                    channel.getManagement().locked = !channel.getManagement().locked;
                    boolean locked = channel.getManagement().locked;
                    player.send(new SendConfig(326, locked ? 0 : 1));
                    if (locked) {
                        player.message("Your clan is now locked and members may not join the clan.");
                    } else {
                        player.message("Your clan is now unlocked and members may join the clan.");
                    }
                }
                return true;
            }
            //clan information
            case -32023:
                if (player.clanChannel != null) {
                    player.dialogueFactory.sendOption("View Clan Information", () -> player.clanViewer.open(player.clanChannel, ClanViewer.ClanTab.OVERVIEW), "View Clan Task", () -> {
                        if (player.clanChannel.getDetails().clanTask == null) {
                            player.dialogueFactory.sendStatement("We have no current task assigned!");
                        } else {
                            player.dialogueFactory.sendStatement("Our current task is to: ", player.clanChannel.getDetails().clanTask.getName(player.clanChannel));
                        }
                    }, "View Clan Achievements", player.clanViewer::viewAchievements);
                    player.dialogueFactory.execute();
                } else {
                    player.clanViewer.open(player.clanChannel, ClanViewer.ClanTab.OVERVIEW);
                }
                return true;
            // clan manage
            case -32016: {
                if (player.clanChannel == null) {
                    player.send(new SendMessage("You need to be in a clan to do this!"));
                } else {
                    ClanChannelHandler.manage(player);
                }
                return true;
            }
            // clan manage close tab
            case -23530:
                player.interfaceManager.setSidebar(Config.CLAN_TAB, 33500);
                return true;
            // clan manage
            case -21929:
                player.dialogueFactory.sendDialogue(new ClanRankDialogue());
                return true;
            case -21926: {
                ClanChannel channel = player.clanChannel;
                if (channel == null)
                    return true;
                ClanMember member = channel.getMember(player.getName()).orElse(null);
                if (member == null || !channel.canManage(member)) {
                    player.send(new SendMessage("You do not have the required rank to do this."));
                    return true;
                }
                ClanMember other = player.attributes.get("CLAN_RANK_MEMBER", ClanMember.class);
                channel.setRank(other, ClanRank.MEMBER);
                player.send(new SendMessage("You have successfully demoted " + other + "."));
                player.interfaceManager.close();
            }
            return true;
            case -21923: {
                ClanChannel channel = player.clanChannel;
                if (channel == null)
                    return true;
                ClanMember member = channel.getMember(player.getName()).orElse(null);
                if (member == null || !channel.canManage(member)) {
                    player.send(new SendMessage("You do not have the required rank to do this."));
                    return true;
                }
                ClanMember other = player.attributes.get("CLAN_RANK_MEMBER", ClanMember.class);
                Player otherPlayer = other.player.orElse(null);
                if (otherPlayer == null) {
                    player.send(new SendMessage("This player is currently offline and can not be kicked from the clan!"));
                    return true;
                }

                if (!member.rank.greaterThanEqual(ClanRank.LEADER)) {
                    ClanChannelHandler.disconnect(otherPlayer, false);
                    player.send(new SendMessage("You have successfully kicked " + other + "."));
                    player.interfaceManager.close();
                } else {
                    player.send(new SendMessage("Naaaah."));
                }
            }
            return true;
            case -21920: {
                ClanChannel channel = player.clanChannel;
                if (channel == null)
                    return true;
                ClanMember member = channel.getMember(player.getName()).orElse(null);
                if (member == null || !channel.canManage(member)) {
                    player.send(new SendMessage("You do not have the required rank to do this."));
                    return true;
                }
                ClanMember other = player.attributes.get("CLAN_RANK_MEMBER", ClanMember.class);
                Player otherPlayer = other.player.orElse(null);
                if (otherPlayer == null) {
                    player.send(new SendMessage("This player is currently offline and can not be banned from the clan!"));
                    return true;
                }
                channel.ban(other.name);
                player.send(new SendMessage("You have successfully banned " + other + "."));
                player.interfaceManager.close();
            }
            return true;
            case -12523:
                player.clanViewer.open(ClanViewer.ClanTab.OVERVIEW);
                return true;
            case -12522:
                player.clanViewer.memberIndex = 0;
                player.clanViewer.open(ClanViewer.ClanTab.MEMBERS);
                return true;
            case -7830:
                player.forClan(channel -> channel.getShowcase().set(player));
                return true;
            case -7827:
                player.forClan(channel -> channel.getShowcase().remove(player));
                return true;
//
        }
        return false;
    }
}
