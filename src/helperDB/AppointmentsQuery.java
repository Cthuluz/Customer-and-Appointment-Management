package helperDB;

import helperTime.TimeTranslation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The appointment query class contains all helper functions for accessing the SQL database for appointments.
 */
public abstract class AppointmentsQuery {

    /**
     * insertApp function adds an appointment to the SQL database.
     *
     * @param title title of appointment to be inserted
     * @param description description of appointment to be inserted
     * @param location location of appointment to be inserted
     * @param type type of appointment to be inserted
     * @param start start time of appointment to be inserted
     * @param end end time of appointment to be inserted
     * @param customerId customer id of appointment to be inserted
     * @param userId user id associated with the appointment to be inserted
     * @param contactId contact id associated with the appointment to be inserted
     * @return integer value of how many changes took place
     * @throws SQLException exception in case sql commands return an error
     */
    public static int insertApp(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
        Timestamp UTCStart = Timestamp.valueOf(start);
        Timestamp UTCEnd = Timestamp.valueOf(end);


        String sqlCommand = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setString(1, title);
        preparedCommand.setString(2, description);
        preparedCommand.setString(3, location);
        preparedCommand.setString(4, type);
        preparedCommand.setTimestamp(5, UTCStart);
        preparedCommand.setTimestamp(6, UTCEnd);
        preparedCommand.setInt(7, customerId);
        preparedCommand.setInt(8, userId);
        preparedCommand.setInt(9, contactId);

        int numChanged = preparedCommand.executeUpdate();
        return numChanged;
    }

    public static int updateApp(int appId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) throws SQLException {
        Timestamp UTCStart = Timestamp.valueOf(start);
        Timestamp UTCEnd = Timestamp.valueOf(end);

        String sqlCommand = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setString(1, title);
        preparedCommand.setString(2, description);
        preparedCommand.setString(3, location);
        preparedCommand.setString(4, type);
        preparedCommand.setTimestamp(5, UTCStart);
        preparedCommand.setTimestamp(6, UTCEnd);
        preparedCommand.setInt(7, customerId);
        preparedCommand.setInt(8, userId);
        preparedCommand.setInt(9, contactId);
        preparedCommand.setInt(10, appId);

        int numChanged = preparedCommand.executeUpdate();
        return numChanged;
    }

    /**
     * deleteApp function deletes the appointment with the inputted id.
     *
     * @param appId inputted appointment id
     * @return int of how many changes took place
     * @throws SQLException exception in case sql commands return an error
     */
    public static int deleteApp(int appId) throws SQLException {
        String sqlCommand = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, appId);

        int numChanged = preparedCommand.executeUpdate();
        return numChanged;
    }

    /**
     * selectApps returns all appointments within the database.
     *
     * @return observable list of appointments
     * @throws SQLException exception in case sql commands return an error
     */
    public static ObservableList<Appointment> selectApps() throws SQLException {
        String sqlCommand = "SELECT * FROM APPOINTMENTS";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedCommand.executeQuery();

        ObservableList<Appointment> currentApps = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int appId = resultSet.getInt("Appointment_ID");
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp start = resultSet.getTimestamp("Start");
            Timestamp end = resultSet.getTimestamp("End");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            LocalDateTime localStart = start.toLocalDateTime();
            LocalDateTime localEnd = end.toLocalDateTime();

            Appointment currentApp = new Appointment(appId, title, description, location, type, localStart, localEnd, customerId, userId, contactId);
            currentApps.add(currentApp);
        }
        return currentApps;
    }

    /**
     * selectApp function returns appointment with the given id.
     *
     * @param appId inputted appointment id
     * @return an appointment
     * @throws SQLException exception in case sql commands return an error
     */
    public static Appointment selectApp(int appId) throws SQLException {
        String sqlCommand = "SELECT * FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, appId);
        ResultSet resultSet = preparedCommand.executeQuery();

        while (resultSet.next()) {
            String title = resultSet.getString("Title");
            String description = resultSet.getString("Description");
            String location = resultSet.getString("Location");
            String type = resultSet.getString("Type");
            Timestamp start = resultSet.getTimestamp("Start");
            Timestamp end = resultSet.getTimestamp("End");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            LocalDateTime localStart = start.toLocalDateTime();
            LocalDateTime localEnd = end.toLocalDateTime();

            Appointment chosenApp = new Appointment(appId, title, description, location, type, localStart, localEnd, customerId, userId, contactId);
            return chosenApp;
        }
        return null;
    }
}
