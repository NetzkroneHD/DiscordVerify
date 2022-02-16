package de.netzkronehd.discordverifybot.verification.impl;

import de.netzkronehd.discordverifybot.verification.DiscordVerification;

import java.util.UUID;

public class DiscordVerificationImpl implements DiscordVerification {

    private final UUID uuid;
    private String name;
    private final String discordId;

    public DiscordVerificationImpl(UUID uuid, String name, String discordId) {
        this.uuid = uuid;
        this.name = name;
        this.discordId = discordId;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDiscordId() {
        return discordId;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
