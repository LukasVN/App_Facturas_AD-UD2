package modelo;

import java.time.LocalDate;

/**
 *
 * @author lucas.varelanegro
 */
public class Factura {
    String num_Factura;
    String id_ClienteF;
    String id_EmpleadoF;
    LocalDate fechaF;
    Boolean cobradaF;
    Float ivaF;

    public Factura(String num_Factura, String id_ClienteF, String id_EmpleadoF, LocalDate fechaF, Boolean cobradaF, Float ivaF) {
        this.num_Factura = num_Factura;
        this.id_ClienteF = id_ClienteF;
        this.id_EmpleadoF = id_EmpleadoF;
        this.fechaF = fechaF;
        this.cobradaF = cobradaF;
        this.ivaF = ivaF;
    }

    public String getNum_Factura() {
        return num_Factura;
    }

    public String getId_ClienteF() {
        return id_ClienteF;
    }

    public String getId_EmpleadoF() {
        return id_EmpleadoF;
    }

    public LocalDate getFechaF() {
        return fechaF;
    }

    public Boolean getCobradaF() {
        return cobradaF;
    }

    public Float getIvaF() {
        return ivaF;
    }

    public void setNum_Factura(String num_Factura) {
        this.num_Factura = num_Factura;
    }

    public void setId_ClienteF(String id_ClienteF) {
        this.id_ClienteF = id_ClienteF;
    }

    public void setId_EmpleadoF(String id_EmpleadoF) {
        this.id_EmpleadoF = id_EmpleadoF;
    }

    public void setFechaF(LocalDate fechaF) {
        this.fechaF = fechaF;
    }

    public void setCobradaF(Boolean cobradaF) {
        this.cobradaF = cobradaF;
    }

    public void setIvaF(Float ivaF) {
        this.ivaF = ivaF;
    }

    @Override
    public String toString() {
        return this.num_Factura;
    }
    
}
