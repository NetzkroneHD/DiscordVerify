package de.netzkronehd.discordverifybot.service.impl;

import de.netzkronehd.discordverifybot.DiscordVerifySpigot;
import de.netzkronehd.discordverifybot.service.ThreadService;
import org.bukkit.Bukkit;

public class SpigotThreadService implements ThreadService {

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifySpigot.getInstance(), runnable);
    }
}
