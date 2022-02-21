package de.netzkronehd.discordverifybot.service.impl;

import com.google.common.collect.ImmutableList;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.DiscordVerifySpigot;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.service.CommandService;
import org.bukkit.entity.Player;

public class SpigotCommandService implements CommandService {


    @Override
    public void registerCommand(Command command) {
        DiscordVerifySpigot.getInstance().getCommand(command.getName()).setExecutor((sender, cmd, label, args) -> {
            if(sender instanceof Player) {
                final DiscordPlayer dp = DiscordVerifyBot.getInstance().getPlayer(((Player)sender).getUniqueId());
                if(dp != null) {
                    command.onExecute(dp, args);
                }
            }
            return false;
        });
        DiscordVerifySpigot.getInstance().getCommand(command.getName()).setAliases(command.getAlias());
        DiscordVerifySpigot.getInstance().getCommand(command.getName()).setTabCompleter((sender, cmd, alias, args) -> {
            if(sender instanceof Player) {
                final DiscordPlayer dp = DiscordVerifyBot.getInstance().getPlayer(((Player)sender).getUniqueId());
                if(dp != null) {
                    return command.onTabComplete(dp, args);
                }
            }
            return ImmutableList.of();
        });
    }
}
