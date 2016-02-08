package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LogViewViewer extends JScrollPane implements ViewComponent {

        private final LogModel model;
        private final LogView view;
        
        private final String name;

        private JComponent mainComponent = null;
        private JComponent properties = null;

        public LogViewViewer(LogView view) throws ProjectComponentException {
                this.view = view;
                model = SwatComponents.getInstance().getLog(view.getParentLogName());
                name = view.getName();
        }

        @Override
        public JComponent getMainComponent() {
                try {
                        mainComponent = getEditorField();
                        getViewport().add(mainComponent);
                        mainComponent.revalidate();
                        mainComponent.repaint();
                } catch (MalformedURLException e) {
                        Workbench.errorMessage("Could not generate Log viewer, URL malformed", e, true);
                } catch (IOException e) {
                        Workbench.errorMessage("Could not generate Log viewer, I/O Error", e, true);
                }
                return this;
        }

        private JComponent getEditorField() throws MalformedURLException, IOException {
                JEditorPane editor = new JEditorPane(view.getFileReference().toURI().toURL());
                editor.setEditable(false);
                return editor;
        }

        @Override
        public JComponent getPropertiesView() {
                if (properties == null) {
                        properties = new JPanel();
                        properties.setLayout(new FlowLayout());
                        properties.add(new JLabel("Textual file"));
                        properties.validate();
                        properties.repaint();
                }
                return properties;
        }

        @Override
        public String getName() {
                return name;
        }

        public LogModel getModel() {
                return model;
        }

        public LogView getView() {
                return view;
        }

        @Override
        public String toString() {
                return name;
        }

        @Override
        public int hashCode() {
                int hash = 3;
                hash = 89 * hash + Objects.hashCode(model);
                hash = 89 * hash + Objects.hashCode(view);
                return hash;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                final LogViewViewer other = (LogViewViewer) obj;
                if (!Objects.equals(this.model, other.model)) {
                        return false;
                }
                return Objects.equals(this.view, other.view);
        }
}
