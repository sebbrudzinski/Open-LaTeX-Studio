/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import java.io.File;
import java.io.InputStream;
import latexstudio.editor.files.FileChooserService;
import latexstudio.editor.files.FileService;

/**
 * This class provides common editor actions
 *
 * @author maciej
 */
public class FileActions {

    private EditorTopComponent etc;
    private static final ApplicationLogger LOGGER = new ApplicationLogger("File Actions");

    public FileActions(EditorTopComponent etc) {
        this.etc = etc;
    }

    /**
     * Opens file content in EditorTopComponent
     *
     * @param file file, that exists and isn't a folder
     */
    public void openFile(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            String content = FileService.readFromFile(file.getAbsolutePath());
            etc.setEditorContent(content);
            etc.setCurrentFile(file);
            etc.setModified(false);
        }
    }

    /**
     * Opens file content in EditorTopComponent
     *
     * @param file text file InputStream
     */
    public void openFile(InputStream file) {
        etc.setEditorContent(FileService.readFromStream(file));
        etc.setModified(false);
    }

    /**
     * Saves current editor content; if file doesn't exist, shows save dialog
     */
    public void saveFile() {
            String content = etc.getEditorContent();
            File file = FileChooserService.getFileWithConfirmation(etc.getCurrentFile(), "tex", "TeX files", FileChooserService.DialogType.SAVE, true);

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
        File file = FileChooserService.getFileWithConfirmation(etc.getCurrentFile(), "tex", "TeX files", FileChooserService.DialogType.SAVEAS, true);

        if (file != null) {
            FileService.writeToFile(file.getAbsolutePath(), content);
            LOGGER.log("Saving file " + file.getAbsolutePath());
            etc.setCurrentFile(file);
            etc.setModified(false);
        }
    }
}
