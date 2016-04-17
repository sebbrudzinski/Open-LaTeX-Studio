/* 
 * Copyright (c) 2015 Sebastian Brudzinski
 * 
 * See the file LICENSE for copying permission.
 */
package latexstudio.editor.menu;

public class Template {

    private String name;
    private String path;
    private String description;
    private String source;

    public Template() {
    }

    public void setName(String name) {
        this.name = name;
    }

    //Works as getName()
    @Override
    public String toString() {
        return name;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
