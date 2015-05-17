package latexstudio.editor.runtime;

import java.io.File;
import latexstudio.editor.ApplicationLogger;

/**
 *
 * @author Sebastian
 */
public class CommandLineBuilder {
    
    private String pathToSource;
    private String outputDirectory;
    private String jobname;
    private File workingFile;
    private ApplicationLogger logger;
    private String latexPath;

    public CommandLineBuilder withPathToSource(String pathToSource) {
        this.pathToSource = pathToSource;
        return this;
    }

    public CommandLineBuilder withOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    public CommandLineBuilder withJobname(String jobname) {
        this.jobname = jobname;
        return this;
    }

    public CommandLineBuilder withWorkingFile(File workingFile) {
        this.workingFile = workingFile;
        return this;
    }

    public CommandLineBuilder withLogger(ApplicationLogger logger) {
        this.logger = logger;
        return this;
    }

    public CommandLineBuilder withLatexPath(String latexPath) {
        this.latexPath = latexPath;
        return this;
    }

    public String getPathToSource() {
        return pathToSource;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getJobname() {
        return jobname;
    }

    public File getWorkingFile() {
        return workingFile;
    }

    public ApplicationLogger getLogger() {
        return logger;
    }

    public String getLatexPath() {
        return latexPath;
    }
    
}
