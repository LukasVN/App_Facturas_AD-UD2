package modelo;

/**
 *
 * @author lucas.varelanegro
 */
public class Producto {
    String id_Producto;
    String nom_Producto;
    int stock_Producto;
    float precio_Producto;
    String cat_Producto;
    float iva_Producto;

    public Producto(String id_Producto, String nom_Producto, int stock_Producto, float precio_Producto, String cat_Producto, float iva_Producto) {
        this.id_Producto = id_Producto;
        this.nom_Producto = nom_Producto;
        this.stock_Producto = stock_Producto;
        this.precio_Producto = precio_Producto;
        this.cat_Producto = cat_Producto;
        this.iva_Producto = iva_Producto;
    }

    public String getId_Producto() {
        return id_Producto;
    }

    public String getNom_Producto() {
        return nom_Producto;
    }

    public int getStock_Producto() {
        return stock_Producto;
    }

    public float getPrecio_Producto() {
        return precio_Producto;
    }

    public String getCat_Producto() {
        return cat_Producto;
    }

    public float getIva_Producto() {
        return iva_Producto;
    }

    public void setId_Producto(String id_Producto) {
        this.id_Producto = id_Producto;
    }

    public void setNom_Producto(String nom_Producto) {
        this.nom_Producto = nom_Producto;
    }

    public void setStock_Producto(int stock_Producto) {
        this.stock_Producto = stock_Producto;
    }

    public void setPrecio_Producto(float precio_Producto) {
        this.precio_Producto = precio_Producto;
    }

    public void setCat_Producto(String cat_Producto) {
        this.cat_Producto = cat_Producto;
    }

    public void setIva_Producto(float iva_Producto) {
        this.iva_Producto = iva_Producto;
    }

    @Override
    public String toString() {
        return this.getNom_Producto();
    }
    
}
