package com.osroyale.content.clanchannel.channel;

import com.google.gson.*;
import com.osroyale.Config;
import com.osroyale.content.Yell;
import com.osroyale.content.clanchannel.ClanMember;
import com.osroyale.content.clanchannel.ClanRank;
import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.clanchannel.ClanType;
import com.osroyale.content.clanchannel.content.*;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.game.world.region.RegionManager;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;
import com.osroyale.util.Difficulty;
import com.osroyale.util.Utility;
import com.osroyale.util.parser.GsonParser;

import java.util.*;
import java.util.function.Consumer;

/**
 * Handles the clan chat channel.
 *
 * @author Daniel
 * @author Michael
 */
public class ClanChannel implements Comparable<ClanChannel> {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    /** The clan channel details. */
    private ClanDetails details = new ClanDetails(this);

    /** The clan channel management tab panel. */
    private ClanManagement management = new ClanManagement(this);

    /** The clan channel showcase items. */
    private ClanShowcase showcase = new ClanShowcase(this);

    /** The clan channel handler. */
    private ClanChannelHandler handler = new ClanChannelHandler(this);

    /** The clan channel member set. */
    private final Set<ClanMember> members = new HashSet<>();

    /** A list of banned members. */
    final List<String> bannedMembers = new ArrayList<>();

    /** The total active logged in members. */
    private int active;

    private ClanChannel() {
    }

    public static ClanChannel create(Player player) {
        if (ClanRepository.getChannel(player.getName()) != null) {
            player.send(new SendMessage("There is already a clan channel under your name!"));
            return null;
        }
        int[] showcase = new int[]{1155, 1117, 1075};
        ClanChannel channel = new ClanChannel();
        channel.management.name = player.getName();
        channel.details.owner = player.getName();
        channel.management.slogan = "Generic slogan here";
        channel.details.established = Utility.getSimpleDate();
        channel.details.type = ClanType.SOCIAL;
        channel.details.level = ClanLevel.BRONZE;
        channel.showcase.showcase = showcase;
        Arrays.stream(showcase).forEach(i -> channel.showcase.showcaseItems.add(i));
        channel.connect(player);
        ClanRepository.addChannel(channel);
        ClanRepository.ALLTIME.add(channel);
        return channel;
    }

    /** Handles a player connecting to the clan channel. */
    void connect(Player player) {
        ClanMember member = new ClanMember(player);

        if (!player.lastClan.isEmpty()) {
            if (player.lastClan.equals(details.owner)) {
                player.lastClan = "";
            } else if (!members.contains(member)) {
                handler.sendConnectionWarning(player, member);
                return;
            }
        }

        add(player, member);
    }

    void add(Player player, ClanMember member) {
        if (member.name.equalsIgnoreCase(details.owner)) {
            member.rank = ClanRank.LEADER;
        }

        if (!handler.attemptConnection(player, member)) {
            return;
        }

        if (handler.testPassword(player, member)) {
            return;
        }

        int count = getMembers().size();

        establish(player, member);

        if (getMembers().size() > count) {
            activateAchievement(ClanAchievement.CLAN_MEMBERS_I);
            activateAchievement(ClanAchievement.CLAN_MEMBERS_II);
            activateAchievement(ClanAchievement.CLAN_MEMBERS_III);
        }
    }

    void establish(Player player, ClanMember member) {
        player.clanChannel = this;
        player.clanTag = getTag();
        player.clanTagColor = details.level.getColor();
        player.updateFlags.add(UpdateFlag.APPEARANCE);

        if (player.lastClan.equals(details.owner)) {
            player.lastClan = "";
        }

        active++;
        member = getMember(member.name).orElse(member);
        member.player = Optional.of(player);
        if (addMember(member)) {
            member.joined = Utility.getSimpleDate();
        }
        handler.connected(member);
        ClanRepository.setActive(this);
    }

    private boolean addMember(ClanMember member) {
        return members.add(member);
    }

