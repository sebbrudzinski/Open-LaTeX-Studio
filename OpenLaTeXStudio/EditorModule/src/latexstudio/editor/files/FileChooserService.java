/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.files;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Sebastian
 */
public class FileChooserService {
    
    public static File getSelectedFile(String extension, String description) {
        return getSelectedFile(extension, description, false);
    }
    
    public static File getSelectedFile(String extension, String description, boolean fixExtension) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        chooser.setFileFilter(filter);
        
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            
            String filePath = file.getAbsolutePath();
            if (fixExtension && !filePath.endsWith("." + extension)) {
                file = new File(filePath + "." + extension);
            }
            
            return file;
        }
        
        return null;
    }
}
