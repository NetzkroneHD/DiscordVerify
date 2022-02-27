package de.netzkronehd.discordverifybot;

import de.netzkronehd.discordverifybot.api.DiscordVerifyApi;
import de.netzkronehd.discordverifybot.api.PluginVersion;
import de.netzkronehd.discordverifybot.bot.DiscordBot;
import de.netzkronehd.discordverifybot.commands.impl.DiscordVerifyCommand;
import de.netzkronehd.discordverifybot.commands.impl.VerifyAdminCommand;
import de.netzkronehd.discordverifybot.commands.impl.VerifyCommand;
import de.netzkronehd.discordverifybot.database.Database;
import de.netzkronehd.discordverifybot.manager.*;
import de.netzkronehd.discordverifybot.message.MessageFormatter;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.service.CommandService;
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
    private final CommandService commandService;

    private DiscordBot bot;
    private Database database;

    private final DiscordVerifyApi api;

    //Manager
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private final DiscordCommandManager discordCommandManager;
    private GroupManager groupManager;
    private MessageManager messageManager;
    private VerifyManager verifyManager;

    public DiscordVerifyBot(Logger logger, PluginVersion pluginVersion, ThreadService threadService, EventService eventService, MessageFormatter messageFormatter, CommandService commandService) {
        this.logger = logger;
        this.pluginVersion = pluginVersion;
        this.threadService = threadService;
        this.eventService = eventService;
        this.messageFormatter = messageFormatter;
        this.commandService = commandService;

        discordCommandManager = new DiscordCommandManager(this);

        playerCache = new HashMap<>();
        playerNameCache = new HashMap<>();
        api = new DiscordVerifyApi(this);

    }

    public void onLoad() {
        final long before = System.currentTimeMillis();

        logger.info("Initialize and loading managers...");

        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this);
        groupManager = new GroupManager(this);
        messageManager = new MessageManager(this);
        verifyManager = new VerifyManager(this);

        Manager.loadManagers();

        logger.info("Managers initialized and loaded.");
        logger.info("Loading DiscordBot...");

        new VerifyAdminCommand(this, "discordverifyadmin", "dcverifyadmin", "dcvadmin").register();
        new VerifyCommand(this, "discordverify", "dcverify", "verify", "link", "dclink", "linkdiscord", "linkdc").register();
        discordCommandManager.registerCommand(new DiscordVerifyCommand("!verify", this));

        loadBot();

        logger.info("System fully enabled after ("+(System.currentTimeMillis()-before)+"ms).");

    }


    public void onReload() {
        if(bot != null && bot.getJda() != null) {
            logger.info("Disabling jda...");
            bot.getJda().shutdownNow();
            logger.info("Jda successfully disabled.");
        }
        Manager.reloadManagers();

        logger.info("Loading DiscordBot...");
        loadBot();

    }

    private void loadBot() {
        if(!configManager.getToken().equalsIgnoreCase("token")) {
            bot = new DiscordBot(this, configManager.getToken(), configManager.getLoadingActivity(), configManager.getLoadedActivity(), configManager.getGuildId(), configManager.getLoadingStatus(), configManager.getLoadedStatus());
            bot.connect();
        } else logger.severe("Token can't be '"+configManager.getToken()+"'.");

    }

    public void onDisable() {
        if(bot != null) bot.disconnect(true);
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void join(DiscordPlayer discordPlayer, Object root) {
        if(!(root.getClass().getName().equalsIgnoreCase("org.bukkit.event.player.PlayerJoinEvent") || root.getClass().getName().equalsIgnoreCase("net.md_5.bungee.api.event.PostLoginEvent"))) return;

        playerCache.put(discordPlayer.getUuid(), discordPlayer);
        playerNameCache.put(discordPlayer.getName().toLowerCase(), discordPlayer);

        discordPlayer.setVerification(verifyManager.getVerification(discordPlayer.getUuid()));
        if(discordPlayer.isVerified()) {
            if(isReady()) {
                verifyManager.updateVerification(discordPlayer);
            }
            if(!discordPlayer.getVerification().getName().equalsIgnoreCase(discordPlayer.getName())) {
                threadService.runAsync(() -> verifyManager.updateName(discordPlayer.getUuid(), discordPlayer.getName()));
            }
        }

    }

    public void leave(DiscordPlayer discordPlayer, Object root) {
        if(!(root.getClass().getName().equalsIgnoreCase("org.bukkit.event.player.PlayerQuitEvent") || root.getClass().getName().equalsIgnoreCase("net.md_5.bungee.api.event.PlayerDisconnectEvent"))) return;
        playerCache.remove(discordPlayer.getUuid());
        playerNameCache.remove(discordPlayer.getName().toLowerCase());
    }

    public DiscordBot getBot() {
        return bot;
    }
    public PluginVersion getPluginVersion() {
        return pluginVersion;
    }
    public ThreadService getThreadService() {
        return threadService;
    }
    public CommandService getCommandService() {
        return commandService;
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
        return (bot != null && bot.getJda() != null && !Objects.requireNonNull(bot.getJda().getPresence().getActivity(), "Activity can't be null").getName().equalsIgnoreCase(configManager.getLoadingActivity().getName()));
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
