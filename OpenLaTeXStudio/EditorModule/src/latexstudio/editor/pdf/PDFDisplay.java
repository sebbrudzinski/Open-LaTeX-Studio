
package latexstudio.editor.pdf;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Sebastian
 */
public class PDFDisplay {

    private int selectedPage;
    
    public PDFDisplay() {
        this.selectedPage = 1;
    }
    
    public JPanel drawPreviewOnJPanel() {
        JPanel pdfImagePanel = new JPanel();
        
        Image generatedImage = PDFPreviewBuilder.buildPDFPreview(PDFService.getPDFPage(selectedPage));
        
        if (generatedImage != null) {
            ImageIcon icon = new ImageIcon(generatedImage);
            JLabel picLabel = new JLabel(icon);
            pdfImagePanel.add(picLabel);
        }
        
        PDFService.closeDocument();
        return pdfImagePanel;
    }
    
    public void previousPage() {
        selectedPage--;
    }
    
    public void nextPage() {
        selectedPage++;
    }
    
    public void setPage(int page) {
        selectedPage = page;
    }
    
    public int getSelectedPage() {
        return selectedPage;
    }
}
