/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * This is a helper class, that resolves the location of certain files, used
 * by the application.
 * 
 * @author Sebastian
 */
public final class ApplicationUtils {
    
    private static final String HOME = System.getProperty("user.home");
    private static final String OS = System.getProperty("os.name");
    private static final String APP_DIR_NAME = ".OpenLaTeXStudio";
    private static final String APP_VERSION_FILENAME = "openlatexstudio/version.txt";
    private static final String PREVIEW_SOURCE_FILENAME = "preview.tex";
    private static final String PREVIEW_PDF_FILENAME = "preview.pdf";
    private static final String SETTINGS_FILENAME = "settings.properties";
    
    public static final String PDFLATEX = "pdflatex";
    
    public static final String TEMPLATES_DIR = "/openlatexstudio/templates/";
    public static final String TEMPLATES_FILE = "/openlatexstudio/templates.xml";

    private ApplicationUtils() {
    }
    
    public static String getPathToTEX(String texDirectory) {
        if (texDirectory == null || texDirectory.equals("")) {
            return PDFLATEX;
        } else {
            if (texDirectory.endsWith(File.separator)) {
                return texDirectory.concat(PDFLATEX);
            } else {
                return texDirectory.concat(File.separator).concat(PDFLATEX);
            }
        }
    }
    
    public static String getTempSourceFile() {
        return getAppDirectory().concat(File.separator).concat(PREVIEW_SOURCE_FILENAME);
    }
    
    public static String getTempPDFFile() {
        return getAppDirectory().concat(File.separator).concat(PREVIEW_PDF_FILENAME);
    }
    
    public static String getSettingsFile() {
        String settingsFileName = getAppDirectory().concat(File.separator).concat(SETTINGS_FILENAME);
        File file = new File(settingsFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        return settingsFileName;
    }
    
    public static String getAppDirectory() {
        File tempDir = new File(HOME + File.separator + APP_DIR_NAME);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return tempDir.getAbsolutePath();
    }
    
    public static String getAppVersion() {
        URL url = ApplicationUtils.class.getClassLoader().getResource(APP_VERSION_FILENAME);
        String version = "UNKNOWN";
        InputStream is = null;
        try {
            is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                // ?!? Allow the use of comments and optionally supplementary info in version.txt           
                if (!line.startsWith("#")) {                    
                    String[] pairs = Pattern.compile("=").split(line);             
                    if (pairs[0].equals("version")) {                        
                        version = pairs[1];
                    }
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return version;
    }
    
    public static void deleteTempFiles() {
        File source = new File(getTempSourceFile());
        File tmpPdf = new File(getTempPDFFile());
        if (source.exists()) {
            source.delete();
        }
        if (tmpPdf.exists()) {
            tmpPdf.delete();
        }
    }
    
    public static boolean isWindows() {
        return OS.startsWith("Windows");
    }
}
