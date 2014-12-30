package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import latexstudio.editor.util.ApplicationUtils;
import latexstudio.editor.pdf.CommandLineExecutor;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.files.FileChooserService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "latexstudio.editor.GeneratePDF"
)
@ActionRegistration(
        displayName = "#CTL_GeneratePDF"
)
@ActionReference(path = "Menu/File", position = 1700)
@Messages("CTL_GeneratePDF=Generate PDF")
public final class GeneratePDF implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;
        
        String content = etc.getEditorContent();
        File file = FileChooserService.getSelectedFile("pdf", "PDF files");
        if (file != null) {
            String filename = file.getName();
            String jobname = filename.endsWith(".pdf") ? 
                    filename.substring(0, filename.length() - 5) : filename;
            CommandLineExecutor.executeGeneratePDF(ApplicationUtils.getTempSourceFile(),
                    file.getParentFile().getAbsolutePath(), jobname);
        }
    }
}
