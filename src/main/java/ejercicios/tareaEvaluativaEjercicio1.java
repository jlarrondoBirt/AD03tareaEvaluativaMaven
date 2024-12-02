package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class tareaEvaluativaEjercicio1 {
	public static void main(String[] args) {
		// Configuración de conexión a la base de datos
		String url = "jdbc:mysql://localhost:3306/dbeventos";
		String user = "root";
		String password = "J240200p-1";

		// Consulta SQL
		String query = 
				"SELECT e.nombre_evento AS Evento, " +
				 "COUNT(ae.dni) AS Asistentes, " +
				           "u.nombre AS Ubicacion, " +
				           "u.direccion AS Direccion " +
				    "FROM eventos e " +
				    "LEFT JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion " +
				    "LEFT JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento " +
				    "GROUP BY e.id_evento, u.nombre, u.direccion " +
				    "ORDER BY e.nombre_evento DESC;";

		// Conexión y ejecución
		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			// Encabezados
			System.out.printf("%-30s| %-12s| %-40s| %-30s%n", "Evento", "Asistentes", "Ubicacion", "Direccion");
			System.out.println(
					"-----------------------------------------------------------------------------------------------------------------");

			// Iterar por los resultados
			while (rs.next()) {
				String evento = rs.getString("Evento");
				int asistentes = rs.getInt("Asistentes");
				String ubicacion = rs.getString("Ubicacion");
				String direccion = rs.getString("Direccion");

				System.out.printf("%-30s| %-12d| %-40s| %-30s%n", evento, asistentes, ubicacion, direccion);
			}
		} catch (Exception e) {
			System.err.println("Error de conexion: " + e.getMessage());
		}
	}
}