package View_Controller;

import Model.City;
import Model.Consultant;
import Model.Customer;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.rmi.server.ExportException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import static Utility.DBConnection.*;

public class AddCustomer implements Initializable {

    @FXML private TextField customerNameInput;
    @FXML private TextField phoneInput;
    @FXML private TextField addressInput;
    @FXML private ChoiceBox<String> cityInput;
    @FXML private TextField postalCodeInput;

    @FXML
    void addNewCustomer(MouseEvent event) throws IOException {
        try {
            String customerName = customerNameInput.getText();
            String phone = phoneInput.getText();
            String address = addressInput.getText();
            String city = cityInput.getValue();
            String postalCode = postalCodeInput.getText();

            Customer newCustomer = new Customer();
            newCustomer.setCustomerName(customerName);
            newCustomer.setPhoneNum(phone);
            newCustomer.setAddress(address);
            newCustomer.setCity(city);
            newCustomer.setPostalCode(postalCode);
            newCustomer.setCreatedBy(Consultant.getConsultant());
            newCustomer.setCreatedOn(LocalDateTime.now().toString());
            newCustomer.setLastUpdatedBy(Consultant.getConsultant());
            newCustomer.setLastUpdatedOn(LocalDateTime.now().toString());
            getAllCustomers().add(newCustomer);

            if(customerName.isEmpty() || address.isEmpty() || city.isEmpty()) {
                getAllCustomers().remove(newCustomer);
                throw new Exception();
            }
            else if (newCustomer.getPhoneNum().length() != 10) {
                getAllCustomers().remove(newCustomer);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is valid.");
                alert.setContentText("Phone number must be exactly 10 digits.\nPostal Code must be exactly 5 digits");
                alert.showAndWait();
            } else if (newCustomer.getPostalCode().length() != 5) {
                getAllCustomers().remove(newCustomer);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Please verify that no fields are empty and information entered is valid.");
                alert.setContentText("Phone number must be exactly 10 digits.\nPostal Code must be exactly 5 digits");
                alert.showAndWait();
            } else {
                addCustomer(newCustomer);

                Parent root = FXMLLoader.load(getClass().getResource("/View/customers.fxml"));
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
    void returnToCustomer(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/customers.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<City> cities = getAllCities();
        for(int i = 0; i< cities.size(); i++) {
            cityInput.getItems().add(cities.get(i).getCityName());
        }
    }
}


