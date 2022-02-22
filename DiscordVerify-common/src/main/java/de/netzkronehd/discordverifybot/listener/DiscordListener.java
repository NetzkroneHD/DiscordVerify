package de.netzkronehd.discordverifybot.listener;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {

    private final DiscordVerifyBot discordVerifyBot;

    public DiscordListener(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        e.getJDA().getPresence().setStatus(discordVerifyBot.getBot().getLoadedStatus());
        e.getJDA().getPresence().setActivity(discordVerifyBot.getBot().getLoadedActivity());
        discordVerifyBot.getBot().setGuild(e.getJDA().getGuildById(discordVerifyBot.getBot().getGuildId()));
        if(discordVerifyBot.getBot().getGuild() == null) {
            discordVerifyBot.getLogger().severe("Cloud not find guild '"+discordVerifyBot.getBot().getGuildId()+"'.");
            return;
        }

        discordVerifyBot.getGroupManager().getGroups().values().forEach(group -> {
            final Role role = discordVerifyBot.getBot().getGuild().getRoleById(group.getRoleId());
            if(role != null) {
                group.setRole(role);
            } else discordVerifyBot.getLogger().severe("Cloud not find role of the group '"+group.getId()+"'. Please check if this is the correct RoleId '"+group.getRoleId()+"'.");
        });


    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if(e.getMessage().getContentRaw().startsWith("!")) {
            discordVerifyBot.getDiscordCommandManager().executeCommand(e.getMessage().getContentRaw(), e.getAuthor(), e.getMember(), e.getChannel(), e.getMessage());
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent e) {
        final DiscordVerification dv = discordVerifyBot.getVerifyManager().getVerification(e.getUser().getId());
        if(dv != null) {
            discordVerifyBot.getThreadService().runAsync(() -> discordVerifyBot.getVerifyManager().unVerify(e.getUser().getId(), verifyResult -> {}));
        }
    }
}
