package model;

import helperDB.ContactsQuery;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The Appointment class contains all variables, the constructor, all getters and all setters for appointments.
 */
public class Appointment {
    /**
     * Appointment id attribute.
     */
    private int appId;
    /**
     * Appointment title attribute.
     */
    private String title;
    /**
     * Appointment description attribute.
     */
    private String description;
    /**
     * Appointment location attribute.
     */
    private String location;
    /**
     * Appointment type attribute.
     */
    private String type;
    /**
     * Appointment start date and time attribute.
     */
    private LocalDateTime start;
    /**
     * Appointment end date and time attribute.
     */
    private LocalDateTime end;
    /**
     * Appointment associated customer id attribute.
     */
    private int customerId;
    /**
     * Appointment associated user id attribute.
     */
    private int userId;
    /**
     * Appointment associated contact id attribute.
     */
    private int contactId;

    /**
     * Constructor for the Appointment class.
     *
     * @param appId appointment id
     * @param title appointment title
     * @param description appointment description
     * @param location appointment location
     * @param type appointment type
     * @param start appointment start time
     * @param end appointment end time
     * @param customerId appointment associated customer id
     * @param userId appointment associated user id
     * @param contactId appointment associated contact id
     */
    public Appointment(int appId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        this.appId = appId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public Contact getContact() throws SQLException {
        return ContactsQuery.selectContact(contactId);
    }
}
