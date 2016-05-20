/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.FileActions;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileChooserService.DialogType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import static latexstudio.editor.util.ApplicationUtils.TEX_NAME;

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
        FileActions fileAction = new FileActions();
        
        switch (etc.canOpen()) {
            case SAVE_AND_OPEN:
                fileAction.saveFile();
                fileAction.openFile(FileChooserService.getSelectedFile(TEX_NAME, "TeX files", DialogType.OPEN));
                break;
            
            case OPEN:
                fileAction.openFile(FileChooserService.getSelectedFile(TEX_NAME, "TeX files", DialogType.OPEN));
                break;
                
            default:
                //Do nothing
                break;
        }
    }
}
