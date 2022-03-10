package de.netzkronehd.discordverifybot.player;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotDiscordPlayer implements DiscordPlayer {

    private final Player player;
    private DiscordVerification verification;

    public SpigotDiscordPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void sendMessage(String text) {
        player.sendMessage(text);
    }

    @Override
    public void sendMessage(TextComponent textComponent) {
        player.spigot().sendMessage(textComponent);
    }

    @Override
    public void sendMessage(Message message) {
        DiscordVerifyBot.getInstance().getMessageFormatter().sendMessage(this, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void setVerification(DiscordVerification verification) {
        this.verification = verification;
    }

    @Override
    public UUID getUuid() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public DiscordVerification getVerification() {
        return verification;
    }

    @Override
    public boolean isVerified() {
        return verification != null;
    }

    public Player getPlayer() {
        return player;
    }
}
