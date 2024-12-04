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

public class dashboardController implements Initializable {

    @FXML private Button addMovies_btn;
    @FXML private Button addMovies_clearBtn;
    @FXML private TableColumn<moviesData, Date> addMovies_col_date;
    @FXML private TableColumn<moviesData, String> addMovies_col_duration;
    @FXML private TableColumn<moviesData, String> addMovies_col_genre;
    @FXML private TableColumn<moviesData, String> addMovies_col_movieTitle;
    @FXML private DatePicker addMovies_date;
    @FXML private Button addMovies_deleteBtn;
    @FXML private TextField addMovies_duration;
    @FXML private AnchorPane addMovies_form;
    @FXML private TextField addMovies_genre;
    @FXML private ImageView addMovies_imageView;
    @FXML private Button addMovies_import;
    @FXML private Button addMovies_insertBtn;
    @FXML private TextField addMovies_movieTitle;
    @FXML private TextField addMovies_search;
    @FXML private TableView<moviesData> addMovies_tableView;
    @FXML private Button addMovies_updateBtn;

    @FXML private Button close;
    @FXML private Button customer_btn;
    @FXML private Button customer_clearBtn;
    @FXML private TableColumn<customerData, String> customer_col_date;
    @FXML private TableColumn<customerData, String> customer_col_movieTitle;
    @FXML private TableColumn<customerData, String> customer_col_ticketNumber;
    @FXML private TableColumn<customerData, String> customer_col_time;
    @FXML private Label customer_date;
    @FXML private Button customer_deleteBtn;
    @FXML private AnchorPane customer_form;
    @FXML private Label customer_genre;
    @FXML private Label customer_movieTitle;
    @FXML private TextField customer_search;
    @FXML private TableView<customerData> customer_tableView;
    @FXML private Label customer_ticketNumber;
    @FXML private Label customer_time;

    @FXML private Label dashboard_AvailableMovies;
    @FXML private Button dashboard_btn;
    @FXML private AnchorPane dashboard_form;
    @FXML private Label dashboard_totalEarnToday;
    @FXML private Label dashboard_totalSoldTicket;
    @FXML private Button editScreening_btn;
    @FXML private TableColumn<moviesData, String> editScreening_col_current;
    @FXML private TableColumn<moviesData, String> editScreening_col_duration;
    @FXML private TableColumn<moviesData, String> editScreening_col_genre;
    @FXML private TableColumn<moviesData, String> editScreening_col_movieTitle;
    @FXML private ComboBox<String> editScreening_current;
    @FXML private Button editScreening_deleteBtn;
    @FXML private AnchorPane editScreening_form;
    @FXML private ImageView editScreening_imageView;
    @FXML private TextField editScreening_search;
    @FXML private TableView<moviesData> editScreening_tableView;
    @FXML private Label editScreening_title;
    @FXML private Button editScreening_updateBtn;

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
    
    private int totalMovies;
    
    public void totalAvailableMovies() {
        
        String sql = "SELECT COUNT(id) FROM movies WHERE current = 'Showing'";
        
        connect = database.connectDb();
        
        try{
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()) {
                
                totalMovies = result.getInt("count(id)");
                
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void displayTotalAvailableMovies() {
        totalAvailableMovies();
        dashboard_AvailableMovies.setText(String.valueOf(totalMovies));
        
    }
    
    private double totalIncome;
    public void totalIncomeToday() {


            Date date = new Date(); // Correct constructor call for creating a new Date object
            java.sql.Date sqlDate = new java.sql.Date(date.getTime()); // Convert java.util.Date to java.sql.Date

        
        String sql = "SELECT SUM(total) FROM customer WHERE date = '" + String.valueOf(sqlDate) + "'";
        
        connect = database.connectDb();
        
        try{
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                
                totalIncome = result.getDouble("SUM(total)");
                
            }
            
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void displayTotalIncomeToday() {
        totalIncomeToday();
        dashboard_totalEarnToday.setText("RM" + String.valueOf(totalIncome)); 
    }
    
    private int soldTicket;
    public void countTicket() {
        
        String sql = "SELECT count(id) FROM customer";
        
        connect = database.connectDb();
        
        try{
            
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        if(result.next()){
            
            soldTicket = result.getInt("count(id)");
            
        }
            
        }catch(Exception e){e.printStackTrace();}
                
        
    }
    
        public void displayTotalSoldTicket() {
            countTicket();
            dashboard_totalSoldTicket.setText(String.valueOf(soldTicket));
   
    }
    
    public void searchCustomer() {
        // Create a FilteredList to filter customer data
        FilteredList<customerData> filter = new FilteredList<>(custList, e -> true);

        // Add a listener to the search field
        customer_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateCustomer -> {
                // If the search field is empty or null, show all customers
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                // Check for matches in various fields
                if (String.valueOf(predicateCustomer.getId()).contains(searchKey)) {
                    return true;
                }else if(predicateCustomer.getTitle().toLowerCase().contains(searchKey)) {
                    
                    return true;
                    
                }else if(predicateCustomer.getDate().toString().contains(searchKey)){
                    
                    return true;
                    
                }else{
                    
                }
                
                
                return false;
            });
        });
        
        SortedList<customerData> sort = new SortedList<>(filter);
        sort.comparatorProperty().bind(customer_tableView.comparatorProperty());
        customer_tableView.setItems(sort);
            
    }
    
