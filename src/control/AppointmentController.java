package control;

import helperDB.AppointmentsQuery;
import helperDB.ContactsQuery;
import helperDB.CustomersQuery;
import helperDB.UsersQuery;
import helperTime.TimeTranslation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Appointment controller allows user to interact with an appointment interface.
 * This includes viewing appointments in a table view (either all, by month or by week) and
 * dding appointments or updating and deleting selected appointments.
 */
public class AppointmentController implements Initializable {

    /**
     * Add, update or delete appointment button.
     */
    public Button Execute = new Button();
    /**
     * Appointment table.
     */
    public TableView<Appointment> AppointmentsTable;
    /**
     * Appointment id column.
     */
    public TableColumn<Appointment, Integer> AppointmentID;
    /**
     * Appointment title column.
     */
    public TableColumn<Appointment, String> Title;
    /**
     * Appointment description column.
     */
    public TableColumn<Appointment, String> Description;
    /**
     * Appointment location column.
     */
    public TableColumn<Appointment, String> Location;
    /**
     * Appointment contact name column.
     */
    public TableColumn<Appointment, Contact> ContactName;
    /**
     * Appointment type column.
     */
    public TableColumn<Appointment, String> AppType;
    /**
     * Appointment start time and date column.
     */
    public TableColumn<Appointment, LocalDateTime> AppStart;
    /**
     * Appointment end time and date column.
     */
    public TableColumn<Appointment, LocalDateTime> AppEnd;
    /**
     * Appointment associated customer id column.
     */
    public TableColumn<Appointment, Integer> CustomerID;
    /**
     * Appointment associated user id column.
     */
    public TableColumn<Appointment, Integer> UserID;
    /**
     * Add tab to change to the add interface.
     */
    public Tab AddTab = new Tab();
    /**
     * Update tab to change to the update interface.
     */
    public Tab UpdateTab = new Tab();
    /**
     * Delete tab to change to the delete interface.
     */
    public Tab DeleteTab = new Tab();
    /**
     * Radio button appointment view group.
     */
    public ToggleGroup appointmentViews;
    /**
     * Appointment id input field - always disabled.
     */
    public TextField AppIdText = new TextField();
    /**
     * Appointment title input field.
     */
    public TextField AppTitleText = new TextField();
    /**
     * Appointment description input field.
     */
    public TextField AppDescriptionText = new TextField();
    /**
     * Appointment location input field.
     */
    public TextField AppLocationText = new TextField();
    /**
     * Appointment type input field.
     */
    public TextField AppTypeText = new TextField();
    /**
     * Appointment associated contact input combo box.
     */
    public ComboBox<Contact> ContactCombo = new ComboBox<>();
    /**
     * Appointment date input date picker.
     */
    public DatePicker AppDatePicker = new DatePicker();
    /**
     * Appointment start time input combo box.
     */
    public ComboBox<LocalTime> AppStartCombo = new ComboBox<>();
    /**
     * Appointment end time input combo box.
     */
    public ComboBox<LocalTime> AppEndCombo = new ComboBox<>();
    /**
     * Appointment associated customer id input combo box.
     */
    public ComboBox<Customer> CustomerIdCombo = new ComboBox<>();
    /**
     * Appointment associated user id input combo box.
     */
    public ComboBox<User> UserIdCombo = new ComboBox<>();
    /**
     * All appointments radio button.
     */
    public RadioButton AllRadio;
    /**
     * Current week appointments radio button.
     */
    public RadioButton WeekRadio;
    /**
     * Current month appointments radio button.
     */
    public RadioButton MonthRadio;

    /**
     * Lambda expression created to Alert the user when appointments are overlapping.
     * This alert is repeated within the code, so this method helps with repetition.
     */
    public alertMessages alertOverlappingMessage = () -> {
        // Alert message for overlapping customer appointments
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Appointments are overlapping for the customer. Please choose alternative times.");
        alert.showAndWait();
    };

