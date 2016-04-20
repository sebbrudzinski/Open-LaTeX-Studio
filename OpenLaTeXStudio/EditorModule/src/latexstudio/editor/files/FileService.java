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
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import org.apache.pdfbox.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * A class that allows all kind of operations on files. 
 * @author Sebastian
 */
public final class FileService {

    private FileService() {
    }
    
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
    
    public static String readFromStream(InputStream stream){
        Scanner sc = null;
        
        try{
            sc = new Scanner(stream);
            StringBuilder sb = new StringBuilder();
            
            while(sc.hasNextLine())
            {
                sb.append(sc.nextLine());
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }
        finally{
            IOUtils.closeQuietly(stream);
            sc.close();
        }
    }
}
