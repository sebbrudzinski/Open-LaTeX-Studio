/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import java.util.List;

/**
 * Class responsible for logging any information to the user's
 * Logs output window.
 * 
 * @author Sebastian
 */
public class ApplicationLogger {
    private final String loggerName;
    private final OutputTopComponent otc = new TopComponentFactory<OutputTopComponent>()
            .getTopComponent(OutputTopComponent.class.getSimpleName());

    public ApplicationLogger(String loggerName) {
        this.loggerName = loggerName;
    }
    
    public void log(String message) {
        otc.logToOutput("[" + loggerName + "] " + message);
    }
    
    public void log(String[] messages) {
        for (String message : messages) {
            log(message);
        }
    }
    
    public void log(List<String> messages) {
        log((String[]) messages.toArray());
    }
    
}
