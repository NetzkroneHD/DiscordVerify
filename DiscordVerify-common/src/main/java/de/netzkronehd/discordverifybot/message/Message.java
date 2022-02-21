package de.netzkronehd.discordverifybot.message;

import net.md_5.bungee.api.chat.TextComponent;

public enum Message {


    PREFIX("&8[&9Discord&8]&7 ", "",false),
    SUCCESSFULLY_LINKED("You successfully linked your account with&e %member%&7.", "",false),
    SUCCESSFULLY_UNLINKED("You successfully unlinked your account&7.", "",false),
    SUCCESSFULLY_DENIED_REQUEST("You successfully denied the request", "", false),
    NOT_VERIFIED("You're not verified", "", false),
    UPDATING("Updating verification...", "", false),
    SUCCESSFULLY_UPDATED("Successfully updated.", "", false),
    FAILED_TO_UPDATE("Update failed.", "", false),
    FAILED_TO_VERIFY("The linking with&e `%member%`&7 was&c cancelled.", "", false),
    USER_DENIED_REQUEST("&e%member%&c denied&7 your request.", "", false),
    REQUEST_RECEIVED("&e%member%&7 wants to link with your account.\n&a/verify accept &7 | &c/verify deny", "", false),
    UNLINKING_PROCESS_FAILED("The process of unlinking was&c cancelled&7.", "", false),
    USER_ALREADY_VERIFIED("That user is already verified.", "", false),
    DID_NOT_RECEIVED_A_REQUEST("You didn't received a request.", "", false),
    ALREADY_VERIFIED("You're already verified.", "", false),
    SEARCHING_USER("Searching user...", "", false),
    //DISCORD
    DISCORD_SUCCESSFULLY_LINKED("You successfully linked your account with `%name%`.", "",true),
    DISCORD_FAILED_TO_VERIFY("The linking with `%name%` was cancelled.", "",true),
    DISCORD_PLAYER_ALREADY_VERIFIED("That player is already verified.", "", true),
    DISCORD_PLAYER_OFFLINE("That player is offline.", "", true),
    DISCORD_LINK_WAS_REMOVED("The link to `%name%` was removed.", "", true),
    DISCORD_DID_NOT_RECEIVED_A_REQUEST("You didn't received a request.", "", true),
    DISCORD_PLAYER_DENIED_REQUEST("`%name%` denied your request.", "", true),
    DISCORD_ALREADY_VERIFIED("You're already verified.", "", true),
    DISCORD_NOT_VERIFIED("You're not verified.", "", true),
    DISCORD_SUCCESSFULLY_DENIED_REQUEST("You successfully denied the request.", "", true),
    DISCORD_PLAYER_RECEIVED_REQUEST("`%name%` received your request.", "", true),
    DISCORD_PLAYER_ALREADY_RECEIVED_REQUEST("That player has already received a request.", "", true),
    DISCORD_CAN_NOT_FIND_PLAYER("Cloud not find player `%name%`.", "", true),

    ;

    private final String defaultValue, configKey;
    private String value;
    private TextComponent textComponent;
    private final boolean discord;

    Message(String value, String configKey, boolean discord) {
        this.defaultValue = value;
        this.configKey = configKey;
        this.value = value;
        this.discord = discord;
    }

    public void setTextComponent(TextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TextComponent getTextComponent() {
        return textComponent;
    }

    public String getValue() {
        return value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isDiscord() {
        return discord;
    }

    public String getConfigKey() {
        return (isDiscord() ? "discord.":"minecraft.")+configKey;
    }
}
