package de.netzkronehd.discordverifybot.bot;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class DiscordBot {

    private final static GatewayIntent[] INTENTS = {
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS};

    private final String token;
    private final Activity loadingActivity, loadedActivity;
    private final String guildId;
    private JDA jda;
    private Guild guild;
    private final OnlineStatus loadingStatus, loadedStatus;
    private final DiscordVerifyBot discordVerifyBot;

    public DiscordBot(DiscordVerifyBot discordVerifyBot, String token, Activity loadingActivity, Activity loadedActivity, String guildId, OnlineStatus loadingStatus, OnlineStatus loadedStatus) {
        this.discordVerifyBot = discordVerifyBot;
        this.token = token;
        this.loadingActivity = loadingActivity;
        this.loadedActivity = loadedActivity;
        this.guildId = guildId;
        this.loadingStatus = loadingStatus;
        this.loadedStatus = loadedStatus;

    }

    public void connect() {
        if(jda == null) {
            try {
                jda = JDABuilder.create(token, Arrays.asList(INTENTS))
                        .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                        .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE)
                        .setActivity(loadingActivity)
                        .setStatus(loadingStatus).build();
            } catch (LoginException e) {
                e.printStackTrace();
                jda = null;
                discordVerifyBot.getLogger().severe("Cloud not login.");
            }
        }
    }

    public void disconnect(boolean now) {
        if(discordVerifyBot.isReady()) {
            if(now) jda.shutdownNow();
            else jda.shutdown();
        }
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public String getGuildId() {
        return guildId;
    }

    public Guild getGuild() {
        return guild;
    }

    public DiscordVerifyBot getDiscordVerifyBot() {
        return discordVerifyBot;
    }

    public JDA getJda() {
        return jda;
    }

    public Activity getLoadedActivity() {
        return loadedActivity;
    }

    public Activity getLoadingActivity() {
        return loadingActivity;
    }

    public OnlineStatus getLoadingStatus() {
        return loadingStatus;
    }

    public OnlineStatus getLoadedStatus() {
        return loadedStatus;
    }
}
