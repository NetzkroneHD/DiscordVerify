package de.netzkronehd.discordverifybot.commands;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class BungeeCommand extends Command implements TabExecutor {

    private final de.netzkronehd.discordverifybot.commands.Command command;

    public BungeeCommand(String name, de.netzkronehd.discordverifybot.commands.Command command) {
        super(name);
        this.command = command;
    }

    public BungeeCommand(String name, String permission, de.netzkronehd.discordverifybot.commands.Command command, String... aliases) {
        super(name, permission, aliases);
        this.command = command;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        final List<String> tabs = new ArrayList<>();
        if(sender instanceof ProxiedPlayer) {
            final DiscordPlayer dp = command.getDiscordVerifyBot().getPlayer(((ProxiedPlayer)sender).getUniqueId());
            if(dp != null) command.onTabComplete(dp, args);
        }
        return tabs;
    }
}
