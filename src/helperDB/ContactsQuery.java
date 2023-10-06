package helperDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Contacts query class contains all helper functions for accessing the SQL database for contacts.
 */
public abstract class ContactsQuery {

    /**
     * selectContacts function returns all contacts within the SQL database.
     *
     * @return observable list of contacts
     * @throws SQLException exception in case sql commands return an error
     */
    public static ObservableList<Contact> selectContacts() throws SQLException {
        String sqlCommand = "SELECT * FROM CONTACTS";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedCommand.executeQuery();

        ObservableList<Contact> currentContacts = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int contactId = resultSet.getInt("Contact_ID");
            String name = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");

            Contact currentContact = new Contact(contactId, name, email);
            currentContacts.add(currentContact);
        }
        return currentContacts;
    }

    /**
     * selectContact function returns the contact with the given id.
     *
     * @param contactId inputted contact id
     * @return a contact
     * @throws SQLException exception in case sql commands return an error
     */
    public static Contact selectContact(int contactId) throws SQLException {

        String sqlCommand = "SELECT * FROM CONTACTS WHERE Contact_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, contactId);
        ResultSet resultSet = preparedCommand.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString("Contact_Name");
            String email = resultSet.getString("Email");
            Contact currentContact = new Contact(contactId, name, email);
            return currentContact;
        }
        return null;
    }
}
