import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Utility.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Utility.DBConnection.*;
import static Utility.Localization.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("language_files/rb", locale);

        Parent root = FXMLLoader.load(getClass().getResource("/View/login.fxml"), rb);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        try {
            Connection con = DBConnection.startConnection();

            ResultSet rs = con.createStatement().executeQuery("SELECT appointment.*, customer.customerName FROM appointment LEFT JOIN customer ON appointment.customerId=customer.customerId");
            ResultSet rs1 = con.createStatement().executeQuery("SELECT customer.*, address.phone, address.address, city.city, address.postalCode FROM customer LEFT JOIN address ON customer.addressId=address.addressId LEFT JOIN city ON address.cityId=city.cityId");
            ResultSet rs2 = con.createStatement().executeQuery("select * from user");
            ResultSet rs3 = con.createStatement().executeQuery("select * from city");

            // get appointments from DB
            while (rs.next()) {
                Appointment newAppt = new Appointment();

                LocalDateTime start = LocalizeTimeFromDB(rs.getString("start"));
                LocalDateTime end = LocalizeTimeFromDB(rs.getString("end"));
                LocalDateTime createDate = LocalizeTimeFromDB(rs.getString("createDate"));
                LocalDateTime updated = LocalizeTimeFromDB(rs.getString("lastUpdate"));

                String createdOn = LDTToString(createDate);
                String lastUpdate = LDTToString(updated);

                newAppt.setAppointmentID(rs.getInt("appointmentId"));
                newAppt.setCustomerName(rs.getString("customerName"));
                newAppt.setConsultantName(rs.getString("createdBy"));
                newAppt.setApptType(rs.getString("type"));
                newAppt.setStart(start);
                newAppt.setEnd(end);
                newAppt.setCreatedOn(createdOn);
                newAppt.setLastUpdatedBy(rs.getString("lastUpdateBy"));
                newAppt.setLastUpdatedOn(lastUpdate);

                addApptFromDB(newAppt);
            }
            while(rs1.next()) {
                Customer newCustomer = new Customer();

                LocalDateTime createDate = LocalizeTimeFromDB(rs1.getString("createDate"));
                LocalDateTime updated = LocalizeTimeFromDB(rs1.getString("lastUpdate"));

                String createdOn = LDTToString(createDate);
                String lastUpdate = LDTToString(updated);

                newCustomer.setCustomerId(rs1.getInt("customerId"));
                newCustomer.setAddressId(rs1.getInt("addressId"));
                newCustomer.setCustomerName(rs1.getString("customerName"));
                newCustomer.setPhoneNum(rs1.getString("phone"));
                newCustomer.setAddress(rs1.getString("address"));
                newCustomer.setCity(rs1.getString("city"));
                newCustomer.setPostalCode(rs1.getString("postalCode"));
                newCustomer.setCreatedBy(rs1.getString("createdBy"));
                newCustomer.setCreatedOn(createdOn);
                newCustomer.setLastUpdatedBy(rs1.getString("lastUpdateBy"));
                newCustomer.setLastUpdatedOn(lastUpdate);

                addCustomerFromDB(newCustomer);
            }
            while (rs2.next()) {
                Consultant newConsultant = new Consultant();
                newConsultant.setConsultant(rs2.getString("userName"));
                addConsultantFromDB(newConsultant);
            }
            while (rs3.next()) {
                City newCity = new City();
                newCity.setCityName(rs3.getString("city"));
                addCityFromDB(newCity);
            }
            System.out.println("Appointments, customers, cities and consultants from DB have been retrieved.\n");
        }
        catch (SQLException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
        launch(args);
        DBConnection.closeConnection();
    }
}
