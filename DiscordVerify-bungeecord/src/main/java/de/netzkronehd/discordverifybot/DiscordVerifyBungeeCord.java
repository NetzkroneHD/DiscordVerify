package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.thread.BungeeThreadService;
import net.md_5.bungee.api.plugin.Plugin;

public final class DiscordVerifyBungeeCord extends Plugin {

    private static DiscordVerifyBungeeCord instance;

    @Override
    public void onEnable() {
        instance = this;
        final DiscordVerifyBot discordVerifyBot = new DiscordVerifyBot(getLogger(), PluginVersion.BUNGEECORD, new BungeeThreadService());
        DiscordVerifyBot.setInstance(discordVerifyBot);
        discordVerifyBot.onLoad();
    }

    @Override
    public void onDisable() {

    }

    public static DiscordVerifyBungeeCord getInstance() {
        return instance;
    }
}
