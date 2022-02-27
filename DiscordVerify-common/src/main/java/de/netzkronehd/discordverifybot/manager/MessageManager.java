package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.message.Message;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class MessageManager extends Manager {

    private File file;
    private YamlConfiguration cfg;

    public MessageManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        file = new File("plugins/DiscordVerifyBot", "messages.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        setPriority(2);
    }

    @Override
    public void onLoad() {
        file = new File("plugins/DiscordVerifyBot", "messages-"+DiscordVerifyBot.getInstance().getConfigManager().getLanguage()+".yml");
        if(getResource("messages-"+discordVerifyBot.getConfigManager().getLanguage()+".yml") != null) {
            saveResource("messages-"+discordVerifyBot.getConfigManager().getLanguage()+".yml", "plugins/DiscordVerifyBot", false);
        } else {
            log(Level.WARNING, "Could not find language '"+discordVerifyBot.getConfigManager().getLanguage()+"' using 'EN' instead.");
            saveResource("messages-EN.yml", "plugins/DiscordVerifyBot", false);
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onReload() {
        onLoad();
    }

    @Override
    public void readFile() {
        Message.PREFIX.setValue(discordVerifyBot.getMessageFormatter().translateColor(cfg.getString(Message.PREFIX.getConfigKey())));
        for(Message message : Message.values()) {
            try {
                message.setValue(discordVerifyBot.getMessageFormatter().translateColor(cfg.getString(message.getConfigKey()).replace("%PREFIX%", Message.PREFIX.getValue())));
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }
}
