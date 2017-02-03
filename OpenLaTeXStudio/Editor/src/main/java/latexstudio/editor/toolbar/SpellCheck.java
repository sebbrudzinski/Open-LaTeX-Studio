/* 
 * Copyright (c) 2017 White Hsu
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.BadLocationException;
import latexstudio.editor.EditorTopComponent;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        EditorTopComponent etc = (EditorTopComponent) tc;        
        
        try {
            etc.spellCheckAllText();
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }      
        
        etc.getEditorState().setDirty(true);        
    }
}
