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
    
    private PDFPreviewBuilder() {
    }

    public static Image buildPDFPreview(int page, int zoom) {
        float newScale = ((float) zoom) / 100.0f;
        BufferedImage pageImage = null;
        
        try (PDDocument pdDocument = PDFService.getPDDocument()) {
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
            pageImage = pdfRenderer.renderImage(page - 1, newScale, ImageType.RGB);

            return pageImage;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return null;
    }
}
