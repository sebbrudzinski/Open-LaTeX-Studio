/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openide.util.Exceptions;

/**
 *
 * @author Sebastian
 */
public final class PropertyService {

    private PropertyService() {
    }
    
    public static Properties readProperties(String propertyFileName) {
        Properties properties = new Properties();
        InputStream is = PropertyService.class.getClassLoader().getResourceAsStream("latexstudio/editor/resources/" + propertyFileName);
        try {
            properties.load(is);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return properties;
    }
}
