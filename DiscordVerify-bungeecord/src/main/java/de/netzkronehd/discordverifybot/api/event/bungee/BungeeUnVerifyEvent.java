package de.netzkronehd.discordverifybot.api.event.bungee;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class BungeeUnVerifyEvent extends Event implements Cancellable {

    private final UUID player;
    private final Member member;
    private boolean cancel;

    public BungeeUnVerifyEvent(UUID player, Member member) {
        this.player = player;
        this.member = member;
    }

    public UUID getUuid() {
        return player;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

}
