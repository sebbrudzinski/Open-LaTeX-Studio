/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.OutputTopComponent;
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
        id = "latexstudio.editor.SaveFile"
)
@ActionRegistration(
        iconBase = "latexstudio/editor/resources/icons/save.png",
        displayName = "#CTL_SaveFile"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = "Toolbars/File", position = 2222)
})
@Messages("CTL_SaveFile=Save File")
public final class SaveFile implements ActionListener {
    
    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private final ApplicationLogger LOGGER = new ApplicationLogger("Open LaTeX Studio");

    @Override
    public void actionPerformed(ActionEvent e) {                
        String content = etc.getEditorContent();
        File file = etc.getCurrentFile();
        if(file == null) file = FileChooserService.getSelectedFile("tex", "TeX files", DialogType.SAVE, true);
        if (file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            LOGGER.log("Saving file " + file.getAbsolutePath());
            etc.setCurrentFile(file);
        }
    }
}
