/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileChooserService.DialogType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "latexstudio.editor.OpenFile"
)
@ActionRegistration(
        displayName = "#CTL_OpenFile"
)
@ActionReference(path = "Menu/File", position = 1200)
@Messages("CTL_OpenFile=Open File")

public final class OpenFile implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        Enum canOpen = etc.canOpen();
        
        if (canOpen == EditorTopComponent.CanOpenState.SAVE_AND_OPEN) {
            etc.fileAction().saveFile();
            etc.fileAction().openFile(FileChooserService.getSelectedFile("tex", "TeX files", DialogType.OPEN));

        } else if (canOpen == EditorTopComponent.CanOpenState.OPEN) {
            etc.fileAction().openFile(FileChooserService.getSelectedFile("tex", "TeX files", DialogType.OPEN));
        }
    }
}
