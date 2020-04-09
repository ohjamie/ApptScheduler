package View_Controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import Model.Consultant;
import Utility.DBConnection;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class LogIn implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;
    @FXML private Button loginButton;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label locationLabel;
    @FXML private Label languageLabel;
    ResourceBundle rb;

    @FXML
    void authenticate(MouseEvent event) throws IOException {
        try {
            Connection con = DBConnection.startConnection();
            String sql = "SELECT * FROM user WHERE username = '"+usernameInput.getText()+"' AND password = '"+passwordInput.getText()+"';";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if(rs.next()) {
                Consultant consultant = new Consultant();
                consultant.setConsultant(usernameInput.getText());

                try(FileWriter fw = new FileWriter("user_logs.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                    out.println("User: " + Consultant.getConsultant() + " logged in at " + LocalDateTime.now());
                    out.println("");
                    out.print("User location: " + Locale.getDefault());
                    out.println("");
                } catch (IOException e) {
                    e.getCause();
                    e.getMessage();
                }
                Parent root = FXMLLoader.load(getClass().getResource("/View/dashboard.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("errorTitle"));
                alert.setHeaderText(rb.getString("errorMessage"));
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorMessage"));
            e.getCause();
            e.getMessage();
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        System.out.println("Current Locale: " + Locale.getDefault());
        System.out.println(5/2);
        System.out.println();

        // localize log in screen with appropriate Locale
        // two valid Locales: en_US, es_ES
        welcomeLabel.setText(rb.getString("welcome"));
        locationLabel.setText("Location: " + rb.getString("location"));
        languageLabel.setText("Language: " + rb.getString("language"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
    }
}