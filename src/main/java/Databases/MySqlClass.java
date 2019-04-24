package databases;



import lombok.extern.log4j.Log4j;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;

@Log4j
public class MySqlClass {
    private final static String INSERT_NAME = "INSERT INTO names (name) VALUES (?)";
    private final static String SELECT_NAME = "SELECT name FROM names";
    public static Connection connect = null;
    public static Statement stat = null;
    public static ResultSet rs = null;
    public static PreparedStatement statement = null;

    public static void conn() throws ClassNotFoundException, SQLException, NamingException {
        Class.forName("org.mysql.JDBC");
        connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmateakademy");
    }

    public static void addName(String name) throws ClassNotFoundException, SQLException {
        try {
            conn();
            stat = connect.createStatement();
            statement = connect.prepareStatement(INSERT_NAME);
            statement.setString(1, name);
            statement.execute();
            statement.close();
        } catch  (Exception e) {
            log.error("ADD operation error: " + e.getMessage());
        } finally {
               if (rs != null) {
                   rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connect != null) {
                    connect.close();
                }
            }
    }

    public static ArrayList<String> getAllNames() throws ClassNotFoundException, SQLException, NamingException {
        ArrayList<String> names = new ArrayList<String>();
        conn();
        stat = connect.createStatement();
        rs = stat.executeQuery(SELECT_NAME);

        while (rs.next()) {
            names.add(rs.getString("name"));
        }

        rs.close();
        stat.close();
        closeDB();

        return names;
    }

    public static void closeDB() throws ClassNotFoundException, SQLException {
        connect.close();
    }
}