    public ObservableList<customerData> customerList(){
        
        ObservableList<customerData> customerL = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM customer";
        
        connect = database.connectDb();
        
 
         try {
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();

        while (result.next()) {
            customerData customerD = new customerData(
                result.getInt("id"),             // ID
                result.getString("type"),        // Type
                result.getString("movieTitle"),    
                result.getInt("quantity"),       // Quantity
                result.getDouble("total"),       // Total
                result.getDate("date"),          // Date
                result.getTime("time")           // Time
            );

            // Add to list
            customerL.add(customerD);
        }
            
        }catch(Exception e){e.printStackTrace();}
    
        return customerL;
    }
    
    private ObservableList<customerData> custList;
    public void showCustomerList(){
        
        custList = customerList();
        customer_col_ticketNumber.setCellValueFactory(new PropertyValueFactory<>("id"));
        customer_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        customer_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customer_col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        customer_tableView.setItems(custList);
        
    }
    
    public void selectCustomerList() {
        
        customerData custD = customer_tableView.getSelectionModel().getSelectedItem();
        int num = customer_tableView.getSelectionModel().getSelectedIndex();
        
        if((num - 1) <-1) {
            return;     
        }
        
        customer_ticketNumber.setText(String.valueOf(custD.getId()));
        customer_movieTitle.setText(String.valueOf(custD.getTitle()));
        customer_date.setText(String.valueOf(custD.getDate()));
        customer_time.setText(String.valueOf(custD.getTime()));
        
        
        
    }
    
