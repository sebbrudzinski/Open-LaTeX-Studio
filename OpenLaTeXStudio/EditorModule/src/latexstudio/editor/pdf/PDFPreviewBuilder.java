/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.pdf;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPage;
import org.openide.util.Exceptions;

/**
 * A class responsible for building PDF preview.
 * @author Sebastian
 */
public class PDFPreviewBuilder {
    
    private static final double SCALE_FACTOR = 0.4;
    private static final int SCALE_TYPE = Image.SCALE_SMOOTH;

    public static Image buildPDFPreview(PDPage pdfPage, int zoom) {
        if (pdfPage == null) {
            return null;
        }
         

        double newScale = SCALE_FACTOR * (((double)zoom)/100.0);
        BufferedImage pageImage = null;
        try {
            pageImage = pdfPage.convertToImage();
            int width = (int) (newScale * pageImage.getWidth());
            int height = (int) (newScale * pageImage.getHeight());
            int type = pageImage.getType();
            
            return pageImage.getScaledInstance(width, height, SCALE_TYPE);
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
}
