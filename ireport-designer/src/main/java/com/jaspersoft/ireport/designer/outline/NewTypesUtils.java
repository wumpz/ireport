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
package com.jaspersoft.ireport.designer.outline;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.JrxmlVisualView;
import com.jaspersoft.ireport.designer.ModelUtils;
import com.jaspersoft.ireport.designer.palette.actions.CreateTextFieldAction;
import com.jaspersoft.ireport.designer.undo.AddStyleUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabParameter;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JRDesignScriptlet;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;
import net.sf.jasperreports.engine.util.Pair;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.datatransfer.NewType;

/**
 *
 * @author gtoffoli
 */
public class NewTypesUtils {

    public static final int PARAMETER = 1;
    public static final int FIELD = 2;
    public static final int VARIABLE = 3;
    public static final int STYLE = 4;
    public static final int CROSSTAB_PARAMETER = 5;
    public static final int CROSSTAB_MEASURE = 6;
    public static final int CROSSTAB_ROW_GROUP = 7;
    public static final int CROSSTAB_COLUMN_GROUP = 8;
    public static final int CONDITIONAL_STYLE = 9;
    public static final int DATASET_GROUP = 10;
    public static final int REPORT_TEMPLATE = 11;
    public static final int SCRIPTLET = 12;
    
    
    private static final NewType[] NO_NEW_TYPES = {  };
    
    public static NewType[] getNewType(Node node, int... types)
    {
        
        Arrays.sort(types);
        
        List<NewType> newTypes = new ArrayList<NewType>();
 
        if ( Arrays.binarySearch( types, PARAMETER) >= 0)
        {
            newTypes.add(new NewObjectType(PARAMETER, node));
        }
        if ( Arrays.binarySearch( types, FIELD) >= 0)
        {
            newTypes.add(new NewObjectType(FIELD, node));
        }
        if ( Arrays.binarySearch( types, VARIABLE) >= 0)
        {
            newTypes.add(new NewObjectType(VARIABLE, node));
        }
        if ( Arrays.binarySearch( types, STYLE) >= 0)
        {
            newTypes.add(new NewObjectType(STYLE, node));
        }
        if ( Arrays.binarySearch( types, CROSSTAB_PARAMETER) >= 0)
        {
            newTypes.add(new NewObjectType(CROSSTAB_PARAMETER, node));
        }
        if ( Arrays.binarySearch( types, CROSSTAB_MEASURE) >= 0)
        {
            newTypes.add(new NewObjectType(CROSSTAB_MEASURE, node));
        }
        if ( Arrays.binarySearch( types, CROSSTAB_ROW_GROUP) >= 0)
        {
            newTypes.add(new NewObjectType(CROSSTAB_ROW_GROUP, node));
        }
        if ( Arrays.binarySearch( types, CROSSTAB_COLUMN_GROUP) >= 0)
        {
            newTypes.add(new NewObjectType(CROSSTAB_COLUMN_GROUP, node));
        }
        if ( Arrays.binarySearch( types, CONDITIONAL_STYLE) >= 0)
        {
            newTypes.add(new NewObjectType(CONDITIONAL_STYLE, node));
        }
        if ( Arrays.binarySearch( types, DATASET_GROUP) >= 0)
        {
            newTypes.add(new NewObjectType(DATASET_GROUP, node));
        }
        if ( Arrays.binarySearch( types, REPORT_TEMPLATE) >= 0)
        {
            newTypes.add(new NewObjectType(REPORT_TEMPLATE, node));
        }
        if ( Arrays.binarySearch( types, SCRIPTLET) >= 0)
        {
            newTypes.add(new NewObjectType(SCRIPTLET, node));
        }
        

        return newTypes.toArray(new NewType[newTypes.size()]);
    }
}

class NewObjectType extends NewType {

    Node parentNode = null;  
    int type = -1;
    public NewObjectType(int type, Node parentNode)
    {
        this.parentNode = parentNode;
        this.type = type;
    }
    
