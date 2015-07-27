/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileChooserService.DialogType;
import latexstudio.editor.runtime.CommandLineBuilder;
import latexstudio.editor.runtime.CommandLineExecutor;
import latexstudio.editor.util.ApplicationUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

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
    
    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private final ApplicationLogger LOGGER = new ApplicationLogger("Open LaTeX Studio");

    @Override
    public void actionPerformed(ActionEvent e) {        
        File file = FileChooserService.getSelectedFile("pdf", "PDF files", DialogType.PDF, false);
        int reply = JOptionPane.NO_OPTION;
        if (file != null) {
            //When the user clicks a file, the Chooser shows "file.pdf", so we have to remove this ".pdf"
            //Otherwise, pdflatex will create a "file.pdf.pdf"
            if (file.toString().endsWith(".pdf")) {
               file = new File(file.toString().substring(0, file.toString().length() - 4));
            }
            
            //If the user only type "file", we have to check if "file.pdf" exists.
            File v_overwriting = new File(file.toString() + ".pdf");
            while (v_overwriting.exists() && reply == JOptionPane.NO_OPTION) {
                reply = JOptionPane.showConfirmDialog(null,
                            v_overwriting.toString() + " already exists. Do you want to overwrite it?",
                            "File already exists.",
                            JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.NO_OPTION) { //The user does not want to overwrite. Then select another name
                    file = FileChooserService.getSelectedFile("pdf", "PDF files", DialogType.PDF, false);
                    if (file != null) {
                        if (file.toString().endsWith(".pdf")) {
                            file = new File(file.toString().substring(0, file.toString().length() - 4));
                         }
                        v_overwriting = new File(file.toString() + ".pdf");
                    }
                    else return;
                }
            }
        }
        if (file == null) return;
        String filename = file.getName();

        LOGGER.log("Invoking pdflatex");

        CommandLineExecutor.executeGeneratePDF(new CommandLineBuilder()
            .withPathToSource(ApplicationUtils.getTempSourceFile())
            .withOutputDirectory(file.getParentFile().getAbsolutePath())
            .withJobname(filename)
            .withWorkingFile(etc.getCurrentFile())
            .withLatexPath(etc.getLatexPath())
            .withLogger(new ApplicationLogger("pdflatex")));  
    }
}
