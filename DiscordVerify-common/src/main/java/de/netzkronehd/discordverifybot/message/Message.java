package de.netzkronehd.discordverifybot.message;

import net.md_5.bungee.api.chat.TextComponent;

public enum Message {

    PREFIX("&8[&9Discord&8]&7 ", false);

    private final String defaultValue;
    private String value;
    private TextComponent textComponent;
    private final boolean discord;

    Message(String value, boolean discord) {
        this.defaultValue = value;
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
        return (isDiscord() ? "discord.":"minecraft.")+name().toLowerCase();
    }

}
