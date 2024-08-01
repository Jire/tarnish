package com.osroyale.game.event.impl.log;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.osroyale.game.service.PostgreService;
import com.osroyale.game.world.entity.mob.player.Player;

public class PrivateMessageChatLogEvent extends LogEvent {

    private final Player sender;
    private final Player receiver;
    private final String decoded;

    public PrivateMessageChatLogEvent(Player sender, Player receiver, String decoded) {
        this.sender = sender;
        this.receiver = receiver;
        this.decoded = decoded;
    }

    @Override
    public void onLog() throws Exception {
        JdbcSession session = new JdbcSession(PostgreService.getConnectionPool());
        long logId = session.autocommit(false)
                .sql("INSERT INTO log.log(log_time) VALUES (?::timestamp) RETURNING id")
                .set(dateTime)
                .insert(new SingleOutcome<>(Long.class));

        session.sql("INSERT INTO log.pm_log(log_id, sender_id, receiver_id, message) VALUES (?, ?, ?, ?)")
                .set(logId)
                .set(sender.getMemberId())
                .set(receiver.getMemberId())
                .set(decoded)
                .execute()
                .commit();
    }

}
