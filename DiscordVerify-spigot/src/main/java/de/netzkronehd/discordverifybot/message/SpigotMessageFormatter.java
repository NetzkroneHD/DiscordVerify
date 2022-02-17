package de.netzkronehd.discordverifybot.message;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import de.netzkronehd.discordverifybot.player.SpigotDiscordPlayer;
import me.clip.placeholderapi.PlaceholderAPI;

public class SpigotMessageFormatter extends MessageFormatter {

    @Override
    public String placePlaceholderApi(String input, DiscordPlayer receiver) {
        return PlaceholderAPI.setPlaceholders(((SpigotDiscordPlayer)receiver).getPlayer(), input);
    }
}
