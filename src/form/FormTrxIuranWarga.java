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
public class FormTrxIuranWarga extends javax.swing.JDialog {

    /**
     * Creates new form FormDaftarUser
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    String pattern = "ddMMyy";
    DateFormat df = new SimpleDateFormat(pattern);
    String fileName = "C:/tmp/iuranwarga.txt" ; 
    File f = new File(fileName);
    
    static private String selectedString(ItemSelectable is) {
        Object selected[] = is.getSelectedObjects();
        return ((selected.length == 0) ? "null" : (String) selected[0]);
    }
    
    public FormTrxIuranWarga(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        keteranganField.setEditable(false);
        keteranganField.setBackground(Color.LIGHT_GRAY);
        
        kodeIuranField.setEditable(false);
        kodeIuranField.setBackground(Color.LIGHT_GRAY);
        
        biayaIuranField.setEditable(false);
        biayaIuranField.setBackground(Color.LIGHT_GRAY);
        
        idWargaField.setEditable(false);
        idWargaField.setBackground(Color.LIGHT_GRAY);
        
        nomorRumahField.setEditable(false);
        nomorRumahField.setBackground(Color.LIGHT_GRAY);
        
        emailField.setEditable(false);
        emailField.setBackground(Color.LIGHT_GRAY);
        
        showTable();
        if(!f.exists()){new util.DBUtils().FileIuranWarga();}
        ReadNoIuranWarga();
        try {
            displayNomorEmailCB();
        } catch (SQLException ex) {
            Logger.getLogger(FormTrxIuranWarga.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ItemListener itemListener = new ItemListener() {
           
        public void itemStateChanged(ItemEvent itemEvent) {
          int state = itemEvent.getStateChange();
          System.out.println((state == ItemEvent.SELECTED) ? "Selected" : "Deselected");
          System.out.println("Item: " + itemEvent.getItem());
          ItemSelectable is = itemEvent.getItemSelectable();
          System.out.println(", Selected: " + selectedString(is));

            try {
                displayTransactionEmail(selectedString(is));              
            } catch (SQLException ex) {
                Logger.getLogger(FormTrxLaporanWarga.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        };
        
        nomorEmailCB.addItemListener(itemListener);
    }
    
    public void showTable(){        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("Nomor Iuran"); 
        model.addColumn("Status Iuran");
        model.addColumn("Kode Iuran");
        model.addColumn("ID Warga");
        model.addColumn("Nomor Rumah");
        
        dataTable.setModel(model);
        
        String sql = "SELECT * FROM form_iuran ";
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getString("noiuran"), 
                    rs.getString("statusiuran"), 
                    rs.getString("kdiuran"),
                    rs.getString("idwarga"),
                    rs.getString("norumah"),
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
                
                String nomorIuran = (String)dataTable.getValueAt(currentRow, 0);
                detailTrxIuran(nomorIuran);
                
                dataTable.setRowSelectionInterval(currentRow, currentRow);
            }
        });
    }
    
    public void detailTrxIuran(String nomorIuran){
        String sql = "SELECT * FROM form_iuran where noiuran = ?";
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, nomorIuran);
            rs = ps.executeQuery();
            
            while (rs.next()){                
               Date tglIuran = rs.getDate("tgliuran");
               String jumlahPembayaran = rs.getString("jmlpembayaran");
               String statusIuran = rs.getString("statusiuran");
               String noEmail = rs.getString("noemail");
               String kodeIuran = rs.getString("kdiuran");
               String tagihan = rs.getString("tagihan");
               String idWarga = rs.getString("idwarga");
               String norumah = rs.getString("norumah");
               String email = rs.getString("email");
               
               nomorIuranField.setText(nomorIuran);
               tglIuranField.setDate(tglIuran);
               jumlahBayarField.setText(jumlahPembayaran);
               statusIuranCB.setSelectedItem(statusIuran);
               nomorEmailCB.setSelectedItem(noEmail);
               kodeIuranField.setText(kodeIuran);
               biayaIuranField.setText(tagihan);
               idWargaField.setText(idWarga);
               nomorRumahField.setText(norumah);
               emailField.setText(email);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void displayNomorEmailCB() throws SQLException{
        nomorEmailCB.removeAllItems();
        String sql = "SELECT * FROM form_email ";
        con = new DBUtils().getKoneksi();
        
        nomorEmailCB.addItem("-- Pilih --");
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                nomorEmailCB.addItem(rs.getString("noemail"));       
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }           
    }
    
    public void displayTransactionEmail(String noEmail) throws SQLException{        
        String sql = "SELECT * FROM form_email where noemail = ?";
        con = new DBUtils().getKoneksi();               
        
        try {
            ps = con.prepareStatement(sql);             
            ps.setString(1, noEmail);
            rs = ps.executeQuery();
            
            while (rs.next()){
                keteranganField.setText(rs.getString("ketemail"));
                kodeIuranField.setText(rs.getString("kdiuran"));
                biayaIuranField.setText(rs.getString("tagihan"));
                idWargaField.setText(rs.getString("idwarga"));
                nomorRumahField.setText(rs.getString("norumah"));
                emailField.setText(rs.getString("email"));
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }   
    }
    
    public void tambahRecord(){
        String sql = "INSERT INTO form_iuran(noiuran, tgliuran, jmlpembayaran, statusiuran, noemail, ketemail, kdiuran, tagihan, idwarga, norumah, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, nomorIuranField.getText());
            ps.setDate(2, new Date(tglIuranField.getDate().getTime()));
            ps.setString(3, jumlahBayarField.getText());
            ps.setString(4, statusIuranCB.getSelectedItem().toString());
            ps.setString(5, nomorEmailCB.getSelectedItem().toString());
            ps.setString(6, keteranganField.getText());
            ps.setString(7, kodeIuranField.getText());
            ps.setString(8, biayaIuranField.getText());
            ps.setString(9, idWargaField.getText());
            ps.setString(10, nomorRumahField.getText());
            ps.setString(11, emailField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            displayNoIuranWarga();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal ditambahkan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updateRecord(){
        String sql = "UPDATE form_iuran SET tgliuran = ?, jmlpembayaran = ?, statusiuran = ?, noemail = ?, ketemail = ?, kdiuran = ?, tagihan = ?, idwarga = ?, norumah = ?, email = ? where noiuran = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);            
            ps.setDate(1, new Date(tglIuranField.getDate().getTime()));
            ps.setString(2, jumlahBayarField.getText());
            ps.setString(3, statusIuranCB.getSelectedItem().toString());
            ps.setString(4, nomorEmailCB.getSelectedItem().toString());
            ps.setString(5, keteranganField.getText());
            ps.setString(6, kodeIuranField.getText());
            ps.setString(7, biayaIuranField.getText());
            ps.setString(8, idWargaField.getText());
            ps.setString(9, nomorRumahField.getText());
            ps.setString(10, emailField.getText());
            ps.setString(11, nomorIuranField.getText());
            
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal diubah", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void hapusRecord(String kode){
        String sql = "DELETE FROM form_iuran WHERE noiuran = ? ";
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
    
    public void ReadNoIuranWarga(){
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
            
            nomorIuranField.setText("IW00"+jmh+df.format(new java.util.Date()));
        } catch (FileNotFoundException e) {
            System.out.println("Terjadi Kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void displayNoIuranWarga(){
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
            
            ReadNoIuranWarga();;
        } catch (FileNotFoundException e) {
            System.out.println("Terjadi Kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    
    }
    
    public void cetak(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
               int i = dataTable.getSelectedRow();
        
         if(i >= 0){  
         try {
            URL url = getClass().getResource("/report/rpt_iuran_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            param.put("noiuran", nomorIuranField.getText());
            
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
            dialog.setTitle("Report Data Pengguna");
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
         }else{
             JOptionPane.showMessageDialog(null, "Anda belum memilih", "Informasi", JOptionPane.INFORMATION_MESSAGE);
         
         }
    };
    
    public void kirim()throws FileNotFoundException{    
        Map data = new HashMap();
        data.put("to", emailField.getText());
        data.put("noiuran", nomorIuranField.getText());
        data.put("idwarga", idWargaField.getText());
        data.put("norumah", nomorRumahField.getText());
        data.put("noemail", nomorEmailCB.getSelectedItem().toString());
        data.put("jmlpembayaran", biayaIuranField.getText());
        data.put("statusiuran", statusIuranCB.getSelectedItem().toString());

        
       File is = new File(cetakdankirim());
        data.put("attachment", is);
        
        new NotificationUtils().sentIuranWarga(data);
        JOptionPane.showMessageDialog(null, "Berhasil Terkirim", "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    public void clearForm(){
        ReadNoIuranWarga();
        keteranganField.setText("");
        kodeIuranField.setText("");
        idWargaField.setText("");
        biayaIuranField.setText("");
        nomorRumahField.setText("");
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
        nomorIuranField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jumlahBayarField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        statusIuranCB = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        nomorEmailCB = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        keteranganField = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        kodeIuranField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        biayaIuranField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tglIuranField = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        idWargaField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        nomorRumahField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-refund-48.png"))); // NOI18N
        jLabel1.setText("IURAN WARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(367, 367, 367)
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

        jLabel2.setText("Tanggal Iuran");

        nomorIuranField.setEnabled(false);

        jLabel3.setText("Nomor Iuran");

        jLabel4.setText("Jumlah Bayar");

        statusIuranCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lunas", "Belum Lunas" }));

        jLabel5.setText("Status Iuran");

        nomorEmailCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setText("Nomor Email");

        keteranganField.setColumns(20);
        keteranganField.setRows(5);
        jScrollPane2.setViewportView(keteranganField);

        jLabel7.setText("Keterangan");

        jLabel8.setText("Kode Iuran");

        biayaIuranField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                biayaIuranFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("Biaya Iuran");

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
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(66, 66, 66)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(biayaIuranField)
                    .addComponent(kodeIuranField)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                    .addComponent(nomorEmailCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusIuranCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jumlahBayarField)
                    .addComponent(nomorIuranField)
                    .addComponent(tglIuranField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(tglIuranField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomorIuranField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jumlahBayarField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusIuranCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(nomorEmailCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(kodeIuranField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(biayaIuranField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {biayaIuranField, kodeIuranField, nomorEmailCB, nomorIuranField, statusIuranCB});

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

        jButton3.setBackground(new java.awt.Color(255, 153, 0));
        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
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
                .addContainerGap())
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

        jLabel10.setText("ID Warga");

        jLabel11.setText("Nomor Rumah");

        jLabel12.setText("Email");

        emailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(71, 71, 71)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(idWargaField, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(nomorRumahField)
                    .addComponent(emailField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idWargaField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomorRumahField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(35, 35, 35))
        );

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel13.setText("DETAIL TABEL");

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
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(435, 435, 435))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void biayaIuranFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_biayaIuranFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_biayaIuranFieldActionPerformed

    private void emailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(tglIuranField.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal Iuran wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(nomorIuranField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nomor Iuran wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(biayaIuranField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Jumlah bayar wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        cetak();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            kirim();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FormTrxIuranWarga.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        clearForm();
    }//GEN-LAST:event_jButton7ActionPerformed

    public String cetakdankirim(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        int i = dataTable.getSelectedRow();
        File dir = new File("C:/tmp/pdf/");
        String FILE_NAME = dir.getAbsolutePath()+"/rpt_iuran_warga.pdf";
         if(i >= 0){  
             
         try {
            URL url = getClass().getResource("/report/rpt_iuran_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            param.put("noiuran", nomorIuranField.getText());
            
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
            java.util.logging.Logger.getLogger(FormTrxIuranWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTrxIuranWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTrxIuranWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTrxIuranWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new FormTrxIuranWarga(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField biayaIuranField;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField idWargaField;
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
    private javax.swing.JTextField jumlahBayarField;
    private javax.swing.JTextArea keteranganField;
    private javax.swing.JTextField kodeIuranField;
    private javax.swing.JComboBox<String> nomorEmailCB;
    private javax.swing.JTextField nomorIuranField;
    private javax.swing.JTextField nomorRumahField;
    private javax.swing.JComboBox<String> statusIuranCB;
    private com.toedter.calendar.JDateChooser tglIuranField;
    // End of variables declaration//GEN-END:variables
}
