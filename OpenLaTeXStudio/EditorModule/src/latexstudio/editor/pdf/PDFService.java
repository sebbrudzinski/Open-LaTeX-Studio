package latexstudio.editor.pdf;

import java.io.File;
import java.io.IOException;
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

    public static PDPage getPDFPage(int number) {
        PDPage page = null;
        
        File pdfFile = null;
        try {
            pdfFile = new File(PDF_PATH);
            if (pdfFile.exists()) {
                inputPDF = PDDocument.load(pdfFile);
                List<PDPage> allPages = inputPDF.getDocumentCatalog().getAllPages();
                if (allPages != null && !allPages.isEmpty() && allPages.size() >= number && number > 0) {
                    page = allPages.get(number - 1);
                }
            }
        } catch (IOException ex) {
            closeDocument();
        }
        
        return page;
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