    /**
     * Initialize function sets up the appointments page with relevant information.
     * Specifically, it sets and fills out the appointments table and combo boxes.
     * In addition, it incorporates a listener for the user selecting different appointments in table.
     * This listener fills out all field boxes below the table.
     *
     * @param url initialize url
     * @param resourceBundle initialize resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Setting all data within the appointments table
            AppointmentsTable.setItems(AppointmentsQuery.selectApps());
            AppointmentID.setCellValueFactory(new PropertyValueFactory<>("appId"));
            Title.setCellValueFactory(new PropertyValueFactory<>("title"));
            Description.setCellValueFactory(new PropertyValueFactory<>("description"));
            Location.setCellValueFactory(new PropertyValueFactory<>("location"));
            ContactName.setCellValueFactory(new PropertyValueFactory<>("contact"));
            AppType.setCellValueFactory(new PropertyValueFactory<>("type"));
            AppStart.setCellValueFactory(new PropertyValueFactory<>("start"));
            AppEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
            CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            UserID.setCellValueFactory(new PropertyValueFactory<>("userId"));

            // Setting combo boxes
            ObservableList<Contact> contactsList = ContactsQuery.selectContacts();
            ObservableList<Customer> customersList = CustomersQuery.selectCustomers();
            ObservableList<User> usersList = UsersQuery.selectUsers();
            ContactCombo.setItems(contactsList);
            CustomerIdCombo.setItems(customersList);
            UserIdCombo.setItems(usersList);
            AppStartCombo.setItems(TimeTranslation.returnBusinessHours());
            AppEndCombo.setItems(TimeTranslation.returnBusinessHours());

            // Listener for user clicking different appointments within the appointment table that fills out the
            // text fields for the update and delete tabs
            AppointmentsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Appointment>() {
                @Override
                public void changed(ObservableValue<? extends Appointment> observableValue, Appointment appointment, Appointment t1) {
                    try {
                        if ((Execute.getText().equals("Delete")) || (Execute.getText().equals("Update"))) {
                            fillAppointments();
                        } else {
                            clearAppointments();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute allows the user to add, update or delete an appointment.
     * The execute button changes its name depending on which tab is selected ("Add", "Update, or "Delete").
     * When the "Add" button is pressed, it adds an appointment to the database and updates the table.
     * When the "Update" button is pressed, it updates the currently selected appointment and updates the table.
     * When the "Delete" button is pressed, it deletes the currently selected appointment and displays a message with
     * the appointment id and type.
     * A Lambda expression is incorporated to print out an error message when appointments are found to be overlapping
     * This function is used multiple times, so helps with repetition within the code.
     *
     * @param actionEvent clicking the execute button (Add, Update or Delete)
     * @throws SQLException exception in case sql commands return an error
     */
    public void execute(ActionEvent actionEvent) throws SQLException {
        // Add appointment button functionality
        if(Execute.getText().equals("Add")) {
            try {
                // Gathering all filled out fields within the form
                String title = AppTitleText.getText();
                String description = AppDescriptionText.getText();
                Customer customer = CustomerIdCombo.getValue();
                int customerId = customer.getId();
                String location = AppLocationText.getText();
                Contact contact = ContactCombo.getValue();
                int contactId = contact.getId();
                String type = AppTypeText.getText();
                User user = UserIdCombo.getValue();
                int userId = user.getId();

                LocalDate date = AppDatePicker.getValue();
                LocalTime startTime = AppStartCombo.getValue();
                LocalTime endTime = AppEndCombo.getValue();
                LocalDateTime start = LocalDateTime.of(date, startTime);
                LocalDateTime end = LocalDateTime.of(date, endTime);

                // Making sure no appointments overlap for any customers before adding
                if (!overlapTest(date, startTime, endTime)) {
                    AppointmentsQuery.insertApp(title, description, location, type, start, end, customerId, userId, contactId);

                    // Clear all selections
                    clearAppointments();

                    // Refresh table
                    setAppointments();

                } else {
                    // Lambda implemented: Alert for overlapping appointments
                    alertOverlappingMessage.printOverlappingAlert();
                }
            } catch (NullPointerException e) {
                //
            }

        // Delete appointment functionality
        } else if (Execute.getText().equals("Delete")) {
            // Making sure an appointment is selected
            if (AppIdText.getText() != "Auto-Generated") {
                try {
                    // Gathering appointment id and type
                    int appId = AppointmentsTable.getSelectionModel().getSelectedItem().getAppId();
                    String type = AppointmentsTable.getSelectionModel().getSelectedItem().getType();

                    // Deleting appointment
                    AppointmentsQuery.deleteApp(appId);

                    // Refresh table
                    setAppointments();

                    // Alert showing which customer was deleted
                    Alert showInformation = new Alert(Alert.AlertType.INFORMATION);
                    showInformation.setTitle("Appointment Information");
                    showInformation.setContentText("Appointment Deleted: \n\nID: " + appId + "\nType: " + type);
                    showInformation.showAndWait();

                    // Clearing all text boxes
                    clearAppointments();
                } catch (NullPointerException e) {
                    //
                }
            }
        // Update appointment functionality
        } else {
            // Making sure an appointment is selected
            if (AppIdText.getText() != "Auto-Generated") {
                // Gathering filled out appointment fields
                int appId = Integer.parseInt(AppIdText.getText());
                String title = AppTitleText.getText();
                String description = AppDescriptionText.getText();
                Customer customer = CustomerIdCombo.getValue();
                int customerId = customer.getId();
                String location = AppLocationText.getText();
                Contact contact = ContactCombo.getValue();
                int contactId = contact.getId();
                String type = AppTypeText.getText();
                User user = UserIdCombo.getValue();
                int userId = user.getId();

                LocalDate date = AppDatePicker.getValue();
                LocalTime startTime = AppStartCombo.getValue();
                LocalTime endTime = AppEndCombo.getValue();
                LocalDateTime start = LocalDateTime.of(date, startTime);
                LocalDateTime end = LocalDateTime.of(date, endTime);

                // Making sure there are no overlapping appointments for customers before updating
                if (!overlapTest(date, startTime, endTime, AppointmentsQuery.selectApp(appId))) {
                    AppointmentsQuery.updateApp(appId, title, description, location, type, start, end, customerId, userId, contactId);

                    // Refresh table
                    setAppointments();

                } else {
                    // Lambda implemented: Error message about overlapping appointments for customers
                    alertOverlappingMessage.printOverlappingAlert();
                }
            }
        }
    }

