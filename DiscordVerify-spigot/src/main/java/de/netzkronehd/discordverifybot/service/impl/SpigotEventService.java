package de.netzkronehd.discordverifybot.service.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.service.EventService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class SpigotEventService implements EventService {


    @Override
    public void fireReadyEvent(DiscordVerifyBot discordVerifyBot) {

    }

    @Override
    public boolean fireVerifyEvent(DiscordPlayer discordPlayer, Member member) {
        return false;
    }

    @Override
    public void fireVerifiedEvent(DiscordPlayer discordPlayer, Member member) {

    }

    @Override
    public boolean fireUnVerifyEvent(DiscordPlayer discordPlayer, Member member) {
        return false;
    }

    @Override
    public void fireUnVerifiedEvent(DiscordPlayer discordPlayer, Member member) {

    }

    @Override
    public boolean fireDiscordCommandPreProcessEvent(DiscordCommand discordCommand, User sender, MessageChannel messageChannel, String[] args, Message message) {
        return false;
    }
}
