package de.netzkronehd.discordverifybot.player;

import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public interface DiscordPlayer {

    void sendMessage(String text);
    void sendMessage(TextComponent textComponent);
    boolean hasPermission(String permission);
    void setVerification(DiscordVerification verification);


    UUID getUuid();
    String getName();
    DiscordVerification getVerification();
    boolean isVerified();

}
