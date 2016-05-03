package latexstudio.editor;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import latexstudio.editor.files.FileService;
import latexstudio.editor.remote.DbxEntryDto;
import latexstudio.editor.remote.DbxState;
import latexstudio.editor.remote.DbxUtil;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

public class DbxFileActions {

    EditorTopComponent etc;

    public DbxFileActions(EditorTopComponent etc) {
        this.etc = etc;
    }

    public void saveProgress(DropboxRevisionsTopComponent drtc) {
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

        DbxState dbxState = etc.getDbxState();

        if (dbxState != null) {
            try {
                DbxEntry.File uploadedFile = client.uploadFile(dbxState.getPath(),
                        DbxWriteMode.update(dbxState.getRevision()), file.length(), inputStream);
                JOptionPane.showMessageDialog(null,
                        "Successfuly updated file " + uploadedFile.name + " (" + uploadedFile.humanSize + ")",
                        "File updated in Dropbox",
                        JOptionPane.INFORMATION_MESSAGE);
                drtc.updateRevisionsList(uploadedFile.path);
                etc.setDbxState(new DbxState(uploadedFile.path, uploadedFile.rev));
            } catch (DbxException ex) {
                DbxUtil.showDbxAccessDeniedPrompt();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                IOUtils.closeQuietly(inputStream);
                etc.setModified(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Dropbox file has been loaded.\n"
                    + "You must open Dropbox file, before you save it.", "Cannot save progress", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void openFromDropbox(DropboxRevisionsTopComponent drtc, RevisionDisplayTopComponent revtc) {
        DbxClient client = DbxUtil.getDbxClient();

        if (client == null) {
            return;
        }

        List<DbxEntryDto> dbxEntries = new ArrayList<>();

        try {
            for (DbxEntry entry : client.searchFileAndFolderNames("/", ".tex")) {
                dbxEntries.add(new DbxEntryDto(entry));
            }
        } catch (DbxException ex) {
            DbxUtil.showDbxAccessDeniedPrompt();
            return; //No point continuing
        }

        JList<DbxEntryDto> list = new JList(dbxEntries.toArray());
        list.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        int option = JOptionPane.showConfirmDialog(null, list, "Open file from Dropbox", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION && !list.isSelectionEmpty()) {
            DbxEntryDto entry = list.getSelectedValue();
            String localPath = ApplicationUtils.getAppDirectory() + File.separator + entry.getName();
            File outputFile = DbxUtil.downloadRemoteFile(entry, localPath);

            revtc.close();

            drtc.updateRevisionsList(entry.getPath());
            drtc.open();
            drtc.requestActive();

            String content = FileService.readFromFile(outputFile.getAbsolutePath());
            etc.setEditorContent(content);
            etc.setCurrentFile(outputFile);
            etc.setDbxState(new DbxState(entry.getPath(), entry.getRevision()));
            etc.setModified(false);
        }
    }
}
