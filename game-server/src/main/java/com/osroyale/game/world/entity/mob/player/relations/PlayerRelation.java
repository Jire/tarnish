package com.osroyale.game.world.entity.mob.player.relations;

import com.osroyale.game.event.impl.log.PrivateMessageChatLogEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PlayerRelation {

    private PrivacyChatMode publicChatMode = PrivacyChatMode.ON;
    private PrivacyChatMode privateChatMode = PrivacyChatMode.ON;
    private PrivacyChatMode clanChatMode = PrivacyChatMode.ON;
    private PrivacyChatMode tradeChatMode = PrivacyChatMode.ON;

    private final List<Long> friendList = new ArrayList<>(200);

    private final List<Long> ignoreList = new ArrayList<>(100);

    private int privateMessageId = 1;

    public int getPrivateMessageId() {
        return privateMessageId++;
    }

    private Player player;

    public PlayerRelation(Player player) {
        this.player = player;
    }

    public PlayerRelation setPrivateMessageId(int privateMessageId) {
        this.privateMessageId = privateMessageId;
        return this;
    }

    public List<Long> getFriendList() {
        return friendList;
    }

    public List<Long> getIgnoreList() {
        return ignoreList;
    }

    public PlayerRelation updateLists(boolean online) {

        if (privateChatMode == PrivacyChatMode.OFF) {
            online = false;
        }

        player.send(new SendPrivateMessageListStatus(PrivateMessageListStatus.LOADED));
        for (Player players : World.getPlayers()) {
            if (players == null) {
                continue;
            }
            boolean temporaryOnlineStatus = online;
            if (players.relations.friendList.contains(player.usernameLong)) {
                if (privateChatMode.equals(PrivacyChatMode.FRIENDS_ONLY) && !friendList.contains(players.usernameLong) ||
                        privateChatMode.equals(PrivacyChatMode.OFF) || ignoreList.contains(players.usernameLong)) {
                    temporaryOnlineStatus = false;
                }
                players.send(new SendAddFriend(player.usernameLong, temporaryOnlineStatus ? 1 : 0));
            }
            boolean tempOn = true;
            if (player.relations.friendList.contains(players.usernameLong)) {
                if (players.relations.privateChatMode.equals(PrivacyChatMode.FRIENDS_ONLY) && !players.relations.getFriendList().contains(player.usernameLong) ||
                        players.relations.privateChatMode.equals(PrivacyChatMode.OFF) || players.relations.getIgnoreList().contains(player.usernameLong)) {
                    tempOn = false;
                }
                player.send(new SendAddFriend(players.usernameLong, tempOn ? 1 : 0));
            }
        }
        return this;
    }

    private void updatePrivacyChatOptions() {
        player.send(new SendChatOption(publicChatMode, privateChatMode, clanChatMode, tradeChatMode));
    }

    private void sendFriends() {
        for (long l : friendList) {
            player.send(new SendAddFriend(l, 0));
        }
    }

    private void sendIgnores() {
        for (long l : ignoreList) {
            player.send(new SendAddIgnore(l));
        }
    }

    private void sendAddFriend(long name) {
        player.send(new SendAddFriend(name, 0));
    }

    public PlayerRelation onLogin() {
        sendFriends();
        sendIgnores();
        updatePrivacyChatOptions();
        updateLists(true);
        return this;
    }

    public void addFriend(Long username) {
        String name = Utility.formatName(Utility.longToString(username));

        if (player.usernameLong == username) {
            return;
        }

        if (friendList.size() >= 200) {
            player.send(new SendMessage("Your friend list is full!"));
            return;
        }
        if (ignoreList.contains(username)) {
            player.send(new SendMessage("Please remove " + name + " from your ignore list first."));
            return;
        }
        if (friendList.contains(username)) {
            player.send(new SendMessage(name + " is already on your friends list!"));
        } else {
            friendList.add(username);
            sendAddFriend(username);
            updateLists(true);
            Optional<Player> result = World.search(name);

            result.ifPresent(it -> it.relations.updateLists(true));
        }
    }

    public boolean isFriendWith(String player) {
        return friendList.contains(Utility.stringToLong(player));
    }

    public void deleteFriend(Long username) {
        final String name = Utility.formatName(Utility.longToString(username));
        if (name.equals(player.getUsername())) {
            return;
        }
        if (friendList.contains(username)) {
            friendList.remove(username);

            if (privateChatMode != PrivacyChatMode.ON) {
                updateLists(false);
            }
        } else {
            player.send(new SendMessage("This player is not on your friends list!"));
        }
    }

    public void addIgnore(Long username) {
        String name = Utility.formatName(Utility.longToString(username));

        if (player.usernameLong == username) {
            return;
        }

        if (ignoreList.size() >= 100) {
            player.send(new SendMessage("Your ignore list is full!"));
            return;
        }
        if (friendList.contains(username)) {
            player.send(new SendMessage("Please remove " + name + " from your friend list first."));
            return;
        }
        if (ignoreList.contains(username)) {
            player.send(new SendMessage(name + " is already on your ignore list!"));
        } else {
            ignoreList.add(username);
            updateLists(true);

            Optional<Player> result = World.search(name);
            result.ifPresent(it -> it.relations.updateLists(false));
        }
    }

    public void deleteIgnore(Long username) {
        String name = Utility.formatName(Utility.longToString(username));
        if (name.equals(player.getUsername())) {
            return;
        }
        if (ignoreList.contains(username)) {
            ignoreList.remove(username);
            updateLists(true);
            if (privateChatMode.equals(PrivacyChatMode.ON)) {
                Optional<Player> result = World.search(name);
                result.ifPresent(it -> it.relations.updateLists(true));
            }
        } else {
            player.send(new SendMessage("This player is not on your ignore list!"));
        }
    }

    public void message(Player friend, PrivateChatMessage message) {
        if (player.punishment.isMuted()) {
            player.message("You can not send private messages while muted!");
            return;
        }
        if (friend == null) {
            player.send(new SendMessage("This player is currently offline."));
            return;
        }

        if (friend.relations.privateChatMode.equals(PrivacyChatMode.FRIENDS_ONLY) && !friend.relations.friendList.contains(player.usernameLong) || friend.relations.privateChatMode.equals(PrivacyChatMode.OFF)) {
            player.send(new SendMessage("This player is currently offline."));
            return;
        }

        if (privateChatMode == PrivacyChatMode.OFF) {
            setPrivateChatMode(PrivacyChatMode.FRIENDS_ONLY, true);
        }

        friend.send(new SendPrivateMessage(player.usernameLong, player.right, message.getCompressed()));
        World.getDataBus().publish(new PrivateMessageChatLogEvent(player, friend, message.getDecompressed()));
    }

    public PlayerRelation setPrivateChatMode(PrivacyChatMode privateChatMode, boolean update) {
        if (this.privateChatMode == privateChatMode) {
            return this;
        }

        this.privateChatMode = privateChatMode;

        if (update) {
            updateLists(true);
        }

        return this;
    }

    public void setPrivacyChatModes(int publicChat, int privateChat, int clanChat, int tradeChat) {
        if (publicChat >= 0 && publicChat <= PrivacyChatMode.HIDE.getCode()) {
            PrivacyChatMode.get(publicChat).ifPresent(it -> this.publicChatMode = it);
        }

        if (privateChat >= 0 && privateChat <= PrivacyChatMode.OFF.getCode()) {
            PrivacyChatMode.get(privateChat).ifPresent(it -> setPrivateChatMode(it, true));
        }

        if (clanChat >= 0 && clanChat <= PrivacyChatMode.OFF.getCode()) {
            PrivacyChatMode.get(clanChat).ifPresent(it -> this.clanChatMode = it);
        }

        if (tradeChat >= 0 && tradeChat <= PrivacyChatMode.OFF.getCode()) {
            PrivacyChatMode.get(tradeChat).ifPresent(it -> this.tradeChatMode = it);
        }
    }

    public PrivacyChatMode getPublicChatMode() {
        return publicChatMode;
    }

    public void setPublicChatMode(PrivacyChatMode publicChatMode, boolean update) {
        this.publicChatMode = publicChatMode;
        if (update) {
            updatePrivacyChatOptions();
        }
    }

    public PrivacyChatMode getPrivateChatMode() {
        return privateChatMode;
    }

    public void setClanChatMode(PrivacyChatMode clanChatMode, boolean update) {
        this.clanChatMode = clanChatMode;
        if (update) {
            updatePrivacyChatOptions();
        }
    }

    public PrivacyChatMode getClanChatMode() {
        return clanChatMode;
    }

    public PrivacyChatMode getTradeChatMode() {
        return tradeChatMode;
    }

    public void setTradeChatMode(PrivacyChatMode tradeChatMode, boolean update) {
        this.tradeChatMode = tradeChatMode;
        if (update) {
            updatePrivacyChatOptions();
        }
    }

}
