/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import controlador.Pool;

/**
 *
 * @author AD
 */
public class Controladorprocs {
         
public static ArrayList <String> procsinparametros() throws SQLException {
    
      Connection conexion=Pool.getCurrentConexion();
       
      ArrayList <String> listado=new ArrayList<>();
      String consulta="{call ejemplo.primerprocshowtables}"; //MostrarTablas
      ResultSet rs=null;
      
      try (CallableStatement proc = conexion.prepareCall(consulta)){         
            rs=proc.executeQuery();
            while (rs.next())
                  listado.add(rs.getString(1));
      
      }   
      catch (Exception e) {          
          e.printStackTrace();
      }
     finally{
          rs.close();
          Pool.Cerrar();
      }
      return listado;
}
public static ArrayList <String> procsoloparamentradanosalida(double sal,String loc) throws SQLException {
    
      Connection conexion=Pool.getCurrentConexion();

      ArrayList <String> listado=new ArrayList<>();
      String consulta="{call ejemplo.secondproc(?,?)}"; 
      ResultSet rs=null;
  
      try (CallableStatement proc = conexion.prepareCall(consulta)){         
          
            proc.setDouble(1, sal);
            proc.setString(2, loc);
            rs=proc.executeQuery();
            
            
            while (rs.next())
                  listado.add(rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getString(3));
      }   
      catch (Exception e) {          
          e.getMessage();
      }
     finally{
          rs.close();
          Pool.Cerrar();
      }
      return listado;
}
public static int procconparamentradayunasalida(double sal,String loc) throws SQLException {
    
      Connection conexion=Pool.getCurrentConexion();

      int resultado=0;
      String consulta="{call ejemplo.tercerproc(?,?,?)}"; 
  
      try (CallableStatement proc = conexion.prepareCall(consulta)){         
          
            proc.setDouble(1, sal);
            proc.setString(2, loc);
            proc.registerOutParameter(3, Types.INTEGER);
         
            proc.executeUpdate();
        
            resultado=proc.getInt(3);   
            
      }   
      catch (Exception e) {          
          e.getMessage();
      }
     finally{
        Pool.Cerrar();
      }
      return resultado;
}
public static Object[] procconparamentradayvariassalidas(double sal,String loc) throws SQLException {
    
      Connection conexion=Pool.getCurrentConexion();

      int cuenta=0;
      String cadena="";
      
      String consulta="{call ejemplo.cuartoproc(?,?,?)}"; 
  
      try (CallableStatement proc = conexion.prepareCall(consulta)){         
          
            proc.setDouble(1, sal);
            proc.setString(2, loc);
            proc.registerOutParameter(3, Types.INTEGER);
         
            proc.executeUpdate();
        
            cuenta=proc.getInt(3);   
            
           ResultSet rs=proc.getResultSet();
          
           while (rs.next()) 
               cadena+=rs.getInt(1)+"-"+rs.getString(2)+"-"+rs.getString(3)+"\n";   
           
      }   
      catch (Exception e) {          
          e.getMessage();
      }
     finally{
          Pool.Cerrar();
      }
      return new Object[] {cuenta,cadena};
}
public static Object[] procconparamentradayvariassalidas2(double sal,String loc) throws SQLException {
    
      Connection conexion=Pool.getCurrentConexion();

      int cuenta=0;
      String cadena="";
      
      String consulta="{call ejemplo.quintoproc(?,?,?)}"; 
  
      try (CallableStatement proc = conexion.prepareCall(consulta)){         
          
            proc.setDouble(1, sal);
            proc.setString(2, loc);
            
            proc.executeUpdate();
        
            cuenta=proc.getInt(3);               
            
            ResultSet rs=proc.getResultSet();
            
            if (rs==null)
                cadena="no hay mas de 3 empleados";
            else while (rs.next()) 
                cadena+=rs.getInt(1)+"-"+rs.getString(2)+"-"+rs.getString(3)+"\n";    
      }   
      catch (Exception e) {          
          e.getMessage();
      }
     finally{
          Pool.Cerrar();
      }
      return new Object[] {cuenta,cadena};
}
}