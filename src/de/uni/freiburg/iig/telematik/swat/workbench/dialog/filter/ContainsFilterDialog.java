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

import de.uni.freiburg.iig.telematik.sewol.log.filter.ContainsFilter;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public final class ContainsFilterDialog extends AbstractFilterDialog<ContainsFilter> {

        JComboBox<ContainsFilter.ContainsFilterParameter> tfParameter = new JComboBox<>(ContainsFilter.ContainsFilterParameter.values());
        JTextField tfValue = new JTextField();

        public ContainsFilterDialog(Frame owner) {
                this(owner, new ContainsFilter());
        }

        public ContainsFilterDialog(Frame owner, ContainsFilter filter) {
                super(owner, filter);
                setUpDialog();
        }

        @Override
        final void setUpDialog() {
                Map<String, JPanel> dialogOption = new LinkedHashMap<>();
                JPanel parameterPanel = new JPanel(new FlowLayout());
                tfParameter.setSelectedItem(getFilter().getParameter());
                parameterPanel.add(tfParameter);
                dialogOption.put("Parameter:", parameterPanel);
                JPanel valuePanel = new JPanel(new FlowLayout());
                tfValue.setColumns(20);
                tfValue.setText(getFilter().getValue());
                valuePanel.add(tfValue);
                dialogOption.put("Value:", valuePanel);

                initialize("Contains filter", dialogOption, getFilter().isInverted());
        }

        @Override
        void updateFilter() {
                super.updateFilter();
                getFilter().setParameter((ContainsFilter.ContainsFilterParameter) tfParameter.getSelectedItem());
                getFilter().setValue(tfValue.getText());
        }

        public static void main(String[] args) {
                JFrame frame = new JFrame();
                ContainsFilterDialog dialog = new ContainsFilterDialog(frame);
                dialog.pack();
                dialog.setVisible(true);
                if (!dialog.isAborted()) {
                        System.out.println(dialog.getFilter());
                }
        }
}
