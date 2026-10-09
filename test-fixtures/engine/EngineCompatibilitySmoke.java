import java.nio.file.*;
import java.util.HashMap;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/** Runs without any NetBeans classes, including on the ERP's Java 17. */
public class EngineCompatibilitySmoke {
    public static void main(String[] args) throws Exception {
        Path fixtures = Path.of(args[0]);
        Path pluginGroovy = fixtures.resolve("modern-plugin-groovy.jasper");
        if (Files.exists(pluginGroovy)) verify((JasperReport) JRLoader.loadObject(pluginGroovy.toFile()), "plugin-groovy-java17");
        Path groovyTransfer = fixtures.resolve("modern-groovy-transfer.jasper");
        if (Files.exists(groovyTransfer)) verify((JasperReport) JRLoader.loadObject(groovyTransfer.toFile()), "transferred-groovy");
        JasperReport transferred = (JasperReport) JRLoader.loadObject(fixtures.resolve("modern-java.jasper").toFile());
        verify(transferred, "transferred-java");
        var design = JRXmlLoader.load(fixtures.resolve("modern-roundtrip.jrxml").toFile());
        verify(JasperCompileManager.compileReport(design), "local-java");
        design.setLanguage("groovy");
        JasperReport groovy = JasperCompileManager.compileReport(design);
        verify(groovy, "local-groovy");
        if (Boolean.getBoolean("ireport.engine.saveGroovy")) net.sf.jasperreports.engine.util.JRSaver.saveObject(groovy, groovyTransfer.toFile());
        System.out.println("PASS Java " + System.getProperty("java.version"));
    }
    private static void verify(JasperReport report, String label) throws Exception {
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(3));
        if (print.getPages().size() != 1) throw new AssertionError(label + ": pagination changed");
        if (JasperExportManager.exportReportToPdf(print).length < 100) throw new AssertionError(label + ": empty PDF");
        System.out.println("PASS " + label);
    }
}
