import java.nio.file.*;
import java.util.*;
import net.sf.jasperreports.engine.JasperCompileManager;

/** Compile every real source; report individual failures without stopping early. */
public class CompileCorpusAudit {
  public static void main(String[] args) throws Exception {
    Path input=Path.of(args[0]), output=Path.of(args[1]); Files.createDirectories(output);
    int passed=0; List<String> failures=new ArrayList<>();
    try(var files=Files.list(input)) {
      for(Path source:files.filter(p->p.toString().endsWith(".jrxml")).sorted().toList()) {
        try {
          JasperCompileManager.compileReportToFile(source.toString(),output.resolve(source.getFileName().toString().replace(".jrxml",".jasper")).toString());
          passed++; System.out.println("PASS COMPILE "+source.getFileName());
        } catch(Exception | LinkageError failure) {
          failures.add(source.getFileName()+"\t"+failure.getClass().getName()+"\t"+failure.getMessage());
          System.out.println("FAIL COMPILE "+source.getFileName()+" "+failure.getMessage());
        }
      }
    }
    Path evidence=args.length>2?Path.of(args[2]):Path.of("compilation-failures.tsv");
    Files.write(evidence,failures);
    System.out.println("CORPUS passed="+passed+" failed="+failures.size());
    if(!failures.isEmpty()) throw new AssertionError("See compilation-failures.tsv");
  }
}
