/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.pdf;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Sebastian
 */
public class PDFDisplay {

    private int selectedPage;
    private int totalPages;
    private int viewZoom;
    
    public PDFDisplay() {
        this.selectedPage = 1;
        this.totalPages = 1;
        this.viewZoom = 50;
    }
    
    public void updateTotalPages(){
        this.totalPages = PDFService.getTotalPDFPages();
    }
    
    public JPanel drawPreviewOnJPanel(JButton nextButton) {
        JPanel pdfImagePanel = new JPanel();
        
        Image generatedImage = PDFPreviewBuilder.buildPDFPreview(PDFService.getPDFPage(selectedPage), viewZoom);
        
        if (generatedImage != null) {
            ImageIcon icon = new ImageIcon(generatedImage);
            JLabel picLabel = new JLabel(icon);
            pdfImagePanel.add(picLabel);
        }
        
        updateTotalPages();
        if(getSelectedPage() != getTotalPages())
            nextButton.setEnabled(true);
        
        PDFService.closeDocument();
        return pdfImagePanel;
    }
    
    /**
     * Shows the previous page, in case it's not the first page already.
     * @return true, if page was modified; false otherwise
     */
    public boolean previousPage() {
        if (selectedPage - 1 > 0) {
            selectedPage--;
            return true;
        }
        return false;
    }
    
    /**
     * Shows the next page, in case it's not the last page already.
     * @return true, if page was modified; false otherwise
     */
    public boolean nextPage() {
        if (selectedPage + 1 <= totalPages) {
            selectedPage++;
            return true;
        }
        return false;
    }
    
    public void setPage(int page) {
        if (page > totalPages) {
            selectedPage = totalPages;
        } else if (page < 1) {
            selectedPage = 1;
        } else {
            selectedPage = page;
        }
    }
    
    public int getSelectedPage() {
        return selectedPage;
    }

    public void setZoom(int zoom) {
        viewZoom = zoom;
    }
    public int getZoom () {
        return viewZoom;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
