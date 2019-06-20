/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import javax.swing.JOptionPane;

/**
 *
 * @author Dwi Candra Permanaa
 */
public class LockFile {
    public boolean lockInstance(final String lockFileName) {
        try {
            final File file = new File(lockFileName);
            final RandomAccessFile raf = new RandomAccessFile(file, "rw");
            final FileLock fl = raf.getChannel().tryLock();
            if (fl != null) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        try {
                            fl.release();
                            raf.close();
                            file.delete();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Gagal bekerja!\n\nSolusi: hapus file " + lockFileName +" pada folder aplikasi!\n\n" + e);
                        }
                    }
                });
            return true;
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mengunci/menghapus kunci: " + lockFileName + "\n\n" + e);
        }
        return false;
    }
    
}
