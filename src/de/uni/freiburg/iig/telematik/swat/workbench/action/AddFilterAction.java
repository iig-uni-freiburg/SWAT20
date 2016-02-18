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
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public class AddFilterAction extends AbstractWorkbenchAction {

        private final static String ICON = "plus2";
        private final static String TOOLTIP = "adds a new filter to the view";
        private final static int KEY_EVENT = KeyEvent.VK_F4;

        public AddFilterAction() {
                super("Add new filter");
                setTooltip(TOOLTIP);
                setAcceleratorKey(KeyStroke.getKeyStroke(KEY_EVENT, 0));
                try {
                        setIcon(IconFactory.getIcon(ICON));
                } catch (ParameterException | PropertyException | IOException e) {
                }
        }

        public AddFilterAction(String name, Icon icon) {
                super(name);
                setAcceleratorKey(KeyStroke.getKeyStroke(KEY_EVENT, 0));
                if (icon != null) {
                        setIcon(icon);
                }
        }

        public AddFilterAction(String string) {
                super(string);
                setAcceleratorKey(KeyStroke.getKeyStroke(KEY_EVENT, 0));
                setTooltip(TOOLTIP);
                setAcceleratorKey(KeyStroke.getKeyStroke(KEY_EVENT, 0));
                try {
                        setIcon(IconFactory.getIcon(ICON));
                } catch (ParameterException | PropertyException | IOException e) {
                }
        }

        @Override
        protected void doFancyStuff(ActionEvent e) throws Exception {
                // TODO add functionality
        }
}
