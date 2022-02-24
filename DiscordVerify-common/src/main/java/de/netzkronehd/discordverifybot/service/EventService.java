package de.netzkronehd.discordverifybot.service;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.UUID;

public interface EventService {

    void fireReadyEvent(DiscordVerifyBot discordVerifyBot);

    boolean fireVerifyEvent(DiscordPlayer discordPlayer, Member member);
    void fireVerifiedEvent(DiscordPlayer discordPlayer, DiscordVerification verification);

    boolean fireUnVerifyEvent(UUID uuid, String userId);
    void fireUnVerifiedEvent(UUID uuid, String userId);

    void fireVerificationUpdatedEvent(DiscordVerification verification);

    boolean fireDiscordCommandPreProcessEvent(DiscordCommand discordCommand, User sender, MessageChannel messageChannel, String[] args, Message message);

}
