package control;

import helperDB.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.control.*;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.ZoneId;

/**
 * Login form controller class allows user to log into the system.
 * The ZoneId current location is displayed above the login.
 * All text is in English or French depending on user's location.
 * Correct input for Username and password logs the user in, while incorrect
 * login information displays an error.
 * Each login or attempt is documented in a file named login_activity.txt at root location of project,
 *      which stores the username, success status, date and time.
 */
public class LoginFormController implements Initializable {
    /**
     * User's current location label.
     */
    public Label CurrentLocation;
    /**
     * Username label.
     */
    public Label Username;
    /**
     * Password label.
     */
    public Label Password;
    /**
     * Location label.
     */
    public Label Location;
    /**
     * Input field for username.
     */
    public TextField UsernameInput;
    /**
     * Input field for password.
     */
    public TextField PasswordInput;
    /**
     * Submit button to login.
     */
    public Button Submit;
    /**
     * Resource bundle for the current system English/French translations.
     */
    ResourceBundle bundle = ResourceBundle.getBundle("languages/Login", Locale.getDefault());
    /**
     * Current system ZoneId.
     */
    static ZoneId systemZoneId = ZoneId.systemDefault();

    /**
     * Initialize sets up the interface for the login form to show all relevant information.
     * It displays the current user's location and all text in either French or english
     * depending on the user's location.
     *
     * @param url initialize url
     * @param resourceBundle initialize resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Displaying user's current ZoneId location
        CurrentLocation.setText(String.valueOf(systemZoneId));

        // Setting values of labels to either be in English or French
        Username.setText(bundle.getString("Username"));
        Password.setText(bundle.getString("Password"));
        Location.setText(bundle.getString("Location"));
        Submit.setText(bundle.getString("Submit"));
    }

    /**
     * The submitLogin function tries to log the user into the program.
     * It compares the inputted username and password to all username and passwords in the database.
     * If there is a match to username and password, it logs the user in and records a successful login
     * in the file login_activity.txt.
     * Otherwise, it displays an error message, and records a failed login attempt in the file login_activity.txt.
     *
     * @param actionEvent clicking the submit button
     * @throws IOException exception in case of input/output error
     * @throws SQLException exception in case sql commands return an error
     * @throws NullPointerException exception in case of null pointer error
     */
    public void submitLogin(ActionEvent actionEvent) throws IOException, SQLException, NullPointerException {
        // Selecting all users from the database who match inputted username and password
        String sqlCommand = "SELECT * FROM USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setString(1, UsernameInput.getText());
        preparedCommand.setString(2, PasswordInput.getText());
        ResultSet resultSet = preparedCommand.executeQuery();

        // Setting up the FileWriter and PrintWriter to be able to write to file login_activity.txt
        FileWriter fileLoginInfo = new FileWriter("login_activity.txt", true);
        PrintWriter printLoginInfo = new PrintWriter(fileLoginInfo);

        // If there is a matching username and password, the user is logged in and a success is recorded in the
        // login_activity.txt file
        if (resultSet.next()) {
            // Setting up loader to send information from the login controller to the main form controller
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/main-form.fxml"));
            loader.load();

            // Calling function defined within the main controller to notify the user of upcoming appointments within 15 minutes
            // Needed information is split between main controller and login form controller,
            //      leading to necessity of sending controller information
            int userId = resultSet.getInt("User_ID");
            MainController mainController = loader.getController();
            mainController.appNotify(userId);

            // Going to the main form
            Parent root = FXMLLoader.load(getClass().getResource("../view/main-form.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 400, 300);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();

            // Printing successful login to the login_activity.txt file
            printLoginInfo.println("User " + UsernameInput.getText() + " successfully logged in on " + LocalDate.now() + " at " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ".\n");
            printLoginInfo.close();

        } else {
            // Printing failed login attempt to the login_activity.txt file
            printLoginInfo.println("User " + UsernameInput.getText() + " failed login attempt on " + LocalDate.now() + " at " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + ".\n");
            printLoginInfo.close();

            // Giving user an alert for failed login depending on language
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("ErrorTitle"));
            alert.setContentText(bundle.getString("ErrorText"));
            alert.showAndWait();
        }
    }
}
