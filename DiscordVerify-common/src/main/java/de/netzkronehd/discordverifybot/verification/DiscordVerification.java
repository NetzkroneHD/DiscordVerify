package de.netzkronehd.discordverifybot.verification;

import java.util.UUID;

public interface DiscordVerification {

    UUID getUuid();
    String getName();
    String getDiscordId();

    void setName(String name);

}
