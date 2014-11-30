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
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        chooser.setFileFilter(filter);
        
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            return file;
        }
        
        return null;
    }
}
