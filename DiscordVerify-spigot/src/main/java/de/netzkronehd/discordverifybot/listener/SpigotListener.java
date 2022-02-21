package de.netzkronehd.discordverifybot.listener;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.player.SpigotDiscordPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpigotListener implements Listener {

    private final DiscordVerifyBot discordVerifyBot;

    public SpigotListener(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final SpigotDiscordPlayer sp = new SpigotDiscordPlayer(e.getPlayer());
        discordVerifyBot.join(sp);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        final DiscordPlayer dp = discordVerifyBot.getPlayer(e.getPlayer().getUniqueId());
        if(dp != null) {
            discordVerifyBot.leave(dp);
        }
    }

}
