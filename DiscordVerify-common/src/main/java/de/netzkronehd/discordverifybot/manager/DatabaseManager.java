package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.database.Database;
import de.netzkronehd.discordverifybot.database.MySQL;
import de.netzkronehd.discordverifybot.database.SQLite;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseManager extends Manager {

    private final File file;
    private YamlConfiguration cfg;

    public DatabaseManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        setPriority(3);
        file = new File("plugins/DiscordVerifyBot", "database.yml");
        cfg = YamlConfiguration.loadConfiguration(file);


    }

    @Override
    public void onLoad() {
        createFile();
        readFile();
    }

    @Override
    public void onReload() {
        cfg = YamlConfiguration.loadConfiguration(file);
        onLoad();
    }

    @Override
    public void createFile() {
        if(!file.exists()) {
            cfg.set("Use-MySQL", false);
            cfg.set("MySQL.host", "127.0.0.1");
            cfg.set("MySQL.port", 3306);
            cfg.set("MySQL.user", "user");
            cfg.set("MySQL.password", "password");
            cfg.set("MySQL.database", "databaase");
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readFile() {
        if(cfg.getBoolean("Use-MySQL")) {
            final Database db = new MySQL(cfg.getString("MySQL.host"), cfg.getString("MySQL.database"), cfg.getString("MySQL.user"), cfg.getString("MySQL.password"), cfg.getInt("MySQL.port"));
            discordVerifyBot.setDatabase(db);
            try {
                db.connect();
                db.update("CREATE TABLE IF NOT EXISTS discordVerifications(uuid VARCHAR(64), name TEXT, discordId TEXT, timepoint TEXT, UNIQUE KEY(uuid, discordId))");
            } catch (SQLException e) {
                e.printStackTrace();
                log(Level.SEVERE, "Cloud not create MySQL-Connection: "+e);
            }

        } else {
            final File dbFile = new File("plugins/DiscordVerifyBot", "data.db");
            if(!dbFile.exists()) {
                try {
                    YamlConfiguration.loadConfiguration(dbFile).save(dbFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final Database db = new SQLite("0",dbFile.getAbsolutePath(),"","",3306);
            discordVerifyBot.setDatabase(db);

            try {
                db.connect();
                db.update("CREATE TABLE IF NOT EXISTS discordVerifications(uuid VARCHAR(64), name TEXT, discordId TEXT, timepoint TEXT)");
            } catch (SQLException e) {
                e.printStackTrace();
                log(Level.SEVERE, "Cloud not create SQLite-Connection: "+e);
            }

        }
    }
}
