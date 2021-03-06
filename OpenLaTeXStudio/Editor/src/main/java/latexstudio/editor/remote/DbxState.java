/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.v2.files.FileMetadata;

/**
 *
 * @author Sebastian
 */
public class DbxState {
    private String path;
    private String revision;

    public DbxState(FileMetadata fileMetadata) {
        this(fileMetadata.getPathDisplay(), fileMetadata.getRev());
    }
    
    public DbxState(String path, String revision) {
        this.path = path;
        this.revision = revision;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
    
    
}
