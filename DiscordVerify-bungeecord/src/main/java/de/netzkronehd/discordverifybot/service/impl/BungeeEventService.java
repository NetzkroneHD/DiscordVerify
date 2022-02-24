package de.netzkronehd.discordverifybot.service.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.service.EventService;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.UUID;

public class BungeeEventService implements EventService {


    @Override
    public void fireReadyEvent(DiscordVerifyBot discordVerifyBot) {

    }

    @Override
    public boolean fireVerifyEvent(DiscordPlayer discordPlayer, Member member) {
        return false;
    }

    @Override
    public void fireVerifiedEvent(DiscordPlayer discordPlayer, DiscordVerification verification) {

    }

    @Override
    public boolean fireUnVerifyEvent(UUID uuid, String userId) {
        return false;
    }

    @Override
    public void fireUnVerifiedEvent(UUID uuid, String userId) {

    }

    @Override
    public void fireVerificationUpdatedEvent(DiscordVerification verification) {

    }

    @Override
    public boolean fireDiscordCommandPreProcessEvent(DiscordCommand discordCommand, User sender, MessageChannel messageChannel, String[] args, Message message) {
        return false;
    }
}
