/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.pdf;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.openide.util.Exceptions;

/**
 * A class responsible for building PDF preview.
 * @author Sebastian
 */
public final class PDFPreviewBuilder {
    private static final int SCALE_TYPE = Image.SCALE_SMOOTH;
    
    private PDFPreviewBuilder() {
    }

    public static Image buildPDFPreview(int page, int zoom) {
        float newScale = ((float) zoom) / 100.0f;
        BufferedImage pageImage = null;
        
        try (PDDocument pdDocument = PDFService.getPDDocument()) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            if (pdfRenderer != null && pdDocument.getNumberOfPages()>= page && page > 0){
                pageImage = pdfRenderer.renderImage(page - 1);
                int width = (int) (newScale * pageImage.getWidth());
                int height = (int) (newScale * pageImage.getHeight());
            
                return pageImage.getScaledInstance(width, height, SCALE_TYPE);
            }
            
                        
            return pageImage;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
}
