package control;

import helperDB.*;
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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Customer controller allows user to interact with a customer interface.
 * This includes viewing all customers in a table view and
 * adding customers or updating and deleting selected customers
 */
public class CustomerController implements Initializable {

    /**
     * Add, update or delete appointment button.
     */
    public Button Execute = new Button();
    /**
     * Customer table.
     */
    public TableView<Customer> CustomerTable;
    /**
     * Customer id column.
     */
    public TableColumn<Customer, Integer> CustomerID;
    /**
     * Customer name column.
     */
    public TableColumn<Customer, String> CustomerName;
    /**
     * Customer address column.
     */
    public TableColumn<Customer, String> Address;
    /**
     * Customer postal code column.
     */
    public TableColumn<Customer, String> PostalCode;
    /**
     * Customer phone number column.
     */
    public TableColumn<Customer, String> Phone;
    /**
     * Customer associated division id column.
     */
    public TableColumn<Customer, Integer> DivisionID;
    /**
     * Customer associated division name column.
     */
    public TableColumn<Customer, String> Division;
    /**
     * Customer associated country column.
     */
    public TableColumn<Customer, String> Country;
    /**
     * Customer id input field.
     */
    public TextField CustomerIdText;
    /**
     * Customer name input field.
     */
    public TextField CustomerNameText;
    /**
     * Customer address input field.
     */
    public TextField CustomerAddressText;
    /**
     * Customer postal code input field.
     */
    public TextField CustomerPostalText;
    /**
     * Customer phone number input field.
     */
    public TextField CustomerPhoneText;
    /**
     * Customer associated country input combo box.
     */
    public ComboBox<model.Country> CustomerCountryCombo;
    /**
     * Customer associated division input combo box.
     */
    public ComboBox<model.Division> CustomerDivisionCombo;

