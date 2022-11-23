package controlador;

import java.sql.*;

/**
 *
 * @author lucas.varelanegro
 */
public class GestionEmpleado {
    public static boolean ExisteEmpleado(String idempleado) throws SQLException {
        String consulta = "Select idempleado from empleado where idempleado='"+idempleado+"'";
        Statement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        ResultSet rs = sentencia.executeQuery(consulta);
        if(rs.next()){
            return true;
        }
        rs.close();
        sentencia.close();
        return false;
    }

    public static void AñadirOperativa(String idempleado) throws SQLException {
        String consulta ="Update empleado set operativas= operativas+1 where idempleado=?";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        sentencia.setString(1, idempleado);
        sentencia.executeUpdate();
        sentencia.close();
    }

    public static void AñadirIncentivo(String idempleado, float total) throws SQLException {
        String incentivo = String.format("%.2f", total*0.01f).replace(',', '.');
        String consulta = "Update empleado set incentivo= incentivo+? where idempleado=?";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        sentencia.setFloat(1, Float.parseFloat(incentivo));
        sentencia.setString(2, idempleado);
        sentencia.executeUpdate();
        sentencia.close();
    }
}
