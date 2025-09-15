package pertemuan_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static final String DATABASE = "prak_pbo_pertemuan3";
    public static final String PORT = "5432";
    public static final String HOST = "localhost";
    public static final String USER = "postgres";
    public static final String PASSWORD = "@awsed121;"; // pastikan benar
    public static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;

    public static Connection connect() {
        Connection conn = null;
        try {
            // Daftarkan driver PostgreSQL
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("❌ PostgreSQL JDBC Driver not found. Pastikan JAR sudah ditambahkan ke classpath!");
            e.printStackTrace();
        }
        return conn;
    }
}
