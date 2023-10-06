package model;

/**
 * The Country class contains all variables, the constructor, all getters and all setters for Countries.
 */
public class Country {
    /**
     * Country id attribute.
     */
    private int countryId;
    /**
     * Country name attribute.
     */
    private String country;

    /**
     * Constructor for the Country class.
     *
     * @param countryId country id
     * @param country country name
     */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return country;
    }
}
