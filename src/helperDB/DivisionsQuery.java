package helperDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The division query class contains all helper functions for accessing the SQL database for divisions.
 */
public abstract class DivisionsQuery {

    /**
     * The selectDivisions function returns all divisions in the division table within the SQL database.
     *
     * @return observable list of divisions
     * @throws SQLException exception in case sql commands return an error
     */
    public static ObservableList<Division> selectDivisions() throws SQLException {
        String sqlCommand = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedCommand.executeQuery();

        ObservableList<Division> currentDivisions = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String country = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            Division currentDivision = new Division(divisionId, country, countryId);
            currentDivisions.add(currentDivision);
        }
        return currentDivisions;
    }

    /**
     * The selectDivision returns the division with the inputted id.
     *
     * @param divisionId inputted division id
     * @return a division
     * @throws SQLException exception in case sql commands return an error
     */
    public static Division selectDivision(int divisionId) throws SQLException {

        String sqlCommand = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE Division_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, divisionId);
        ResultSet resultSet = preparedCommand.executeQuery();
        while (resultSet.next()) {
            String division = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");
            Division currentDivision = new Division(divisionId, division, countryId);
            return currentDivision;
        }
        return null;
    }
}
