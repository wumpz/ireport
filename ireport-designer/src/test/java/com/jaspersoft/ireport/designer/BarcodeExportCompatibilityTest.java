package com.jaspersoft.ireport.designer;

import java.nio.file.*;
import java.util.*;
import java.awt.RenderingHints;
import net.sf.jasperreports.engine.*;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/** Exercise the designer's report context, SVG export and three barcode libraries. */
public class BarcodeExportCompatibilityTest {
    @Test public void exportedBarcodePdfCanBeDecoded() throws Exception {
        Path fixture=Path.of("..", "test-fixtures", "reports", "barcode-components.jrxml");
        Path output=Path.of("target", "compatibility-results", "designer-barcodes");
        Files.createDirectories(output);
        var context=new IRLocalJasperReportsContext();
        JasperReport report=JasperCompileManager.getInstance(context).compile(fixture.toString());
        assertEquals(net.sf.jasperreports.compilers.JRGroovyCompiler.class.getName(),report.getCompilerClass());
        JasperPrint print=JasperFillManager.getInstance(context).fill(report,new HashMap<>(),new JREmptyDataSource(1));
        Path pdf=output.resolve("barcodes.pdf");
        JasperExportManager.getInstance(context).exportToPdfFile(print,pdf.toString());
        try(PDDocument document=PDDocument.load(pdf.toFile())) {
            assertEquals(1,document.getNumberOfPages());
            PDFRenderer renderer=new PDFRenderer(document);
            renderer.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF));
            var rendered=renderer.renderImageWithDPI(0,288);
            javax.imageio.ImageIO.write(rendered,"png",output.resolve("barcodes.png").toFile());
            int[][] bounds={{10,10,280,80},{10,100,280,90},{10,200,140,140}};
            String[] expected={"LAB0001","5901234123457","TESTE SEM VALIDADE"};
            for(int index=0;index<bounds.length;index++) {
                int[] b=bounds[index];
                var image=rendered.getSubimage(b[0]*4,b[1]*4,b[2]*4,b[3]*4);
                var bitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                assertEquals(expected[index],new MultiFormatReader().decode(bitmap,Map.of(DecodeHintType.TRY_HARDER,true)).getText());
            }
        }
    }
}
