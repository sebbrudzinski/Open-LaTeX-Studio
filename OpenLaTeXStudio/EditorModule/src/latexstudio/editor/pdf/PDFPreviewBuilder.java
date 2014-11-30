
package latexstudio.editor.pdf;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDPage;
import org.openide.util.Exceptions;

/**
 * A class responsible for building PDF preview.
 * @author Sebastian
 */
public class PDFPreviewBuilder {
    
    private static final int SCALE_FACTOR = 3;
    private static final int SCALE_TYPE = Image.SCALE_SMOOTH;

    public static Image buildPDFPreview(List<PDPage> pdfPages) {
        if (pdfPages == null || pdfPages.isEmpty()) {
            return null;
        }
        
        PDPage firstPage = pdfPages.get(0);
        
        BufferedImage firstPageImage = null;
        try {
            firstPageImage = pdfPages.get(0).convertToImage();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        int width = firstPageImage.getWidth() / SCALE_FACTOR;
        int height = firstPageImage.getHeight() / SCALE_FACTOR;
        int type = firstPageImage.getType();

        BufferedImage finalImg = new BufferedImage(width, height * pdfPages.size(), type); 
        Graphics2D graphics = finalImg.createGraphics();
  
        for (int i = 0; i < pdfPages.size(); i++) {  
            try {
                BufferedImage image = pdfPages.get(i).convertToImage();
                
                graphics.drawImage(image.getScaledInstance(width, height, SCALE_TYPE), 0, height * i, null);  
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        return finalImg;
    }
}
