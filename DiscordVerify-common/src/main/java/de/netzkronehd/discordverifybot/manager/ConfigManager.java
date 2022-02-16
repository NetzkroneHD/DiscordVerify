package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;

import java.io.File;

public class ConfigManager extends Manager {


    private final File file;
    private YamlConfiguration cfg;

    private boolean multipleGroups;

    public ConfigManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        file = new File("plugins/DiscordVerifyBot", "config.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onLoad() {
        createFile();
        readFile();
    }

    @Override
    public void onReload() {
        cfg = YamlConfiguration.loadConfiguration(file);
        onLoad();
    }


    public boolean isMultipleGroups() {
        return multipleGroups;
    }
}
