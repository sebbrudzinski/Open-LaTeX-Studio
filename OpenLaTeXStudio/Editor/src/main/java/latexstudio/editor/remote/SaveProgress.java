/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.DbxFileActions;
import latexstudio.editor.DropboxRevisionsTopComponent;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Remote",
        id = "latexstudio.editor.remote.SaveProgress"
)
@ActionRegistration(
        displayName = "#CTL_SaveProgress"
)
@ActionReference(path = "Menu/Remote", position = 3420)
@Messages("CTL_SaveProgress=Save Progress")
public final class SaveProgress implements ActionListener {
    
    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private final DropboxRevisionsTopComponent drtc = new TopComponentFactory<DropboxRevisionsTopComponent>()
            .getTopComponent(DropboxRevisionsTopComponent.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        new DbxFileActions().saveProgress(DbxUtil.getDbxClient(), drtc, true);
    }
}
