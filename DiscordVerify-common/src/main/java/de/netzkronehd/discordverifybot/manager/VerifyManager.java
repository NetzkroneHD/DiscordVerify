package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.api.VerifyResult;
import de.netzkronehd.discordverifybot.group.Group;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class VerifyManager extends Manager {

    private final HashMap<UUID, DiscordVerification> verifications;
    private final HashMap<String, DiscordVerification> verificationsByUserId;

    private final HashMap<UUID, String> requestsByDiscord;
    private final HashMap<String, UUID> requestsByMinecraft;

    public VerifyManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        requestsByDiscord = new HashMap<>();
        requestsByMinecraft = new HashMap<>();
        verifications = new HashMap<>();
        verificationsByUserId = new HashMap<>();
    }

    @Override
    public void onLoad() {
        //discordVerifications(uuid VARCHAR(64), name TEXT, discordId TEXT, timepoint TEXT)
        try {
            final ResultSet rs = discordVerifyBot.getDatabase().getResult("SELECT * FROM discordVerification");
            while(rs.next()) {
                final DiscordVerification dv = new DiscordVerification(UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getString("discordId"), rs.getLong("timepoint"));
                verifications.put(dv.getUuid(), dv);
                verificationsByUserId.put(dv.getDiscordId(), dv);
                final DiscordPlayer dp = discordVerifyBot.getPlayer(dv.getUuid());
                if(dp != null) dp.setVerification(dv);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onReload() {
        verifications.clear();
        verificationsByUserId.clear();
        requestsByMinecraft.clear();
        requestsByDiscord.clear();
        discordVerifyBot.getPlayers().forEach(discordPlayer -> discordPlayer.setVerification(null));
        onLoad();
    }

    public void verify(DiscordPlayer dp, Member member, Consumer<VerifyResult> callback) {
        if(!isVerified(dp.getUuid())) {
            if(!isVerified(member.getId())) {
                final DiscordVerification dv = new DiscordVerification(dp.getUuid(), dp.getName(), member.getId(), System.currentTimeMillis());

                try {
                    //discordVerifications(uuid VARCHAR(64), name TEXT, discordId TEXT, timepoint TEXT)
                    discordVerifyBot.getDatabase().update("INSERT INTO discordVerification(uuid, name, discordId, timepoint) VALUES " +
                            "('"+dv.getUuid()+"', '"+dv.getName()+"', '"+dv.getDiscordId()+"', '"+dv.getTimepoint()+"')");
                    verifications.put(dv.getUuid(), dv);
                    verificationsByUserId.put(dv.getDiscordId(), dv);
                    dv.setCachedMember(member);
                    dp.setVerification(dv);
                    if(callback != null) callback.accept(VerifyResult.SUCCESS);
                    discordVerifyBot.getEventService().fireVerifiedEvent(dp, dv);

                } catch (SQLException e) {
                    e.printStackTrace();
                    if(callback != null) callback.accept(VerifyResult.FAILED);
                }


            } else {
                if(callback != null) callback.accept(VerifyResult.CANCELLED);
                throw new IllegalStateException("Member '"+member.getUser().getName()+"#"+member.getUser().getName()+"' is already verified.");
            }
        } else {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw new IllegalStateException("Player '"+dp.getName()+"' is already verified.");
        }
    }

    public void unVerify(String userId, Consumer<VerifyResult> callback) {
        final DiscordVerification dv = getVerification(userId);
        if(dv != null) {
            try {
                discordVerifyBot.getDatabase().update("DELETE * FROM discordVerifications WHERE discordId='"+userId+"'");
                verifications.remove(dv.getUuid());
                verificationsByUserId.remove(dv.getDiscordId());
                final DiscordPlayer dp = discordVerifyBot.getPlayer(dv.getUuid());
                if(dp != null) {
                    dp.setVerification(null);
                }
                discordVerifyBot.getBot().getGuild().retrieveMemberById(userId).queue(member -> discordVerifyBot.getGroupManager().removeGroups(member));
                if(callback != null) callback.accept(VerifyResult.SUCCESS);
                discordVerifyBot.getEventService().fireUnVerifiedEvent(dv.getUuid(), dv.getDiscordId());
            } catch (Exception e) {
                e.printStackTrace();
                if(callback != null) callback.accept(VerifyResult.FAILED);
            }
        } else {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw new IllegalStateException("User '"+userId+"' is not verified.");
        }
    }

    public void unVerify(Member member, Consumer<VerifyResult> callback) {
        final DiscordVerification dv = getVerification(member.getId());
        if(dv != null) {
            try {
                discordVerifyBot.getDatabase().update("DELETE * FROM discordVerifications WHERE discordId='"+member.getId()+"'");
                verifications.remove(dv.getUuid());
                verificationsByUserId.remove(dv.getDiscordId());
                final DiscordPlayer dp = discordVerifyBot.getPlayer(dv.getUuid());
                if(dp != null) {
                    dp.setVerification(null);
                }
                discordVerifyBot.getGroupManager().removeGroups(member);
                if(callback != null) callback.accept(VerifyResult.SUCCESS);
                discordVerifyBot.getEventService().fireUnVerifiedEvent(dv.getUuid(), dv.getDiscordId());
            } catch (Exception e) {
                e.printStackTrace();
                if(callback != null) callback.accept(VerifyResult.FAILED);
            }
        } else {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw new IllegalStateException("Member '"+member.getUser().getName()+"#"+member.getUser().getName()+"' is not verified.");
        }
    }

    public void unVerify(UUID uuid, Consumer<VerifyResult> callback) {
        final DiscordVerification dv = getVerification(uuid);
        if(dv != null) {
            try {
                discordVerifyBot.getDatabase().update("DELETE * FROM discordVerifications WHERE uuid='"+uuid+"'");
                verifications.remove(dv.getUuid());
                verificationsByUserId.remove(dv.getDiscordId());
                final DiscordPlayer dp = discordVerifyBot.getPlayer(uuid);
                if(dp != null) {
                    dp.setVerification(null);
                }
                dv.getMember().queue(member -> discordVerifyBot.getGroupManager().removeGroups(member));
                if(callback != null) callback.accept(VerifyResult.SUCCESS);
                discordVerifyBot.getEventService().fireUnVerifiedEvent(dv.getUuid(), dv.getDiscordId());
            } catch (Exception e) {
                e.printStackTrace();
                if(callback != null) callback.accept(VerifyResult.FAILED);
            }
        } else {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw new IllegalStateException("Player '"+uuid+"' is not verified.");
        }
    }

    public void updateVerification(DiscordPlayer dp, Member member, Consumer<VerifyResult> callback) {
        if(dp.isVerified()) {
            try {
                dp.getVerification().setName(dp.getName());
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
                            } else log(Level.SEVERE, "Could not find role '"+group.getRoleId()+"'");
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
                            } else log(Level.SEVERE, "Could not find role '"+group.getRoleId()+"'");
                        }
                    }
                }
                if(callback != null) callback.accept(VerifyResult.SUCCESS);
                discordVerifyBot.getEventService().fireVerificationUpdatedEvent(dp.getVerification());

            } catch (Exception e) {
                e.printStackTrace();
                if(callback != null) callback.accept(VerifyResult.FAILED);
            }
        } else {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw new IllegalStateException("The player '"+dp.getName()+"' has to be verified to update the verification.");
        }

    }

    public void updateVerification(DiscordPlayer dp, Consumer<VerifyResult> callback) {
        if(dp.isVerified()) {
            dp.getVerification().getMember().queue(member -> updateVerification(dp, member, callback));

        } else {
            if(callback != null) callback.accept(VerifyResult.CANCELLED);
            throw new IllegalStateException("The player '"+dp.getName()+"' has to be verified to update the verification.");
        }
    }

    public void updateVerification(DiscordPlayer dp) {
        updateVerification(dp, null);
    }

    public void updateName(UUID uuid, String name) {
        if(isVerified(uuid)) {
            try {
                discordVerifyBot.getDatabase().update("UPDATE discordVerification SET name='"+name+"' WHERE uuid='"+uuid+"'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public DiscordVerification getVerification(UUID uuid) {
        return verifications.get(uuid);
    }

    public DiscordVerification getVerification(String userId) {
        return verificationsByUserId.get(userId);
    }

    public boolean isVerified(UUID uuid) {
        return verifications.containsKey(uuid);
    }

    public boolean isVerified(String userId) {
        return verificationsByUserId.containsKey(userId);
    }

    public HashMap<String, UUID> getRequestsByMinecraft() {
        return requestsByMinecraft;
    }

    public HashMap<UUID, String> getRequestsByDiscord() {
        return requestsByDiscord;
    }

    public Collection<DiscordVerification> getVerifications() {
        return Collections.unmodifiableCollection(verifications.values());
    }

}
