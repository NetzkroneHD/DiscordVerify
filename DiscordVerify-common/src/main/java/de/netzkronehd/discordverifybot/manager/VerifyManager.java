package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.api.VerifyUpdateResult;
import de.netzkronehd.discordverifybot.group.Group;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

public class VerifyManager extends Manager {

    private final HashMap<UUID, String> requestsByDiscord;
    private final HashMap<String, UUID> requestsByMinecraft;


    public VerifyManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        requestsByDiscord = new HashMap<>();
        requestsByMinecraft = new HashMap<>();

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onReload() {
        onLoad();
    }

    public void verify(DiscordPlayer dp, Member member) {
        if(!isVerified(dp.getUuid())) {
            if(!isVerified(member.getId())) {
                final DiscordVerification discordVerification = new DiscordVerification(dp.getUuid(), dp.getName(), member.getId());
                dp.setVerification(discordVerification);
                member.getUser().openPrivateChannel().queue(privateChannel ->
                        privateChannel.sendMessage("You successfully linked your account with `"+dp.getName()+"`.").queue(message ->
                                dp.sendMessage("You successfully linked your account with&e "+member.getUser().getName()+"#"+member.getUser().getName()+"&7.")));

            } else throw new IllegalStateException("Member '"+member.getUser().getName()+"#"+member.getUser().getName()+"' is already verified.");
        } else throw new IllegalStateException("Player '"+dp.getName()+"' is already verified.");
    }

    public void updateVerification(DiscordPlayer dp, Member member, Consumer<VerifyUpdateResult> callback) {
        if(dp.isVerified()) {
            try {
                final List<Group> permissionGroup = discordVerifyBot.getGroupManager().getGroups(dp);
                final List<Group> discordGroup = discordVerifyBot.getGroupManager().getGroups(member);
                for(Group group : discordGroup) {
                    if(!permissionGroup.contains(group)) {
                        if(group.getRole() != null) {
                            member.getGuild().removeRoleFromMember(member, group.getRole()).queue();
                        } else {
                            final Role role = member.getGuild().getRoleById(group.getRoleId());
                            if(role != null) {
                                member.getGuild().removeRoleFromMember(member, group.getRole()).queue();
                            } else log(Level.SEVERE, "Cloud not find role '"+group.getRoleId()+"'");
                        }
                    }
                }

                for(Group group : permissionGroup) {
                    if(!discordGroup.contains(group)) {
                        if(group.getRole() != null) {
                            member.getGuild().addRoleToMember(member, group.getRole()).queue();
                        } else {
                            final Role role = member.getGuild().getRoleById(group.getRoleId());
                            if(role != null) {
                                member.getGuild().addRoleToMember(member, group.getRole()).queue();
                            } else log(Level.SEVERE, "Cloud not find role '"+group.getRoleId()+"'");
                        }
                    }
                }
                callback.accept(VerifyUpdateResult.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                callback.accept(VerifyUpdateResult.FAILED);
            }
        } else {
            callback.accept(VerifyUpdateResult.CANCELLED);
            throw new IllegalStateException("The player '"+dp.getName()+"' has to be verified to update the verification.");
        }

    }

    public void updateVerification(DiscordPlayer dp, Consumer<VerifyUpdateResult> callback) {
        if(dp.getVerification().getMember() == null) {
            discordVerifyBot.getBot().getGuild().retrieveMemberById(dp.getVerification().getDiscordId()).queue(member -> updateVerification(dp, member, callback));
        } else updateVerification(dp, dp.getVerification().getMember(), callback);
    }

    public boolean isVerified(UUID uuid) {
        return false;
    }

    public boolean isVerified(String userId) {
        return false;
    }

    public HashMap<String, UUID> getRequestsByMinecraft() {
        return requestsByMinecraft;
    }

    public HashMap<UUID, String> getRequestsByDiscord() {
        return requestsByDiscord;
    }

}