    /** Handles disconnecting a player from the clan channel. */
    void disconnect(String name, boolean logout) {
        Iterator<ClanMember> iterator = members.iterator();
        while (iterator.hasNext()) {
            ClanMember member = iterator.next();
            if (member.name.equals(name)) {
                if (member.rank == ClanRank.SYSTEM || (member.rank == ClanRank.MEMBER && !member.hasContributed())) {
                    iterator.remove();
                }
                member.player.ifPresent(player -> {
                    if (!logout) {
                        ClanChannelHandler.clean(player);
                        player.clanTag = "";
                        player.clanChannel = null;
                        player.updateFlags.add(UpdateFlag.APPEARANCE);
                        player.send(new SendMessage("You have disconnected from the clan chat channel."));
                    }
                    if (member.rank != ClanRank.SYSTEM) {
                        player.lastClan = details.owner;
                    }
                    member.player = Optional.empty();
                    if (--active == 0) {
                        ClanRepository.setInactive(this);
                    }
                });
                return;
            }
        }
    }

    public void ban(String name) {
        bannedMembers.add(name);
        Iterator<ClanMember> iterator = members.iterator();
        while (iterator.hasNext()) {
            ClanMember member = iterator.next();

            if (member.name.equals(name)) {
                if (member.rank.greaterThanEqual(ClanRank.LEADER)) {
                    return;
                }

                member.player.ifPresent(player -> {
                    ClanChannelHandler.clean(player);
                    player.clanTag = "";
                    player.lastClan = details.owner;
                    player.clanChannel = null;
                    player.updateFlags.add(UpdateFlag.APPEARANCE);
                    player.send(new SendMessage("You have been banned from the clan chat channel."));
                    member.player = Optional.empty();

                    if (--active == 0) {
                        ClanRepository.setInactive(this);
                    }
                });

                iterator.remove();
                return;
            }
        }
    }

    public void unban(Player player, int index) {
        if (index < 0 || index >= bannedMembers.size())
            return;
        String name = bannedMembers.get(index);
        if (bannedMembers.remove(index) != null) {
            int string = 51311;
            int size = bannedMembers.size() < 10 ? 10 : bannedMembers.size();
            for (int idx = 0; idx < size; idx++) {
                boolean valid = idx < bannedMembers.size();
                Optional<String> banned = valid ? Optional.of(bannedMembers.get(idx)) : Optional.empty();
                player.send(new SendString(banned.orElse(""), string));
                player.send(new SendTooltip(valid ? "Unban " + bannedMembers.get(idx) : "", string));
                string++;
            }
            player.send(new SendScrollbar(51310, size * 23));
            player.send(new SendMessage("You have unbanned " + name + " from the clan chat channel."));
        }
    }

    public void addExperience(double experience) {
        if (experience + details.experience > Double.MAX_VALUE)
            return;
        details.experience += experience;
        ClanLevel level = ClanLevel.getLevel(details.experience);
        if (details.level != level) {
            details.level = level;
            details.points += level.getPoints();
            forPlayers(player -> {
                player.clanTagColor = level.getColor();
                player.updateFlags.add(UpdateFlag.APPEARANCE);
            });
            message("We have leveled up to <col=255>" + Utility.formatEnum(level.name()) + "</col>! Total Experience: <col=255>" + Utility.formatDigits(details.experience), "We have earned <col=255>" + level.getPoints() + "</col> CP, which puts us at <col=255>" + Utility.formatDigits(details.points) + "</col> CP.");
        }
    }

    public void receiveTask(Difficulty difficulty) {
        if (details.clanTask == null) {
            ClanTask task = ClanTask.getAssignment(details.type, difficulty);
            details.taskAmount = task.getAmount();
            details.clanTask = task;
            message("Task time! <col=255>" + task.getName(this) + "</col>.");
        }
    }

    public void activateTask(ClanTaskKey key, String name) {
        activateTask(key, name, 1);
    }

