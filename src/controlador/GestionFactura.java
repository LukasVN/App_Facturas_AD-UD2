package controlador;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lucas.varelanegro
 */
public class GestionFactura {

    public static void ActualizarCobro(String numFactura) throws SQLException {
        
        String consulta = "update factura set cobrada = true where numfactura ='"+numFactura+"' and cobrada = false";
        Statement sentencia = Pool.getCurrentConexion().createStatement();               
        sentencia.executeUpdate(consulta);
    }
    
}
