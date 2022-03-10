package de.netzkronehd.discordverifybot.api;

import com.google.common.base.Preconditions;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.bot.DiscordBot;
import de.netzkronehd.discordverifybot.commands.DiscordCommand;
import de.netzkronehd.discordverifybot.message.Message;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

public class DiscordVerifyApi {

    private final DiscordVerifyBot discordVerifyBot;

    public DiscordVerifyApi(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    /**
     * Gets the {@link DiscordVerification DiscordVerification} of an {@link UUID uuid}
     * @param uuid {@link UUID Uuid} of the verified {@link DiscordPlayer player}
     * @return {@link DiscordVerification Verification} of the {@link UUID uuid} - {@code null} if not verified
     *
     */
    public DiscordVerification getVerification(UUID uuid) {
        return discordVerifyBot.getVerifyManager().getVerification(uuid);
    }

    /**
     * Gets the {@link DiscordVerification DiscordVerification} of an {@link Member member}
     * @see Member#getId()
     * @param userId {@link String Id} of the verified {@link Member member}
     * @return {@link DiscordVerification Verification} of the {@link UUID uuid} - {@code null} if not verified
     *
     */
    public DiscordVerification getVerification(String userId) {
        return discordVerifyBot.getVerifyManager().getVerification(userId);
    }

    public Collection<DiscordVerification> getVerifications() {
        return discordVerifyBot.getVerifyManager().getVerifications();
    }

    public void registerCommand(DiscordCommand dc) {
        discordVerifyBot.getDiscordCommandManager().registerCommand(dc);
    }

    public void unregisterCommand(DiscordCommand dc) {
        discordVerifyBot.getDiscordCommandManager().unregisterCommand(dc);
    }

    public void unregisterCommand(String commandPrefix) {
        discordVerifyBot.getDiscordCommandManager().unregisterCommand(commandPrefix);
    }

    public void sendRequest(DiscordPlayer sender, Member receiver, Consumer<VerifyResult> callback) {
        try {
            Preconditions.checkNotNull(sender, "'sender' can't be null.");
            Preconditions.checkNotNull(receiver, "'receiver' can't be null.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsKey(sender.getUuid()))), "'"+sender.getName()+"' already got a request.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsValue(receiver.getId()))), "'"+receiver.getUser().getAsTag()+"' already sent a request.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsValue(sender.getUuid()))), "'"+sender.getName()+"' already sent a request.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsKey(receiver.getId()))), "'"+receiver.getUser().getAsTag()+"' already got a request.");

        } catch (Exception ex) {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw ex;
        }
        receiver.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(
                discordVerifyBot.getMessageFormatter().format(Message.DISCORD_REQUEST, sender.getName(), receiver.getUser().getName()+"#"+receiver.getUser().getDiscriminator(), null))
                .queue(message -> {
                    discordVerifyBot.getMessageFormatter().sendMessage(sender, Message.USER_RECEIVED_REQUEST, sender.getName(), receiver.getUser().getName()+"#"+receiver.getUser().getDiscriminator(), null);
                    if(callback != null) callback.accept(VerifyResult.SUCCESS);
                }));
    }

    public void sendRequest(Member sender, DiscordPlayer receiver, Consumer<VerifyResult> callback) {
        try {
            Preconditions.checkNotNull(sender, "'sender' can't be null.");
            Preconditions.checkNotNull(receiver, "'receiver' can't be null.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsValue(sender.getId()))), "'"+sender.getUser().getAsTag()+"' already got a request.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsKey(receiver.getUuid()))), "'"+receiver.getName()+"' already sent a request.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsKey(sender.getId()))), "'"+sender.getUser().getAsTag()+"' already sent a request.");
            Preconditions.checkState((!(discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsValue(receiver.getUuid()))), "'"+receiver.getName()+"' already got a request.");

        } catch (Exception ex) {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw ex;
        }
        sender.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(
                        discordVerifyBot.getMessageFormatter().format(Message.DISCORD_PLAYER_RECEIVED_REQUEST, receiver.getName(), sender.getUser().getName()+"#"+sender.getUser().getDiscriminator(), null))
                .queue(message -> {
                    discordVerifyBot.getMessageFormatter().sendMessage(receiver, Message.REQUEST, receiver.getName(), sender.getUser().getName()+"#"+sender.getUser().getDiscriminator(), null);
                    if(callback != null) callback.accept(VerifyResult.SUCCESS);
                }));
    }

    public String getRequestByDiscord(UUID uuid) {
        return discordVerifyBot.getVerifyManager().getRequestsByDiscord().get(uuid);
    }

    public UUID getRequestByMinecraft(String id) {
        return discordVerifyBot.getVerifyManager().getRequestsByMinecraft().get(id);
    }

    public boolean hasSentRequest(String id) {
        return (discordVerifyBot.getVerifyManager().getRequestsByDiscord().containsValue(id));
    }

    public boolean hasSentRequest(UUID uuid) {
        return (discordVerifyBot.getVerifyManager().getRequestsByMinecraft().containsValue(uuid));
    }

    public boolean canRequest(UUID uuid) {
        return (!isVerified(uuid) && getRequestByDiscord(uuid) == null && !hasSentRequest(uuid));
    }

    public boolean canRequest(String id) {
        return (!isVerified(id) && getRequestByMinecraft(id) == null && !hasSentRequest(id));
    }

    public boolean isVerified(UUID uuid) {
        return (getVerification(uuid) != null);
    }

    public boolean isVerified(String id) {
        return (getVerification(id) != null);
    }

    public DiscordBot getBot() {
        return discordVerifyBot.getBot();
    }

    public boolean isReady() {
        return discordVerifyBot.isReady();
    }

    public DiscordPlayer getPlayer(UUID uuid) {
        return discordVerifyBot.getPlayer(uuid);
    }

    public DiscordPlayer getPlayer(String name) {
        return discordVerifyBot.getPlayer(name);
    }

}

