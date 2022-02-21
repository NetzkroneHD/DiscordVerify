package de.netzkronehd.discordverifybot.service;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface EventService {

    void fireReadyEvent(DiscordVerifyBot discordVerifyBot);

    boolean fireVerifyEvent(DiscordPlayer discordPlayer, Member member);
    void fireVerifiedEvent(DiscordPlayer discordPlayer, Member member);

    boolean fireUnVerifyEvent(DiscordPlayer discordPlayer, Member member);
    void fireUnVerifiedEvent(DiscordPlayer discordPlayer, Member member);

    boolean fireDiscordCommandPreProcessEvent(DiscordCommand discordCommand, User sender, MessageChannel messageChannel, String[] args, Message message);

}
