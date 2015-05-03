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
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.DropboxRevisionsTopComponent;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileService;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
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
    private final ApplicationLogger LOGGER = new ApplicationLogger("Dropbox");

    @Override
    public void actionPerformed(ActionEvent e) {
        DbxClient client = DbxUtil.getDbxClient();
        
        List<DbxEntryDto> dbxEntries = new ArrayList<DbxEntryDto>();
                             
        try {
            for (DbxEntry entry : client.searchFileAndFolderNames("/", ".tex")) {
                dbxEntries.add(new DbxEntryDto(entry));
            } 
        } catch (DbxException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        JList list = new JList(dbxEntries.toArray());
        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        JOptionPane.showMessageDialog(null, list, "Open file from Dropbox", JOptionPane.PLAIN_MESSAGE);
        
        DbxEntryDto entry = (DbxEntryDto) list.getSelectedValue();
        String localPath = ApplicationUtils.getAppDirectory() + File.separator + entry.getName();
        File outputFile = DbxUtil.downloadRemoteFile(entry, localPath);
        
        drtc.updateRevisionsList(entry.getPath());
        
        String content = FileService.readFromFile(outputFile.getAbsolutePath());
        etc.setEditorContent(content);
        etc.setCurrentFile(outputFile); 
        etc.setDbxState(new DbxState(entry.getPath(), entry.getRevision()));
    }
}
