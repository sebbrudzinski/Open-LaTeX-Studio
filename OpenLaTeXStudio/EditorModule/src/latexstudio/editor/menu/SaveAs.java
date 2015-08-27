package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileChooserService.DialogType;
import latexstudio.editor.files.FileService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "latexstudio.editor.SaveAs"
)
@ActionRegistration(
        iconBase = "latexstudio/editor/resources/icons/saveas.png",
        displayName = "#CTL_SaveAs"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1301),
    @ActionReference(path = "Toolbars/File", position = 2223)
})
@Messages("CTL_SaveAs=Save As...")
public final class SaveAs implements ActionListener {
    
    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private final ApplicationLogger LOGGER = new ApplicationLogger("Open LaTeX Studio");

    @Override
    public void actionPerformed(ActionEvent e) {                
        String content = etc.getEditorContent();
        File file = FileChooserService.getSelectedFile("tex", "TeX files", DialogType.SAVE, true);
        
        int reply = JOptionPane.NO_OPTION;
        while (file != null && file.exists() && reply == JOptionPane.NO_OPTION) {            
            reply = JOptionPane.showConfirmDialog(null,
                            file.getAbsoluteFile() + " already exists. Do you want to overwrite it?",
                            "File already exists.",
                            JOptionPane.YES_NO_OPTION);
            
            if (reply == JOptionPane.NO_OPTION) {
                file = FileChooserService.getSelectedFile("tex", "TeX files", DialogType.SAVE, true);
            }
        }
        
        if (file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            LOGGER.log("Saving file " + file.getAbsolutePath());
            etc.setCurrentFile(file);
        }
    }
}

