/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author galih
 */
public class DBUtils {
    Connection con;
    Statement stat;
    PreparedStatement ps;
    ResultSet rs;  
    
      public Connection getKoneksi(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/db_simrt", "root", "");
            stat = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal melakukan koneksi database", "Error", JOptionPane.ERROR_MESSAGE);
           
        }
        return con;
    }
    
      public void FileEmailWarga(){
          try{
           FileOutputStream out = new FileOutputStream("C:/tmp/emailwarga.txt");
            PrintStream p = new PrintStream(out);
            p.print("0");
            p.close();
        }
        catch (Exception e)
        { System.out.println("Kesalahan : "+e.getMessage());}
      }
      
      public void FileIuranWarga(){
          try{
           FileOutputStream out = new FileOutputStream("C:/tmp/iuranwarga.txt");
            PrintStream p = new PrintStream(out);
            p.print("0");
            p.close();
        }
        catch (Exception e)
        { System.out.println("Kesalahan : "+e.getMessage());}
      }
      
      public void FileSuratWarga(){
          try{
           FileOutputStream out = new FileOutputStream("C:/tmp/suratwarga.txt");
            PrintStream p = new PrintStream(out);
            p.print("0");
            p.close();
        }
        catch (Exception e)
        { System.out.println("Kesalahan : "+e.getMessage());}
      }
      
      public void FileLaporanWarga(){
          try{
           FileOutputStream out = new FileOutputStream("C:/tmp/laporanwarga.txt");
            PrintStream p = new PrintStream(out);
            p.print("0");
            p.close();
        }
        catch (Exception e)
        { System.out.println("Kesalahan : "+e.getMessage());}
      }
      
      
      
            public static void main(String[] args){
                new DBUtils().getKoneksi();
            }
}
