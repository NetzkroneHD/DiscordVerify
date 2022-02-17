package de.netzkronehd.discordverifybot.commands.impl;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.api.VerifyResult;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
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
                                                        privateChannel.sendMessage("You successfully linked your account with `"+dp.getName()+"`.").queue(message2 ->
                                                                dp.sendMessage("You successfully linked your account with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7.")));
                                            } else {
                                                member.getUser().openPrivateChannel().queue(privateChannel ->
                                                        privateChannel.sendMessage("The linking with `"+dp.getName()+"` was cancelled.").queue(message2 ->
                                                                dp.sendMessage("The linking with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7 was&c cancelled.")));
                                            }
                                        }));

                            } else messageChannel.sendMessage("That player is already verified.").queue();

                        } else messageChannel.sendMessage("That player is offline.").queue();
                        discordVerifyBot.getVerifyManager().getRequestsByMinecraft().remove(sender.getId());

                    } else messageChannel.sendMessage("You didn't received a request.").queue();
                } else messageChannel.sendMessage("You're already verified.").queue();
            } else if(args[0].equalsIgnoreCase("deny")) {
                if(!discordVerifyBot.getVerifyManager().isVerified(sender.getId())) {
                    final UUID uuid = discordVerifyBot.getVerifyManager().getRequestsByMinecraft().get(sender.getId());
                    if(uuid != null) {
                        final DiscordPlayer dp = discordVerifyBot.getPlayer(uuid);
                        if(dp != null) {
                            dp.sendMessage("&e"+sender.getName()+"#"+sender.getDiscriminator()+"&c denied&7 your request.");
                        }
                        messageChannel.sendMessage("You successfully denied the request.").queue();

                        discordVerifyBot.getVerifyManager().getRequestsByMinecraft().remove(sender.getId());
                    } else messageChannel.sendMessage("You didn't received a request.").queue();
                } else messageChannel.sendMessage("You're already verified.").queue();
            } else if(args[0].equalsIgnoreCase("delete")) {
                if(discordVerifyBot.getVerifyManager().isVerified(sender.getId())) {

                } else messageChannel.sendMessage("You're not verified.").queue();
            } else {
                final DiscordPlayer dp = discordVerifyBot.getPlayer(args[0]);
                if(dp != null) {
                    if(!dp.isVerified()) {
                        if(!discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsKey(dp.getUuid())) {
                            discordVerifyBot.getVerifyManager().getRequestsByDiscord().put(dp.getUuid(), sender.getId());
                            dp.sendMessage("&e"+sender.getName()+"#"+sender.getDiscriminator()+"&7 wants to link with your account.\n&a/verify accept &7 | &c/verify deny");
                            messageChannel.sendMessage("`"+dp.getName()+"` received your request.").queue();
                        } else messageChannel.sendMessage("That player has already received a request.").queue();
                    } else messageChannel.sendMessage("That player is already verified.").queue();
                } else messageChannel.sendMessage("Cloud not find player `"+args[0]+"`.").queue();
            }
        }
    }
}
