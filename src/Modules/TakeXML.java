 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;
import Forms.*;
import java.awt.TrayIcon;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Dwi Candra Permanaa
 */
public class TakeXML {
    private String HariTgl;
    private String Kekuatan;
    private String Dalam;
    private String Geografis;
    private String Wilayah1;
    private String Potensi;
    private String alamatGIF;
    private String HariTgl2, Kekuatan2, Dalam2, Geografis2, Wilayah12, Potensi2, alamatGIF2;
    
    private frmInfo frmInfo;
    private frmBar frmBar;
    private PlayMedia PlayMedia;
    private sysTray sysTray;
    
    private static frmDebug frmDebug;
    private frmLastInfo frmLastInfo;
    
    public TakeXML() {
        frmInfo = new frmInfo();
        frmDebug = new frmDebug();
        frmBar = new frmBar();
        PlayMedia = new PlayMedia();
        frmLastInfo = new frmLastInfo();
        sysTray = new sysTray();
    }
    
    public boolean DapatkanXML_BMKG (String mediaPath, double magnitude_min) {
        try {
            // read config
            File configFile = new File("src/Prefer/JGQI_Config.properties");
            FileReader read = new FileReader(configFile);
            Properties prop = new Properties();
            
            prop.load(read);
            
            String BMKG_URL = prop.getProperty("bmkg_url");
            String BMKG_GIF = prop.getProperty("bmkg_gif");
            String waveS = prop.getProperty("warn");
            boolean waveStatus = Boolean.parseBoolean(waveS);
            
            //System.out.println(BMKG_URL);
            //read.close();
            
            // download and read it
            URL url = new URL(BMKG_URL);
            boolean tes = isInternetReachable(BMKG_URL);
            if (tes == true) {
                sysTray.changeTrayIconImage("src/Resources/iconTray/retrieve.png");
                File lastInfo = new File("src/Prefer/LastInfo.properties");
                FileReader reader = new FileReader(lastInfo);
                Properties prop2 = new Properties();
                prop2.load(reader);
                InputStream link = url.openStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuild = dbFactory.newDocumentBuilder();
                Document doc = dBuild.parse(link);

                doc.getDocumentElement().normalize();

                NodeList ndList = doc.getElementsByTagName("Gempa");

                for (int temp = 0; temp < ndList.getLength(); temp++) {
                    Node nNode = ndList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele = (Element) nNode;
                        //ele.getChildNodes();
                        HariTgl = String.valueOf(ele.getElementsByTagName("Tanggal").item(0).getTextContent() + " " + ele.getElementsByTagName("Jam").item(0).getTextContent());
                        Geografis = String.valueOf(ele.getElementsByTagName("Lintang").item(0).getTextContent()) + ", " + String.valueOf(ele.getElementsByTagName("Bujur").item(0).getTextContent());
                        Kekuatan = String.valueOf(ele.getElementsByTagName("Magnitude").item(0).getTextContent());
                        Dalam = String.valueOf(ele.getElementsByTagName("Kedalaman").item(0).getTextContent());
                        Wilayah1 = String.valueOf(ele.getElementsByTagName("Dirasakan").item(0).getTextContent());
                        Potensi = String.valueOf(ele.getElementsByTagName("Keterangan").item(0).getTextContent());
                        
                        HariTgl2 = prop2.getProperty("DateTime");
                        Kekuatan2 = prop2.getProperty("Strength");
                        Dalam2 = prop2.getProperty("Depth");
                        Geografis2 = prop2.getProperty("Geo_Pos");
                        Wilayah12 = prop2.getProperty("Area");
                        Potensi2 = prop2.getProperty("Tsunami");
                        alamatGIF = prop.getProperty("bmkg_gif");
                        
                        //int epe = HariTgl.compareToIgnoreCase(HariTgl2);
                                
                        if (!HariTgl2.equals(HariTgl)) {
                            FileWriter write = new FileWriter(lastInfo);
                            prop2.setProperty("DateTime", HariTgl);
                            prop2.setProperty("Strength", Kekuatan);
                            prop2.setProperty("Depth", Dalam);
                            prop2.setProperty("Geo_Pos", Geografis);
                            prop2.setProperty("Area", Wilayah1);
                            prop2.setProperty("Tsunami", Potensi);
                            prop2.setProperty("GIF", alamatGIF);

                            prop2.store(write, null);
                            //sysTray.ti.displayMessage("JGetQuakeInfo", "Anda mendapatkan informasi tentang gempa terbaru dari BMKG!", TrayIcon.MessageType.INFO);
                        } else {
                            sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
                            return false;
                        }
                    }
                }
                String k = Kekuatan.substring(0, Kekuatan.length() - 3);
                double magn = Double.parseDouble(k);
                //System.out.println(magn);
                if (magn >= magnitude_min && waveStatus == true) {
                    PlayMedia.PlayWAV(mediaPath);
                }
                read.close();
                reader.close();
                
                final Icon i = new ImageIcon(alamatGIF);
                
                frmBar.dispose();
                frmBar.setVisible(true);
                frmBar.info.setText(HariTgl.toUpperCase() + " | " + Kekuatan.toUpperCase() + " | " + Dalam.toUpperCase() + " | " + Wilayah1.toUpperCase());
                frmBar.info.setToolTipText("<html>" + HariTgl.toUpperCase() + " | " + Kekuatan.toUpperCase() + " | " + Dalam.toUpperCase() + " | " + Wilayah1.toUpperCase() + "<br/><br/><i>Klik, dan klik kanan untuk menampilkan menu.</i></html>");
                
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        frmInfo.setVisible(true);
                        frmInfo.setTitle("NEW: " + HariTgl + " at " + Wilayah1);
                        frmInfo.datetime.setText(HariTgl);
                        frmInfo.strength.setText(Kekuatan);
                        frmInfo.depth.setText(Dalam);
                        frmInfo.geo.setText(Geografis);
                        frmInfo.area.setText(Wilayah1);
                        frmInfo.tsunami.setText(Potensi);
                        frmInfo.datetime.setToolTipText(HariTgl);
                        frmInfo.strength.setToolTipText(Kekuatan);
                        frmInfo.depth.setToolTipText(Dalam);
                        frmInfo.geo.setToolTipText(Geografis);
                        frmInfo.area.setToolTipText(Wilayah1);
                        frmInfo.tsunami.setToolTipText(Potensi);
                        //frmInfo.gif.setIcon(i);
                        frmInfo.requestFocus();
                        frmInfo.MenghilanglahWahaiJendelaku();
                    }
                }, 5*60*10);
                
                return true;
            } else {
                return false;
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
//        } catch (IOException | ParserConfigurationException ex) {    
                System.out.println(ex.getSuppressed());
                String errTit = ex.getMessage();
                CharArrayWriter cw = new CharArrayWriter();
                PrintWriter w = new PrintWriter(cw);
                ex.printStackTrace(w);
                w.close();
                String trace = cw.toString();

            if (trace.contains("SAXParseException")) {
                JOptionPane.showMessageDialog(null, "Pembacaan informasi dari BMKG gagal!\n\nKeterangan:\n" + ex.toString());
                sysTray.ti.displayMessage("JGetQuakeInfo", "Gagal membaca informasi dari BMKG!\nKeterangan:\n" + ex.toString(), TrayIcon.MessageType.ERROR);
                return false;
            } else if (trace.contains("ConnectException")) {
                sysTray.ti.displayMessage("JGetQuakeInfo", "Gagal tersambung ke internet.\nPeriksa koneksi internet anda.", TrayIcon.MessageType.WARNING);
                return false;
            } else {
                sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
                frmDebug.setVisible(true);
                frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
                Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } catch (NullPointerException e) {
            sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
            return false;
        }
    }
    
    public boolean DapatkanXML_USGS (String mediaPath, double magnitude_min) {
        try {
            // read config
            File configFile = new File("src/Prefer/JGQI_Config.properties");
            FileReader read = new FileReader(configFile);
            Properties prop = new Properties();
            
            prop.load(read);
            
            String BMKG_URL = prop.getProperty("bmkg_url");
            String BMKG_GIF = prop.getProperty("bmkg_gif");
            String waveS = prop.getProperty("warn");
            boolean waveStatus = Boolean.parseBoolean(waveS);
            
            //System.out.println(BMKG_URL);
            //read.close();
            
            // download and read it
            URL url = new URL(BMKG_URL);
            boolean tes = isInternetReachable(BMKG_URL);
            if (tes == true) {
                sysTray.changeTrayIconImage("src/Resources/iconTray/retrieve.png");
                File lastInfo = new File("src/Prefer/LastInfo.properties");
                FileReader reader = new FileReader(lastInfo);
                Properties prop2 = new Properties();
                prop2.load(reader);
                InputStream link = url.openStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuild = dbFactory.newDocumentBuilder();
                Document doc = dBuild.parse(link);

                doc.getDocumentElement().normalize();

                NodeList ndList = doc.getElementsByTagName("event");
                NodeList ndListA = doc.getElementsByTagName("description");
                NodeList ndListB = doc.getElementsByTagName("origin");
                    NodeList ndListB1 = doc.getElementsByTagName("time");
                    NodeList ndListB2 = doc.getElementsByTagName("longitude");
                    NodeList ndListB3 = doc.getElementsByTagName("latitude");
                    NodeList ndListB4 = doc.getElementsByTagName("depth");
                NodeList ndListC = doc.getElementsByTagName("magnitude");
                    NodeList ndListC1 = doc.getElementsByTagName("mag");
                    
                for (int temp = 0; temp < ndList.getLength(); temp++) {
                    Node nNode = ndList.item(temp);
                    Node nNode2 = ndListA.item(temp);
                    Node nNode3 = ndListB1.item(temp);
                    Node nNode4 = ndListB2.item(temp);
                    Node nNode5 = ndListB3.item(temp);
                    Node nNode6 = ndListB4.item(temp);
                    Node nNode7 = ndListC1.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele = (Element) nNode;
                        Element ele2 = (Element) nNode2;
                        Element ele3 = (Element) nNode3;
                        Element ele4 = (Element) nNode4;
                        Element ele5 = (Element) nNode5;
                        Element ele6 = (Element) nNode6;
                        Element ele7 = (Element) nNode7;
                        //ele.getChildNodes();
                        HariTgl = String.valueOf(ele3.getElementsByTagName("value").item(0).getTextContent());
                        Geografis = String.valueOf(ele4.getElementsByTagName("value").item(0).getTextContent()) + "°N, " + String.valueOf(ele5.getElementsByTagName("value").item(0).getTextContent().substring(1) + "°W");
                        Kekuatan = String.valueOf(ele7.getElementsByTagName("value").item(0).getTextContent());
                        int Dalams = Integer.parseInt(String.valueOf(ele6.getElementsByTagName("value").item(0).getTextContent()));
                        Dalams = Dalams / 1000;
                        Dalam = String.valueOf(Dalams) + " KM";
                        Wilayah1 = String.valueOf(ele2.getElementsByTagName("text").item(0).getTextContent());
                        Potensi = "Event ID: "+String.valueOf(ele.getAttribute("catalog:eventid"));
                        
                        HariTgl2 = prop2.getProperty("DateTime");
                        Kekuatan2 = prop2.getProperty("Strength");
                        Dalam2 = prop2.getProperty("Depth");
                        Geografis2 = prop2.getProperty("Geo_Pos");
                        Wilayah12 = prop2.getProperty("Area");
                        Potensi2 = prop2.getProperty("Tsunami");
                        alamatGIF = prop.getProperty("bmkg_gif");
                        
                        //int epe = HariTgl.compareToIgnoreCase(HariTgl2);
                                
                        if (!HariTgl2.equals(HariTgl)) {
                            FileWriter write = new FileWriter(lastInfo);
                            prop2.setProperty("DateTime", HariTgl);
                            prop2.setProperty("Strength", Kekuatan);
                            prop2.setProperty("Depth", Dalam);
                            prop2.setProperty("Geo_Pos", Geografis);
                            prop2.setProperty("Area", Wilayah1);
                            prop2.setProperty("Tsunami", Potensi);
                            prop2.setProperty("GIF", alamatGIF);

                            prop2.store(write, null);
                            //sysTray.ti.displayMessage("JGetQuakeInfo", "Anda mendapatkan informasi tentang gempa terbaru dari BMKG!", TrayIcon.MessageType.INFO);
                        } else {
                            sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
                            return false;
                        }
                    }
                }
                String k = Kekuatan.substring(0, Kekuatan.length() - 3);
                double magn = Double.parseDouble(k);
                //System.out.println(magn);
                if (magn >= magnitude_min && waveStatus == true) {
                    PlayMedia.PlayWAV(mediaPath);
                }
                read.close();
                reader.close();
                
                final Icon i = new ImageIcon(alamatGIF);
                
                frmBar.dispose();
                frmBar.setVisible(true);
                frmBar.info.setText(HariTgl.toUpperCase() + " | " + Kekuatan.toUpperCase() + " | " + Dalam.toUpperCase() + " | " + Wilayah1.toUpperCase());
                frmBar.info.setToolTipText("<html>" + HariTgl.toUpperCase() + " | " + Kekuatan.toUpperCase() + " | " + Dalam.toUpperCase() + " | " + Wilayah1.toUpperCase() + "<br/><br/><i>Klik, dan klik kanan untuk menampilkan menu.</i></html>");
                
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        frmInfo.setVisible(true);
                        frmInfo.setTitle("NEW: " + HariTgl + " at " + Wilayah1);
                        frmInfo.datetime.setText(HariTgl);
                        frmInfo.strength.setText(Kekuatan);
                        frmInfo.depth.setText(Dalam);
                        frmInfo.geo.setText(Geografis);
                        frmInfo.area.setText(Wilayah1);
                        frmInfo.tsunami.setText(Potensi);
                        frmInfo.datetime.setToolTipText(HariTgl);
                        frmInfo.strength.setToolTipText(Kekuatan);
                        frmInfo.depth.setToolTipText(Dalam);
                        frmInfo.geo.setToolTipText(Geografis);
                        frmInfo.area.setToolTipText(Wilayah1);
                        frmInfo.tsunami.setToolTipText(Potensi);
                        //frmInfo.gif.setIcon(i);
                        frmInfo.requestFocus();
                        frmInfo.MenghilanglahWahaiJendelaku();
                    }
                }, 5*60*10);
                
                return true;
            } else {
                return false;
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
//        } catch (IOException | ParserConfigurationException ex) {    
                System.out.println(ex.getSuppressed());
                String errTit = ex.getMessage();
                CharArrayWriter cw = new CharArrayWriter();
                PrintWriter w = new PrintWriter(cw);
                ex.printStackTrace(w);
                w.close();
                String trace = cw.toString();

            if (trace.contains("SAXParseException")) {
                JOptionPane.showMessageDialog(null, "Pembacaan informasi dari BMKG gagal!\n\nKeterangan:\n" + ex.toString());
                sysTray.ti.displayMessage("JGetQuakeInfo", "Gagal membaca informasi dari BMKG!\nKeterangan:\n" + ex.toString(), TrayIcon.MessageType.ERROR);
                return false;
            } else if (trace.contains("ConnectException")) {
//                sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
//                frmDebug.setVisible(true);
//                frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
//                Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
                sysTray.ti.displayMessage("JGetQuakeInfo", "Gagal tersambung ke internet.\nPeriksa koneksi internet anda.", TrayIcon.MessageType.WARNING);
                return false;
            } else {
                sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
                frmDebug.setVisible(true);
                frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
                Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } catch (NullPointerException e) {
            sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
//                System.out.println(e.getSuppressed());
//                String errTit = e.getMessage();
//                CharArrayWriter cw = new CharArrayWriter();
//                PrintWriter w = new PrintWriter(cw);
//                e.printStackTrace(w);
//                w.close();
//                String trace = cw.toString();
//                frmDebug.setVisible(true);
//                frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
//                Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
    
    public boolean DapatkanXMLPaksa_USGS (String mediaPath, double magnitude_min) {
        return false;
    }
    
    public boolean DapatkanXMLPaksa_BMKG (String mediaPath, double magnitude_min) {
        try {
            // read config
            File configFile = new File("src/Prefer/JGQI_Config.properties");
            FileReader read = new FileReader(configFile);
            Properties prop = new Properties();
            
            prop.load(read);
            
            String BMKG_URL = prop.getProperty("bmkg_url");
            String BMKG_GIF = prop.getProperty("bmkg_gif");
            
            //System.out.println(BMKG_URL);
            //read.close();
            
            // download and read it
            URL url = new URL(BMKG_URL);
            boolean tes = isInternetReachable(BMKG_URL);
            if (tes == true) {
                sysTray.changeTrayIconImage("src/Resources/iconTray/retrieve.png");
                File lastInfo = new File("src/Prefer/LastInfo.properties");
                FileReader reader = new FileReader(lastInfo);
                Properties prop2 = new Properties();
                prop2.load(reader);
                InputStream link = url.openStream();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuild = dbFactory.newDocumentBuilder();
                Document doc = dBuild.parse(link);

                doc.getDocumentElement().normalize();

                NodeList ndList = doc.getElementsByTagName("Gempa");

                for (int temp = 0; temp < ndList.getLength(); temp++) {
                    Node nNode = ndList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ele = (Element) nNode;
                        //ele.getChildNodes();
                        HariTgl = String.valueOf(ele.getElementsByTagName("Tanggal").item(0).getTextContent() + " " + ele.getElementsByTagName("Jam").item(0).getTextContent());
                        Kekuatan = String.valueOf(ele.getElementsByTagName("Magnitude").item(0).getTextContent());
                        Dalam = String.valueOf(ele.getElementsByTagName("Kedalaman").item(0).getTextContent());
                        Geografis = String.valueOf(ele.getElementsByTagName("Lintang").item(0).getTextContent()) + ", " + String.valueOf(ele.getElementsByTagName("Bujur").item(0).getTextContent());
                        Wilayah1 = String.valueOf(ele.getElementsByTagName("Dirasakan").item(0).getTextContent());
                        Potensi = String.valueOf(ele.getElementsByTagName("Keterangan").item(0).getTextContent());
                        alamatGIF = prop.getProperty("bmkg_gif");
                        
                        sysTray.changeTrayIconImage("src/Resources/iconTray/at_risk.png");
                        
                            FileWriter write = new FileWriter(lastInfo);
                            prop2.setProperty("DateTime", HariTgl);
                            prop2.setProperty("Strength", Kekuatan);
                            prop2.setProperty("Depth", Dalam);
                            prop2.setProperty("Geo_Pos", Geografis);
                            prop2.setProperty("Area", Wilayah1);
                            prop2.setProperty("Tsunami", Potensi);
                            prop2.setProperty("GIF", alamatGIF);

                            prop2.store(write, null);
                    }
                }
//                String k = Kekuatan.substring(0, Kekuatan.length() - 3);
//                double magn = Double.parseDouble(k);
//                //System.out.println(magn);
//                if (magn >= magnitude_min) {
//                    PlayMedia.PlayWAV(mediaPath);
//                }
                read.close();
                reader.close();
                
                frmBar.dispose();
                frmBar.setVisible(true);
                frmBar.info.setText(HariTgl.toUpperCase() + " | " + Kekuatan.toUpperCase() + " | " + Dalam.toUpperCase() + " | " + Wilayah1.toUpperCase());
                frmBar.info.setToolTipText("<html>" + HariTgl.toUpperCase() + " | " + Kekuatan.toUpperCase() + " | " + Dalam.toUpperCase() + " | " + Wilayah1.toUpperCase() + "<br/><br/><i>Klik, dan klik kanan untuk menampilkan menu.</i></html>");
                frmLastInfo.setVisible(true);
                frmLastInfo.datetime.setText(HariTgl);
                frmLastInfo.strength.setText(Kekuatan);
                frmLastInfo.depth.setText(Dalam);
                frmLastInfo.geo.setText(Geografis);
                frmLastInfo.area.setText(Wilayah1);
                frmLastInfo.tsunami.setText(Potensi);
                frmLastInfo.datetime.setToolTipText(HariTgl);
                frmLastInfo.strength.setToolTipText(Kekuatan);
                frmLastInfo.depth.setToolTipText(Dalam);
                frmLastInfo.geo.setToolTipText(Geografis);
                frmLastInfo.area.setToolTipText(Wilayah1);
                frmLastInfo.tsunami.setToolTipText(Potensi);
                frmLastInfo.requestFocus();
                
                return true;
            } else {
                return false;
            }
        } catch (IOException | ParserConfigurationException | SAXException ex) {
//        } catch (IOException | ParserConfigurationException ex) {    
                System.out.println(ex.getSuppressed());
                String errTit = ex.getMessage();
                CharArrayWriter cw = new CharArrayWriter();
                PrintWriter w = new PrintWriter(cw);
                ex.printStackTrace(w);
                w.close();
                String trace = cw.toString();

            if (trace.contains("SAXParseException")) {
                JOptionPane.showMessageDialog(null, "Pembacaan file XML Gagal!\n\nKeterangan:\n" + ex.toString());
                sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
                return false;
            } else if (trace.contains("ConnectException")) {
                sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
//                System.out.println(e.getSuppressed());
//                String errTit = e.getMessage();
//                CharArrayWriter cw = new CharArrayWriter();
//                PrintWriter w = new PrintWriter(cw);
//                e.printStackTrace(w);
//                w.close();
//                String trace = cw.toString();
//                frmDebug.setVisible(true);
//                frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
//                Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } else {
                sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
//                frmDebug.setVisible(true);
//                frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
                Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } catch (NullPointerException e) {
            sysTray.changeTrayIconImage("src/Resources/iconTray/safe.png");
            return false;
        }
    }
    
    private static boolean isInternetReachable(String link) {
        try {
            //make a URL to a known source
            URL url = new URL(link);

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();
            
            //returning "true" into variable
            return true;
        } catch (UnknownHostException | ConnectException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getSuppressed());
            String errTit = e.getMessage();
            CharArrayWriter cw = new CharArrayWriter();
            PrintWriter w = new PrintWriter(cw);
            e.printStackTrace(w);
            w.close();
            String trace = cw.toString();

            frmDebug.setVisible(true);
            frmDebug.log.setText(trace + "\n" + String.valueOf(System.getProperties()));
            Logger.getLogger(frmConfig.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        
    }
    
    public void TakeInfoAutomatically() {
        try {
            File f = new File("src/Prefer/JGQI_Config.properties");
            FileReader in = new FileReader(f);
            Properties prop = new Properties();
            
            prop.load(in);
            
            String watch = prop.getProperty("refreshint");
            final String info = prop.getProperty("info");
            final String medPath = prop.getProperty("warn_path");
            String mg = prop.getProperty("warn_mag");
            final float mag = Float.parseFloat(mg);
            int time = Integer.parseInt(watch);
            
            if (info.equals("BMKG")) {
                DapatkanXML_BMKG(medPath, mag);
            } else {
                DapatkanXML_USGS(medPath, mag);
            }
            
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (info.equals("BMKG")) {
                        DapatkanXML_BMKG(medPath, mag);
                    } else {
                        DapatkanXML_USGS(medPath, mag);
                    }
                }
            }, time * 60 * 1000, time * 60 * 1000);
            
            
        } catch (IOException ex) {
            Logger.getLogger(TakeXML.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