    /**
     * Cancel brings the user back to the main navigational page.
     *
     * @param actionEvent clicking the cancel button
     * @throws IOException exception in case of input/output error
     */
    public void cancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../view/main-form.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * addTabClicked function changes the interface for adding appointments.
     * It changes the label of the execute button to say "Add",
     * enables all fields except for the appointment id,
     * and clears all fields so the user can easily input an appointment to add.
     *
     * @param event selecting the "Add" tab
     */
    public void addTabClicked(Event event) {
        // Changing label of button to Add
        Execute.setText("Add");

        // Enabling all text fields except appointment ID
        disabledOrEnabled(false);

        // Clearing all text fields
        clearAppointments();
    }

    /**
     * updateTabClicked function changes the interface for updating appointments.
     * It changes the label of the execute button to say "Update",
     * enables all fields except for the appointment id,
     * anc fills all fields with the selected appointment information.
     *
     * @param event selecting the "Update" tab
     * @throws SQLException exception in case sql commands return an error
     */
    public void updateTabClicked(Event event) throws SQLException {
        // Changing label of button to Update
        Execute.setText("Update");

        // Enabling all text fields except appointment ID
        disabledOrEnabled(false);

        // Filling all text fields with the selected appointment information
        fillAppointments();
    }

    /**
     * deleteTabClicked function changes the interface for deleting appointments.
     * It changes the label of the execute button to say "Delete",
     * disables all fields, and fills all fields with the selected appointment information.
     *
     * @param event selecting the "Delete" tab
     * @throws SQLException exception in case sql commands return an error
     */
    public void deleteTabClicked(Event event) throws SQLException {
        // Changing label of button to Delete
        Execute.setText("Delete");

        // Disabling all fields
        disabledOrEnabled(true);

        // Filling all fields with the selected appointment information
        fillAppointments();
    }

