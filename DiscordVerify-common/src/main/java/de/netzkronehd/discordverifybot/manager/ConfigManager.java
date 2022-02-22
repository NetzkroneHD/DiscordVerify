package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            cfg.set("Bot.Loading-Activity-Method", "");
            cfg.set("Bot.Loading-Activity-Value", "");
            cfg.set("Bot.Loaded-Activity-Method", "");
            cfg.set("Bot.Loaded-Activity-Value", "");

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
            final Method ac = Activity.class.getMethod(cfg.getString("Bot.Loading-Activity-Method"), String.class);
            ac.setAccessible(true);
            loadingActivity = (Activity) ac.invoke(this, cfg.getString("Bot.Loading-Activity-Value"));

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
            loadingActivity = Activity.playing(cfg.getString("Bot.Loading-Activity-Value"));
            discordVerifyBot.getBot().getDiscordVerifyBot().getLogger().warning("Cloud not find method '"+cfg.getString("Bot.Loading-Activity-Method")+"', using 'playing' instead.");
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
