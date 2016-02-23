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
package de.uni.freiburg.iig.telematik.swat.workbench.action;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.sewol.log.filter.AbstractLogFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.ContainsFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.MaxEventsFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.MinEventsFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.TimeFilter;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.filter.AbstractFilterDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.filter.ContainsFilterDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.filter.MaxFilterDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.filter.MinFilterDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.filter.TimeFilterDialog;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.Icon;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public class AddFilterAction extends AbstractWorkbenchAction {

        public final static String FILTER_NAME_FORMAT = "New %s filter";

        private final static String DEFAULT_ICON = "plus2";
        private final static String TOOLTIP = "adds a new filter to the view";

        private final SwatTreeNode viewNode;
        private final AbstractLogFilter filter;

        public AddFilterAction(SwatTreeNode viewNode, AbstractLogFilter filter) {
                this(String.format(FILTER_NAME_FORMAT, filter.getName()), null, viewNode, filter);
        }

        public AddFilterAction(String name, SwatTreeNode viewNode, AbstractLogFilter filter) {
                this(name, null, viewNode, filter);
        }

        public AddFilterAction(String name, Icon icon, SwatTreeNode viewNode, AbstractLogFilter filter) {
                super(name);
                this.viewNode = viewNode;
                this.filter = filter;
                setTooltip(TOOLTIP);
                if (icon != null) {
                        setIcon(icon);
                } else {
                        try {
                                setIcon(IconFactory.getIcon(DEFAULT_ICON));
                        } catch (ParameterException | PropertyException | IOException ex) {
                        }
                }
        }

        @Override
        protected void doFancyStuff(ActionEvent e) throws Exception {
                final String viewName = ((LogView) viewNode.getUserObject()).getName();
                LogView view = SwatComponents.getInstance().getContainerLogViews().getComponent(viewName);

                AbstractFilterDialog dialog = null;
                if (filter instanceof ContainsFilter) {
                        dialog = new ContainsFilterDialog(Workbench.getInstance(), (ContainsFilter) filter);
                } else if (filter instanceof MaxEventsFilter) {
                        dialog = new MaxFilterDialog(Workbench.getInstance(), (MaxEventsFilter) filter);
                } else if (filter instanceof MinEventsFilter) {
                        dialog = new MinFilterDialog(Workbench.getInstance(), (MinEventsFilter) filter);
                } else if (filter instanceof TimeFilter) {
                        dialog = new TimeFilterDialog(Workbench.getInstance(), (TimeFilter) filter);
                }
                if (dialog == null) {
                        return;
                }

                dialog.pack();
                dialog.setVisible(true);
                if (dialog.isAborted()) {
                        return;
                }
                
                view.addFilter(filter);

                SwatComponents.getInstance().reloadComponents();
                SwatComponents.getInstance().getContainerLogViews().storeComponent(viewName);
        }
}
