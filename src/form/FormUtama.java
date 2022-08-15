/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import util.DBUtils;

/**
 *
 * @author galih
 */
public final class FormUtama extends javax.swing.JDialog {

    /**
     * Creates new form FormUtama
     */
    
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    String userLevel = "";
    public FormUtama(java.awt.Frame parent, boolean modal, Map data) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        System.out.println(data);
        
        userLevel = (String)data.get("level");
        userNameLabel.setText(data.get("userName").toString());
        userLevelLabel.setText("( "+data.get("level").toString()+" )");
        //userNameLabel.setText("administrator");
        dateLabel.setText(sdf.format(today));
        AksesLevel();
        getWargaPermanen();
        getWargaSementara();
        getWargaLaki();
        getWargaPerempuan();
        getLaporanDarurat();
        getLaporanPengaduan();
        getSuratPengantar();
        getSuratKematian();
        getSuratDomisili();
        getIuranWajib();
        getIuranSwadaya();
        getTotalEmail();
        getPeriodeEmail();
    }

    public void AksesLevel(){
        //Ketua RT, Admin RT, Administrator
        if(userLevel.equals("Ketua RT")){ 
            menuEmailWarga.setVisible(false);
            menuIuranWarga.setVisible(false);
        }else if(userLevel.equals("Admin RT")){
            jMenu1.setVisible(false);
            jMenu3.setVisible(false);
            menuLaporanWarga.setVisible(false);
            menuSuratWarga.setVisible(false);
            jPanel3.setVisible(false);
            this.setSize(new Dimension(750, 600));
            this.setResizable(false);
        }else{}
    }
    
    public void getWargaPermanen(){
        String sql = "SELECT count(1) as cnt FROM tb_daftarwarga where statustinggal  = 'Tetap'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                permanenLabel.setText("Tetap : "+ qty + " orang");
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getWargaSementara(){
        String sql = "SELECT count(1) as cnt FROM tb_daftarwarga where statustinggal  = 'Sementara'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                sementaraLabel.setText("Sementara : "+ qty + " orang");
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getWargaLaki(){
        String sql = "SELECT count(1) as cnt FROM tb_daftarwarga where jeniskelamin  = 'L'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                lakiLabel.setText("Laki-Laki : "+ qty + " orang");
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getWargaPerempuan(){
        String sql = "SELECT count(1) as cnt FROM tb_daftarwarga where jeniskelamin  = 'P'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                perempuanLabel.setText("Perempuan : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getLaporanPengaduan(){
        String sql = "SELECT count(1) as cnt FROM form_laporan where jenislaporan = 'Pengaduan'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                pengaduanLabel.setText("Pengaduan : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getLaporanDarurat(){
        String sql = "SELECT count(1) as cnt FROM form_laporan where jenislaporan = 'Darurat'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                daruratLabel.setText("Darurat : "+ qty + " orang");
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getSuratPengantar(){
        String sql = "SELECT count(1) as cnt FROM form_surat where jenissurat = 'Pengantar'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                pengantarLabel.setText("Pengantar : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getSuratKematian(){
        String sql = "SELECT count(1) as cnt FROM form_surat where jenissurat = 'Kematian'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                kematianLabel.setText("Kematian : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getSuratDomisili(){
        String sql = "SELECT count(1) as cnt FROM form_surat where jenissurat = 'Domisili'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                domisiliLabel.setText("Domisili : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getIuranWajib(){
        String sql = "SELECT count(1) as cnt FROM form_iuran fi, tb_daftariuran td where fi.kdiuran = td .kdiuran and td.jenisiuran  = 'Wajib'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                wajibLabel.setText("Wajib : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getIuranSwadaya(){
        String sql = "SELECT count(1) as cnt FROM form_iuran fi, tb_daftariuran td where fi.kdiuran = td .kdiuran and td.jenisiuran  = 'Swadaya'";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                swadayaLabel.setText("Swadaya : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getPeriodeEmail(){
        String sql = "";
        if("Januari".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 1";
        }else if("Februari".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 2";
        }else if("Maret".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 3";
        }else if("April".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 4";
        }else if("Mei".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 5";
        }else if("Juni".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 6";
        }else if("Juli".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 7";
        }else if("Agustus".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 8";
        }else if("September".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 9";
        }else if("Oktober".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 10";
        }else if("November".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 11";
        }else if("Desember".equals(jComboBox1.getSelectedItem())){
            sql = "SELECT count(*) as cnt FROM form_email WHERE MONTH(tglemail) = 12";
        }else{
            sql = "SELECT count(*) as cnt FROM form_email";
        }
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            //ps.setString(1, jComboBox1.getSelectedItem().toString());
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                periodeemail.setText(" : "+ qty);
            }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void getTotalEmail(){
        String sql = "SELECT count(*) as cnt FROM form_email";
        con = new DBUtils().getKoneksi();
        
        try {
            ps = con.prepareStatement(sql);             
            rs = ps.executeQuery();
            
            while (rs.next()){
                String qty = String.valueOf(rs.getInt("cnt"));
                totalemail.setText("Total Keseluruhan : "+ qty);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        permanenLabel = new javax.swing.JLabel();
        sementaraLabel = new javax.swing.JLabel();
        lakiLabel = new javax.swing.JLabel();
        perempuanLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pengaduanLabel = new javax.swing.JLabel();
        daruratLabel = new javax.swing.JLabel();
        pengantarLabel = new javax.swing.JLabel();
        kematianLabel = new javax.swing.JLabel();
        domisiliLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        wajibLabel = new javax.swing.JLabel();
        swadayaLabel = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        periodeemail = new javax.swing.JLabel();
        totalemail = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        userNameLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        userLevelLabel = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuDaftarUser = new javax.swing.JMenuItem();
        menuDaftarWarga = new javax.swing.JMenuItem();
        menuDaftarIuran = new javax.swing.JMenuItem();
        menuDaftarLaporan = new javax.swing.JMenuItem();
        menuDaftarSurat = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuLaporanWarga = new javax.swing.JMenuItem();
        menuSuratWarga = new javax.swing.JMenuItem();
        menuEmailWarga = new javax.swing.JMenuItem();
        menuIuranWarga = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menuRekapWarga = new javax.swing.JMenuItem();
        menuRekapLapWarga = new javax.swing.JMenuItem();
        menuRekapSuratWarga = new javax.swing.JMenuItem();
        menuRekapEmail = new javax.swing.JMenuItem();
        menuRekapIuran = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 153, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setBackground(new java.awt.Color(255, 153, 0));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-content-48.png"))); // NOI18N
        jLabel1.setText("Dasbor");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-user-account-skin-type-7-48.png"))); // NOI18N
        jLabel2.setText("Total Warga ");

        permanenLabel.setText("Permanen : 10 Orang");

        sementaraLabel.setText("Sementara: 0 Orang");

        lakiLabel.setText("Laki Laki: 10 Orang");

        perempuanLabel.setText("Peremuan: 12 Orang");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(permanenLabel)
                            .addComponent(sementaraLabel))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(perempuanLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lakiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(permanenLabel)
                    .addComponent(lakiLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sementaraLabel)
                    .addComponent(perempuanLabel))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-notepad-48.png"))); // NOI18N
        jLabel3.setText("Total Laporan & Surat ");

        pengaduanLabel.setText("Pengaduan : 10");

        daruratLabel.setText("Darurat : 10");

        pengantarLabel.setText("Pengantar : 10");

        kematianLabel.setText("Kematian : 10");

        domisiliLabel.setText("Domisili : 10");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pengaduanLabel)
                            .addComponent(daruratLabel))
                        .addGap(71, 71, 71)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(domisiliLabel)
                            .addComponent(kematianLabel)
                            .addComponent(pengantarLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pengaduanLabel)
                    .addComponent(pengantarLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(daruratLabel)
                    .addComponent(kematianLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(domisiliLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-refund-48.png"))); // NOI18N
        jLabel4.setText("Total Iuran ");

        wajibLabel.setText("Wajib: 10");

        swadayaLabel.setText("Swadaya :10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(swadayaLabel)
                            .addComponent(wajibLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wajibLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(swadayaLabel)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/email.png"))); // NOI18N
        jLabel5.setText("Total Email");

        periodeemail.setText(" : -");

        totalemail.setText("Total Keseluruhan :10");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Periode", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(periodeemail)
                        .addGap(6, 38, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalemail)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(periodeemail)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(totalemail)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 102, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userNameLabel.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        userNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        userNameLabel.setText("userNameLabel ");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8-person-64.png"))); // NOI18N

        dateLabel.setForeground(new java.awt.Color(255, 255, 255));
        dateLabel.setText("10/12/2022");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Selamat Datang");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Logout");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        userLevelLabel.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        userLevelLabel.setForeground(new java.awt.Color(255, 255, 255));
        userLevelLabel.setText("levelLabel ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(userNameLabel)
                    .addComponent(dateLabel)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userLevelLabel))
                .addGap(91, 91, 91))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(userNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userLevelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dateLabel)
                .addGap(27, 27, 27)
                .addComponent(jLabel8)
                .addGap(33, 33, 33)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel9.setText("SISTEM INFORMASI MANAJEMEN RUKUN TETANGGA ");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("PERUMAHAN LEGOK PERMAI ");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("CLUSTER HELICONIA ");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("RT 02 RW 11 ");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("TANGERANG BANTEN");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/kab_tangerang_footer_transparan.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel15)
                .addGap(42, 42, 42)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel12, jLabel13, jLabel9});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(1, 1, 1)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addGap(34, 34, 34))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jMenu1.setText("Master");
        jMenu1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        menuDaftarUser.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuDaftarUser.setText("Daftar User");
        menuDaftarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDaftarUserActionPerformed(evt);
            }
        });
        jMenu1.add(menuDaftarUser);

        menuDaftarWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuDaftarWarga.setText("Daftar Warga");
        menuDaftarWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDaftarWargaActionPerformed(evt);
            }
        });
        jMenu1.add(menuDaftarWarga);

        menuDaftarIuran.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuDaftarIuran.setText("Daftar Iuran");
        menuDaftarIuran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDaftarIuranActionPerformed(evt);
            }
        });
        jMenu1.add(menuDaftarIuran);

        menuDaftarLaporan.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuDaftarLaporan.setText("Daftar Laporan");
        menuDaftarLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDaftarLaporanActionPerformed(evt);
            }
        });
        jMenu1.add(menuDaftarLaporan);

        menuDaftarSurat.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuDaftarSurat.setText("Daftar Surat");
        menuDaftarSurat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDaftarSuratActionPerformed(evt);
            }
        });
        jMenu1.add(menuDaftarSurat);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Transaksi");
        jMenu2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        menuLaporanWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuLaporanWarga.setText("Laporan Warga");
        menuLaporanWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLaporanWargaActionPerformed(evt);
            }
        });
        jMenu2.add(menuLaporanWarga);

        menuSuratWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuSuratWarga.setText("Surat Warga");
        menuSuratWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSuratWargaActionPerformed(evt);
            }
        });
        jMenu2.add(menuSuratWarga);

        menuEmailWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuEmailWarga.setText("Email Warga");
        menuEmailWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEmailWargaActionPerformed(evt);
            }
        });
        jMenu2.add(menuEmailWarga);

        menuIuranWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuIuranWarga.setText("Iuran Warga");
        menuIuranWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIuranWargaActionPerformed(evt);
            }
        });
        jMenu2.add(menuIuranWarga);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Laporan");
        jMenu3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N

        menuRekapWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuRekapWarga.setText("Rekap Warga");
        menuRekapWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRekapWargaActionPerformed(evt);
            }
        });
        jMenu3.add(menuRekapWarga);

        menuRekapLapWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuRekapLapWarga.setText("Rekap Laporan Warga");
        menuRekapLapWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRekapLapWargaActionPerformed(evt);
            }
        });
        jMenu3.add(menuRekapLapWarga);

        menuRekapSuratWarga.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuRekapSuratWarga.setText("Rekap Surat Warga");
        menuRekapSuratWarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRekapSuratWargaActionPerformed(evt);
            }
        });
        jMenu3.add(menuRekapSuratWarga);

        menuRekapEmail.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuRekapEmail.setText("Rekap Email Warga");
        menuRekapEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRekapEmailActionPerformed(evt);
            }
        });
        jMenu3.add(menuRekapEmail);

        menuRekapIuran.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        menuRekapIuran.setText("Rekap Iuran Warga");
        menuRekapIuran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRekapIuranActionPerformed(evt);
            }
        });
        jMenu3.add(menuRekapIuran);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuDaftarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDaftarUserActionPerformed
        Map data = new HashMap();
        data.put("level", userLevel);
        new FormDaftarUser(null, true, data).setVisible(true);
    }//GEN-LAST:event_menuDaftarUserActionPerformed

    private void menuDaftarWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDaftarWargaActionPerformed
        new FormDaftarWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuDaftarWargaActionPerformed

    private void menuDaftarIuranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDaftarIuranActionPerformed
        new FormDaftarIuran(null, true).setVisible(true);
    }//GEN-LAST:event_menuDaftarIuranActionPerformed

    private void menuDaftarLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDaftarLaporanActionPerformed
        new FormDaftarLaporan(null, true).setVisible(true);
    }//GEN-LAST:event_menuDaftarLaporanActionPerformed

    private void menuDaftarSuratActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDaftarSuratActionPerformed
        new FormDaftarSurat(null, true).setVisible(true);
    }//GEN-LAST:event_menuDaftarSuratActionPerformed

    private void menuLaporanWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLaporanWargaActionPerformed
        new FormTrxLaporanWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuLaporanWargaActionPerformed

    private void menuSuratWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSuratWargaActionPerformed
        new FormTrxSuratWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuSuratWargaActionPerformed

    private void menuEmailWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEmailWargaActionPerformed
        new FormTrxEmailWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuEmailWargaActionPerformed

    private void menuIuranWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIuranWargaActionPerformed
        new FormTrxIuranWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuIuranWargaActionPerformed

    private void menuRekapWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRekapWargaActionPerformed
        new FormRekapWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuRekapWargaActionPerformed

    private void menuRekapLapWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRekapLapWargaActionPerformed
        new FormRekapLaporanWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuRekapLapWargaActionPerformed

    private void menuRekapSuratWargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRekapSuratWargaActionPerformed
        new FormRekapSuratWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuRekapSuratWargaActionPerformed

    private void menuRekapEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRekapEmailActionPerformed
        new FormRekapEmailWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuRekapEmailActionPerformed

    private void menuRekapIuranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRekapIuranActionPerformed
        new FormRekapIuranWarga(null, true).setVisible(true);
    }//GEN-LAST:event_menuRekapIuranActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
        new FormLogin().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        getPeriodeEmail();// TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        getWargaPermanen();
        getWargaSementara();
        getWargaLaki();
        getWargaPerempuan();
        getLaporanDarurat();
        getLaporanPengaduan();
        getSuratPengantar();
        getSuratKematian();
        getSuratDomisili();
        getIuranWajib();
        getIuranSwadaya();
        getTotalEmail();
        getPeriodeEmail();// TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

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
            java.util.logging.Logger.getLogger(FormUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormUtama(null, true, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel daruratLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel domisiliLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel kematianLabel;
    private javax.swing.JLabel lakiLabel;
    private javax.swing.JMenuItem menuDaftarIuran;
    private javax.swing.JMenuItem menuDaftarLaporan;
    private javax.swing.JMenuItem menuDaftarSurat;
    private javax.swing.JMenuItem menuDaftarUser;
    private javax.swing.JMenuItem menuDaftarWarga;
    private javax.swing.JMenuItem menuEmailWarga;
    private javax.swing.JMenuItem menuIuranWarga;
    private javax.swing.JMenuItem menuLaporanWarga;
    private javax.swing.JMenuItem menuRekapEmail;
    private javax.swing.JMenuItem menuRekapIuran;
    private javax.swing.JMenuItem menuRekapLapWarga;
    private javax.swing.JMenuItem menuRekapSuratWarga;
    private javax.swing.JMenuItem menuRekapWarga;
    private javax.swing.JMenuItem menuSuratWarga;
    private javax.swing.JLabel pengaduanLabel;
    private javax.swing.JLabel pengantarLabel;
    private javax.swing.JLabel perempuanLabel;
    private javax.swing.JLabel periodeemail;
    private javax.swing.JLabel permanenLabel;
    private javax.swing.JLabel sementaraLabel;
    private javax.swing.JLabel swadayaLabel;
    private javax.swing.JLabel totalemail;
    private javax.swing.JLabel userLevelLabel;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JLabel wajibLabel;
    // End of variables declaration//GEN-END:variables
}
