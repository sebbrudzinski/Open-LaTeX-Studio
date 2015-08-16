/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.util.Exceptions;

/**
 * Class responsible for reading and storing of the user settings.
 * 
 * @author Sebastian
 */
public final class SettingsService {

    private SettingsService() {
    }

    public static ApplicationSettings loadApplicationSettings() {
        ApplicationSettings appSettings = new ApplicationSettings();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(ApplicationUtils.getSettingsFile());
            appSettings.load(fis);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return appSettings;
    }
    
    public static void saveApplicationSettings(ApplicationSettings appSettings) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ApplicationUtils.getSettingsFile());
            appSettings.store(fos, "Open LaTeX Studio application settings");
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
