import java.nio.file.*;
import java.util.zip.*;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/** Loads every packaged report without executing queries or ERP startup. */
public class PackagedReportsAudit {
  public static void main(String[] args) throws Exception {
    int count = 0;
    Path compiled = Path.of(args[1]);
    try (ZipFile jar = new ZipFile(args[0])) {
      var entries = jar.entries();
      while (entries.hasMoreElements()) {
        var entry = entries.nextElement();
        if (entry.getName().startsWith("Reports/") && !entry.isDirectory()
            && !entry.getName().endsWith(".jasper") && !entry.getName().equals("Reports/LogoBradesco.png"))
          throw new AssertionError("Unexpected report resource: " + entry.getName());
        if (!entry.getName().startsWith("Reports/") || !entry.getName().endsWith(".jasper")) continue;
        byte[] bytes;
        try (var input = jar.getInputStream(entry)) { bytes = input.readAllBytes(); }
        Path expected = compiled.resolve(entry.getName().substring("Reports/".length()));
        if (!java.util.Arrays.equals(bytes, Files.readAllBytes(expected)))
          throw new AssertionError("Packaged report differs: " + entry.getName());
        try (var input = new java.io.ByteArrayInputStream(bytes)) {
          if (!(JRLoader.loadObject(input) instanceof JasperReport report) || report.getName().isBlank())
            throw new AssertionError("Invalid report: " + entry.getName());
        }
        count++;
      }
    }
    if (count != 215) throw new AssertionError("Expected 215 reports, found " + count);
    System.out.println("PASS all 215 packaged reports match source-build binaries and deserialize");
  }
}
