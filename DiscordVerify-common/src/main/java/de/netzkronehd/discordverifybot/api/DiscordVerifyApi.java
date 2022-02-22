package de.netzkronehd.discordverifybot.api;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.bot.DiscordBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;

import java.util.UUID;

public class DiscordVerifyApi {

    private final DiscordVerifyBot discordVerifyBot;

    public DiscordVerifyApi(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    public DiscordVerification getVerification(UUID uuid) {
        return discordVerifyBot.getVerifyManager().getVerification(uuid);
    }

    public DiscordVerification getVerification(String userId) {
        return discordVerifyBot.getVerifyManager().getVerification(userId);
    }

    public void registerCommand(DiscordCommand dc) {
        discordVerifyBot.getDiscordCommandManager().registerCommand(dc);
    }

    public void unregisterCommand(DiscordCommand dc) {
        discordVerifyBot.getDiscordCommandManager().unregisterCommand(dc);
    }

    public void unregisterCommand(String commandPrefix) {
        discordVerifyBot.getDiscordCommandManager().unregisterCommand(commandPrefix);
    }

    public DiscordBot getBot() {
        return discordVerifyBot.getBot();
    }

    public boolean isReady() {
        return discordVerifyBot.isReady();
    }

}

