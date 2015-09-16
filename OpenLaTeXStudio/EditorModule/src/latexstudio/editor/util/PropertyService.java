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
import java.util.Properties;
import org.openide.util.Exceptions;

/**
 *
 * @author Sebastian
 */
public final class PropertyService {
    
    public static final String PROPERTIES_FILENAME = "latexProperties.txt";
    
    public static final String PROPERTIES_FILEPATH = System.getProperty("user.home") + "/latexstudio/editor/resources/latexProperties.txt";
    
    public static final String PROPERTIES_DIRECTORY = "latexstudio/editor/resources";
    
    public static Properties readProperties(String propertyFileName) {
        Properties properties = new Properties(); //start from here
        //args formerly: "latexstudio/editor/resources/" + propertyFileName
        try {
            //formerly InputStream is = PropertyService.class.getClassLoader().getResourceAsStream(PROPERTIES_FILEPATH);
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILEPATH);
            properties.load(fileInputStream);
        } catch (Exception ex) { //may throw either IOException or FileNotFoundException; user shouldn't see either
            //Exceptions.printStackTrace(ex);
            return null;
        }
        return properties;
    }
    
    public static void writeProperties(Properties toWrite) {
        try {
             FileOutputStream fileOutputStream;
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
            fileOutputStream.close();
        }
        catch(IOException exception)
        { 
           Exceptions.printStackTrace(exception);
        }
    }
    
}
