package model;

/**
 * The User class contains all variables, the constructor, all getters and all setters for users.
 */
public class User {
    /**
     * User id attribute.
     */
    private int id;
    /**
     * User name attribute.
     */
    private String name;
    /**
     * User password attribute.
     */
    private String password;

    /**
     * Constructor for the User class.
     *
     * @param id user id
     * @param name user name
     * @param password user password
     */
    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
