package com.lev_prav.server.usersmanagers;

import com.lev_prav.server.util.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

public class SQLUserManager extends UserManager {
    private final Lock lock;
    private final List<User> users;
    private final Connection connection;
    private final String usersTableName;
    private final Logger logger;

    public SQLUserManager(Lock lock, List<User> users, Connection connection, String usersTableName,
                          Logger logger) {
        super(users, lock);
        this.lock = lock;
        this.users = users;
        this.connection = connection;
        this.usersTableName = usersTableName;
        this.logger = logger;
    }

    @Override
    public void registerUser(User user) {
        try {
            lock.lock();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + usersTableName
                    + "(username,password) VALUES (?,?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.execute();
            users.add(user);
        } catch (SQLException e) {
            logger.severe(e.toString() + user.getPassword());
        } finally {
            lock.unlock();
        }
    }
}