    public void activateTask(ClanTaskKey key, String name, int amount) {
        if (details.clanTask == null)
            return;
        ClanTask task = details.clanTask;
        if (task.key != key)
            return;
        double experience = task.getProgressExperience();
        int progress = details.taskAmount - amount;
        if (progress <= 0) {
            int reward = details.clanTask.getReward();
            details.points += reward;
            message("Task complete! Reward: <col=255>" + reward + "</col> CP. We now have: <col=255>" + Utility.formatDigits(details.points) + "</col> CP.");
            details.clanTask = null;
            progress = 0;
            activateAchievement(ClanAchievement.TASKS_I);
            activateAchievement(ClanAchievement.TASKS_II);
            activateAchievement(ClanAchievement.TASKS_III);
        }

        details.taskAmount = progress;
        addExperience(experience);
        getMember(name).ifPresent(member -> {
            member.expGained += experience;
            if (task.type == ClanType.PVP) {
                member.playerKills++;
            } else if (task.type == ClanType.PVM) {
                member.npcKills++;
            }

            member.player.ifPresent(player -> {
                if (!player.lastClan.isEmpty() && !player.lastClan.equalsIgnoreCase(player.getName())) {
                    ClanChannel channel = ClanRepository.getChannel(player.lastClan);
                    if (channel != null)
                        channel.disconnect(name, false);
                }
            });
        });
    }

    public void activateAchievement(ClanAchievement achievement) {
        final int current = details.achievements.computeIfAbsent(achievement, a -> 0);
        for (ClanAchievement list : ClanAchievement.values()) {
            if (list == achievement) {
                details.achievements.put(achievement, current + 1);
                if (details.completedAchievement(list)) {
                    message("Achievement completed! " + list.details, "We have earned " + list.getPoints() + " CP and " + Utility.formatDigits(list.getExperience()) + " Clan EXP.");
                }
            }
        }
    }

    public void setRank(ClanMember member, ClanRank rank) {
        member.player.ifPresent(player -> {
            if (!player.lastClan.isEmpty() && !player.lastClan.equalsIgnoreCase(player.getName())) {
                ClanChannel channel = ClanRepository.getChannel(player.lastClan);
                if (channel != null)
                    channel.disconnect(member.name, false);
            }
        });
        member.rank = rank;
    }

    public void setName(Player player, String name) {
        if (getName().equalsIgnoreCase(name)) {
            player.message("Your clan channel name is already set to " + getName() + "!");
            return;
        }
        if (Arrays.stream(Config.BAD_STRINGS).anyMatch(name::contains)) {
            player.message("The name you have entered was prohibited! Try something else.");
            return;
        }
        if (ClanRepository.nameExist(name)) {
            player.message("There is already a clan that exists with that name!");
            return;
        }
        name = Utility.formatName(name);
        if (!management.name.isEmpty())
            ClanRepository.ACTIVE_NAMES.remove(management.name.toLowerCase().trim());
        ClanRepository.ACTIVE_NAMES.add(name.toLowerCase().trim());
        management.name = name;
        message("The clan name has been changed to: <col=255>" + name + "</col>.");
        player.clanViewer.update(this);
    }

    public void setTag(Player player, String tag) {
        if (tag.length() > 4)
            return;
        if (getTag().equalsIgnoreCase(tag)) {
            player.message("Your clan channel tag is already set to " + getTag() + "!");
            return;
        }
        if (Arrays.stream(Config.BAD_STRINGS).anyMatch(tag::contains)) {
            player.message("The tag you have entered was prohibited! Try something else.");
            return;
        }
        if (ClanRepository.tagExist(tag)) {
            player.message("There is already a clan that exists with that tag!");
            return;
        }
        if (!management.tag.isEmpty())
            ClanRepository.ACTIVE_TAGS.remove(management.tag);
        ClanRepository.ACTIVE_TAGS.add(management.tag = tag);
        message("The clan tag has been changed to: <col=255>" + getTag() + "</col>.");
        player.clanViewer.update(this);
    }

    public void setSlogan(Player player, String slogan) {
        if (Arrays.stream(Config.BAD_STRINGS).anyMatch(slogan::contains)) {
            player.message("The slogan you have entered was prohibited! Try something else.");
            return;
        }
        management.slogan = Utility.formatName(slogan);
        player.clanViewer.update(this);
    }

    public Optional<ClanMember> getMember(String name) {
        for (ClanMember member : members) {
            if (member.name.equalsIgnoreCase(name))
                return Optional.of(member);
        }
        return Optional.empty();
    }

    List<ClanMember> getActiveMembers() {
        List<ClanMember> list = new LinkedList<>();
        forEach(member -> member.player.ifPresent(player -> list.add(member)));
        return list;
    }

