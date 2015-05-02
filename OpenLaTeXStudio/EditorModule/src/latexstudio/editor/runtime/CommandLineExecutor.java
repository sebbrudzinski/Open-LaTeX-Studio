/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.runtime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.openide.util.Exceptions;

/**
 * The class responsible for execution of commands in runtime and handling the outputs
 * of external processes.
 * 
 * @author Sebastian
 */
public final class CommandLineExecutor {
    
    public static void executeGeneratePDF(String pathToSource, String outputDir, File workingFile) {
        executeGeneratePDF(pathToSource, outputDir, null, workingFile);
    }
    
    public static void executeGeneratePDF(String pathToSource, String outputDir, String jobname, File workingFile) {
        executeGeneratePDF(pathToSource, outputDir, jobname, workingFile, null);
    }
    
    public static void executeGeneratePDF(String pathToSource, String outputDir, String jobname, File workingFile, ApplicationLogger logger) {
        String outputDirectory = "--output-directory=" + outputDir;
        String outputFormat = "--output-format=pdf";
        
        String job = jobname == null ? "" : "--jobname=" + jobname.replaceAll(" ", "_");
        String includeDir = workingFile == null ? "" : "--include-directory=" + workingFile.getParentFile().getAbsolutePath();
        
        ByteArrayOutputStream outputStream = null;
        
        try {           
            String[] command =  new String[] {
                outputDirectory, outputFormat, job, includeDir, pathToSource};
         
            CommandLine cmdLine = new CommandLine(ApplicationUtils.PATH_TO_TEX);
            //For windows, we set handling quoting to true
            cmdLine.addArguments(command, ApplicationUtils.isWindows());
            
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(8000);
            
            outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            
            // generating pdf returns code 1, so we add it to the accepted values
            executor.setExitValues(new int[] {0,1});
            executor.setWatchdog(watchdog);
            executor.execute(cmdLine);      

            if (logger != null) {
                logger.log(outputStream.toString());
            }
                        
        } catch (IOException e) {
            Exceptions.printStackTrace(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
