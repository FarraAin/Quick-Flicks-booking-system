package cinema;


import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;

public class user1 implements Initializable {


    @FXML private Button availableMovie_buy;
    @FXML private Button availableMovie_clear;
    @FXML private Label availableMovie_normalClass_price;
    @FXML private Spinner<Integer> availableMovie_specialClass_quality;
    @FXML private Button availableMovie_receipt;
    @FXML private Label availableMovie_specialClass_price;
    @FXML private Spinner<Integer> availableMovie_normalClass_quality;
    @FXML private Label availableMovie_total;
    @FXML private Button availableMovies_btn;
    @FXML private TableColumn<moviesData, String> availableMovies_col_genre;
    @FXML private TableColumn<moviesData, String> availableMovies_col_movieTitle;
    @FXML private TableColumn<moviesData, String> availableMovies_col_showingDate;
    @FXML private Label availableMovies_date;
    @FXML private AnchorPane availableMovies_form;
    @FXML private Label availableMovies_genre;
    @FXML private ImageView availableMovies_imageView;
    @FXML private Label availableMovies_movieTitle;
    @FXML private Button availableMovies_selectMovie;
    @FXML private TableView<moviesData> availableMovies_tableView;
    @FXML private Label availableMovies_title;


    @FXML private Label dashboard_AvailableMovies;
    @FXML private Button dashboard_btn;
    @FXML private AnchorPane dashboard_form;
    @FXML private Label dashboard_totalEarnToday;
    @FXML private Label dashboard_totalSoldTicket;

    @FXML private Button minimize;
    @FXML private Button signout;
    @FXML private Label username;
    @FXML private AnchorPane topForm;
    
    private Image image;
    
    private double x = 0;
    private double y = 0;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    private SpinnerValueFactory<Integer> spinner1;
    private SpinnerValueFactory<Integer> spinner2;
    
    private float price1 = 0;
    private float price2 = 0;
    private float total = 0;
    private int qty1 = 0;
    private int qty2 = 0;
    

    
    private int num;
    private int qty;
    
