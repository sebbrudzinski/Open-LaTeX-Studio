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

/**
 *
 * @author Labelpm
 */
@ActionID(
        category = "Edit",
        id = "latexstudio.editor.ToggleComment"
)
@ActionRegistration(
        iconBase = "latexstudio/editor/resources/icons/comment_symbol.png",
        displayName = "#CTL_ToggleComment"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 200),
    @ActionReference(path = "Toolbars/Comment", position = 3333)
})
@Messages("CTL_ToggleComment=Toggle Comment")
public final class ToggleComment implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;
        etc.commentOutText();
        etc.setDirty(true);
    }

}
