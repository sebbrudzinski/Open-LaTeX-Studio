/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Help",
        id = "latexstudio.editor.About"
)
@ActionRegistration(
        displayName = "#CTL_About"
)
@ActionReference(path = "Menu/Help", position = 1400)
@Messages("CTL_About=About")
public final class About implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, new AboutPanel(), "About Open LaTeX Studio", JOptionPane.PLAIN_MESSAGE);
    }
}
