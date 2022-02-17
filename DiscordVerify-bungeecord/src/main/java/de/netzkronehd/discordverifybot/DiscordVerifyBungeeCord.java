package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.message.BungeeMessageFormatter;
import de.netzkronehd.discordverifybot.service.BungeeEventService;
import de.netzkronehd.discordverifybot.service.BungeeThreadService;
import net.md_5.bungee.api.plugin.Plugin;

public final class DiscordVerifyBungeeCord extends Plugin {

    private static DiscordVerifyBungeeCord instance;

    private DiscordVerifyBot discordVerifyBot;

    @Override
    public void onLoad() {
        instance = this;
        final DiscordVerifyBot discordVerifyBot = new DiscordVerifyBot(getLogger(), PluginVersion.BUNGEECORD, new BungeeThreadService(), new BungeeEventService(), new BungeeMessageFormatter());
        this.discordVerifyBot = discordVerifyBot;
        DiscordVerifyBot.setInstance(discordVerifyBot);
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

    public static DiscordVerifyBungeeCord getInstance() {
        return instance;
    }
}
