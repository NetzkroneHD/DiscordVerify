package de.netzkronehd.discordverifybot.message;

import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageFormatter {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[A-fA-F0-9]{6}");

    public String format(Message input, String name, String member, String groups) {
        return format(input.getValue(), name, member, groups);
    }

    public String format(String input, String name, String member, String groups) {
        input = input.replace("%name%", (name != null ? name:""));
        input = input.replace("%member%", (member != null ? member:""));
        input = input.replace("%groups%", (groups != null ? groups:""));
        return input;
    }

    public void sendMessage(DiscordPlayer dp, Message message, String name, String member, String groups) {
        dp.sendMessage(placePlaceholderApi(format(message.getValue(), name, member, groups), dp));
    }

    public void sendMessage(DiscordPlayer dp, Message message) {
        sendMessage(dp, message, dp.getName(), (dp.isVerified() ? dp.getVerification().getCachedMemberName():null), null);
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
