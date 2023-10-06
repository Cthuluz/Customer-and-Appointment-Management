package model;

import helperDB.CountriesQuery;
import helperDB.DivisionsQuery;

import java.sql.SQLException;

/**
 * The Customer class contains all variables, the constructor, all getters and all setters for customers.
 */
public class Customer {
    /**
     * Customer id attribute.
     */
    private int id;
    /**
     * Customer name attribute.
     */
    private String name;
    /**
     * Customer address attribute.
     */
    private String address;
    /**
     * Customer postal code attribute.
     */
    private String postalCode;
    /**
     * Customer phone number attribute.
     */
    private String phone;
    /**
     * Customer associated division id attribute.
     */
    private int divisionId;

    /**
     * Constructor for the Customer class.
     *
     * @param id customer id
     * @param name customer name
     * @param address customer address
     * @param postalCode customer postal code
     * @param phone customer phone number
     * @param divisionId customer associated division id
     */
    public Customer(int id, String name, String address, String postalCode, String phone, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public Country getCountry() throws SQLException {
        int countryId = DivisionsQuery.selectDivision(divisionId).getCountryId();
        return CountriesQuery.selectCountry(countryId);
    }

    public Division getDivision() throws SQLException {
        return DivisionsQuery.selectDivision(divisionId);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
