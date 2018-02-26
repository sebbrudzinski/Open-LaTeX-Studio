/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.v2.files.FileMetadata;
import java.util.Date;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Sebastian
 */
public class DbxEntryDto {
    private String name;
    private String path;
    private String humanSize;
    private Date lastModified;
    private String revision;
    
    public DbxEntryDto(FileMetadata metadata) {
        name = metadata.getName();
        path = metadata.getPathDisplay();
        humanSize = FileUtils.byteCountToDisplaySize(metadata.getSize());
        lastModified = metadata.getServerModified();
        revision = metadata.getRev();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHumanSize() {
        return humanSize;
    }

    public void setHumanSize(String humanSize) {
        this.humanSize = humanSize;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return path + " (" + humanSize + ")";
    }

}
