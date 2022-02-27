package de.netzkronehd.discordverifybot.service.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.DiscordVerifyBungeeCord;
import de.netzkronehd.discordverifybot.commands.BungeeCommand;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.service.CommandService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeCommandService implements CommandService {

    @Override
    public void registerCommand(Command command) {
        DiscordVerifyBungeeCord.getInstance().getProxy().getPluginManager().registerCommand(DiscordVerifyBungeeCord.getInstance(), new BungeeCommand(command.getName(), null, command, command.getAlias().toArray(new String[0])) {
            @Override
            public void execute(CommandSender sender, String[] args) {
                if(sender instanceof ProxiedPlayer) {
                    final DiscordPlayer dp = DiscordVerifyBot.getInstance().getPlayer(((ProxiedPlayer)sender).getUniqueId());
                    if(dp != null) {
                        command.onExecute(dp, args);
                    }
                }
            }
        });
    }
}
