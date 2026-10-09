package com.jaspersoft.ireport.designer;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.*;
import org.junit.Test;
import org.junit.Assume;
import static org.junit.Assert.*;

/** Reads a lab snapshot only. Generated XML never replaces its input. */
public class ReportCorpusTest {
    @Test public void snapshotCanBeLoadedAndRoundTripped() throws Exception {
        String configured = System.getProperty("ireport.test.corpus");
        Assume.assumeTrue("Optional snapshot test requires ireport.test.corpus", configured != null);
        Path root = Path.of(configured).toAbsolutePath().normalize();
        Path output = Path.of("target", "corpus-results").toAbsolutePath().normalize();
        assertFalse("Input must be outside generated results", root.startsWith(output));
        Files.createDirectories(output);
        List<String> results = new ArrayList<>();
        int failed = 0;
        List<Path> files;
        try (var walk = Files.walk(root)) {
            files = walk.filter(p -> p.toString().toLowerCase(Locale.ROOT).endsWith(".jrxml")).sorted().toList();
        }
        assertFalse("Snapshot is empty", files.isEmpty());
        for (Path input : files) {
            try {
                JasperDesign original = JRXmlLoader.load(input.toFile());
                String first = JRXmlWriter.writeReport(original, "UTF-8");
                JasperDesign restored = JRXmlLoader.load(new ByteArrayInputStream(first.getBytes(StandardCharsets.UTF_8)));
                String second = JRXmlWriter.writeReport(restored, "UTF-8");
                assertEquals("Writer must be stable after reloading " + root.relativize(input), first, second);
                Path result = output.resolve(root.relativize(input));
                Files.createDirectories(result.getParent());
                Files.writeString(result, second);
                results.add("PASS\t" + root.relativize(input));
            } catch (Exception | AssertionError ex) {
                failed++;
                results.add("FAIL\t" + root.relativize(input) + "\t" + ex.getClass().getSimpleName() + ": " + ex.getMessage().replace('\n',' '));
            }
        }
        Files.write(output.resolve("results.tsv"), results);
        assertEquals("See target/corpus-results/results.tsv; checked " + files.size() + " reports", 0, failed);
    }
}