    /**
     * The all radio button function changes the appointment table to show all appointments.
     *
     * @param actionEvent selecting the "All" radio button
     * @throws SQLException exception in case sql commands return an error
     */
    public void allRadioPressed(ActionEvent actionEvent) throws SQLException {
        setAppointments();
    }

    /**
     * The Week radio button changes the appointment table to show all appointments within the current week.
     * The current week is from Sunday to Saturday.
     *
     * @param actionEvent selecting the "Week" radio button
     * @throws SQLException exception in case sql commands return an error
     */
    public void weekRadioPressed(ActionEvent actionEvent) throws SQLException {
        setAppointments();
    }

    /**
     * The Month radio button changes the appointment table to show all appointments within the current month.
     *
     * @param actionEvent selecting the "Month" radio button
     * @throws SQLException exception in case sql commands return an error
     */
    public void monthRadioPressed(ActionEvent actionEvent) throws SQLException {
        setAppointments();
    }

    /**
     * The setAppointments function sets the appointments table to either show all appointments, all appointments in current
     * week or all appointments in current month.
     * This depends on whether the "All", "Month", or "Week" radio buttons are selected.
     * The current week is set as Sunday to Saturday.
     * This function is called within all radio buttons and whenever the table needs to be updated.
     * A lambda expression is incorporated to calculate the last day of the week given the current day's int value.
     * The current days input int value is 1 to 7 for Monday to Sunday and
     * The last day of the week output int value is 1 to 7 for Sunday to Saturday.
     * This method helps decrease the amount of times the equation is written out, decreasing programmer error.
     *
     * @throws SQLException exception in case sql commands return an error
     */
    public void setAppointments() throws SQLException {
        // Functionality for the week radio button being selected
        if (WeekRadio.isSelected()) {
            ObservableList<Appointment> appByWeek = FXCollections.observableArrayList();
            LocalDate currentDate = LocalDate.now();
            int currentDayValue = currentDate.getDayOfWeek().getValue(); // Integer value of week (1 to 7) (Monday is 1)

            // Going through all appointments and checking if date is within the current week (Sunday to Saturday)
            for(Appointment i : AppointmentsQuery.selectApps()) {
                // Checking if the current week's Sunday is before or equal to the appointment date
                Boolean firstDateBefore = currentDate.minusDays(currentDayValue).isBefore(i.getStart().toLocalDate());
                Boolean firstDateEqual = currentDate.minusDays(currentDayValue).isEqual(i.getStart().toLocalDate());

                // Lambda expression implemented
                lastDayInt lastDay = n -> 7 - (n + 1);

                // Checking if the current week's Saturday is after or equal to the appointment date
                Boolean lastDateAfter = currentDate.plusDays(lastDay.calculateLastDay(currentDayValue)).isAfter(i.getStart().toLocalDate());
                Boolean lastDateEqual = currentDate.plusDays(lastDay.calculateLastDay(currentDayValue)).isEqual(i.getStart().toLocalDate());

                // If all above booleans are true, then the appointment is within the current week, and it is added to the new list
                if ((firstDateBefore || firstDateEqual) && (lastDateAfter || lastDateEqual)) {
                    appByWeek.add(i);
                }

                // The appointment table is updated with new list
                AppointmentsTable.setItems(appByWeek);
            }

        // Functionality for the month radio button being selected
        } else if (MonthRadio.isSelected()) {
            ObservableList<Appointment> appByMonth = FXCollections.observableArrayList();
            LocalDate dateNow = LocalDate.now();
            LocalDate dateFirst = dateNow.withDayOfMonth(1); // Getting the first day of the month
            LocalDate dateLast = YearMonth.now().atEndOfMonth(); // Getting the last day of the month

            // Checking if any appointments are within the current month
            for(Appointment i : AppointmentsQuery.selectApps()) {
                if ((dateFirst.getDayOfMonth() <= i.getStart().getDayOfMonth()) && (dateLast.getDayOfMonth() >= i.getStart().getDayOfMonth()) && (dateFirst.getMonth() == i.getStart().getMonth())) {
                    appByMonth.add(i);
                }
            }

            // Setting the table with only appointments from current month
            AppointmentsTable.setItems(appByMonth);

        // Functionality for the all radio button being selected
        } else {
            // Setting table to show all appointments
            AppointmentsTable.setItems(AppointmentsQuery.selectApps());
        }
    }

