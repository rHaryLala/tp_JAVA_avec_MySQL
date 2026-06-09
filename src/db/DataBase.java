package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {

    public static Connection connecterBase() {
        String url = System.getProperty(
                "tp4.db.url",
                "jdbc:mysql://localhost:3306/tp4?useSSL=false&serverTimezone=UTC"
        );
        String user = System.getProperty("tp4.db.user", "root");
        String password = System.getProperty("tp4.db.password", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion etablie.");
            return connection;
        } catch (ClassNotFoundException | SQLException exception) {
            System.out.println("Erreur de connexion : " + exception.getMessage());
            return null;
        }
    }
}