package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.database.Database;
import de.netzkronehd.discordverifybot.manager.DatabaseManager;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.thread.ThreadService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class DiscordVerifyBot {

    private static DiscordVerifyBot instance;

    private final HashMap<UUID, DiscordPlayer> playerCache;
    private Database database;
    private DatabaseManager databaseManager;
    private final Logger logger;
    private final PluginVersion pluginVersion;
    private final ThreadService threadService;


    public DiscordVerifyBot(Logger logger, PluginVersion pluginVersion, ThreadService threadService) {
        playerCache = new HashMap<>();
        this.logger = logger;
        this.pluginVersion = pluginVersion;
        this.threadService = threadService;
    }

    public void onLoad() {
        databaseManager = new DatabaseManager(this);
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void join(DiscordPlayer discordPlayer) {
        playerCache.put(discordPlayer.getUuid(), discordPlayer);
    }

    public void leave(DiscordPlayer discordPlayer) {
        playerCache.remove(discordPlayer.getUuid());
    }

    public PluginVersion getPluginVersion() {
        return pluginVersion;
    }
    public Database getDatabase() {
        return database;
    }
    public Logger getLogger() {
        return logger;
    }
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public Collection<DiscordPlayer> getPlayers() {
        return Collections.unmodifiableCollection(playerCache.values());
    }
    public ThreadService getThreadService() {
        return threadService;
    }

    public static void setInstance(DiscordVerifyBot instance) {
        DiscordVerifyBot.instance = instance;
    }

    public static DiscordVerifyBot getInstance() {
        return instance;
    }


}
