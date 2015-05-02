/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
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
        File file = FileChooserService.getSelectedFile("pdf", "PDF files", false);
        if (file != null) {
            String filename = file.getName();
            
            LOGGER.log("Invoking pdflatex");
            CommandLineExecutor.executeGeneratePDF(ApplicationUtils.getTempSourceFile(),
                    file.getParentFile().getAbsolutePath(), 
                    filename, 
                    etc.getCurrentFile(), 
                    new ApplicationLogger("pdflatex"));
        }
    }
}
