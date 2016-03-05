/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Sebastian
 */
public final class PropertyService {

    private PropertyService() {
    }
    
    private static final String PROPERTIES_FILENAME = "latexProperties.txt";
    
    private static final String PROPERTIES_FILEPATH = System.getProperty("user.home") + "/latexstudio/editor/resources/latexProperties.txt";
    
    private static final String PROPERTIES_DIRECTORY = "latexstudio/editor/resources";
    
    public static Properties readProperties(String propertyFileName) {
        Properties properties = new Properties(); //start from here
        try
        {
            FileInputStream fileInputStream = new FileInputStream(propertyFileName);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (Exception ex) { //may throw either IOException or FileNotFoundException; user shouldn't see either
            //Exceptions.printStackTrace(ex);
            return null;
        }
        return properties;
    }
    
    public static void writeProperties(Properties toWrite) {
        FileOutputStream fileOutputStream = null;
        try {
            String homeDirectory = System.getProperty("user.home");
            File file = new File(homeDirectory + "/" + PROPERTIES_DIRECTORY);
            file.mkdirs();
            file = new File(PROPERTIES_FILEPATH);
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            toWrite.store(fileOutputStream, null);
            
        }
        catch(IOException exception)
        { 
           Exceptions.printStackTrace(exception);
        }
        finally
        {
            IOUtils.closeQuietly(fileOutputStream);
        }
        
    }
    
    public static boolean hasProperties()
    {
        return new File(PROPERTIES_FILEPATH).exists();
    }
    
    public static String getPropertyFilePath() {
        return PROPERTIES_FILEPATH;
    }
}
