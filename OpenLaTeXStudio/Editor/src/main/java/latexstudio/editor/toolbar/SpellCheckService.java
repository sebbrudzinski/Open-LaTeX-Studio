/* 
 * Copyright (c) 2017 White Hsu
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import javax.swing.text.BadLocationException;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import org.openide.util.Exceptions;

/**
 *
 * @author White Hsu
 */
public class SpellCheckService implements Runnable {

    private final EditorTopComponent etc;
    private static final ApplicationLogger LOGGER = new ApplicationLogger("Spell Check Support");

    public SpellCheckService(EditorTopComponent etc) {
        this.etc = etc;
    }        
    
    @Override
    public void run() {
        while(true) {
            if (etc.getEditorState().isDirty()) {
                try {
                    etc.getEditorState().setDirty(false);
                    etc.spellCheckAllText();
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
}
