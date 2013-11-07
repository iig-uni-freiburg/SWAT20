package de.unifreiburg.iig.bpworkbench2.editor.relict;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutFrame extends JDialog {

    public AboutFrame(Frame owner) {
        super(owner);
        setTitle("About");
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("PetriNet Editor");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 0, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        JLabel subtitleLabel = new JLabel("Shilov Yuri IO-81");
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 0));
        panel.add(subtitleLabel, BorderLayout.CENTER);
        panel.add(new JSeparator(), BorderLayout.SOUTH);
        getContentPane().add(panel, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(new JLabel("Created during \"Computer Simulation\" course"));
        content.add(new JLabel("at Kiev Polytechnic Institute in 2010"));
        content.add(new JLabel(" "));
        content.add(new JLabel("Based on:"));
        content.add(new JLabel("JGraph X - The Swing Portion of mxGraph"));
        content.add(new JLabel("mxGraph Version 1.4.1.2"));
        content.add(new JLabel("Copyright (C) 2009 by JGraph Ltd."));
        content.add(new JLabel("All rights reserved."));
        getContentPane().add(content, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JSeparator(), BorderLayout.NORTH);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(closeButton);
        setResizable(false);
        setSize(400, 250);
    }
}
