package labvalidation;

import java.nio.file.*;
import net.sf.jasperreports.engine.JasperCompileManager;

/** Laboratory compiler; does not start the ERP or use its external services. */
public class CompileReports {
    public static void main(String[] args) throws Exception {
        Path input = Path.of(args[0]), output = Path.of(args[1]);
        Files.createDirectories(output);
        int count = 0;
        try (var files = Files.list(input)) {
            for (Path file : files.filter(f -> f.toString().endsWith(".jrxml")).sorted().toList()) {
                JasperCompileManager.compileReportToFile(file.toString(),
                        output.resolve(file.getFileName().toString().replace(".jrxml", ".jasper")).toString());
                count++;
            }
        }
        if (count != 215) throw new AssertionError("Unexpected report count: " + count);
        System.out.println("PASS fresh compilation of " + count + " reports with application classpath");
    }
}
