package com.osroyale.game.event.impl.log;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.osroyale.game.service.PostgreService;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.ground.GroundItem;

public class PickupItemLogEvent extends LogEvent {

    private final Player player;
    private final GroundItem groundItem;

    public PickupItemLogEvent(Player player, GroundItem groundItem) {
        this.player = player;
        this.groundItem = groundItem;
    }

    @Override
    public void onLog() throws Exception {
        if ((groundItem.item.getValue() * groundItem.item.getAmount()) < 250_000) {
            return;
        }

        JdbcSession session = new JdbcSession(PostgreService.getConnectionPool());
        long logId = session.autocommit(false)
                .sql("INSERT INTO log.log(log_time) VALUES (?::timestamp) RETURNING id")
                .set(dateTime)
                .insert(new SingleOutcome<>(Long.class));

        session.sql("INSERT INTO log.pickup_item_log(player_id, log_id, item_id, item_amount) VALUES (?, ?, ?, ?)")
                .set(player.getMemberId())
                .set(logId)
                .set(groundItem.item.getId())
                .set(groundItem.item.getAmount())
                .execute()
                .commit();
    }

}
