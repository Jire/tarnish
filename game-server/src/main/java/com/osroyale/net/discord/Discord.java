package com.osroyale.net.discord;


import com.osroyale.Config;
import com.osroyale.Starter;
import com.osroyale.game.world.WorldType;
import com.osroyale.util.Stopwatch;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.Channel;

import java.util.Objects;

/**
 * Handles creating a connection to the OS Royale community discord channel.
 * <p>
 * !!! - > Better way of handling the community and administration message instance.
 * When creating the channel instance by using client.getChannelById(long)
 * the channel is always null. I am "cheap haxing" it by initializing the channel
 * in the onMessage event. Problem with this is if you start the server from fresh the
 * Discord bot will not be able to send outgoing messages (message) until someone
 * Has triggered the discord event.
 *
 * @author Daniel
 */
public final class Discord {
    
    /**
     * The community channel identification.
     */
    static final Snowflake COMMUNITY_CHANNEL = Snowflake.of(0L);

    /**
     * The discord client.
     */
    public static DiscordClient client;

    public static GatewayDiscordClient gateway;

    /**
     * The community channel instance.
     */
    static Channel communityChannel;

    /**
     * The discord stopwatch (prevent spamming).
     */
    public static Stopwatch stopwatch = Stopwatch.start();

    /**
     * Handles starting the discord bot.
     */
    public static void start(Starter starter) {
        if (Config.WORLD_TYPE == WorldType.LIVE) {
            client = DiscordClient.create(Config.DISCORD_TOKEN);
            gateway = Objects.requireNonNull(client.login().block());
            gateway.on(MessageCreateEvent.class).subscribe(new DiscordDispatcher(starter.getUptime()));
            communityChannel = gateway.getChannelById(Discord.COMMUNITY_CHANNEL).block();
        }
    }

    public static void message(final String message) {
        if (Config.WORLD_TYPE == WorldType.LIVE && communityChannel != null) {
            communityChannel.getRestChannel().createMessage(message).block();
        }
    }

}
