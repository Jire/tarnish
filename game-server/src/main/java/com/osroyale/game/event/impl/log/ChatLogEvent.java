package com.osroyale.game.event.impl.log;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.osroyale.game.service.PostgreService;
import com.osroyale.game.world.entity.mob.player.Player;

public final class ChatLogEvent extends LogEvent {

    private final Player player;
    private final String message;

    public ChatLogEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }

    @Override
    public void onLog() throws Exception {
        JdbcSession session = new JdbcSession(PostgreService.getConnectionPool());
        long logId = session.autocommit(false)
                .sql("INSERT INTO log.log(log_time) VALUES (?::timestamp) RETURNING id")
                .set(dateTime)
                .insert(new SingleOutcome<>(Long.class));
                session.sql("INSERT INTO log.chat_log(player_id, message, log_id) VALUES (?, ?, ?)")
                .set(player.getMemberId())
                .set(message)
                .set(logId)
                .execute()
                .commit();
    }

}
