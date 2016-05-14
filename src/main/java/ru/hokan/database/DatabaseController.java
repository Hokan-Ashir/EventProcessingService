package ru.hokan.database;

import org.apache.log4j.Logger;

import java.sql.*;

public enum DatabaseController {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(DatabaseController.class);

    public static final String USER_NAME = "test";
    public static final String USER_PASSWORD = "test";
    private Connection connection;

    public void initialize() {
        try {
            Class.forName("org.h2.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:h2:file:./database",
                    USER_NAME, USER_PASSWORD);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createEventsTableSQL());
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private String createEventsTableSQL() {
        return "DROP TABLE EVENTS;" +
                "CREATE TABLE EVENTS" +
                "(event_time TIMESTAMP);";
    }

    public void insertEvent(long time) {
        Timestamp timestamp = new Timestamp(time);
        try {
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO EVENTS (event_time) VALUES (\'" + timestamp.toString() + "\')");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public int selectEvents(Duration duration) {
        String parameter = duration.getValue();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT event_time FROM EVENTS WHERE event_time > timestampadd(" + parameter + ", -1, now());");
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return 0;
    }
}
