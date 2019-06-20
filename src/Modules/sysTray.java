/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;
import Forms.*;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;

/**
 *
 * @author Dwi Candra Permanaa
 */
public class sysTray {
    private frmMain frmMain;
    private TakeXML TakeXML;
    public boolean TrayStatus;
    
    TrayIcon ti;
    
    public boolean openSystemTray() {
        if (SystemTray.isSupported()) {
//            TakeXML.TakeInfoAutomatically();
            SystemTray tray = SystemTray.getSystemTray();
            final JPopupMenu popup = frmMain.Pop;
            Image image = Toolkit.getDefaultToolkit().getImage("src/Resources/iconTray/main2.png");
            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
//                        popup.setLocation(ae.getXOnScreen(), ae.getYOnScreen());
//                        popup.setInvoker(popup);
//                        popup.setVisible(true);
                }
            };
            
            
            ti = new TrayIcon(image, "JGetQuakeInfo ver. 1.1-alpha [APR 2017]\n\nKlik kanan untuk menu", null);
            
            ti.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        popup.setLocation(e.getXOnScreen(), e.getYOnScreen());
                        popup.setInvoker(popup);
                        popup.setVisible(true);
                    }
                }
            });
            
            ti.addActionListener(listener);
            
            try {
                tray.add(ti);
                ti.displayMessage("JGetQuakeInfo", "Jendela Utama JGetQuakeInfo tersembunyi.\nKlik kanan pada ikon SystemTray untuk membuka menu.", TrayIcon.MessageType.INFO);
                TrayStatus = true;
            } catch (AWTException ex) {
                Logger.getLogger(sysTray.class.getName()).log(Level.SEVERE, null, ex);
                TrayStatus = false;
            }
        } else {
            TrayStatus = false;
        }
        
        return TrayStatus;
    }
    
    public void changeTrayIconImage (String path) {
        if (ti != null) {
            Image image = Toolkit.getDefaultToolkit().getImage(path);
            ti.setImage(image);
        }
    }
}
