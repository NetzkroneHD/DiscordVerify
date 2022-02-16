package de.netzkronehd.discordverifybot.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends Database {

    public SQLite(String host, String database, String user, String password, int port) {
        super(host, database, user, password, port);

        try {
            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void connect() throws SQLException {
        setConnection(DriverManager.getConnection("jdbc:sqlite:"+database));
    }


}
