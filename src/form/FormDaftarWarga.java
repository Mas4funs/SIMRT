/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import util.DBUtils;

/**
 *
 * @author galih
 */
public class FormDaftarWarga extends javax.swing.JDialog {

    /**
     * Creates new form FormDaftarUser
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    
    public FormDaftarWarga(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        showTable();
    }

    public void showTable(){
        
        DefaultTableModel model= new DefaultTableModel(); 
        model.addColumn("Tanggal Input"); 
        model.addColumn("Nomor Rumah");
        model.addColumn("ID Warga");
        model.addColumn("Nama");
        model.addColumn("Jenis Kelamin");
        dataTable.setModel(model);
        
        String sql = "SELECT * FROM tb_daftarwarga ";
        
        con = new DBUtils().getKoneksi();
        int cnt = 1;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getString("tglmasuk"), 
                    rs.getString("norumah"), 
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
               
                String idWarga = (String)dataTable.getValueAt(currentRow, 2);
                
                detailWarga(idWarga);               
                idWargaField.setEnabled(false);
                dataTable.setRowSelectionInterval(currentRow, currentRow);
            }
        });
    }
    
    public void updateRecord(){
        String sql = "UPDATE tb_daftarwarga SET tglmasuk = ?, norumah = ?, nama = ?, jeniskelamin = ?, tgllahir = ?, agama = ?, kewarganegaraan = ?, statuskeluarga = ?, statustinggal = ?, statuswarga = ?, alamat = ?, telepon = ?, email = ? WHERE idwarga = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setDate(1, new Date(tglMasukField.getDate().getTime()));
            ps.setString(2, noRumahField.getText());
            ps.setString(3, namaField.getText());                       
            ps.setString(4, lakiRb.isSelected()?"L":"P");
            ps.setDate(5, new Date(jDateChooser1.getDate().getTime()));
            ps.setString(6, agamaCB.getSelectedItem().toString());
            ps.setString(7, kewarganegaraanCB.getSelectedItem().toString());
            ps.setString(8, statusKeluargaCB.getSelectedItem().toString());
            ps.setString(9, statusTinggalCB.getSelectedItem().toString());
            ps.setString(10, statusTinggalCB1.getSelectedItem().toString());
            ps.setString(11, alamatField.getText());
            ps.setString(12, teleponField.getText());
            ps.setString(13, emailField.getText());
            ps.setString(14, idWargaField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal diubah", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    
    public void detailWarga(String idWarga){
        String sql = "SELECT * FROM tb_daftarwarga where idwarga = ? ";        
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, idWarga);
            rs = ps.executeQuery();
            
            while(rs.next()){
                String nama = rs.getString("nama");
                String noRumah = rs.getString("norumah");
                String jnsKelamin = rs.getString("jeniskelamin");
                Date tglMasuk = rs.getDate("tglmasuk");
                Date tglLahir = rs.getDate("tgllahir");                
                String agama = rs.getString("agama");
                String kewarganegaraan = rs.getString("kewarganegaraan");
                String statusKeluarga = rs.getString("statuskeluarga");
                String statusTinggal = rs.getString("statustinggal");
                //tglMasukField.setDate(rs.getDate("tglmasuk"));
                String alamat = rs.getString("alamat");
                String telepon = rs.getString("telepon");
                String email = rs.getString("email");
                
                tglMasukField.setDate(new Date(tglMasuk.getTime()));
                jDateChooser1.setDate(new Date(tglLahir.getTime()));
                namaField.setText(nama);
                idWargaField.setText(idWarga);
                noRumahField.setText(noRumah);
                if("L".equals(jnsKelamin)){
                    lakiRb.setSelected(true);
                    perempuanRb.setSelected(false);
                }else{
                    lakiRb.setSelected(false);
                    perempuanRb.setSelected(true);
                }
                
                
                agamaCB.setSelectedItem(agama.toString());
                kewarganegaraanCB.setSelectedItem(kewarganegaraan.toString());
                statusKeluargaCB.setSelectedItem(statusKeluarga.toString());
                statusTinggalCB.setSelectedItem(statusTinggal.toString());
                alamatField.setText(alamat);
                teleponField.setText(telepon);
                emailField.setText(email);
            }   
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
        jLabel3 = new javax.swing.JLabel();
        noRumahField = new javax.swing.JTextField();
        idWargaField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        namaField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lakiRb = new javax.swing.JRadioButton();
        perempuanRb = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        agamaCB = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        kewarganegaraanCB = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        statusKeluargaCB = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        tglMasukField = new com.toedter.calendar.JDateChooser();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        tambahBtn = new javax.swing.JButton();
        ubahBtn = new javax.swing.JButton();
        hapusBtn = new javax.swing.JButton();
        keluarBtn = new javax.swing.JButton();
        bersihkanBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        statusTinggalCB = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        alamatField = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        teleponField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        statusTinggalCB1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-crowd-48.png"))); // NOI18N
        jLabel1.setText("DAFTAR WARGA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(336, 336, 336))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Nomor Rumah");

        jLabel6.setText("ID Warga");

        jLabel7.setText("Nama");

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

        jLabel4.setText("Jenis Kelamin");

        jLabel5.setText("Tanggal Lahir");

        agamaCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Islam", "Kristen", "Protestan", "Hindu", "Budha", "Konghucu" }));

        jLabel8.setText("Agama");

        kewarganegaraanCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "WNI", "WNA" }));

        jLabel9.setText("Kewarganegaraan");

        jLabel10.setText("Status Keluarga");

        statusKeluargaCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kepala Keluarga", "Anak", "Istri", "Saudara" }));

        jLabel15.setText("Tanggal Input");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15))
                .addGap(63, 63, 63)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(kewarganegaraanCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(agamaCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lakiRb)
                        .addGap(18, 18, 18)
                        .addComponent(perempuanRb))
                    .addComponent(namaField)
                    .addComponent(idWargaField)
                    .addComponent(noRumahField)
                    .addComponent(statusKeluargaCB, 0, 263, Short.MAX_VALUE)
                    .addComponent(tglMasukField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(tglMasukField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noRumahField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idWargaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lakiRb)
                    .addComponent(perempuanRb)
                    .addComponent(jLabel4))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agamaCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kewarganegaraanCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(statusKeluargaCB, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {agamaCB, idWargaField, kewarganegaraanCB, namaField, noRumahField, statusKeluargaCB});

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tambahBtn.setBackground(new java.awt.Color(255, 153, 0));
        tambahBtn.setText("Tambah");
        tambahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahBtnActionPerformed(evt);
            }
        });

        ubahBtn.setBackground(new java.awt.Color(255, 153, 0));
        ubahBtn.setText("Ubah");
        ubahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahBtnActionPerformed(evt);
            }
        });

        hapusBtn.setBackground(new java.awt.Color(255, 153, 0));
        hapusBtn.setText("Hapus");
        hapusBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusBtnActionPerformed(evt);
            }
        });

        keluarBtn.setBackground(new java.awt.Color(255, 153, 0));
        keluarBtn.setText("Keluar");
        keluarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarBtnActionPerformed(evt);
            }
        });

        bersihkanBtn.setBackground(new java.awt.Color(255, 153, 0));
        bersihkanBtn.setText("Bersihkan");
        bersihkanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bersihkanBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(tambahBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ubahBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(hapusBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(bersihkanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(keluarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tambahBtn)
                    .addComponent(ubahBtn)
                    .addComponent(hapusBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bersihkanBtn)
                    .addComponent(keluarBtn))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {hapusBtn, keluarBtn, tambahBtn, ubahBtn});

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

        jLabel11.setText("Status Tinggal");

        statusTinggalCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tetap", "Sementara" }));

        alamatField.setColumns(20);
        alamatField.setRows(5);
        jScrollPane2.setViewportView(alamatField);

        jLabel12.setText("Alamat");

        jLabel13.setText("Telepon");

        jLabel14.setText("Email");

        jLabel16.setText("Status Warga");

        statusTinggalCB1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hidup", "Meninggal" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailField)
                            .addComponent(teleponField)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .addComponent(statusTinggalCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(46, 46, 46)
                        .addComponent(statusTinggalCB1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(statusTinggalCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(statusTinggalCB1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teleponField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("DETAIL TABEL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void keluarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarBtnActionPerformed
        dispose();
    }//GEN-LAST:event_keluarBtnActionPerformed

    private void ubahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahBtnActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silakan pilih Data yang akan diubah", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateRecord();
        showTable();
        clearForm();
        
    }//GEN-LAST:event_ubahBtnActionPerformed

    private void hapusBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusBtnActionPerformed
        if(dataTable.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Silakan pilih Data yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int i = dataTable.getSelectedRow();
        String idWarga = (String) dataTable.getValueAt(i, 2);
        System.out.println("idWarga == "+idWarga);
        
        int pilih = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if(pilih == JOptionPane.OK_OPTION){
            hapusRecord(idWarga);
            clearForm();
        }
    }//GEN-LAST:event_hapusBtnActionPerformed

    private void tambahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahBtnActionPerformed
        if(tglMasukField.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal Input wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(noRumahField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nomor Rumah wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(idWargaField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "ID Warga wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(namaField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Nama wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(!lakiRb.isSelected() && !perempuanRb.isSelected()){
            JOptionPane.showMessageDialog(null, "Jenis kelamin wajib dipilih", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(jDateChooser1.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal Lahir wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(alamatField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Alamat wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(teleponField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Telepon wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(emailField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Email wajib diisi", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tambahRecord();
        showTable();
        clearForm();
    }//GEN-LAST:event_tambahBtnActionPerformed

    private void lakiRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lakiRbActionPerformed
        lakiRb.setSelected(true);
        perempuanRb.setSelected(false);
    }//GEN-LAST:event_lakiRbActionPerformed

    private void perempuanRbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perempuanRbActionPerformed
        lakiRb.setSelected(false);
        perempuanRb.setSelected(true);
    }//GEN-LAST:event_perempuanRbActionPerformed

    private void bersihkanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bersihkanBtnActionPerformed
        clearForm();
    }//GEN-LAST:event_bersihkanBtnActionPerformed

    public void hapusRecord(String kode){
        String sql = "DELETE FROM tb_daftarwarga WHERE idwarga = ? ";
        con = new DBUtils().getKoneksi();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, kode);
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal dihapus\n"+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        showTable();
    }
    
    public void tambahRecord(){
        String sql = "INSERT INTO tb_daftarwarga(idwarga, tglmasuk, norumah, nama, jeniskelamin, tgllahir, agama, kewarganegaraan, statuskeluarga, statustinggal, statuswarga, alamat, telepon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        System.out.println(tglMasukField.getDate());
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, idWargaField.getText());
            ps.setDate(2, new Date(tglMasukField.getDate().getTime()));
            ps.setString(3, noRumahField.getText());
            ps.setString(4, namaField.getText());
            ps.setString(5, lakiRb.isSelected()?"L":"P");
            ps.setDate(6, new Date(jDateChooser1.getDate().getTime()));
            ps.setString(7, agamaCB.getSelectedItem().toString());
            ps.setString(8, kewarganegaraanCB.getSelectedItem().toString());
            ps.setString(9, statusKeluargaCB.getSelectedItem().toString());
            ps.setString(10, statusTinggalCB.getSelectedItem().toString());
            ps.setString(11, statusTinggalCB1.getSelectedItem().toString());
            ps.setString(12, alamatField.getText());
            ps.setString(13, teleponField.getText());
            ps.setString(14, emailField.getText());
            ps.execute();
            
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data gagal ditambahkan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void clearForm(){
        idWargaField.setEnabled(true);
        tglMasukField.setDate(null);
        noRumahField.setText("");
        namaField.setText("");
        jDateChooser1.setDate(null);
        alamatField.setText("");
        teleponField.setText("");
        emailField.setText("");
        idWargaField.setText("");
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
            java.util.logging.Logger.getLogger(FormDaftarWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormDaftarWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormDaftarWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormDaftarWarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormDaftarWarga(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> agamaCB;
    private javax.swing.JTextArea alamatField;
    private javax.swing.JButton bersihkanBtn;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField emailField;
    private javax.swing.JButton hapusBtn;
    private javax.swing.JTextField idWargaField;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JButton keluarBtn;
    private javax.swing.JComboBox<String> kewarganegaraanCB;
    private javax.swing.JRadioButton lakiRb;
    private javax.swing.JTextField namaField;
    private javax.swing.JTextField noRumahField;
    private javax.swing.JRadioButton perempuanRb;
    private javax.swing.JComboBox<String> statusKeluargaCB;
    private javax.swing.JComboBox<String> statusTinggalCB;
    private javax.swing.JComboBox<String> statusTinggalCB1;
    private javax.swing.JButton tambahBtn;
    private javax.swing.JTextField teleponField;
    private com.toedter.calendar.JDateChooser tglMasukField;
    private javax.swing.JButton ubahBtn;
    // End of variables declaration//GEN-END:variables
}
