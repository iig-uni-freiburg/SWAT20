package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import javax.swing.JCheckBoxMenuItem;

public class TogglePropertyItem extends JCheckBoxMenuItem {

    /**
     *
     */
    public TogglePropertyItem(Object target, String name, String fieldname) {
        this(target, name, fieldname, false);
    }

    /**
     *
     */
    public TogglePropertyItem(Object target, String name, String fieldname,
            boolean refresh) {
        this(target, name, fieldname, refresh, null);
    }

    /**
     *
     */
    public TogglePropertyItem(final Object target, String name,
            final String fieldname, final boolean refresh,
            ActionListener listener) {
        super(name);

        // Since action listeners are processed last to first we add the given
        // listener here which means it will be processed after the one below
        if (listener != null) {
            addActionListener(listener);
        }

        addActionListener(new ActionListener() {

            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                execute(target, fieldname, refresh);
            }
        });

        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

            /*
             * (non-Javadoc)
             * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
             */
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equalsIgnoreCase(fieldname)) {
                    update(target, fieldname);
                }
            }
        };

        if (target instanceof mxGraphComponent) {
            ((mxGraphComponent) target).addPropertyChangeListener(propertyChangeListener);
        } else if (target instanceof mxGraph) {
            ((mxGraph) target).addPropertyChangeListener(propertyChangeListener);
        }

        update(target, fieldname);
    }

    /**
     *
     */
    public void update(Object target, String fieldname) {
        if (target != null && fieldname != null) {
            try {
                Method getter = target.getClass().getMethod(
                        "is" + fieldname);

                if (getter != null) {
                    Object current = getter.invoke(target);

                    if (current instanceof Boolean) {
                        setSelected(((Boolean) current).booleanValue());
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     *
     */
    public void execute(Object target, String fieldname, boolean refresh) {
        if (target != null && fieldname != null) {
            try {
                Method getter = target.getClass().getMethod(
                        "is" + fieldname);
                Method setter = target.getClass().getMethod(
                        "set" + fieldname, new Class[]{boolean.class});

                Object current = getter.invoke(target);

                if (current instanceof Boolean) {
                    boolean value = !((Boolean) current).booleanValue();
                    setter.invoke(target, value);
                    setSelected(value);
                }

                if (refresh) {
                    mxGraph graph = null;

                    if (target instanceof mxGraph) {
                        graph = (mxGraph) target;
                    } else if (target instanceof mxGraphComponent) {
                        graph = ((mxGraphComponent) target).getGraph();
                    }

                    graph.refresh();
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
