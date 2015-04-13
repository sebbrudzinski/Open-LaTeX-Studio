/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;
import java.util.Locale;
import latexstudio.editor.settings.SettingsService;

/**
 *
 * @author Sebastian
 */
public final class DbxUtil {
       
    public static DbxClient getDbxClient() {
        String accessToken = SettingsService.loadApplicationSettings().getDropboxToken();
        return new DbxClient(getDbxConfig(), accessToken); 
    }
    
    public static DbxRequestConfig getDbxConfig() {
        return new DbxRequestConfig("Open LaTex Studio",
            Locale.getDefault().toString());
    }
}
