/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.DBUtils;
import util.NotificationUtils;

/**
 *
 * @author galih
 */
public class FormTrxSuratWarga extends javax.swing.JDialog {

    /**
     * Creates new form FormDaftarUser
     */
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    String pattern = "ddMMyy";
    DateFormat df = new SimpleDateFormat(pattern);
    String fileName = "C:/tmp/laporanwarga.txt" ; 
    File f = new File(fileName);
    
    static private String selectedString(ItemSelectable is) {
      Object selected[] = is.getSelectedObjects();
      return ((selected.length == 0) ? "null" : (String) selected[0]);
    }
    
    public FormTrxSuratWarga(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        jenisSuratField.setEditable(false);
        jenisSuratField.setBackground(Color.LIGHT_GRAY);
        keteranganField.setEditable(false);
        keteranganField.setBackground(Color.LIGHT_GRAY);
        namaField.setEditable(false);
        namaField.setBackground(Color.LIGHT_GRAY);
        lakiRb.setEnabled(false);
        perempuanRb.setEnabled(false);
        alamatField.setEditable(false);
        alamatField.setBackground(Color.LIGHT_GRAY);
        teleponField.setEditable(false);
        teleponField.setBackground(Color.LIGHT_GRAY);
        emailField.setEditable(false);
        emailField.setBackground(Color.LIGHT_GRAY);
        
        showTable();
        if(!f.exists()){new util.DBUtils().FileLaporanWarga();}
        ReadNoSuratWarga();
        try {
            displayIdWarga();
            displayKodeSurat();
        } catch (SQLException ex) {
            Logger.getLogger(FormTrxSuratWarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ItemListener itemListener = new ItemListener() {
           
      public void itemStateChanged(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();
        System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
        System.out.println("Item: " + itemEvent.getItem());
        ItemSelectable is = itemEvent.getItemSelectable();
        System.out.println(", Selected: " + selectedString(is));
        
          try {
              displayDetailWarga(selectedString(is));
              displayDetailSurat(selectedString(is));
          } catch (SQLException ex) {
              Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    };
       idWargaCB.addItemListener(itemListener);
       kodeSuratCB.addItemListener(itemListener);
    }
    
    public void showTable(){        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("Nomor Surat"); 
        model.addColumn("Jenis Surat");
        model.addColumn("ID Warga");
        model.addColumn("Nama");
        model.addColumn("Jenis Kelamin");
        
        dataTable.setModel(model);
        
        String sql = "SELECT * FROM form_surat ";
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getString("nosurat"), 
                    rs.getString("jenissurat"), 
                    rs.getString("idwarga"),
                    rs.getString("nama"),
                    rs.getString("jeniskelamin").equals("L")?"Laki-Laki":"Perempuan",
                    } 
                );
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
     
//        jumlahLabel.setText(String.valueOf(dataTable.getRowCount()));
        model.fireTableDataChanged();
        
//        dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
        
        dataTable.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent event) {
                Point point = event.getPoint();
                int currentRow = dataTable.rowAtPoint(point);
                System.out.println("currentRow == "+currentRow);
                
                String nomorSurat = (String)dataTable.getValueAt(currentRow, 0);
                detailTrxSurat(nomorSurat);
                
                dataTable.setRowSelectionInterval(currentRow, currentRow);
            }
        });
    }
        
    public void detailTrxSurat(String nomorSurat){
        String sql = "SELECT * FROM form_surat where nosurat = ?";
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, nomorSurat);
            rs = ps.executeQuery();
            
            while (rs.next()){                
               Date tglSurat = rs.getDate("tglsurat");
               String kodeSurat = rs.getString("kdsurat");
               String jenisSurat = rs.getString("jenissurat");
               String ketSurat = rs.getString("ketsurat");
               String idWarga = rs.getString("idwarga");
               String nama = rs.getString("nama");
               String jenisKelamin = rs.getString("jeniskelamin");
               String alamat = rs.getString("alamat");
               String telepon = rs.getString("telepon");
               String email = rs.getString("email");
               
               tglSuratField.setDate(tglSurat);
               kodeSuratCB.setSelectedItem(kodeSurat);
               jenisSuratField.setText(jenisSurat);
               keteranganField.setText(ketSurat);
               idWargaCB.setSelectedItem(idWarga);
               namaField.setText(nama);
               if("L".equals(jenisKelamin)){
                   lakiRb.setSelected(true);
                   perempuanRb.setSelected(false);
               }else{
                   lakiRb.setSelected(false);
                   perempuanRb.setSelected(true);
               }
               alamatField.setText(alamat);
               teleponField.setText(telepon);
               emailField.setText(email);
               noSuratField.setText(nomorSurat);
               
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void tambahRecord(){
        String sql = "INSERT INTO form_surat(nosurat, tglsurat, kdsurat, jenissurat, ketsurat, idwarga, nama, jeniskelamin, alamat, telepon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, noSuratField.getText());
            ps.setDate(2, new Date(tglSuratField.getDate().getTime()));
            ps.setString(3, kodeSuratCB.getSelectedItem().toString());
            ps.setString(4, jenisSuratField.getText());
            ps.setString(5, keteranganField.getText());
            ps.setString(6, idWargaCB.getSelectedItem().toString());
            ps.setString(7, namaField.getText());
            ps.setString(8, lakiRb.isSelected()?"L":"P");
            ps.setString(9, alamatField.getText());
            ps.setString(10, teleponField.getText());
            ps.setString(11, emailField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            displayNoSuratWarga();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal ditambahkan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateRecord(){
        String sql = "UPDATE form_surat SET tglsurat = ?, kdsurat = ?, jenissurat = ?, ketsurat = ?, idwarga = ?, nama = ?, jeniskelamin = ?, alamat = ?, telepon = ?, email = ? where nosurat = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);            
            
            ps.setDate(1, new Date(tglSuratField.getDate().getTime()));
            ps.setString(2, kodeSuratCB.getSelectedItem().toString());
            ps.setString(3, jenisSuratField.getText());
            ps.setString(4, keteranganField.getText());
            ps.setString(5, idWargaCB.getSelectedItem().toString());
            ps.setString(6, namaField.getText());
            ps.setString(7, lakiRb.isSelected()?"L":"P");
            ps.setString(8, alamatField.getText());
            ps.setString(9, teleponField.getText());
            ps.setString(10, emailField.getText());
            ps.setString(11, noSuratField.getText());
            
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal diubah", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusRecord(String kode){
        String sql = "DELETE FROM form_surat WHERE nosurat = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, kode);
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal dihapus\n"+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        showTable();
    }
    
    public void ReadNoSuratWarga(){
        int data = 0, jmh = 0;
        
        try {
            // membaca file
            File myFile = new File(fileName);
            Scanner fileReader = new Scanner(myFile);
            
            // cetak isi file
            while(fileReader.hasNextLine()){
                data = fileReader.nextInt();
                jmh = 1 + data;
            }
            
            noSuratField.setText("SW00"+jmh+df.format(new java.util.Date()));
        } catch (FileNotFoundException e) {
            System.out.println("Terjadi Kesalahan: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public void displayNoSuratWarga(){
        int data = 0, jmh = 0;
        try {
            // membaca file
            File myFile = new File(fileName);
            Scanner fileReader = new Scanner(myFile);
            
            // cetak isi file
            while(fileReader.hasNextLine()){
                data = fileReader.nextInt();
                jmh = 1 + data;
            }
            FileOutputStream out = new FileOutputStream(fileName);
            PrintStream p = new PrintStream(out);
            p.print(jmh);
            
            ReadNoSuratWarga();;
        } catch (FileNotFoundException e) {
            System.out.println("Terjadi Kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    
    }
    
    
    public void displayIdWarga() throws SQLException{
        idWargaCB.removeAllItems();
        String sql = "SELECT * FROM tb_daftarwarga ";
        con = new DBUtils().getKoneksi();
        
        idWargaCB.addItem("-- Pilih --");
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                System.out.println(rs.getString("idwarga"));
                idWargaCB.addItem(rs.getString("idwarga"));       
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
        
        System.out.println("jumlah warga === " + idWargaCB.getItemCount());
    }
    
    public void displayDetailWarga(String idWarga) throws SQLException{        
        String sql = "SELECT * FROM tb_daftarwarga where idwarga = ?";
        con = new DBUtils().getKoneksi();               
        
        try {
            ps = con.prepareStatement(sql);             
            ps.setString(1, idWarga);
            rs = ps.executeQuery();
            
            while (rs.next()){
                namaField.setText(rs.getString("nama"));
                alamatField.setText(rs.getString("alamat"));
                teleponField.setText(rs.getString("telepon"));
                emailField.setText(rs.getString("email"));
                
                String jenisKelamin = rs.getString("jeniskelamin");
                if("L".equals(jenisKelamin)){
                    lakiRb.setSelected(true);
                    perempuanRb.setSelected(false);
                }else{
                    lakiRb.setSelected(false);
                    perempuanRb.setSelected(true);
                }
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
    }
    
    public void displayKodeSurat() throws SQLException{
        kodeSuratCB.removeAllItems();
        String sql = "SELECT * FROM tb_daftarsurat ";
        con = new DBUtils().getKoneksi();
        
        kodeSuratCB.addItem("-- Pilih --");
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                kodeSuratCB.addItem(rs.getString("kdsurat"));       
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
        
    }
    
    public void displayDetailSurat(String kodeSurat) throws SQLException{        
        String sql = "SELECT * FROM tb_daftarsurat where kdsurat = ?";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            ps.setString(1, kodeSurat);
            rs = ps.executeQuery();
            
            while (rs.next()){
                jenisSuratField.setText(rs.getString("jenissurat"));
                keteranganField.setText(rs.getString("ketsurat"));                
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
    }
    
    public void kirim()throws FileNotFoundException{    
        Map data = new HashMap();
        data.put("to", emailField.getText());
        data.put("nosurat", noSuratField.getText());
        data.put("idwarga", idWargaCB.getSelectedItem().toString());
        data.put("nama", namaField.getText());
        data.put("jenissurat", jenisSuratField.getText());

        
       File is = new File(cetakdankirim());
        data.put("attachment", is);
        
        new NotificationUtils().sentSuratWarga(data);
        JOptionPane.showMessageDialog(null, "Berhasil Terkirim", "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void clearForm(){
        ReadNoSuratWarga(); 
        keteranganField.setText("");
        jenisSuratField.setText("");
        keteranganField.setText("");
        alamatField.setText("");
        teleponField.setText("");
        emailField.setText("");
    }
    
    public void cetak(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
               int i = dataTable.getSelectedRow();
        
         if(i >= 0){  
             try {
            URL url = getClass().getResource("/report/rpt_surat_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            
            param.put("nosurat", noSuratField.getText());
            
            InputStream logo = new FileInputStream(new File("src/image/kab_tangerang_footer_transparan.png"));
            param.put("logo", logo);

            InputStream logo2 = new FileInputStream(new File("src/image/log_perumahan_kop_transparan.png"));
            param.put("logo2", logo2);

            InputStream tdTangan = new FileInputStream(new File("src/image/td_tangan.png"));
            param.put("tandaTangan", tdTangan);

            java.util.Date today = new java.util.Date();            
            param.put("printDate", new SimpleDateFormat("dd/MM/yyyy").format(today));

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, new DBUtils().getKoneksi());

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);          
            
            JDialog dialog = new JDialog(this);
            dialog.setContentPane(jasperViewer.getContentPane());
            dialog.setSize(jasperViewer.getSize());
            dialog.setTitle("Surat Warga");
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
         }else{
             JOptionPane.showMessageDialog(null, "Anda belum memilih", "Informasi", JOptionPane.INFORMATION_MESSAGE);
         
         }
    }

    public String cetakdankirim(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        int i = dataTable.getSelectedRow();
        File dir = new File("C:/tmp/pdf/");
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_surat_warga.pdf";
         if(i >= 0){  
             
         try {
            URL url = getClass().getResource("/report/rpt_surat_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            param.put("nosurat", noSuratField.getText());
            
            InputStream logo = new FileInputStream(new File("src/image/kab_tangerang_footer_transparan.png"));
            param.put("logo", logo);
            
            InputStream logo2 = new FileInputStream(new File("src/image/log_perumahan_kop_transparan.png"));
            param.put("logo2", logo2);
                        
            InputStream tdTangan = new FileInputStream(new File("src/image/td_tangan.png"));
            param.put("tandaTangan", tdTangan);

            java.util.Date today = new java.util.Date();            
            param.put("printDate", new SimpleDateFormat("dd/MM/yyyy").format(today));


            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, param, new DBUtils().getKoneksi());

            //JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);    
            JasperExportManager jem = new JasperExportManager();
            JasperExportManager.exportReportToPdfFile(jasperPrint, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
         }else{
             JOptionPane.showMessageDialog(null, "Anda belum memilih", "Informasi", JOptionPane.INFORMATION_MESSAGE);
         }
        return FILE_NAME;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        noSuratField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        kodeSuratCB = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jenisSuratField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        keteranganField = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        tglSuratField = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        idWargaCB = new javax.swing.JComboBox<>();
        namaField = new javax.swing.JTextField();
        lakiRb = new javax.swing.JRadioButton();
        perempuanRb = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        alamatField = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        teleponField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-new-message-48.png"))); // NOI18N
        jLabel1.setText("SURAT WARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(371, 371, 371)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Tanggal Surat");

        noSuratField.setEnabled(false);

        jLabel3.setText("Nomor Surat");

        kodeSuratCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeSuratCBActionPerformed(evt);
            }
        });

        jLabel4.setText("Kode Surat");

        jLabel5.setText("Jenis Surat");

        keteranganField.setColumns(20);
        keteranganField.setRows(5);
        jScrollPane2.setViewportView(keteranganField);

        jLabel6.setText("Keterangan");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5)
                        .addComponent(jLabel3)))
                .addGap(66, 66, 66)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addComponent(jenisSuratField)
                    .addComponent(kodeSuratCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(noSuratField)
                    .addComponent(tglSuratField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tglSuratField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noSuratField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kodeSuratCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jenisSuratField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jenisSuratField, kodeSuratCB, noSuratField, tglSuratField});

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setBackground(new java.awt.Color(255, 153, 0));
        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 153, 0));
        jButton2.setText("Ubah");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 153, 0));
        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 153, 0));
        jButton4.setText("Keluar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 153, 0));
        jButton5.setText("Cetak");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 153, 0));
        jButton6.setText("Kirim");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 153, 0));
        jButton7.setText("Bersihkan");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jButton3, jButton4, jButton5, jButton6});

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(dataTable);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("ID Warga");

        jLabel8.setText("Nama");

        jLabel9.setText("Jenis Kelamin");

        lakiRb.setText("Laki Laki");
        lakiRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lakiRbActionPerformed(evt);
            }
        });

        perempuanRb.setText("Perempuan");
        perempuanRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                perempuanRbActionPerformed(evt);
            }
        });

        alamatField.setColumns(20);
        alamatField.setRows(5);
        jScrollPane3.setViewportView(alamatField);

        jLabel11.setText("Alamat");

        jLabel12.setText("Telepon");

        jLabel13.setText("Email");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(66, 66, 66)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(teleponField)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lakiRb)
                        .addGap(18, 18, 18)
                        .addComponent(perempuanRb))
                    .addComponent(namaField)
                    .addComponent(emailField)
                    .addComponent(idWargaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idWargaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lakiRb)
                        .addComponent(perempuanRb))
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(teleponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("DETAIL TABEL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(448, 448, 448))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void kodeSuratCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeSuratCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kodeSuratCBActionPerformed

    private void lakiRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lakiRbActionPerformed
        lakiRb.setSelected(true);
        perempuanRb.setSelected(false);
    }//GEN-LAST:event_lakiRbActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(tglSuratField.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal Surat wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(noSuratField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nomor Surat wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(idWargaCB.getSelectedItem().equals("-- Pilih --")){
            JOptionPane.showMessageDialog(null, "Id Warga wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tambahRecord();
        showTable();

        clearForm();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silahkan pilih Data yang akan diubah", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateRecord();
        clearForm();
        showTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silahkan pilih Data yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int i = dataTable.getSelectedRow();
        String nomorEmail = (String) dataTable.getValueAt(i, 0);
        System.out.println("nomorEmail == "+nomorEmail);
        
        int pilih = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if(pilih == JOptionPane.OK_OPTION){
            hapusRecord(nomorEmail);
            clearForm();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        cetak();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void perempuanRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perempuanRbActionPerformed
        lakiRb.setSelected(false);
        perempuanRb.setSelected(true);
    }//GEN-LAST:event_perempuanRbActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       clearForm();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            kirim();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormTrxSuratWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTrxSuratWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTrxSuratWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTrxSuratWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTrxSuratWarga(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea alamatField;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField emailField;
    private javax.swing.JComboBox<String> idWargaCB;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jenisSuratField;
    private javax.swing.JTextArea keteranganField;
    private javax.swing.JComboBox<String> kodeSuratCB;
    private javax.swing.JRadioButton lakiRb;
    private javax.swing.JTextField namaField;
    private javax.swing.JTextField noSuratField;
    private javax.swing.JRadioButton perempuanRb;
    private javax.swing.JTextField teleponField;
    private com.toedter.calendar.JDateChooser tglSuratField;
    // End of variables declaration//GEN-END:variables
}
