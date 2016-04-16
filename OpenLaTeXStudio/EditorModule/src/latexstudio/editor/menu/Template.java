package latexstudio.editor.menu;

public class Template {

    private String name, path, description, source;

    public Template(String name, String path, String description, String source) {
        this.name = name;
        this.path = path;
        this.description = description;
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return name;
    }
}
