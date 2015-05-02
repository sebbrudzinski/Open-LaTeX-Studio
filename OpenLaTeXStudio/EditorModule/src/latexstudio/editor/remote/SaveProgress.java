/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JOptionPane;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.io.IOUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        DbxClient client = DbxUtil.getDbxClient();

        String sourceFileName = ApplicationUtils.getTempSourceFile();
        File file = new File(sourceFileName);
        
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        DbxState dbxState = etc.getDbxState();
        
        if (dbxState != null) {
            try {
                DbxEntry.File uploadedFile = client.uploadFile(dbxState.getPath(),
                    DbxWriteMode.update(dbxState.getRevision()), file.length(), inputStream);
                JOptionPane.showMessageDialog(null, 
                        "Successfuly updated file " + uploadedFile.name + " (" + uploadedFile.humanSize + ")",
                        "File updated in Dropbox",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (DbxException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }
}
