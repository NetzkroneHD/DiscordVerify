package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;

import java.util.ArrayList;
import java.util.List;

public class VerifyAdminCommand extends Command {

    public VerifyAdminCommand(DiscordVerifyBot discordVerifyBot, String name, String... alias) {
        super(discordVerifyBot, name, alias);
    }

    @Override
    public void onExecute(DiscordPlayer dp, String[] args) {
        if(dp.hasPermission("discordverify.verifyadmin")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    final long before = System.currentTimeMillis();
                    dp.sendMessage(Message.PREFIX+"§7Reloading...");
                    discordVerifyBot.getThreadService().runAsync(() -> {
                        try {
                            discordVerifyBot.onReload();
                            dp.sendMessage(Message.PREFIX+"§7Reloaded after §e"+(System.currentTimeMillis()-before)+"ms§7.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dp.sendMessage(Message.PREFIX+"§cAn error occurred while reloading, please check the console for details.");
                        }
                    });
                } else sendHelp(dp);
            } else sendHelp(dp);
        } else dp.sendMessage(Message.NO_PERMISSIONS);
    }

    @Override
    public List<String> onTabComplete(DiscordPlayer dp, String[] args) {
        final List<String> tabs = new ArrayList<>();
        if(dp.hasPermission("discordverify.verifyadmin")) {
            if(args.length == 1) {
                args[0] = args[0].toLowerCase();
                if("reload".startsWith(args[0])) tabs.add("reload");
                if("rl".startsWith(args[0])) tabs.add("rl");
            }
        }
        return tabs;
    }

    private void sendHelp(DiscordPlayer dp) {
        discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.HELP_ADMIN);
    }
}
