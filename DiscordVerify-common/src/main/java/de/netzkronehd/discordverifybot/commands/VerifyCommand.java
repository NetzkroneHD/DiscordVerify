package de.netzkronehd.discordverifybot.commands;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;

import java.util.List;

public class VerifyCommand extends Command {

    public VerifyCommand(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
    }

    @Override
    public void onExecute(DiscordPlayer dp, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("delete")) {
                if(dp.isVerified()) {

                } else dp.sendMessage("You're not verified.");
            } else if(args[0].equalsIgnoreCase("update")) {
                dp.sendMessage("Updateing verification...");
                discordVerifyBot.getThreadService().runAsync(() -> {

                });
            } else if(args[0].contains("#")) {

            }
        }
    }

    @Override
    public List<String> onTabComplete() {
        return null;
    }
}
