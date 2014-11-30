package latexstudio.editor.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import latexstudio.editor.util.ApplicationUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.openide.util.Exceptions;

/**
 * This class allows all kind of operations on the PDF files.
 * @author Sebastian
 */
public class PDFService {
    
    private static PDDocument inputPDF;
    private static final String PDF_PATH = ApplicationUtils.getTempPDFFile();

    public static List<PDPage> getPDFPages() {
        List<PDPage> allPages = new ArrayList<PDPage>();
        
        try {
            File pdfFile = new File(PDF_PATH);
            if (pdfFile.exists()) {
                inputPDF = PDDocument.load(pdfFile);
                allPages = inputPDF.getDocumentCatalog().getAllPages();
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        return allPages;
    }
    
    public static void closeDocument() {
        if (inputPDF != null) {
            try {
                inputPDF.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
