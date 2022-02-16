package de.netzkronehd.discordverifybot.thread;

import de.netzkronehd.discordverifybot.DiscordVerifySpigot;
import org.bukkit.Bukkit;

public class SpigotThreadService implements ThreadService {

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscordVerifySpigot.getInstance(), runnable);
    }
}
