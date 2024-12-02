package ejercicios;

import org.vibur.dbcp.ViburDBCPDataSource;
import java.sql.Connection;


public class tareaEvaluativaEjercicio5 {
    public static void main(String[] args) {
        // Configurar el DataSource
        ViburDBCPDataSource dataSource = new ViburDBCPDataSource();
        dataSource.setJdbcUrl("jdbc:hsqldb:hsql://localhost/demo"); // Ruta del archivo HSQLDB
        dataSource.setUsername("SA"); // Usuario predeterminado de HSQLDB
        dataSource.setPassword("");   // Contraseña predeterminada
        dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver"); // Driver para HSQLDB

        // Configuración del pool
        dataSource.setPoolInitialSize(5);
        dataSource.setPoolMaxSize(20);
        dataSource.start(); // Iniciar el pool

        try (Connection connection = dataSource.getConnection()) {
            // Validar la conexión
            if (connection.isValid(0)) {
                System.out.println("Conexión válida a la base de datos HSQLDB.");
            } else {
                System.out.println("Conexión no válida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dataSource.terminate(); // Liberar recursos del pool
        }
    }
}

/**
package ejercicios;

import org.vibur.dbcp.ViburDBCPDataSource;
import java.sql.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 * @see https://www.jc-mouse.net/
 * @author mouse

public class tareaEvaluativaEjercicio5 {

    static final String URL = "jdbc:hsqldb:hsql://localhost/demo";
    static final String USUARIO = "SA";
    static final String PASSWORD = "";

    public static void main(String[] args) {

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Customer");
            while (rs.next()) {
                Long id = rs.getLong("ID");
                String firstname = rs.getString("FIRSTNAME");
                String lastname = rs.getString("LASTNAME");
                String street = rs.getString("STREET");
                String city = rs.getString("CITY");                
                System.out.println(String.format("%d | %s | %s | %s | %s", id, firstname, lastname, street, city));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
 */
