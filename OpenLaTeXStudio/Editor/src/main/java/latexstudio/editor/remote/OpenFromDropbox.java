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
import latexstudio.editor.FileActions;
import latexstudio.editor.RevisionDisplayTopComponent;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Remote",
        id = "latexstudio.editor.remote.OpenFromDropbox"
)
@ActionRegistration(
        displayName = "#CTL_OpenFromDropbox"
)
@ActionReference(path = "Menu/Remote", position = 3408)
@Messages("CTL_OpenFromDropbox=Open from Dropbox")
public final class OpenFromDropbox implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private final DropboxRevisionsTopComponent drtc = new TopComponentFactory<DropboxRevisionsTopComponent>()
            .getTopComponent(DropboxRevisionsTopComponent.class.getSimpleName());
    private final RevisionDisplayTopComponent revtc = new TopComponentFactory<RevisionDisplayTopComponent>()
            .getTopComponent(RevisionDisplayTopComponent.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        DbxFileActions dbxFileAction = new DbxFileActions();
        
        switch (etc.canOpen()) {
            case SAVE_AND_OPEN:
                dbxFileAction.saveProgress(DbxUtil.getDbxClient(), drtc);
                dbxFileAction.openFromDropbox(drtc, revtc);
                break;

            case OPEN:
                dbxFileAction.openFromDropbox(drtc, revtc);
                break;

            default:
                //Do nothing
                break;
        }
    }
}
