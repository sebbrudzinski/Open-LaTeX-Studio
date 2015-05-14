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
    
    private final static String DROPBOX_TOKEN = "dropbox.token";
    
    public void setDropboxToken(String token) {
        setProperty(DROPBOX_TOKEN, token);
    }
    
    public String getDropboxToken() {
        return getProperty(DROPBOX_TOKEN);
    }
}
