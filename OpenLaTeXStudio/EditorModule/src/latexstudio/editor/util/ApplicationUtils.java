/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.util;

import java.io.File;

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
    private static final String PREVIEW_SOURCE_FILENAME = "preview.tex";
    private static final String PREVIEW_PDF_FILENAME = "preview.pdf";
    
    public static final String PATH_TO_TEX = "pdflatex";
    
    public static final String getTempSourceFile() {
        return getAppTempDirectory().concat(File.separator).concat(PREVIEW_SOURCE_FILENAME);
    }
    
    public static final String getTempPDFFile() {
        return getAppTempDirectory().concat(File.separator).concat(PREVIEW_PDF_FILENAME);
    }
    
    public static String getAppTempDirectory() {
        File tempDir = new File(HOME + File.separator + APP_DIR_NAME);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return tempDir.getAbsolutePath();
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
