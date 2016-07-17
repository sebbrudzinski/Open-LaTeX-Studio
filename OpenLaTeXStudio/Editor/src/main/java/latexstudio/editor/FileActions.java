/*
 * Copyright (c) 2016 Sebastian Brudzinski
 *
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor;

import java.io.File;
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
        if (file != null && file.isFile()) {
            String content = FileService.readFromFile(file.getAbsolutePath());
            etc.setEditorContent(content);
            etc.setCurrentFile(file);
            etc.setModified(false);
            etc.setPreviewDisplayed(false);
        }
    }

    /**
     * Saves current editor content; if file doesn't exist, shows save dialog
     *
     * @param dialogType determines saving action can be either SAVE or SAVEAS
     */
    public void saveFile(FileChooserService.DialogType dialogType) {
        if (dialogType == FileChooserService.DialogType.SAVE || dialogType == FileChooserService.DialogType.SAVEAS) {
            String content = etc.getEditorContent();
            File file = FileChooserService.getFileWithConfirmation(etc.getCurrentFile(), TEX_NAME, "TeX files", dialogType, true);

            if (file != null) {
                FileService.writeToFile(file.getAbsolutePath(), content);
                LOGGER.log("Saving file " + file.getAbsolutePath());
                etc.setCurrentFile(file);
                etc.setModified(false);
                etc.setPreviewDisplayed(false);
            }
        }
    }
}
