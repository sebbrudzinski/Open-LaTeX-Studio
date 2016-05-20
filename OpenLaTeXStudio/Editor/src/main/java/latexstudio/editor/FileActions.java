/* 
 * Copyright (c) 2016 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import java.io.File;
import java.io.InputStream;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileService;
import static latexstudio.editor.util.ApplicationUtils.TEX_NAME;

/**
 * This class provides common editor actions
 *
 * @author maciej
 */
public class FileActions {

    private static final ApplicationLogger LOGGER = new ApplicationLogger("File Actions");

    private final EditorTopComponent etc = new TopComponentFactory<EditorTopComponent>()
            .getTopComponent(EditorTopComponent.class.getSimpleName());

    public FileActions() {
    }

    /**
     * Opens file content in EditorTopComponent
     *
     * @param file file, that exists and isn't a folder
     */
    public void openFile(File file) {
        if (file.isFile()) {
            String content = FileService.readFromFile(file.getAbsolutePath());
            etc.setEditorContent(content);
            etc.setCurrentFile(file);
            etc.setModified(false);
        }
    }

    /**
     * Saves current editor content; if file doesn't exist, shows save dialog
     */
    public void saveFile() {
        String content = etc.getEditorContent();
        File file = FileChooserService.getFileWithConfirmation(etc.getCurrentFile(), TEX_NAME, "TeX files", FileChooserService.DialogType.SAVE, true);

        if (file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            LOGGER.log("Saving file " + file.getAbsolutePath());
            etc.setCurrentFile(file);
            etc.setModified(false);
        }
    }

    /**
     * Shows save dialog to save current file into a new location
     */
    public void saveFileAs() {
        String content = etc.getEditorContent();
        File file = FileChooserService.getFileWithConfirmation(etc.getCurrentFile(), TEX_NAME, "TeX files", FileChooserService.DialogType.SAVEAS, true);

        if (file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            LOGGER.log("Saving file " + file.getAbsolutePath());
            etc.setCurrentFile(file);
            etc.setModified(false);
        }
    }
}
