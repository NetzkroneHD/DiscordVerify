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

    public final static GatewayIntent[] INTENTS = {
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS};

    private final String token, activity, activityValue;
    private final String guildId;
    private JDA jda;
    private Guild guild;
    private final DiscordVerifyBot discordVerifyBot;

    public DiscordBot(DiscordVerifyBot discordVerifyBot, String token, String activity, String activityValue, String guildId) {
        this.discordVerifyBot = discordVerifyBot;
        this.token = token;
        this.activity = activity;
        this.activityValue = activityValue;
        this.guildId = guildId;

    }

    public void connect() {
        if(jda == null) {
            try {
                jda = JDABuilder.create(token, Arrays.asList(INTENTS))
                        .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                        .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE)
                        .setActivity(Activity.playing("loading..."))
                        .setStatus(OnlineStatus.DO_NOT_DISTURB).build();
            } catch (LoginException e) {
                e.printStackTrace();
                jda = null;
            }

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

    public String getActivity() {
        return activity;
    }

    public String getActivityValue() {
        return activityValue;
    }
}
