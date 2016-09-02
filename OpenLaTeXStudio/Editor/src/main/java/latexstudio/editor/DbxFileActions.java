/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import java.io.File;
import java.io.FileInputStream;
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
import static latexstudio.editor.util.ApplicationUtils.TEX_EXTENSION;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * Provides methods for manipulating on user's dropbox files
 */
public class DbxFileActions {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    
    private static final ApplicationLogger LOGGER = new ApplicationLogger("Cloud Support");

    public DbxFileActions() {
    }

    /**
     * Saves progress to dropbox and updates file history (DropboxRevisions)
     *
     * @param drtc to be updated with a new entry (file history)
     */
    public void saveProgress(DbxClient client, DropboxRevisionsTopComponent drtc, boolean isDialogMsg) {
        DbxState dbxState = etc.getDbxState();

        if (client == null) {
            return;
        }

        File file = new File(ApplicationUtils.getTempSourceFile());

        try (FileInputStream inputStream = new FileInputStream(file)) {
            if (dbxState != null) {
                try {
                    DbxEntry.File uploadedFile = client.uploadFile(dbxState.getPath(),
                            DbxWriteMode.update(dbxState.getRevision()), file.length(), inputStream);
                    
                    if(isDialogMsg)
                        JOptionPane.showMessageDialog(null,
                                "Successfuly updated file " + uploadedFile.name + " (" + uploadedFile.humanSize + ")",
                                "File updated in Dropbox",
                                JOptionPane.INFORMATION_MESSAGE);
                    else
                        LOGGER.log("Successfuly updated file " + uploadedFile.name + " (" + uploadedFile.humanSize + ")");
                    
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
                if(isDialogMsg)
                    JOptionPane.showMessageDialog(null, "No Dropbox file has been loaded.\n"
                            + "You must open Dropbox file, before you save it.", "Cannot save progress", JOptionPane.WARNING_MESSAGE);
                else
                    LOGGER.log("No Dropbox file has been loaded. You must open Dropbox file, before you save it.");
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Shows a .tex files list from user's dropbox and opens the selected one
     *
     * @return List, that contatins user's .tex files from his dropbox; can be
     * empty
     */
    public void openFromDropbox(DropboxRevisionsTopComponent drtc, RevisionDisplayTopComponent revtc) {
        List<DbxEntryDto> dbxEntries = getDbxTexEntries(DbxUtil.getDbxClient());

        if (!dbxEntries.isEmpty()) {
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
                etc.setPreviewDisplayed(false);
            }
        } else{
            JOptionPane.showMessageDialog(etc, "No .tex files found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return List with user's files or empty List if error occurs
     */
    public List<DbxEntryDto> getDbxTexEntries(DbxClient client) {
        List<DbxEntryDto> dbxEntries = new ArrayList<>();

        if (client == null) {
            return dbxEntries;
        }

        try {
            for (DbxEntry entry : client.searchFileAndFolderNames("/", TEX_EXTENSION)) {
                dbxEntries.add(new DbxEntryDto(entry));
            }
        } catch (DbxException ex) {
            DbxUtil.showDbxAccessDeniedPrompt();
            dbxEntries = new ArrayList<>(); //Empty list
        } finally {
            return dbxEntries;
        }
    }
}
