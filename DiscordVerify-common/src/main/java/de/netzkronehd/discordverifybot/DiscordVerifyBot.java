package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.bot.DiscordBot;
import de.netzkronehd.discordverifybot.database.Database;
import de.netzkronehd.discordverifybot.manager.*;
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
    private final HashMap<String, DiscordPlayer> playerNameCache;
    private final Logger logger;
    private final PluginVersion pluginVersion;
    private final ThreadService threadService;

    private DiscordBot bot;
    private Database database;

    //Manager
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private DiscordCommandManager discordCommandManager;
    private GroupManager groupManager;
    private MessageManager messageManager;
    private VerifyManager verifyManager;

    public DiscordVerifyBot(Logger logger, PluginVersion pluginVersion, ThreadService threadService) {
        playerCache = new HashMap<>();
        playerNameCache = new HashMap<>();
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
        playerNameCache.put(discordPlayer.getName().toLowerCase(), discordPlayer);
    }

    public void leave(DiscordPlayer discordPlayer) {
        playerCache.remove(discordPlayer.getUuid());
        playerNameCache.remove(discordPlayer.getName().toLowerCase());
    }

    public DiscordBot getBot() {
        return bot;
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
    public Collection<DiscordPlayer> getPlayers() {
        return Collections.unmodifiableCollection(playerCache.values());
    }
    public ThreadService getThreadService() {
        return threadService;
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public DiscordCommandManager getDiscordCommandManager() {
        return discordCommandManager;
    }
    public GroupManager getGroupManager() {
        return groupManager;
    }
    public MessageManager getMessageManager() {
        return messageManager;
    }
    public VerifyManager getVerifyManager() {
        return verifyManager;
    }

    public DiscordPlayer getPlayer(UUID uuid) {
        return playerCache.get(uuid);
    }
    public DiscordPlayer getPlayer(String name) {
        return playerNameCache.get(name.toLowerCase());
    }

    public static void setInstance(DiscordVerifyBot instance) {
        DiscordVerifyBot.instance = instance;
    }
    public static DiscordVerifyBot getInstance() {
        return instance;
    }


}
