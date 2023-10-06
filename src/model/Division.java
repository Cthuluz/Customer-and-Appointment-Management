package model;

/**
 * The Division class contains all variables, the constructor, all getters and all setters for divisions.
 */
public class Division {
    /**
     * Division id attribute.
     */
    private int divisionId;
    /**
     * Division name attribute.
     */
    private String division;
    /**
     * Division associated country id attribute.
     */
    private int countryId;

    /**
     * Constructor for the Division class.
     *
     * @param divisionId division id
     * @param division division name
     * @param countryId division associated country id
     */
    public Division(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return division;
    }
}
