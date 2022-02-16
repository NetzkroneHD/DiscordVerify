package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.group.Group;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class GroupManager extends Manager {

    private final HashMap<String, Group> groups;
    private Group defaultGroup;
    private final File file;
    private YamlConfiguration cfg;

    public GroupManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        groups = new HashMap<>();
        file = new File("plugins/DiscordVerifyBot", "groups.yml");
        cfg = YamlConfiguration.loadConfiguration(file);

    }

    @Override
    public void onLoad() {
        createFile();
        readFile();
    }

    @Override
    public void onReload() {
        groups.clear();
        cfg = YamlConfiguration.loadConfiguration(file);
        onLoad();
    }

    public List<Group> getGroups(DiscordPlayer dp) {
        return null;
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }
}
