package ejercicios;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class tareaEvaluativaEjercicio3 {
    private static final String URL = "jdbc:mysql://localhost:3306/dbeventos";
    private static final String USER = "root";
    private static final String PASSWORD = "J240200p-1";

    public static void main(String[] args) throws IOException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            
            // Solicitar DNI
            System.out.println("Introduce el DNI del asistente:");
            String dni = Consola.readString();
            
            if (!dni.matches("\\d{8}[A-Z]")) {
                System.out.println("Formato de DNI inválido. Debe tener 8 números y una letra al final.");
                return;
            }
            
            // Consultar nombre del asistente
            String nombre = obtenerNombreAsistente(conn, dni);
            if (nombre == null) {
                System.out.println("No se encontro el asistente seleccionado");
                System.out.println("Introduce el nombre del asistente:");
                nombre = Consola.readString();
                registrarAsistente(conn, dni, nombre);
            }
            System.out.println("Estás realizando la reserva para: " + nombre);
            
            // Mostrar lista de eventos
            mostrarEventos(conn);
            
            // Elegir evento
            System.out.println("Elige el número del evento al que quiere asistir:");
            int eventoSeleccionado = Consola.readInt();
            
            // Verificar y registrar al asistente en el evento
            if (registrarEnEvento(conn, dni, eventoSeleccionado)) {
                System.out.println(nombre + " ha sido registrado para el evento seleccionado.");
            } else {
                System.out.println("No se pudo registrar al asistente. El evento está lleno o hubo un error.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String obtenerNombreAsistente(Connection conn, String dni) throws SQLException {
        String sql = "SELECT nombre FROM asistentes WHERE dni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        }
        return null;
    }

    private static void registrarAsistente(Connection conn, String dni, String nombre) throws SQLException {
        String sql = "INSERT INTO asistentes (dni, nombre) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.executeUpdate();
        }
    }

    private static void mostrarEventos(Connection conn) throws SQLException {
        String sql = "SELECT e.id_evento, e.nombre_evento, l.capacidad - COUNT(ae.dni) AS espacios_disponibles " +
                     "FROM eventos e " +
                     "JOIN ubicaciones l ON e.id_ubicacion = l.id_ubicacion " +
                     "LEFT JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento " +
                     "GROUP BY e.id_evento, l.capacidad";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int idEvento = rs.getInt("id_evento");
                String nombreEvento = rs.getString("nombre_evento");
                int espaciosDisponibles = rs.getInt("espacios_disponibles");
                System.out.println(idEvento + ". " + nombreEvento + " - Espacios disponibles: " + espaciosDisponibles);
            }
        }
    }

    private static boolean registrarEnEvento(Connection conn, String dni, int idEvento) throws SQLException {
        String sqlCapacidad = "SELECT l.capacidad - COUNT(ae.dni) AS espacios_disponibles " +
                              "FROM eventos e " +
                              "JOIN ubicaciones l ON e.id_ubicacion = l.id_ubicacion " +
                              "LEFT JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento " +
                              "WHERE e.id_evento = ? " +
                              "GROUP BY l.capacidad";
        try (PreparedStatement stmt = conn.prepareStatement(sqlCapacidad)) {
            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt("espacios_disponibles") > 0) {
                String sqlRegistro = "INSERT INTO asistentes_eventos (dni, id_evento) VALUES (?, ?)";
                try (PreparedStatement stmtRegistro = conn.prepareStatement(sqlRegistro)) {
                    stmtRegistro.setString(1, dni);
                    stmtRegistro.setInt(2, idEvento);
                    stmtRegistro.executeUpdate();
                    return true;
                }
            }
        }
        return false;
    }
}
