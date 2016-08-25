/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.FileActions;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
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
        iconBase = "openlatexstudio/icons/save.png",
        displayName = "#CTL_SaveFile"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = "Toolbars/File", position = 2222),
    @ActionReference(path = "Shortcuts", name = "D-S")
})
@Messages("CTL_SaveFile=Save")
public final class SaveFile implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private static final ApplicationLogger LOGGER = new ApplicationLogger("Open LaTeX Studio");

    @Override
    public void actionPerformed(ActionEvent e) {
        new FileActions().saveFile(FileChooserService.DialogType.SAVE);
    }
}
