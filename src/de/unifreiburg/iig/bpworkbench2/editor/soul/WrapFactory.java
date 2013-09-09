package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.awt.*;
import javax.swing.*;


public class WrapFactory {

    public static JPanel wrapInScrollpane(JComponent component) {
        JPanel wrapped = new JPanel();
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setPreferredSize(new Dimension(1600, 1600));
        wrapped.add(scrollPane, "wrap");
        return wrapped;
    }

    public static JPanel wrapInScrollPaneWithHistory(JComponent component) {
        JPanel wrapped = wrapInScrollpane(component);
        wrapped.add(createLegendPanel());
        return wrapped;
    }

    private static JLabel createLegendLabel(String text, Color background) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(70, 15));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setOpaque(true);
        label.setBackground(background);
        return label;
    }

    private static JPanel createLegendPanel() {
        JPanel history = new JPanel();
        history.add(new JLabel("Legend:"), "wrap");
        JLabel label = createLegendLabel("common", Color.white);
        history.add(label);
        label = createLegendLabel("root", Color.decode(Constants.ROOT_COLOR));
        label.setForeground(Color.WHITE);
        history.add(label, "wrap");
        label = createLegendLabel("terminal", Color.decode(Constants.TERMINAL_COLOR));
        history.add(label);
        label = createLegendLabel("immediate", Color.decode(Constants.IMMEDIATE_COLOR));
        history.add(label, "wrap");
        label = createLegendLabel("<html><b> covering </b></html>", Color.white);
        history.add(label);
        label = createLegendLabel("<html> <i>duplicate</i> </html>", Color.white);
        history.add(label, "wrap");
        return history;
    }
}
