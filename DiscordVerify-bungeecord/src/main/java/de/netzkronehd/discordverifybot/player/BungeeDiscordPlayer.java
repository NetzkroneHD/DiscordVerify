package de.netzkronehd.discordverifybot.player;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeDiscordPlayer implements DiscordPlayer {

    private final ProxiedPlayer proxiedPlayer;
    private DiscordVerification verification;

    public BungeeDiscordPlayer(ProxiedPlayer proxiedPlayer) {
        this.proxiedPlayer = proxiedPlayer;
    }

    @Override
    public void sendMessage(String text) {
        sendMessage(new TextComponent(text));
    }

    @Override
    public void sendMessage(TextComponent textComponent) {
        proxiedPlayer.sendMessage(textComponent);
    }

    @Override
    public void sendMessage(Message message) {
        DiscordVerifyBot.getInstance().getMessageFormatter().sendMessage(this, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return proxiedPlayer.hasPermission(permission);
    }

    public void setVerification(DiscordVerification verification) {
        this.verification = verification;
    }

    @Override
    public UUID getUuid() {
        return proxiedPlayer.getUniqueId();
    }

    @Override
    public String getName() {
        return proxiedPlayer.getName();
    }

    @Override
    public DiscordVerification getVerification() {
        return verification;
    }

    @Override
    public boolean isVerified() {
        return verification != null;
    }

    public ProxiedPlayer getProxiedPlayer() {
        return proxiedPlayer;
    }
}
