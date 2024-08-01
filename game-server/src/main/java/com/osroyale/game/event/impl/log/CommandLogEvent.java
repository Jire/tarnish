package com.osroyale.game.event.impl.log;

import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import com.osroyale.game.service.PostgreService;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;

import java.util.Arrays;

public class CommandLogEvent extends LogEvent {

    private final Player player;
    private final CommandParser parser;

    public CommandLogEvent(Player player, CommandParser parser) {
        this.player = player;
        this.parser = parser;
    }

    @Override
    public void onLog() throws Exception {
       JdbcSession session = new JdbcSession(PostgreService.getConnectionPool());
        long logId = session.autocommit(false)
                .sql("INSERT INTO log.log(log_time) VALUES (?::timestamp) RETURNING id")
                .set(dateTime)
                .insert(new SingleOutcome<>(Long.class));

                session.sql("INSERT INTO log.command_log(player_id, name, argument, log_id) VALUES (?, ?, ?, ?)")
                .set(player.getMemberId())
                .set(parser.getCommand())
                .set(Arrays.toString(parser.getArguments()).replace("[", "").replace("]", ""))
                .set(logId)
                .execute()
                .commit();
    }

}
