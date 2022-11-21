package controlador;

import java.sql.*;
import java.time.Instant;
import javax.swing.table.DefaultTableModel;

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
        rs.close();
        sentencia.close();
        //Query con fallo
        String consulta_insert = "Insert into historicofacturadoporcliente values (?,concat(cli.nombrecli,cli.apellidocli),sum(det.precio),?) select cli.nombrecli,apellidocli,det.precio from cliente cli,detalle det";
        PreparedStatement sentenciapr = Pool.getCurrentConexion().prepareStatement(consulta_insert);
        sentenciapr.setString(1, idcliente);
        sentenciapr.setString(2, facturas);
        sentenciapr.executeUpdate();
        sentenciapr.close();
    }
    public static void BorradoFacturas(String idcliente) {
        
    }
    public static void RealizarFactura() {
        
    }
    public static boolean ExisteFactura(String numfactura) throws SQLException {
        String consulta = "Select numfactura from factura where numfactura='"+numfactura+"'";
        Statement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        ResultSet rs = sentencia.executeQuery(consulta);
        if(rs.next()){
            return true;
        }
        rs.close();
        sentencia.close();
        return false;
    }

    public static void InsertarFactura(String numfactura,String idcliente, String idempleado, boolean cobrada,double iva, DefaultTableModel model_ProductosFacturar) throws SQLException {
        String consulta = "Insert into factura values(?,?,?,?,?,?)";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        sentencia.setString(1, numfactura);
        sentencia.setString(2, idcliente);
        sentencia.setString(3, idempleado);
        sentencia.setDate(4, new Date(Date.from(Instant.now()).getTime()));
        sentencia.setBoolean(5, cobrada);
        sentencia.setDouble(6, iva);
        sentencia.executeUpdate();
        sentencia.close();
               
        consulta = "Insert into detalle values(?,?,?,?,?)";
        for(int i=0; i<model_ProductosFacturar.getRowCount();i++){
            sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
            sentencia.setString(1, numfactura);
            sentencia.setInt(2, i + 1);
            sentencia.setString(3, model_ProductosFacturar.getValueAt(i, 0).toString());
            sentencia.setInt(4, Integer.parseInt(model_ProductosFacturar.getValueAt(i, 1).toString()));
            sentencia.setDouble(5, GestionProducto.getPrecioProducto(model_ProductosFacturar.getValueAt(i, 0).toString()));
        }
        sentencia.executeUpdate();
        sentencia.close();
        
    }

}
