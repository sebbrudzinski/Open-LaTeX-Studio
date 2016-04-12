/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.files;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.settings.ApplicationSettings;
import latexstudio.editor.settings.SettingsService;

/**
 * This is a simple util class, generating the file/directory chooser modals and
 * returning the user choices.
 *
 * @author Sebastian
 */
public final class FileChooserService {

    private FileChooserService() {
    }

    public enum DialogType {

        SAVE, SAVEAS, OPEN, PDF;
    }

    public static File getSelectedDirectory(String buttonText) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // retreiving last directory selected by the user
        String currentDirectory = SettingsService.loadApplicationSettings().getUserLastDir();
        if(currentDirectory != null && currentDirectory.length() > 0) {
            chooser.setCurrentDirectory(new File(currentDirectory));
        }
        
        int returnVal = chooser.showDialog(null, buttonText);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            
            // storing last directory selected by the user
            ApplicationSettings appSettings = SettingsService.loadApplicationSettings();
            appSettings.setUserLastDir(file.getAbsolutePath());
            SettingsService.saveApplicationSettings(appSettings);
            
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

        String currentDirectory = SettingsService.loadApplicationSettings().getUserLastDir();
        if(currentDirectory != null && currentDirectory.length() > 0) {
            chooser.setCurrentDirectory(new File(currentDirectory));
        }
        
        int returnVal = 0;
        switch (type) {
            case SAVE:
                returnVal = chooser.showSaveDialog(null);
                break;
            case SAVEAS:
                returnVal = chooser.showSaveDialog(null);
                break;
            case OPEN:
                returnVal = chooser.showOpenDialog(null);
                break;
            case PDF:
                returnVal = chooser.showDialog(null, "Generate PDF");
                break;
            default:
                break;
        }
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.exists() && type == DialogType.OPEN) {
                JOptionPane.showMessageDialog(null, "File doesn't exist", "Item not found", JOptionPane.ERROR_MESSAGE); 
            } else {              
                String filePath = file.getAbsolutePath();
                if (fixExtension && !filePath.endsWith("." + extension)) {
                    file = new File(filePath + "." + extension);
                }
                
                // storing last directory selected by the user
                ApplicationSettings appSettings = SettingsService.loadApplicationSettings();
                appSettings.setUserLastDir(file.getParentFile().getAbsolutePath());
                SettingsService.saveApplicationSettings(appSettings);

                return file;
            }
        }
        return null;
    }
    
    /**
    * Get the selected file with Saving Confirmation
    * @author WhiteHsu
    */
    public static File getFileWithConfirmation(File currentFile, String extension, String description, DialogType type, boolean fixExtension) {
        File file = currentFile;
        if(file == null || type == DialogType.SAVEAS)
            file = FileChooserService.getSelectedFile(extension, description, type, fixExtension);
        
        file = FileChooserService.confirmFileSave(file, currentFile);
        
        return file;
    }
    
    /**
    * Confirm file saving and show the confirmation dialog
    * @author WhiteHsu
    * @param file the selected file
    * @param currentFIle current file saved in Editor Top Component
    * @return the selected file object
    */
    public static File confirmFileSave(File file, File currentFile) {
        int reply = JOptionPane.NO_OPTION;
        while (file != null && file.exists() && file != currentFile && reply == JOptionPane.NO_OPTION) {            
            reply = JOptionPane.showConfirmDialog(null,
                            file.getAbsoluteFile() + " already exists. Do you want to overwrite it?",
                            "File already exists",
                            JOptionPane.YES_NO_OPTION);
            
            if (reply == JOptionPane.NO_OPTION) {
                file = FileChooserService.getSelectedFile("tex", "TeX files", FileChooserService.DialogType.SAVE, true);
            }
        }
        
        return file;
    }
}
