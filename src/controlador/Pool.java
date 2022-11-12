package controlador;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class Pool {

    static Connection Con;
    static BasicDataSource basicdatasource = new BasicDataSource();
    private static final String BD = "practicaud2";//*******Indica la BD **************
    private static final String IP = "192.168.56.101";//*******Indica la IP ***************

    public static Connection IniciaPoolconFichero() throws SQLException {
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream("src//Controlador//connect.txt"));
            basicdatasource = (BasicDataSource) BasicDataSourceFactory.createDataSource(propiedades);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la conexión a la base de datos (Pool con Fichero)");
        }
        return null;
    }

    public static Connection IniciaPool() throws SQLException {
        try {
            basicdatasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            basicdatasource.setUsername("root");
            basicdatasource.setPassword("root");
            basicdatasource.setUrl("jdbc:mysql://" + IP + ":3306/" + BD);

            //comprobación de conexion
            basicdatasource.setValidationQuery("Select 1");
            basicdatasource.setMaxTotal(5);//conexiones del pool
            //basicdatasource.setInitialSize(5); //Cuando el pool comienza el número mínimo de conexiones
            basicdatasource.setMinIdle(50); //Numero minimo de conexiones inactivas que queremos que haya
            basicdatasource.setMaxIdle(100); //Numero máximo de conexciones inactivas que queremos que haya
            basicdatasource.setMaxWaitMillis(1000); //Tiempo que espera en las conexiones.

            //Si queremos comprobar que funciona bastaría con hacer un getConexion
            //Connection con=basicdatasource.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la conexión a la base de datos (Pool)");
        }
        return null;
    }

    public static Connection getConexion() throws SQLException {
      System.out.println("Conexiones activas Antes" + basicdatasource.getNumActive());
        Con = basicdatasource.getConnection();
        Con.setAutoCommit(false);
        System.out.println("Conexiones activas Despues" + basicdatasource.getNumActive());
        return Con;
    }
    
    public static Connection getCurrentConexion() throws SQLException {
        if (Con == null || Con.isClosed()) {
            getConexion();
        }
        return Con;
    }

    public static void Cerrar() {
        try {
            if (Con!=null)
                Con.close();   
        } catch (SQLException ex) {
            Logger.getLogger(Pool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
