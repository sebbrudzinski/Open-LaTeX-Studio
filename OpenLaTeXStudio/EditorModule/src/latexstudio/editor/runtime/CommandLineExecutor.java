/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.runtime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;

/**
 * The class responsible for execution of commands in runtime and handling the outputs
 * of external processes.
 * 
 * @author Sebastian
 */
public final class CommandLineExecutor {
    
    private static final DefaultExecutor EXECUTOR;
    
    static {
        EXECUTOR = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(8000);

        // generating pdf returns code 1, so we add it to the accepted values
        EXECUTOR.setExitValues(new int[] {0,1});
        EXECUTOR.setWatchdog(watchdog);
    }
      
    public synchronized static void executeGeneratePDF(CommandLineBuilder cmd) {
        String outputDirectory = "--output-directory=" + cmd.getOutputDirectory();
        String outputFormat = "--output-format=pdf";
        String job = cmd.getJobname() == null ? "" : "--jobname=" + cmd.getJobname().replaceAll(" ", "_");
        ByteArrayOutputStream outputStream = null;
        
        try {           
            String[] command =  new String[] {
                outputDirectory, outputFormat, job, cmd.getPathToSource()
            };
         
            CommandLine cmdLine = new CommandLine(ApplicationUtils.getPathToTEX(cmd.getLatexPath()));
            //For windows, we set handling quoting to true
            cmdLine.addArguments(command, ApplicationUtils.isWindows());
            
            outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            EXECUTOR.setStreamHandler(streamHandler);
            
            if (cmd.getWorkingFile() != null) {
                EXECUTOR.setWorkingDirectory(cmd.getWorkingFile().getParentFile().getAbsoluteFile());
            }
            
            EXECUTOR.execute(cmdLine);      

            if (cmd.getLogger() != null) {
                cmd.getLogger().log(cmdLine.toString());
                cmd.getLogger().log(outputStream.toString());
            }
                        
        } catch (IOException e) {
            if (cmd.getLogger() != null) {
                cmd.getLogger().log("The path to the pdflatex tool is incorrect or has not been set.");
            }
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
