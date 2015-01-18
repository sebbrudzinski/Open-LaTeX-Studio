package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.OutputTopComponent;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "latexstudio.editor.SaveFile"
)
@ActionRegistration(
        iconBase = "latexstudio/editor/resources/icons/save.png",
        displayName = "#CTL_SaveFile"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300),
    @ActionReference(path = "Toolbars/File", position = 2222)
})
@Messages("CTL_SaveFile=Save File")
public final class SaveFile implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;
        
        tc = WindowManager.getDefault().findTopComponent("OutputTopComponent");
        OutputTopComponent otc = (OutputTopComponent) tc;
        
        String content = etc.getEditorContent();
        File file = FileChooserService.getSelectedFile("tex", "TeX files", true);
        if (file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            otc.logToOutput("[Open LaTeX Studio] Saving file " + file.getAbsolutePath());
        }
    }
}
