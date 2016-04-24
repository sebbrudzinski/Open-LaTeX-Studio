/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import latexstudio.editor.settings.ApplicationSettings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

/**
 * Menu Presenter and listener to Line Wrap menu item
 * @author sabbir
 */

@ActionID(
        category = "Edit",
        id = "latexstudio.editor.menu.LineWrap"
)
@ActionRegistration(
        displayName = "#CTL_LineWrap",
        lazy = false
)
@ActionReference(path = "Menu/Edit", position = 3300)
@Messages("CTL_LineWrap=Line Wrap")
public final class LineWrap extends AbstractAction implements Presenter.Menu, ActionListener {
    private static final String WORD_WRAP = "Line Wrap";
    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem linewrapMenuItem = (JCheckBoxMenuItem)e.getSource();
        ApplicationSettings.Setting.LINEWRAP_ENABLED.setValue(linewrapMenuItem.isSelected());
        ApplicationSettings.INSTANCE.save();
    }
    
    @Override
    public JMenuItem getMenuPresenter() {
       JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(WORD_WRAP,null);
       menuItem.addActionListener(this);
       menuItem.setSelected((boolean) ApplicationSettings.Setting.LINEWRAP_ENABLED.getValue());
       return menuItem; 
    }
    
}