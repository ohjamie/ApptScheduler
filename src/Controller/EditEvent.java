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
import static Utility.Localization.*;
import static View_Controller.Dashboard.getSelectedAppt;

public class EditEvent implements Initializable {

    @FXML private TextField apptTypeInput;
    @FXML private ChoiceBox<String> customerNameInput;
    @FXML private ChoiceBox<String> startHourInput;
    @FXML private ChoiceBox<String> startMinInput;
    @FXML private ChoiceBox<String> endHourInput;
    @FXML private ChoiceBox<String> endMinInput;
    @FXML private ChoiceBox<String> startAmPmInput;
    @FXML private ChoiceBox<String> endAmPmInput;
    @FXML private DatePicker startDateInput;
    @FXML private DatePicker endDateInput;
    @FXML private Label updatedByText;
    @FXML private Label updatedOnText;
    @FXML private Label createdByText;
    @FXML private Label createdOnText;

    private static int indexOfSelected;
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    void deleteEvent(MouseEvent event) throws IOException {
        Appointment selectedAppt = appointmentList.get(indexOfSelected);
        deleteAppt(selectedAppt);
        System.out.print("Appointment has successfully been deleted from DB.\n");

        Parent root = FXMLLoader.load(getClass().getResource("/View/dashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void updateEvent(MouseEvent event) throws IOException {

        try {
            Appointment selectedAppt = appointmentList.get(indexOfSelected);

            String consultant = Consultant.getConsultant();
            String apptType = apptTypeInput.getText();
            String customer = customerNameInput.getValue();
            String startDate = startDateInput.getValue().toString();
            String startTime = startHourInput.getValue() + ":" + startMinInput.getValue() + " " + startAmPmInput.getValue();
            String endDate = endDateInput.getValue().toString();
            String endTime = endHourInput.getValue() + ":" + endMinInput.getValue() + " " + endAmPmInput.getValue();

            LocalDateTime start = StringToLDT(startDate + " " + startTime);
            LocalDateTime end = StringToLDT(endDate + " " + endTime);

            if (!isWithinBusinessHours(start, end)) {
                System.out.println("Error: Outside of business hours.");

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is correct.");
                alert.setContentText("All appointments must be scheduled between business hours of 9AM to 5PM, " +
                        "\nMonday through Friday.\n\nStart date and time must be before end date and time.");
                alert.showAndWait();
            } else if (selectedApptIsOverlapping(selectedAppt, start, end)) {
                System.out.println("Error: Time is overlapping.");

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is valid.");
                alert.setContentText("Date or time entered overlaps existing appointments.");
                alert.showAndWait();
            } else {
                selectedAppt.setAppointmentID(selectedAppt.getAppointmentID());
                selectedAppt.setConsultantName(consultant);
                selectedAppt.setCustomerName(customer);
                selectedAppt.setApptType(apptType);
                selectedAppt.setStart(start);
                selectedAppt.setEnd(end);
                selectedAppt.setCreatedOn(selectedAppt.getCreatedOn());
                selectedAppt.setLastUpdatedBy(consultant);
                selectedAppt.setLastUpdatedOn(LocalDateTime.now().toString());

                updateAppt(selectedAppt);

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
        indexOfSelected = getSelectedAppt();

        // fill dropdown with all customers and time values
        for(int i=0; i<customerList.size(); i++) {
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

        // get selected appointment
        appointmentList = getAllAppointments();
        Appointment selectedAppt = appointmentList.get(indexOfSelected);

        // retrieve information to fill fields with existing appointment data
        apptTypeInput.setText("" + selectedAppt.getApptType());
        customerNameInput.setValue(selectedAppt.getCustomerName());
        startDateInput.setValue(selectedAppt.getStart().toLocalDate());
        endDateInput.setValue(selectedAppt.getEnd().toLocalDate());
        String startTime = LDTToString(selectedAppt.getStart());
        String endTime = LDTToString(selectedAppt.getEnd());
        startHourInput.setValue(startTime.substring(11,13));
        startMinInput.setValue(startTime.substring(14,16));
        startAmPmInput.setValue(startTime.substring(17,19));
        endHourInput.setValue(endTime.substring(11,13));
        endMinInput.setValue(endTime.substring(14,16));
        endAmPmInput.setValue(endTime.substring(17,19));
        updatedByText.setText("Last updated by: " + selectedAppt.getLastUpdatedBy());
        updatedOnText.setText("Last updated on: " + selectedAppt.getLastUpdatedOn());
        createdOnText.setText("Created on: " + selectedAppt.getCreatedOn());
        createdByText.setText("Created by: " + selectedAppt.getConsultantName());
    }
}
