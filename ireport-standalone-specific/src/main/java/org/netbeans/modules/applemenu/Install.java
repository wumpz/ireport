/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.netbeans.modules.applemenu;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.lang.reflect.*;
import org.openide.modules.ModuleInstall;
import org.openide.util.Utilities;

/** Module installer that installs an com.apple.eawt.ApplicationListener on
 * the com.apple.eawt.Application object for this session, which will interpret
 * apple events and call the appropriate action from the actions pool.
 *
 * @author  Tim Boudreau
 */
public class Install extends ModuleInstall {
    private CtrlClickHack listener;

    @Override
    public void restored () {
        listener = new CtrlClickHack();
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
        if (Utilities.isMac() ) { // NOI18N
//            FontReferenceQueue.install();
            try {
                Class<?> adapter = Class.forName("org.netbeans.modules.applemenu.NbApplicationAdapter");
                Method m = adapter.getDeclaredMethod("install", new Class[0] );
                m.invoke(adapter, new Object[0]);
            } catch (NoClassDefFoundError e) {
            } catch (ClassNotFoundException e) {
            } catch (Exception e) {
            }
            String pn = "apple.laf.useScreenMenuBar"; // NOI18N
            if (System.getProperty(pn) == null) {
                System.setProperty(pn, "true"); // NOI18N
            }
        }
    }
    
    @Override
    public void uninstalled () {
         if (listener != null) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
            listener = null;
         }
        if (System.getProperty("mrj.version") != null) { // NOI18N

            try {
                Class<?> adapter = Class.forName("org.netbeans.modules.applemenu.NbApplicationAdapter");
                Method m = adapter.getDeclaredMethod("uninstall", new Class[0] );
                m.invoke(adapter, new Object[0]);
            } catch (NoClassDefFoundError e) {
            } catch (ClassNotFoundException e) {
            } catch (Exception e) {
            }
        }
    }
}
