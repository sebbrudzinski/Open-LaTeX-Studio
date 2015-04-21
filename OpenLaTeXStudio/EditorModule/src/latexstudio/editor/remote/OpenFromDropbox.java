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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.files.FileService;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.pdfbox.io.IOUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

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

        FileOutputStream outputStream = null;
        File outputFile = new File(ApplicationUtils.getAppDirectory() + File.separator + entry.getName());
        
        try {
            outputStream = new FileOutputStream(outputFile);
            client.getFile(entry.getPath(), entry.getRevision(), outputStream);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (DbxException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
                
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc; 

        String content = FileService.readFromFile(outputFile.getAbsolutePath());
        etc.setEditorContent(content);
        etc.setDirty(true);
        etc.setCurrentFile(outputFile);
    }
}
