package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.*;

import java.util.UUID;

public class DiscordVerifyCommand extends DiscordCommand {


    public DiscordVerifyCommand(String command, DiscordVerifyBot discordVerifyBot) {
        super(command, discordVerifyBot);
    }

    @Override
    public void onExecute(User sender, PrivateChannel privateChannel, String[] args, Message message) {
        onExecute(sender, (MessageChannel) privateChannel, args, message);
    }

    @Override
    public void onExecute(Member sender, TextChannel textChannel, String[] args, Message message) {
        onExecute(sender.getUser(), textChannel, args, message);
    }

    @Override
    public void onExecute(User sender, MessageChannel messageChannel, String[] args, Message message) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("accept")) {
                if(!discordVerifyBot.getVerifyManager().isVerified(sender.getId())) {
                    final UUID uuid = discordVerifyBot.getVerifyManager().getRequestsByMinecraft().get(sender.getId());
                    if(uuid != null) {
                        final DiscordPlayer dp = discordVerifyBot.getPlayer(uuid);
                        if(dp != null) {
                            if(!dp.isVerified()) {
                                discordVerifyBot.getBot().getGuild().retrieveMemberById(sender.getId()).queue(member ->
                                        discordVerifyBot.getVerifyManager().verify(dp, member, verifyResult -> {
                                            if(verifyResult.isSucceed()) {
                                                member.getUser().openPrivateChannel().queue(privateChannel ->
                                                        privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_SUCCESSFULLY_LINKED, dp.getName(), null, null)).queue(message2 ->
                                                                dp.sendMessage(de.netzkronehd.discordverifybot.message.Message.SUCCESSFULLY_LINKED)));
                                            } else {
                                                member.getUser().openPrivateChannel().queue(privateChannel ->
                                                        privateChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_FAILED_TO_VERIFY, dp.getName(), null, null)).queue(message2 ->
                                                                discordVerifyBot.getMessageFormatter().sendMessage(dp, de.netzkronehd.discordverifybot.message.Message.FAILED_TO_VERIFY, dp.getName(), sender.getName()+"#"+sender.getDiscriminator(), null)));
                                            }
                                        }));

                            } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_ALREADY_VERIFIED, dp.getName(), null, null)).queue();

                        } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_PLAYER_OFFLINE, null, null, null)).queue();
                        discordVerifyBot.getVerifyManager().getRequestsByMinecraft().remove(sender.getId());

                    } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_DID_NOT_RECEIVED_A_REQUEST, null, null, null)).queue();
                } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_ALREADY_VERIFIED, null, null, null)).queue();
            } else if(args[0].equalsIgnoreCase("deny")) {
                if(!discordVerifyBot.getVerifyManager().isVerified(sender.getId())) {
                    final UUID uuid = discordVerifyBot.getVerifyManager().getRequestsByMinecraft().get(sender.getId());
                    if(uuid != null) {
                        final DiscordPlayer dp = discordVerifyBot.getPlayer(uuid);
                        if(dp != null) {
                            discordVerifyBot.getMessageFormatter().sendMessage(dp, de.netzkronehd.discordverifybot.message.Message.USER_DENIED_REQUEST, dp.getName(), sender.getName()+"#"+sender.getDiscriminator(), null);
                            messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_SUCCESSFULLY_DENIED_REQUEST, dp.getName(), null, null)).queue();
                        } else {
                            messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_SUCCESSFULLY_DENIED_REQUEST, null, null, null)).queue();
                        }



                        discordVerifyBot.getVerifyManager().getRequestsByMinecraft().remove(sender.getId());
                    } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_DID_NOT_RECEIVED_A_REQUEST, null, null, null)).queue();
                } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.ALREADY_VERIFIED, null, null, null)).queue();
            } else if(args[0].equalsIgnoreCase("delete")) {
                final DiscordVerification dv = discordVerifyBot.getApi().getVerification(sender.getId());
                if(dv != null) {
                   discordVerifyBot.getVerifyManager().unVerify(sender.getId(), verifyResult -> {
                       if(verifyResult.isSucceed()) {
                           final DiscordPlayer dp = discordVerifyBot.getPlayer(dv.getUuid());
                           if(dp != null) {
                               discordVerifyBot.getMessageFormatter().sendMessage(dp, de.netzkronehd.discordverifybot.message.Message.LINK_WAS_REMOVED, dp.getName(), dv.getCachedMemberName(), null);
                           }
                           messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_SUCCESSFULLY_UNLINKED, dv.getName(), dv.getCachedMemberName(), null)).queue();
                       } else {
                           messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_UNLINKING_PROCESS_FAILED, null, null, null)).queue();
                       }
                   });
                } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_NOT_VERIFIED, null, null, null)).queue();
            } else {
                final DiscordPlayer dp = discordVerifyBot.getPlayer(args[0]);
                if(dp != null) {
                    if(!dp.isVerified()) {
                        if(!discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsKey(dp.getUuid())) {
                            if(!discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsValue(dp.getUuid())) {
                                discordVerifyBot.getVerifyManager().getRequestsByDiscord().put(dp.getUuid(), sender.getId());
                                discordVerifyBot.getMessageFormatter().sendMessage(dp, de.netzkronehd.discordverifybot.message.Message.REQUEST, dp.getName(), sender.getName()+"#"+sender.getDiscriminator(), null);
                                messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_PLAYER_RECEIVED_REQUEST, dp.getName(), null, null)).queue();

                            } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_YOU_ALREADY_RECEIVED_REQUEST, dp.getName(), sender.getName()+"#"+sender.getDiscriminator(), null)).queue();
                        } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_PLAYER_ALREADY_RECEIVED_REQUEST, dp.getName(), null, null)).queue();
                    } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_PLAYER_ALREADY_VERIFIED, dp.getName(), null, null)).queue();
                } else messageChannel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_PLAYER_ALREADY_VERIFIED, args[0], null, null)).queue();
            }
        } else sendHelp(messageChannel);
    }

    private void sendHelp(MessageChannel channel) {
        channel.sendMessage(discordVerifyBot.getMessageFormatter().format(de.netzkronehd.discordverifybot.message.Message.DISCORD_HELP, null, null, null)).queue();
    }

}
