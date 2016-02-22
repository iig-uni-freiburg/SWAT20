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

import java.awt.Frame;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public class MinFilterDialog extends AbstractFilterDialog {

        public MinFilterDialog(Frame owner, String title, Map<String, JPanel> dialogOption, boolean inverted) {
                super(owner, title, dialogOption, inverted);
        }

        public static void main(String[] args) {
                Map<String, JPanel> dialogOption = new HashMap<>();
                JPanel minPanel = new JPanel();
                NumberFormat integerFieldFormatter = NumberFormat.getIntegerInstance();
                integerFieldFormatter.setGroupingUsed(false);
                integerFieldFormatter.setMaximumFractionDigits(0);
                JFormattedTextField minTextField = new JFormattedTextField(integerFieldFormatter);
                minTextField.setColumns(10);
                minPanel.add(minTextField);
                dialogOption.put("Min # of events:", minPanel);
                JFrame frame = new JFrame();
                MinFilterDialog dialog = new MinFilterDialog(frame, "Min # of events filter", dialogOption, true);
                dialog.pack();
                dialog.setVisible(true);
        }
}
