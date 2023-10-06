package model;

/**
 * The Contact class contains all variables, the constructor, all getters and all setters for contacts.
 */
public class Contact {
    /**
     * Contact id attribute.
     */
    private int id;
    /**
     * Contact name attribute.
     */
    private String name;
    /**
     * Contact email attribute.
     */
    private String email;

    /**
     * Constructor for the Contact class.
     *
     * @param id contact id
     * @param name contact name
     * @param email contact email
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name;
    }
}
