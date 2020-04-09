package View_Controller;

import Model.City;
import Model.Consultant;
import Model.Customer;
import Utility.DBConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static Utility.DBConnection.*;
import static View_Controller.Customers.getSelectedCustomer;

public class EditCustomer implements Initializable {

    @FXML private TextField customerNameInput;
    @FXML private TextField phoneInput;
    @FXML private TextField addressInput;
    @FXML private ChoiceBox<String> cityInput;
    @FXML private TextField postalCodeInput;
    @FXML private Label updatedByText;
    @FXML private Label updatedOnText;
    @FXML private Label createdByText;
    @FXML private Label createdOnText;

    public static int indexOfSelected;

    @FXML
    void returnToCustomer(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/customers.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void updateCustomerInfo(MouseEvent event) throws IOException {
        try {
            Customer selectedCustomer = getAllCustomers().get(indexOfSelected);

            String customerName = customerNameInput.getText();
            String phone = phoneInput.getText();
            String address = addressInput.getText();
            String city = cityInput.getValue();
            String postalCode = postalCodeInput.getText();
            String createdBy = selectedCustomer.getCreatedBy();
            String createdOn = selectedCustomer.getCreatedOn();

            selectedCustomer.setCustomerId(selectedCustomer.getCustomerId());
            selectedCustomer.setCustomerName(customerName);
            selectedCustomer.setPhoneNum(phone);
            selectedCustomer.setAddress(address);
            selectedCustomer.setCity(city);
            selectedCustomer.setPostalCode(postalCode);
            selectedCustomer.setCreatedBy(createdBy);
            selectedCustomer.setCreatedOn(createdOn);
            selectedCustomer.setLastUpdatedBy(Consultant.getConsultant());
            selectedCustomer.setLastUpdatedOn(LocalDateTime.now().toString());

            if (selectedCustomer.getPhoneNum().length() != 10) {
                getAllCustomers().remove(selectedCustomer);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is valid.");
                alert.setContentText("Phone number must be exactly 10 digits.\nPostal Code must be exactly 5 digits");
                alert.showAndWait();
            } else if (selectedCustomer.getPostalCode().length() != 5) {
                getAllCustomers().remove(selectedCustomer);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is valid.");
                alert.setContentText("Phone number must be exactly 10 digits.\nPostal Code must be exactly 5 digits");
                alert.showAndWait();
            } else {
                updateCustomer(selectedCustomer);

                Parent root = FXMLLoader.load(getClass().getResource("/View/customers.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please verify that no fields are empty and information entered is correct.");
            alert.setContentText("Fill all fields.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // retrieve customer selected from customer table
        indexOfSelected = getSelectedCustomer();
        Customer selectedCustomer = DBConnection.getAllCustomers().get(indexOfSelected);

        // fill dropdown with all cities
        ObservableList<City> cities = getAllCities();
        for(int i = 0; i< cities.size(); i++) {
            cityInput.getItems().add(cities.get(i).getCityName());
        }

        updatedByText.setText("Last updated by: " + selectedCustomer.getLastUpdatedBy());
        updatedOnText.setText("Last updated on: " + selectedCustomer.getLastUpdatedOn());
        createdOnText.setText("Created on: " + selectedCustomer.getCreatedOn());
        createdByText.setText("Created by: " + selectedCustomer.getCreatedBy());

        customerNameInput.setText(selectedCustomer.getCustomerName());
        phoneInput.setText(selectedCustomer.getPhoneNum());
        addressInput.setText(selectedCustomer.getAddress());
        cityInput.setValue(selectedCustomer.getCity());
        postalCodeInput.setText(selectedCustomer.getPostalCode());
    }
}
