package de.netzkronehd.discordverifybot.api.event.bungee;

import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeUnVerifiedEvent extends Event {

    private final UUID uuid;
    private final String userId;

    public BungeeUnVerifiedEvent(UUID uuid, String userId) {
        this.uuid = uuid;
        this.userId = userId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUserId() {
        return userId;
    }

}
