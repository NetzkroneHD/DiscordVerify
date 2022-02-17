package de.netzkronehd.discordverifybot.message;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.md_5.bungee.api.ChatColor;

import javax.naming.OperationNotSupportedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageFormatter {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[A-fA-F0-9]{6}");

    public String format(String input, String name, String member, String groups) {
        input = translateColor(input);
        if(name != null) input = input.replace("%name%", name);
        if(member != null) input = input.replace("%member%", member);
        if(groups != null) input = input.replace("%groups%", groups);
        return input;
    }

    public abstract String placePlaceholderApi(String input, DiscordPlayer receiver);

    public String translateColor(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color)+"");
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public static Pattern getHexPattern() {
        return HEX_PATTERN;
    }
}
