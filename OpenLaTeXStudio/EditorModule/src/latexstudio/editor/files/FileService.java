/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.apache.pdfbox.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * A class that allows all kind of operations on files. 
 * @author Sebastian
 */
public class FileService {
    public static void writeToFile(String filename, String content) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.print(content);
        } catch (FileNotFoundException ex) {
            //Should never happen
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedEncodingException ex) {
            //Should never happen
            Exceptions.printStackTrace(ex);
        } finally {
            writer.close();
        }
    }
    
    public static String readFromFile(String filename) {  
        BufferedReader br = null;
        String content = "";
        
        try {
            br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            content = sb.toString();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            IOUtils.closeQuietly(br);
        }
        
        return content;
    }
}
