package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.message.Message;

import java.io.File;
import java.io.IOException;

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
            if(!file.exists()) {
                for(Message msg : Message.values()) {
                    cfg.set(msg.getConfigKey(), msg.getDefaultValue());
                }
                try {
                    cfg.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            message.setValue(discordVerifyBot.getMessageFormatter().translateColor(cfg.getString(message.getConfigKey()).replace("%PREFIX%", Message.PREFIX.getValue())));
        }
    }
}
