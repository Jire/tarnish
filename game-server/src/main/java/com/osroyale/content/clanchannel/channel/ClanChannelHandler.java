package com.osroyale.content.clanchannel.channel;

import com.osroyale.Config;
import com.osroyale.content.clanchannel.*;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.Utility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.osroyale.content.clanchannel.channel.ClanManagement.*;

/**
 * The handler for the clan channel.
 *
 * @author Michael
 * @author Daniel
 */
public class ClanChannelHandler {

    /**
     * The clan channel instance.
     */
    private final ClanChannel channel;

    /**
     * Constructs a new <code>ClanChannelHandler</code>.
     */
    ClanChannelHandler(ClanChannel channel) {
        this.channel = channel;
    }

    /**
     * Handles logging into the server.
     */
    public static void onLogin(Player player) {
        clean(player);
        if (player.clanChannel != null) {
            String owner = player.clanChannel.getOwner();
            player.clanChannel = null;
            connect(player, owner);
        }
    }

    /**
     * Handles player joining a clan channel.
     */
    public static void connect(Player player, String owner) {
        player.send(new SendMessage("Attempting to connect to clan..."));
        ClanChannel channel = ClanRepository.getChannel(owner);
        if (channel == null) {
            boolean loaded = true;
            Path path = Paths.get("./data/content/clan/");
            File[] files = path.toFile().listFiles();

            if (files == null) {
                System.out.println("No clan files were found!");
                player.message("Clan does not exist!");

                if (owner.equalsIgnoreCase(player.getName())) {
                    ClanChannel.create(player);
                }
                return;
            }

            for (File file : files) {
                if (file.getName().toLowerCase().contains(player.getUsername().toLowerCase().trim()) && file.length() > 0) {
                    loaded = false;
                    break;
                }
            }

            if (owner.equalsIgnoreCase(player.getName())) {
                if (!loaded) {
                    player.send(new SendMessage("Your clan didn't load into the server properly!"));
                    player.send(new SendMessage("If you see this message, your clan was not reset."));
                    player.message("Try to join your clan again. If you still see this message, contact a staff member.");
                    ClanChannel.load(player.getUsername().toLowerCase().trim());
                    return;
                }
                ClanChannel.create(player);
            } else {
                if (!loaded) {
                    player.send(new SendMessage("This clan didn't load into the server properly!"));
                    player.message("Try to join your clan again. If you still see this message, contact a staff member.");
                    ClanChannel.load(player.getUsername().toLowerCase().trim());
                    return;
                }
                player.send(new SendMessage("Connection was refused: No channel exists!"));
            }
            return;
        }
        if (!channel.getOwner().equals("Osroyale") && channel.activeSize() >= 80) {
            player.send(new SendMessage("Connection was refused: Channel currently full!"));
            return;
        }
        if (channel.isBanned(player.getName())) {
            player.send(new SendMessage("Connection was refused: Currently banned from channel!"));
            return;
        }
        if (channel.getDetails().type == ClanType.IRON_MAN && !PlayerRight.isIronman(player)) {
            player.message("Only Iron-man accounts can join this clan!");
            return;
        }
        channel.connect(player);
    }

    /**
     * Handles player joining a clan channel.
     */
    public static boolean disconnect(Player player, boolean logout) {
        if (player == null || player.clanChannel == null)
            return false;
        ClanChannel channel = player.clanChannel;
        channel.disconnect(player.getName(), logout);
        return true;
    }

    public static void manage(Player player) {
        ClanChannel channel = player.clanChannel;
        Optional<ClanMember> member = channel.getMember(player.getName());
        member.ifPresent(m -> {
            if (!channel.canManage(m)) {
                player.send(new SendMessage("You do not have sufficient privileges to manage this clan!"));
                return;
            }
            player.send(new SendString(channel.getName(), 42102));
            player.send(new SendString(channel.getTag(), 42104));
            player.send(new SendString(channel.getSlogan(), 42106));
            player.send(new SendString(channel.getPassword(), 42108));
            player.send(new SendString(channel.getDetails().type.getIcon() + "" + channel.getDetails().type.getName(), 42110));
            player.send(new SendString(channel.getManagement().getRank(ENTER_RANK_INDEX) + " " + channel.getManagement().getEnter(), 42112));
            player.send(new SendString(channel.getManagement().getRank(TALK_RANK_INDEX) + " " + channel.getManagement().getTalk(), 42114));
            player.send(new SendString(channel.getManagement().getRank(MANAGE_RANK_INDEX) + " " + channel.getManagement().getManage(), 42116));
            player.send(new SendConfig(326, channel.getManagement().locked ? 1 : 0));
            player.send(new SendItemOnInterface(42126, channel.getShowcaseItems()));
            player.interfaceManager.setSidebar(Config.CLAN_TAB, 42000);
        });
    }

