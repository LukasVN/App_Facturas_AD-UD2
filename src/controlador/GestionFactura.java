package controlador;

import java.sql.*;

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
    public static void Factura_a_Historico(String idcliente) throws SQLException {
        String facturas = "";
        String consulta_facturas_cliente = "Select fact.numfactura from detalle det,factura fact,cliente cli where cli.idcliente=fact.idcliente and fact.idcliente='"+idcliente+"' group by fact.numfactura;";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(consulta_facturas_cliente);
        while (rs.next()) {            
            if(!rs.isLast()){
                facturas+=rs.getString(1)+":";
            }
            else{
                facturas+=rs.getString(1);
            }
        }     
        String consulta_insert = "Insert into historicofacturadoporcliente values ('"+idcliente+"',concat(cli.nombrecli,cli.apellidocli),sum(det.precio),'"+facturas+"'";
    }
    
}
