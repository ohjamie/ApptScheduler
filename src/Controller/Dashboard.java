package View_Controller;

import Model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.*;
import static Model.ErrorChecks.*;
import static Utility.DBConnection.getAllAppointments;

public class Dashboard implements Initializable {

    @FXML private Label consultant;
    @FXML private Text appointmentLabel;
    @FXML private Text apptType1;
    @FXML private Text apptType2;
    @FXML private Text otherApptType;
    @FXML private RadioButton allView;
    @FXML private RadioButton monthView;
    @FXML private RadioButton weekView;
    @FXML private TableView<Appointment> calendarTable;
    @FXML private TableColumn<Appointment, LocalDateTime> startCol;
    @FXML private TableColumn<Appointment, LocalDateTime> endCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> customerCol;
    @FXML private TableColumn<Appointment, String> consultantCol;
    public static int indexOfSelected;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public static int getSelectedAppt() {
        return indexOfSelected;
    }

    @FXML
    void addEvent(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/addEvent.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void toEditAppt(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Appointment selectedAppt = calendarTable.getSelectionModel().getSelectedItem();
            indexOfSelected = getAllAppointments().indexOf(selectedAppt);

            Parent root = FXMLLoader.load(getClass().getResource("/View/editEvent.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    @FXML
    void toEditApptButton(MouseEvent event) throws IOException {
        Appointment selectedAppt = calendarTable.getSelectionModel().getSelectedItem();
        indexOfSelected = getAllAppointments().indexOf(selectedAppt);

        Parent root = FXMLLoader.load(getClass().getResource("/View/editEvent.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void switchToAllView() {
        if (allView.isSelected()) {
            monthView.setSelected(false);
            weekView.setSelected(false);
            calendarTable.setItems(appointments);
        }
    }

    @FXML
    void switchToMonthView() {
        if (monthView.isSelected()) {
            allView.setSelected(false);
            weekView.setSelected(false);

            LocalDateTime startDateRange = LocalDateTime.now();
            LocalDateTime endDateRange = startDateRange.plusMonths(1);

            ObservableList<Appointment> monthList = FXCollections.observableArrayList();
            for(Appointment appt : appointments) {
                LocalDateTime apptDate = appt.getStart();
                if (isWithinRange(startDateRange, apptDate, endDateRange)) {
                    monthList.add(appt);
                }
            }
            calendarTable.setItems(monthList);
        }
    }

    @FXML
    void switchToWeekView() {
        if (weekView.isSelected()) {
            allView.setSelected(false);
            monthView.setSelected(false);

            LocalDateTime startDateRange = LocalDateTime.now();
            LocalDateTime endDateRange = startDateRange.plusWeeks(1);

            ObservableList<Appointment> weekList = FXCollections.observableArrayList();
            for(Appointment appt : appointments) {
                LocalDateTime apptDate = appt.getStart();
                if (isWithinRange(startDateRange, apptDate, endDateRange)) {
                    weekList.add(appt);
                }
            }
            calendarTable.setItems(weekList);
        }
    }

    @FXML
    void toConsultants(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/View/consultants.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void toCustomers(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/customers.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultant.setText(Consultant.getConsultant());
        appointments = getAllAppointments();
        allView.setSelected(true);

        int[] nums = {1,2};
        System.out.println(nums);
        System.out.println(nums.length);

        /** Justification for LAMBDA EXPRESSION #1: Binding columns to a value
         * Multiple lambda expressions are make the block more readable,
         * and will efficiently set the values according to All/Month/Week filters. */

        startCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getStart()));
        endCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getEnd()));
        customerCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCustomerName()));
        consultantCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getConsultantName()));
        typeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getApptType()));
        calendarTable.setItems(appointments);

        // Display number of appointment types in side bar
        int presentations = 0;
        int scrums = 0;
        int other = 0;
        int apptNum = 0;
        for(Appointment appt : appointments) {
            ++apptNum;
            if(appt.getApptType().contains("Presentation")) {
                ++presentations;
            }
            else if(appt.getApptType().contains("Scrum")) {
                ++scrums;
            }
            else {
                ++other;
            }
        }
        appointmentLabel.setText(apptNum + " total appointments");
        apptType1.setText(presentations + " Presentations");
        apptType2.setText(scrums + " Scrum sessions");
        otherApptType.setText(other + " Other appointments");

        if(apptInFifteenMinutes()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("You have an appointment scheduled within the next 15 minutes.");
            alert.showAndWait();
        }
    }
}