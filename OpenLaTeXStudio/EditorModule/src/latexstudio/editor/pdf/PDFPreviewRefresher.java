package latexstudio.editor.pdf;

import latexstudio.editor.files.FileService;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import latexstudio.editor.util.ApplicationUtils;
import latexstudio.editor.EditorTopComponent;
import org.apache.pdfbox.pdmodel.PDPage;
import org.openide.util.Exceptions;

/**
 * A thread that is responsible for refreshing the PDF preview when necessary.
 * @author Sebastian
 */
public class PDFPreviewRefresher implements Runnable {
    
    private final JScrollPane jScrollPane;
    private final EditorTopComponent etc;
    
    public PDFPreviewRefresher(JScrollPane jScrollPane, EditorTopComponent etc) {
        this.jScrollPane = jScrollPane;
        this.etc = etc;
    }

    @Override
    public void run() {
        while(true) {
            if (etc.isDirty()) {
                compileTemporaryFile();
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
        JPanel pdfImagePanel = new JPanel();
        
        List<PDPage> allPages = PDFService.getPDFPages();
        Image generatedImage = PDFPreviewBuilder.buildPDFPreview(allPages);
        
        if (generatedImage != null) {
            ImageIcon icon = new ImageIcon(generatedImage);
            JLabel picLabel = new JLabel(icon);
            pdfImagePanel.add(picLabel);

            jScrollPane.setViewportView(pdfImagePanel);
        }
        
        PDFService.closeDocument();
    }

    private void compileTemporaryFile() {        
        String fileLocation = ApplicationUtils.getTempSourceFile();
        String content = etc.getEditorContent();
        
        FileService.writeToFile(fileLocation, content);
        CommandLineExecutor.executeGeneratePDF(ApplicationUtils.getTempSourceFile(), ApplicationUtils.getAppTempDirectory());
    }

}
