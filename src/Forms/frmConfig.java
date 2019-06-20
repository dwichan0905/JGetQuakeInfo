/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import Modules.PlayMedia;
/**
 *
 * @author Dwi Candra Permana
 */
public class frmConfig extends javax.swing.JFrame {
    private frmDebug frmDebug;
    private frmBar frmBar;
    private PlayMedia PlayMedia;
    /**
     * Creates new form frmConfig
     */
    public frmConfig() {
        frmBar = new frmBar();
        frmDebug = new frmDebug();
        PlayMedia = new PlayMedia();
        initComponents();
        jPanel1.setVisible(true);
        BacaKonfig();
        sourceSel.removeItemAt(1); // USGS Still Buggy, that's why I remove it.
    }

    private void Terapkan() {
        try {
            int ref = Integer.parseInt(refreshInt.getText());
            int hide = Integer.parseInt(hideNotify.getText());
            String warnBool = Boolean.toString(play.isSelected());
            //String warnBool = String.valueOf(play.getSelectedObjects());
            String warnGedenya = String.valueOf(playmag.getValue());
            double warnval = Double.parseDouble(warnGedenya);
            double trans = Double.parseDouble(String.valueOf(transBar.getValue()));
            
            if (ref < 3) {
                JOptionPane.showMessageDialog(this, "Refresh Interval minimal 3 menit!", "Minimum Value Reached", JOptionPane.WARNING_MESSAGE);
                refreshInt.requestFocus();
            } else if (hide < 5) {
                JOptionPane.showMessageDialog(this, "Jendela Notifikasi minimal 5 detik agar dapat menghilang secara otomatis!", "Minimum Value Reached", JOptionPane.WARNING_MESSAGE);
                hideNotify.requestFocus();
            } else if (warnval < 3.0 && warnBool == "true") {
                JOptionPane.showMessageDialog(this, "Pemutaran suara disaat ada gempa minimal sebesar 3.0 SR!", "Minimum Value Reached", JOptionPane.WARNING_MESSAGE);
                playmag.requestFocus();
            } else if (trans < 15) {
                JOptionPane.showMessageDialog(this, "Transparansi baris informasi minimal 15%!", "Minimum Value Reached", JOptionPane.WARNING_MESSAGE);
                hideNotify.requestFocus();
            } else if (loc.getText() == null) {
                int tanya = JOptionPane.showConfirmDialog(this, "Lokasi file suara tidak Anda isi. Apakah Anda ingin mengisi lokasinya secara default?", "No Sound Selected", JOptionPane.WARNING_MESSAGE + JOptionPane.YES_NO_OPTION);
                if (tanya == JOptionPane.YES_OPTION) {
                    loc.setText("default");
                } else {
                    loc.requestFocus();
                }
            } else {
                File configFile = new File("src/Prefer/JGQI_Config.properties");
                Properties prop = new Properties();
                try {
                    
                        // Setting the Properties first
                        // General
                        prop.setProperty("refreshint", String.valueOf(ref));
                        prop.setProperty("info", String.valueOf(sourceSel.getSelectedItem()));
                        prop.setProperty("transinfo", String.valueOf(transBar.getValue()));
                        prop.setProperty("hideinfo", String.valueOf(hide));
                        prop.setProperty("ui", String.valueOf(guiSel.getSelectedItem()));
                        
                        // Sound
                        prop.setProperty("warn", warnBool);
                        prop.setProperty("warn_mag", warnGedenya);
                        prop.setProperty("warn_path", loc.getText());

                        // URL
                        prop.setProperty("bmkg_url", XML_BMKG.getText());
                        prop.setProperty("bmkg_gif", GIF_BMKG.getText());
                        prop.setProperty("usgs_url", XML_USGS.getText());
                        
                        // Save it
                        prop.store(new FileOutputStream("src/Prefer/JGQI_Config.properties"), " JGetQuakeInfo ver 1.1-alpha\n JGQI_Config.properties\n This is configuration data of JGetQuakeInfo. \n Please don't modify this file, except you know what should you do.");
                        
                        // Apply transparent bar
                        // verify it first
//                        if (frmBar.isVisible() == true) {
//                            frmBar.takeTransparent();
//                        }
                } catch (IOException ex) {
                    String errTit = ex.getMessage();
                    JOptionPane.showMessageDialog(this, ex.getStackTrace(), errTit, JOptionPane.ERROR_MESSAGE);
                    CharArrayWriter cw = new CharArrayWriter();
                    PrintWriter w = new PrintWriter(cw);
                    ex.printStackTrace(w);
                    w.close();
                    String trace = cw.toString();
                    
                    frmDebug.setVisible(true);
                    frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
                    Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Success");
            } 
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Beberapa kolom hanya dapat diisi dengan angka. Cek kembali preferensinya!\n\nKeterangan: " + e.getMessage(), "Invalid Character", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void BacaKonfig () {
        try {
            File f = new File("src/Prefer/JGQI_Config.properties");
            FileReader configFile = new FileReader(f);
            Properties prop = new Properties();
            String sys = System.getProperty("os.name");
            if (sys.startsWith("Windows")) {
                guiSel.removeAllItems();
                guiSel.addItem("Windows");
                guiSel.addItem("Windows Classic");
                guiSel.addItem("Metal");
                guiSel.addItem("Nimbus");
                guiSel.addItem("CDE/Motif");
            } else {
                guiSel.removeAllItems();
                guiSel.addItem("Metal");
                guiSel.addItem("Nimbus");
                guiSel.addItem("CDE/Motif");
            }
            try {
                //FileReader f = new FileReader(configFile);
                prop.load(configFile);
                
                // Declarations
                // General
                String reInt = prop.getProperty("refreshint");
                String gui = prop.getProperty("ui");
                String transint = prop.getProperty("transinfo");
                int tr = Integer.parseInt(transint);
                String sourceInfo = prop.getProperty("info");
                String hideInfo = prop.getProperty("hideinfo");

                // Sound
                String playCheck = prop.getProperty("warn");
                boolean playch = Boolean.parseBoolean(playCheck);
                String warnMag = prop.getProperty("warn_mag");
                float waM = Float.parseFloat(warnMag);
                String warnPath = prop.getProperty("warn_path");

                // URL
                String BMKG_URL = prop.getProperty("bmkg_url");
                String BMKG_GIF = prop.getProperty("bmkg_gif");
                String USGS_XML = prop.getProperty("usgs_url");
                
                // Let's load the configurations!
                // General
                refreshInt.setText(reInt);
                transBar.setValue(tr);
                sourceSel.setSelectedItem(sourceInfo);
                hideNotify.setText(hideInfo);
                guiSel.setSelectedItem(gui);
                
                // Sound
                play.setSelected(playch);
                playmag.setValue(waM);
                loc.setText(warnPath);
                
                if (playch == true) {
                    playmag.setEnabled(true);
                    loc.setEnabled(true);
                    btnBrowse.setEnabled(true);
                    playFile.setEnabled(true);
                } else {
                    playmag.setEnabled(false);
                    loc.setEnabled(false);
                    btnBrowse.setEnabled(false);
                    playFile.setEnabled(false);
                }
                
                //URL
                XML_BMKG.setText(BMKG_URL);
                GIF_BMKG.setText(BMKG_GIF);
                XML_USGS.setText(USGS_XML);
                
                configFile.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            
            
        } catch (FileNotFoundException ex) {
                    String errTit = ex.getMessage();
                    JOptionPane.showMessageDialog(this, ex.getStackTrace(), errTit, JOptionPane.ERROR_MESSAGE);
                    CharArrayWriter cw = new CharArrayWriter();
                    PrintWriter w = new PrintWriter(cw);
                    ex.printStackTrace(w);
                    w.close();
                    String trace = cw.toString();
                    
                    frmDebug.setVisible(true);
                    frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
                    Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
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

        btnApply = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnOke = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        sourceSel = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        refreshInt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        transBar = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        hideNotify = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        guiSel = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        play = new javax.swing.JCheckBox();
        loc = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        playmag = new javax.swing.JSpinner();
        playFile = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        XML_BMKG = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        GIF_BMKG = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        XML_USGS = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnDef = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Preferensi");
        setMaximumSize(new java.awt.Dimension(429, 330));
        setMinimumSize(new java.awt.Dimension(429, 330));
        setResizable(false);
        setSize(new java.awt.Dimension(429, 330));
        setType(java.awt.Window.Type.UTILITY);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        btnApply.setText("Terapkan");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });
        getContentPane().add(btnApply);
        btnApply.setBounds(320, 270, 100, 27);

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        getContentPane().add(btnBatal);
        btnBatal.setBounds(240, 270, 80, 27);

        btnOke.setText("Oke");
        btnOke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkeActionPerformed(evt);
            }
        });
        getContentPane().add(btnOke);
        btnOke.setBounds(170, 270, 70, 27);

