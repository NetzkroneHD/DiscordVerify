package de.netzkronehd.discordverifybot.commands;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.entities.*;

public abstract class DiscordCommand {

    private final String command;
    protected final DiscordVerifyBot discordVerifyBot;

    public DiscordCommand(String command) {
        this.command = command;
        discordVerifyBot = DiscordVerifyBot.getInstance();
    }

    public DiscordCommand(String command, DiscordVerifyBot discordVerifyBot) {
        this.command = command;
        this.discordVerifyBot = discordVerifyBot;
    }

    public abstract void onExecute(User sender, PrivateChannel privateChannel, String[] args, Message message);
    public abstract void onExecute(Member sender, TextChannel textChannel, String[] args, Message message);

    public void onExecute(User sender, MessageChannel messageChannel, String[] args, Message message) {}

    public void register() {
        discordVerifyBot.getDiscordCommandManager().registerCommand(this);
    }

    public String getCommand() {
        return command;
    }

    public DiscordVerifyBot getDiscordVerifyBot() {
        return discordVerifyBot;
    }
}
