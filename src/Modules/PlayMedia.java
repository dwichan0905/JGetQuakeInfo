/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Dwi Candra Permanaa
 */
public class PlayMedia {
    public void PlayWAV (String pathFile) {
        try {
            File f;
            System.out.println("Playing: " + pathFile);
            if (pathFile.equals("default")) {
                f = new File("src/Resources/default.wav");
            } else {
                f = new File(pathFile);
            }  
            
            InputStream in = new FileInputStream(f);
            AudioStream as = new AudioStream(in);
            
            AudioPlayer.player.start(as);
            System.out.println("Stopped.");
        } catch (IOException ex) {
            System.out.println("Play Error: File Not Found");
            JOptionPane.showMessageDialog(null, "File Audio tidak ditemukan. Cek kembali konfigurasi Anda!", "Audio Not Found", JOptionPane.WARNING_MESSAGE);
            //Logger.getLogger(PlayMedia.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
