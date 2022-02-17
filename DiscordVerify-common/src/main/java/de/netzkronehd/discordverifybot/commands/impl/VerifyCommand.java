package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class VerifyCommand extends Command {

    public VerifyCommand(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
    }

    @Override
    public void onExecute(DiscordPlayer dp, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("accept")) {
                if (!dp.isVerified()) {
                    final String userId = discordVerifyBot.getVerifyManager().getRequestsByDiscord().get(dp.getUuid());
                    if(userId != null) {
                        if(!discordVerifyBot.getVerifyManager().isVerified(userId)) {
                            discordVerifyBot.getBot().getGuild().retrieveMemberById(userId).queue(member -> {

                            });
                        } else dp.sendMessage("That user is already verified.");
                    } else dp.sendMessage("You didn't received a request.");
                } else dp.sendMessage("You're already verified.");
            } else if(args[0].equalsIgnoreCase("deny")) {
                if (!dp.isVerified()) {

                } else dp.sendMessage("You're already verified.");
            } else if(args[0].equalsIgnoreCase("delete")) {
                if (dp.isVerified()) {

                } else dp.sendMessage("You're not verified.");
            } else if(args[0].equalsIgnoreCase("update")) {
                dp.sendMessage("Updateing verification...");
                discordVerifyBot.getThreadService().runAsync(() -> {

                });

            } else if(args[0].contains("#")) {
                dp.sendMessage("Searching user...");
                discordVerifyBot.getBot().getGuild().loadMembers().onSuccess(members -> {
                    for(Member member : members) {
                        if((member.getUser().getName()+"#"+member.getUser().getDiscriminator()).equals(args[0])) {

                        }
                    }
                }).onError(throwable -> {

                });
            } else sendHelp(dp);
        }
    }

    @Override
    public List<String> onTabComplete() {
        return null;
    }

    private void sendHelp(DiscordPlayer dp) {

    }

}
