/* 
 * Copyright (c) 2017 White Hsu
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "latexstudio.editor.toolbar.SpellCheck"
)
@ActionRegistration(
        iconBase = "openlatexstudio/icons/spellcheck.png",
        displayName = "#CTL_SpellCheck"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 250),
    @ActionReference(path = "Toolbars/Comment", position = 3433)
})
@Messages("CTL_SpellCheck=Spell Check")
public final class SpellCheck implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());
    
    @Override
    public void actionPerformed(ActionEvent e) {            
            if(etc.getAutoCheckThread() == null) {
                etc.setAutoCheckThread(new Thread(new SpellCheckService(etc)));
                etc.getAutoCheckThread().start();
            } else {
                etc.getEditorState().setDirty(true);
            }        
    }
}
