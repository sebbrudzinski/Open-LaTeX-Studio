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
import latexstudio.editor.RevisionDisplayTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.io.IOUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import static latexstudio.editor.util.ApplicationUtils.TEX_EXTENSION;

@ActionID(
        category = "Remote",
        id = "latexstudio.editor.remote.UploadToDropbox"
)
@ActionRegistration(
        displayName = "#CTL_UploadToDropbox"
)
@ActionReference(path = "Menu/Remote", position = 3433, separatorAfter = 3483)
@Messages("CTL_UploadToDropbox=Upload to Dropbox")
public final class UploadToDropbox implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private final RevisionDisplayTopComponent revtc = new TopComponentFactory<RevisionDisplayTopComponent>()
            .getTopComponent(RevisionDisplayTopComponent.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        DbxClient client = DbxUtil.getDbxClient();
        
        if (client == null) {
            return;
        }

        String sourceFileName = ApplicationUtils.getTempSourceFile();
        File file = new File(sourceFileName);
        
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        String defaultFileName = etc.getEditorState().getCurrentFile() == null ? "welcome.tex" : etc.getEditorState().getCurrentFile().getName();
        String fileName = (String) JOptionPane.showInputDialog(null, "Please enter file name", "Upload file", 
                JOptionPane.INFORMATION_MESSAGE, null, null, defaultFileName);
        if (fileName != null) {
            fileName = fileName.endsWith(TEX_EXTENSION) ? fileName : fileName.concat(TEX_EXTENSION);
            try {
                DbxEntry.File uploadedFile = client.uploadFile("/OpenLaTeXStudio/" + fileName,
                    DbxWriteMode.add(), file.length(), inputStream);
                JOptionPane.showMessageDialog(null, 
                        "Successfuly uploaded file " + uploadedFile.name + " (" + uploadedFile.humanSize + ")",
                        "File uploaded to Dropbox",
                        JOptionPane.INFORMATION_MESSAGE);
                revtc.close();
            } catch (DbxException ex) {
                DbxUtil.showDbxAccessDeniedPrompt();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }
}
