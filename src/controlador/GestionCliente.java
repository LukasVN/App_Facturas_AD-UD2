package controlador;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import java.sql.*;

/**
 *
 * @author lucas.varelanegro
 */
public class GestionCliente {
    public static void cargarCombo(JComboBox<Cliente> cmbCliente) throws SQLException {
        Cliente c;
        cmbCliente.removeAllItems();
        String consulta = "Select * from cliente";
        ResultSet rs;
        cmbCliente.addItem(null); //Cargamos este nulo para que no se active el actionperformed del combo y de error
        try (Statement sentencia = Pool.getCurrentConexion().createStatement()) {
            rs = sentencia.executeQuery(consulta);
            while (rs.next()) {
                c = new Cliente(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4));
                cmbCliente.addItem(c);
            }
        }
        rs.close();
        cmbCliente.removeItemAt(0); //Borramos ahora el null. Para que quede solo con los datos
    }
    public static void CargarFilaFacturaCliente(DefaultTableModel tbl_FactCliente, String id_Cliente) throws SQLException {
        String consulta = "Select distinct f.numfactura,cli.nombrecli,cli.apellidocli,f.fecha,sum(precio) as importe,f.cobrada from cliente cli,factura f,detalle d "
                + "where cli.idcliente='"+id_Cliente+"' and cli.idcliente = f.idcliente and f.numfactura = d.numfactura group by cli.nombrecli, f.numfactura";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        tbl_FactCliente.setRowCount(0);
        
        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            tbl_FactCliente.setRowCount(tbl_FactCliente.getRowCount()+1);
            tbl_FactCliente.setValueAt(rs.getString(1), tbl_FactCliente.getRowCount() -1, 0);
            tbl_FactCliente.setValueAt(rs.getString(2), tbl_FactCliente.getRowCount() -1, 1);
            tbl_FactCliente.setValueAt(rs.getString(3), tbl_FactCliente.getRowCount() -1, 2);
            tbl_FactCliente.setValueAt(rs.getDate(4), tbl_FactCliente.getRowCount() -1, 3);
            tbl_FactCliente.setValueAt(rs.getFloat(5), tbl_FactCliente.getRowCount() -1, 4);
            tbl_FactCliente.setValueAt(rs.getBoolean(6), tbl_FactCliente.getRowCount() -1, 5);
            
        }
        sentencia.close();
        rs.close();     
    }
    public static boolean ComprobarCobradasID_Cliente(String idcliente) throws SQLException {
        String consulta = "Select cli.idcliente,fact.cobrada from cliente cli, factura fact where cli.idcliente='"+idcliente+"' and cli.idcliente=fact.idcliente";
        Boolean cobrada = true;
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            if(rs.getBoolean(2) == false){
                cobrada = false;
            }
        }
        sentencia.close();
        rs.close();
        return cobrada;   
    }
    public static boolean ExisteCliente(String idcliente) throws SQLException {
        String consulta = "Select idcliente from cliente where idcliente='"+idcliente+"'";
        Statement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        ResultSet rs = sentencia.executeQuery(consulta);
        if(rs.next()){
            return true;
        }        
        rs.close();
        sentencia.close();
        return false;
    }
    public static void Cliente_a_Historico(String idcliente) throws SQLException {     
        String consulta ="Insert into historicocliente select * from cliente where idcliente='"+idcliente+"'";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        sentencia.executeUpdate(consulta); 
        sentencia.close();
        
    }
    public static void BorradoCliente(String idcliente) throws SQLException {
        String consulta ="Delete from cliente where idcliente=?";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        sentencia.setString(1, idcliente);
        sentencia.executeUpdate();
        sentencia.close();
    }
    
    
}
