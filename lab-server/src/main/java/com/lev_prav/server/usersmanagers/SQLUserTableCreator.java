package com.lev_prav.server.usersmanagers;

import com.lev_prav.server.util.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SQLUserTableCreator {
    private final Connection connection;
    private final String usersTableName;
    private final Logger logger;

    public SQLUserTableCreator(Connection connection, String usersTableName, Logger logger) {
        this.connection = connection;
        this.usersTableName = usersTableName;
        this.logger = logger;
    }

    private void createUserTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS " + usersTableName + " (username VARCHAR(100) NOT NULL"
                + " PRIMARY KEY, password VARCHAR(100) NOT NULL)");
    }

    public List<User> init() throws SQLException {
        createUserTable();
        List<User> sqlUsers = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + usersTableName);
        while (result.next()) {
            sqlUsers.add(new User(result.getString("username"), result.getString("password")));
        }
        logger.info(() -> "added data of " + sqlUsers.size() + " users from the database");
        return sqlUsers;
    }
}
