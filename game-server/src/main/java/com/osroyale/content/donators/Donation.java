package com.osroyale.content.donators;

import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.content.writer.impl.InformationWriter;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.out.SendAnnouncement;
import com.osroyale.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles everything related to donators.
 *
 * @author Daniel
 */
public class Donation {

    private static final Logger logger = LogManager.getLogger();

    private final Player player;
    private int credits;
    private int spent;

    public Donation(Player player) {
        this.player = player;
    }

    public void redeem(DonatorBond bond) {
        setSpent(getSpent() + bond.moneySpent);
        setCredits(getCredits() + bond.credits);
        player.message("<col=FF0000>You have claimed your donator bond. You now have " + Utility.formatDigits(getCredits()) + " donator credits!");
        World.sendMessage("<col=CF2192>Tarnish: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has opened <col=CF2192>" + Utility.formatEnum(bond.name()) + "");
        updateRank(false);
    }

    public void updateRank(boolean login) {
        PlayerRight rank = PlayerRight.forSpent(spent);

        if (rank == null) {
            return;
        }

        if (player.right.equals(rank)) {
            return;
        }

        if (login) {
            if (!PlayerRight.isIronman(player) && !PlayerRight.isHelper(player)) {
                player.right = rank;
                player.updateFlags.add(UpdateFlag.APPEARANCE);
            }
            return;
        }

        if (PlayerRight.isIronman(player)) {
            player.message("Since you are an iron man, your rank icon will not change.");
        } else if (!PlayerRight.isModerator(player)) {
            player.right = rank;
            player.updateFlags.add(UpdateFlag.APPEARANCE);
        }

        String name = rank.getName();
        player.send(new SendAnnouncement("Rank Level Up!", "You are now " + Utility.getAOrAn(name) + " " + PlayerRight.getCrown(player) + " " + name + "!", 0x1C889E));
        player.dialogueFactory.sendStatement("You are now " + Utility.getAOrAn(name) + " " + PlayerRight.getCrown(player) + " " + name + "!").execute();
        InterfaceWriter.write(new InformationWriter(player));
    }

    public int getCredits() {
        return credits;
    }

    public int getSpent() {
        return spent;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }
}
