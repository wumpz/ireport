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
package com.jaspersoft.ireport.designer.compiler.xml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.sf.jasperreports.engine.xml.JRXmlDigester;

import org.apache.commons.digester.FactoryCreateRule;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: SourceTraceDigester.java 22 2007-03-08 15:18:26Z lucianc $
 */
public class SourceTraceDigester extends JRXmlDigester
{

	private final Map sourceLocations = new HashMap();
	
	protected static class ElementStack
	{
		protected static class ElementInfo
		{
			private String path;
			private final Map childrenCounts = new HashMap();
			
            @SuppressWarnings("unchecked")
			public int addChild(String name)
			{
				Integer last = (Integer) childrenCounts.get(name);
				int position = last == null ? 1 : (last.intValue() + 1);
				childrenCounts.put(name, new Integer(position));
				return position;
			}
			
			public String getPath()
			{
				return path;
			}
			
			public void setPath(String path)
			{
				this.path = path;
			}
		}
		
		private final LinkedList infoStack = new LinkedList();
		
        @SuppressWarnings("unchecked")
		public void pushElement(String elementName)
		{
			String currentPath;
			if (infoStack.isEmpty())
			{
				currentPath = "/" + elementName;
			}
			else
			{
				ElementInfo parentInfo = (ElementInfo) infoStack.getFirst();
				int position = parentInfo.addChild(elementName);
				currentPath = parentInfo.getPath() + "/" + elementName + "[" + position + "]";
			}
			
			ElementInfo info = new ElementInfo();
			info.setPath(currentPath);

			infoStack.addFirst(info);
		}
		
		public void popElement()
		{
			infoStack.removeFirst();
		}
		
		public String getCurrentPath()
		{
			ElementInfo info = (ElementInfo) infoStack.getFirst();
			return info.getPath();
		}
	}

	private final ElementStack elementStack = new ElementStack();

	public SourceTraceDigester()
	{
		super();
	}

	public SourceTraceDigester(XMLReader xmlReader)
	{
		super(xmlReader);
	}

	public void addFactoryCreate(String pattern, Class clazz)
	{
        addRule(pattern, new SourceTraceFactoryCreateRule(clazz));
	}

	public void addFactoryCreate(String pattern, String className)
	{
        addRule(pattern, new SourceTraceFactoryCreateRule(className));
	}

	public void addFactoryCreate(String pattern, Class clazz, String attributeName)
	{
        addRule(pattern, new SourceTraceFactoryCreateRule(clazz, attributeName));
	}

	public void addFactoryCreate(String pattern, String className, String attributeName)
	{
        addRule(pattern, new SourceTraceFactoryCreateRule(className, attributeName));
	}

	public SourceLocation getLocation(Object instance)
	{
		return (SourceLocation) sourceLocations.get(instance);
	}

	public void startElement(String namespaceURI, String localName,
            String qName, Attributes list) throws SAXException
	{
        String name = localName != null && localName.length() > 0 ? localName : qName;
		elementStack.pushElement(name);

		super.startElement(namespaceURI, localName, qName, list);
	}

	public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException
	{
		super.endElement(namespaceURI, localName, qName);
		
		elementStack.popElement();
	}
	
    @SuppressWarnings("unchecked")
	protected void objectCreated()
	{
		Object instance = peek();
		if (instance != null && !sourceLocations.containsKey(instance))
		{
			SourceLocation location = currentLocation();
			sourceLocations.put(instance, location);
		}
	}

	protected SourceLocation currentLocation()
	{
		Locator documentLocator = getDocumentLocator();
		SourceLocation location = new SourceLocation();
		location.setLineNumber(documentLocator.getLineNumber());
		location.setColumnNumber(documentLocator.getColumnNumber());
		location.setXPath(elementStack.getCurrentPath());
		return location;
	}
	
	protected class SourceTraceFactoryCreateRule extends FactoryCreateRule
	{

		public SourceTraceFactoryCreateRule(Class clazz)
		{
			super(clazz, false);
		}

		public SourceTraceFactoryCreateRule(String className)
		{
			super(className, false);
		}

		public SourceTraceFactoryCreateRule(Class clazz, String attributeName)
		{
			super(clazz, attributeName, false);
		}

		public SourceTraceFactoryCreateRule(String className, String attributeName)
		{
			super(className, attributeName, false);
		}

		public void begin(String namespace, String name, Attributes attributes) throws Exception
		{
			super.begin(namespace, name, attributes);
			
			objectCreated();
		}

	}
}

