package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
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
                                discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                                discordVerifyBot.getVerifyManager().verify(dp, member, verifyResult -> {
                                    if(verifyResult.isSucceed()) {
                                        member.getUser().openPrivateChannel().queue(privateChannel ->
                                                privateChannel.sendMessage("You successfully linked your account with `"+dp.getName()+"`.").queue(message ->
                                                        dp.sendMessage("You successfully linked your account with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7.")));
                                    } else {
                                        member.getUser().openPrivateChannel().queue(privateChannel ->
                                                privateChannel.sendMessage("The linking with `"+dp.getName()+"` was cancelled.").queue(message ->
                                                        dp.sendMessage("The linking with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7 was&c cancelled&7.")));
                                    }
                                });
                            });
                        } else {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                            dp.sendMessage("That user is already verified.");
                        }
                    } else dp.sendMessage("You didn't received a request.");
                } else dp.sendMessage("You're already verified.");
            } else if(args[0].equalsIgnoreCase("deny")) {
                if (!dp.isVerified()) {
                    final String userId = discordVerifyBot.getVerifyManager().getRequestsByDiscord().get(dp.getUuid());
                    if(userId != null) {
                        if(!discordVerifyBot.getVerifyManager().isVerified(userId)) {
                            discordVerifyBot.getBot().getGuild().retrieveMemberById(userId).queue(member ->
                                    member.getUser().openPrivateChannel().queue(privateChannel ->
                                            privateChannel.sendMessage("`"+dp.getName()+"` denied your request.").queue(message ->
                                                    dp.sendMessage("You successfully denied the request."))));
                        } else {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                            dp.sendMessage("That user is already verified.");
                        }
                    } else dp.sendMessage("You didn't received a request.");
                } else dp.sendMessage("You're already verified.");
            } else if(args[0].equalsIgnoreCase("delete")) {
                if(dp.isVerified()) {
                    dp.getVerification().getMember().queue(member -> discordVerifyBot.getVerifyManager().unVerify(dp.getUuid(), verifyResult -> {
                        if(verifyResult.isSucceed()) {
                            member.getUser().openPrivateChannel().queue(privateChannel ->
                                    privateChannel.sendMessage("The link to `"+dp.getName()+"` was removed.").queue(message ->
                                            dp.sendMessage("You successfully unlinked your account with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7.")));
                        } else {
                            dp.sendMessage("The process of unlinking with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7 was&c cancelled&7.");
                        }
                    }), throwable -> discordVerifyBot.getVerifyManager().unVerify(dp.getUuid(), verifyResult -> {
                        if(verifyResult.isSucceed()) {
                            dp.sendMessage("You successfully unlinked your account.");
                        } else {
                            dp.sendMessage("The process of unlinking was&c cancelled&7.");
                        }
                    }));
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
    public List<String> onTabComplete(DiscordPlayer dp, String[] args) {
        final List<String> tabs = new ArrayList<>();
        if(args.length == 1) {
            args[0] = args[0].toLowerCase();
            if("accept".startsWith(args[0])) tabs.add("accept");
            if("deny".startsWith(args[0])) tabs.add("deny");
            if("delete".startsWith(args[0])) tabs.add("delete");

        }
        return tabs;
    }

    private void sendHelp(DiscordPlayer dp) {

    }

}
