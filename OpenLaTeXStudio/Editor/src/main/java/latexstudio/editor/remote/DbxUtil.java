/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import javax.swing.JOptionPane;
import latexstudio.editor.settings.ApplicationSettings;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Sebastian
 */
public final class DbxUtil {

    private DbxUtil() {
    }

    public static DbxClientV2 getDbxClient() {
        String accessToken = (String) ApplicationSettings.Setting.DROPBOX_TOKEN.getValue();
        if (accessToken == null) {
            JOptionPane.showMessageDialog(null, 
                "The authentication token has not been set.\n"
                        + "Please connect the application to your Dropbox account first!", 
                "Dropbox authentication token not found", JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
        return new DbxClientV2(getDbxConfig(), accessToken); 
    }
    
    public static DbxRequestConfig getDbxConfig() {
        return DbxRequestConfig
                .newBuilder("Open LaTex Studio")
                .withUserLocale(Locale.getDefault().toString())
                .build();
    }
    
    public static File downloadRemoteFile(DbxEntryDto remoteEntry, String localPath) {
        return downloadRemoteFile(remoteEntry.getRevision(), localPath);
    }
    
    public static File downloadRemoteFile(FileMetadata metadata, String localPath) {
        return downloadRemoteFile(metadata.getRev(), localPath);
    }
    
    private static File downloadRemoteFile(String revision, String localPath) {
        DbxClientV2 client = getDbxClient();
        
        FileOutputStream outputStream = null;
        File outputFile = new File(localPath);
        
        try {
            outputStream = new FileOutputStream(outputFile);
            client.files().download(localPath, revision).download(outputStream);
        } catch (DbxException | IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        
        return outputFile;
    }
    
    public static void showDbxAccessDeniedPrompt() {
        JOptionPane.showMessageDialog(null, 
            "The application failed to connect to your Dropbox account.\nThis might mean that "
                    + "your access token is no longer valid or you are not connected to the Internet.", 
            "Could not connect to Dropbox", JOptionPane.ERROR_MESSAGE
        );
    }
}