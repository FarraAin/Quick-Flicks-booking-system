package cinema;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {

    public static Connection connectDb() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to the database
            String url = "jdbc:mysql://localhost:3306/cinemabook";
            String username = "root";
            String password = ""; 
            Connection connect = DriverManager.getConnection(url, username, password);

            return connect;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if connection fails
        }
    }
}
