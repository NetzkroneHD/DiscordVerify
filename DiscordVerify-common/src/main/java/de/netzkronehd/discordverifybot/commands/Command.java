package de.netzkronehd.discordverifybot.commands;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Command {

    protected final DiscordVerifyBot discordVerifyBot;
    private final String name;
    private final List<String> alias;

    public Command(String name, String... alias) {
        this.discordVerifyBot = DiscordVerifyBot.getInstance();
        this.name = name;
        this.alias = Collections.unmodifiableList(Arrays.asList(alias));
    }

    public Command(DiscordVerifyBot discordVerifyBot, String name, String... alias) {
        this.discordVerifyBot = discordVerifyBot;
        this.name = name;
        this.alias = Collections.unmodifiableList(Arrays.asList(alias));
    }

    public abstract void onExecute(DiscordPlayer dp, String[] args);
    public abstract List<String> onTabComplete(DiscordPlayer dp, String[] args);

    public void register() {
        discordVerifyBot.getCommandService().registerCommand(this);
    }

    public String getName() {
        return name;
    }
    public List<String> getAlias() {
        return alias;
    }
    public DiscordVerifyBot getDiscordVerifyBot() {
        return discordVerifyBot;
    }
}
