package controlador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;

/**
 *
 * @author lucas.varelanegro
 */
public class GestionProducto {
    public static void cargarCombo(JComboBox<Producto> cmbProducto) throws SQLException {
        Producto prod;
        cmbProducto.removeAllItems();
        String consulta = "Select * from productos";
        ResultSet rs;
        cmbProducto.addItem(null); //Cargamos este nulo para que no se active el actionperformed del combo y de error
        try (Statement sentencia = Pool.getCurrentConexion().createStatement()) {
            rs = sentencia.executeQuery(consulta);
            while (rs.next()) {
                prod = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3),rs.getFloat(4),rs.getString(5),rs.getFloat(6));
                cmbProducto.addItem(prod);
            }
        }
        rs.close();
        cmbProducto.removeItemAt(0); //Borramos ahora el null. Para que quede solo con los datos
    }
    public static void cargarTablaProducto(DefaultTableModel tbl_Productos) throws SQLException {
        Producto prod;
        String consulta = "Select * from productos";
        Statement sentencia = Pool.getCurrentConexion().createStatement();

        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            prod = new Producto(rs.getString(1), rs.getString(2), rs.getInt(3),rs.getFloat(4),rs.getString(5),rs.getFloat(6));
            tbl_Productos.setRowCount(tbl_Productos.getRowCount()+1);
            tbl_Productos.setValueAt(prod.getId_Producto(), tbl_Productos.getRowCount() -1, 0);
            tbl_Productos.setValueAt(prod.getNom_Producto(), tbl_Productos.getRowCount() -1, 1);
            tbl_Productos.setValueAt(prod.getStock_Producto(), tbl_Productos.getRowCount() -1, 2);
            tbl_Productos.setValueAt(prod.getPrecio_Producto(), tbl_Productos.getRowCount() -1, 3);
            tbl_Productos.setValueAt(prod.getCat_Producto(), tbl_Productos.getRowCount() -1, 4);
            tbl_Productos.setValueAt(prod.getIva_Producto(), tbl_Productos.getRowCount() -1, 5);
        }
        sentencia.close();
        rs.close();     
    }
    public static void CargarFilaProducto(DefaultTableModel tbl_Productos, String id_Prod, String nom_Prod, int stock_Prod, float precio_Prod, String cat_Prod, float iva_Prod) {
        tbl_Productos.setRowCount(tbl_Productos.getRowCount()+1);
            tbl_Productos.setValueAt(id_Prod, tbl_Productos.getRowCount() -1, 0);
            tbl_Productos.setValueAt(nom_Prod, tbl_Productos.getRowCount() -1, 1);
            tbl_Productos.setValueAt(stock_Prod, tbl_Productos.getRowCount() -1, 2);
            tbl_Productos.setValueAt(precio_Prod, tbl_Productos.getRowCount() -1, 3);
            tbl_Productos.setValueAt(cat_Prod, tbl_Productos.getRowCount() -1, 4);
            tbl_Productos.setValueAt(iva_Prod/100, tbl_Productos.getRowCount() -1, 5);
    }
    public static void CargarFilaFacturaProd(DefaultTableModel tbl_ProductosFact, String nom_Prod, int stock) throws SQLException {
        String consulta = "Select * from productos where nomproducto="+"'"+nom_Prod+"'";
        Statement sentencia = Pool.getCurrentConexion().createStatement();

        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            tbl_ProductosFact.setRowCount(tbl_ProductosFact.getRowCount()+1);
            tbl_ProductosFact.setValueAt(nom_Prod, tbl_ProductosFact.getRowCount() -1, 0);
            tbl_ProductosFact.setValueAt(stock, tbl_ProductosFact.getRowCount() -1, 1);
            tbl_ProductosFact.setValueAt(getPrecioProducto(nom_Prod), tbl_ProductosFact.getRowCount() -1, 2);
        }
        sentencia.close();
        rs.close();     
    }
    
    public static boolean ComprobarNombreTBL(DefaultTableModel tbl_ProductosFact,String nombreProd, int stock) throws SQLException {
        for(int i=0; i<tbl_ProductosFact.getRowCount();i++){
            if(nombreProd.equals(tbl_ProductosFact.getValueAt(i, 0))){
                int aux_cant = Integer.parseInt(tbl_ProductosFact.getValueAt(i, 1).toString());
                tbl_ProductosFact.setValueAt(aux_cant+stock, i, 1);
                tbl_ProductosFact.setValueAt(getPrecioProducto(nombreProd)*(stock+aux_cant), i, 2);

                return true;
            }
        }        
        return false;        
    }
    public static float getPrecioProducto(String nomProducto) throws SQLException {
        float precio = (float) 0;
        String consulta = "Select precio from productos where nomproducto="+"'"+nomProducto+"'";
        Statement sentencia = Pool.getCurrentConexion().createStatement();

        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            precio = rs.getFloat(1);

        }
        sentencia.close();
        rs.close();  
        return precio;
    }

    public static boolean ComprobacionProducto(String id_producto) throws SQLException {     
        boolean enFactura = false;
        String consulta ="Select prod.idproducto from productos prod, detalle det where prod.idproducto = det.idproducto and prod.idproducto='"+id_producto+"' group by prod.idproducto";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        ResultSet rs = sentencia.executeQuery(consulta);
        if(rs.next()){        
        enFactura = true;
        }
        
        sentencia.close();
        rs.close(); 
        return enFactura;
    }
    public static void ModificarTablaProductos(DefaultTableModel tblProductos) throws SQLException {       
        for(int i=0;i<tblProductos.getRowCount();i++){
            String consulta ="Update productos set stock="+tblProductos.getValueAt(i, 2)+",precio="+tblProductos.getValueAt(i, 3)
                    +",categoria='"+tblProductos.getValueAt(i, 4)+"',iva="+tblProductos.getValueAt(i, 5)+" where idproducto='"+tblProductos.getValueAt(i, 0)+"'";
            Statement sentencia = Pool.getCurrentConexion().createStatement();
            sentencia.executeUpdate(consulta);
        }
    }
    
    
}
