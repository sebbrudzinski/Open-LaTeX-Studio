
package latexstudio.editor.pdf;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Sebastian
 */
public final class CommandLineExecutor {
    
    private static final Logger LOGGER = Logger.getLogger(CommandLineExecutor.class.getName());
    
    public static void executeGeneratePDF(String pathToSource, String outputDir) {
        executeGeneratePDF(pathToSource, outputDir, null);
    }
    
    public static void executeGeneratePDF(String pathToSource, String outputDir, String jobname) {
        String opts = " --output-directory=" + outputDir + " --output-format=pdf";
        
        if (jobname != null) {
            opts = opts.concat(" --jobname=".concat(jobname));
        }
        
        try {           
            String command = ApplicationUtils.PATH_TO_TEX + opts + " " + pathToSource ;
         
            Process p = Runtime.getRuntime().exec(command);
            LOGGER.log(Level.INFO, "Executing: {0}", command);          
            
            p.waitFor(3, TimeUnit.SECONDS);
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } 
    }
}
