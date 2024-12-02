package ejercicios;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class tareaEvaluativaEjercicio2 {
    private static final String URL = "jdbc:mysql://localhost:3306/dbeventos";
    private static final String USER = "root";
    private static final String PASSWORD = "J240200p-1";

    public static void main(String[] args) throws IOException {
        System.out.print("Introduce el nombre de la ubicación: ");
        String nombreUbicacion = Consola.readString();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Consultar capacidad actual
            String consultaCapacidad = "SELECT capacidad FROM ubicaciones WHERE nombre = ?";
            try (PreparedStatement psConsulta = connection.prepareStatement(consultaCapacidad)) {
                psConsulta.setString(1, nombreUbicacion);
                ResultSet rs = psConsulta.executeQuery();

                if (rs.next()) {
                    int capacidadActual = rs.getInt("capacidad");
                    System.out.println("La capacidad actual de la ubicación '" + nombreUbicacion + "' es: " + capacidadActual);

                    // Pedir nueva capacidad
                    System.out.print("Introduce la nueva capacidad máxima: ");
                    int nuevaCapacidad = Consola.readInt();

                    // Actualizar capacidad
                    String actualizarCapacidad = "UPDATE ubicaciones SET capacidad = ? WHERE nombre = ?";
                    try (PreparedStatement psActualizar = connection.prepareStatement(actualizarCapacidad)) {
                        psActualizar.setInt(1, nuevaCapacidad);
                        psActualizar.setString(2, nombreUbicacion);

                        int filasActualizadas = psActualizar.executeUpdate();
                        if (filasActualizadas > 0) {
                            System.out.println("Capacidad actualizada correctamente.");
                        } else {
                            System.out.println("Error al actualizar la capacidad.");
                        }
                    }
                } else {
                    System.out.println("La ubicación '" + nombreUbicacion + "' no existe.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error de conexion: " + e.getMessage());
        }
    }
}
        
