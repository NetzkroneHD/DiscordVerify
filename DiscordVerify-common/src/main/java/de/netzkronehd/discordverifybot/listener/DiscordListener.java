package de.netzkronehd.discordverifybot.listener;

import de.netzkronehd.discordverifybot.bot.DiscordBot;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DiscordListener extends ListenerAdapter {

    private final DiscordBot bot;

    public DiscordListener(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        e.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
        try {
            final Method activityMethod = Activity.class.getMethod(bot.getActivity(), String.class);
            activityMethod.setAccessible(true);
            e.getJDA().getPresence().setActivity((Activity) activityMethod.invoke(this, bot.getActivityValue()));
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
            e.getJDA().getPresence().setActivity(Activity.playing(bot.getActivityValue()));
            bot.getDiscordVerifyBot().getLogger().warning("Cloud not find method '"+bot.getActivity()+"', using 'playing' instead.");
        }

        bot.setGuild(e.getJDA().getGuildById(bot.getGuildId()));


    }
}
