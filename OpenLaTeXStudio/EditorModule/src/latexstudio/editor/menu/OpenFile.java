/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import latexstudio.editor.EditorTopComponent;
import latexstudio.editor.TopComponentFactory;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileChooserService.DialogType;
import latexstudio.editor.files.FileService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "latexstudio.editor.OpenFile"
)
@ActionRegistration(
        displayName = "#CTL_OpenFile"
)
@ActionReference(path = "Menu/File", position = 1200)
@Messages("CTL_OpenFile=Open File")

public final class OpenFile implements ActionListener {

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = FileChooserService.getSelectedFile("tex", "TeX files", DialogType.OPEN);
        if (file != null) {
            openFile(etc, file);
        }
    }

    /**
      * Opens file content in EditorTopComponent
      *
      * @param etc editor component, where you want to open specified file
      * @param file file, that exists and isn't a folder
     */
    public void openFile(EditorTopComponent etc, File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String content = FileService.readFromFile(file.getAbsolutePath());
            etc.setEditorContent(content);
            etc.setCurrentFile(file);
        }
    }

    public void openFile(EditorTopComponent etc, InputStream file) {
        etc.setEditorContent(FileService.readFromStream(file));
    }
}
