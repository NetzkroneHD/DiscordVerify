package de.netzkronehd.discordverifybot.listener;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.bot.DiscordBot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DiscordListener extends ListenerAdapter {

    private final DiscordVerifyBot discordVerifyBot;

    public DiscordListener(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        e.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
        try {
            final Method activityMethod = Activity.class.getMethod(discordVerifyBot.getBot().getActivity(), String.class);
            activityMethod.setAccessible(true);
            e.getJDA().getPresence().setActivity((Activity) activityMethod.invoke(this, discordVerifyBot.getBot().getActivityValue()));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
            e.getJDA().getPresence().setActivity(Activity.playing(discordVerifyBot.getBot().getActivityValue()));
            discordVerifyBot.getBot().getDiscordVerifyBot().getLogger().warning("Cloud not find method '"+discordVerifyBot.getBot().getActivity()+"', using 'playing' instead.");
        }
        discordVerifyBot.getBot().setGuild(e.getJDA().getGuildById(discordVerifyBot.getBot().getGuildId()));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if(e.getMessage().getContentRaw().startsWith("!")) {
            discordVerifyBot.getDiscordCommandManager().executeCommand(e.getMessage().getContentRaw(), e.getAuthor(), e.getMember(), e.getChannel(), e.getMessage());
        }
    }
}
