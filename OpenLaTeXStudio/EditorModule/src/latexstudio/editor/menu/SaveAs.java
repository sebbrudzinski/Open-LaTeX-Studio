/* @author rbanna01
 * See file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

@ActionID (
        category = "File",
        id = "latexstudio.editor.SaveAs"
)
@ActionRegistration (
        iconBase = "latexstudio/editor/resources/icons/save.png",
        displayName= "#CTL_SaveAs"
)
@ActionReferences ({
        @ActionReference(path="Menu/File", position = 1400),
        @ActionReference(path="Toolbars/File", position = 2300)
})
@Messages("CTL_SaveAs= Save As")
public class SaveAs implements ActionListener {
      private final EditorTopComponent ETC = new TopComponentFactory<EditorTopComponent>()
              .getTopComponent(EditorTopComponent.class.getSimpleName());
      private final ApplicationLogger LOGGER = new ApplicationLogger("Open LaTeX Studio");
    @Override
    public void actionPerformed(ActionEvent aE) {
        String content = ETC.getEditorContent();
        File file = FileChooserService.getSelectedFile("tex", "TeX files", 
                DialogType.SAVE, true);
        if(file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            LOGGER.log("Saving file as " + file.getAbsolutePath());
            ETC.setCurrentFile(file);
        }
       
    }   
}
