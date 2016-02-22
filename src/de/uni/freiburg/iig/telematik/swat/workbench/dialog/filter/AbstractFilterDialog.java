/*
 * Copyright (c) 2016, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uni.freiburg.iig.telematik.swat.workbench.dialog.filter;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public abstract class AbstractFilterDialog extends JDialog {

        protected JButton btnCancel = new JButton("Cancel");
        protected JButton btnOk = new JButton("OK");

        private boolean inverted;

        private Map<String, JPanel> dialogOptions = null;

        public AbstractFilterDialog(Dialog owner, String title, Map<String, JPanel> dialogOptions, boolean inverted) {
                super(owner);
                initialize(title, dialogOptions, inverted);
        }

        public AbstractFilterDialog(Frame owner, String title, Map<String, JPanel> dialogOptions, boolean inverted) {
                super(owner);
                initialize(title, dialogOptions, inverted);
        }

        public AbstractFilterDialog(Window owner, String title, Map<String, JPanel> dialogOptions, boolean inverted) {
                super(owner);
                initialize(title, dialogOptions, inverted);
        }

        private void initialize(String title, Map<String, JPanel> dialogOptions, boolean inverted) {
                setTitle(title);

                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                setLayout(new BorderLayout());
                this.dialogOptions = dialogOptions;
                this.inverted = inverted;
                
                // add inverted checkbox
                final String invertLabel = "Invert filter:";
                JPanel invertFilterPanel = new JPanel();
                JCheckBox invertFilterCheckbox = new JCheckBox();
                invertFilterCheckbox.setEnabled(inverted);
                invertFilterPanel.add(invertFilterCheckbox);
                dialogOptions.put(invertLabel, invertFilterPanel);
                
                JPanel centerPanel = new JPanel(new GridLayout(dialogOptions.size() + 1, 2));
                for (Map.Entry<String, JPanel> line : dialogOptions.entrySet()) {
                        final JLabel label = new JLabel(line.getKey());
                        label.setHorizontalAlignment(JLabel.RIGHT);
                        label.setVerticalAlignment(JLabel.TOP);
                        centerPanel.add(label);
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.add(line.getValue(), BorderLayout.WEST);
                        centerPanel.add(panel);
                }
                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.add(btnOk);
                buttonPanel.add(btnCancel);
                add(centerPanel, BorderLayout.CENTER);
                add(buttonPanel, BorderLayout.SOUTH);
        }
}
