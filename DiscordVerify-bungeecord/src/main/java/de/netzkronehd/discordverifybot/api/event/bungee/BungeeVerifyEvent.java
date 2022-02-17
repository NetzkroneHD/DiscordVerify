package de.netzkronehd.discordverifybot.api.event.bungee;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class BungeeVerifyEvent extends Event implements Cancellable {

    private final DiscordPlayer player;
    private final Member member;
    private boolean cancel;

    public BungeeVerifyEvent(DiscordPlayer player, Member member) {
        this.player = player;
        this.member = member;
    }

    public DiscordPlayer getPlayer() {
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
