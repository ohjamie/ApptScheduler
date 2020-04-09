package View_Controller;

import Model.Appointment;
import Model.Consultant;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import static Model.ErrorChecks.*;
import static Utility.DBConnection.*;
import static Utility.Localization.StringToLDT;

public class AddEvent implements Initializable {

    // Inputs
    @FXML private ChoiceBox<String> customerNameInput;
    @FXML private ChoiceBox<String> startHourInput;
    @FXML private ChoiceBox<String> startMinInput;
    @FXML private ChoiceBox<String> endHourInput;
    @FXML private ChoiceBox<String> endMinInput;
    @FXML private ChoiceBox<String> startAmPmInput;
    @FXML private ChoiceBox<String> endAmPmInput;
    @FXML private DatePicker startDateInput;
    @FXML private DatePicker endDateInput;
    @FXML private TextField apptTypeInput;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    void saveEvent(MouseEvent event) throws IOException {

        try {
            String consultant = Consultant.getConsultant();
            String apptType = apptTypeInput.getText();
            String customer = customerNameInput.getValue();
            String startDate = startDateInput.getValue().toString();
            String startTime = startHourInput.getValue() + ":" + startMinInput.getValue() + " " + startAmPmInput.getValue();
            String endDate = endDateInput.getValue().toString();
            String endTime = endHourInput.getValue() + ":" + endMinInput.getValue() + " " + endAmPmInput.getValue();

            LocalDateTime start = StringToLDT(startDate + " " + startTime);
            LocalDateTime end = StringToLDT(endDate + " " + endTime);

            Appointment newAppt = new Appointment();
            newAppt.setAppointmentID(getAllAppointments().size()+1);
            newAppt.setCustomerName(customer);
            newAppt.setConsultantName(consultant);
            newAppt.setApptType(apptType);
            newAppt.setStart(start);
            newAppt.setEnd(end);
            newAppt.setCreatedOn(LocalDateTime.now().toString());
            newAppt.setLastUpdatedBy(consultant);
            newAppt.setLastUpdatedOn(LocalDateTime.now().toString());
            getAllAppointments().add(newAppt);

            if (!isWithinBusinessHours(start, end)) {
                System.out.println("Error: Outside of business hours.");
                getAllAppointments().remove(newAppt);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is correct.");
                alert.setContentText("All appointments must be scheduled between business hours of 9AM to 5PM, " +
                        "\nMonday through Friday.\n\nStart date and time must be before end date and time.");
                alert.showAndWait();
            } else if (newApptIsOverlapping(newAppt, start, end)) {
                System.out.println("Error: Time is overlapping.");
                getAllAppointments().remove(newAppt);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is valid.");
                alert.setContentText("Date or time entered overlaps existing appointments.");
                alert.showAndWait();
            } else {
                addAppt(newAppt);

                Parent root = FXMLLoader.load(getClass().getResource("/View/dashboard.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please verify that no fields are empty and information entered is correct.");
            alert.setContentText("Fill all fields.");
            alert.showAndWait();
        }
    }

    @FXML
    void returnToDash(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/dashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerList = getAllCustomers();
        for (int i = 0; i < customerList.size(); i++) {
            customerNameInput.getItems().add(customerList.get(i).getCustomerName());
        }

        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                startHourInput.getItems().add("0" + i);
                endHourInput.getItems().add("0" + i);
            } else {
                startHourInput.getItems().add(Integer.toString(i));
                endHourInput.getItems().add(Integer.toString(i));
            }
        }
        for (int x = 0; x < 46; x += 15) {
            if (x < 1){
                startMinInput.getItems().add("0" + x);
                endMinInput.getItems().add("0" + x);
            } else {
                startMinInput.getItems().add(Integer.toString(x));
                endMinInput.getItems().add(Integer.toString(x));
            }
        }
        startAmPmInput.getItems().add("AM");
        startAmPmInput.getItems().add("PM");
        endAmPmInput.getItems().add("AM");
        endAmPmInput.getItems().add("PM");
    }
}

