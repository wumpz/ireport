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
package com.jaspersoft.ireport.designer.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import net.sf.jasperreports.crosstabs.JRCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.netbeans.api.languages.CompletionItem;
import org.netbeans.api.languages.CompletionItem.Type;
import org.netbeans.api.languages.Context;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class JRSyntaxSupport {

    List<ExpChunk> expChunks = new ArrayList<ExpChunk>();
    Context context = null;
    Token tokenAtPosition = null;
    int currentExpChunkIndex = -1;
    Token previousPositionToken = null;
    String textFromLastValidToked = "";
    TokenHierarchy th = null;
    TokenSequence ts = null;
    int caretPos = 0;

    public JRSyntaxSupport(Context context)
    {
        this.context = context;
        AbstractDocument document = (AbstractDocument) context.getDocument();
            document.readLock();
        

            try {


                th = TokenHierarchy.get(document);
                ts = th.tokenSequence();
                caretPos = ExpressionContext.activeEditor.getCaretPosition();

                try {
                    textFromLastValidToked = document.getText(0, caretPos);
                } catch (BadLocationException ex) {
                    //Exceptions.printStackTrace(ex);
                }

                ts.moveStart();
                int pos = 0;
                int indexExpChunk = 0;
                while (ts.moveNext())
                {
                    Token t = ts.token();
                    if (t.length() <= 0) continue; // skip null tokens...

                    int t_start = pos;
                    int t_end = pos + t.length();
                    pos = t_end;

                    expChunks.add(new ExpChunk(t, t_start));
                    if (caretPos > t_start && caretPos <= t_end)
                    {
                        tokenAtPosition = t;
                        currentExpChunkIndex=indexExpChunk;
                    }
                    if (caretPos > t_end)
                    {
                        previousPositionToken = t;
                        try {
                            textFromLastValidToked = document.getText(t_end, caretPos - t_end);
                        } catch (BadLocationException ex) {
                            //Exceptions.printStackTrace(ex);
                        }
                    }
                    indexExpChunk++;
                }
            } finally {
                document.readUnlock();
            }
    }

    public List<CompletionItem> getItems()
    {
        List<CompletionItem> result = new ArrayList<CompletionItem> ();
        


        if (currentExpChunkIndex >= 0)
        {
            ExpChunk currentExpChunk = expChunks.get(currentExpChunkIndex);
            // Code completion starts here...
            List<Identifier> callsPath = null;
            if (tokenString(currentExpChunk).equals("."))
            {
                callsPath = previousIdentifiers(previousChunk(currentExpChunk));
            }
            else  if (textFromLastValidToked.length() > 0 && currentExpChunk.token.id().name().equals("identifier"))
            {
                ExpChunk prevExpChunk = previousChunk(currentExpChunk);
                if (prevExpChunk != null && tokenString(prevExpChunk).equals("."))
                {
                    callsPath = previousIdentifiers(previousChunk(prevExpChunk));
                    if (callsPath != null)
                    {
                        callsPath.add(0, new Identifier(textFromLastValidToked, Identifier.UNDEFINED));
                    }
                }
            }
            

            if (callsPath != null)
            {
                List<CompletionItem> methodsAndAttributes = getMethods(callsPath);
                if (methodsAndAttributes != null) result.addAll(methodsAndAttributes);
            }

            if (result.isEmpty())
            {
                if (tokenString(currentExpChunk).equals("$") ||
                    currentExpChunk.token.id().name().equals("whitespace") ||
                    (currentExpChunk.token.id().name().equals("operator") &&
                     !tokenString(currentExpChunk).equals(".")))
                {
                    result.addAll(getExpressionContextFields());
                    result.addAll(getExpressionContextVariables());
                    result.addAll(getExpressionContextParameters());
                }
            }
        }

        
        return result;
    }

    private String tokenString(ExpChunk currentExpChunk)
    {
        if (currentExpChunk.token != null &&
            currentExpChunk.token.text() != null)
        {
            return  currentExpChunk.token.text().toString();
        }
        return "";
    }


    public static List<CompletionItem> completionItems (Context context) {
 
        List<CompletionItem> result = new ArrayList<CompletionItem> ();
        
        ExpressionContext editorContext = ExpressionContext.getGlobalContext();

        if (ExpressionContext.activeEditor != null && ExpressionContext.activeEditor.getDocument() == context.getDocument())
        {

            AbstractDocument document = (AbstractDocument) context.getDocument();
            document.readLock();

            

            try {

                  JRSyntaxSupport s = new JRSyntaxSupport(context);

                  return s.getItems();
//                Method[] ms = ((Class)obj).getMethods();
//                                for (int i=0; i<ms.length; ++i)
//                                {
//                                     result.add(CompletionItem.create(ms[i].getName()));
//                                }

            } finally {
                document.readUnlock();
            }
        }
        else
        {
            System.out.println("Active editor not the same as the one in the expression context...");
            System.out.flush();
        }
 
        if (editorContext == null) return result;
        
        return result;
    }
    
    private static Token previousToken (TokenSequence ts) {
        do {
            if (!ts.movePrevious ()) return ts.token ();
        } while (
            ts.token ().id ().name ().endsWith ("whitespace") ||
            ts.token ().id ().name ().endsWith ("comment")
        );
        return ts.token ();
    }

    private List<CompletionItem> getMethods(List<Identifier> list) {

        List<CompletionItem> methods = new ArrayList<CompletionItem>();

        if (list.size() == 0) return methods;
        Identifier id = list.get(list.size()-1);
        if (id.getType() == Identifier.REPORT_OBJECT)
        {
            // Suppose this report Object is a String;
            try {

                ExpressionContext expressionContext = ExpressionContext.getGlobalContext();
                if (expressionContext != null)
                {
                    String objName = id.getName().substring(3);
                    objName = objName.substring(0,objName.length()-1);
                    char type = id.getName().charAt(1);
                    String className = expressionContext.findObjectClassName(objName, type);
                    try {
                        Class c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                        return getMethods(c, list.subList(0,list.size()-1));
                    } catch (Throwable ttt) {}
                }
            } catch (Exception ex){

            ex.printStackTrace();
            }
        }

        return methods;
    }

    /**
     * Find the methods of the identifier path, the rest of the identifiers are
     * methods call or attributes...
     * @param clazz
     * @param startIndex
     * @param list
     * @return
     */
    private List<CompletionItem> getMethods(Class clazz, List<Identifier> list) throws NoSuchFieldException {

        List<CompletionItem> listMethods = new ArrayList<CompletionItem>();

        if (list.size() == 0)
        {
            // list clazz methods...
            Method[] methods = clazz.getMethods();
            for (int i=0; i<methods.length; ++i)
            {
                // We use just the first matching method without looking
                // at the method firm...
                if (Modifier.isPublic(methods[i].getModifiers()) && !methods[i].getReturnType().getName().equals("void"))
                {
                    listMethods.add( CompletionItem.create(methodFirm(methods[i]), "<font color=\"#ababab\">" + getPrintableTypeName(methods[i].getReturnType().getName()), "",CompletionItem.Type.FIELD,0));
                }
            }
            Field[] fields = clazz.getFields();
            for (int i=0; i<fields.length; ++i)
            {

                // We use just the first matching method without looking
                // at the method firm...
                if (Modifier.isPublic(fields[i].getModifiers()))
                {
                    listMethods.add( CompletionItem.create(fields[i].getName(), "<font color=\"#ababab\">" + getPrintableTypeName(fields[i].getType().getName()), "",CompletionItem.Type.FIELD,0));
                }
            }
        }
        else if (list.size() == 1 && list.get(0).getType() == Identifier.UNDEFINED)
        {
            // Try to resolve a method or an attribute...
            // list clazz methods...

            String identifierName = list.get(0).getName();
            Method[] methods = clazz.getMethods();
            for (int i=0; i<methods.length; ++i)
            {
                // We use just the first matching method without looking
                // at the method firm...
                if (methods[i].getName().startsWith(identifierName) &&
                    !methods[i].getReturnType().getName().equals("void") &&
                    Modifier.isPublic(methods[i].getModifiers()))
                {
                    listMethods.add( CompletionItem.create(methodFirm(methods[i]), "<font color=\"#ababab\">" + getPrintableTypeName(methods[i].getReturnType().getName()), "",CompletionItem.Type.FIELD,0));
                }
            }
            Field[] fields = clazz.getFields();
            for (int i=0; i<fields.length; ++i)
            {

                // We use just the first matching method without looking
                // at the method firm...
                if (fields[i].getName().startsWith(identifierName) &&
                    Modifier.isPublic(fields[i].getModifiers()))
                {
                    listMethods.add( CompletionItem.create(fields[i].getName(), "<font color=\"#ababab\">" + getPrintableTypeName(fields[i].getType().getName()), "",CompletionItem.Type.FIELD,1));
                }
            }
        }
        else if (list.get(list.size()-1).getType() == Identifier.METHOD)
        {
            Method[] methods = clazz.getMethods();
            for (int i=0; i<methods.length; ++i)
            {
                // We use just the first matching method without looking
                // at the method firm...
                if (methods[i].getName().equals(list.get(list.size()-1).getName()) &&
                   !methods[i].getReturnType().getName().equals("void") &&
                   Modifier.isPublic(methods[i].getModifiers()))
                {
                    return getMethods(methods[i].getReturnType(), list.subList(0, list.size()-1));
                }
            }
        }
        else
        {
            // assuming it is a field...
            Field f = clazz.getDeclaredField(list.get(list.size()-1).getName());
            if (f == null || !Modifier.isPublic(f.getModifiers())) return null;
            return getMethods(f.getType(), list.subList(1, list.size()));
        }

        return listMethods;
    }



    private ExpChunk previousChunk(ExpChunk current)
    {
        int index = expChunks.indexOf(current);
        if (index > 0)
        {
            ExpChunk chunk = expChunks.get(index-1);
            if (chunk.token.id ().name ().endsWith ("whitespace") ||
                chunk.token.id ().name ().endsWith ("comment"))
            {
                System.out.println("Skipping whitespace...");
                System.out.flush();

                return previousChunk(chunk);
            }
            return chunk;
        }
        return null;
    }



    private List<Identifier> previousIdentifiers(ExpChunk current)
    {
            if (current == null) return null;
            List<Identifier> list = new ArrayList<Identifier>();
            if (current.token.id().name().equals("identifier"))
            {
                Identifier id = new Identifier(tokenString(current)+"", Identifier.ATTRIBUTE);
                list.add(id);

                // This can be an attribute or a class name or a package name
                ExpChunk prev = previousChunk(current);
                if (prev != null && tokenString(prev).equals("."))
                {
                    // this is an attribute or a class or a package name...
                    List<Identifier> li = previousIdentifiers(previousChunk(prev));
                    if (li != null) list.addAll(li);
                    else return null;
                }
                return list;
            }
            else if (isReportObject(current))
            {
                Identifier id = new Identifier(tokenString(current)+"", Identifier.REPORT_OBJECT);
                list.add(id);
                return list;
            }
            else if (tokenString(current).equals(")"))
            {
                // The previous portion is a method call
                // Let's find the matching parenthesis...
                int par = 1;
                ExpChunk prev = previousChunk(current);
                while (par > 0)
                {
                    if (prev == null)
                    {
                        // no previous chunk, return null;
                        return null;
                    }
                    if (tokenString(prev).equals(")")) par ++;
                    if (tokenString(prev).equals("(")) par --;

                    prev = previousChunk(prev);
                }
                // Now we are at the identifier token....
                if (prev == null) return null;
                if (prev.token.id().name().equals("identifier"))
                {
                    list.add(new Identifier(tokenString(prev)+"", Identifier.METHOD));
                    // The method may be prefixed by a . and an identifier, otherwise it is a local method...
                    prev = previousChunk(prev);
                    if (prev != null && tokenString(prev).equals("."))
                    {
                        List<Identifier> li = previousIdentifiers(previousChunk(prev));
                        if (li != null) list.addAll(li);
                        else return null;
                    }
                    return list;
                }
                else if (isReportObject(prev))
                {
                    Identifier id = new Identifier(tokenString(current)+"", Identifier.REPORT_OBJECT);
                    list.add(id);
                    return list;
                }
            }
            return null;
    }

    public boolean isReportObject(ExpChunk chunk)
    {
        return chunk != null &&
               (chunk.token.id().name().equals("object_V") ||
                chunk.token.id().name().equals("object_F") ||
                chunk.token.id().name().equals("object_P")
               );
    }

    public String methodFirm(Method m)
    {
        String method_firm = m.getName() + "(";
        Class[] params = m.getParameterTypes();
        int j=0;
        for (j=0; j<params.length; ++j)
        {

            if (j > 0) method_firm +=",";
            method_firm +=  getPrintableTypeName( params[j].getName() );
        }
        //if (j>0) method_firm+=" ";
        method_firm += ")";

        //String rname = m.getReturnType().getName();
        //method_firm += "<font color=\"#999999'\">(<i>" + getPrintableTypeName( rname) + "</i>)</font>";
        return method_firm;
    }

    public String getPrintableTypeName( String type )
    {
            if (type == null) return "void";

            if (type.endsWith(";")) type = type.substring(0,type.length()-1);

            while (type.startsWith("["))
            {
                type = type.substring(1) + "[]";
                if (type.startsWith("[")) continue;
                if (type.startsWith("L")) type = type.substring(1);
                if (type.startsWith("Z")) type = "boolean" + type.substring(1);
                if (type.startsWith("B")) type = "byte" + type.substring(1);
                if (type.startsWith("C")) type = "char" + type.substring(1);
                if (type.startsWith("D")) type = "double" + type.substring(1);
                if (type.startsWith("F")) type = "float" + type.substring(1);
                if (type.startsWith("I")) type = "int" + type.substring(1);
                if (type.startsWith("J")) type = "long" + type.substring(1);
                if (type.startsWith("S")) type = "short" + type.substring(1);
            }

            if (type.startsWith("java.lang."))
            {
                type = type.substring("java.lang.".length());
                if (type.indexOf(".") > 0)
                {
                    type = "java.lang." + type;
                }
            }
            return type;
    }


    public List<CompletionItem> getExpressionContextFields()
    {

        List<CompletionItem> list = new ArrayList<CompletionItem>();
        ExpressionContext ec = ExpressionContext.getGlobalContext();
        if (ec == null) return list;
        for (JRDesignDataset dataset : ec.getDatasets())
        {
            JRField[] fields = dataset.getFields();
            for (int i=0; i<fields.length; ++i)
            {
                list.add(CompletionItem.create("$F{" + fields[i].getName() + "}", "<font color=\"#ababab\">" + getPrintableTypeName(fields[i].getValueClassName()), null,Type.CONSTANT,0));
            }
        }

        
        return list;
    }

    public List<CompletionItem> getExpressionContextVariables()
    {

        List<CompletionItem> list = new ArrayList<CompletionItem>();
        ExpressionContext ec = ExpressionContext.getGlobalContext();
        if (ec == null) return list;
        for (JRDesignDataset dataset : ec.getDatasets())
        {
            JRVariable[] variables = dataset.getVariables();
            for (int i=0; i<variables.length; ++i)
            {
                list.add(CompletionItem.create("$V{" + variables[i].getName() + "}", "<font color=\"#ababab\">" +getPrintableTypeName(variables[i].getValueClassName()), null,Type.CONSTANT,0));
            }
        }
        for (JRDesignCrosstab crosstab : ec.getCrosstabs())
        {
            addCrosstabVariables(crosstab, list);
        }

        return list;
    }

    public List<CompletionItem> getExpressionContextParameters()
    {

        List<CompletionItem> list = new ArrayList<CompletionItem>();
        ExpressionContext ec = ExpressionContext.getGlobalContext();
        if (ec == null) return list;
        for (JRDesignDataset dataset : ec.getDatasets())
        {
            JRParameter[] parameters = dataset.getParameters();
            for (int i=0; i<parameters.length; ++i)
            {
                list.add(CompletionItem.create("$P{" + parameters[i].getName() + "}", "<font color=\"#ababab\">" + getPrintableTypeName(parameters[i].getValueClassName()), null,Type.CONSTANT,0));
            }
        }
        for (JRDesignCrosstab crosstab : ec.getCrosstabs())
        {
            addCrosstabParameters(crosstab, list);
        }

        return list;
    }

    private void addCrosstabParameters(JRDesignCrosstab crosstab, List<CompletionItem> list)
    {
            JRCrosstabParameter[] params = crosstab.getParameters();
            for (int i=0; i<params.length; ++i)
            {
                list.add(CompletionItem.create("$P{" + params[i].getName() + "}", "<font color=\"#ababab\">" + getPrintableTypeName(params[i].getValueClassName()), null,Type.CONSTANT,0));
            }
    }

    private void addCrosstabVariables(JRDesignCrosstab crosstab, List<CompletionItem> list)
    {

            List rowGroups = crosstab.getRowGroupsList();
            List columnGroups = crosstab.getColumnGroupsList();

            Iterator measures = crosstab.getMesuresList().iterator();
            while (measures.hasNext())
            {
                JRDesignCrosstabMeasure measure = (JRDesignCrosstabMeasure)measures.next();
                list.add(CompletionItem.create("$V{" + measure.getVariable().getName() + "}", "<font color=\"#ababab\">" + getPrintableTypeName(measure.getVariable().getValueClassName()), null,Type.CONSTANT,0));
                

                for (int i=0; i<rowGroups.size(); ++i)
                {
                    JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                    CrosstabTotalVariable var = new CrosstabTotalVariable(measure, rowGroup, null);
                    list.add(CompletionItem.create(var.getExpression(), "<font color=\"#ababab\">" + getPrintableTypeName(var.getClassType()), null,Type.CONSTANT,0));


                    for (int j=0; j<columnGroups.size(); ++j)
                    {
                        JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(j);
                        if (j==0)
                        {
                            CrosstabTotalVariable var2 = new CrosstabTotalVariable(measure, null, columnGroup);
                            list.add(CompletionItem.create(var2.getExpression(), "<font color=\"#ababab\">" + getPrintableTypeName(var2.getClassType()), null,Type.CONSTANT,0));
                            
                        }

                        CrosstabTotalVariable var3 = new CrosstabTotalVariable(measure, rowGroup, columnGroup);
                        list.add(CompletionItem.create(var3.getExpression(), "<font color=\"#ababab\">" + getPrintableTypeName(var3.getClassType()), null,Type.CONSTANT,0));

                    }
                }
            }

            for (int i=0; i<rowGroups.size(); ++i)
            {
                JRDesignCrosstabRowGroup rowGroup = (JRDesignCrosstabRowGroup)rowGroups.get(i);
                list.add(CompletionItem.create("$V{" + rowGroup.getVariable().getName() + "}", "<font color=\"#ababab\">" + getPrintableTypeName(rowGroup.getVariable().getValueClassName()), null,Type.CONSTANT,0));
            }

            for (int i=0; i<columnGroups.size(); ++i)
            {
                JRDesignCrosstabColumnGroup columnGroup = (JRDesignCrosstabColumnGroup)columnGroups.get(i);
                list.add(CompletionItem.create("$V{" + columnGroup.getVariable().getName() + "}", "<font color=\"#ababab\">" + getPrintableTypeName(columnGroup.getVariable().getValueClassName()), null,Type.CONSTANT,0));
            }
    }


}