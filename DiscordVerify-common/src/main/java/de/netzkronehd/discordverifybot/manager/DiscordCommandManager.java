package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import net.dv8tion.jda.api.entities.*;

import java.util.Arrays;
import java.util.HashMap;

public class DiscordCommandManager extends Manager {

    private final HashMap<String, DiscordCommand> commands;

    public DiscordCommandManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        commands = new HashMap<>();
    }


    @Override
    public void onLoad() {

    }

    @Override
    public void onReload() {

    }

    public void registerCommand(DiscordCommand dc) {
        commands.put(dc.getCommand(), dc);
    }
    public void unregisterCommand(String commandPrefix) {
        commandPrefix = commandPrefix.toLowerCase()+((commandPrefix.contains("!") ? "":"!"));
        commands.remove(commandPrefix);
    }
    public void unregisterCommand(DiscordCommand dc) {
        commands.remove(dc.getCommand());
    }

    public void executeCommand(String commandLine, User user, Member member, MessageChannel messageChannel, Message message) {
        if(commandLine.contains(" ")) {
            final String[] argsWithCommand = commandLine.split(" ");
            final DiscordCommand dc = commands.get(argsWithCommand[0].toLowerCase());
            if (dc != null) {
                final String[] args = Arrays.copyOfRange(argsWithCommand, 1, argsWithCommand.length);

                if(!discordVerifyBot.getEventService().fireDiscordCommandPreProcessEvent(dc, user, messageChannel, args, message)) {
                    if(messageChannel instanceof TextChannel) {
                        dc.onExecute(member, (TextChannel) message, args, message);
                    } else if(messageChannel instanceof PrivateChannel) {
                        dc.onExecute(user, (PrivateChannel) message, args, message);
                    }
                }


            }
        } else {
            final DiscordCommand dc = commands.get(commandLine.toLowerCase());
            if (dc != null) {
                final String[] args = commandLine.split(" ");
                if(!discordVerifyBot.getEventService().fireDiscordCommandPreProcessEvent(dc, user, messageChannel, args, message)) {
                    if(messageChannel instanceof TextChannel) {
                        dc.onExecute(member, (TextChannel) message, args, message);
                    } else if(messageChannel instanceof PrivateChannel) {
                        dc.onExecute(user, (PrivateChannel) message, args, message);
                    }
                }
            }
        }
    }

}
