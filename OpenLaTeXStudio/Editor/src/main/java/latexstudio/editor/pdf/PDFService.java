/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.pdf;

import java.io.File;
import java.io.IOException;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * This class allows all kind of operations on the PDF files.
 * @author Sebastian
 */
public final class PDFService {
    
    private static PDDocument inputPDF;
    private static final String PDF_PATH = ApplicationUtils.getTempPDFFile();

    private PDFService() {
    }
    
    public static int getTotalPDFPages(){
        int pages = 0;
        
        File pdfFile = null;
        try {
            pdfFile = new File(PDF_PATH);
            if (pdfFile.exists()) {
                inputPDF = PDDocument.load(pdfFile);
                pages = inputPDF.getNumberOfPages();
            }
        } catch (IOException ex) {
            // fallback to finally
        } finally {
            closeDocument();
        }
        
        return pages;
    }
    
    public static PDDocument getPDDocument() {
        PDPage page = null;
        
        File pdfFile = null;
        try {
            pdfFile = new File(PDF_PATH);
            if (pdfFile.exists()) {
                inputPDF = PDDocument.load(pdfFile);
            }
        } catch (IOException ex) {
            closeDocument();
        }
        
        return inputPDF;
    }
    
    public static void closeDocument() {
        if (inputPDF != null) {
            IOUtils.closeQuietly(inputPDF);
        }
    }
}
