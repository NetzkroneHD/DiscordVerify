package de.netzkronehd.discordverifybot.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {

    protected String host, database, user, password;
    protected int port;
    protected Connection con;

    public Database(String host, String database, String user, String password, int port) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public void update(String qry) throws SQLException {
        con.createStatement().executeUpdate(qry);
    }

    public ResultSet getResult(String query) throws SQLException {
        return con.prepareStatement(query).executeQuery();
    }

    public abstract void connect() throws SQLException;
    public void disConnect() throws SQLException {
        con.close();
    }
    public boolean isConnected() {
        return con != null;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public Connection getConnection() {
        return con;
    }
    public void setConnection(Connection con) {
        this.con = con;
    }


}
