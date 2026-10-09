import java.nio.file.*;
import java.util.*;
import net.sf.jasperreports.engine.*;

/** Offline failures must be explicit and leave the engine usable. */
public class FailureRecoverySmoke {
  interface Attempt { void run() throws Exception; }
  static void rejected(String label, Attempt attempt) throws Exception {
    try { attempt.run(); } catch (Exception expected) {
      System.out.println("PASS rejected " + label + ": " + expected.getClass().getSimpleName());
      return;
    }
    throw new AssertionError("Expected failure: " + label);
  }
  public static void main(String[] args) throws Exception {
    Path root = Path.of(args[0]);
    rejected("malformed XML", () -> JasperCompileManager.compileReport(root.resolve("invalid.jrxml").toString()));
    String start = "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"negative\" pageWidth=\"200\" pageHeight=\"200\" columnWidth=\"160\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">";
    String dependency = start + "<detail><band height=\"20\"><textField><reportElement x=\"0\" y=\"0\" width=\"160\" height=\"20\"/><textFieldExpression><![CDATA[missing.lab.Dependency.value()]]></textFieldExpression></textField></band></detail></jasperReport>";
    rejected("missing expression dependency", () -> JasperCompileManager.compileReport(new java.io.ByteArrayInputStream(dependency.getBytes(java.nio.charset.StandardCharsets.UTF_8))));
    String sub = start + "<detail><band height=\"20\"><subreport><reportElement x=\"0\" y=\"0\" width=\"160\" height=\"20\"/><dataSourceExpression><![CDATA[new net.sf.jasperreports.engine.JREmptyDataSource(1)]]></dataSourceExpression><subreportExpression><![CDATA[\"missing-lab-subreport.jasper\"]]></subreportExpression></subreport></band></detail></jasperReport>";
    JasperReport report = JasperCompileManager.compileReport(new java.io.ByteArrayInputStream(sub.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    rejected("missing subreport", () -> JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(1)));
    JasperReport valid = JasperCompileManager.compileReport(root.resolve("barcode-components.jrxml").toString());
    JasperPrint print = JasperFillManager.fillReport(valid, new HashMap<>(), new JREmptyDataSource(1));
    if (print.getPages().size() != 1) throw new AssertionError("Recovery failed");
    JasperExportManager.exportReportToPdfFile(print, root.resolve("recovery.pdf").toString());
    System.out.println("PASS valid compile/fill/PDF after three failures");
  }
}
