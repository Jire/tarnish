package com.osroyale.content.dialogue.impl;

import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.ClanRank;
import com.osroyale.content.clanchannel.channel.ClanChannel;
import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;

public class ClanRankDialogue extends Dialogue {

    @Override
    public void sendDialogues(DialogueFactory factory) {
        Player player = factory.getPlayer();
        ClanChannel channel = player.clanChannel;
        if (channel == null)
            return;
        ClanMember playerMember = channel.getMember(player.getName()).orElse(null);
        if (playerMember == null || !channel.canManage(playerMember)) {
            player.send(new SendMessage("You do not have the required rank to do this."));
            return;
        }
        ClanMember member = player.attributes.get("CLAN_RANK_MEMBER", ClanMember.class);
        if (member == null)
            return;
        factory.sendOption("Recruit", () -> factory.onAction(() -> {
            setRank(player, channel, member, ClanRank.RECRUIT);
        }), "Corporal", () -> factory.onAction(() -> {
            setRank(player, channel, member, ClanRank.CORPORAL);
        }), "Sergeant", () -> factory.onAction(() -> {
            setRank(player, channel, member, ClanRank.SERGEANT);
        }), "Lieutenant", () -> factory.onAction(() -> {
            setRank(player, channel, member, ClanRank.LIEUTENANT);
        }), "Next", () -> {
            factory.onAction(() -> {
                factory.sendOption("Captain", () -> factory.onAction(() -> {
                    setRank(player, player.clanChannel, member, ClanRank.CAPTAIN);
                }), "General", () -> factory.onAction(() -> {
                    setRank(player, player.clanChannel, member, ClanRank.GENERAL);
                }), "Nevermind", () -> factory.onAction(factory::clear));
            });
        }).execute();
    }

    private void setRank(Player player, ClanChannel channel, ClanMember member, ClanRank rank) {
        channel.setRank(member, rank);
        player.dialogueFactory.sendStatement("You have promoted " + member.name + " to " + rank.getString() + "  " + rank.name).execute();
        player.interfaceManager.close();
    }

}
