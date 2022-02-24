package de.netzkronehd.discordverifybot.api.event.bungee;

import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class BungeeDiscordCommandPreProcessEvent extends Event implements Cancellable {

    private final DiscordCommand command;
    private final User sender;
    private final MessageChannel messageChannel;
    private final String[] args;
    private final Message message;
    private boolean cancel;

    public BungeeDiscordCommandPreProcessEvent(DiscordCommand command, User sender, MessageChannel messageChannel, String[] args, Message message) {
        this.command = command;
        this.sender = sender;
        this.messageChannel = messageChannel;
        this.args = args;
        this.message = message;
    }

    public DiscordCommand getCommand() {
        return command;
    }

    public User getSender() {
        return sender;
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    public String[] getArgs() {
        return args;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
