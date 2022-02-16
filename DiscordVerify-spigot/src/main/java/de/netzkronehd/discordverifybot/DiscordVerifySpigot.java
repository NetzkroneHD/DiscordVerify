package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.thread.SpigotThreadService;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordVerifySpigot extends JavaPlugin {

    private static DiscordVerifySpigot instance;

    @Override
    public void onEnable() {
        instance = this;
        final DiscordVerifyBot discordVerifyBot = new DiscordVerifyBot(getLogger(), PluginVersion.SPIGOT, new SpigotThreadService());
        DiscordVerifyBot.setInstance(discordVerifyBot);
        discordVerifyBot.onLoad();
    }

    @Override
    public void onDisable() {

    }

    public static DiscordVerifySpigot getInstance() {
        return instance;
    }
}
