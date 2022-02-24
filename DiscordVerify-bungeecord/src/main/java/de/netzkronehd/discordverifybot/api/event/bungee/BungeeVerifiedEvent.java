package de.netzkronehd.discordverifybot.api.event.bungee;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.verification.DiscordVerification;
import net.md_5.bungee.api.plugin.Event;

public class BungeeVerifiedEvent extends Event {

    private final DiscordPlayer player;
    private final DiscordVerification verification;

    public BungeeVerifiedEvent(DiscordPlayer player, DiscordVerification verification) {
        this.player = player;
        this.verification = verification;
    }

    public DiscordPlayer getPlayer() {
        return player;
    }

    public DiscordVerification getVerification() {
        return verification;
    }
}
