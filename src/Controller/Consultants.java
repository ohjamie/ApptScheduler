package View_Controller;

import Model.Appointment;
import Model.Consultant;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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

public class Consultants implements Initializable {

    @FXML private TableView<Consultant> consultantsTable;
    @FXML private TableColumn<Consultant, String> consultantName;
    @FXML private TableView<Appointment> calendarTable;
    @FXML private TableColumn<Appointment, LocalDateTime> dateCol;
    @FXML private TableColumn<Appointment, String> apptCol;
    @FXML private TableColumn<Appointment, String> customerCol;
    @FXML private RadioButton monthView;
    @FXML private RadioButton weekView;
    @FXML private RadioButton allView;
    public static int indexOfSelected;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    void returnToDash(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/dashboard.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void toAppt(MouseEvent event) throws IOException {
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
    void switchToAllView(MouseEvent event) {
        if (allView.isSelected()) {
            monthView.setSelected(false);
            weekView.setSelected(false);
            calendarTable.setItems(appointments);
        }
    }

    @FXML
    void switchToMonthView(MouseEvent event) {
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
    void switchToWeekView(MouseEvent event) {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        allView.setSelected(true);
        appointments = getAllAppointments();
        ObservableList<Consultant> consultants = getAllConsultants();
        calendarTable.setItems(appointments);
        consultantsTable.setItems(consultants);

        /** LAMBDA EXPRESSION #1: BINDING COLUMNS TO A VALUE */
        consultantName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getConsultant()));
        dateCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper(cellData.getValue().getStart()));
        apptCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getApptType()));
        customerCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCustomerName()));
    }

}
