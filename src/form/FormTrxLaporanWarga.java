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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
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
public class FormTrxLaporanWarga extends javax.swing.JDialog {

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
    
    public FormTrxLaporanWarga(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        //noLaporanField.setText(new DBUtils().generateNoLapWarga());
        jenisLaporanField.setEditable(false);
        jenisLaporanField.setBackground(Color.LIGHT_GRAY);
        keteranganField.setEditable(false);
        keteranganField.setBackground(Color.LIGHT_GRAY);
        keteranganField.setEditable(false);
        keteranganField.setBackground(Color.LIGHT_GRAY);
        
        namaField.setEditable(false);
        namaField.setBackground(Color.LIGHT_GRAY);
        lakiRb.setEnabled(false);
        perempuanRb.setEnabled(false);
        statusTinggalField.setEditable(false);
        statusTinggalField.setBackground(Color.LIGHT_GRAY);
        alamatField.setEditable(false);
        alamatField.setBackground(Color.LIGHT_GRAY);
        teleponField.setEditable(false);
        teleponField.setBackground(Color.LIGHT_GRAY);
        emailField.setEditable(false);
        emailField.setBackground(Color.LIGHT_GRAY);
        showTable();
        if(!f.exists()){new util.DBUtils().FileLaporanWarga();}
        ReadNoLaporanWarga();
        try {
            displayIdWarga();
            displayKodeLaporan();
        } catch (SQLException ex) {
            Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
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
              displayDetailLaporan(selectedString(is));
          } catch (SQLException ex) {
              Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    };
       idWargaCB.addItemListener(itemListener);
       kodeLaporanCB.addItemListener(itemListener);
        
        showTable();        

    }
    
