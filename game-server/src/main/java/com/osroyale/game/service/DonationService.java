package com.osroyale.game.service;

import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.donators.DonatorBond;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public final class DonationService {
    private static final Logger logger = LogManager.getLogger();
    private static final String USER = "osroyjs_exo1";
    private static final String PASS = "3AXbU=W7IfzX";
    private static final String CONNECTION_STRING = "jdbc:mysql://173.82.152.23:3306/osroyjs_store_2";

    private DonationService() {

    }

    public static void claimDonation(Player player) {
        if (!player.databaseRequest.elapsed(1, TimeUnit.MINUTES)) {
            player.dialogueFactory.sendNpcChat(5523,"You can only check our database once every 1 minute!").execute();
            return;
        }

        if (!player.inventory.isEmpty()) {
            player.dialogueFactory.sendNpcChat(5523, Expression.SAD, "You must have an empty inventory to do this!").execute();
            return;
        }

        player.dialogueFactory.sendStatement("Checking request...").execute();
        boolean claimed = false;
        player.databaseRequest.reset();

        try(Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER, PASS);
            PreparedStatement sta = connection.prepareStatement("SELECT * FROM payments WHERE player_name = ? AND status='Completed' AND claimed=0", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            sta.setString(1, player.getName().replace("_", " "));

            ResultSet rs = sta.executeQuery();


            while(rs.next()) {
                final int itemNumber = rs.getInt("item_number");
                final int quantity = rs.getInt("quantity");

                switch(itemNumber) {
                    //$10 DONATOR BOND
                    case 10:
                        if (player.inventory.add(new Item(DonatorBond.BOND_10.item, quantity))) {
                            claimed = true;
                        }
                        break;

                    //$50 DONATOR BOND
                    case 20:
                        if (player.inventory.add(new Item(DonatorBond.BOND_50.item, quantity))) {
                            claimed = true;
                        }
                        break;

                    //$100 DONATOR BOND
                    case 21:
                        if (player.inventory.add(new Item(DonatorBond.BOND_100.item, quantity))) {
                            claimed = true;
                        }
                        break;

                    //$200 DONATOR BOND
                    case 22:
                        if (player.inventory.add(new Item(DonatorBond.BOND_200.item, quantity))) {
                            claimed = true;
                        }
                        break;

                    //$500 DONATOR BOND
                    case 23:
                        if (player.inventory.add(new Item(DonatorBond.BOND_500.item, quantity))) {
                            claimed = true;
                        }
                        break;
                }

                if (claimed) {
                    rs.updateInt("claimed", 1);
                    rs.updateRow();
                }

            }

            if (!claimed) {
                player.dialogueFactory.sendNpcChat(5523,"There is no donation reward for you to claim!").execute();
            } else {
                player.dialogueFactory.sendNpcChat(5523,"Thank-you for your donation!").execute();
            }

        } catch (SQLException ex) {
            logger.error(String.format("Failed to claim donation for player=%s", player.getName()), ex);
        }
    }
}
