/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        iconBase = "latexstudio/editor/resources/icons/comment-symbol24.png",
        displayName = "#CTL_ToggleComment"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 250),
    @ActionReference(path = "Toolbars/Comment", position = 3333)
})
@Messages("CTL_ToggleComment=ToggleComment")
public final class ToggleComment implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;
        etc.commentOutText();
    }

}
