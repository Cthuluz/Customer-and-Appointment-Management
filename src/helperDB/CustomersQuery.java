package helperDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The customer query class contains all helper functions for accessing the SQL database for customers.
 */
public abstract class CustomersQuery {

    /**
     * The insertCustomer function adds a customer to the customer table within the SQL database.
     *
     * @param name name of customer to be inserted
     * @param address address of customer to be inserted
     * @param postalCode postal code of customer to be inserted
     * @param phone phone number of customer to be inserted
     * @param divisionId division id associated with the customer to be inserted
     * @return an int for the number of changes made
     * @throws SQLException exception in case sql commands return an error
     */
    public static int insertCustomer(String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sqlCommand = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setString(1, name);
        preparedCommand.setString(2, address);
        preparedCommand.setString(3, postalCode);
        preparedCommand.setString(4, phone);
        preparedCommand.setInt(5, divisionId);

        int numChanged = preparedCommand.executeUpdate();
        return numChanged;
    }

    /**
     * The updateCustomer function updates the given customer within the customer table in the SQL database.
     *
     * @param customerId id of customer to be updated
     * @param name name of customer to be updated
     * @param address address of customer to be updated
     * @param postalCode postal code of customer to be updated
     * @param phone phone of customer to be updated
     * @param divisionId division associated with the customer to be updated
     * @return an int of how many changes were made
     * @throws SQLException exception in case sql commands return an error
     */
    public static int updateCustomer(int customerId, String name, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sqlCommand = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setString(1, name);
        preparedCommand.setString(2, address);
        preparedCommand.setString(3, postalCode);
        preparedCommand.setString(4, phone);
        preparedCommand.setInt(5, divisionId);
        preparedCommand.setInt(6, customerId);

        int numChanged = preparedCommand.executeUpdate();
        return numChanged;
    }

    /**
     * The deleteCustomer function deletes the customer with the inputted id.
     *
     * @param customerId inputted customer id
     * @return an int of how many changes were made
     * @throws SQLException exception in case sql commands return an error
     */
    public static int deleteCustomer(int customerId) throws SQLException {
        String sqlCommand = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, customerId);

        int numChanged = preparedCommand.executeUpdate();
        return numChanged;
    }

    /**
     * The selectedCustomers function returns all customers within the customer table in the SQL database.
     *
     * @return observable list of customers
     * @throws SQLException exception in case sql commands return an error
     */
    public static ObservableList<Customer> selectCustomers() throws SQLException {
        String sqlCommand = "SELECT * FROM CUSTOMERS";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedCommand.executeQuery();

        ObservableList<Customer> currentCustomers = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int customerId = resultSet.getInt("Customer_ID");
            String name = resultSet.getString("Customer_Name");
            String postalCode = resultSet.getString("Postal_Code");
            String address = resultSet.getString("Address");
            String phone = resultSet.getString("Phone");
            int divisionId = resultSet.getInt("Division_ID");

            Customer currentCustomer = new Customer(customerId, name, address, postalCode, phone, divisionId);
            currentCustomers.add(currentCustomer);
        }
        return currentCustomers;
    }

    /**
     * The selectCustomer function returns the customer with the inputted id.
     *
     * @param customerId inputted customer id
     * @return a customer
     * @throws SQLException exception in case sql commands return an error
     */
    public static Customer selectCustomer(int customerId) throws SQLException {
        String sqlCommand = "SELECT * FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, customerId);
        ResultSet resultSet = preparedCommand.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("Customer_Name");
            String address = resultSet.getString("Address");
            String postalCode = resultSet.getString("Postal_Code");
            String phone = resultSet.getString("Phone");
            int divisionId = resultSet.getInt("Division_ID");

            Customer chosenCustomer = new Customer(customerId, name, address, postalCode, phone, divisionId);
            return chosenCustomer;
        }
        return null;
    }
}
