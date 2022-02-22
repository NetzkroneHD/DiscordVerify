package de.netzkronehd.discordverifybot.listener;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.player.BungeeDiscordPlayer;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListener implements Listener {

    private final DiscordVerifyBot discordVerifyBot;

    public BungeeListener(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    @EventHandler
    public void onLogin(PostLoginEvent e) {
        final BungeeDiscordPlayer bp = new BungeeDiscordPlayer(e.getPlayer());
        discordVerifyBot.join(bp, e);
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        final DiscordPlayer dp = discordVerifyBot.getPlayer(e.getPlayer().getUniqueId());
        if(dp != null) {
            discordVerifyBot.leave(dp, e);
        }
    }

}
