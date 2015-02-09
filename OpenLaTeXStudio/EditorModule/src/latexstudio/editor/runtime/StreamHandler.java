package latexstudio.editor.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import latexstudio.editor.OutputTopComponent;
import org.apache.pdfbox.io.IOUtils;

/**
 *
 * @author Sebastian
 */
public class StreamHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(StreamHandler.class.getName());
    
    private final String name;
    private final InputStream is;
    private final OutputTopComponent outputComponent;
    private Thread thread;      
    
    public StreamHandler(String name, InputStream is, OutputTopComponent outputTopComponent) {
        this.name = name;
        this.is = is;
        this.outputComponent = outputTopComponent;
    }       
    public void start () {
        thread = new Thread(this);
        thread.start();
    }       
    
    @Override
    public void run () {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);   
            while (true) {
                String s = br.readLine ();
                if (s == null) break;
                outputComponent.logToOutput("[" + name + "] " + s);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Problem reading stream {0}... :{1}", new Object[]{name, ex});
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
