package de.unifreiburg.iig.bpworkbench2.editor.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class DefaultFileFilter extends FileFilter {

    protected String extension;
    protected String description;

    public DefaultFileFilter(String extension, String description) {
        this.extension = extension.toLowerCase();
        this.description = description;
    }

    public boolean accept(File file) {
        return file.isDirectory() || file.getName().toLowerCase().endsWith(extension);
    }

    public String getDescription() {
        return description;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
