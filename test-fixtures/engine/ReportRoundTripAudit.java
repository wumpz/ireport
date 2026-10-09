import java.nio.file.*;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

/** Canonicalize the complete source and reconstructed design for independent comparison. */
public class ReportRoundTripAudit {
    public static void main(String[] args) throws Exception {
        Path root = Path.of(args[0]);
        try (var files = Files.list(root.resolve("reference"))) {
            for (Path before : files.filter(p -> p.toString().endsWith(".jrxml")).sorted().toList()) {
                String name = before.getFileName().toString();
                Path after = root.resolve("work").resolve(name);
                JasperReport binary = (JasperReport) JRLoader.loadObject(
                    root.resolve("work").resolve(name.replace(".jrxml", ".jasper")).toFile());
                Files.writeString(root.resolve("evidence").resolve(name + ".before.xml"),
                    JRXmlWriter.writeReport(JRXmlLoader.load(before.toFile()), "UTF-8"));
                Files.writeString(root.resolve("evidence").resolve(name + ".after.xml"),
                    JRXmlWriter.writeReport(JRXmlLoader.load(after.toFile()), "UTF-8"));
                Files.writeString(root.resolve("evidence").resolve(name + ".binary.xml"),
                    JRXmlWriter.writeReport(binary, "UTF-8"));
                System.out.println("AUDIT " + name + " compiler=" + binary.getCompilerClass());
            }
        }
    }
}
