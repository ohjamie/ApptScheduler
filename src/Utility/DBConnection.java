package Utility;

import java.sql.*;
import java.time.LocalDateTime;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import static Utility.Localization.FormatTimeForDB;
import static Utility.Localization.localTimeToUTC;

/** This section connects to a mySQL database
 * YOU NEED THE FOLLOWIN INFO TO MAKE A CONNECTION
 * Server name/IP Address
 * Database name
 * Username & Password
 */

public class DBConnection {

    // jdbc url parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05eUD";

    // jdbc url
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // driver interface reference
    private static final String mySQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U05eUD";
    private static final String password = "53688482960";

    public static Connection startConnection() {
        try {
            Class.forName(mySQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection to DB established");
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(SQLException e) {
            System.out.println("Error with startConnection: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection terminated");
        } catch(SQLException e) {
            System.out.println("Error: with closeConnection" + e.getMessage());
        }
    }

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Consultant> allConsultants = FXCollections.observableArrayList();
    private static ObservableList<City> allCities = FXCollections.observableArrayList();

    // add data from DB
    public static void addCustomerFromDB(Customer newCustomer) { allCustomers.add(newCustomer); }
    public static void addApptFromDB(Appointment newAppt) { allAppointments.add(newAppt); }
    public static void addConsultantFromDB(Consultant consultant) { allConsultants.add(consultant); }
    public static void addCityFromDB(City newCity) { allCities.add(newCity); }

    // alter data in DB based on GUI input
    public static void addCustomer(Customer newCustomer) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?, (SELECT cityId FROM city WHERE city = ?),?,?,?,?,?,?)");

            st.setString(1, newCustomer.getAddress());
            st.setString(2, " ");
            st.setString(3, newCustomer.getCity());
            st.setString(4, newCustomer.getPostalCode());
            st.setString(5, newCustomer.getPhoneNum());
            st.setString(6, newCustomer.getCreatedOn());
            st.setString(7, newCustomer.getCreatedBy());
            st.setString(8, newCustomer.getLastUpdatedOn()); // last update on
            st.setString(9, Consultant.getConsultant()); // last update by

            st.executeUpdate();
            System.out.println("Customer address successfully added to DB");

            PreparedStatement st1 = conn.prepareStatement("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,(SELECT addressId FROM address WHERE address = ?),?,?,?,?,?)");

            st1.setString(1, newCustomer.getCustomerName());
            st1.setString(2, newCustomer.getAddress());
            st1.setString(3, "1");
            st1.setString(4, newCustomer.getCreatedOn());
            st1.setString(5, newCustomer.getCreatedBy());
            st1.setString(6, newCustomer.getLastUpdatedOn());
            st1.setString(7, Consultant.getConsultant());

            st1.executeUpdate();
            System.out.println("Rest of customer data successfully added to DB");
        }
        catch (SQLException e) {
            System.out.println("Error 1 adding customer to DB: " + e.getMessage());
        }
    }

    public static void addAppt(Appointment newAppt) {
        try {
            PreparedStatement st = conn.prepareStatement ("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ((SELECT customerId FROM customer WHERE customerName = ?), (SELECT userId FROM user WHERE userName = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            st.setString(1, newAppt.getCustomerName());
            st.setString(2, Consultant.getConsultant());
            st.setString(3, "not needed");
            st.setString(4, "not needed");
            st.setString(5, "not needed");
            st.setString(6, "not needed");
            st.setString(7, newAppt.getApptType());
            st.setString(8, "not needed");
            st.setString(9, localTimeToUTC(newAppt.getStart()).toString());
            st.setString(10, localTimeToUTC(newAppt.getEnd()).toString());
            st.setString(11, LocalDateTime.now().toString().substring(0,16));
            st.setString(12, Consultant.getConsultant());
            st.setString(13, LocalDateTime.now().toString().substring(0,16));
            st.setString(14, Consultant.getConsultant());

            st.executeUpdate();
            System.out.println("Retrieved from DB as: " + localTimeToUTC(newAppt.getStart()));
            System.out.println("Appointment added to DB\n");
        }
        catch (SQLException e) {
            System.out.println("Error adding appointment to DB: " + e.getMessage());
        }
    }

    public static void updateCustomer(Customer selectedCustomer) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE address SET address=(?), address2=(?), cityId=(SELECT cityId FROM city WHERE city = ?), postalCode=(?), phone=(?), createDate=(?), createdBy=(?), lastUpdate=(?), lastUpdateBy=(?) WHERE addressId=(SELECT addressId FROM customer WHERE customerId = ?)");

            st.setString(1, selectedCustomer.getAddress());
            st.setString(2, " ");
            st.setString(3, selectedCustomer.getCity());
            st.setString(4, selectedCustomer.getPostalCode());
            st.setString(5, selectedCustomer.getPhoneNum());
            st.setString(6, LocalDateTime.now().toString());
            st.setString(7, Consultant.getConsultant());
            st.setString(8, LocalDateTime.now().toString());
            st.setString(9, Consultant.getConsultant());
            st.setInt(10, selectedCustomer.getCustomerId());

            st.executeUpdate();
            System.out.println("Customer address successfully updated in DB");

            PreparedStatement st1 = conn.prepareStatement("UPDATE customer SET customerName=(?), addressId=(SELECT addressId FROM address WHERE address = ?), active=(?), createDate=(?), createdBy=(?), lastUpdate=(?), lastUpdateBy=(?) WHERE customerId=(?)");

            st1.setString(1, selectedCustomer.getCustomerName());
            st1.setString(2, selectedCustomer.getAddress());
            st1.setString(3, "1");
            st1.setString(4, LocalDateTime.now().toString());
            st1.setString(5, Consultant.getConsultant());
            st1.setString(6, LocalDateTime.now().toString());
            st1.setString(7, Consultant.getConsultant());
            st1.setInt(8, selectedCustomer.getCustomerId());

            st1.executeUpdate();
            System.out.println("Rest of customer data successfully updated in DB");
        }
        catch (SQLException e) {
            System.out.println("Error updating customer data in DB: " + e.getMessage());
        }
    }

    public static void updateAppt(Appointment selectedAppt) {
        try {
            PreparedStatement st = conn.prepareStatement ("UPDATE appointment SET customerId=(SELECT customerId FROM customer WHERE customerName = ?), userId=(SELECT userId FROM user WHERE userName = ?), title=(?), description=(?), location=(?), contact=(?), type=(?), url=(?), start=(?), end=(?), createDate=(?), createdBy=(?), lastUpdate=(?), lastUpdateBy=(?) WHERE appointmentId=(?)");

            // String start = FormatTimeForDB(selectedAppt.getStart());
            // String end = FormatTimeForDB(selectedAppt.getEnd());

            st.setString(1, selectedAppt.getCustomerName());
            st.setString(2, Consultant.getConsultant());
            st.setString(3, "not needed");
            st.setString(4, "not needed");
            st.setString(5, "not needed");
            st.setString(6, "not needed");
            st.setString(7, selectedAppt.getApptType());
            st.setString(8, "not needed");
            st.setString(9,  localTimeToUTC(selectedAppt.getStart()).toString());
            st.setString(10, localTimeToUTC(selectedAppt.getEnd()).toString());
            st.setString(11, LocalDateTime.now().toString().substring(0,16));
            st.setString(12, Consultant.getConsultant());
            st.setString(13, LocalDateTime.now().toString().substring(0,16));
            st.setString(14, Consultant.getConsultant());
            st.setInt(15, selectedAppt.getAppointmentID());

            st.executeUpdate();
            System.out.println("Stored in DB as: " + localTimeToUTC(selectedAppt.getStart()));
            System.out.println("Appointment successfully updated in DB");
            System.out.println();
        }
        catch (SQLException e) {
            System.out.println("Error updating appointment in DB: " + e.getMessage());
        }
    }
    public static void deleteCustomer(Customer selectedCustomer) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM customer WHERE customerName = (?)");
            st.setString(1, selectedCustomer.getCustomerName());
            st.executeUpdate();
            allCustomers.remove(selectedCustomer);
            System.out.println("Customer deleted.");
        }
        catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Customer cannot be deleted.");
            alert.setContentText("Customer has upcoming appointments. Appointments must either be completed or deleted before deleting customer.");
            alert.showAndWait();
        }
    }

    public static void deleteAppt(Appointment selectedAppt) {
        allAppointments.remove(selectedAppt);
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM appointment WHERE appointmentId = (?)");
            st.setInt(1, selectedAppt.getAppointmentID());
            st.executeUpdate();
            System.out.println("Appointment deleted");
        }
        catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    public static ObservableList<Customer> getAllCustomers() { return allCustomers; }
    public static ObservableList<Appointment> getAllAppointments() { return allAppointments; }
    public static ObservableList<Consultant> getAllConsultants() { return allConsultants; }
    public static ObservableList<City> getAllCities() { return allCities; }
}
