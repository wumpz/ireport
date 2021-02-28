/*
 * iReport - Visual Designer for JasperReports.
 * Copyright (C) 2002 - 2009 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of iReport.
 *
 * iReport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * iReport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iReport. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.ireport.designer.utils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

/**
 *
 * @author gtoffoli
 */
public class WeakPreferenceChangeListener implements PreferenceChangeListener{
    WeakReference listenerRef;
    Object src;

    public WeakPreferenceChangeListener(PreferenceChangeListener listener, Object src){
        listenerRef = new WeakReference(listener);
        this.src = src;
    }

    private void removeListener(){
        try{
            Method method = src.getClass().getMethod("removePreferenceChangeListener"
                    , new Class[] {PreferenceChangeListener.class});
            method.invoke(src, new Object[]{ this });
        } catch(Exception e){
            e.printStackTrace(); 
        }
    }

    public void preferenceChange(PreferenceChangeEvent evt) {
        PreferenceChangeListener listener = (PreferenceChangeListener)listenerRef.get();
        if(listener==null){
            removeListener();
        }else
            listener.preferenceChange(evt);
    }
}
