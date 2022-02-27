package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigManager extends Manager {


    private final File file;
    private YamlConfiguration cfg;

    private String token, language, guildId;
    private Activity loadingActivity, loadedActivity;
    private OnlineStatus loadingStatus, loadedStatus;
    private boolean multipleGroups;

    public ConfigManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        setPriority(1);

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

    @Override
    public void createFile() {
        if(!file.exists()) {
            cfg.set("Language", "EN");
            cfg.set("Multiple-Groups", false);
            cfg.set("Bot.Token", "token");

            cfg.set("Bot.GuildId", "-1");

            cfg.set("Bot.Loading.Activity-Method", "playing");
            cfg.set("Bot.Loading.Activity-Value", "loading...");
            cfg.set("Bot.Loading.Status", OnlineStatus.DO_NOT_DISTURB.name());

            cfg.set("Bot.Loaded.Activity-Method", "playing");
            cfg.set("Bot.Loaded.Activity-Value", "Made by NetzkroneHD");
            cfg.set("Bot.Loaded.Status", OnlineStatus.ONLINE.name());
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void readFile() {
        language = cfg.getString("Language");
        token = cfg.getString("Bot-Token");
        multipleGroups = cfg.getBoolean("Multiple-Groups");

        try {
            final Method ac = Activity.class.getMethod(Objects.requireNonNull(cfg.getString("Bot.Loading-Activity-Method")), String.class);
            ac.setAccessible(true);
            loadingActivity = (Activity) ac.invoke(this, cfg.getString("Bot.Loading-Activity-Value"));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
            loadingActivity = Activity.playing(Objects.requireNonNull(cfg.getString("Bot.Loading-Activity-Value")));
            discordVerifyBot.getBot().getDiscordVerifyBot().getLogger().warning("Could not find method '"+cfg.getString("Bot.Loading-Activity-Method")+"', using 'playing' instead.");
        }

        try {
            final Method ac = Activity.class.getMethod(Objects.requireNonNull(cfg.getString("Bot.Loaded-Activity-Method")), String.class);
            ac.setAccessible(true);
            loadedActivity = (Activity) ac.invoke(this, cfg.getString("Bot.Loaded-Activity-Value"));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
            loadedActivity = Activity.playing(Objects.requireNonNull(cfg.getString("Bot.Loaded-Activity-Value")));
            discordVerifyBot.getBot().getDiscordVerifyBot().getLogger().warning("Could not find method '"+cfg.getString("Bot.Loading-Loaded-Method")+"', using 'playing' instead.");
        }

        loadingStatus = OnlineStatus.fromKey(cfg.getString("Bot.Loading.Status"));
        loadedStatus = OnlineStatus.fromKey(cfg.getString("Bot.Loaded.Status"));

        if(loadingStatus == OnlineStatus.UNKNOWN) {
            log(Level.WARNING, "Loading.Status can't be '"+loadingStatus.name()+"'. Using 'ONLINE' instead.");
            loadingStatus = OnlineStatus.ONLINE;
        }

        if(loadedStatus == OnlineStatus.UNKNOWN) {
            log(Level.WARNING, "Loaded.Status can't be '"+loadedStatus.name()+"'. Using 'ONLINE' instead.");
            loadedStatus = OnlineStatus.ONLINE;
        }


    }

    public String getGuildId() {
        return guildId;
    }

    public boolean isMultipleGroups() {
        return multipleGroups;
    }

    public Activity getLoadedActivity() {
        return loadedActivity;
    }

    public Activity getLoadingActivity() {
        return loadingActivity;
    }

    public OnlineStatus getLoadedStatus() {
        return loadedStatus;
    }

    public OnlineStatus getLoadingStatus() {
        return loadingStatus;
    }

    public String getToken() {
        return token;
    }

    public String getLanguage() {
        return language;
    }
}
