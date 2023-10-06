README

Title: Database Client Application
Purpose of application: Create a GUI based scheduling application for company based in a range of locations that allows logins, and appointment and customer schedule manipulation.
Author: Isabel Nelson
Contact information: 303-619-9409; inels19@wgu.edu
Application version: version number 1
Date: 7/12/2023
IDE version: IntelliJ 2023.1.1 (Community Edition)
JDK version: 17.0.1
JavaFX version: JavaFX-SDK-17.0.1

Directions:
1. Open project in Intellij
2. Run program
3. Login with username and password (Possibilities: test, test; admin, admin)
4. Select one of the four buttons:
    a. Appointments to reach the appointments form page
    b. Customers to reach the customers form page
    c. Reports to reach the reports page
    d Exit to exit the program
5. In the appointments form page:
    a. Fill out all fields and then select "Add" to add an appointment
    b. Select an appointment from the table to update, change relevant field, and then press "Update" to update an appointment
    c. Select an appointment from the table and press "Delete" to delete an appointment
6. In the customers form page:
    a. Fill out all fields and then select "Add" to add a customer
    b. Select a customer from the table to update, change relevant field, and then press "Update" to update a customer
    c. Select a customer from the table and press "Delete" to delete a customer
7. In the reports page:
    a. Select the type and month combo boxes to show all appointments of that type
    b. Select a customer from the customer combo box to show all appointments for that customer in the table

Additional report information: The third report is called "Customers of the Month", and shows first, second and third place of customers
with the most hours of appointments scheduled. If there are not enough customers with appointments in the current month, the first, second
or third place may be left with "None" displayed.

MySQL connector driver version: mysql-connector-java-8.0.25