    /** Handles looping through each clan member. */
    private void forPlayers(Consumer<Player> player) {
        for (ClanMember member : members) {
            member.player.ifPresent(player);
        }
    }

    /** Handles looping through each clan member. */
    public void forEach(Consumer<ClanMember> member) {
        members.forEach(member);
    }

    /** Handles messaging all the members in the clan channel. */
    public void message(Object... messages) {
        forEach(member -> member.message(messages));
    }

    public void chat(String name, String message) {
        getMember(name).ifPresent(member -> {
            if (management.canTalk(member)) {
                if (Arrays.stream(Yell.INVALID).anyMatch(message::contains)) {
                    member.message("Your message contains invalid characters.");
                    return;
                }

                forEach(other -> other.chat(member, message));
            } else {
                member.message("Your rank is too low to talk in this channel.");
            }
        });
    }

    public void splitLoot(Player player, Mob dead, Item item) {
        if (!lootshareEnabled()) {
            return;
        }

        Set<Player> players = new HashSet<>();
        long value = item.getValue(PriceType.HIGH_ALCH_VALUE) * item.getAmount();
        long tax = value / 4;
        players.add(player);

        RegionManager.forNearbyPlayer(dead, 32, nearby -> {
            if (Objects.equals(nearby.clanChannel, player.clanChannel)) {
                players.add(nearby);
            }
        });

        long split = (value - tax) / players.size();
        players.forEach(other -> {
            getMember(other.getName()).ifPresent(member ->
                    member.message(Utility.formatDigits(split) + " coins have been deposited into your bank vault."));
//            other.bankVault.deposit((int) split);
        });
    }

    public String getName() {
        return management.name;
    }

    public String getOwner() {
        return details.owner;
    }

    public int size() {
        return members.size();
    }

    public int activeSize() {
        return active;
    }

    public boolean lootshareEnabled() {
        return management.lootshare;
    }

    public boolean canManage(ClanMember member) {
        return member != null && management.canManage(member);
    }