        public void deleteCustomer() {
            // SQL query with placeholder
            String sql = "DELETE FROM customer WHERE id = ?";

            // Connect to the database
            connect = database.connectDb();

            try {
                Alert alert;

                // Check if required fields are empty
                if (customer_ticketNumber.getText().isEmpty() ||
                    customer_movieTitle.getText().isEmpty() ||
                    customer_date.getText().isEmpty() ||
                    customer_time.getText().isEmpty()) {

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select the customer first.");
                    alert.showAndWait();
                    return; // Exit the method as the fields are not valid
                }

                // Confirmation Alert
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete customer: " + customer_movieTitle.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    // Use PreparedStatement to avoid SQL Injection
                    PreparedStatement preparedStatement = connect.prepareStatement(sql);
                    preparedStatement.setString(1, customer_ticketNumber.getText());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Customer successfully removed!");
                        alert.showAndWait();

                        showCustomerList();
                        clearCustomer(); // Clear the fields after successful deletion
                        
                        
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Customer not found. Please try again.");
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while deleting the customer: " + e.getMessage());
                alert.showAndWait();
            } finally {
                // Close the database connection
                try {
                    if (connect != null) connect.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    
    public void clearCustomer() {
        
        customer_ticketNumber.setText("");
        customer_movieTitle.setText("");
        customer_date.setText("");
        customer_time.setText("");
        
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

// Edit Screening Coding
    


private String[] currentList = {"Showing", "End Showing"};
public void comboBox() {
    
    List<String> listCurrent = new ArrayList<>();
    
    for(String data: currentList) {
        listCurrent.add(data);
    }
    
    ObservableList listC = FXCollections.observableArrayList(listCurrent);
    editScreening_current.setItems(listC);
    
}


public void updateEditScreening() {
    
    String sql = "UPDATE movies SET current = '"
                + editScreening_current.getSelectionModel().getSelectedItem() 
                + "' WHERE movieTitle = '" + editScreening_title.getText() + "'";
    
    connect = database.connectDb();
    
    try{
        
        statement = connect.createStatement();
        
        Alert alert;
        
        if(editScreening_title.getText().isEmpty()
                || editScreening_imageView.getImage() == null 
                || editScreening_current.getSelectionModel().isEmpty()){
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the movie first");
            alert.showAndWait();
            
        }else{
            
            statement.executeUpdate(sql);
            
            
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Update!");
            alert.showAndWait();
            
            showEditScreening();
            clearEditScreening();
            
        }
        
    }catch(Exception e) {e.printStackTrace();}
    
    
}

public void clearEditScreening(){
    
    editScreening_title.setText("");
    editScreening_imageView.setImage(null);


}

public void searchEditScreening(){
    
           FilteredList<moviesData>filter = new FilteredList<>(editScreeningL, e -> true);
    
        editScreening_search.textProperty().addListener((observale, oldValue, newValue) ->{
        
        filter.setPredicate(predicateMoviesData ->{
            
            if(newValue.isEmpty() || newValue == null){
                
                return true;
            }
            
            String keySearch = newValue.toLowerCase();
            
            if(predicateMoviesData.getTitle().toLowerCase().contains(keySearch)){
                
                return true;
            }else if(predicateMoviesData.getGenre().toLowerCase().contains(keySearch)) {
                
                return true;
                
            }else if(predicateMoviesData.getDuration().toLowerCase().contains(keySearch)){
                
                return true;
                
            }else if(predicateMoviesData.getCurrent().toString().contains(keySearch)){
                
                return true;
            }
            

            
            return false;
        });
    });
    
    SortedList<moviesData> sortData = new SortedList<>(filter);
    sortData.comparatorProperty().bind(editScreening_tableView.comparatorProperty());
    editScreening_tableView.setItems(sortData);
    
}


public void selectedEditScreening() {
    
    moviesData movD = editScreening_tableView.getSelectionModel().getSelectedItem();
    int num = editScreening_tableView.getSelectionModel().getFocusedIndex();
    
    if((num - 1) < -1) {
        return;
    }
    
    String uri = "file:" + movD.getImage();
    image = new Image(uri, 136, 174, false, true);
    // Set the image to the ImageView
    editScreening_imageView.setImage(image);
    
    editScreening_title.setText(movD.getTitle());
    
}

public ObservableList<moviesData> editScreeningList(){
    
    ObservableList<moviesData> editSList = FXCollections.observableArrayList();
    
    String sql = "SELECT * FROM movies";
    
    connect = database.connectDb();
    
    try{
        
      prepare = connect.prepareStatement(sql);
      result = prepare.executeQuery();
      
      moviesData movD;
      
      while(result.next()) {
          movD = new moviesData(result.getInt("id"), result.getString("movieTitle"),
                                result.getString("genre"),
                                result.getString("duration"),
                                result.getString("image"),
                                result.getDate("date"),
                                result.getString("current"));
          
          editSList.add(movD);
          
      }
        
    }catch(Exception e) {e.printStackTrace();}
    return editSList;
}

private ObservableList<moviesData> editScreeningL;
public void showEditScreening(){
    
    editScreeningL = editScreeningList();
    
    editScreening_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    editScreening_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
    editScreening_col_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
    editScreening_col_current.setCellValueFactory(new PropertyValueFactory<>("current"));
    
    editScreening_tableView.setItems(editScreeningL);
    
}

public void searchAddMovies() {
    
    FilteredList<moviesData>filter = new FilteredList<>(listAddMovies, e -> true);
    
    addMovies_search.textProperty().addListener((observale, oldValue, newValue) ->{
        
        filter.setPredicate(predicateMoviesData ->{
            
            if(newValue.isEmpty() || newValue == null){
                
                return true;
            }
            
            String keySearch = newValue.toLowerCase();
            
            if(predicateMoviesData.getTitle().toLowerCase().contains(keySearch)){
                
                return true;
            }else if(predicateMoviesData.getGenre().toLowerCase().contains(keySearch)) {
                
                return true;
                
            }else if(predicateMoviesData.getDuration().toLowerCase().contains(keySearch)){
                
                return true;
                
            }else if(predicateMoviesData.getDate().toString().contains(keySearch)){
                
                return true;
            }
            

            
            return false;
        });
    });
    
    SortedList<moviesData> sortData = new SortedList<>(filter);
    sortData.comparatorProperty().bind(addMovies_tableView.comparatorProperty());
    addMovies_tableView.setItems(sortData);
    
}

public void importImage() {
    
    FileChooser open = new FileChooser();
    open.setTitle("Open Image File");
    open.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg")); 
    
    Stage stage = (Stage)addMovies_form.getScene().getWindow();
    File file = open.showOpenDialog(stage);
    
    if(file !=null){
        
        image = new Image(file.toURI().toString(), 134, 172, false, true);
        
        addMovies_imageView.setImage(image);
        
        //TO GET IMAGE PATH
        getData.path = file.getAbsolutePath();
    }
}
private int countId;

public void movieId(){
    
    String sql = "SELECT count(id) FROM movies";
    
    connect = database.connectDb();
    
    try{
        
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        if(result.next()) {
            
            getData.movieId = result.getInt("count(id)");
            
        }
        
    }catch(Exception e) {e.printStackTrace();}
    
}

public void insertAddMovies() {
    
    
    
    String sql1 = "SELECT * FROM movies WHERE movieTitle = '"
                + addMovies_movieTitle.getText() +"'";
    
    connect = database.connectDb();
    
    Alert alert;
    
    try{
        
        statement = connect.createStatement();
        result = prepare.executeQuery(sql1);
        
        if(result.next()) {
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText(addMovies_movieTitle.getText()+ "was already exist");
            alert.showAndWait();
            
            
        }else{

             if(addMovies_movieTitle.getText().isEmpty()
                    || addMovies_genre.getText().isEmpty()
                    || addMovies_duration.getText().isEmpty()
                    || addMovies_imageView.getImage() == null
                    || addMovies_date.getValue() == null){

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields!");
            alert.showAndWait();
               
        }else{
                
            String sql = "INSERT INTO movies (id, movieTitle,genre,duration,image,date) VALUES (?,?,?,?,?,?)";
            
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");
        
        movieId();
        
        String mID = String.valueOf(getData.movieId + 1);
        
        prepare = connect.prepareStatement(sql);
        prepare.setString(1, mID);
        prepare.setString(2, addMovies_movieTitle.getText());
        prepare.setString(3, addMovies_genre.getText());
        prepare.setString(4, addMovies_duration.getText());
        prepare.setString(5, uri);
        prepare.setString(6, String.valueOf(addMovies_date.getValue()));
        
        prepare.execute();
            
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully add new movie!");
            alert.showAndWait();
            
            clearAddMoviesList();
            showAddmovieList();
        
                
            }
            
        }
       
            
    }catch(Exception e) {e.printStackTrace();}
}

public void updateAddMovie() {
    
    String uri = getData.path;
    uri = uri.replace("\\", "\\\\");
    
    String sql = "UPDATE movies SET movieTitle = '" + addMovies_movieTitle.getText()
                    + "', genre = '" + addMovies_genre.getText()
                    + "', duration = '" + addMovies_duration.getText()
                    + "', image = '" + uri 
                    + "', date = '" + addMovies_date.getValue()
                    + "' WHERE id = '"+ String.valueOf(getData.movieId) +"'";
    
    connect = database.connectDb();
    
    try{
        
        statement = connect.createStatement();
        
        Alert alert;
        
        if(addMovies_movieTitle.getText().isEmpty() 
                || addMovies_genre.getText().isEmpty()
                || addMovies_duration.getText().isEmpty()
                || addMovies_imageView.getImage() == null
                || addMovies_date.getValue() == null){
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the movie first");
            alert.showAndWait();
            
            
        }else{
            
            statement.executeUpdate(sql);
            
            showAddmovieList();
            clearAddMoviesList();

            
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully update" + addMovies_movieTitle.getText());
            alert.showAndWait();
            
            clearAddMoviesList();
            
        }
                
    }catch(Exception e) {e.printStackTrace();}
    
}

public void deleteAddMovies() {
    
    String sql = "DELETE FROM  movies WHERE movieTitle = '" 
                + addMovies_movieTitle.getText() + "'";
    
    connect = database.connectDb();
    
    try{
        
        statement = connect.createStatement();
        
        Alert alert;
        
        if(addMovies_movieTitle.getText().isEmpty() 
                || addMovies_genre.getText().isEmpty()
                || addMovies_duration.getText().isEmpty()
                || addMovies_date.getValue() == null
                || addMovies_imageView.getImage() == null){
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the movie first");
            alert.showAndWait();
            
        }else{
            
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to delete " 
                                    + addMovies_movieTitle.getText() +"?");
            
            Optional<ButtonType> option = alert.showAndWait();
            
            if(ButtonType.OK.equals(option.get())) {
                
            statement.executeUpdate(sql);
            
            showAddmovieList();
            clearAddMoviesList();
             
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Sucessfully delete!");
            alert.showAndWait();
             
            }else{
                return;
            }
            
        }
        
    }catch(Exception e){e.printStackTrace();}
        
    
}

public void clearAddMoviesList(){
    
    addMovies_movieTitle.setText("");
    addMovies_genre.setText("");
    addMovies_duration.setText("");
    addMovies_imageView.setImage(null);
    addMovies_date.setValue(null);
}

public ObservableList<moviesData> addMoviesList() {
    
    ObservableList<moviesData> listData = FXCollections.observableArrayList();
    
    String sql = "SELECT * FROM movies";
    
    connect = database.connectDb();
    
    try{
        
        prepare = connect.prepareStatement(sql);
        result = prepare.executeQuery();
        
        moviesData movD;
        
        while(result.next()) {
            movD = new moviesData(result.getInt("id"), result.getString("movieTitle"), result.getString("genre"),
                                    result.getString("duration"), result.getString("image"),
                                    result.getDate("date"),result.getString("current"));
            
            listData.add(movD);
        }
        
        
    }catch (Exception e){e.printStackTrace();}
    return listData;
}  


    private ObservableList<moviesData> listAddMovies;
    public void showAddmovieList() {
        listAddMovies = addMoviesList();

        addMovies_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        addMovies_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        addMovies_col_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        addMovies_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addMovies_tableView.setItems(listAddMovies);
    
        
}
        
            public void selectAddMoviesList() {
        
        moviesData movD = addMovies_tableView.getSelectionModel().getSelectedItem();
        int num = addMovies_tableView.getSelectionModel().getSelectedIndex();
        
        if((num - 1) < -1) {
            return;
        }
        
        getData.path = movD.getImage();
        
        getData.movieId = movD.getId();
        
        addMovies_movieTitle.setText(movD.getTitle());
        addMovies_genre.setText(movD.getGenre());
        addMovies_duration.setText(movD.getDuration());
        
        String getDate = String.valueOf(movD.getDate());
        
        String uri = "file:" + movD.getImage();
        
        image = new Image (uri, 134, 172, false, true);
        addMovies_imageView.setImage(image);
        
        addMovies_date.setValue(movD.getDate().toLocalDate());
        
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
    // Logic for switching between different forms
    if(event.getSource() == dashboard_btn){
        showForm(dashboard_form);
        setButtonStyle(dashboard_btn);
        
        displayTotalSoldTicket();
        displayTotalIncomeToday();
        displayTotalAvailableMovies();
        
    } else if(event.getSource() == addMovies_btn){
        showForm(addMovies_form);
        setButtonStyle(addMovies_btn);
        
        showAddmovieList();
        
        
    } else if(event.getSource() == editScreening_btn){
        showForm(editScreening_form);
        setButtonStyle(editScreening_btn);
        
        showEditScreening();
        
    } else if(event.getSource() == customer_btn) {
        showForm(customer_form);
        setButtonStyle(customer_btn);
        
        showCustomerList();
    }
}

private void showForm(AnchorPane formToShow) {
    // Hide all forms
    dashboard_form.setVisible(false);
    addMovies_form.setVisible(false);
    editScreening_form.setVisible(false);
    customer_form.setVisible(false);

    // Show the selected form
    formToShow.setVisible(true);
}

private void setButtonStyle(Button activeButton) {
    // Reset button styles to transparent
    dashboard_btn.setStyle("-fx-background-color: transparent;");
    addMovies_btn.setStyle("-fx-background-color: transparent;");
    editScreening_btn.setStyle("-fx-background-color: transparent;");
    customer_btn.setStyle("-fx-background-color: transparent;");

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
    
    showAddmovieList();
    
    showEditScreening();
    
    comboBox();
    
    showCustomerList();
    
    showEditScreening();
    
    displayTotalSoldTicket();
    
    displayTotalIncomeToday();
    
    displayTotalAvailableMovies();
}
}
