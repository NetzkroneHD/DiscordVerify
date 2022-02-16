package de.netzkronehd.discordverifybot.player;

import de.netzkronehd.discordverifybot.verification.DiscordVerification;

import java.util.UUID;

public interface DiscordPlayer {

    void sendMessage(String text);
    boolean hasPermission(String permission);
    void setVerification(DiscordVerification verification);


    UUID getUuid();
    String getName();
    DiscordVerification getVerification();
    boolean isVerified();

}
