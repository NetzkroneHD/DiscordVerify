package de.netzkronehd.discordverifybot.verification;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.UUID;

public class DiscordVerification {

    private final UUID uuid;
    private String name;
    private final String discordId;
    private final long timepoint;
    private Member cachedMember;

    public DiscordVerification(UUID uuid, String name, String discordId, long timepoint) {
        this.uuid = uuid;
        this.name = name;
        this.discordId = discordId;
        this.timepoint = timepoint;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setCachedMember(Member cachedMember) {
        this.cachedMember = cachedMember;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDiscordId() {
        return discordId;
    }

    public long getTimepoint() {
        return timepoint;
    }

    public Member getCachedMember() {
        return cachedMember;
    }

    public String getCachedMemberName() {
        if(cachedMember != null) return cachedMember.getUser().getName()+"#"+cachedMember.getUser().getDiscriminator();
        return null;
    }

    public RestAction<Member> getMember() {
        return DiscordVerifyBot.getInstance().getBot().getGuild().retrieveMemberById(discordId);
    }

}
