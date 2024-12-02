package ejercicios;

import java.io.IOException;
import java.sql.*;

public class tareaEvaluativaEjercicio4 {

    public static void main(String[] args) throws IOException {
        // Configuración de la conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/dbeventos"; // Reemplaza con tu URL de conexión
        String usuario = "root"; // Cambia por tu usuario de MySQL
        String contrasena = "J240200p-1"; // Cambia por tu contraseña de MySQL

        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            // Mostrar lista de eventos
            System.out.println("Lista de eventos:");
            String consultaEventos = "SELECT id_evento, nombre_evento FROM eventos";
            try (Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery(consultaEventos)) {
                while (rs.next()) {
                    int idEvento = rs.getInt("id_evento");
                    String nombreEvento = rs.getString("nombre_evento");
                    System.out.println(idEvento + ". " + nombreEvento);
                }
            }

            // Solicitar ID del evento al usuario
            System.out.print("Introduce el ID del evento para consultar la cantidad de asistentes: ");
            System.out.println();
            int idEventoSeleccionado = Consola.readInt();

            // Llamar a la función almacenada obtener_numero_asistentes
            String llamadaFuncion = "{ ? = call obtener_numero_asistentes(?) }";
            try (CallableStatement callableStmt = conexion.prepareCall(llamadaFuncion)) {
                callableStmt.registerOutParameter(1, Types.INTEGER);
                callableStmt.setInt(2, idEventoSeleccionado);

                callableStmt.execute();
                int numeroAsistentes = callableStmt.getInt(1);

                // Mostrar resultado
                System.out.println("El número de asistentes para el evento seleccionado es: " + numeroAsistentes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
