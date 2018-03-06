/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.runtime;

import java.awt.Point;
import java.io.IOException;
import java.util.List;
import latexstudio.editor.runtime.CommandLineExecutor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.files.FileService;
import latexstudio.editor.pdf.PDFDisplay;
import latexstudio.editor.runtime.CommandLineBuilder;
import latexstudio.editor.settings.ApplicationSettings;
import latexstudio.editor.util.ApplicationUtils;
import org.languagetool.rules.RuleMatch;
import org.openide.util.Exceptions;

/**
 * A thread that is responsible for refreshing the PDF preview when necessary.
 * @author Sebastian
 */
public class PeriodicTasksService implements Runnable {
    
    private final JScrollPane jScrollPane;
    private final JLabel jLabel;
    private final EditorTopComponent etc;
    private final PDFDisplay pdfDisplay;
    
    public PeriodicTasksService(JScrollPane jScrollPane, JLabel jLabel, EditorTopComponent etc, PDFDisplay pdfDisplay) {
        this.jScrollPane = jScrollPane;
        this.jLabel = jLabel;
        this.etc = etc;
        this.pdfDisplay = pdfDisplay;
    }

    @Override
    public void run() {
        while(true) {
            if (etc.getEditorState().isDirty()) {
                try {
                    spellCheckAllText();
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
                compileTemporaryFile();
                pdfDisplay.updateTotalPages();
                drawPreview();
                etc.getEditorState().setDirty(false);
            }
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    private void drawPreview() {
        JPanel pdfImagePanel = pdfDisplay.drawPreviewOnJPanel();
       
        if (pdfImagePanel != null) {
            JViewport vp = jScrollPane.getViewport();
            Point p = vp.getViewPosition();
            vp.setView(pdfImagePanel);
            vp.setViewPosition(p);

        }
        
        jLabel.setText("of " + pdfDisplay.getTotalPages());
    }

    private void compileTemporaryFile() {        
        String fileLocation = ApplicationUtils.getTempSourceFile();
        String content = etc.getEditorContent();
        
        FileService.writeToFile(fileLocation, content);

        CommandLineExecutor.executeGeneratePDF(new CommandLineBuilder()
                .withPathToSource(ApplicationUtils.getTempSourceFile())
                .withOutputDirectory(ApplicationUtils.getAppDirectory())
                .withJobname("preview")
                .withWorkingFile(etc.getEditorState().getCurrentFile())
                .withLatexPath( (String) ApplicationSettings.Setting.LATEX_PATH.getValue() )
        );
    }
    
    private void spellCheckAllText() throws BadLocationException {   
        Document doc = etc.getrSyntaxTextArea().getDocument();   
        if (doc != null) {                                                
            String editorText = etc.getrSyntaxTextArea().getText(0, doc.getLength());            
            if (editorText != null) {  
                Highlighter highlighter = etc.getrSyntaxTextArea().getHighlighter();                                      
                if(etc.getEditorState().isSpellCheckActive()) {  
                    List<RuleMatch> matches = null;  
                    try {
                        matches = etc.getLangTool().check(editorText);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }

                    highlighter.removeAllHighlights();
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
