/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.remote;

import com.dropbox.core.DbxEntry;
import java.util.Date;

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
    
    public DbxEntryDto(DbxEntry entry) {
        name = entry.name;
        path = entry.path;
        humanSize = entry.asFile().humanSize;
        lastModified = entry.asFile().lastModified;
        revision = entry.asFile().rev;
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
