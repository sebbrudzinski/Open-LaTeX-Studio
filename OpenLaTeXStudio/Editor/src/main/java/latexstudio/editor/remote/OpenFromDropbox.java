/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import latexstudio.editor.DropboxRevisionsTopComponent;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.RevisionDisplayTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileService;
import latexstudio.editor.util.ApplicationUtils;
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
        Enum canOpen = etc.canOpen();
        
        if(canOpen == EditorTopComponent.CanOpenState.SAVE_AND_OPEN){
            etc.dbxFileAction().saveProgress(drtc);
            etc.dbxFileAction().openFromDropbox(drtc, revtc);
        }
        else if(canOpen == EditorTopComponent.CanOpenState.OPEN){
            etc.dbxFileAction().openFromDropbox(drtc, revtc);
        }
    }
}
