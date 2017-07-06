/* 
 * Copyright (c) 2017 White Hsu
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import java.io.IOException;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import latexstudio.editor.EditorTopComponent;
import org.languagetool.rules.RuleMatch;
import org.openide.util.Exceptions;

/**
 *
 * @author White Hsu
 */
public class SpellCheckService implements Runnable {

    private final EditorTopComponent etc;

    public SpellCheckService(EditorTopComponent etc) {
        this.etc = etc;
    }        
    
    @Override
    public void run() {
        while(true) {
            if (etc.getEditorState().isDirty()) {
                try {
                    etc.getEditorState().setDirty(false);
                    spellCheckAllText();
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
    
    public void spellCheckAllText() throws BadLocationException {   
        Document doc = etc.getrSyntaxTextArea().getDocument();   
        if (doc != null) {                                                
            String editorText = etc.getrSyntaxTextArea().getText(0, doc.getLength());            
            if (editorText != null) {  
                Highlighter highlighter = etc.getrSyntaxTextArea().getHighlighter();                                      
                if(etc.getEditorState().isHighlighted()) {  
                    List<RuleMatch> matches = null;  
                    try {
                        matches = etc.getLangTool().check(editorText);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    //Highlight the spelling check results
                    for (RuleMatch match : matches) {
                        highlighter.addHighlight(match.getFromPos(), match.getToPos(), etc.getPainter());   
                    }                        
                } else {  
                    highlighter.removeAllHighlights();
                }
            }
        }        
    }
    
}