    boolean isBanned(String name) {
        for (String banned : bannedMembers) {
            if (banned.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocked() {
        return management.locked;
    }

    boolean canEnter(ClanMember member) {
        return management.canEnter(member);
    }

    boolean hasPassword() {
        return management.password != null && !management.password.isEmpty();
    }

    boolean isPassword(String input) {
        return input.equals(management.password);
    }

    public ClanDetails getDetails() {
        return details;
    }

    public ClanManagement getManagement() {
        return management;
    }

    public Set<ClanMember> getMembers() {
        return members;
    }

    public ClanChannelHandler getHandler() {
        return handler;
    }

    public String getSlogan() {
        return management.slogan;
    }

    public String getPassword() {
        return management.password;
    }

    public String getTag() {
        return management.tag;
    }

    public String getColor() {
        return management.color;
    }

    public ClanShowcase getShowcase() {
        return showcase;
    }

    public Item[] getShowcaseItems() {
        Item[] items = new Item[showcase.showcase.length];
        int count = 0;
        for (int iem : showcase.showcase) {
            items[count] = new Item(iem, 1);
            if (items[count].isStackable()) {
                items[count].setAmount(250);
            }
            count++;
        }
        return items;
    }

    public void setColor(String color) {
        management.color = color;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("owner", details.owner);
        object.addProperty("established", details.established);
        object.addProperty("name", management.name);
        object.addProperty("password", management.password);
        object.addProperty("forum", management.forum);
        object.addProperty("slogan", management.slogan);
        object.addProperty("clan-tag", management.tag);
        object.addProperty("clan-type", String.valueOf(details.type));
        object.addProperty("clan-color", management.color);
        object.addProperty("clan-points", details.points);
        object.addProperty("clan-experience", details.experience);
        object.addProperty("clan-level", String.valueOf(details.level));
        if (details.clanTask != null) {
            object.addProperty("clan-task", String.valueOf(details.clanTask));
        }
        object.addProperty("task-amount", details.taskAmount);
        object.addProperty("locked", management.locked);
        object.addProperty("lootshare", management.lootshare);
        management.saveRanks(object);

        object.add("showcase", GSON.toJsonTree(showcase.showcase));
        object.add("showcase-items", GSON.toJsonTree(showcase.showcaseItems));
        object.add("banned-members", GSON.toJsonTree(bannedMembers));

        JsonArray memberArray = new JsonArray();
        for (ClanMember member : members) {
            JsonObject memberObj = new JsonObject();
            memberObj.addProperty("name", member.name);
            memberObj.addProperty("joined", member.joined);
            memberObj.addProperty("rank", String.valueOf(member.rank));
            memberObj.addProperty("exp-gained", member.expGained);
            memberObj.addProperty("npc-kills", member.npcKills);
            memberObj.addProperty("player-kills", member.playerKills);
            memberObj.addProperty("total-level", member.totalLevel);
            memberArray.add(memberObj);
        }
        object.add("members", memberArray);
        return object;
    }

    public static void load(String owner) {
        new GsonParser("/content/clan/" + owner, false) {
            @Override
            protected void parse(JsonObject object) {
                ClanChannel channel = new ClanChannel();

                channel.details.owner = object.get("owner").getAsString();
                channel.management.name = object.get("name").getAsString();
                if (object.has("password")) {
                    channel.management.password = object.get("password").getAsString();
                }
                channel.details.established = object.get("established").getAsString();
                if (object.has("forum")) {
                    channel.management.forum = object.get("forum").getAsString();
                }
                if (object.has("slogan")) {
                    channel.management.slogan = object.get("slogan").getAsString();
                }
                if (object.has("clan-tag")) {
                    channel.management.tag = object.get("clan-tag").getAsString();
                }
                channel.details.type = ClanType.valueOf(object.get("clan-type").getAsString());
                if (object.has("clan-color")) {
                    channel.management.color = object.get("clan-color").getAsString();
                }

                channel.details.points = object.get("clan-points").getAsInt();
                channel.details.experience = object.get("clan-experience").getAsLong();
                channel.details.level = ClanLevel.valueOf(object.get("clan-level").getAsString());
                if (object.has("clan-task")) {
                    channel.details.clanTask = ClanTask.valueOf(object.get("clan-task").getAsString());
                }
                channel.details.taskAmount = object.get("task-amount").getAsInt();
                channel.management.locked = object.get("locked").getAsBoolean();
                channel.management.lootshare = object.get("lootshare").getAsBoolean();
                channel.management.loadRanks(object);
                channel.showcase.showcase = GSON.fromJson(object.get("showcase"), int[].class);

                JsonArray array = object.get("showcase-items").getAsJsonArray();
                for (JsonElement element : array) {
                    channel.showcase.showcaseItems.add(element.getAsInt());
                }

                array = object.get("banned-members").getAsJsonArray();
                for (JsonElement element : array) {
                    channel.bannedMembers.add(element.getAsString());
                }

                JsonArray members = object.get("members").getAsJsonArray();
                for (JsonElement element : members) {
                    JsonObject memberObj = (JsonObject) element;
                    String name = memberObj.get("name").getAsString();
                    ClanMember member = new ClanMember(name);
                    member.joined = memberObj.get("joined").getAsString();
                    member.rank = ClanRank.valueOf(memberObj.get("rank").getAsString());
                    member.expGained = memberObj.get("exp-gained").getAsDouble();
                    member.npcKills = memberObj.get("npc-kills").getAsInt();
                    member.playerKills = memberObj.get("player-kills").getAsInt();
                    member.totalLevel = memberObj.get("total-level").getAsInt();
                    channel.addMember(member);
                }

                if (!channel.getTag().isEmpty())
                    ClanRepository.ACTIVE_TAGS.add(channel.getTag());
                if (!channel.getName().isEmpty())
                    ClanRepository.ACTIVE_NAMES.add(channel.getName());
                ClanRepository.ALLTIME.add(channel);
                ClanRepository.addChannel(channel);
            }
        }.run();
    }

    @Override
    public int compareTo(ClanChannel other) {
        int compare = Double.compare(other.details.experience, details.experience);
        if (compare == 0)
            return management.name.compareTo(other.management.name);
        return compare;
    }

    @Override
    public int hashCode() {
        return details.owner.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof ClanChannel) {
            ClanChannel other = (ClanChannel) obj;
            return Objects.equals(details, other.details)
                    && Objects.equals(management, other.management);
        }
        return super.equals(obj);
    }
}
