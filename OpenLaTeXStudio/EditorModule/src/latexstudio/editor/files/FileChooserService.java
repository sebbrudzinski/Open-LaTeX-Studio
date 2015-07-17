/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.files;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is a simple util class, generating the file/directory chooser modals and
 * returning the user choices.
 *
 * @author Sebastian
 */
public class FileChooserService {

    public enum DialogType {

        SAVE, OPEN, PDF;
    }

    public static File getSelectedDirectory(String buttonText) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = chooser.showDialog(null, buttonText);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            return file;
        }

        return null;
    }

    public static File getSelectedFile(String extension, String description, DialogType type) {
        return getSelectedFile(extension, description, type, false);
    }

    public static File getSelectedFile(String extension, String description, DialogType type, boolean fixExtension) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        chooser.setFileFilter(filter);

        int returnVal = 0;
        switch (type) {
            case SAVE:
                returnVal = chooser.showSaveDialog(null);
                break;
            case OPEN:
                returnVal = chooser.showOpenDialog(null);
                break;
            case PDF:
                returnVal = chooser.showDialog(null, "Generate PDF");
                break;
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.exists() && type == DialogType.OPEN) {
                JOptionPane.showMessageDialog(null, "File doesn't exist", "Item not found", JOptionPane.ERROR_MESSAGE); 
            } 
            else if(file.exists() && type == DialogType.SAVE) {
                int promptResponse = showOverwritePrompt(file);
                while(promptResponse == JOptionPane.NO_OPTION){
                        chooser.showSaveDialog(null);
                        file = chooser.getSelectedFile();
                        if(file.exists()) { 
                            promptResponse = showOverwritePrompt(file);
                        }
                }
                String filePath = file.getAbsolutePath();
                if (fixExtension && !filePath.endsWith("." + extension)) {
                    file = new File(filePath + "." + extension);
                }
                return file;
            }
            else {              
                String filePath = file.getAbsolutePath();
                if (fixExtension && !filePath.endsWith("." + extension)) {
                    file = new File(filePath + "." + extension);
                }

                return file;
            }
        }
        return null;
    }
    
    private static int showOverwritePrompt(File file) {
         return  JOptionPane.showConfirmDialog(null, "File " + file.getName() 
                            + " exists and will be overwritten. Are you sure?", 
                                "Overwrite File?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);  
    }
}