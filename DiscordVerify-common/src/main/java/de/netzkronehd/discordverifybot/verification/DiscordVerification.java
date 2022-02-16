package de.netzkronehd.discordverifybot.verification;

import net.dv8tion.jda.api.entities.Member;

import java.util.UUID;

public class DiscordVerification {

    private final UUID uuid;
    private String name;
    private final String discordId;
    private Member member;

    public DiscordVerification(UUID uuid, String name, String discordId) {
        this.uuid = uuid;
        this.name = name;
        this.discordId = discordId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMember(Member member) {
        this.member = member;
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

    public Member getMember() {
        return member;
    }

}
