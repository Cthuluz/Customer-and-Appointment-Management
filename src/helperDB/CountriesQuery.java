package helperDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The countries query class contains all helper functions for accessing the SQL database for countries.
 */
public abstract class CountriesQuery {

    /**
     * The selectCountries function returns all countries in the SQL database.
     *
     * @return an observable list of countries
     * @throws SQLException exception in case sql commands return an error
     */
    public static ObservableList<Country> selectCountries() throws SQLException {
        String sqlCommand = "SELECT * FROM COUNTRIES";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        ResultSet resultSet = preparedCommand.executeQuery();

        ObservableList<Country> currentCountries = FXCollections.observableArrayList();

        while (resultSet.next()) {
            int countryId = resultSet.getInt("Country_ID");
            String country = resultSet.getString("Country");

            Country currentCountry = new Country(countryId, country);
            currentCountries.add(currentCountry);
        }
        return currentCountries;
    }

    /**
     * The selectCountry function returns the country with the inputted id.
     *
     * @param countryId inputted country id
     * @return a country
     * @throws SQLException exception in case sql commands return an error
     */
    public static Country selectCountry(int countryId) throws SQLException {

        String sqlCommand = "SELECT * FROM COUNTRIES WHERE Country_ID = ?";
        PreparedStatement preparedCommand = JDBC.connection.prepareStatement(sqlCommand);
        preparedCommand.setInt(1, countryId);
        ResultSet resultSet = preparedCommand.executeQuery();
        while (resultSet.next()) {
            String country = resultSet.getString("Country");
            Country currentCountry = new Country(countryId, country);
            return currentCountry;
        }
        return null;
    }
}
