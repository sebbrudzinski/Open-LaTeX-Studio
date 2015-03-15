/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.EditorTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Edit",
        id = "latexstudio.editor.Undo"
)
@ActionRegistration(
        iconBase = "latexstudio/editor/resources/icons/arrow-undo.png",
        displayName = "#CTL_Undo"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 100),
    @ActionReference(path = "Toolbars/UndoRedo", position = 3333)
})
@Messages("CTL_Undo=Undo")
public final class Undo implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;
        
        etc.undoAction();
    }
}
