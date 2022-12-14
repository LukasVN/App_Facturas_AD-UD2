package controlador;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        String consulta_insert = "Insert into historicofacturadoporcliente values "
                + "(?,"
                + "(select concat(cli.nombrecli,' ',apellidocli) from cliente cli where cli.idcliente='"+idcliente+"'),"
                + "(select sum(det.precio) from detalle det inner join factura fac on fac.numfactura=det.numfactura where fac.idcliente='"+idcliente+"'),"
                + "?)";
        PreparedStatement sentenciapr = Pool.getCurrentConexion().prepareStatement(consulta_insert);
        sentenciapr.setString(1, idcliente);
        sentenciapr.setString(2, facturas);
        sentenciapr.executeUpdate();
        sentenciapr.close();
    }
    public static void BorradoFacturas(String idcliente) throws SQLException {
        String consulta ="Delete from factura where idcliente=?";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        sentencia.executeUpdate();
        sentencia.close();
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
            sentencia.setString(3, GestionProducto.GetID_ProductoBD(model_ProductosFacturar.getValueAt(i, 0).toString().trim()));
            sentencia.setInt(4, Integer.parseInt(model_ProductosFacturar.getValueAt(i, 1).toString()));
            sentencia.setDouble(5, GestionProducto.getPrecioProducto(model_ProductosFacturar.getValueAt(i, 0).toString()));
            sentencia.executeUpdate();
            sentencia.close();
        }      
        
        
    }

    public static void BorradoDetalle(String idcliente) throws SQLException {
        String consulta ="Delete det from detalle det inner join factura fact on det.numfactura=fact.numfactura where fact.idcliente=?";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        sentencia.executeUpdate();
        sentencia.close();
    }

    public static float GetFacturadoLast30() throws SQLException {
        float facturado=0;
        Date fechalimit = new Date(Date.from(Instant.now().minus(30,ChronoUnit.DAYS)).getTime());
        String consulta ="select sum(precio*cantidad) as Importe from detalle det inner join factura fact on det.numfactura=fact.numfactura where fact.fecha>="+fechalimit.toString();
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(consulta);
        if(rs.next()){
            facturado = rs.getFloat(1);
        }
        sentencia.close();
        return facturado;
        
    }
    public static int getF_Pendientes() throws SQLException {
        int pendientes = 0;
        String sqlComand = "select count(*) from factura where cobrada=0";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(sqlComand);
        if (rs.next()) {
            pendientes = rs.getInt(1);
        }
        return pendientes;
    }

    public static void cargarTablaConsultaClientesFiltro(String idcliente,DefaultTableModel model_Consulta) throws SQLException {
        model_Consulta.setRowCount(0);
        String sqlComand = "select cli.nombrecli,count(fact.numfactura) from factura fact inner join cliente cli on cli.idcliente=fact.idcliente where cobrada=0 group by cli.nombrecli";
        if (!idcliente.equals("")) {
            sqlComand = sqlComand.substring(0, sqlComand.indexOf("0") + 1) + " and cli.idcliente='" + idcliente + "'" + sqlComand.substring(sqlComand.indexOf("0") + 1);
        }
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(sqlComand);
        while (rs.next()) {
            model_Consulta.setRowCount(model_Consulta.getRowCount() + 1);
            model_Consulta.setValueAt(rs.getString(1), model_Consulta.getRowCount() - 1, 0);
            model_Consulta.setValueAt(rs.getString(2), model_Consulta.getRowCount() - 1, 1);
        }
    }
    public static void cargarTablaConsultaClientes(DefaultTableModel model_Consulta) throws SQLException {
        model_Consulta.setRowCount(0);
        String sqlComand = "select cli.nombrecli,count(fact.numfactura) from factura fact inner join cliente cli on cli.idcliente=fact.idcliente where cobrada=0 group by cli.nombrecli";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(sqlComand);
        while (rs.next()) {
            model_Consulta.setRowCount(model_Consulta.getRowCount() + 1);
            model_Consulta.setValueAt(rs.getString(1), model_Consulta.getRowCount() - 1, 0);
            model_Consulta.setValueAt(rs.getString(2), model_Consulta.getRowCount() - 1, 1);
        }
    }
}
