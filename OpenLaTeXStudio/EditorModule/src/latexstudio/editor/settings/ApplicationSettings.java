/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.util.Exceptions;

/**
 * A class representing settings of the application.
 * @author Sebastian
 */
public final class ApplicationSettings extends Properties {
    
    public static final ApplicationSettings INSTANCE = new ApplicationSettings();
    
    private static final String DROPBOX_TOKEN   = "dropbox.token";
    private static final String USER_LASTDIR    = "user.lastdir";
    private static final String LATEX_PATH      = "latex.path";
    
    private ApplicationSettings() {
        load();
    }
    
    public void load() {        
        try {
            load( new FileInputStream( ApplicationUtils.getSettingsFile() ) );
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public void save() {
        try {
            store( new FileOutputStream( ApplicationUtils.getSettingsFile() ), "Open LaTeX Studio application settings" );
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public void setDropboxToken(String token) {
        setProperty(DROPBOX_TOKEN, token);
    }
    
    public String getDropboxToken() {
        return getProperty(DROPBOX_TOKEN);
    }
    
    public void setLatexPath(String path) {
        setProperty(LATEX_PATH, path);
    }
    
    public String getLatexPath() {
        return getProperty(LATEX_PATH);
    }
    
    public void setUserLastDir(String dir) {
        setProperty(USER_LASTDIR, dir);
    }
    
    public String getUserLastDir() {
        return getProperty(USER_LASTDIR);
    }
}
