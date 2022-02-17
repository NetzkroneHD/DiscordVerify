package de.netzkronehd.discordverifybot.message;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;

public class BungeeMessageFormatter extends MessageFormatter {

    @Override
    public String placePlaceholderApi(String input, DiscordPlayer receiver) {
        return input;
    }

}
