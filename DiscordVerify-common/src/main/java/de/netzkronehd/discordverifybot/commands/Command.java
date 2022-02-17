package de.netzkronehd.discordverifybot.commands;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;

import java.util.List;

public abstract class Command {

    protected final DiscordVerifyBot discordVerifyBot;

    public Command(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    public abstract void onExecute(DiscordPlayer dp, String[] args);
    public abstract List<String> onTabComplete(DiscordPlayer dp, String[] args);

}
