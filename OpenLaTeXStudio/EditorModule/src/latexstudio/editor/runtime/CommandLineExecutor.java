package latexstudio.editor.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import latexstudio.editor.OutputTopComponent;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * The class responsible for execution of commands in runtime and handling the outputs
 * of external processes.
 * 
 * @author Sebastian
 */
public final class CommandLineExecutor {
    
    private static final Logger LOGGER = Logger.getLogger(CommandLineExecutor.class.getName());
    
    public static void executeGeneratePDF(String pathToSource, String outputDir) {
        executeGeneratePDF(pathToSource, outputDir, null);
    }
    
    public static void executeGeneratePDF(String pathToSource, String outputDir, String jobname) {
        executeGeneratePDF(pathToSource, outputDir, jobname, null);
    }
    
    public static void executeGeneratePDF(String pathToSource, String outputDir, String jobname, OutputTopComponent outputComponent) {
        String outputDirectory = "--output-directory=" + outputDir;
        String outputFormat = "--output-format=pdf";
        
        String job = jobname == null ? "" : "--jobname=" + jobname;
        
        InputStream es = null;
        InputStream is = null;
        
        try {           
            String[] command =  new String[] {ApplicationUtils.PATH_TO_TEX, 
                outputDirectory, outputFormat, job, pathToSource};
         
            Process p = Runtime.getRuntime().exec(command);
            LOGGER.log(Level.INFO, "Executing: {0} {1} {2} {3} {4}", command);        

            if (outputComponent != null) {
                /* Output is handled in separate thread, due to the possibility 
                   to one stream blocking another */
                new StreamHandler("pdflatex", p.getInputStream(), outputComponent).start();
                new StreamHandler("ERROR", p.getErrorStream(), outputComponent).start();
            }
                        
            p.waitFor(3, TimeUnit.SECONDS);
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(es);
        }
    }
}
