package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.message.SpigotMessageFormatter;
import de.netzkronehd.discordverifybot.service.impl.SpigotCommandService;
import de.netzkronehd.discordverifybot.service.impl.SpigotEventService;
import de.netzkronehd.discordverifybot.service.impl.SpigotThreadService;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordVerifySpigot extends JavaPlugin {

    private static DiscordVerifySpigot instance;
    private DiscordVerifyBot discordVerifyBot;

    @Override
    public void onLoad() {
        instance = this;
        final DiscordVerifyBot discordVerifyBot = new DiscordVerifyBot(getLogger(), PluginVersion.SPIGOT, new SpigotThreadService(), new SpigotEventService(), new SpigotMessageFormatter(), new SpigotCommandService());
        DiscordVerifyBot.setInstance(discordVerifyBot);
        this.discordVerifyBot = discordVerifyBot;
    }

    @Override
    public void onEnable() {
        discordVerifyBot.onLoad();
    }

    @Override
    public void onDisable() {

    }

    public DiscordVerifyBot getDiscordVerifyBot() {
        return discordVerifyBot;
    }

    public static DiscordVerifySpigot getInstance() {
        return instance;
    }
}
