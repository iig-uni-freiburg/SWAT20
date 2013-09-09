package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import de.unifreiburg.iig.bpworkbench2.editor.soul.CellInfo;

public class InputDialog extends JDialog {

    private CellInfo info;

    public InputDialog(CellInfo info) {
        this.info = info;
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        setModal(true);
        setTitle("Transiton " + info.getName());
//        setSize(250, 250);
        initComponents();
        pack();
    }

    private void initComponents() {
        JPanel outer = new JPanel();
        outer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel label = new JLabel("Intensity:");
        final DigitInputField fieldLambda = new DigitInputField(String.valueOf(info.getLambda()));
        outer.add(label);
        outer.add(fieldLambda, "wrap");
        label = new JLabel("Probability:");
        final DigitInputField fieldProbability = new DigitInputField(String.valueOf(info.getProbability()));
        outer.add(label);
        outer.add(fieldProbability, "wrap");
        label = new JLabel("Variance:");
        final DigitInputField fieldVariance = new DigitInputField(String.valueOf(info.getVariance()));
        outer.add(label);
        outer.add(fieldVariance, "wrap");
        add(outer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JSeparator(), BorderLayout.NORTH);
        JButton button = new JButton("   Ok   ");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                info.setLambda(Double.parseDouble(fieldLambda.getText()));
                info.setProbability(Double.parseDouble(fieldProbability.getText()));
                info.setVariance(Double.parseDouble(fieldVariance.getText()));
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(button);
        button = new JButton("Cancel");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(button);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(button);

    }

    private class DigitInputField extends JTextField {

        public DigitInputField(String string) {
            super(string);
            setColumns(8);
            addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c)) {
                        if (c != '.') {
                            e.consume();
                        }
                    }
                    super.keyPressed(e);
                }
            });
        }
    }
}
