package de.netzkronehd.discordverifybot.api.event.bungee;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeUnVerifyEvent extends Event implements Cancellable {

    private final UUID uuid;
    private final String userId;
    private boolean cancel;

    public BungeeUnVerifyEvent(UUID uuid, String userId) {
        this.uuid = uuid;
        this.userId = userId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