        jPanel6.setLayout(null);

        jLabel5.setText("Dapatkan info dari");
        jPanel6.add(jLabel5);
        jLabel5.setBounds(10, 40, 290, 20);

        sourceSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BMKG", "USGS" }));
        sourceSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceSelActionPerformed(evt);
            }
        });
        jPanel6.add(sourceSel);
        sourceSel.setBounds(310, 40, 90, 25);

        jLabel3.setText("Refresh informasi setiap");
        jPanel6.add(jLabel3);
        jLabel3.setBounds(10, 10, 300, 20);

        refreshInt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        refreshInt.setText("3");
        jPanel6.add(refreshInt);
        refreshInt.setBounds(310, 10, 40, 27);

        jLabel4.setText("Menit");
        jPanel6.add(jLabel4);
        jLabel4.setBounds(361, 10, 40, 20);

        jLabel12.setText("Transparansi bar informasi (perlu restart aplikasi)");
        jPanel6.add(jLabel12);
        jLabel12.setBounds(10, 70, 290, 20);

        transBar.setModel(new javax.swing.SpinnerNumberModel(30, 0, 100, 1));
        transBar.setName(""); // NOI18N
        jPanel6.add(transBar);
        transBar.setBounds(310, 70, 50, 28);

        jLabel1.setText("%");
        jPanel6.add(jLabel1);
        jLabel1.setBounds(370, 70, 20, 20);

        jLabel13.setText("Jendela notifikasi otomatis hilang setelah");
        jPanel6.add(jLabel13);
        jLabel13.setBounds(10, 100, 290, 20);

        hideNotify.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hideNotify.setText("20");
        jPanel6.add(hideNotify);
        hideNotify.setBounds(310, 100, 40, 27);

        jLabel14.setText("Detik");
        jPanel6.add(jLabel14);
        jLabel14.setBounds(360, 100, 30, 20);

        jLabel17.setText("Desain UI (perlu restart aplikasi)");
        jPanel6.add(jLabel17);
        jLabel17.setBounds(10, 130, 290, 20);

        guiSel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Windows", "Windows Classic", "Nimbus", "CDE/Motif", "Metal" }));
        jPanel6.add(guiSel);
        guiSel.setBounds(310, 130, 90, 25);

        jTabbedPane1.addTab("Umum", jPanel6);

        jPanel7.setLayout(null);

        play.setText("Putar suara saat diketahui gempa sebesar");
        play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playActionPerformed(evt);
            }
        });
        jPanel7.add(play);
        play.setBounds(10, 10, 290, 20);
        jPanel7.add(loc);
        loc.setBounds(10, 60, 330, 27);

        btnBrowse.setText("...");
        btnBrowse.setToolTipText("Cari");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        jPanel7.add(btnBrowse);
        btnBrowse.setBounds(370, 60, 30, 20);

        jLabel2.setText(" SR");
        jPanel7.add(jLabel2);
        jLabel2.setBounds(370, 10, 30, 20);

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel15.setText("* restart aplikasi setelah konfigurasi!");
        jPanel7.add(jLabel15);
        jLabel15.setBounds(10, 200, 390, 15);

        jLabel16.setText("Lokasi:");
        jPanel7.add(jLabel16);
        jLabel16.setBounds(10, 40, 70, 15);

        playmag.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 10.0d, 0.10000000149011612d));
        playmag.setEditor(new javax.swing.JSpinner.NumberEditor(playmag, "0.0"));
        jPanel7.add(playmag);
        playmag.setBounds(310, 10, 50, 28);

        playFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Play_16px.png"))); // NOI18N
        playFile.setToolTipText("Putar");
        playFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playFileActionPerformed(evt);
            }
        });
        jPanel7.add(playFile);
        playFile.setBounds(340, 60, 30, 20);

        jTabbedPane1.addTab("Suara", jPanel7);

        jPanel8.setEnabled(false);
        jPanel8.setLayout(null);

        jPanel1.setLayout(null);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("These settings are disabled because of an error occured");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(10, 160, 390, 15);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("If you still want to configure, click Advanced button below.");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(10, 180, 390, 15);

        jButton1.setText("Advanced");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(150, 200, 110, 27);

        jPanel8.add(jPanel1);
        jPanel1.setBounds(0, -70, 420, 290);

        jPanel9.setBackground(new java.awt.Color(51, 51, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel9.setLayout(null);

        jLabel6.setBackground(new java.awt.Color(255, 51, 51));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("BMKG (Badan Meteorologi, Klimatologi dan Geofisika)");
        jLabel6.setOpaque(true);
        jPanel9.add(jLabel6);
        jLabel6.setBounds(0, 0, 380, 15);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("XML");
        jPanel9.add(jLabel7);
        jLabel7.setBounds(10, 30, 23, 15);

        XML_BMKG.setEnabled(false);
        jPanel9.add(XML_BMKG);
        XML_BMKG.setBounds(80, 30, 290, 27);

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Map (GIF)");
        jPanel9.add(jLabel8);
        jLabel8.setBounds(10, 50, 60, 15);

        GIF_BMKG.setEnabled(false);
        jPanel9.add(GIF_BMKG);
        GIF_BMKG.setBounds(80, 50, 290, 27);

        jPanel8.add(jPanel9);
        jPanel9.setBounds(10, 10, 380, 80);

        jPanel10.setBackground(new java.awt.Color(51, 51, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel10.setLayout(null);

        jLabel9.setBackground(new java.awt.Color(255, 51, 51));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("USGS (U.S. Geological Survey)");
        jLabel9.setOpaque(true);
        jPanel10.add(jLabel9);
        jLabel9.setBounds(0, 0, 380, 15);

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("XML");
        jPanel10.add(jLabel10);
        jLabel10.setBounds(10, 30, 23, 15);

        XML_USGS.setEnabled(false);
        jPanel10.add(XML_USGS);
        XML_USGS.setBounds(80, 30, 290, 27);

        jLabel11.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("* map USGS masih belum support");
        jPanel10.add(jLabel11);
        jLabel11.setBounds(180, 60, 209, 15);

        jPanel8.add(jPanel10);
        jPanel10.setBounds(10, 100, 380, 80);

        jTabbedPane1.addTab("URL", jPanel8);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 10, 410, 250);

        btnDef.setText("Default");
        btnDef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefActionPerformed(evt);
            }
        });
        getContentPane().add(btnDef);
        btnDef.setBounds(10, 270, 100, 27);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sourceSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceSelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sourceSelActionPerformed

    private void btnOkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkeActionPerformed
        // TODO add your handling code here:
        Terapkan();
        jPanel1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnOkeActionPerformed

    private void playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playActionPerformed
        // TODO add your handling code here:
        if (play.isSelected() == true) {
            playmag.setEnabled(true);
            loc.setEnabled(true);
            btnBrowse.setEnabled(true);
            playFile.setEnabled(true);
        } else {
            playmag.setEnabled(false);
            loc.setEnabled(false);
            btnBrowse.setEnabled(false);
            playFile.setEnabled(false);
        }
    }//GEN-LAST:event_playActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        jPanel1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        // TODO add your handling code here:
        Terapkan();
    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnDefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefActionPerformed
        // TODO add your handling code here:
        int tanya = JOptionPane.showConfirmDialog(this, "Yakin ingin mengembalikan semua pengaturan ke asalnya?\n\nPERHATIAN: Tindakan ini tidak dapat dikembalikan!", "Reset to Default?", JOptionPane.YES_NO_OPTION);
        if (tanya == JOptionPane.YES_OPTION) {
            // Umum
            guiSel.setSelectedItem("Metal");
            refreshInt.setText("3");
            sourceSel.setSelectedItem("BMKG");
            transBar.setValue(30);
            hideNotify.setText("70");
            
            // Suara
            play.setSelected(true);
            playmag.setValue(4.0);
            loc.setText("default");
            
            // URL
            XML_BMKG.setText("http://data.bmkg.go.id/lastgempadirasakan.xml");
            GIF_BMKG.setText("http://dataweb.bmkg.go.id/INATEWS/eqmap.gif");
            XML_USGS.setText("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.xml");
            
            Terapkan();
            jPanel1.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnDefActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("File Suara (*.wav)", "wav");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loc.setText(selectedFile.getAbsolutePath());
            //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        XML_BMKG.setEnabled(true);
        XML_USGS.setEnabled(true);
        GIF_BMKG.setEnabled(true);
        jPanel1.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void playFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playFileActionPerformed
        // TODO add your handling code here:
        PlayMedia.PlayWAV(loc.getText());
    }//GEN-LAST:event_playFileActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    new frmConfig().setVisible(true);
                    
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField GIF_BMKG;
    private javax.swing.JTextField XML_BMKG;
    private javax.swing.JTextField XML_USGS;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnDef;
    private javax.swing.JButton btnOke;
    private javax.swing.JComboBox<String> guiSel;
    private javax.swing.JTextField hideNotify;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField loc;
    private javax.swing.JCheckBox play;
    private javax.swing.JButton playFile;
    private javax.swing.JSpinner playmag;
    private javax.swing.JTextField refreshInt;
    private javax.swing.JComboBox<String> sourceSel;
    private javax.swing.JSpinner transBar;
    // End of variables declaration//GEN-END:variables

}