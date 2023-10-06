package control;

import helperDB.AppointmentsQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Main controller class allows user to navigate to all pages of the system.
 * This includes buttons that lead to the appointments page, customers page and reports page.
 * Exiting is possible through the exit button.
 */
public class MainController {

    /**
     * The goToAppointments function sends the user to the appointments form.
     *
     * @param actionEvent clicking the appointments button
     * @throws IOException exception in case of input/output error
     */
    public void goToAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/appointment-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Appointments Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The goToCustomers sends the user to the customers form.
     *
     * @param actionEvent clicking the customers button
     * @throws IOException exception in case of input/output error
     */
    public void goToCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/customers-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Customer Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The goToReports function sends the user to the reports form.
     *
     * @param actionEvent clicking the reports button
     * @throws IOException exception in case of input/output error
     */
    public void goToReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/reports-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Reports Page");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The exit button exits the program.
     *
     * @param actionEvent clicking the exit button
     */
    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * The appNotify function checks if there are any upcoming appointments.
     * It notifies the user of any upcoming appointments for that user within the next 15 minutes or
     * it notifies user if there are no upcoming appointments.
     *
     * @param userId user id input of the user logging in
     * @throws SQLException exception in case sql commands return an error
     */
    public void appNotify(int userId) throws SQLException {
        // Defining time from now to 15 minutes from now
        LocalDateTime S1 = LocalDateTime.now();
        LocalDateTime E1 = S1.plusMinutes(15);
        String message = "";

        // Going through all appointments
        for (Appointment i : AppointmentsQuery.selectApps()) {
            // Taking time for each appointment start and end
            LocalDateTime S2 = i.getStart();
            LocalDateTime E2 = i.getEnd();

            // Making sure to only check appointments for the current user
            if (userId == i.getUserId()) {
                if ((S2.isAfter(S1) || S2.isEqual(S1)) && S2.isBefore(E1)) {
                    // Checking for overlaps with the next 15 minutes starting after or at the same time as the given appointment
                    message += "Appointment ID: " + i.getAppId() + "\nDate: " + i.getStart().toLocalDate() + "\nStart Time: " + i.getStart().toLocalTime() + "\nEnd Time: " + i.getEnd().toLocalTime() + "\n\n";
                } else if (E2.isAfter(S1) && (E2.isBefore(E1) || E2.isEqual(E1))) {
                    // Checking for overlaps with the next 15 minutes enveloping entire given appointment
                    message += "Appointment ID: " + i.getAppId() + "\nDate: " + i.getStart().toLocalDate() + "\nStart Time: " + i.getStart().toLocalTime() + "\nEnd Time: " + i.getEnd().toLocalTime() + "\n\n";
                } else if ((S2.isBefore(S1) || (S2.isEqual(S1))) && (E2.isAfter(E1) || E2.isEqual(E1))) {
                    // Checking for overlaps with the next 15 minutes starting before given appointment
                    message += "Appointment ID: " + i.getAppId() + "\nDate: " + i.getStart().toLocalDate() + "\nStart Time: " + i.getStart().toLocalTime() + "\nEnd Time: " + i.getEnd().toLocalTime() + "\n\n";
                }
            }
        }

        // If appointments are upcoming in the next 15 minutes, displays the appointments id, date, and times
        if (message != "") {
            Alert showInformation = new Alert(Alert.AlertType.INFORMATION);
            showInformation.setTitle("Appointment Information");
            showInformation.setContentText("Upcoming appointments: \n\n" +message);
            showInformation.showAndWait();
        // If no appointments found within the first 15 minutes, a message informs the user that there are no upcoming appointments
        } else {
            Alert showInformation = new Alert(Alert.AlertType.INFORMATION);
            showInformation.setTitle("Appointment Information");
            showInformation.setContentText("No upcoming appointments.");
            showInformation.showAndWait();
        }
    }
}
