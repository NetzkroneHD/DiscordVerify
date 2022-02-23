package de.netzkronehd.discordverifybot.message;

import net.md_5.bungee.api.chat.TextComponent;

public enum Message {


    PREFIX("&8[&9Discord&8]&7 ", false),
    NO_PERMISSIONS("%prefix%&cYou do not have the permissions to execute this command.", false),
    SUCCESSFULLY_LINKED("%prefix%You successfully linked your account with&e %member%&7.",false),
    SUCCESSFULLY_UNLINKED("%prefix%You successfully unlinked your account&7.", false),
    SUCCESSFULLY_DENIED_REQUEST("%prefix%You successfully denied the request", false),
    NOT_VERIFIED("%prefix%You're not verified", false),
    UPDATING("%prefix%Updating verification...", false),
    SUCCESSFULLY_UPDATED("%prefix%Successfully updated.", false),
    FAILED_TO_UPDATE("%prefix%Update failed.", false),
    FAILED_TO_VERIFY("%prefix%The linking with&e `%member%`&7 was&c cancelled.", false),
    USER_DENIED_REQUEST("%prefix%&e%member%&c denied&7 your request.", false),
    REQUEST_RECEIVED("%prefix%&e%member%&7 wants to link with your account.\n&a/verify accept &7 | &c/verify deny", false),
    UNLINKING_PROCESS_FAILED("%prefix%The process of unlinking was&c cancelled&7.", false),
    USER_ALREADY_VERIFIED("%prefix%That user is already verified.", false),
    DID_NOT_RECEIVED_A_REQUEST("%prefix%You didn't received a request.", false),
    ALREADY_VERIFIED("%prefix%You're already verified.", false),
    SEARCHING_USER("%prefix%Searching user...", false),
    //DISCORD
    DISCORD_SUCCESSFULLY_LINKED("You successfully linked your account with `%name%`.",true),
    DISCORD_FAILED_TO_VERIFY("The linking with `%name%` was cancelled.",true),
    DISCORD_PLAYER_ALREADY_VERIFIED("That player is already verified.", true),
    DISCORD_PLAYER_OFFLINE("That player is offline.", true),
    DISCORD_LINK_WAS_REMOVED("The link to `%name%` was removed.", true),
    DISCORD_DID_NOT_RECEIVED_A_REQUEST("You didn't received a request.", true),
    DISCORD_PLAYER_DENIED_REQUEST("`%name%` denied your request.", true),
    DISCORD_ALREADY_VERIFIED("You're already verified.", true),
    DISCORD_NOT_VERIFIED("You're not verified.", true),
    DISCORD_SUCCESSFULLY_DENIED_REQUEST("You successfully denied the request.", true),
    DISCORD_PLAYER_RECEIVED_REQUEST("`%name%` received your request.", true),
    DISCORD_PLAYER_ALREADY_RECEIVED_REQUEST("That player has already received a request.", true),
    DISCORD_CAN_NOT_FIND_PLAYER("Cloud not find player `%name%`.", true),

    ;

    private final String defaultValue, configKey;
    private String value;
    private final boolean discord;

    Message(String value, boolean discord) {
        this.defaultValue = value;
        this.configKey = this.name().replace("DISCORD_", "").replace("_", "-");
        this.value = value;
        this.discord = discord;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Override
    public String toString() {
        return value;
    }
}
