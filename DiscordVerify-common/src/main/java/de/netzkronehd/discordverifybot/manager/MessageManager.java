package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;

import java.io.File;

public class MessageManager extends Manager {

    private final File file;
    private YamlConfiguration cfg;

    public MessageManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        file = new File("plugins/DiscordVerifyBot", "messages.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onReload() {

    }
}
