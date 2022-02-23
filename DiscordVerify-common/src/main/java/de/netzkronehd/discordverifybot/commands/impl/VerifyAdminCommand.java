package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;

import java.util.List;

public class VerifyAdminCommand extends Command {

    public VerifyAdminCommand(DiscordVerifyBot discordVerifyBot, String name, String... alias) {
        super(discordVerifyBot, name, alias);
    }

    @Override
    public void onExecute(DiscordPlayer dp, String[] args) {
        if(dp.hasPermission("discordverify.verifyadmin")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    final long before = System.currentTimeMillis();
                    discordVerifyBot.getThreadService().runAsync(() -> {
                        dp.sendMessage(Message.PREFIX+"Reloading...");
                        try {
                            discordVerifyBot.onReload();
                            dp.sendMessage(Message.PREFIX+"Reloaded after "+(System.currentTimeMillis()-before)+"ms.");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dp.sendMessage(Message.PREFIX+"&cAn error occurred while reloading, please check the console for details.");
                        }
                    });
                } else sendHelp(dp);
            } else sendHelp(dp);
        } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.NO_PERMISSIONS, null, null, null);
    }

    @Override
    public List<String> onTabComplete(DiscordPlayer dp, String[] args) {
        return null;
    }

    private void sendHelp(DiscordPlayer dp) {

    }
}
