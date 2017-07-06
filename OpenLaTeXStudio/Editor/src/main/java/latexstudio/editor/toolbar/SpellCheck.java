/*
 * Copyright (c) 2017 White Hsu
 *
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

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
public final class SpellCheck extends AbstractAction implements Presenter.Menu, Presenter.Toolbar, ActionListener {    
    
    private static final String SPELL_CHECK_TEXT = "Spell Check";
    private static JMenuItem specllCheckMenuItem;
    private static JButton specllCheckButton;
    
    @Override
    public void actionPerformed(ActionEvent e) {                
        EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());                
        
        etc.getEditorState().setHighlighted(!etc.getEditorState().isHighlighted());
        
        if(etc.getAutoCheckThread() == null) {
            etc.setAutoCheckThread(new Thread(new SpellCheckService(etc)));
            etc.getAutoCheckThread().start();
        } else {
            etc.getEditorState().setDirty(true);
        }

        if(etc.getEditorState().isHighlighted()) {
            if(specllCheckButton != null) {
                specllCheckButton.setBorder(BorderFactory.createLoweredBevelBorder());
            }
            if(specllCheckMenuItem != null) {
                specllCheckMenuItem.setBorder(BorderFactory.createLoweredBevelBorder());
                specllCheckMenuItem.setBorderPainted(true); //Border will not change without paint
            }            
        } else {
            if(specllCheckButton != null) {
                specllCheckButton.setBorder(BorderFactory.createEmptyBorder());
            }
            if(specllCheckMenuItem != null) {
                specllCheckMenuItem.setBorder(BorderFactory.createEmptyBorder());
                specllCheckMenuItem.setBorderPainted(true); //Border will not change without paint
            }            
        }
    }

    @Override
    public JMenuItem getMenuPresenter() {
       specllCheckMenuItem = new JMenuItem(SPELL_CHECK_TEXT, new ImageIcon(ImageUtilities.loadImage("openlatexstudio/icons/spellcheck.png")));
       specllCheckMenuItem.addActionListener(this);
       
       return specllCheckMenuItem; 
    }
    
    @Override
    public JButton getToolbarPresenter() {
       specllCheckButton = new JButton(new ImageIcon(ImageUtilities.loadImage("openlatexstudio/icons/spellcheck24.png")));
       specllCheckButton.addActionListener(this);
       
       return specllCheckButton; 
    }
}
