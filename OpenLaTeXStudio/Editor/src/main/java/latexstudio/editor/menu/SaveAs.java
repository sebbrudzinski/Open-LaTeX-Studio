package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.ApplicationLogger;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.FileActions;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "latexstudio.editor.SaveAs"
)
@ActionRegistration(
        iconBase = "openlatexstudio/icons/saveas.png",
        displayName = "#CTL_SaveAs"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1301),
    @ActionReference(path = "Toolbars/File", position = 2223)
})
@Messages("CTL_SaveAs=Save As...")
public final class SaveAs implements ActionListener {
    
    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    private static final ApplicationLogger LOGGER = new ApplicationLogger("Open LaTeX Studio");

    @Override
    public void actionPerformed(ActionEvent e) {               
        new FileActions().saveFileAs();
    }
}

