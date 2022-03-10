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
                                                        discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_LINKED)));
                                    } else {
                                        member.getUser().openPrivateChannel().queue(privateChannel ->
                                                privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_FAILED_TO_VERIFY, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                                        dp.sendMessage(Message.FAILED_TO_VERIFY)));
                                    }
                                });
                            });
                        } else {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                            dp.sendMessage(Message.ALREADY_VERIFIED);
                        }
                    } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.DID_NOT_RECEIVED_A_REQUEST);
                } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.ALREADY_VERIFIED);
            } else if(args[0].equalsIgnoreCase("deny")) {
                if (!dp.isVerified()) {
                    final String userId = discordVerifyBot.getVerifyManager().getRequestsByDiscord().get(dp.getUuid());
                    if(userId != null) {
                        if(!discordVerifyBot.getVerifyManager().isVerified(userId)) {
                            discordVerifyBot.getBot().getGuild().retrieveMemberById(userId).queue(member ->
                                    member.getUser().openPrivateChannel().queue(privateChannel ->
                                            privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_PLAYER_DENIED_REQUEST, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                                    dp.sendMessage(Message.SUCCESSFULLY_DENIED_REQUEST))));
                        } else {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().remove(dp.getUuid());
                            dp.sendMessage(Message.USER_ALREADY_VERIFIED);
                        }
                    } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.DID_NOT_RECEIVED_A_REQUEST);
                } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.ALREADY_VERIFIED);
            } else if(args[0].equalsIgnoreCase("delete")) {
                if(dp.isVerified()) {
                    dp.getVerification().getMember().queue(member -> discordVerifyBot.getVerifyManager().unVerify(dp.getUuid(), verifyResult -> {
                        if(verifyResult.isSucceed()) {
                            member.getUser().openPrivateChannel().queue(privateChannel ->
                                    privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_LINK_WAS_REMOVED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message ->
                                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SUCCESSFULLY_UNLINKED, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)));
                        } else {
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.UNLINKING_PROCESS_FAILED);
                        }
                    }), throwable -> discordVerifyBot.getVerifyManager().unVerify(dp.getUuid(), verifyResult -> {
                        if(verifyResult.isSucceed()) {
                            dp.sendMessage(Message.SUCCESSFULLY_UNLINKED);
                        } else {
                            dp.sendMessage(Message.UNLINKING_PROCESS_FAILED);
                        }
                    }));
                } else discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.NOT_VERIFIED);
            } else if(args[0].equalsIgnoreCase("update")) {
                discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.UPDATING);
                discordVerifyBot.getThreadService().runAsync(() ->
                        discordVerifyBot.getVerifyManager().updateVerification(dp, verifyResult -> {
                    if(verifyResult.isSucceed()) {
                        dp.sendMessage(Message.SUCCESSFULLY_UPDATED);
                    } else dp.sendMessage(Message.FAILED_TO_UPDATE);
                }));

            } else if(args[0].contains("#")) {
                if(!dp.isVerified()) {
                    if(!discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsKey(dp.getUuid())) {
                        if(!discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsValue(dp.getUuid())) {
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.SEARCHING_USER);
                            discordVerifyBot.getBot().getGuild().findMembers(member ->
                                            ((member.getUser().getName()+"#"+member.getUser().getDiscriminator()).equals(args[0])))
                                    .onSuccess(members -> {
                                        if(!members.isEmpty()) {
                                            final Member member = members.get(0);
                                            if(!discordVerifyBot.getVerifyManager().isVerified(member.getId())) {
                                                member.getUser().openPrivateChannel().queue(privateChannel -> {
                                                    privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(Message.DISCORD_REQUEST, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null)).queue(message -> discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.USER_RECEIVED_REQUEST, dp.getName(), member.getUser().getName()+"#"+member.getUser().getDiscriminator(), null));
                                                    discordVerifyBot.getVerifyManager().getRequestsByMinecraft().put(member.getId(), dp.getUuid());

                                                });
                                            } else dp.sendMessage(Message.USER_ALREADY_VERIFIED);
                                        } else dp.sendMessage(Message.NO_USER_FOUND);
                                    })
                                    .onError(throwable -> {
                                        throwable.printStackTrace();
                                        dp.sendMessage(Message.NO_USER_FOUND);
                                    });
                        } else dp.sendMessage(Message.USER_ALREADY_RECEIVED_REQUEST);
                    } else dp.sendMessage(Message.YOU_ALREADY_RECEIVED_REQUEST);
                } else dp.sendMessage(Message.ALREADY_VERIFIED);

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
        discordVerifyBot.getMessageFormatter().sendMessage(dp, Message.HELP);
    }

}
