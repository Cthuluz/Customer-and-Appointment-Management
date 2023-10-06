package control;

import helperDB.AppointmentsQuery;
import helperDB.ContactsQuery;
import helperDB.CustomersQuery;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

/**
 * Reports controller class allows viewing of three different reports.
 * The section labelled "Number of Appointments by Type and Month" report allows input for type and month, and displays
 * the associated appointments.
 * The section labelled "Contact Schedule" shows all appointments for the selected contact
 * The section labelled "Customers of the Month" shows the top three customers with the most hours of appointments scheduled.
 */
public class ReportsController implements Initializable {
    /**
     * Label showing number of appointments given month and type.
     */
    public Label NumberResult;
    /**
     * Contact input combo box.
     */
    public ComboBox<Contact> ContactCombo = new ComboBox<>();
    /**
     * Appointments table.
     */
    public TableView<Appointment> AppointmentsTable;
    /**
     * Appointment id table column.
     */
    public TableColumn<Appointment, Integer> AppointmentId;
    /**
     * Appointment title table column.
     */
    public TableColumn<Appointment, String> Title;
    /**
     * Appointment type table column.
     */
    public TableColumn<Appointment, String> Type;
    /**
     * Appointment description table column.
     */
    public TableColumn<Appointment, String> Description;
    /**
     * Appointment start time and date table column.
     */
    public TableColumn<Appointment, LocalDateTime> StartDateTime;
    /**
     * Appointment end time and date table column.
     */
    public TableColumn<Appointment, LocalDateTime> EndDateTime;
    /**
     * Appointment associated customer id table column.
     */
    public TableColumn<Appointment, Integer> CustomerId;
    /**
     * Months input combo box.
     */
    public ComboBox<String> MonthsCombo = new ComboBox<>();
    /**
     * Type input combo box.
     */
    public ComboBox<String> TypeCombo = new ComboBox<>();
    /**
     * Observable list of all month String values.
     */
    public static ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    /**
     * First place label.
     */
    public Label FirstPlaceCustomer;
    /**
     * Second place label.
     */
    public Label SecondPlaceCustomer;
    /**
     * Third place label.
     */
    public Label ThirdPlaceCustomer;

    /**
     * The initialize sets up the reports page with all relevant information.
     * It fills combo boxes with options, sets the table of appointments and adds a listener for the contact combo box.
     *
     * @param url initialize url
     * @param resourceBundle initialize resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            // Appointments by type and month section
            MonthsCombo.setItems(months);

            // Creating a set of all types so there are no duplicates
            Set<String> type = new HashSet<>();
            for (Appointment i : AppointmentsQuery.selectApps()) {
                type.add(i.getType());
            }

            // Filling combo box with all types
            ObservableList<String> types = FXCollections.observableArrayList(type);
            TypeCombo.setItems(types);

            // Contact schedule section
            ContactCombo.setItems(ContactsQuery.selectContacts());

            // Setting up appointment table
            AppointmentsTable.setItems(AppointmentsQuery.selectApps());
            AppointmentId.setCellValueFactory(new PropertyValueFactory<>("appId"));
            Title.setCellValueFactory(new PropertyValueFactory<>("title"));
            Description.setCellValueFactory(new PropertyValueFactory<>("description"));
            Type.setCellValueFactory(new PropertyValueFactory<>("type"));
            StartDateTime.setCellValueFactory(new PropertyValueFactory<>("start"));
            EndDateTime.setCellValueFactory(new PropertyValueFactory<>("end"));
            CustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            // Listener to see if the contact combo box was changed. If is was, the appointments table shows all appointments
            // for that contact
            ContactCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {
                @Override
                public void changed(ObservableValue<? extends Contact> observableValue, Contact contact, Contact t1) {
                    try {
                        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
                        for (Appointment i : AppointmentsQuery.selectApps()) {
                            if (i.getContactId() == ContactCombo.getValue().getId()) {
                                contactAppointments.add(i);
                            }
                        }

                        AppointmentsTable.setItems(contactAppointments);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Customers of Month section
            // Using getCustomersOfMonth function to find top three customers
            Customer[] topThreeCustomers = getCustomersOfMonth();

            // If there are top customers, it shows them displayed on top customers report
            // If there are not enough customers or not enough appointments, it displays "None"
            if (topThreeCustomers[0] != null) {
                FirstPlaceCustomer.setText(String.valueOf(topThreeCustomers[0].getId()));
            } else {
                FirstPlaceCustomer.setText("None");
            }
            if (topThreeCustomers[1] != null) {
                SecondPlaceCustomer.setText(String.valueOf(topThreeCustomers[1].getId()));
            } else {
                SecondPlaceCustomer.setText("None");
            }
            if (topThreeCustomers[2] != null) {
                ThirdPlaceCustomer.setText(String.valueOf(topThreeCustomers[2].getId()));
            } else {
                ThirdPlaceCustomer.setText("None");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        };
    }

    /**
     * Cancel button brings the user back to the main navigational page.
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
     * The search button allows the user to look for the number of appointments with the given type and month selected.
     * This takes in the input from the combo boxes for type and month, and displays the number of appointments.
     *
     * @param actionEvent clicking the search button
     * @throws SQLException exception in case sql commands return an error
     */
    public void Search(ActionEvent actionEvent) throws SQLException {
        int selectedMonthInt = months.indexOf(MonthsCombo.getValue()) + 1; // Making the selected month a number from 1 to 12
        String selectedType = TypeCombo.getValue();
        int count = 0;

        for (Appointment i : AppointmentsQuery.selectApps()) {
            if ((selectedMonthInt == i.getStart().getMonth().getValue()) && (i.getType().equalsIgnoreCase(selectedType))) {
                count += 1;
            }
        }

        NumberResult.setText(String.valueOf(count));
    }

