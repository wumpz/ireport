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
package com.jaspersoft.ireport;

import com.jaspersoft.ireport.designer.JrxmlEditorSupport;
import com.jaspersoft.ireport.designer.utils.FileEncodingQueryImpl;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class JrxmlDataObject extends MultiDataObject 
        implements Lookup.Provider {
    
    
    final InstanceContent ic;

    public InstanceContent getIc() {
        return ic;
    }
   private AbstractLookup lookup;
    
    public JrxmlDataObject(FileObject pf, JrxmlDataLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        ic = new InstanceContent();
        lookup = new AbstractLookup(ic);
        JrxmlEditorSupport support = JrxmlEditorSupport.create(this);
        ic.add(support);
        ic.add(this);
        ic.add( getPrimaryFile() );
        ic.add( new FileEncodingQueryImpl());
        //ic.add(new JrxmlProviderImpl(getPrimaryFile()));
        //CookieSet cookies = getCookieSet();
        
        //cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
        
    }
   
    @Override
    protected Node createNodeDelegate() {
        return new JrxmlDataNode(this,getLookup()); //, 
    }
    
    
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {

        Object o = lookup.lookup(type);
        if (o == null && Node.Cookie.class.isAssignableFrom(type)) // try to look in the super cookie...
        {
           o = super.getCookie(type); 
        }
        
        return o instanceof Node.Cookie ? (T)o : null;
    }
}