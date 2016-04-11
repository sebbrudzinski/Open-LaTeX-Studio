/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package latexstudio.editor.menu;

import java.io.File;
import javax.swing.JOptionPane;
import latexstudio.editor.files.FileChooserService;

/**
 * Confirm file saving and show the confirmation dialog
 * @author WhiteHsu
 * @param reply reply from the JOptionPane dialog
 * @param file the selected file
 * @param currentFIle current file saved in Editor Top Component
 * @re
 */
public class SaveConfirmDialog {
    public static File confirmFileSave(int reply, File file, File currentFile) {
        while (file != null && file.exists() && file != currentFile && reply == JOptionPane.NO_OPTION) {            
            reply = JOptionPane.showConfirmDialog(null,
                            file.getAbsoluteFile() + " already exists. Do you want to overwrite it?",
                            "File already exists.",
                            JOptionPane.YES_NO_OPTION);
            
            if (reply == JOptionPane.NO_OPTION) {
                file = FileChooserService.getSelectedFile("tex", "TeX files", FileChooserService.DialogType.SAVE, true);
            }
        }
        
        return file;
    }
}
