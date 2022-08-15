/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.DBUtils;

/**
 *
 * @author galih
 */
public class FormRekapWarga extends javax.swing.JDialog {

    /**
     * Creates new form FormDaftarUser
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    public FormRekapWarga(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("ID Warga"); 
        model.addColumn("Tgl Input");
        model.addColumn("Nama");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Agama");
        
        dataTable.setModel(model);
        
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
        jLabel4 = new javax.swing.JLabel();
        filterCB = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        fromDateField = new com.toedter.calendar.JDateChooser();
        toDateField = new com.toedter.calendar.JDateChooser();
        filterCB1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-notepad-48.png"))); // NOI18N
        jLabel1.setText("REKAP WARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jLabel1)
                .addContainerGap(316, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Tanggal Input");

        jLabel4.setText("Cari Berdasarkan");

        filterCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jenis Kelamin", "Agama", "Status Tinggal", "Kewarganegaraan", "Status Keluarga", "Status Warga" }));
        filterCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterCBActionPerformed(evt);
            }
        });

        jLabel3.setText("Dari");

        jLabel6.setText("Sampai");

        jButton4.setBackground(new java.awt.Color(255, 153, 0));
        jButton4.setText("Cari");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L", "P" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(48, 48, 48)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3)
                            .addComponent(fromDateField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(filterCB, 0, 208, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(filterCB1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(toDateField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(fromDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterCB, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(filterCB1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fromDateField, toDateField});

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

        jLabel5.setText("Total Data: ");

        jLabel7.setText("0");

        jButton1.setBackground(new java.awt.Color(255, 153, 0));
        jButton1.setText("Cetak");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 153, 0));
        jButton2.setText("Reset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 153, 0));
        jButton3.setText("Keluar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("DETAIL TABEL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButton2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButton3))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(361, 361, 361)
                        .addComponent(jLabel10)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void cari(){
        if(fromDateField.getDate()== null || fromDateField.getDate().toString().equals("")){
            JOptionPane.showMessageDialog(null, "Anda belum memilih date", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(toDateField.getDate()== null || toDateField.getDate().toString().equals("")){
            JOptionPane.showMessageDialog(null, "Anda belum memilih date", "Informasi", JOptionPane.INFORMATION_MESSAGE);
         }else{
        String kataKunci = "%%";
        Date fromDate = new Date(fromDateField.getDate().getTime());
        Date toDate = new Date(toDateField.getDate().getTime());
        
       
        System.out.println("fromDate == " + fromDate);
        System.out.println("toDate == " + toDate);
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("ID Warga"); 
        model.addColumn("Tgl Input");
        model.addColumn("Nama");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Agama");
        
        dataTable.setModel(model);
               
        String sql = "";
        
        if("Semua".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ? AND agama = ?";
        }else if("Jenis Kelamin".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ?  AND jeniskelamin LIKE ?";
        }else if("Agama".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ?  AND agama LIKE ?";
        }else if("Status Tinggal".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ?  AND statustinggal LIKE ?";
        }else if("Kewarganegaraan".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ?  AND kewarganegaraan LIKE ?";
        }else if("Status Keluarga".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ?  AND statuskeluarga LIKE ?";
        }else if("Status Warga".equals(filterCB.getSelectedItem())){
            sql = "SELECT * FROM tb_daftarwarga where tglmasuk BETWEEN ? AND ?  AND statuswarga LIKE ?";
        }
        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            if("Semua".equals(filterCB.getSelectedItem())){
            ps.setString(1, kataKunci);;
            }else{
            ps.setDate(1, fromDate);
            ps.setDate(2, toDate);
            ps.setString(3, filterCB1.getSelectedItem().toString());
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getString("idwarga"), 
                    rs.getString("tglmasuk"), 
                    rs.getString("nama"),
                    rs.getString("jeniskelamin").equals("L")?"Laki-Laki":"Perempuan",
                    rs.getString("agama"),
                    } 
                );
            }   
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
     
     jLabel7.setText(String.valueOf(dataTable.getRowCount()));
        model.fireTableDataChanged();
    }
    }
    
    public void cetak(){
        JasperDesign jasperDesign = null;
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
               if(fromDateField.getDate()== null || fromDateField.getDate().toString().equals("")){
            JOptionPane.showMessageDialog(null, "Anda belum memilih date", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }else if(toDateField.getDate()== null || toDateField.getDate().toString().equals("")){
            JOptionPane.showMessageDialog(null, "Anda belum memilih date", "Informasi", JOptionPane.INFORMATION_MESSAGE);
         }else{
         try {
            URL url = getClass().getResource("/report/rpt_rekap_warga.jrxml");
            jasperDesign = JRXmlLoader.load(url.openStream());
            
            Map param = new HashMap();
            param.put("dateFrom", fromDateField.getDate());
            param.put("dateTo", toDateField.getDate());
             //Jenis Kelamin, Agama, Status Tinggal, Kewarganegaraan, Status Keluarga, Status Warga
            param.put("jeniskelamin", filterCB1.getSelectedItem().toString());
            param.put("agama", filterCB1.getSelectedItem().toString());
            param.put("statustinggal", filterCB1.getSelectedItem().toString());
            param.put("kewarganegaraan", filterCB1.getSelectedItem().toString());
            param.put("statuskeluarga", filterCB1.getSelectedItem().toString());
            param.put("statuswarga", filterCB1.getSelectedItem().toString());
            param.put("jml", jLabel7.getText());
            
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
            dialog.setTitle("Laporan Rekap Iuran");
            dialog.setVisible(true);
            dialog.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }        
    }
   
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cetak();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        cari();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void filterCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterCBActionPerformed
        if("Jenis Kelamin".equals(filterCB.getSelectedItem()) ){
                    filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L","P" }));
                }else if("Agama".equals(filterCB.getSelectedItem()) ){
                    filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Islam", "Kristen", "Protestan", "Hindu", "Budha", "Konghucu" }));
                }else if("Status Tinggal".equals(filterCB.getSelectedItem()) ){
                    filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tetap", "Sementara" }));
                }else if("Kewarganegaraan".equals(filterCB.getSelectedItem()) ){
                   filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "WNI", "WNA" }));
                }else if("Status Keluarga".equals(filterCB.getSelectedItem()) ){
                    filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kepala Keluarga", "Anak", "Istri", "Saudara" }));
                }else if("Status Warga".equals(filterCB.getSelectedItem()) ){
                    filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hidup", "Meninggal" }));
                }else{
                   filterCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NULL" }));
                }
    }//GEN-LAST:event_filterCBActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        fromDateField.setDate(null);
        toDateField.setDate(null);
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        jLabel7.setText(String.valueOf(dataTable.getRowCount()));
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(FormRekapWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormRekapWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormRekapWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormRekapWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormRekapWarga(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable dataTable;
    private javax.swing.JComboBox<String> filterCB;
    private javax.swing.JComboBox<String> filterCB1;
    private com.toedter.calendar.JDateChooser fromDateField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser toDateField;
    // End of variables declaration//GEN-END:variables
}
