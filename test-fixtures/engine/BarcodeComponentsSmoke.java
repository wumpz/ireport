import java.nio.file.*;
import java.util.*;
import net.sf.jasperreports.engine.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

/** Compile, export, render and decode synthetic report barcodes offline. */
public class BarcodeComponentsSmoke {
  public static void main(String[] args) throws Exception {
    String xml = """
      <jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:jr="http://jasperreports.sourceforge.net/jasperreports/components"
        xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd http://jasperreports.sourceforge.net/jasperreports/components http://jasperreports.sourceforge.net/xsd/components.xsd"
        name="synthetic_barcodes" language="groovy" pageWidth="400" pageHeight="400" columnWidth="380"
        leftMargin="10" rightMargin="10" topMargin="10" bottomMargin="10">
        <detail><band height="350">
          <componentElement><reportElement x="10" y="10" width="250" height="60"/>
            <jr:barbecue type="Code128" drawText="false" checksumRequired="false">
              <jr:codeExpression><![CDATA["LAB0001"]]></jr:codeExpression>
            </jr:barbecue>
          </componentElement>
          <componentElement><reportElement x="10" y="100" width="250" height="70"/>
            <jr:EAN13 textPosition="none"><jr:codeExpression><![CDATA["5901234123457"]]></jr:codeExpression></jr:EAN13>
          </componentElement>
          <componentElement><reportElement x="10" y="200" width="120" height="120"/>
            <jr:QRCode><jr:codeExpression><![CDATA["TESTE SEM VALIDADE"]]></jr:codeExpression></jr:QRCode>
          </componentElement>
        </band></detail>
      </jasperReport>
      """;
    Path output = Path.of(args[0]); Files.createDirectories(output);
    JasperReport report = args.length>1
        ? (JasperReport)net.sf.jasperreports.engine.util.JRLoader.loadObject(Path.of(args[1]).toFile())
        : JasperCompileManager.compileReport(new java.io.ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), new JREmptyDataSource(1));
    Path pdf = output.resolve("synthetic-barcodes.pdf");
    JasperExportManager.exportReportToPdfFile(print, pdf.toString());
    try (var document = org.apache.pdfbox.pdmodel.PDDocument.load(pdf.toFile())) {
      var renderer = new org.apache.pdfbox.rendering.PDFRenderer(document);
      // Avoid artificial seams between adjacent SVG modules during rasterization.
      renderer.setRenderingHints(new java.awt.RenderingHints(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF));
      var rendered = renderer.renderImageWithDPI(0, 288);
      javax.imageio.ImageIO.write(rendered, "png", output.resolve("synthetic-barcodes.png").toFile());
      int[][] bounds = {{10,10,280,80},{10,100,280,90},{10,200,140,140}};
      String[] expected = {"LAB0001", "5901234123457", "TESTE SEM VALIDADE"};
      for (int index=0; index<bounds.length; index++) {
        int[] b=bounds[index];
        var crop=rendered.getSubimage(b[0]*4,b[1]*4,b[2]*4,b[3]*4);
        var bitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(crop)));
        var decoded=new MultiFormatReader().decode(bitmap, Map.of(DecodeHintType.TRY_HARDER,true));
        if (!expected[index].equals(decoded.getText())) throw new AssertionError("Barcode changed: "+decoded);
        System.out.println("PASS decoded " + decoded.getBarcodeFormat() + " " + decoded.getText());
      }
    }
  }
}
