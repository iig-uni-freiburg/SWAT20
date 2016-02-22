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

import de.uni.freiburg.iig.telematik.sewol.log.filter.TimeFilter;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public final class TimeFilterDialog extends AbstractFilterDialog<TimeFilter> {

        private DatePickerPanel startDatePanel = null;
        private DatePickerPanel endDatePanel = null;

        public TimeFilterDialog(Frame owner) {
                this(owner, new TimeFilter());
        }

        public TimeFilterDialog(Frame owner, TimeFilter filter) {
                super(owner, filter);
                setUpDialog();
        }

        @Override
        final void setUpDialog() {
                Map<String, JPanel> dialogOption = new LinkedHashMap<>();
                startDatePanel = new DatePickerPanel(getFilter().getStartDate());
                dialogOption.put("Start date:", startDatePanel);
                endDatePanel = new DatePickerPanel(getFilter().getEndDate());
                dialogOption.put("End date:", endDatePanel);

                initialize("Date filter", dialogOption, getFilter().isInverted());
        }

        @Override
        void updateFilter() {
                super.updateFilter();
                getFilter().setStartDate(startDatePanel.getDate());
                getFilter().setEndDate(endDatePanel.getDate());
        }

        public static class DatePickerPanel extends JPanel {

                final JCheckBox cbIsNull;
                final JSpinner spDate;
                final SpinnerModel smDateModel;

                private Date date;

                public DatePickerPanel(Date date) {
                        super(new FlowLayout());

                        this.date = date;

                        // is null checkbox
                        cbIsNull = new JCheckBox();
                        cbIsNull.setToolTipText("is null?");

                        // date text field
                        smDateModel = new SpinnerDateModel();
                        spDate = new JSpinner(smDateModel);

                        cbIsNull.setSelected(date == null);
                        spDate.setEnabled(date != null);
                        if (date != null) {
                                smDateModel.setValue(date);
                        }

                        cbIsNull.addItemListener(new CheckboxListener());
                        spDate.addChangeListener(new DatePickerListener());

                        add(cbIsNull);
                        add(spDate);
                }

                public Date getDate() {
                        return date;
                }

                private class CheckboxListener implements ItemListener {

                        @Override
                        public void itemStateChanged(ItemEvent e) {
                                if (e.getSource() == cbIsNull) {
                                        spDate.setEnabled(!cbIsNull.isSelected());
                                        if (cbIsNull.isSelected()) {
                                                date = null;
                                        } else {
                                                date = (Date) spDate.getValue();
                                        }
                                }
                        }
                }

                private class DatePickerListener implements ChangeListener {

                        @Override
                        public void stateChanged(ChangeEvent e) {
                                if (e.getSource() == spDate) {
                                        date = (Date) spDate.getValue();
                                }
                        }
                }
        }

        public static void main(String[] args) {
                JFrame frame = new JFrame();
                TimeFilterDialog dialog = new TimeFilterDialog(frame);
                dialog.pack();
                dialog.setVisible(true);
                if (!dialog.isAborted()) {
                        System.out.println(dialog.getFilter());
                }
        }
}
