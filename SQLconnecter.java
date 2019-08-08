
import java.sql.*;
public class SQLconnecter {
    public static Connection connect() {
        Connection conn = null;
            try {

                String url = "jdbc:sqlite::memory:";

                // create a connection to the .db file
                conn = DriverManager.getConnection(url);
                System.out.println("Connection to SQLite has been established.");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
    }
}

