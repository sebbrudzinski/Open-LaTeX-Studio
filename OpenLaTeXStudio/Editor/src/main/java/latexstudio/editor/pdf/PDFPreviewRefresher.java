/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.pdf;

import java.awt.Point;
import javax.swing.JButton;
import latexstudio.editor.runtime.CommandLineExecutor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.files.FileService;
import latexstudio.editor.runtime.CommandLineBuilder;
import latexstudio.editor.settings.ApplicationSettings;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.util.Exceptions;

/**
 * A thread that is responsible for refreshing the PDF preview when necessary.
 * @author Sebastian
 */
public class PDFPreviewRefresher implements Runnable {
    
    private final JScrollPane jScrollPane;
    private final JLabel jLabel;
    private final EditorTopComponent etc;
    private final PDFDisplay pdfDisplay;
    private final JButton previousButton;
    private final JButton nextButton;
    
    public PDFPreviewRefresher(JScrollPane jScrollPane, JLabel jLabel,EditorTopComponent etc, PDFDisplay pdfDisplay, JButton previousButton, JButton nextButton) {
        this.jScrollPane = jScrollPane;
        this.jLabel = jLabel;
        this.etc = etc;
        this.pdfDisplay = pdfDisplay;
        
        previousButton.setEnabled(false);
        if(pdfDisplay.getTotalPages()>1){
            nextButton.setEnabled(true);
        } else{
            nextButton.setEnabled(false);
        }
        this.previousButton = previousButton;
        this.nextButton = nextButton;
    }

    @Override
    public void run() {
        while(true) {
            if (etc.isDirty()) {
                compileTemporaryFile();
                pdfDisplay.updateTotalPages();
                drawPreview();
                etc.setDirty(false);
            }
            
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
    private void drawPreview() {
        JPanel pdfImagePanel = pdfDisplay.drawPreviewOnJPanel(nextButton);
       
        if (pdfImagePanel != null) {
            JViewport vp = jScrollPane.getViewport();
            Point p = vp.getViewPosition();
            vp.setView(pdfImagePanel);
            vp.setViewPosition(p);

        }
        
        jLabel.setText("of "+pdfDisplay.getTotalPages());
    }

    private void compileTemporaryFile() {        
        String fileLocation = ApplicationUtils.getTempSourceFile();
        String content = etc.getEditorContent();
        
        FileService.writeToFile(fileLocation, content);

        CommandLineExecutor.executeGeneratePDF(new CommandLineBuilder()
                .withPathToSource(ApplicationUtils.getTempSourceFile())
                .withOutputDirectory(ApplicationUtils.getAppDirectory())
                .withJobname("preview")
                .withWorkingFile(etc.getCurrentFile())
                .withLatexPath( (String) ApplicationSettings.Setting.LATEX_PATH.getValue() )
        );
    }

}
