package controlador;

import java.sql.*;
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
    public static void CargarFilaFacturaProd(DefaultTableModel tbl_ProductosFact, String nom_Prod, int stock,Producto p) throws SQLException {
        String consulta = "Select * from productos where nomproducto="+"'"+nom_Prod+"'";
        Statement sentencia = Pool.getCurrentConexion().createStatement();

        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            tbl_ProductosFact.setRowCount(tbl_ProductosFact.getRowCount()+1);
            tbl_ProductosFact.setValueAt(nom_Prod, tbl_ProductosFact.getRowCount() -1, 0);
            tbl_ProductosFact.setValueAt(stock, tbl_ProductosFact.getRowCount() -1, 1);
            tbl_ProductosFact.setValueAt(getPrecioProducto(nom_Prod), tbl_ProductosFact.getRowCount() -1, 2);
            p.setStock_Producto(getStockProducto(p)-stock);
        }
        sentencia.close();
        rs.close();     
    }
    
    public static boolean ComprobarNombreTBL(DefaultTableModel tbl_ProductosFact,String nombreProd, int stock) throws SQLException {
        //(aux_cant+stock)<=getPrecioProducto(nombreProd)
        for(int i=0; i<tbl_ProductosFact.getRowCount();i++){
            int aux_cant = Integer.parseInt(tbl_ProductosFact.getValueAt(i, 1).toString());
            if(nombreProd.equals(tbl_ProductosFact.getValueAt(i, 0))){           
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
    public static int getStockProducto(Producto p) throws SQLException {
        int stock = p.getStock_Producto();      
        return stock;
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
    public static void InsertarProductoBD(DefaultTableModel tblProductos) throws SQLException {
        String consulta = "Insert into productos values ('"+tblProductos.getValueAt(tblProductos.getRowCount()-1, 0)+
                "','"+tblProductos.getValueAt(tblProductos.getRowCount()-1, 1)+
                "',"+tblProductos.getValueAt(tblProductos.getRowCount()-1, 2)+
                ","+tblProductos.getValueAt(tblProductos.getRowCount()-1, 3)+
                ",'"+tblProductos.getValueAt(tblProductos.getRowCount()-1, 4)+
                "',"+tblProductos.getValueAt(tblProductos.getRowCount()-1, 5)+")";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        sentencia.executeUpdate(consulta);
    }
    public static void BorrarProductoBD(DefaultTableModel model_Productos, int fila) throws SQLException {
        String consulta = "delete from productos where idproducto='"+model_Productos.getValueAt(fila, 0)+"'";
        Statement sentencia = Pool.getCurrentConexion().createStatement();
        sentencia.executeUpdate(consulta);
    }
    public static boolean ComprobarStockTabla(String nomproducto, DefaultTableModel model_productosF, int stock) throws SQLException {
        for(int i=0;i<model_productosF.getRowCount();i++){
            int newstock= stock+Integer.parseInt(model_productosF.getValueAt(i, 1).toString());
            if(model_productosF.getValueAt(i, 0) == nomproducto && newstock<=getPrecioProducto(nomproducto)){
                return true;
            }
            
        }
        return false;
    }

    public static void ActualizarStock(DefaultTableModel tblProductoF, JComboBox cmbProd) throws SQLException {
        String consulta = "Update productos set stock=? where nomproducto=?";
        PreparedStatement sentencia = Pool.getCurrentConexion().prepareStatement(consulta);
        for(int i=0;i<tblProductoF.getRowCount();i++){
            Producto p= (Producto)cmbProd.getItemAt(i);
            if(p.getNom_Producto()==tblProductoF.getValueAt(i, 0)){
                sentencia.setInt(1, p.getStock_Producto());
                sentencia.setString(2, p.getNom_Producto());
                sentencia.executeUpdate();
            }
        }
        sentencia.close();
        
        
    }
    public static String GetID_ProductoBD(String nomproducto) throws SQLException {
        String idproducto="";
        String consulta="Select idproducto from productos where nomproducto='"+nomproducto+"'";
        Statement sentencia = Pool.getCurrentConexion().createStatement();

        ResultSet rs = sentencia.executeQuery(consulta);
        
        while(rs.next()){
            idproducto = rs.getString(1);

        }
        sentencia.close();
        rs.close();  
        return idproducto;
    }
}
