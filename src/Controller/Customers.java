package View_Controller;

import Model.Customer;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static Utility.DBConnection.deleteCustomer;
import static Utility.DBConnection.getAllCustomers;

public class Customers implements Initializable {

    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> phoneCol;
    @FXML private TableColumn<Customer, String> addressCol;
    @FXML private TableColumn<Customer, String> cityCol;
    @FXML private TableColumn<Customer, String> postalCodeCol;
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    public static int indexOfSelected;

    @FXML
    void deleteSelectedCustomer(MouseEvent event) {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        deleteCustomer(selectedCustomer);
    }

    @FXML
    void editCustomer(MouseEvent event) throws IOException {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        indexOfSelected = customerList.indexOf(selectedCustomer);

        Parent root = FXMLLoader.load(getClass().getResource("/View/editCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public static int getSelectedCustomer() {
        return indexOfSelected;
    }

    @FXML
    void newCustomer(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/addCustomer.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
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
        customersTable.setItems(customerList);

        /** LAMBDA EXPRESSION #1: BINDING COLUMNS TO A VALUE */
        nameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCustomerName()));
        phoneCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPhoneNum()));
        addressCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAddress()));
        cityCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCity()));
        postalCodeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPostalCode()));
    }
}