    public void buy() {
        
        String sql = "INSERT INTO customer (type, movieTitle, quantity, total, date, time) VALUES (?,?,?,?,?,?)";
        
        connect = database.connectDb();
        String type = "";
        
        if(price1 > 0 && price2 == 0) {
            type = "Special Class";
        }else if(price2 > 0 && price1 == 0) {
            type = "Normal Class";
        }else if(price2 > 0 && price1 > 0) {
            type = "Special & Normal Class";
        }
        
        Date date = new Date();
        java.sql.Date setDate = new java.sql.Date(date.getTime());
        
        try{

            LocalTime localtime = LocalTime.now();
            Time time = Time.valueOf(localtime);
            
            qty = qty1 + qty2;
            
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, type);
            prepare.setString(2,availableMovies_title.getText());
            prepare.setString(3, String.valueOf(qty));
            prepare.setString(4, String.valueOf(total));
            prepare.setString(5,String.valueOf(setDate));
            prepare.setString(6, String.valueOf(time));
            
            Alert alert;
            
            if(availableMovies_imageView.getImage() == null 
                    ||availableMovies_title.getText().isEmpty()){
                
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the movie first");
                alert.showAndWait();

            }
            else if (price1 == 0 && price2 == 0){
            
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please indicate the quantity of ticket you want to purchase");
                alert.showAndWait();
            
            }else{
            
    
            prepare.executeUpdate();
            
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully purchase!");
                alert.showAndWait();
                
                String sql1 = "SELECT * FROM customer";
                
                prepare = connect.prepareStatement(sql1);
                result = prepare.executeQuery();
                
                num = 0;
                
                while(result.next()) {
                    
                    //get the last id we inserted
                    num = result.getInt("id");
                }
                
                String sql2 = "INSERT INTO customer_info (customer_id, type, quantity, total,movieTitle) VALUES (?,?,?,?,?)";
                
                prepare = connect.prepareStatement(sql2);
                prepare.setString(1, String.valueOf(num));
                prepare.setString(2, type);
                prepare.setString(3, String.valueOf(qty));
                prepare.setString(4, String.valueOf(total));
                prepare.setString(5, availableMovies_movieTitle.getText());
                prepare.execute();
                
                clearPurchaseTicketInfo();
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void clearPurchaseTicketInfo() {
        
        spinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        spinner2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        
        availableMovie_specialClass_quality.setValueFactory(spinner1);
        availableMovie_normalClass_quality.setValueFactory(spinner2);
        
        availableMovie_specialClass_price.setText("$0.0");
        availableMovie_normalClass_price.setText("$0.0");
        availableMovie_total.setText("$0.0");     
        
        availableMovies_imageView.setImage(null);
        availableMovies_movieTitle.setText("");

    }
    
    
    public void showsSpinnerValue() {
        
        spinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        spinner2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        
        availableMovie_normalClass_quality.setValueFactory(spinner1);
        availableMovie_specialClass_quality.setValueFactory(spinner2);
        
        
    }
    
    public void getSpinnerValue() {
        
        qty1 = availableMovie_specialClass_quality.getValue();
        qty2 = availableMovie_normalClass_quality.getValue();
        
        price1 = (qty1 * 25); //special class
        price2 = (qty2 *10); //normal class
        
        total = (price1 + price2);
        
        availableMovie_specialClass_price.setText("$" + String.valueOf(price1));
        availableMovie_normalClass_price.setText("$" + String.valueOf(price2));
        
        availableMovie_total.setText("$" + String.valueOf(total));
    }
    
    
// Available Movies Coding
    
   public ObservableList<moviesData> availableMoviesList() {
    
    ObservableList<moviesData> listAvMovies = FXCollections.observableArrayList();
    
    String sql = "SELECT * FROM movies WHERE current = 'Showing'";
    
    connect = database.connectDb();
    
    try{
        
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        moviesData movD;
        
        while(result.next()) {
            
            movD = new moviesData(result.getInt("id"), result.getString("movieTitle"),
                                    result.getString("genre"), result.getString("duration"),
                                    result.getString("image"), result.getDate("date"),
                                    result.getString("current"));
            
            listAvMovies.add(movD);
            
        }
        
    }catch(Exception e) {e.printStackTrace();}
    
    return listAvMovies;
    
}

private ObservableList<moviesData> availableMovieList;
public void showAvailableMovies() {
    
    availableMovieList = availableMoviesList();
    
    availableMovies_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    availableMovies_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
    availableMovies_col_showingDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    
    availableMovies_tableView.setItems(availableMovieList);
    
}

public void selectedAvailableMovies() {
    
    moviesData movD = availableMovies_tableView.getSelectionModel().getSelectedItem();
    int num = availableMovies_tableView.getSelectionModel().getSelectedIndex();
    
    if((num -1) < -1) {
        return;
    }
    
    availableMovies_movieTitle.setText(movD.getTitle());
    availableMovies_genre.setText(movD.getGenre());
    availableMovies_date.setText(String.valueOf(movD.getDate()));
    
    getData.path = movD.getImage();
    getData.title = movD.getTitle();
    
}

public void selectMovie() {
    
    Alert alert;
    
    // check if did not select the movie before click select movie
    if(availableMovies_title.getText().isEmpty()
            || availableMovies_genre.getText().isEmpty()
            || availableMovies_date.getText().isEmpty()){
        
        alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the movie first");
            alert.showAndWait();
    }else{
    
    String uri = "file:" + getData.path;
    
    image = new Image(uri, 127, 154,false, true);
    availableMovies_imageView.setImage(image);
    availableMovies_title.setText(getData.title);
    
    availableMovies_movieTitle.setText("");
    availableMovies_genre.setText("");
    availableMovies_date.setText("");
    
}
}


public void logout(){
    
    signout.getScene().getWindow().hide();
    
    try{
    Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
    
    Stage stage = new Stage();
    Scene scene = new Scene(root);
    
    root.setOnMousePressed((MouseEvent event) -> {
        
        x = event.getSceneX();
        y = event.getSceneY();
            
    });
    
    root.setOnMouseDragged((MouseEvent event) ->{
        
       stage.setX(event.getScreenX() -x);
       stage.setX(event.getScreenY() -y);
        
    });
    
    stage.initStyle(StageStyle.TRANSPARENT);
    
    stage.setScene(scene);
    stage.show();
    
    }catch(Exception e) {e.printStackTrace();}
}

public void switchForm(ActionEvent event) {
if(event.getSource() == availableMovies_btn){
        showForm(availableMovies_form);
        setButtonStyle(availableMovies_btn);
        
        showAvailableMovies();
        
    
        
    }
}

private void showForm(AnchorPane formToShow) {
    // Hide all forms
    dashboard_form.setVisible(false);
    availableMovies_form.setVisible(false);

    // Show the selected form
    formToShow.setVisible(true);
}

private void setButtonStyle(Button activeButton) {
    // Reset button styles to transparent
    dashboard_btn.setStyle("-fx-background-color: transparent;");
    availableMovies_btn.setStyle("-fx-background-color: transparent;");
    // Set active button style
    activeButton.setStyle("-fx-background-color: #ae2d3c;");
}

public void displayUsername(){
    username.setText(getData.username); // Make sure getData.username is properly set
}

public void close() {
    System.exit(0); // Close application
}

public void minimize() {
    Stage stage = (Stage) topForm.getScene().getWindow();
    stage.setIconified(true); // Minimize window
}



@Override
public void initialize(URL location, ResourceBundle resources) {
    displayUsername(); // Display username when the form initializes
    
    
    // To show available movies
    showAvailableMovies();
    
    showsSpinnerValue();
    

}
}
