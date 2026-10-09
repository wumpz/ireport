/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2023 Cloud Software Group, Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Contributors:
 * Peter Severin - peter_p_s@users.sourceforge.net
 */
/* Modified in this fork: Java 17 target, classic Groovy calls and standard evaluator identity. */
package com.jaspersoft.ireport.designer.compiler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import net.sf.jasperreports.compilers.JRGroovyCompiler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.CompiledClasses;
import net.sf.jasperreports.engine.design.JRCompilationUnit;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.tools.GroovyClass;

/** Compiled reports retain the standard JasperReports evaluator for ERP portability. */
public final class PortableGroovyCompiler extends JRGroovyCompiler {
    public PortableGroovyCompiler(JasperReportsContext context) { super(context); }

    @Override protected String getCompilerClass() {
        return JRGroovyCompiler.class.getName();
    }

    @Override protected String compileUnits(JRCompilationUnit[] units, String classpath, File tempDir) throws JRException {
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setSourceEncoding(StandardCharsets.UTF_8.name());
        configuration.setTargetBytecode("17");
        configuration.getOptimizationOptions().put("indy", false);
        if (reportClassFilter.isFilteringEnabled()) {
            try {
                // Jasper's sandbox dependency is optional; load it only when requested.
                CompilationCustomizer filter = (CompilationCustomizer) Class.forName(
                    "net.sf.jasperreports.compilers.GroovyClassFilterTransformer").getConstructor().newInstance();
                configuration.addCompilationCustomizers(filter);
            } catch (ReflectiveOperationException | LinkageError ex) {
                throw new JRException("Groovy class filtering requires the JasperReports sandbox dependency", ex);
            }
        }
        CompilationUnit compilation = new CompilationUnit(configuration);
        for (JRCompilationUnit unit : units) {
            compilation.addSource("calculator_" + unit.getCompileName(),
                new ByteArrayInputStream(unit.getSourceCode().getBytes(StandardCharsets.UTF_8)));
        }
        try {
            compilation.compile(Phases.CLASS_GENERATION);
        } catch (CompilationFailedException ex) {
            throw new JRException(EXCEPTION_MESSAGE_KEY_COMPILING_EXPRESSIONS_CLASS_FILE, new Object[]{ex.toString()}, ex);
        }
        var classes = compilation.getClasses();
        if (classes.size() < units.length) throw new JRException(EXCEPTION_MESSAGE_KEY_TOO_FEW_CLASSES_GENERATED, (Object[])null);
        Map<String, byte[]> bytes = classes.stream().collect(Collectors.toMap(GroovyClass::getName, GroovyClass::getBytes));
        CompiledClasses data = new CompiledClasses(bytes);
        for (JRCompilationUnit unit : units) unit.setCompileData(data);
        return null;
    }
}
