
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

    public static Image buildPDFPreview(PDPage pdfPage) {
        if (pdfPage == null) {
            return null;
        }
         
        BufferedImage pageImage = null;
        try {
            pageImage = pdfPage.convertToImage();
            int width = pageImage.getWidth() / SCALE_FACTOR;
            int height = pageImage.getHeight() / SCALE_FACTOR;
            int type = pageImage.getType();
            
            return pageImage.getScaledInstance(width, height, SCALE_TYPE);
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
}
