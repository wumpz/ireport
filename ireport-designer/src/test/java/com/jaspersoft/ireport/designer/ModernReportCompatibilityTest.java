package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.designer.sheet.properties.HorizontalAlignmentProperty;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import net.sf.jasperreports.export.*;
import org.junit.Test;
import static org.junit.Assert.*;

/** Regression checks for modern JRXML; all outputs stay inside target/. */
public class ModernReportCompatibilityTest {
    private JasperDesign load(String language) throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\""
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + " xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\""
            + " name=\"modern-regression\" language=\"" + language + "\" pageWidth=\"226\" pageHeight=\"842\""
            + " columnWidth=\"206\" leftMargin=\"10\" rightMargin=\"10\" topMargin=\"10\" bottomMargin=\"10\" isIgnorePagination=\"true\">"
            + "<detail><band height=\"30\"><textField textAdjust=\"StretchHeight\">"
            + "<reportElement key=\"sale-total\" x=\"0\" y=\"0\" width=\"206\" height=\"20\" stretchType=\"ContainerHeight\"/>"
            + "<textElement textAlignment=\"Right\"><font size=\"10.5\"/></textElement>"
            + "<textFieldExpression><![CDATA[\"Venda: \" + $V{REPORT_COUNT}]]></textFieldExpression>"
            + "</textField></band></detail></jasperReport>";
        return JRXmlLoader.load(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Test public void optionsChartThemesLoadWithModernFonts() {
        java.util.Set<String> names = new java.util.HashSet<>();
        for (net.sf.jasperreports.charts.ChartThemeBundle bundle :
                net.sf.jasperreports.extensions.ExtensionsEnvironment.getExtensionsRegistry()
                    .getExtensions(net.sf.jasperreports.charts.ChartThemeBundle.class)) {
            names.addAll(java.util.Arrays.asList(bundle.getChartThemeNames()));
        }
        assertTrue("Options must load built-in chart themes", names.contains("eye.candy.sixties"));
    }

    @Test public void modernPropertiesSurviveRoundTrip() throws Exception {
        JasperDesign original = load("java");
        Files.createDirectories(Path.of("target", "compatibility-results"));
        Files.writeString(Path.of("target", "compatibility-results", "modern-roundtrip.jrxml"), JRXmlWriter.writeReport(original, "UTF-8"));
        String xml = com.jaspersoft.ireport.designer.compatibility.JRXmlWriterHelper.writeReport(original, "UTF-8");
        JasperDesign saved = JRXmlLoader.load(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        JRTextField field = (JRTextField)saved.getDetailSection().getBands()[0].getElements()[0];
        assertEquals(TextAdjustEnum.STRETCH_HEIGHT, field.getTextAdjust());
        assertEquals(StretchTypeEnum.CONTAINER_HEIGHT, field.getStretchTypeValue());
        assertEquals(10.5f, field.getFontsize(), 0.001f);
        assertEquals(HorizontalTextAlignEnum.RIGHT, field.getHorizontalTextAlign());
        assertTrue(saved.isIgnorePagination());
        assertNull(saved.getPageFooter());
    }

    @Test public void javaReportCompilesFillsAndExports() throws Exception {
        JasperReport report = JasperCompileManager.compileReport(load("java"));
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(3));
        assertEquals(1, print.getPages().size());
        byte[] pdf = JasperExportManager.exportReportToPdf(print);
        assertTrue(pdf.length > 100);
        ByteArrayOutputStream xls = new ByteArrayOutputStream();
        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xls));
        exporter.exportReport();
        assertTrue(xls.size() > 100);
        Path output = Path.of("target", "compatibility-results");
        Files.createDirectories(output);
        net.sf.jasperreports.engine.util.JRSaver.saveObject(report, output.resolve("modern-java.jasper").toFile());
        Files.write(output.resolve("modern-java.pdf"), pdf);
        Files.write(output.resolve("modern-java.xls"), xls.toByteArray());
    }

    @Test public void groovyReportCompilesAndFills() throws Exception {
        JasperReport report = JasperCompileManager.compileReport(load("groovy"));
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(1));
        assertEquals(1, print.getPages().size());
    }

    @Test public void pluginGroovyCompilerProducesPortableReports() throws Exception {
        String target = System.getProperty("groovy.target.bytecode");
        String indy = System.getProperty("groovy.target.indy");
        try {
            System.setProperty("groovy.target.bytecode", "25");
            System.setProperty("groovy.target.indy", "true");
            JasperReport report = new com.jaspersoft.ireport.designer.compiler.PortableGroovyCompiler(
                new IRLocalJasperReportsContext()).compileReport(load("groovy"));
            assertEquals("net.sf.jasperreports.compilers.JRGroovyCompiler", report.getCompilerClass());
            assertEquals("25", System.getProperty("groovy.target.bytecode"));
            assertEquals("true", System.getProperty("groovy.target.indy"));
            assertEquals(1, JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(3)).getPages().size());
            Path output = Path.of("target", "compatibility-results");
            Files.createDirectories(output);
            net.sf.jasperreports.engine.util.JRSaver.saveObject(report, output.resolve("modern-plugin-groovy.jasper").toFile());
        } finally {
            if (target == null) System.clearProperty("groovy.target.bytecode"); else System.setProperty("groovy.target.bytecode", target);
            if (indy == null) System.clearProperty("groovy.target.indy"); else System.setProperty("groovy.target.indy", indy);
        }
    }

    @Test public void compiledModernReportConvertsBackToJrxml() throws Exception {
        JasperReport report = JasperCompileManager.compileReport(load("java"));
        ByteArrayOutputStream binary = new ByteArrayOutputStream();
        net.sf.jasperreports.engine.util.JRSaver.saveObject(report, binary);
        JasperReport restored = (JasperReport) net.sf.jasperreports.engine.util.JRLoader.loadObject(new ByteArrayInputStream(binary.toByteArray()));
        String xml = JRXmlWriter.writeReport(restored, "UTF-8");
        JasperDesign converted = JRXmlLoader.load(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        JRTextField field = (JRTextField) converted.getDetailSection().getBands()[0].getElements()[0];
        assertEquals(TextAdjustEnum.STRETCH_HEIGHT, field.getTextAdjust());
        assertEquals(10.5f, field.getFontsize(), 0.001f);
        assertTrue(converted.isIgnorePagination());
    }

    @Test public void alignmentEditorHandlesImagesAndTextSeparately() {
        JRDesignImage image = new JRDesignImage(null);
        HorizontalAlignmentProperty imageProperty = new HorizontalAlignmentProperty(image);
        imageProperty.setPropertyValue(HorizontalImageAlignEnum.CENTER);
        assertEquals(HorizontalImageAlignEnum.CENTER, imageProperty.getOwnPropertyValue());
        assertEquals(3, imageProperty.getTagList().size());
        JRDesignTextField text = new JRDesignTextField();
        HorizontalAlignmentProperty textProperty = new HorizontalAlignmentProperty(text);
        textProperty.setPropertyValue(HorizontalTextAlignEnum.JUSTIFIED);
        assertEquals(HorizontalTextAlignEnum.JUSTIFIED, textProperty.getOwnPropertyValue());
        assertEquals(4, textProperty.getTagList().size());
    }

    @Test public void fractionalFontUndoRestoresInheritedValue() {
        JRDesignTextField text = new JRDesignTextField();
        text.setFontSize(10.5f);
        ObjectPropertyUndoableEdit edit = new ObjectPropertyUndoableEdit(text, "FontSize", Float.class, null, 10.5f);
        edit.undo();
        assertNull(text.getOwnFontsize());
        edit.redo();
        assertEquals(10.5f, text.getOwnFontsize(), 0.001f);
    }

}
