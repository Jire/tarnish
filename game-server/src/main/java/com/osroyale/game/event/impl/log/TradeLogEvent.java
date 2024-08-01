package com.osroyale.game.event.impl.log;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.osroyale.game.service.PostgreService;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class TradeLogEvent extends LogEvent {

    private final Player player;
    private final Item[] items;
    private final Player other;
    private final Item[] otherItems;

    public TradeLogEvent(Player player, Item[] items, Player other, Item[] otherItems) {
        this.player = player;
        this.items = items;
        this.other = other;
        this.otherItems = otherItems;
    }

    @Override
    public void onLog() throws Exception {
        final JdbcSession session = new JdbcSession(PostgreService.getConnectionPool());

        long logId = session.autocommit(false)
                .sql("INSERT INTO log.log(log_time) VALUES (?::timestamp) RETURNING id")
                .set(dateTime)
                .insert(new SingleOutcome<>(Long.class));

        for (Item item : items) {

            if (item == null) {
                continue;
            }

            session.sql("INSERT INTO log.trade_log(log_id, item_id, amount, sender_id, receiver_id) VALUES (?, ?, ?, ?, ?)")
                    .set(logId)
                    .set(item.getId())
                    .set(item.getAmount())
                    .set(player.getMemberId())
                    .set(other.getMemberId())
                    .execute();
        }

        for (Item item : otherItems) {
            if (item == null) {
                continue;
            }
            session.sql("INSERT INTO log.trade_log(log_id, item_id, amount, sender_id, receiver_id) VALUES (?, ?, ?, ?, ?)")
                    .set(logId)
                    .set(item.getId())
                    .set(item.getAmount())
                    .set(other.getMemberId())
                    .set(player.getMemberId())
                    .execute();

        }

        session.commit();

    }

}