    public void create() throws IOException {
        JasperDesign jd = parentNode.getLookup().lookup(JasperDesign.class);
        if (jd == null) return;

        JRDesignCrosstab crosstab = null;
        
        JRDesignDataset dataset = parentNode.getLookup().lookup(JRDesignDataset.class);
        if (dataset == null)
        {
            crosstab = parentNode.getLookup().lookup(JRDesignCrosstab.class);
            if (crosstab != null) dataset = ModelUtils.getElementDataset(crosstab, jd);
        }    
            
        if (dataset == null) dataset = jd.getMainDesignDataset();
        if (dataset == null) return;
        
        Object obj = null;
        
        switch (type)
        {
            case NewTypesUtils.PARAMETER:
            {
                try {
                    JRDesignParameter p = new JRDesignParameter();
                    String baseName = "parameter"; // NOI18N
                    String new_name = baseName;

                    List list = dataset.getParametersList();
                    boolean found = true;
                    for (int j = 1; found; j++) {
                        found = false;
                        new_name = baseName + j;
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignParameter tmpP = (JRDesignParameter) list.get(i);
                            if (tmpP.getName().equals(new_name)) {
                                found = true;
                            }
                        }
                    }

                    p.setName(new_name);
                    obj = p;
                    dataset.addParameter(p);
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.FIELD:
            {
                try {
                    JRDesignField f = new JRDesignField();
                    String baseName = "field"; // NOI18N
                    String new_name = baseName;

                    List list = dataset.getFieldsList();
                    boolean found = true;
                    for (int j = 1; found; j++) {
                        found = false;
                        new_name = baseName + j;
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignField tmpP = (JRDesignField) list.get(i);
                            if (tmpP.getName().equals(new_name)) {
                                found = true;
                            }
                        }
                    }
                    f.setName(new_name);
                    obj = f;
                    dataset.addField(f);
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            } 
            case NewTypesUtils.VARIABLE:
            {
                try {
                    JRDesignVariable v = new JRDesignVariable();
                    String baseName = "variable"; // NOI18N
                    String new_name = baseName;

                    List list = dataset.getVariablesList();
                    boolean found = true;
                    for (int j = 1; found; j++) {
                        found = false;
                        new_name = baseName + j;
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignVariable tmpP = (JRDesignVariable) list.get(i);
                            if (tmpP.getName().equals(new_name)) {
                                found = true;
                            }
                        }
                    }
                    v.setName(new_name);
                    obj = v;
                    dataset.addVariable(v);
                
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.DATASET_GROUP:
            {
                try {
                    JRDesignGroup v = new JRDesignGroup();
                    String baseName = "group"; // NOI18N
                    String new_name = baseName;

                    List list = dataset.getGroupsList();
                    boolean found = true;
                    for (int j = 1; found; j++) {
                        found = false;
                        new_name = baseName + j;
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignGroup tmpP = (JRDesignGroup) list.get(i);
                            if (tmpP.getName().equals(new_name)) {
                                found = true;
                            }
                        }
                    }
                    v.setName(new_name);
                    obj = v;
                    dataset.addGroup(v);

                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.STYLE:
            {
                try {
                    JRDesignStyle v = new JRDesignStyle();
                    String baseName = "style"; // NOI18N
                    String new_name = baseName;

                    List list = jd.getStylesList();
                    boolean found = true;
                    for (int j = 1; found; j++) {
                        found = false;
                        new_name = baseName + j;
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignStyle tmpP = (JRDesignStyle) list.get(i);
                            if (tmpP.getName().equals(new_name)) {
                                found = true;
                            }
                        }
                    }
                    v.setName(new_name);
                    obj = v;
                    jd.addStyle(v);
                    
                    AddStyleUndoableEdit undo = new AddStyleUndoableEdit(v, jd); //newIndex
                    IReportManager.getInstance().addUndoableEdit(undo);
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.CONDITIONAL_STYLE:
            {
                JRDesignStyle parentStyle = parentNode.getLookup().lookup(JRDesignStyle.class);
                if (parentStyle == null)
                {
                    return;
                }    
               
                JRDesignConditionalStyle v = new JRDesignConditionalStyle();
                parentStyle.addConditionalStyle(v);

                //AddconditionalStyleUndoableEdit undo = new AddconditionalStyleUndoableEdit(v, jd); //newIndex
                //IReportManager.getInstance().addUndoableEdit(undo);

                
                break;
            } 
            case NewTypesUtils.CROSSTAB_PARAMETER:
            {
                if (crosstab == null) return;
                try {
                    JRDesignCrosstabParameter p = new JRDesignCrosstabParameter();
                    String baseName = "parameter"; // NOI18N
                    String new_name = baseName;

                    List list = crosstab.getParametersList();
                    boolean found = true;
                    for (int j = 1; found; j++) {
                        found = false;
                        new_name = baseName + j;
                        for (int i = 0; i < list.size(); ++i) {
                            JRDesignParameter tmpP = (JRDesignParameter) list.get(i);
                            if (tmpP.getName().equals(new_name)) {
                                found = true;
                            }
                        }
                    }

                    p.setName(new_name);
                    obj = p;
                    crosstab.addParameter(p);
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.CROSSTAB_MEASURE:
            {
                if (crosstab == null) return;
                try {
                    JRDesignCrosstabMeasure p = new JRDesignCrosstabMeasure();
                    String baseName = "measure"; // NOI18N
                    String new_name = baseName;

                    for (int j = 1; ; j++) {
                        new_name = baseName + j;
                        if (ModelUtils.isValidNewCrosstabObjectName(crosstab, new_name))
                        {
                            break;
                        }
                    }

                    
                    p.setName(new_name);
                    
                    JRDesignExpression exp = Misc.createExpression("java.lang.Integer", "\"\""); // NOI18N
                    p.setValueExpression(exp);
                    p.setValueClassName("java.lang.Integer"); // NOI18N
                    obj = p;
                    crosstab.addMeasure(p);
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.CROSSTAB_ROW_GROUP:
            {
                if (crosstab == null) return;
                try {
                    JRDesignCrosstabRowGroup group = new JRDesignCrosstabRowGroup();
                    String baseName = "group";
                    String new_name = baseName;

                    for (int j = 1; ; j++) {
                        new_name = baseName + j;
                        if (ModelUtils.isValidNewCrosstabObjectName(crosstab, new_name))
                        {
                            break;
                        }
                    }

                    
                    group.setName(new_name);
                    group.setWidth(100);
                    
                    JRDesignExpression exp = Misc.createExpression("java.lang.String", "");
                    JRDesignCrosstabBucket bucket = new JRDesignCrosstabBucket();
                    bucket.setExpression(exp);
                    group.setBucket(bucket);
                    JRDesignCellContents headerCell = new JRDesignCellContents();
                    group.setHeader(headerCell);
                    // the width is the with of the current base cell...
                    JRDesignCrosstabCell baseCell = (JRDesignCrosstabCell)crosstab.getCellsMap().get(new Pair(null,null));
                    int baseHeight = (baseCell.getHeight() != null) ? baseCell.getHeight() : ( (baseCell.getContents() != null) ? baseCell.getContents().getHeight() : 30);
                    
                    headerCell.addElement( 
                            createField(jd, Misc.createExpression( "java.lang.String", "$V{" + new_name + "}"), 100, baseHeight, "Crosstab Data Text")); // NOI18N
                
                    group.setTotalHeader(new JRDesignCellContents());
                    
                    obj = group;
                    
                    
                    crosstab.addRowGroup(group);
                    
                    // I need to add the extra cells...
                    JRCrosstabColumnGroup[] columns = crosstab.getColumnGroups();
                    JRDesignCrosstabCell dT =  new JRDesignCrosstabCell();
                    dT.setRowTotalGroup(new_name);
                    crosstab.addCell(dT);
                    // for each column, we need to add the total...
                    for (int i=0; i<columns.length; ++i)
                    {
                        JRDesignCrosstabCell cell =  new JRDesignCrosstabCell();
                        cell.setRowTotalGroup(new_name);
                        cell.setColumnTotalGroup(columns[i].getName());
                        crosstab.addCell(cell);
                        
                        // Add some cells...
                        
                    }
                   
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.CROSSTAB_COLUMN_GROUP:
            {
                if (crosstab == null) return;
                try {
                    JRDesignCrosstabColumnGroup group = new JRDesignCrosstabColumnGroup();
                    String baseName = "group";
                    String new_name = baseName;

                    for (int j = 1; ; j++) {
                        
                        new_name = baseName + j;
                        
                        if (ModelUtils.isValidNewCrosstabObjectName(crosstab, new_name))
                        {
                            break;
                        }
                    }

                    
                    group.setName(new_name);
                    group.setHeight(30);
                    
                    JRDesignExpression exp = Misc.createExpression("java.lang.String", ""); // NOI18N
                    JRDesignCrosstabBucket bucket = new JRDesignCrosstabBucket();
                    bucket.setExpression(exp);
                    group.setBucket(bucket);
                    JRDesignCellContents headerCell = new JRDesignCellContents();
                    group.setHeader(headerCell);
                    // the width is the with of the current base cell...
                    JRDesignCrosstabCell baseCell = (JRDesignCrosstabCell)crosstab.getCellsMap().get(new Pair(null,null));
                    int baseWidth = (baseCell.getWidth() != null) ? baseCell.getWidth() : ( (baseCell.getContents() != null) ? baseCell.getContents().getWidth() : 50);
                    
                    headerCell.addElement( 
                            createField(jd, Misc.createExpression( "java.lang.String", "$V{" + new_name + "}"), baseWidth, 30, "Crosstab Data Text")); // NOI18N
                
                    group.setTotalHeader(new JRDesignCellContents());
                    
                    obj = group;
                    
                    crosstab.addColumnGroup(group);
                    
                    // I need to add the extra cells...
                    JRCrosstabRowGroup[] rows = crosstab.getRowGroups();
                    JRDesignCrosstabCell dT =  new JRDesignCrosstabCell();
                    dT.setColumnTotalGroup(new_name);
                    crosstab.addCell(dT);
                    // for each column, we need to add the total...
                    for (int i=0; i<rows.length; ++i)
                    {
                        JRDesignCrosstabCell cell =  new JRDesignCrosstabCell();
                        cell.setColumnTotalGroup(new_name);
                        cell.setRowTotalGroup(rows[i].getName());
                        crosstab.addCell(cell);
                    }
                    
                    //TODO there are not totals by default? We should ask for...
                    
                    
                    // at this point the bucket is null...
                    
                    
                    
                    
                    
                    
                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
            case NewTypesUtils.REPORT_TEMPLATE:
            {
                
                    JRDesignReportTemplate v = new JRDesignReportTemplate();

                    File parent = new File(IReportManager.getInstance().getCurrentDirectory());
                    // Try to figure it out the current directory of the report...
                    if (IReportManager.getInstance().getActiveVisualView() != null)
                    {
                        JrxmlVisualView view = IReportManager.getInstance().getActiveVisualView();
                        FileObject fobj = view.getEditorSupport().getDataObject().getPrimaryFile();
                        File f = FileUtil.toFile(fobj);
                        if (f != null && f.getParentFile().exists())
                        {
                            parent = f.getParentFile();
                        }
                    }

                    final javax.swing.JFileChooser jfc = new javax.swing.JFileChooser( parent  );
                    jfc.setDialogTitle("Select an Report Template (JRTX) file....");
                    jfc.setFileFilter( new javax.swing.filechooser.FileFilter() {
                        public boolean accept(java.io.File file) {
                            String filename = file.getName();
                            return (filename.endsWith(".jrtx") ||
                                    file.isDirectory()) ;
                        }
                        public String getDescription() {
                            return "JasperReports Tempate *.jrtx|*.xml";
                        }
                    });
        
                    jfc.setMultiSelectionEnabled(false);
                    jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
                    if  (jfc.showOpenDialog( null) == javax.swing.JOptionPane.OK_OPTION)
                    {
                            IReportManager.getInstance().setCurrentDirectory(jfc.getSelectedFile().getParent(), true);
                            
                            v.setSourceExpression( Misc.createExpression("java.lang.String", "\""+ Misc.string_replace("\\\\","\\",jfc.getSelectedFile().getPath() +"\"")));
                            IReportManager.getInstance().setCurrentDirectory(jfc.getSelectedFile().getParent(), true);
                    }
                    else
                    {
                        v.setSourceExpression(Misc.createExpression("java.lang.String", ""));
                    }
                    jd.addTemplate(v);

                    //AddStyleUndoableEdit undo = new AddStyleUndoableEdit(v, jd); //newIndex
                    //IReportManager.getInstance().addUndoableEdit(undo);

                
                break;
            }
            case NewTypesUtils.SCRIPTLET:
            {
                try {
                    JRDesignScriptlet p = new JRDesignScriptlet();
                    p.setValueClassName("net.sf.jasperreports.engine.JRDefaultScriptlet");
                    String baseName = "scriptlet"; // NOI18N
                    String new_name = baseName;

                    List list = dataset.getScriptletsList();

                    //if (list.size() == 0)
                    //{
                    //    new_name = "REPORT";
                    //    //We need a trick here: remove the REPORT_SCRIPTLET parameter...
                    //    dataset.removeParameter("REPORT_SCRIPTLET");
                    //}
                    //else

                    //{
                        boolean found = true;
                        for (int j = 1; found; j++) {
                            found = false;
                            new_name = baseName + j;
                            for (int i = 0; i < list.size(); ++i) {
                                JRDesignScriptlet tmpP = (JRDesignScriptlet) list.get(i);
                                if (tmpP.getName().equals(new_name)) {
                                    found = true;
                                }
                            }
                        }
                    //}
                    p.setName(new_name);
                    
                    obj = p;
                    dataset.addScriptlet(p);

                } catch (JRException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            }
        }
        
        if (obj != null)
        {
            IReportManager.getInstance().setSelectedObject(obj);
        }
    }

    @Override
    public String getName() {
        switch (type)
        {
            case NewTypesUtils.PARAMETER: return I18n.getString("NewType.Parameter");
            case NewTypesUtils.FIELD: return I18n.getString("NewType.Field");
            case NewTypesUtils.VARIABLE: return I18n.getString("NewType.Variable");
            case NewTypesUtils.STYLE: return I18n.getString("NewType.Style");
            case NewTypesUtils.CONDITIONAL_STYLE: return I18n.getString("NewType.ConditionalStyle");
            case NewTypesUtils.CROSSTAB_PARAMETER: return I18n.getString("NewType.CrosstabParameter");
            case NewTypesUtils.CROSSTAB_MEASURE: return I18n.getString("NewType.Measure");
            case NewTypesUtils.CROSSTAB_ROW_GROUP: return I18n.getString("NewType.RowGroup");
            case NewTypesUtils.CROSSTAB_COLUMN_GROUP: return I18n.getString("NewType.ColumnGroup");
            case NewTypesUtils.DATASET_GROUP: return I18n.getString("NewType.DatasetGroup");
            case NewTypesUtils.REPORT_TEMPLATE: return I18n.getString("Style reference");
            case NewTypesUtils.SCRIPTLET: return I18n.getString("Scriptlet");
        }
        return super.getName();
    }
    
    
    
    
    
    private JRDesignStaticText createLabel(JasperDesign jd, String text, int w, int h, String styleName)
    {
        JRDesignStaticText element = new JRDesignStaticText();
        element.setX(0);
        element.setY(0);
        element.setWidth(w);
        element.setHeight(h);
        element.setText(text);
        element.setHorizontalAlignment( HorizontalAlignEnum.CENTER );
        element.setVerticalAlignment( VerticalAlignEnum.MIDDLE);
        
        if (styleName != null && jd.getStylesMap().containsKey(styleName))
        {
            element.setStyle( (JRStyle) jd.getStylesMap().get(styleName) );
        }
        return element;
    }
    
    private JRDesignTextField createField(JasperDesign jd, JRDesignExpression exp, int w, int h, String styleName)
    {
        JRDesignTextField element = new JRDesignTextField();
        element.setX(0);
        element.setY(0);
        element.setWidth(w);
        element.setHeight(h);
        
        if (styleName != null && jd.getStylesMap().containsKey(styleName))
        {
            element.setStyle( (JRStyle) jd.getStylesMap().get(styleName) );
        }
        
        try {
            CreateTextFieldAction.setMatchingClassExpression(exp,exp.getValueClassName(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        element.setExpression(exp);
        return element;
    }
    
    
}