    /**
     * Attempts a connection to the clan channel.
     */
    boolean attemptConnection(Player player, ClanMember member) {
        if (member.rank.equals(ClanRank.MEMBER)) {
            if (ProfileRepository.isFriend(player, channel.getOwner()))
                member.rank = ClanRank.FRIEND;
            if (PlayerRight.isDeveloper(player))
                member.rank = ClanRank.SYSTEM;
        }

        if (!channel.canEnter(member)) {
            player.send(new SendMessage("Connection was refused: No required permission!"));
            return false;
        }

        if (channel.isLocked() && !channel.canManage(member)) {
            player.send(new SendMessage("Connection was refused: Clan is currently locked!"));
            return false;
        }

        return true;
    }

    /**
     * Handles entering a clan channel password.
     */
    boolean testPassword(Player player, ClanMember member) {
        if (!channel.hasPassword() || channel.canManage(member)) {
            return false;
        }

        player.dialogueFactory.sendStatement("This clan requires a password in order to join.").onAction(() -> {
            player.send(new SendInputMessage("Enter the password:", 6, input -> {
                if (!channel.isPassword(input)) {
                    player.send(new SendMessage("You have entered an invalid clan password."));
                    player.dialogueFactory.clear();
                    return;
                }
                player.dialogueFactory.sendStatement("Password was accepted!").execute();
                channel.establish(player, member);
            }));
        }).execute();
        return true;
    }

    /**
     * Handles connecting to the clan channel.
     */
    void connected(ClanMember member) {
        updateMemberList(member);
        member.player.ifPresent(player -> {
            player.send(new SendMessage("Now talking in clan chat <col=FFFF64><shad=0>" + Utility.formatName(channel.getName()) + "</shad></col>."));
            player.send(new SendMessage("To talk, start each line of chat with the / symbol."));
        });
    }

    /**
     * The sends the connection warning for hopping clans.
     */
    void sendConnectionWarning(Player player, ClanMember member) {
        player.dialogueFactory.sendStatement(
                "-<col=FF0000>WARNING</col>-",
                "Contributing to this clan or gaining a rank will remove",
                "all of your progress from your current clan <col=FF4444>" + player.lastClan + "</col>.",
                "Are you sure you want to join?")
                .sendOption(
                        "Yes, I accept.",
                        () -> channel.add(player, member),
                        "No, I don't want to risk it.",
                        () -> player.clanChannel = null).execute();
    }

    public static void manageMember(Player player, int button) {
        ClanChannel channel = player.clanChannel;
        if (channel == null)
            return;
        channel.getMember(player.getName()).ifPresent(member -> {
            if (!channel.canManage(member))
                return;

            List<ClanMember> sorted = new ArrayList<>(channel.getActiveMembers());
            sorted.sort(player.settings.clanMemberComporator);

            int ordinal = ClanUtility.getRankOrdinal(button);
            if (ordinal >= sorted.size())
                return;

            ClanMember other = sorted.get(ordinal);
            if (other != null && !other.name.equalsIgnoreCase(player.getName()) && other.rank.lessThan(ClanRank.LEADER)) {
                player.attributes.set("CLAN_RANK_MEMBER", other);
                player.send(new SendString(other.name, 43606));
                player.interfaceManager.open(43600);
            }
        });
    }

    /**
     * Handles updating the clan channel member list.
     */
    public void updateMemberList(ClanMember member) {
        Player player = member.player.orElse(null);
        if (player == null)
            return;
        int size = channel.size() < 10 ? 10 : channel.size();
        player.send(new SendString("Talking in: <col=F7DC6F>" + Utility.formatName(channel.getName()), 33502));
        player.send(new SendConfig(393, channel.lootshareEnabled() ? 1 : 0));
        player.send(new SendScrollbar(33530, size * 22));


        List<ClanMember> members = new LinkedList<>();
        members.addAll(channel.getMembers());
        members.sort(player.settings.clanMemberComporator);

        Iterator<ClanMember> iterator = members.iterator();
        boolean tooltip = channel.canManage(member);
        int string = 33532;

        for (int index = 0; index < size * 3; index++) {
            ClanMember nextMember = null;
            if (iterator.hasNext()) {
                nextMember = iterator.next();
            }
            if (nextMember == null || (!nextMember.player.isPresent() && index / 3 >= members.size())) {
                player.send(new SendString("", string++));
                player.send(new SendString("", string));
                player.send(new SendTooltip("", string++));
                player.send(new SendString("", string++));
                string++;
            } else if (nextMember.player.isPresent()) {
                player.send(new SendString(nextMember.rank.getString(), string++));
                player.send(new SendString(nextMember.name, string));
                player.send(new SendTooltip(tooltip ? (nextMember.name.equals(player.getName()) ? "" : "Manage " + nextMember.name) : "", string++));
                player.send(new SendString("", string++));
                string++;
            }
        }
    }

    /**
     * Handles cleaning the clan tab itemcontainer.
     */
    public static void clean(Player player) {
        player.send(new SendString("Talking in: <col=F7DC6F>None", 33502));
        for (int i = 0; i < 99; i += 4) {
            player.send(new SendString("", 33531 + (i + 1)));
            player.send(new SendString("", 33531 + (i + 2)));
            player.send(new SendString("", 33531 + (i + 3)));
        }
        player.send(new SendScrollbar(33530, 11 * 22));
    }

}
