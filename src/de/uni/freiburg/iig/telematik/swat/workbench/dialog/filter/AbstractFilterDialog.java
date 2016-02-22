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

import de.uni.freiburg.iig.telematik.sewol.log.filter.AbstractLogFilter;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
 * @param <O>
 */
public abstract class AbstractFilterDialog<O extends AbstractLogFilter> extends JDialog {

        final JButton btnCancel = new JButton("Cancel");
        final JButton btnOk = new JButton("OK");
        final JCheckBox cbInvertFilter = new JCheckBox();

        boolean abortedClosing = true;

        private boolean inverted;

        private final O filter;

        public AbstractFilterDialog(Dialog owner, O filter) {
                super(owner);
                this.filter = filter;
        }

        public AbstractFilterDialog(Frame owner, O filter) {
                super(owner);
                this.filter = filter;
        }

        public AbstractFilterDialog(Window owner, O filter) {
                super(owner);
                this.filter = filter;
        }

        public boolean isInverted() {
                return inverted;
        }

        public O getFilter() {
                return filter;
        }

        void updateFilter() {
                getFilter().setInverted(isInverted());
        }

        public boolean isAborted() {
                return abortedClosing;
        }

        void initialize(String title, Map<String, JPanel> dialogOptions, boolean inverted) {
                setModal(true);
                setTitle(title);
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                setLayout(new BorderLayout());
                this.inverted = inverted;

                // add inverted checkbox
                final String invertLabel = "Invert filter:";
                JPanel invertFilterPanel = new JPanel();
                cbInvertFilter.setSelected(inverted);
                cbInvertFilter.addItemListener(new InvertedCheckboxListener());
                invertFilterPanel.add(cbInvertFilter);
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
                btnOk.addActionListener(new OKButtonListener());
                btnCancel.addActionListener(new CancelButtonListener());
                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.add(btnOk);
                buttonPanel.add(btnCancel);
                add(centerPanel, BorderLayout.CENTER);
                add(buttonPanel, BorderLayout.SOUTH);
        }

        abstract void setUpDialog();

        private class InvertedCheckboxListener implements ItemListener {

                @Override
                public void itemStateChanged(ItemEvent e) {
                        if (e.getSource() == cbInvertFilter) {
                                inverted = cbInvertFilter.isSelected();
                        }
                }
        }

        private class OKButtonListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                        updateFilter();
                        abortedClosing = false;
                        dispose();
                }
        }

        private class CancelButtonListener implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                        abortedClosing = true;
                        dispose();
                }
        }
}
