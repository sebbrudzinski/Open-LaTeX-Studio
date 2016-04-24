/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.files;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * A class that allows all kind of operations on files. 
 * @author Sebastian
 */
public final class FileService {

    private FileService() {
    }
    
    public static void writeToFile(String filename, String content) {
        try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
            writer.print(content);
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            //Should never happen
            Exceptions.printStackTrace(ex);
        }
    }
    
    public static String readFromFile(String filename) {  
        try (FileReader reader = new FileReader(filename)) {
            return IOUtils.toString(reader);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
    
    public static String readFromStream(InputStream stream) {
        try {
            return IOUtils.toString(stream);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            IOUtils.closeQuietly(stream);
        }
        
        return null;
    }
}
