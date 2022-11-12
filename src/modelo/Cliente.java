package modelo;
/**
 *
 * @author lucas.varelanegro
 */
public class Cliente {
    String id_Cliente;
    String nom_Cliente;
    String ap_Cliente;
    String dir_Cliente;

    public Cliente(String id_Cliente, String nom_Cliente, String ap_Cliente, String dir_Cliente) {
        this.id_Cliente = id_Cliente;
        this.nom_Cliente = nom_Cliente;
        this.ap_Cliente = ap_Cliente;
        this.dir_Cliente = dir_Cliente;
    }

    public String getId_Cliente() {
        return id_Cliente;
    }

    public String getNom_Cliente() {
        return nom_Cliente;
    }

    public String getAp_Cliente() {
        return ap_Cliente;
    }

    public String getDir_Cliente() {
        return dir_Cliente;
    }

    public void setId_Cliente(String id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public void setNom_Cliente(String nom_Cliente) {
        this.nom_Cliente = nom_Cliente;
    }

    public void setAp_Cliente(String ap_Cliente) {
        this.ap_Cliente = ap_Cliente;
    }

    public void setDir_Cliente(String dir_Cliente) {
        this.dir_Cliente = dir_Cliente;
    }

    @Override
    public String toString() {
        return this.getId_Cliente();
    }
    
}