    /**
     * The initialize sets up the customer form with relevant data.
     * It sets and fills out the customer table and a combo boxes.
     * It incorporates a listener for the user to change the country combo box,
     * which sets the division combo box based on that country.
     * It incorporates a listener for the user to select different customers in table,
     * which fills out all field boxes.
     *
     * @param url initialize url
     * @param resourceBundle initialize resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Setting all data within the customers table
            CustomerTable.setItems(CustomersQuery.selectCustomers());
            CustomerID.setCellValueFactory(new PropertyValueFactory<>("id"));
            CustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            Address.setCellValueFactory(new PropertyValueFactory<>("address"));
            PostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            DivisionID.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
            Division.setCellValueFactory(new PropertyValueFactory<>("division"));
            Country.setCellValueFactory(new PropertyValueFactory<>("country"));

            // Setting country combo box
            ObservableList<Country> usersList = CountriesQuery.selectCountries();
            CustomerCountryCombo.setItems(usersList);

            // Listener for user to change the country combo box, setting the division combo box based off the country
            CustomerCountryCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Country>() {

                @Override
                public void changed(ObservableValue<? extends model.Country> observableValue, model.Country country, model.Country t1) {
                    try {
                        CustomerDivisionCombo.valueProperty().set(null);
                        ObservableList<Division> currentDivisions = FXCollections.observableArrayList();
                        int countryId = CustomerCountryCombo.getValue().getCountryId();

                        for (Division i : DivisionsQuery.selectDivisions()) {
                            if (countryId == i.getCountryId()) {
                                currentDivisions.add(i);
                            }
                        }

                        CustomerDivisionCombo.setItems(currentDivisions);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (NullPointerException e) {
                        //
                    }
                }
            });

            // Listener for user clicking different customers within the customer table that fills out the
            // text fields for the update and delete tabs
            CustomerTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {

                @Override
                public void changed(ObservableValue<? extends Customer> observableValue, Customer customer, Customer t1) {
                    try {
                        if ((Execute.getText().equals("Delete")) || (Execute.getText().equals("Update"))) {
                            fillCustomers();
                        } else {
                            clearCustomers();
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
     * Execute allows the user to add, update or delete a customer.
     * The execute button changes its name depending on which tab is selected ("Add", "Update, or "Delete").
     * When the "Add" button is pressed, it adds a customer to the database and updates the table.
     * When the "Update" button is pressed, it updates the currently selected customer and updates the table.
     * When the "Delete" button is pressed, it deletes the currently selected customer and displays a message with
     * the customer id and name.
     *
     * @param actionEvent clicking the execute button (Add, Update or Delete)
     * @throws SQLException exception in case sql commands return an error
     */
    public void execute(ActionEvent actionEvent) throws SQLException {
        // Add customer button functionality
        if(Execute.getText().equals("Add")) {
            // Gathering all filled out fields within the form
            String name = CustomerNameText.getText();
            String address = CustomerAddressText.getText();
            String postalCode = CustomerPostalText.getText();
            String phone = CustomerPhoneText.getText();
            int divisionId = CustomerDivisionCombo.getValue().getDivisionId();

            // Adding customer to database
            CustomersQuery.insertCustomer(name, address, postalCode, phone, divisionId);

            // Clear all selections
            clearCustomers();

            // Refresh table
            CustomerTable.setItems(CustomersQuery.selectCustomers());

        // Delete customer functionality
        } else if (Execute.getText().equals("Delete")) {
            // Making sure a customer is selected
            if (CustomerIdText.getText() != "Auto-Generated") {
                // Gathering customer id and name
                int customerId = CustomerTable.getSelectionModel().getSelectedItem().getId();
                String customerName = CustomerTable.getSelectionModel().getSelectedItem().getName();

                // Deleting all associated appointments for the customer
                for (Appointment i : AppointmentsQuery.selectApps()) {
                    if (i.getCustomerId() == customerId) {
                        AppointmentsQuery.deleteApp(i.getAppId());
                    }
                }

                // Deleting customer from database
                CustomersQuery.deleteCustomer(customerId);

                // Refresh table
                CustomerTable.setItems(CustomersQuery.selectCustomers());

                // Alert box showing which customer was deleted
                Alert showInformation = new Alert(Alert.AlertType.INFORMATION);
                showInformation.setTitle("Customer Information");
                showInformation.setContentText("Customer Deleted: \n\nID: " + customerId + "\nName: " + customerName);
                showInformation.showAndWait();

                // Clearing all text boxes
                clearCustomers();
            }
        // Update customer functionality
        } else {
            // Making sure a customer is selected
            if (CustomerIdText.getText() != "Auto-Generated") {
                // Gathering filled out appointment fields
                int customerId = Integer.parseInt(CustomerIdText.getText());
                String name = CustomerNameText.getText();
                String address = CustomerAddressText.getText();
                String postalCode = CustomerPostalText.getText();
                String phone = CustomerPhoneText.getText();
                int divisionId = CustomerDivisionCombo.getValue().getDivisionId();

                // Updating customer in database
                CustomersQuery.updateCustomer(customerId, name, address, postalCode, phone, divisionId);

                // Refresh table
                CustomerTable.setItems(CustomersQuery.selectCustomers());
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
     * addTabClicked function changes the interface for adding customers.
     * It changes the label of the execute button to say "Add",
     * enables all fields except for the customer id,
     * and clears all fields so the user can easily input a customer to add.
     *
     * @param event selecting the "Add" tab
     */
    public void addTabClicked(Event event) {
        // Changing label of button to Add
        Execute.setText("Add");

        // Enabling all text fields except customer ID
        disabledOrEnabled(false);

        // Clearing all text fields
        try {
            clearCustomers();
        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * updateTabClicked function changes the interface for updating customers.
     * It changes the label of the execute button to say "Update",
     * enables all fields except for the customer id,
     * anc fills all fields with the selected customer information.
     *
     * @param event selecting the "Update" tab
     * @throws SQLException exception in case sql commands return an error
     */
    public void updateTabClicked(Event event) throws SQLException {
        // Changing label of button to Update
        Execute.setText("Update");

        // Enabling all text fields except customer ID
        disabledOrEnabled(false);

        // Filling all text fields with the selected customer information
        fillCustomers();
    }

    /**
     * deleteTabClicked function changes the interface for deleting customers.
     * It changes the label of the execute button to say "Delete",
     * disables all fields, and fills all fields with the selected customer information.
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
        fillCustomers();
    }

    /**
     * The fillCustomers function fills all input fields including text boxes,
     * combo boxes and date pickers for the selected customer.
     *
     * @throws SQLException exception in case sql commands return an error
     */
    public void fillCustomers() throws SQLException {
        try {
            CustomerIdText.setText(String.valueOf(CustomerTable.getSelectionModel().getSelectedItem().getId()));
            CustomerNameText.setText(String.valueOf(CustomerTable.getSelectionModel().getSelectedItem().getName()));
            CustomerAddressText.setText(String.valueOf(CustomerTable.getSelectionModel().getSelectedItem().getAddress()));
            CustomerPostalText.setText(String.valueOf(CustomerTable.getSelectionModel().getSelectedItem().getPostalCode()));
            CustomerPhoneText.setText(String.valueOf(CustomerTable.getSelectionModel().getSelectedItem().getPhone()));
            model.Division division = DivisionsQuery.selectDivision(CustomerTable.getSelectionModel().getSelectedItem().getDivisionId());
            CustomerCountryCombo.setValue(CountriesQuery.selectCountry(division.getCountryId()));
            CustomerDivisionCombo.setValue(division);

        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * The clearCustomers function clears all input fields including text boxes,
     * combo boxes and date pickers for the customer.
     */
    public void clearCustomers() {
        try {
            CustomerIdText.clear();
            CustomerNameText.clear();
            CustomerAddressText.clear();
            CustomerPostalText.clear();
            CustomerPhoneText.clear();
            CustomerDivisionCombo.valueProperty().set(null);
            CustomerCountryCombo.valueProperty().set(null);
        } catch (NullPointerException e) {
            //
        }
    }

    /**
     * The disableOrEnable function sets all input fields (except the customer id)
     * as enabled if the input is false, and disabled if the input is true.
     *
     * @param value the input of true or false
     */
    public void disabledOrEnabled(Boolean value) {
        try {
            CustomerNameText.setDisable(value);
            CustomerAddressText.setDisable(value);
            CustomerPhoneText.setDisable(value);
            CustomerPostalText.setDisable(value);
            CustomerDivisionCombo.setDisable(value);
            CustomerCountryCombo.setDisable(value);
        } catch (NullPointerException e) {
            //
        }
    }
}
