/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.settings;

import java.util.Properties;

/**
 * A class representing settings of the application.
 * @author Sebastian
 */
public class ApplicationSettings extends Properties {
    
    private static final String DROPBOX_TOKEN = "dropbox.token";
    private static final String USER_LASTDIR = "user.lastdir";
    private static final String USER_LASTFILE = "user.lastfile";
    
    public void setDropboxToken(String token) {
        setProperty(DROPBOX_TOKEN, token);
    }
    
    public String getDropboxToken() {
        return getProperty(DROPBOX_TOKEN);
    }
    
    public void setUserLastDir(String dir) {
        setProperty(USER_LASTDIR, dir);
    }
    
    public String getUserLastDir() {
        return getProperty(USER_LASTDIR);
    }

    public void setUserLastFile(String dir) {
        setProperty(USER_LASTFILE, dir);
    }
    
    public String getUserLastFile() {
        return getProperty(USER_LASTFILE);
    }
}