    public void showTable(){        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("Nomor Laporan"); 
        model.addColumn("Jenis Laporan");
        model.addColumn("ID Warga");
        model.addColumn("Nama");
        model.addColumn("Jenis Kelamin");
        
        dataTable.setModel(model);
        
        String sql = "SELECT * FROM form_laporan ";
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getString("nolaporan"), 
                    rs.getString("tgllapor"), 
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
                
                String nomorLaporan = (String)dataTable.getValueAt(currentRow, 0);
                detailTrxLaporan(nomorLaporan);
                
                dataTable.setRowSelectionInterval(currentRow, currentRow);
            }
        });
    }
        
    public void detailTrxLaporan(String nomorLaporan){
        String sql = "SELECT * FROM form_laporan where nolaporan = ?";
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, nomorLaporan);
            rs = ps.executeQuery();
            
            while (rs.next()){                
               Date tglLaporan = rs.getDate("tgllapor");
               String kodeLaporan = rs.getString("kdlaporan");
               String jenisLaporan = rs.getString("jenislaporan");
               String ketLaporan = rs.getString("ketlaporan");
               String idWarga = rs.getString("idwarga");
               String nama = rs.getString("nama");
               String jenisKelamin = rs.getString("jeniskelamin");
               String statusTinggal = rs.getString("statustinggal");
               String alamat = rs.getString("alamat");
               String telepon = rs.getString("telepon");
               String email = rs.getString("email");
               
               tglLaporanField.setDate(tglLaporan);
               kodeLaporanCB.setSelectedItem(kodeLaporan);
               jenisLaporanField.setText(jenisLaporan);
               keteranganField.setText(ketLaporan);
               idWargaCB.setSelectedItem(idWarga);
               namaField.setText(nama);
               if("L".equals(jenisKelamin)){
                   lakiRb.setSelected(true);
                   perempuanRb.setSelected(false);
               }else{
                   lakiRb.setSelected(false);
                   perempuanRb.setSelected(true);
               }
               statusTinggalField.setText(statusTinggal);
               alamatField.setText(alamat);
               teleponField.setText(telepon);
               emailField.setText(email);
               noLaporanField.setText(nomorLaporan);
               
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void tambahRecord(){
        String sql = "INSERT INTO form_laporan(nolaporan, tgllapor, kdlaporan, jenislaporan, ketlaporan, idwarga, nama, jeniskelamin, statustinggal, alamat, telepon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, noLaporanField.getText());
            ps.setDate(2, new Date(tglLaporanField.getDate().getTime()));
            ps.setString(3, kodeLaporanCB.getSelectedItem().toString());
            ps.setString(4, jenisLaporanField.getText());
            ps.setString(5, keteranganField.getText());
            ps.setString(6, idWargaCB.getSelectedItem().toString());
            ps.setString(7, namaField.getText());
            ps.setString(8, lakiRb.isSelected()?"L":"P");
            ps.setString(9, statusTinggalField.getText());
            ps.setString(10, alamatField.getText());
            ps.setString(11, teleponField.getText());
            ps.setString(12, emailField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            displayNoLaporanWarga();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal ditambahkan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateRecord(){
        String sql = "UPDATE form_laporan SET tgllapor = ?, kdlaporan = ?, jenislaporan = ?, ketlaporan = ?, idwarga = ?, nama = ?, jeniskelamin = ?, statustinggal = ?, alamat = ?, telepon = ?, email = ? where nolaporan = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);            
            
            ps.setDate(1, new Date(tglLaporanField.getDate().getTime()));
            ps.setString(2, kodeLaporanCB.getSelectedItem().toString());
            ps.setString(3, jenisLaporanField.getText());
            ps.setString(4, keteranganField.getText());
            ps.setString(5, idWargaCB.getSelectedItem().toString());
            ps.setString(6, namaField.getText());
            ps.setString(7, lakiRb.isSelected()?"L":"P");
            ps.setString(8, statusTinggalField.getText());
            ps.setString(9, alamatField.getText());
            ps.setString(10, teleponField.getText());
            ps.setString(11, emailField.getText());
            ps.setString(12, noLaporanField.getText());
            
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal diubah", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusRecord(String kode){
        String sql = "DELETE FROM form_laporan WHERE nolaporan = ? ";
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
    
    public void ReadNoLaporanWarga(){
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
            
            noLaporanField.setText("LW00"+jmh+df.format(new java.util.Date()));
        } catch (FileNotFoundException e) {
            System.out.println("Terjadi Kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void displayNoLaporanWarga(){
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
            
            ReadNoLaporanWarga();;
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
                statusTinggalField.setText(rs.getString("statustinggal"));
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
    
    public void displayKodeLaporan() throws SQLException{
        kodeLaporanCB.removeAllItems();
        String sql = "SELECT * FROM tb_daftarlaporan ";
        con = new DBUtils().getKoneksi();
        
        kodeLaporanCB.addItem("-- Pilih --");
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();            
            while (rs.next()){
                kodeLaporanCB.addItem(rs.getString("kdlaporan"));       
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
    }
    
    public void displayDetailLaporan(String kodeLaporan) throws SQLException{        
        String sql = "SELECT * FROM tb_daftarlaporan where kdlaporan = ?";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            ps.setString(1, kodeLaporan);
            rs = ps.executeQuery();
            
            while (rs.next()){
                jenisLaporanField.setText(rs.getString("jenislaporan"));
                keteranganField.setText(rs.getString("ketlaporan"));                
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
    }
    
    public void cetak(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        int i = dataTable.getSelectedRow();
         if(i >= 0){  
             
         try {
            URL url = getClass().getResource("/report/rpt_laporan_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            param.put("nolaporan", noLaporanField.getText());
            
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
            dialog.setTitle("Laporan Rekap Warga");
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
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_laporan_warga.pdf";
         if(i >= 0){  
             
         try {
            URL url = getClass().getResource("/report/rpt_laporan_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            param.put("nolaporan", noLaporanField.getText());
            
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
    
    
    public void kirim()throws FileNotFoundException{    
        Map data = new HashMap();
        data.put("to", emailField.getText());
        data.put("nolaporan", noLaporanField.getText());
        data.put("idwarga", idWargaCB.getSelectedItem().toString());
        data.put("nama", namaField.getText());
        data.put("jenislaporan", jenisLaporanField.getText());

        
       File is = new File(cetakdankirim());
        data.put("attachment", is);
        
        new NotificationUtils().sentLaporanWarga(data);
        JOptionPane.showMessageDialog(null, "Berhasil Terkirim", "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public void clearForm(){
        ReadNoLaporanWarga();
        keteranganField.setText("");
        jenisLaporanField.setText("");
        keteranganField.setText("");
        statusTinggalField.setText("");
        alamatField.setText("");
        teleponField.setText("");
        emailField.setText("");
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
        noLaporanField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        kodeLaporanCB = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jenisLaporanField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        keteranganField = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        tglLaporanField = new com.toedter.calendar.JDateChooser();
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
        jLabel8 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        alamatField = new javax.swing.JTextArea();
        namaField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        idWargaCB = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        statusTinggalField = new javax.swing.JTextField();
        teleponField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        perempuanRb = new javax.swing.JRadioButton();
        lakiRb = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-notepad-48.png"))); // NOI18N
        jLabel1.setText("LAPORAN WARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(360, 360, 360)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Tanggal Laporan");

        noLaporanField.setEnabled(false);

        jLabel3.setText("Nomor Laporan");

        kodeLaporanCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeLaporanCBActionPerformed(evt);
            }
        });

        jLabel4.setText("Kode Laporan");

        jLabel5.setText("Jenis Laporan");

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
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(65, 65, 65)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jenisLaporanField, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(kodeLaporanCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noLaporanField)
                        .addComponent(tglLaporanField, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel2)
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tglLaporanField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noLaporanField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kodeLaporanCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jenisLaporanField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jenisLaporanField, kodeLaporanCB, noLaporanField, tglLaporanField});

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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton4))
                .addGap(42, 42, 42))
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

        jLabel8.setText("Nama");

        jLabel11.setText("Alamat");

        alamatField.setColumns(20);
        alamatField.setRows(5);
        jScrollPane3.setViewportView(alamatField);

        jLabel10.setText("Status Tinggal");

        jLabel12.setText("Telepon");

        idWargaCB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                idWargaCBItemStateChanged(evt);
            }
        });
        idWargaCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idWargaCBActionPerformed(evt);
            }
        });

        jLabel13.setText("Email");

        jLabel9.setText("Jenis Kelamin");

        perempuanRb.setText("Perempuan");
        perempuanRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                perempuanRbActionPerformed(evt);
            }
        });

        lakiRb.setText("Laki Laki");
        lakiRb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lakiRbActionPerformed(evt);
            }
        });

        jLabel7.setText("ID Warga");

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
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(65, 65, 65)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emailField)
                    .addComponent(teleponField)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lakiRb)
                        .addGap(18, 18, 18)
                        .addComponent(perempuanRb))
                    .addComponent(namaField)
                    .addComponent(idWargaCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusTinggalField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idWargaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lakiRb)
                        .addComponent(jLabel9))
                    .addComponent(perempuanRb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(statusTinggalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(teleponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel14.setText("DETAIL TABEL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(426, 426, 426))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void kodeLaporanCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeLaporanCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kodeLaporanCBActionPerformed

    private void lakiRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lakiRbActionPerformed
        lakiRb.setSelected(true);
        perempuanRb.setSelected(false);
    }//GEN-LAST:event_lakiRbActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(tglLaporanField.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal Laporan wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(noLaporanField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nomor Laporan wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tambahRecord();
        showTable();
        clearForm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silakan pilih Data yang akan ubah", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateRecord();
        clearForm();
        showTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        cetak();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            kirim();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silakan pilih Data yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void idWargaCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idWargaCBActionPerformed
//        if(idWargaCB.getSelectedItem() != null){                    
//            String idWarga = idWargaCB.getSelectedItem().toString();
//            try {
//                displayDetailWarga(idWarga);
//            } catch (SQLException ex) {
//                Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }//GEN-LAST:event_idWargaCBActionPerformed

    private void idWargaCBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_idWargaCBItemStateChanged
//        if(idWargaCB.getSelectedItem() != null){                    
//            String idWarga = idWargaCB.getSelectedItem().toString();
//            try {
//                displayDetailWarga(idWarga);
//            } catch (SQLException ex) {
//                Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }//GEN-LAST:event_idWargaCBItemStateChanged

    private void perempuanRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perempuanRbActionPerformed
        lakiRb.setSelected(false);
        perempuanRb.setSelected(true);
    }//GEN-LAST:event_perempuanRbActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        clearForm();
    }//GEN-LAST:event_jButton7ActionPerformed

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
            java.util.logging.Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTrxLaporanWarga(null, true).setVisible(true);
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
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JTextField jenisLaporanField;
    private javax.swing.JTextArea keteranganField;
    private javax.swing.JComboBox<String> kodeLaporanCB;
    private javax.swing.JRadioButton lakiRb;
    private javax.swing.JTextField namaField;
    private javax.swing.JTextField noLaporanField;
    private javax.swing.JRadioButton perempuanRb;
    private javax.swing.JTextField statusTinggalField;
    private javax.swing.JTextField teleponField;
    private com.toedter.calendar.JDateChooser tglLaporanField;
    // End of variables declaration//GEN-END:variables
}
