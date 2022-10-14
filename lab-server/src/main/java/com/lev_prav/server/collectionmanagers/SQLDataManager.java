package com.lev_prav.server.collectionmanagers;

import com.lev_prav.common.data.Coordinates;
import com.lev_prav.common.data.Country;
import com.lev_prav.common.data.Location;
import com.lev_prav.common.data.Person;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.logging.Logger;

public class SQLDataManager {
    private static final int NAME_INDEX = 1;
    private static final int LOCATION_NAME_INDEX = 2;
    private static final int LOCATION_X_INDEX = 3;
    private static final int LOCATION_Y_INDEX = 4;
    private static final int LOCATION_Z_INDEX = 5;
    private static final int X_INDEX = 6;
    private static final int Y_INDEX = 7;
    private static final int PASSPORTID_INDEX = 8;
    private static final int NATIONALITY_INDEX = 9;
    private static final int HEIGHT_INDEX = 10;
    private static final int BIRTHDAY_INDEX = 11;
    private static final int CREATIONDATE_INDEX = 12;
    private static final int OWNER_INDEX = 13;
    private static final int ID_INDEX = 14;

    private final Connection connection;
    private final String personTableName;
    private final String usersTableName;
    private final Logger logger;

    public SQLDataManager(Connection connection, String personTableName, String usersTableName, Logger logger) {
        this.connection = connection;
        this.personTableName = personTableName;
        this.usersTableName = usersTableName;
        this.logger = logger;
    }

    private void createDataTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS " + personTableName
                + "(id SERIAL PRIMARY KEY, "
                + "name VARCHAR(50) NOT NULL, "
                + "location_name VARCHAR(50), "
                + "location_x DOUBLE precision NOT NULL, "
                + "location_y BIGINT NOT NULL, "
                + "location_z FLOAT NOT NULL, "
                + "x BIGINT NOT NULL CHECK(x<=177), "
                + "y FLOAT NOT NULL CHECK(y>-635), "
                + "passportid VARCHAR(50), "
                + "nationality VARCHAR(20) null , "
                + "height DOUBLE precision check(height>0) NOT NULL, "
                + "birthday TIMESTAMP NOT NULL, "
                + "creationdate TIMESTAMP NOT NULL, "
                + "owner VARCHAR(100) NOT NULL,"
                + "FOREIGN KEY(owner) REFERENCES " + usersTableName + "(username))");
    }

    public ArrayDeque<Person> initCollection() throws SQLException {
        createDataTable();
        ArrayDeque<Person> persons = new ArrayDeque<>();
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + personTableName);
        while (result.next()) {
            persons.add(getPersonFromTable(result));
        }
        logger.info(() -> "added " + persons.size() + " objects from the database");
        return persons;
    }

    private Person getPersonFromTable(ResultSet result) throws SQLException {
        Person person = new Person(result.getString("name"),
                new Coordinates(result.getLong("x"), result.getFloat("y")),
                result.getDouble("height"),
                result.getTimestamp("birthday").toLocalDateTime(),
                result.getString("passportid") != null ? result.getString("passportid") : null,
                result.getString("nationality") != null ? Country.valueOf(result.getString("nationality")) : null,
                new Location(
                        result.getDouble("location_x"),
                        result.getInt("location_y"),
                        result.getFloat("location_z"),
                        result.getString("location_name") != null ? result.getString("location_name") : null),
                result.getString("owner"));
        person.setId(result.getInt("id"));
        person.setCreationDate(result.getTimestamp("creationdate").toLocalDateTime().atZone(ZoneId.of("+05:00")));
        return person;
    }

    public boolean removeById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "
                    + personTableName + " WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.warning("error during removing object from table" + e);
            return false;
        }
        return true;
    }

    public Integer add(Person person) {
        int id;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + personTableName
                    + "(id,name,location_name,location_x,location_y,location_z,x,y,passportid,nationality,height,birthday,creationdate,"
                    + "owner) VALUES (default,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id");
            prepareStatement(preparedStatement, person);
            preparedStatement.setTimestamp(CREATIONDATE_INDEX, Timestamp.valueOf(person.getCreationDate().toLocalDateTime()));
            preparedStatement.setString(OWNER_INDEX, person.getOwnerUserName());
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            id = result.getInt("id");
        } catch (SQLException e) {
            logger.warning("error during adding object to table" + e);
            return null;
        }
        return id;
    }

    public boolean update(int id, Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + personTableName + " SET "
                    + "name=?,location_name=?, location_x=?, location_y=?, location_z=?, x=?, y=?, passportid=?, nationality=?,height=?,birthday=?"
                    + " WHERE id=?");
            prepareStatement(preparedStatement, person);
            preparedStatement.setInt(12, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.warning("error during updating object from table" + e);
            return false;
        }
        return true;
    }

    private void prepareStatement(PreparedStatement statement, Person person) throws SQLException {
        statement.setString(NAME_INDEX, person.getName());
        if (person.getLocation().getLocationName() == null) {
            statement.setNull(LOCATION_NAME_INDEX, Types.NULL);
        } else {
            statement.setString(LOCATION_NAME_INDEX, person.getLocation().getLocationName());
        }
        statement.setDouble(LOCATION_X_INDEX, person.getLocation().getLocationX());
        statement.setInt(LOCATION_Y_INDEX, person.getLocation().getLocationY());
        statement.setFloat(LOCATION_Z_INDEX, person.getLocation().getLocationZ());
        statement.setLong(X_INDEX, person.getCoordinates().getX());
        statement.setFloat(Y_INDEX, person.getCoordinates().getY());
        if (person.getPassportID() == null) {
            statement.setNull(PASSPORTID_INDEX, Types.NULL);
        } else {
            statement.setString(PASSPORTID_INDEX, person.getPassportID());
        }
        if (person.getNationality() == null) {
            statement.setNull(NATIONALITY_INDEX, Types.NULL);
        } else {
            statement.setString(NATIONALITY_INDEX, person.getNationality().toString());
        }
        statement.setDouble(HEIGHT_INDEX, person.getHeight());
        statement.setDate(BIRTHDAY_INDEX, Date.valueOf(person.getBirthday().toLocalDate()));
    }

    public boolean deleteAllOwned(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + personTableName
                    + " WHERE owner=?");
            statement.setString(1, username);
            statement.execute();
        } catch (SQLException e) {
            logger.warning("error during removing object from table" + e);
            return false;
        }
        return true;
    }
}
