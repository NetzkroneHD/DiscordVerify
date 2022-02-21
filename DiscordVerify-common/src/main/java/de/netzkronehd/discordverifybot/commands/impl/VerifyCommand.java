package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.Command;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;

public class VerifyCommand extends Command {


    public VerifyCommand(DiscordVerifyBot discordVerifyBot, String name, String... alias) {
        super(discordVerifyBot, name, alias);
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
                                                privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_SUCCESSFULLY_LINKED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                                        discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_LINKED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)));
                                    } else {
                                        member.getUser().openPrivateChannel().queue(privateChannel ->
                                                privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_FAILED_TO_VERIFY, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                                        discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.FAILED_TO_VERIFY, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)));
                                    }
                                });
                            });
                        } else {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.ALREADY_VERIFIED, dp.getName(), null, null);
                        }
                    } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.DID_NOT_RECEIVED_A_REQUEST, dp.getName(), null, null);
                } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.ALREADY_VERIFIED, dp.getName(), null, null);
            } else if(args[0].equalsIgnoreCase("deny")) {
                if (!dp.isVerified()) {
                    final String userId = discordVerifyBot.getVerifyManager().getRequestsByDiscord().get(dp.getUuid());
                    if(userId != null) {
                        if(!discordVerifyBot.getVerifyManager().isVerified(userId)) {
                            discordVerifyBot.getBot().getGuild().retrieveMemberById(userId).queue(member ->
                                    member.getUser().openPrivateChannel().queue(privateChannel ->
                                            privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_PLAYER_DENIED_REQUEST, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                                    discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_DENIED_REQUEST, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null))));
                        } else {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.USER_ALREADY_VERIFIED, dp.getName(), null, null);
                        }
                    } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.DID_NOT_RECEIVED_A_REQUEST, dp.getName(), null, null);
                } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.ALREADY_VERIFIED, dp.getName(), null, null);
            } else if(args[0].equalsIgnoreCase("delete")) {
                if(dp.isVerified()) {
                    dp.getVerification().getMember().queue(member -> discordVerifyBot.getVerifyManager().unVerify(dp.getUuid(), verifyResult -> {
                        if(verifyResult.isSucceed()) {
                            member.getUser().openPrivateChannel().queue(privateChannel ->
                                    privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_LINK_WAS_REMOVED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_UNLINKED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)));
                        } else {
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.UNLINKING_PROCESS_FAILED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null);
                        }
                    }), throwable -> discordVerifyBot.getVerifyManager().unVerify(dp.getUuid(), verifyResult -> {
                        if(verifyResult.isSucceed()) {
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_UNLINKED, dp.getName(), null, null);
                        } else {
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.UNLINKING_PROCESS_FAILED, dp.getName(), null, null);
                        }
                    }));
                } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.NOT_VERIFIED, dp.getName(), null, null);
            } else if(args[0].equalsIgnoreCase("update")) {
                discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.UPDATING, dp.getName(), null, null);
                discordVerifyBot.getThreadService().runAsync(() ->
                        discordVerifyBot.getVerifyManager().updateVerification(dp, verifyResult -> {
                    if(verifyResult.isSucceed()) {
                        discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_UPDATED, dp.getName(), null, null);
                    } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.FAILED_TO_UPDATE, dp.getName(), null, null);
                }));

            } else if(args[0].contains("#")) {
                dp.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.SEARCHING_USER, dp.getName(), null, null));
                discordVerifyBot.getBot().getGuild().loadMembers().onSuccess(members -> {
                    for(Member member : members) {
                        if((member.getUser().getName()+"#"+member.getUser().getDiscriminator()).equals(args[0])) {

                            return;
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
