package de.netzkronehd.discordverifybot.service.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBungeeCord;
import de.netzkronehd.discordverifybot.service.ThreadService;
import net.md_5.bungee.api.ProxyServer;

public class BungeeThreadService implements ThreadService {


    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(DiscordVerifyBungeeCord.getInstance(), runnable);
    }
}
