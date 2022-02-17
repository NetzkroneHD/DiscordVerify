package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.DiscordVerifyApi;
import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.bot.DiscordBot;
import de.netzkronehd.discordverifybot.database.Database;
import de.netzkronehd.discordverifybot.manager.*;
import de.netzkronehd.discordverifybot.message.MessageFormatter;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.service.EventService;
import de.netzkronehd.discordverifybot.service.ThreadService;

import java.util.*;
import java.util.logging.Logger;

public class DiscordVerifyBot {

    private static DiscordVerifyBot instance;

    private final HashMap<UUID, DiscordPlayer> playerCache;
    private final HashMap<String, DiscordPlayer> playerNameCache;
    private final Logger logger;
    private final PluginVersion pluginVersion;
    private final ThreadService threadService;
    private final EventService eventService;
    private final MessageFormatter messageFormatter;

    private DiscordBot bot;
    private Database database;

    private final DiscordVerifyApi api;

    //Manager
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private DiscordCommandManager discordCommandManager;
    private GroupManager groupManager;
    private MessageManager messageManager;
    private VerifyManager verifyManager;

    public DiscordVerifyBot(Logger logger, PluginVersion pluginVersion, ThreadService threadService, EventService eventService, MessageFormatter messageFormatter) {
        this.logger = logger;
        this.pluginVersion = pluginVersion;
        this.threadService = threadService;
        this.eventService = eventService;
        this.messageFormatter = messageFormatter;

        playerCache = new HashMap<>();
        playerNameCache = new HashMap<>();
        api = new DiscordVerifyApi(this);

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
    public EventService getEventService() {
        return eventService;
    }
    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
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

    public boolean isReady() {
        return (bot != null && bot.getJda() != null && !Objects.requireNonNull(bot.getJda().getPresence().getActivity(), "Activity can't be null").getName().equalsIgnoreCase("loading..."));
    }

    public DiscordVerifyApi getApi() {
        return api;
    }

    public static void setInstance(DiscordVerifyBot instance) {
        DiscordVerifyBot.instance = instance;
    }
    public static DiscordVerifyBot getInstance() {
        return instance;
    }


}