    /**
     * The getCustomersOfMonth function finds the highest three customers with most appointment hours scheduled.
     *
     * @return array of customers with the highest amount of time with appointments scheduled
     * @throws SQLException exception in case sql commands return an error
     */
    public Customer[] getCustomersOfMonth() throws SQLException {
        LocalDate dateNow = LocalDate.now();
        LocalDate dateFirst = dateNow.withDayOfMonth(1); // Getting the first day of the month
        LocalDate dateLast = YearMonth.now().atEndOfMonth(); // Getting the last day of the month

        ArrayList<Appointment> monthAppointments = new ArrayList<>();

        // Checking if any appointments are within the current month
        for(Appointment i : AppointmentsQuery.selectApps()) {
            if ((dateFirst.getDayOfMonth() <= i.getStart().getDayOfMonth()) && (dateLast.getDayOfMonth() >= i.getStart().getDayOfMonth()) && (dateFirst.getMonth() == i.getStart().getMonth())) {
                monthAppointments.add(i);
            }
        }

        // Creating a new hashmap and array to contain results
        Map<Customer, Integer> monthTimes = new HashMap<>();
        Customer[] topThreeCustomers = {null, null, null};

        // Filling the monthTimes with all customers and total time scheduled in appointments
        for (Customer i : CustomersQuery.selectCustomers()) {
            int duration = 0;
            for (Appointment j : monthAppointments) {
                if (i.getId() == j.getCustomerId()) {
                    duration += Duration.between(j.getStart(), j.getEnd()).getSeconds()/60; // Minutes taken per appointment
                }
            }

            monthTimes.put(i, duration);
        }

        // If there exist appointments within given month and if there are enough customers, then the maximum appointment
        // duration is found for first, second and third place. Otherwise, it is left as "null".
        if (!monthAppointments.isEmpty()) {
            try {
                if (Collections.max(monthTimes.values()) != 0) {
                    topThreeCustomers[0] = Collections.max(monthTimes.entrySet(), Map.Entry.comparingByValue()).getKey();
                    monthTimes.remove(Collections.max(monthTimes.entrySet(), Map.Entry.comparingByValue()).getKey());
                }

                if (Collections.max(monthTimes.values()) != 0) {
                    topThreeCustomers[1] = Collections.max(monthTimes.entrySet(), Map.Entry.comparingByValue()).getKey();
                    monthTimes.remove(Collections.max(monthTimes.entrySet(), Map.Entry.comparingByValue()).getKey());
                }

                if (Collections.max(monthTimes.values()) != 0) {
                    topThreeCustomers[2] = Collections.max(monthTimes.entrySet(), Map.Entry.comparingByValue()).getKey();
                    monthTimes.remove(Collections.max(monthTimes.entrySet(), Map.Entry.comparingByValue()).getKey());
                }
            } catch (NoSuchElementException e) {
                //
            }
        }

        return topThreeCustomers;
    }
}
