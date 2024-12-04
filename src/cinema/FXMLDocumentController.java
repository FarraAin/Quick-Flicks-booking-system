package cinema;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane signin_form;
    
    @FXML
    private AnchorPane signin_form1;

    @FXML
    private AnchorPane signUp_form;

    @FXML
    private TextField signin_username;

    @FXML
    private PasswordField signin_password;
    
    @FXML
    private TextField signin_username1;

    @FXML
    private PasswordField signin_password1;

    @FXML
    private TextField signUp_username;

    @FXML
    private PasswordField signUp_password;

    @FXML
    private TextField signUp_email;

    @FXML
    private Button admin_loginBtn;

    @FXML
    private Button user_loginBtn;

    @FXML
    private Button signUp_btn;

    @FXML
    private Hyperlink signin_createAcc;
    
    @FXML
    private Hyperlink admin_login;
    
        @FXML
    private Hyperlink userBack;

    @FXML
    private Hyperlink signUp_alreadyHaveAcc;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

public void adminLogin() {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        
        connect = database.connectDb();
        try {
            
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, signin_username1.getText());
            prepare.setString(2, signin_password1.getText());
            result = prepare.executeQuery();

            Alert alert;
            if (signin_username1.getText().isEmpty() || signin_password1.getText().isEmpty()) {
              
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all blank fields");
                    alert.showAndWait();
                
            }else{    
                
                if (result.next()) {
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();
                    
                    
                    
                    
                    admin_loginBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                
            }else{
                    
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username/Password");
                    alert.showAndWait();   
                }
                
            }
        } catch (Exception e) { e.printStackTrace();}    
    }

public void userLogin() {
    String sql5 = "SELECT * FROM users WHERE username = ? AND password = ?";

    connect = database.connectDb(); // Ensure this method establishes a valid database connection

    try {
        // Prepare SQL statement
        prepare = connect.prepareStatement(sql5);
        prepare.setString(1, signin_username.getText());
        prepare.setString(2, signin_password.getText());
        result = prepare.executeQuery();

        Alert alert;

        // Check for empty fields
        if (signin_username.getText().isEmpty() || signin_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields.");
            alert.showAndWait();
        } else if (result.next()) {
            // Successful login
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Welcome User!");
            alert.showAndWait();

            // Close current login window
            signin_username.getScene().getWindow().hide();


            // Load user.fxml
            Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            // Optional: Make window draggable
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            stage.setScene(scene);
            stage.show();
        } else {
            // Invalid credentials
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Username or Password!");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText("Login Failed");
        alert.setContentText("An error occurred while processing the login: " + e.getMessage());
        alert.showAndWait();
    }
}





    public void signUp() {
        String checkUserSql = "SELECT * FROM users WHERE username = ? OR email = ?";
        String sql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";

        try {
            connect = database.connectDb();
            prepare = connect.prepareStatement(checkUserSql);
            prepare.setString(1, signUp_username.getText());
            prepare.setString(2, signUp_email.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                showAlert(Alert.AlertType.ERROR, "Username or email already exists!");
            } else if (signUp_email.getText().isEmpty() || signUp_username.getText().isEmpty() || signUp_password.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Please fill all blank fields");
            } else if (signUp_password.getText().length() < 8) {
                showAlert(Alert.AlertType.ERROR, "Password must be at least 8 characters long");
            } else if (validEmail()) {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, signUp_email.getText());
                prepare.setString(2, signUp_username.getText());
                prepare.setString(3, signUp_password.getText());
                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Account Created Successfully!");
                clearSignUpFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public boolean validEmail() {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._]+@[a-zA-Z0-9]+(\\.[a-zA-Z]+)+$");
        Matcher match = pattern.matcher(signUp_email.getText());
        if (match.find()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid email");
            return false;
        }
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == signin_createAcc) {
            signin_form.setVisible(false);
            signUp_form.setVisible(true);
        } else if (event.getSource() == signUp_alreadyHaveAcc) {
            signin_form.setVisible(true);
            signUp_form.setVisible(false);
        }
    }
    
        public void switchForm1(ActionEvent event) {
        if (event.getSource() == admin_login) {
            signin_form.setVisible(false);
            signin_form1.setVisible(true);
        } else if (event.getSource() == userBack) {
            signin_form.setVisible(true);
            signin_form1.setVisible(false);
        }
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearSignUpFields() {
        signUp_email.clear();
        signUp_username.clear();
        signUp_password.clear();
    }

    private void closeResources() {
        try {
            if (result != null) result.close();
            if (prepare != null) prepare.close();
            if (connect != null) connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

private void loadDashboard() {
    try {
        // Load the dashboard.fxml
        Parent root = FXMLLoader.load(getClass().getResource("user.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        // Optional: Handle dragging of the window if necessary
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.initStyle(StageStyle.TRANSPARENT); // Optional: For a transparent window
        stage.setScene(scene);
        stage.show(); // Show the dashboard window
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
     // Close Application
    public void signin_close() {
        System.exit(0);
    }

    // Minimize Application
    public void signin_minimize() {
        Stage stage = (Stage) signin_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void signup_close() {
        System.exit(0);
    }

    public void signup_minimize() {
        Stage stage = (Stage) signUp_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
}