    /**
     * The clearAppointments function clears all input fields including text boxes,
     * combo boxes and date pickers for the appointments.
     */
    public void clearAppointments() {
        try {
            AppIdText.clear();
        } catch (NullPointerException e) {
            //
        }
        AppIdText.clear();
        AppTitleText.clear();
        AppDescriptionText.clear();
        AppLocationText.clear();
        AppTypeText.clear();
        AppDatePicker.getEditor().clear();
        AppDatePicker.setValue(null);
        CustomerIdCombo.valueProperty().set(null);
        ContactCombo.valueProperty().set(null);
        UserIdCombo.valueProperty().set(null);
        AppStartCombo.valueProperty().set(null);
        AppEndCombo.valueProperty().set(null);
    }

    /**
     * The fillAppointments function fills all input fields including text boxes,
     * combo boxes and date pickers for the selected appointment.
     *
     * @throws SQLException exception in case sql commands return an error
     */
    public void fillAppointments() throws SQLException {
        try {
            AppIdText.setText(String.valueOf(AppointmentsTable.getSelectionModel().getSelectedItem().getAppId()));
            AppTitleText.setText(String.valueOf((AppointmentsTable.getSelectionModel().getSelectedItem().getTitle())));
            AppDescriptionText.setText(String.valueOf((AppointmentsTable.getSelectionModel().getSelectedItem().getDescription())));
            CustomerIdCombo.setValue(CustomersQuery.selectCustomer(AppointmentsTable.getSelectionModel().getSelectedItem().getCustomerId()));
            AppLocationText.setText(String.valueOf((AppointmentsTable.getSelectionModel().getSelectedItem().getLocation())));
            ContactCombo.setValue(ContactsQuery.selectContact(AppointmentsTable.getSelectionModel().getSelectedItem().getContactId()));
            AppTypeText.setText(String.valueOf(AppointmentsTable.getSelectionModel().getSelectedItem().getType()));
            UserIdCombo.setValue(UsersQuery.selectUser(AppointmentsTable.getSelectionModel().getSelectedItem().getUserId()));
            AppDatePicker.setValue(AppointmentsTable.getSelectionModel().getSelectedItem().getStart().toLocalDate());
            AppStartCombo.setValue(AppointmentsTable.getSelectionModel().getSelectedItem().getStart().toLocalTime());
            AppEndCombo.setValue(AppointmentsTable.getSelectionModel().getSelectedItem().getEnd().toLocalTime());
        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * The disableOrEnable function sets all input fields (except the appointment id)
     * as enabled if the input is false, and disabled if the input is true.
     *
     * @param value the input of true or false
     */
    public void disabledOrEnabled(Boolean value) {
        AppTitleText.setDisable(value);
        AppDescriptionText.setDisable(value);
        AppLocationText.setDisable(value);
        AppTypeText.setDisable(value);
        AppDatePicker.setDisable(value);
        CustomerIdCombo.setDisable(value);
        ContactCombo.setDisable(value);
        UserIdCombo.setDisable(value);
        AppStartCombo.setDisable(value);
        AppEndCombo.setDisable(value);
    }

    /**
     * The overlapTest function checks if there is an overlap with appointments.
     * It returns true if the inputted appointment times coincide with any other existing appointments.
     * This is an overloaded function.
     *
     * @param date date (LocalDate) user is trying to use for adding or updating an appointment
     * @param start start (LocalTime) time user is trying to use for adding or updating an appointment
     * @param end end (LocalTime) time user is trying to use for adding or updating an appointment
     * @return true or false - true if there is an overlap and false if there isn't
     * @throws SQLException exception in case sql commands return an error
     */
    public boolean overlapTest(LocalDate date, LocalTime start, LocalTime end) throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentsQuery.selectApps();
        boolean isOverlapping = false;
        LocalDateTime S1 = LocalDateTime.of(date, start);
        LocalDateTime E1 = LocalDateTime.of(date, end);

        for (Appointment i : allAppointments) {
            // Variables for each appointment's start and end
            LocalDateTime S2 = i.getStart();
            LocalDateTime E2 = i.getEnd();

            if (i.getCustomerId() != CustomerIdCombo.getValue().getId()) {
                continue; // Making sure to only check appointments for the chosen customer
            } else if ((S2.isAfter(S1) || S2.isEqual(S1)) && S2.isBefore(E1)) {
                isOverlapping = true; // Checking for overlaps with possible appointment starting after or at the same time
            } else if (E2.isAfter(S1) && (E2.isBefore(E1) || E2.isEqual(E1))) {
                isOverlapping = true; // Checking for overlaps with possible appointment enveloping entire appointment
            } else if ((S2.isBefore(S1) || (S2.isEqual(S1))) && (E2.isAfter(E1) || E2.isEqual(E1))) {
                isOverlapping = true; // Checking for overlaps with possible appointment starting before appointment
            }
        }

        return isOverlapping;
    }

    /**
     * The overlapTest function checks if there is an overlap with appointments.
     * It returns true if the inputted appointment times coincide with any other existing appointments.
     * This is an overloaded function.
     *
     * @param date date (LocalDate) user is trying to use for adding or updating an appointment
     * @param start start (LocalTime) time user is trying to use for adding or updating an appointment
     * @param end end (LocalTime) time user is trying to use for adding or updating an appointment
     * @param appointment the currently selected appointment
     * @return true or false - true if there is an overlap and false if there isn't
     * @throws SQLException exception in case sql commands return an error
     */
    public boolean overlapTest(LocalDate date, LocalTime start, LocalTime end, Appointment appointment) throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentsQuery.selectApps();
        boolean isOverlapping = false;
        LocalDateTime S1 = LocalDateTime.of(date, start);
        LocalDateTime E1 = LocalDateTime.of(date, end);

        for (Appointment i : allAppointments) {
            // Variables for each appointment's start and end
            LocalDateTime S2 = i.getStart();
            LocalDateTime E2 = i.getEnd();

            if ((i.getCustomerId() != CustomerIdCombo.getValue().getId()) || (i.getAppId() == appointment.getAppId())) {
                continue; // Making sure to only check appointments for the chosen customer
            } else if ((S2.isAfter(S1) || S2.isEqual(S1)) && S2.isBefore(E1)) {
                isOverlapping = true; // Checking for overlaps with possible appointment starting after or at the same time
            } else if (E2.isAfter(S1) && (E2.isBefore(E1) || E2.isEqual(E1))) {
                isOverlapping = true; // Checking for overlaps with possible appointment enveloping entire appointment
            } else if ((S2.isBefore(S1) || (S2.isEqual(S1))) && (E2.isAfter(E1) || E2.isEqual(E1))) {
                isOverlapping = true; // Checking for overlaps with possible appointment starting before appointment
            }
        }

        return isOverlapping;
    }

    /**
     * Interface for calculating the last day in a week given the current int value. Returns an int corresponding to the last day.
     */
    public interface lastDayInt {
        /**
         * A lambda expression is incorporated to calculate the last day of the week given the current day's int value.
         * The current days input int value is 1 to 7 for Monday to Sunday and
         * The last day of the week output int value is 1 to 7 for Sunday to Saturday.
         * This method helps decrease the amount of times the equation is written out, decreasing programmer error.
         */
        int calculateLastDay(int n);
    }

    /**
     * Interface for printing an alert when appointments are overlapping. THis is used in multiple locations, so helps
     * with repetition.
     */
    public interface  alertMessages {
        /**
         * Lambda expression created to Alert the user when appointments are overlapping.
         * This alert is repeated within the code, so this method helps with repetition.
         */
        void printOverlappingAlert();
    }
}
