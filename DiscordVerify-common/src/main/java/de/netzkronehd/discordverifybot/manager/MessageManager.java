package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.message.Message;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MessageManager extends Manager {

    private File file;
    private YamlConfiguration cfg;

    public MessageManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        setPriority(2);
    }

    @Override
    public void onLoad() {
        if(getResource("messages-"+discordVerifyBot.getConfigManager().getLanguage()+".yml") != null) {
            saveResource("messages-"+discordVerifyBot.getConfigManager().getLanguage()+".yml", "plugins/DiscordVerifyBot", false);
            file = new File("plugins/DiscordVerifyBot", "messages-" + DiscordVerifyBot.getInstance().getConfigManager().getLanguage() + ".yml");
        } else {
            log(Level.WARNING, "Could not find language '"+discordVerifyBot.getConfigManager().getLanguage()+"' using 'EN' instead.");
            saveResource("messages-EN.yml", "plugins/DiscordVerifyBot", false);
            file = new File("plugins/DiscordVerifyBot", "messages-EN.yml");
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void onReload() {
        onLoad();
    }

    @Override
    public void createFile() {
        if(!file.exists()) {

            for(Message msg : Message.values()) {
                if(msg.getDefaultValue().contains("\n")) {
                    cfg.set(msg.getConfigKey(), Arrays.asList(msg.getDefaultValue().split("\n")));
                } else cfg.set(msg.getConfigKey(), msg.getDefaultValue());
            }

            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void readFile() {
        Message.PREFIX.setValue(discordVerifyBot.getMessageFormatter().translateColor(cfg.getString(Message.PREFIX.getConfigKey())));
        for(Message msg : Message.values()) {
            try {
                if(!cfg.getStringList(msg.getConfigKey()).isEmpty()) {
                    final List<String> lines = cfg.getStringList(msg.getConfigKey());
                    for(int i = 0; i < lines.size(); i++) {
                        if((i+1) == lines.size()) {
                            msg.setValue(msg.getValue()+discordVerifyBot.getMessageFormatter().translateColor(lines.get(i)).replace("%PREFIX%", Message.PREFIX.getValue()));
                        } else msg.setValue(msg.getValue()+discordVerifyBot.getMessageFormatter().translateColor(lines.get(i)).replace("%PREFIX%", Message.PREFIX.getValue())+"\n");
                    }
                } else msg.setValue(discordVerifyBot.getMessageFormatter().translateColor(cfg.getString(msg.getConfigKey()).replace("%PREFIX%", Message.PREFIX.getValue())));
            } catch (NullPointerException ex) {
                log(Level.SEVERE, "Cloud not find Message '"+msg.getConfigKey()+"' in '"+file.getName()+"'.");
            }
        }
    }
}
