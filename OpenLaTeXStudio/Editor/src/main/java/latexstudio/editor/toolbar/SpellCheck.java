/*
 * Copyright (c) 2017 White Hsu
 *
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.Toolbar;
import org.openide.awt.ToolbarPool;
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
        etc.getEditorState().setHighlighted(!etc.getEditorState().isHighlighted());
        
        if(etc.getAutoCheckThread() == null) {
            etc.setAutoCheckThread(new Thread(new SpellCheckService(etc)));
            etc.getAutoCheckThread().start();
        } else {
            etc.getEditorState().setDirty(true);
        }

        JButton SpecllCheckButton = getToolbarSpecllCheckButton();
        JMenuItem SpecllCheckMenuItem = getMenuBarSpecllCheckMenuItem();
        if(etc.getEditorState().isHighlighted()) {
            if(SpecllCheckButton != null)
                SpecllCheckButton.setBorder(BorderFactory.createLoweredBevelBorder());
            if(SpecllCheckMenuItem != null) {
                SpecllCheckMenuItem.setBorder(BorderFactory.createLoweredBevelBorder());
                SpecllCheckMenuItem.setBorderPainted(true); //Border will not change without paint
            }            
        } else {
            if(SpecllCheckButton != null)
                SpecllCheckButton.setBorder(BorderFactory.createEmptyBorder());
            if(SpecllCheckMenuItem != null) {
                SpecllCheckMenuItem.setBorder(BorderFactory.createEmptyBorder());
                SpecllCheckMenuItem.setBorderPainted(true); //Border will not change without paint
            }            
        }
    }

    public JButton getToolbarSpecllCheckButton() {
        Toolbar[] toolbars = ToolbarPool.getDefault().getToolbars();
        for(Toolbar toolbar : toolbars) {
            if(toolbar.getDisplayName().trim().startsWith("Comment")) {
                for(Component component : toolbar.getComponents()) {
                    if(component.getClass().toString().contains("Button") &&
                            ((JButton) component).getToolTipText().contains("Spell Check")) {
                        return (JButton) component;
                    }
                }
            }
        }

        return null;
    }

    public JMenuItem getMenuBarSpecllCheckMenuItem() {
        JRootPane rp = (JRootPane) SwingUtilities.getRootPane(etc);
        JMenuBar menubar = rp.getJMenuBar();
        Component[] comps = menubar.getComponents();
        for(Component comp : comps) {
            if(comp.getName().trim().contains("Edit")) {
                JMenu menu = (JMenu) comp;
                for(int i = 0; i < menu.getItemCount(); i++) {
                    if(menu.getMenuComponent(i).getClass().toString().trim().contains("MenuItem") &&
                            ((JMenuItem) menu.getMenuComponent(i)).getText().trim().contains("Spell Check")) {
                        return (JMenuItem) menu.getMenuComponent(i);
                    }
                }
            }
        }

        return null;
    }
}
