package helperDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The users query class contains all helper functions for accessing the SQL database for users.
 */
public abstract class UsersQuery {

    /**
     * The selectUsers returns all users in the users table from the SQL database.
     *
     * @return observable list of users
     * @throws SQLException exception in case sql commands return an error
     */
    public static ObservableList<User> selectUsers() throws SQLException {
        String sqlCommand = "SELECT * FROM USERS";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedCommand.executeQuery();

        ObservableList<User> currentUsers = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int userId = resultSet.getInt("User_ID");
            String name = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");

            User currentUser = new User(userId, name, password);
            currentUsers.add(currentUser);
        }
        return currentUsers;
    }

    /**
     * The selectUser function returns the user with the associated inputted id.
     *
     * @param userId inputted user id
     * @return a user
     * @throws SQLException exception in case sql commands return an error
     */
    public static User selectUser(int userId) throws SQLException {
        String sqlCommand = "SELECT * FROM USERS WHERE User_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, userId);
        ResultSet resultSet = preparedCommand.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("User_Name");
            String password = resultSet.getString("Password");
            String createDate = resultSet.getString("Create_Date");
            String createdBy = resultSet.getString("Created_By");
            String lastUpdate = resultSet.getString("Last_Update");

            User chosenUser = new User(userId, name, password);
            return chosenUser;
        }

        return null;
    }


}
