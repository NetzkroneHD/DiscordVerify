package de.netzkronehd.discordverifybot.placeholder;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DiscordVerifyExpansion extends PlaceholderExpansion {

    private final DiscordVerifyBot discordVerifyBot;

    public DiscordVerifyExpansion(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "discordverify";
    }

    @Override
    public String getAuthor() {
        return "NetzkroneHD";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        final DiscordPlayer dp = discordVerifyBot.getPlayer(p.getUniqueId());
        if(dp != null) {
            onRequest(dp, params);
        }
        return Message.PLACEHOLDER_PLAYER_IS_OFFLINE.toString();
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        final DiscordPlayer dp = discordVerifyBot.getPlayer(p.getUniqueId());
        if(dp != null) {
            onRequest(dp, params);
        }
        return Message.PLACEHOLDER_PLAYER_IS_OFFLINE.toString();
    }

    public String onRequest(DiscordPlayer dp, String params) {
        if(params != null) {
            if(params.equalsIgnoreCase("member")) {
                if(dp.isVerified()) {
                    if(dp.getVerification().getCachedMember() != null) {
                        return dp.getVerification().getCachedMemberName();
                    }
                } else return Message.PLACEHOLDER_NOT_VERIFIED.toString();
            }
        }
        return Message.PLACEHOLDER_WRONG_USAGE.toString();
    }

}
