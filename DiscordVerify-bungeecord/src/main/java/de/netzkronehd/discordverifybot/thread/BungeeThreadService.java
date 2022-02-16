package de.netzkronehd.discordverifybot.thread;

import de.netzkronehd.discordverifybot.DiscordVerifyBungeeCord;
import net.md_5.bungee.api.ProxyServer;

public class BungeeThreadService implements ThreadService {


    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(DiscordVerifyBungeeCord.getInstance(), runnable);
    }
}
