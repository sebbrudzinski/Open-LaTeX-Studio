package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.OutputTopComponent;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.runtime.CommandLineExecutor;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "latexstudio.editor.GeneratePDF"
)
@ActionRegistration(
        iconBase = "latexstudio/editor/resources/icons/document-pdf-text.png",
        displayName = "#CTL_GeneratePDF"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1700),
    @ActionReference(path = "Toolbars/File", position = 3333)
})
@Messages("CTL_GeneratePDF=Generate PDF")
public final class GeneratePDF implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;
        
        tc = WindowManager.getDefault().findTopComponent("OutputTopComponent");
        OutputTopComponent otc = (OutputTopComponent) tc;
        
        String content = etc.getEditorContent();
        File file = FileChooserService.getSelectedFile("pdf", "PDF files", false);
        if (file != null) {
            String filename = file.getName();
            
            otc.logToOutput("[Open LaTeX Studio] Invoking pdflatex");
            CommandLineExecutor.executeGeneratePDF(ApplicationUtils.getTempSourceFile(),
                    file.getParentFile().getAbsolutePath(), 
                    filename, 
                    etc.getCurrentFile(), 
                    otc);
        }
    }
}
